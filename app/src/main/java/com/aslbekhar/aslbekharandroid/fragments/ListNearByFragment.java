package com.aslbekhar.aslbekharandroid.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.aslbekhar.aslbekharandroid.R;
import com.aslbekhar.aslbekharandroid.adapters.StoreDiscountListAdapter;
import com.aslbekhar.aslbekharandroid.adapters.StoreListAdapter;
import com.aslbekhar.aslbekharandroid.models.AnalyticsAdvertisementModel;
import com.aslbekhar.aslbekharandroid.models.AnalyticsDataModel;
import com.aslbekhar.aslbekharandroid.models.CityModel;
import com.aslbekhar.aslbekharandroid.models.StoreModel;
import com.aslbekhar.aslbekharandroid.utilities.Constants;
import com.aslbekhar.aslbekharandroid.utilities.Interfaces;
import com.aslbekhar.aslbekharandroid.utilities.NetworkRequests;
import com.aslbekhar.aslbekharandroid.utilities.Snippets;
import com.aslbekhar.aslbekharandroid.utilities.StaticData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.rey.material.widget.ProgressView;
import com.rey.material.widget.Slider;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.aslbekhar.aslbekharandroid.utilities.Constants.ADDRESS;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.ADVERTISEMENT_MAX_COUNT;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.ADVERTISEMENT_TIMEOUT;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.ADVERTISEMENT_VIEW_TIMEOUT;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.ADVERTISE_DEALS;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.BANNER;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.BRAND_ID;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.BRAND_NAME;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CAT_NAME;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CAT_NUMBER;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CITY_CODE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CITY_TO_CAT_FULL_AD;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.DEAL;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.DEALS_BANNER_AD;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.DEALS_BRAND;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.DEALS_BRAND_STORE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.DEFAULT_DISTANCE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.DISCOUNT;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.DISTANCE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.FALSE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.FULL;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LAST_CITY_CODE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LAST_LAT;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LAST_LONG;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LATITUDE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LOGO;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LONGITUDE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LIST_OR_SINGLE_STORE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.SINGLE_STORE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.NORMAL;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.NORMAL_OR_DEAL;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.STORE_DETAILS;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.TELL;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.TITLE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.VERIFIED;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.WORK_HOUR;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.getSP;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.setSP;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.showFade;

/**
 * Created by Amin on 14/05/2016.
 * <p/>
 * This class will be used for
 */
public class ListNearByFragment extends android.support.v4.app.Fragment
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, Interfaces.NetworkListeners {

    View view;
    Interfaces.MainActivityInterface callBack;
    Interfaces.OfflineInterface offlineCallBack;
    RecyclerView recyclerView;
    StoreDiscountListAdapter adapterDeals;
    StoreListAdapter adapterNormal;
    List<StoreModel> modelList = new ArrayList<>();
    LinearLayoutManager layoutManager;
    String cityCode;
    int distance = DEFAULT_DISTANCE;
    private ProgressView progressBar;
    private View nodata;
    boolean fullScreenAdvertiseTimer = false;
    boolean fullScreenAdvertiseSecondTimer = false;
    ImageView fullScreenAdImageView;
    boolean normalOrDeal = false;
    boolean isDownloading = false;
    boolean googlePlayServices = false;
    View listOverLay;

    // for location
    private Location lastLocation;

    // Google client to interact with Google API
    private GoogleApiClient googleApiClient;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    private static final int PERMISSION_REQUEST = 0;

    private LocationRequest mLocationRequest;
    private boolean locationAccess = false;

    public ListNearByFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_deals_nearby, container, false);

        cityCode = getSP(LAST_CITY_CODE);
        if (cityCode.equals(FALSE)) {
            cityCode = "021";
        }

        if (getArguments() != null) {
            if (getArguments().getString(NORMAL_OR_DEAL, FALSE).equals(NORMAL)) {
                normalOrDeal = true;
            }
            distance = getArguments().getInt(DISTANCE, DEFAULT_DISTANCE);
        }

        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView = (RecyclerView) view.findViewById(R.id.listView);
        listOverLay = view.findViewById(R.id.listOverLay);
        progressBar = (ProgressView) view.findViewById(R.id.progressBar);
        fullScreenAdImageView = (ImageView) view.findViewById(R.id.fullScreenAdvertise);
        nodata = view.findViewById(R.id.nodata);

        // setting the layout manager of recyclerView
        recyclerView.setLayoutManager(layoutManager);

        if (normalOrDeal) {
            adapterNormal = new StoreListAdapter(modelList, getActivity(), null, this, cityCode);
            recyclerView.setAdapter(adapterNormal);
        } else {
            adapterDeals = new StoreDiscountListAdapter(modelList, getActivity(), this, cityCode);
            recyclerView.setAdapter(adapterDeals);
        }

        createLocationRequest();
        // Building the GoogleApi client
        buildGoogleApiClient();


        view.findViewById(R.id.sliderMapListToggle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();

                bundle.putString(CITY_CODE, cityCode);
                if (normalOrDeal) {
                    bundle.putString(NORMAL_OR_DEAL, NORMAL);
                } else {
                    bundle.putString(NORMAL_OR_DEAL, DEAL);
                }
                MapNearByFragment fragment = new MapNearByFragment();
                fragment.setArguments(bundle);
                callBack.openNewContentFragment(fragment);
            }
        });


        final Slider slider = (Slider) view.findViewById(R.id.slider);
        slider.setValue(distance, false);
        slider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (distance != (int) slider.getExactValue()) {
                    distance = (int) slider.getExactValue();
                    getDealsNearBy();
                }
            }
        });

        checkForBannerAdvertise();

        return view;
    }

    private void checkForAdvertisement(final StoreModel model) {

        if (!Snippets.isOnline(getActivity())){
            openStoreFragment(model);
            return;
        }


        if (StaticData.addShownCount > ADVERTISEMENT_MAX_COUNT) {
            openStoreFragment(model);
            return;
        }

        if (StaticData.addShownCount > ADVERTISEMENT_MAX_COUNT) {
            StaticData.addShownCount++;
            return;
        }

        fullScreenAdvertiseTimer = true;
        Snippets.showFade(listOverLay, true, 500);
        progressBar.start();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (fullScreenAdvertiseTimer) {
                    fullScreenAdvertiseTimer = false;
                    openStoreFragment(model);
                }
            }
        }, ADVERTISEMENT_TIMEOUT);
        Picasso.with(getContext())
                .load(CITY_TO_CAT_FULL_AD + ".deal." + cityCode + ".png")
                .into(fullScreenAdImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        if (fullScreenAdvertiseTimer) {
                            AnalyticsAdvertisementModel.sendAdvertisementAnalytics(
                                    new AnalyticsAdvertisementModel(CITY_TO_CAT_FULL_AD + ".deal." + cityCode + ".png", ADVERTISE_DEALS, FULL));
                            fullScreenAdvertiseTimer = false;
                            fullScreenAdImageView.setVisibility(View.VISIBLE);
                            progressBar.stop();
                            listOverLay.setVisibility(View.GONE);
                            fullScreenAdImageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    fullScreenAdvertiseSecondTimer = false;
                                    openStoreFragment(model);
                                }
                            });
                            fullScreenAdvertiseSecondTimer = true;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (fullScreenAdvertiseSecondTimer) {
                                        fullScreenAdvertiseSecondTimer = false;
                                        openStoreFragment(model);
                                    }
                                }
                            }, ADVERTISEMENT_VIEW_TIMEOUT);

                        }
                    }

                    @Override
                    public void onError() {

                        if (fullScreenAdvertiseTimer) {
                            fullScreenAdvertiseTimer = false;
                            openStoreFragment(model);
                        }

                    }
                });
    }

    public void openStoreFromAdapter(StoreModel model) {
        AnalyticsDataModel.saveAnalytic(DEALS_BRAND_STORE,
                model.getbId() + "_" +
                        model.getsId());
        AnalyticsDataModel.saveAnalytic(DEALS_BRAND,
                model.getbId());
        checkForAdvertisement(model);
    }


    private void checkForBannerAdvertise() {
        String url = DEALS_BANNER_AD + cityCode + ".png";
        Picasso.with(getContext()).load(url).into((ImageView) view.findViewById(R.id.bannerAdvertise), new Callback() {
            @Override
            public void onSuccess() {
                AnalyticsAdvertisementModel.sendAdvertisementAnalytics(
                        new AnalyticsAdvertisementModel("ad." + cityCode + ".png", ADVERTISE_DEALS, BANNER));
                view.findViewById(R.id.bannerAdvertise).setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
            }
        });
    }


    private void openStoreFragment(StoreModel model) {

        Bundle bundle = new Bundle();

        bundle.putString(CITY_CODE, cityCode);
        bundle.putString(CAT_NAME, model.getbCategory());
        bundle.putString(CAT_NUMBER, model.getbCategoryId());
        bundle.putString(BRAND_ID, model.getbId());
        bundle.putString(BRAND_NAME, model.getbName());
        bundle.putString(STORE_DETAILS, JSON.toJSONString(model));

        StoreFragment fragment = new StoreFragment();
        fragment.setArguments(bundle);
        callBack.openNewContentFragment(fragment);

    }


    public void showOnMap(StoreModel model) {

        Bundle bundle = new Bundle();
        bundle.putString(CITY_CODE, cityCode);
        bundle.putString(LATITUDE, model.getsLat());
        bundle.putString(LONGITUDE, model.getsLong());
        bundle.putString(TITLE, model.getsName());
        bundle.putString(WORK_HOUR, model.getsHour());
        bundle.putString(ADDRESS, model.getsAddress());
        bundle.putString(TELL, model.getsTel1());
        bundle.putInt(DISCOUNT, model.getdPrecentageInt());
        bundle.putString(VERIFIED, model.getsVerified());
        bundle.putString(LOGO, model.getbName());
        bundle.putInt(LIST_OR_SINGLE_STORE, SINGLE_STORE);

        MapNearByFragment fragment = new MapNearByFragment();
        fragment.setArguments(bundle);

        callBack.openNewContentFragment(fragment);
    }

    private void getDealsNearBy() {

        if (recyclerView.getVisibility() == View.VISIBLE && listOverLay.getVisibility() != View.VISIBLE) {
            listOverLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            showFade(listOverLay, true, 500);
        }
        if (lastLocation != null) {
            progressBar.start();
            isDownloading = true;
            if (normalOrDeal) {
                NetworkRequests.getRequest(Constants.STORESLIST_NEARBY + lastLocation.getLatitude() + "/"
                        + lastLocation.getLongitude() + "/" + distance, this, Constants.DOWNLOAD);
            } else {
                NetworkRequests.getRequest(Constants.DEALS_NEARBY_URL + lastLocation.getLatitude() + "/"
                        + lastLocation.getLongitude() + "/" + distance, this, Constants.DOWNLOAD);
            }
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            callBack = (Interfaces.MainActivityInterface) getActivity();
            offlineCallBack = (Interfaces.OfflineInterface) getActivity();
        } catch (ClassCastException e) {
            throw new ClassCastException(getActivity().toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    /**
     * Creating google api client object
     */
    protected synchronized void buildGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    /**
     * Starting the location updates
     */
    protected void startLocationUpdates(boolean googlePlayOrNot) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (
                ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            locationAccess = false;
            lastLocation = new Location("");
            lastLocation.setLatitude(Double.parseDouble(CityModel.findCityById(cityCode).getLat()));
            lastLocation.setLongitude(Double.parseDouble(CityModel.findCityById(cityCode).getLon()));
            setSP(LAST_LAT, String.valueOf(lastLocation.getLatitude()));
            setSP(LAST_LONG, String.valueOf(lastLocation.getLongitude()));
            getDealsNearBy();
        } else {
            locationAccess = true;
            if (googlePlayOrNot) {
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        googleApiClient, mLocationRequest, this);
                lastLocation = LocationServices
                        .FusedLocationApi
                        .getLastLocation(googleApiClient);
                setSP(LAST_LAT, String.valueOf(lastLocation.getLatitude()));
                setSP(LAST_LONG, String.valueOf(lastLocation.getLongitude()));
                getDealsNearBy();

            } else {
                android.location.LocationManager locationManager = (android.location.LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
                // Define a listener that responds to location updates
                android.location.LocationListener locationListener = new android.location.LocationListener() {
                    public void onLocationChanged(Location location) {
                        lastLocation = location;
                        getDealsNearBy();
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {}

                    public void onProviderEnabled(String provider) {}

                    public void onProviderDisabled(String provider) {}
                };

                // Register the listener with the Location Manager to receive location updates
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 120000, 300, locationListener);
            }
        }

    }


    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST) {
            if (grantResults.length >= 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates(googlePlayServices);
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (googleApiClient != null) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!isDownloading) {
            listOverLay.setVisibility(View.GONE);
        }
    }

    /*
    these methods are for getting the current location
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        googlePlayServices = true;
        startLocationUpdates(googlePlayServices);
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        googlePlayServices = false;
        startLocationUpdates(googlePlayServices);

    }

    @Override
    public void onLocationChanged(Location location) {
        googleApiClient.disconnect();
        if (lastLocation != null) {
            if (lastLocation.distanceTo(location) > 500) {
                lastLocation = location;
                setSP(LAST_LAT, String.valueOf(lastLocation.getLatitude()));
                setSP(LAST_LONG, String.valueOf(lastLocation.getLongitude()));
                getDealsNearBy();
            }
        } else {
            lastLocation = location;
            getDealsNearBy();
        }
    }

    @Override
    public void onResponse(String response, String tag) {
        if (isAdded() && getActivity() != null) {
            view.findViewById(R.id.offlineLay).setVisibility(View.GONE);
            offlineCallBack.offlineMode(false);
            modelList.clear();
            try {
                modelList.addAll(JSON.parseArray(response, StoreModel.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (normalOrDeal) {
                adapterNormal.notifyDataSetChanged();
            } else {
                adapterDeals.notifyDataSetChanged();
            }
            if (listOverLay.getVisibility() == View.VISIBLE) {
                showFade(listOverLay, false, 500);
            }
            progressBar.stop();
            isDownloading = false;
            if (modelList.size() == 0) {
                showFade(nodata, true, 500);
            } else {
                if (nodata.getVisibility() == View.VISIBLE) {
                    showFade(nodata, false, 300);
                }
            }
        }
    }

    @Override
    public void onError(VolleyError error, String tag) {
        view.findViewById(R.id.offlineLay).setVisibility(View.GONE);
        offlineCallBack.offlineMode(false);
        listOverLay.setVisibility(View.GONE);
        progressBar.stop();
        Snackbar.make(view.findViewById(R.id.root), R.string.connection_error, Snackbar.LENGTH_INDEFINITE).show();
        isDownloading = false;
    }

    @Override
    public void onOffline(String tag) {
        progressBar.stop();
        isDownloading = false;
        view.findViewById(R.id.offlineLay).setVisibility(View.VISIBLE);
        offlineCallBack.offlineMode(true);
    }
}

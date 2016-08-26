package com.aslbekhar.aslbekharandroid.fragments;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSeekBar;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.aslbekhar.aslbekharandroid.R;
import com.aslbekhar.aslbekharandroid.models.AnalyticsAdvertisementModel;
import com.aslbekhar.aslbekharandroid.models.BrandModel;
import com.aslbekhar.aslbekharandroid.models.CityModel;
import com.aslbekhar.aslbekharandroid.models.StoreModel;
import com.aslbekhar.aslbekharandroid.utilities.Constants;
import com.aslbekhar.aslbekharandroid.utilities.Interfaces;
import com.aslbekhar.aslbekharandroid.utilities.NetworkRequests;
import com.aslbekhar.aslbekharandroid.utilities.StaticData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.rey.material.widget.Slider;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.aslbekhar.aslbekharandroid.utilities.Constants.ADDRESS;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.ADVERTISEMENT_MAX_COUNT;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.ADVERTISEMENT_TIMEOUT;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.ADVERTISEMENT_VIEW_TIMEOUT;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.ADVERTISE_NEARME;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.BANNER;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.BRAND_ID;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.BRAND_NAME;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CAT_NAME;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CAT_NUMBER;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CITY_CODE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CITY_TO_CAT_FULL_AD;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.DEAL;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.DEALS_NEARBY_URL;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.DEFAULT_DISTANCE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.DISCOUNT;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.DISTANCE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.DOWNLOAD;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.FALSE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.FULL;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.GPS_ON_OR_OFF;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LAST_CITY_CODE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LAST_LAT;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LAST_LONG;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LATITUDE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LIST_OF_STORES;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LIST_OR_SINGLE_STORE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LOGO;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LONGITUDE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.NEARME_BANNER_AD;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.NORMAL;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.NORMAL_OR_DEAL;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.OFFLINE_MODE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.SINGLE_STORE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.STORESLIST_NEARBY;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.STORE_DETAILS;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.TELL;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.TITLE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.VERIFIED;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.WORK_HOUR;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.getSP;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.getSPboolean;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.setSP;
import static com.aslbekhar.aslbekharandroid.utilities.StaticData.addShownCount;

/**
 * Created by Amin on 14/05/2016.
 * <p/>
 * This class will be used for
 */
public class MapNearByFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener,
        OnMapReadyCallback,
        LocationListener,
        Interfaces.NetworkListeners {


    int type = 1;
    Interfaces.MainActivityInterface callBack;
    Interfaces.OfflineInterface offlineCallBack;
    View view;
    GoogleApiClient googleApiClient;
    GoogleMap mMap;
    MapView mMapView;
    String cityCode;
    List<Marker> markerList = new ArrayList<>();
    List<MarkerOptions> markerOptionsList = new ArrayList<>();
    List<StoreModel> storeModelList = new ArrayList<>();
    private View nodata;
    boolean fullScreenAdvertiseTimer = false;
    boolean fullScreenAdvertiseSecondTimer = false;
    ImageView fullScreenAdImageView;
    int distance = DEFAULT_DISTANCE;
    private Location lastLocation;
    boolean locationAccess = false;
    boolean gpsAvailableOrNot = false;
    boolean normalOrDeal = true;
    Location tempLocation = new Location("reverseGeocoded");
    boolean cameraAnimatedBefore = false;
    CameraPosition cameraPosition;
    StoreModel model;
    private static final int GPS_SETTING_REQUEST_CODE = 9002;

    private static final int PERMISSION_REQUEST = 0;
    private Interfaces.showProgressBar progressbar;
    private boolean waitingForRefreshMap = false;


    public MapNearByFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(LIST_OR_SINGLE_STORE, 1);
            if (getArguments().getString(NORMAL_OR_DEAL, FALSE).equals(DEAL)) {
                normalOrDeal = false;
            }
            distance = getArguments().getInt(DISTANCE, DEFAULT_DISTANCE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_maps, container, false);


        if (getArguments() != null && getArguments().getBoolean(OFFLINE_MODE, false)) {
            view.findViewById(R.id.offlineLay).setVisibility(View.VISIBLE);
        }

        final AppCompatSeekBar seekBar = (AppCompatSeekBar) view.findViewById(R.id.slider);
        seekBar.setProgress(distance);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (distance != seekBar.getProgress()) {
                    distance = seekBar.getProgress();
                    getStoresNearBy(lastLocation);
                }
            }
        });

        cityCode = getSP(LAST_CITY_CODE);
        if (cityCode.equals(FALSE)) {
            cityCode = "021";
        }
        gpsAvailableOrNot = getSPboolean(GPS_ON_OR_OFF);


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
                ListNearByFragment fragment = new ListNearByFragment();
                fragment.setArguments(bundle);
                callBack.openNewContentFragment(fragment);
            }
        });

        fullScreenAdImageView = (ImageView) view.findViewById(R.id.fullScreenAdvertise);

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMap = mMapView.getMap();

        checkForBannerAdvertise();

        return view;
    }


    private void checkForAdvertisement(final StoreModel model) {

        if (StaticData.addShownCount < ADVERTISEMENT_MAX_COUNT) {
            StaticData.addShownCount++;
        }

        if (addShownCount > ADVERTISEMENT_MAX_COUNT) {
            openStoreFragment(model);
            return;
        }

        fullScreenAdvertiseTimer = true;

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
                .load(CITY_TO_CAT_FULL_AD + ".nearme." + model.getbName() + "." + model.getsId() + ".png")
                .into(fullScreenAdImageView, new Callback() {
                    @Override
                    public void onSuccess() {
                        if (fullScreenAdvertiseTimer) {
                            AnalyticsAdvertisementModel.sendAdvertisementAnalytics(
                                    new AnalyticsAdvertisementModel(CITY_TO_CAT_FULL_AD + ".nearme." + model.getbName() + "." + model.getsId() + ".png", ADVERTISE_NEARME, FULL));
                            fullScreenAdvertiseTimer = false;
                            fullScreenAdImageView.setVisibility(View.VISIBLE);
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


    private void checkForBannerAdvertise() {
        String url = NEARME_BANNER_AD + cityCode + ".png";
        Picasso.with(getContext()).load(url).into((ImageView) view.findViewById(R.id.bannerAdvertise), new Callback() {
            @Override
            public void onSuccess() {
                AnalyticsAdvertisementModel.sendAdvertisementAnalytics(
                        new AnalyticsAdvertisementModel("ad." + cityCode + ".png", ADVERTISE_NEARME, BANNER));
                view.findViewById(R.id.bannerAdvertise).setVisibility(View.VISIBLE);
            }

            @Override
            public void onError() {
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        if (gpsAvailableOrNot) {
            view.findViewById(R.id.GPSOffLay).setVisibility(View.GONE);
        } else {
            view.findViewById(R.id.GPSOffLay).setVisibility(View.VISIBLE);
            view.findViewById(R.id.GPSOffLay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    turnOnGPS();
                }
            });
        }
        googleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    private void turnOnGPS() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, GPS_SETTING_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GPS_SETTING_REQUEST_CODE && resultCode == 0) {
            if (isGpsEnabled(getActivity())) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getString(R.string.gps_turned_on_succesfully))
                        .setIcon(getResources().getDrawable(R.drawable.icon))
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                view.findViewById(R.id.GPSOffLay).setVisibility(View.GONE);
                            }
                        })
                        .show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(getString(R.string.gps_didnt_turn_on))
                        .setPositiveButton(getString(R.string.ok_i_try_again), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                turnOnGPS();
                            }
                        })
                        .setCancelable(false)
                        .setNegativeButton(getString(R.string.no_idont_want), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
            }
        }
    }


    public static boolean isGpsEnabled(Context context) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            String providers = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            if (TextUtils.isEmpty(providers)) {
                return false;
            }
            return providers.contains(LocationManager.GPS_PROVIDER);
        } else {
            final int locationMode;
            try {
                locationMode = Settings.Secure.getInt(context.getContentResolver(),
                        Settings.Secure.LOCATION_MODE);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            switch (locationMode) {

                case Settings.Secure.LOCATION_MODE_HIGH_ACCURACY:
                case Settings.Secure.LOCATION_MODE_SENSORS_ONLY:
                case Settings.Secure.LOCATION_MODE_BATTERY_SAVING:
                    return true;
                case Settings.Secure.LOCATION_MODE_OFF:
                default:
                    return false;
            }
        }
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        this.mMap.setOnMapLongClickListener(this);
        this.mMap.setOnMapClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (googleApiClient != null && googleApiClient.isConnected()) {
            googleApiClient.disconnect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        if (mMap != null && storeModelList != null && storeModelList.size() > 0 && type == LIST_OF_STORES) {
            showStoresOnMap(null, false);
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
        cameraPosition = mMap.getCameraPosition();
    }

    @Override
    public void onConnected(Bundle bundle) {
        onLocationConnected();
    }

    private void onLocationConnected() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (
                ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            locationAccess = false;

        } else {
            locationAccess = true;
            lastLocation = LocationServices
                    .FusedLocationApi
                    .getLastLocation(googleApiClient);

        }
        if (lastLocation == null) {
            lastLocation = new Location("");
            if (getSP(LAST_LAT).equals(FALSE) || getSP(LAST_LONG).equals(FALSE)) {
                lastLocation.setLatitude(Double.parseDouble(CityModel.findCityById(cityCode).getLat()));
                lastLocation.setLongitude(Double.parseDouble(CityModel.findCityById(cityCode).getLon()));
            } else {
                lastLocation.setLatitude(Double.parseDouble(getSP(LAST_LAT)));
                lastLocation.setLongitude(Double.parseDouble(getSP(LAST_LONG)));
            }
        }
        setSP(LAST_LAT, String.valueOf(lastLocation.getLatitude()));
        setSP(LAST_LONG, String.valueOf(lastLocation.getLongitude()));

        if (type == Constants.SINGLE_STORE) {
            showSingleStore();
        } else {
            initCamera(lastLocation);
            getStoresNearBy(lastLocation);
        }
    }

    private void showSingleStore() {
        Bundle bundle = getArguments();
        model = new StoreModel(bundle.getString(TITLE),
                bundle.getString(LOGO),
                bundle.getString(ADDRESS),
                bundle.getString(WORK_HOUR),
                bundle.getString(TELL),
                bundle.getString(LATITUDE),
                bundle.getString(LONGITUDE),
                bundle.getInt(DISCOUNT),
                bundle.getString(VERIFIED)
        );
        storeModelList.add(model);
        LatLng position = new LatLng(Double.parseDouble(model.getsLat()), Double.parseDouble(model.getsLong()));
        markerList.add(mMap.addMarker(new MarkerOptions()
                .position(position).title(model.getsName())
                .snippet(model.get_id())
                .icon(BitmapDescriptorFactory.fromBitmap(createBitMpaForMarker(model)))));
        Location temp = new Location(LocationManager.GPS_PROVIDER);
        temp.setLatitude(position.latitude);
        temp.setLongitude(position.longitude);
        initCamera(new Location(temp));
    }

    @Override
    public void onLocationChanged(Location location) {
        if (lastLocation != null) {
            if (lastLocation.distanceTo(location) > 1000) {
                if (type == Constants.LIST_OF_STORES) {
                    lastLocation = location;
                    initCamera(lastLocation);
                    setSP(LAST_LAT, String.valueOf(lastLocation.getLatitude()));
                    setSP(LAST_LONG, String.valueOf(lastLocation.getLongitude()));
                    getStoresNearBy(lastLocation);
                }
            }
        } else {
            if (type == Constants.LIST_OF_STORES) {
                lastLocation = location;
                initCamera(lastLocation);
                getStoresNearBy(lastLocation);
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
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onLocationConnected();
            }
        }
    }

    private void initCamera(Location location) {
        if (!cameraAnimatedBefore) {
            cameraPosition = CameraPosition.builder()
                    .target(new LatLng(location.getLatitude(),
                            location.getLongitude()))
                    .zoom(13f)
                    .bearing(0.0f)
                    .tilt(0.0f)
                    .build();

            mMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition), null);

            cameraAnimatedBefore = true;
        } else {
            mMap.moveCamera(CameraUpdateFactory
                    .newCameraPosition(cameraPosition));
        }

        if (type != Constants.SINGLE_STORE) {
            mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                private CameraPosition prevCameraPosition;

                @Override
                public void onCameraChange(CameraPosition cameraPosition) {

                    if (prevCameraPosition != null) {
                        tempLocation.setLatitude(cameraPosition.target.latitude);
                        tempLocation.setLongitude(cameraPosition.target.longitude);
                        if (lastLocation.distanceTo(tempLocation) > 3000) {
                            if (!waitingForRefreshMap) {
                                waitingForRefreshMap = true;
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getStoresNearBy(tempLocation);
                                        waitingForRefreshMap = false;
                                    }
                                }, 2000);
                            }
                        }
                    }
                    prevCameraPosition = cameraPosition;
                }
            });
        }

        mMap.setTrafficEnabled(true);
        if (locationAccess) {
            mMap.setMyLocationEnabled(true);
        }
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    private void getPermission() {
        // TODO: Consider calling
        //    ActivityCompat#requestPermissions
        // here to request the missing permissions, and then overriding
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
        //                                          int[] grantResults)
        // to handle the case where the user grants the permission. See the documentation
        // for ActivityCompat#requestPermissions for more details.
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        callBack = (Interfaces.MainActivityInterface) getActivity();
        offlineCallBack = (Interfaces.OfflineInterface) getActivity();
        progressbar = (Interfaces.showProgressBar) getActivity();

    }


    private void getStoresNearBy(Location location) {
        progressbar.showProgressBar(true);
        if (type == LIST_OF_STORES) {
            if (normalOrDeal) {
                NetworkRequests.getRequest(STORESLIST_NEARBY + location.getLatitude() + "/"
                        + location.getLongitude() + "/" + distance, this, DOWNLOAD);
            } else {
                NetworkRequests.getRequest(DEALS_NEARBY_URL + location.getLatitude() + "/"
                        + location.getLongitude() + "/" + distance, this, DOWNLOAD);
            }
        }
    }


    private void openStoreFragment(StoreModel model) {

        Bundle bundle = new Bundle();

        bundle.putString(CITY_CODE, getSP(LAST_CITY_CODE));
        bundle.putString(CAT_NAME, model.getbCategory());
        bundle.putString(CAT_NUMBER, model.getbCategoryId());
        bundle.putString(BRAND_ID, model.getbId());
        bundle.putString(BRAND_NAME, model.getbName());
        bundle.putString(STORE_DETAILS, JSON.toJSONString(model));
        bundle.putInt(LIST_OR_SINGLE_STORE, SINGLE_STORE);

        StoreListFragment fragment = new StoreListFragment();
        fragment.setArguments(bundle);
        callBack.openNewContentFragment(fragment);

    }


    @Override
    public void onResponse(String response, String tag) {
        progressbar.showProgressBar(false);

        if (isAdded() && getActivity() != null) {
            view.findViewById(R.id.offlineLay).setVisibility(View.GONE);
            offlineCallBack.offlineMode(false);
            List<StoreModel> downloadedStoreModels = new ArrayList<>();
            try {
                downloadedStoreModels.addAll(JSON.parseArray(response, StoreModel.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
            showStoresOnMap(downloadedStoreModels, true);
        }
    }

    private void showStoresOnMap(List<StoreModel> downloadedStoreModels, boolean isDownloaded) {
        if (isDownloaded) {
            for (int i = 0; i < downloadedStoreModels.size(); i++) {
                StoreModel store = downloadedStoreModels.get(i);
                boolean found = false;
                for (Marker marker : markerList) {
                    if (marker.getPosition().latitude == Double.parseDouble(store.getsLat())
                            && marker.getPosition().longitude == Double.parseDouble(store.getsLong())
                            && marker.getTitle().equals(store.getsName())) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    storeModelList.add(store);
                    LatLng position = new LatLng(Double.parseDouble(store.getsLat()), Double.parseDouble(store.getsLong()));
                    Bitmap bitmap = createBitMpaForMarker(store);
                    MarkerOptions markerOptions = new MarkerOptions().position(position).title(store.getsName())
                            .snippet(store.get_id()).icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                    markerOptionsList.add(markerOptions);
                    markerList.add(mMap.addMarker(markerOptions));
                }
                if (storeModelList.size() > 80) {
                    for (int j = 0; j < (storeModelList.size() - 80); j++) {
                        storeModelList.remove(0);
                        markerList.get(0).remove();
                        markerList.remove(0);
                    }
                }
            }
        } else {
            for (int i = 0; i < storeModelList.size(); i++) {
                StoreModel store = storeModelList.get(i);
                boolean found = false;
                for (Marker marker : markerList) {
                    if (marker.getPosition().latitude == Double.parseDouble(store.getsLat())
                            && marker.getPosition().longitude == Double.parseDouble(store.getsLong())
                            && marker.getTitle().equals(store.getsName())) {
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    LatLng position = new LatLng(Double.parseDouble(store.getsLat()), Double.parseDouble(store.getsLong()));
                    Bitmap bitmap = createBitMpaForMarker(store);
                    MarkerOptions markerOptions = new MarkerOptions().position(position).title(store.getsName())
                            .snippet(store.get_id()).icon(BitmapDescriptorFactory.fromBitmap(bitmap));
                    markerOptionsList.add(markerOptions);
                    markerList.add(mMap.addMarker(markerOptions));
                } else {
                    mMap.addMarker(markerOptionsList.get(i));
                }
            }
        }
    }

    private Bitmap createBitMpaForMarker(StoreModel store) {

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;

        float ratio = size.x / 1440f;

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap((int) (200 * ratio), (int) (200 * ratio), conf);
        Canvas canvas = new Canvas(bmp);

        // paint defines the text color, stroke width and size
        Paint color = new Paint();
        color.setColor(Color.BLACK);

        RectF targetRect = new RectF(0, 0, (int) (200 * ratio), (int) (200 * ratio));

        Bitmap logo = null;
        canvas.drawBitmap(BitmapFactory.decodeResource(getResources(),
                R.drawable.map_pin_store_base), null, targetRect, color);
        targetRect = new RectF((int) (28 * ratio), (int) (15 * ratio), (int) (174 * ratio), (int) (162 * ratio));
        try {
            InputStream bitmap = getActivity().getAssets().open("logos/" + BrandModel.getBrandLogo(store.getbName()) + ".png");
            logo = BitmapFactory.decodeStream(bitmap);
            canvas.drawBitmap(logo, null, targetRect, color);
        } catch (Exception e1) {
            return BitmapFactory.decodeResource(getActivity().getResources(),
                    R.drawable.map_pin_store);
        }

        return bmp;
    }


    @Override
    public void onError(VolleyError error, String tag) {

        progressbar.showProgressBar(false);
        view.findViewById(R.id.offlineLay).setVisibility(View.GONE);
        offlineCallBack.offlineMode(false);
        Snackbar.make(view.findViewById(R.id.root), R.string.connection_error, Snackbar.LENGTH_INDEFINITE).show();
    }

    @Override
    public void onOffline(String tag) {
        view.findViewById(R.id.offlineLay).setVisibility(View.VISIBLE);
        offlineCallBack.offlineMode(true);
    }

}
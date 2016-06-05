package com.aslbekhar.aslbekharandroid.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.aslbekhar.aslbekharandroid.R;
import com.aslbekhar.aslbekharandroid.adapters.StoreDiscountListAdapter;
import com.aslbekhar.aslbekharandroid.models.StoreDiscountModel;
import com.aslbekhar.aslbekharandroid.models.StoreModel;
import com.aslbekhar.aslbekharandroid.utilities.Constants;
import com.aslbekhar.aslbekharandroid.utilities.Interfaces;
import com.aslbekhar.aslbekharandroid.utilities.NetworkRequests;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.rey.material.widget.ProgressView;
import com.rey.material.widget.Slider;

import java.util.ArrayList;
import java.util.List;

import static com.aslbekhar.aslbekharandroid.utilities.Constants.ADDRESS;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CITY_CODE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.DISCOUNT;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LATITUDE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LOGO;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LONGITUDE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.MAP_TYPE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.MAP_TYPE_SHOW_SINGLE_STORE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.TELL;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.TITLE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.VERIFIED;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.WORK_HOUR;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.showFade;

/**
 * Created by Amin on 14/05/2016.
 * <p/>
 * This class will be used for
 */
public class DealNearByFragment extends android.support.v4.app.Fragment
        implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, Interfaces.NetworkListeners {

    View view;
    Interfaces.MainActivityInterface callBack;
    Interfaces.OfflineInterface offlineCallBack;
    RecyclerView recyclerView;
    StoreDiscountListAdapter adapter;
    List<StoreDiscountModel> modelList = new ArrayList<>();
    LinearLayoutManager layoutManager;
    String cityCode;
    int distance = 2;
    private View listOverLay;
    private ProgressView progressBar;
    private View nodata;

    // for location

    private Location mLastLocation;
    private Location currentLocation;

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

    public DealNearByFragment() {

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

        layoutManager = new LinearLayoutManager(getActivity());

        recyclerView = (RecyclerView) view.findViewById(R.id.listView);
        listOverLay = view.findViewById(R.id.listOverLay);
        progressBar = (ProgressView) view.findViewById(R.id.progressBar);
        nodata = view.findViewById(R.id.nodata);

        // setting the layout manager of recyclerView
        recyclerView.setLayoutManager(layoutManager);

        adapter = new StoreDiscountListAdapter(modelList, getActivity(), this, cityCode);
        recyclerView.setAdapter(adapter);

        // First we need to check availability of play services
        if (checkPlayServices()) {

            createLocationRequest();
            // Building the GoogleApi client
            buildGoogleApiClient();
        }


        final Slider slider = (Slider) view.findViewById(R.id.slider);

        slider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (distance != (int) slider.getExactValue()) {
                    distance = (int) slider.getExactValue();
                    getDealsNearBy();
                }
            }
        });


        return view;
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
        bundle.putInt(DISCOUNT, model.getsDiscount());
        bundle.putString(VERIFIED, model.getsVerified());
        bundle.putString(LOGO, model.getbName());
        bundle.putInt(MAP_TYPE, MAP_TYPE_SHOW_SINGLE_STORE);

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
        if (mLastLocation != null) {
            progressBar.start();
            NetworkRequests.getRequest(Constants.DEALS_NEARBY_URL + mLastLocation.getLatitude() + "/"
                    + mLastLocation.getLongitude() + "/" + distance, this, Constants.DOWNLOAD);
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
    protected void startLocationUpdates() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (
                ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
//                    Snackbar.make(view.findViewById(R.id.root), R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
//                            .setAction(R.string.i_allow, new View.OnClickListener() {
//                                @Override
//                                @TargetApi(Build.VERSION_CODES.M)
//                                public void onClick(View v) {
//                                }
//                            });

            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST);

        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    googleApiClient, mLocationRequest, this);
            mLastLocation = LocationServices
                    .FusedLocationApi
                    .getLastLocation(googleApiClient);
            getDealsNearBy();
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
                startLocationUpdates();
            }
        }
    }


    private boolean checkPlayServices() {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(getContext());
        if (result != ConnectionResult.SUCCESS) {
            if (googleAPI.isUserResolvableError(result)) {
                googleAPI.getErrorDialog(getActivity(), result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }

            return false;
        }

        return true;
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

        checkPlayServices();
    }

    /*
    these methods are for getting the current location
     */
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        googleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        googleApiClient.disconnect();
        if (mLastLocation != null) {
            if (mLastLocation.distanceTo(location) > 500) {
                mLastLocation = location;
                getDealsNearBy();
            }
        } else {
            mLastLocation = location;
            getDealsNearBy();
        }
    }

    @Override
    public void onResponse(String response, String tag) {
        view.findViewById(R.id.offlineLay).setVisibility(View.GONE);
        offlineCallBack.offlineMode(false);
        modelList.clear();
        try {
            modelList.addAll(JSON.parseArray(response, StoreDiscountModel.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
        if (listOverLay.getVisibility() == View.VISIBLE) {
            showFade(listOverLay, false, 500);
        }
        progressBar.stop();
        if (modelList.size() == 0){
            showFade(nodata, true, 500);
        } else {
            if (nodata.getVisibility() == View.VISIBLE){
                showFade(nodata, false, 300);
            }
        }
    }

    @Override
    public void onError(VolleyError error, String tag) {
        view.findViewById(R.id.offlineLay).setVisibility(View.GONE);
        offlineCallBack.offlineMode(false);
    }

    @Override
    public void onOffline() {
        progressBar.stop();
        view.findViewById(R.id.offlineLay).setVisibility(View.VISIBLE);
        offlineCallBack.offlineMode(true);
    }
}

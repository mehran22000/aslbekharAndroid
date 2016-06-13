package com.aslbekhar.aslbekharandroid.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.aslbekhar.aslbekharandroid.R;
import com.aslbekhar.aslbekharandroid.models.BrandModel;
import com.aslbekhar.aslbekharandroid.models.StoreDiscountModel;
import com.aslbekhar.aslbekharandroid.models.StoreModel;
import com.aslbekhar.aslbekharandroid.utilities.Constants;
import com.aslbekhar.aslbekharandroid.utilities.Interfaces;
import com.aslbekhar.aslbekharandroid.utilities.NetworkRequests;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
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
import com.rey.material.widget.ProgressView;

import java.util.ArrayList;
import java.util.List;

import static com.aslbekhar.aslbekharandroid.utilities.Constants.ADDRESS;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.DIFAULT_DISTANCE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.DISCOUNT;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.DOWNLOAD;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.GPS_ON_OR_OFF;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LAST_LAT;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LAST_LONG;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LATITUDE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LOGO;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LONGITUDE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.MAP_TYPE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.MAP_TYPE_SHOW_NEAR_BY;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.OFFLINE_MODE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.STORESLIST_NEARBY;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.TELL;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.TITLE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.VERIFIED;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.WORK_HOUR;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.getSPboolean;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.setSP;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.showFade;

/**
 * Created by Amin on 14/05/2016.
 * <p/>
 * This class will be used for
 */
public class MapNearByFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnInfoWindowClickListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener,
        GoogleMap.OnMarkerClickListener,
        OnMapReadyCallback,
        LocationListener,
        Interfaces.NetworkListeners, GoogleMap.InfoWindowAdapter {


    int type = 1;
    Interfaces.MainActivityInterface mCallback;
    Interfaces.OfflineInterface offlineCallBack;
    View view;
    GoogleApiClient googleApiClient;
    GoogleMap mMap;
    MapView mMapView;
    List<Marker> markerList = new ArrayList<>();
    List<StoreModel> storeModelList = new ArrayList<>();
    private View listOverLay;
    private ProgressView progressBar;
    private Location lastLocation;
    boolean gpsAvailableOrNot = false;
    boolean playServiceAvailableOrNot = false;

    private static final int PERMISSION_REQUEST = 0;


    public MapNearByFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getInt(MAP_TYPE, 1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_maps, container, false);



        if (getArguments() != null && getArguments().getBoolean(OFFLINE_MODE, false)){
            view.findViewById(R.id.offlineLay).setVisibility(View.VISIBLE);
        }

        gpsAvailableOrNot = getSPboolean(GPS_ON_OR_OFF);


        listOverLay = view.findViewById(R.id.listOverLay);
        progressBar = (ProgressView) view.findViewById(R.id.progressBar);

        mMapView = (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMap = mMapView.getMap();

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        if (gpsAvailableOrNot) {
            googleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onInfoWindowClick(Marker marker) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mMap = googleMap;
        this.mMap.setOnMarkerClickListener(this);
        this.mMap.setOnMapLongClickListener(this);
        this.mMap.setOnInfoWindowClickListener(this);
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
//                    Snackbar.make(view.findViewById(R.id.root), R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
//                            .setAction(R.string.i_allow, new View.OnClickListener() {
//                                @Override
//                                @TargetApi(Build.VERSION_CODES.M)
//                                public void onClick(View v) {
//                                }
//                            });

            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST);

        } else {
            lastLocation = LocationServices
                    .FusedLocationApi
                    .getLastLocation(googleApiClient);
            setSP(LAST_LAT, String.valueOf(lastLocation.getLatitude()));
            setSP(LAST_LONG, String.valueOf(lastLocation.getLongitude()));

            if (type == Constants.MAP_TYPE_SHOW_SINGLE_STORE) {
                showSingleStore();
            } else {
                initCamera(lastLocation);
                getStoresNearBy();
            }

        }
    }

    private void showSingleStore() {
        Bundle bundle = getArguments();
        StoreModel model = new StoreModel(bundle.getString(TITLE),
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
        markerList.add(mMap.addMarker(new MarkerOptions().position(position).title(model.getsName()).snippet(String.valueOf(0)).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin_store))));
        Location temp = new Location(LocationManager.GPS_PROVIDER);
        temp.setLatitude(position.latitude);
        temp.setLongitude(position.longitude);
        initCamera(new Location(temp));
    }

    @Override
    public void onLocationChanged(Location location) {
        if (lastLocation != null) {
            if (lastLocation.distanceTo(location) > 400) {
                if (type == Constants.MAP_TYPE_SHOW_NEAR_BY) {
                    lastLocation = location;
                    initCamera(lastLocation);
                    setSP(LAST_LAT, String.valueOf(lastLocation.getLatitude()));
                    setSP(LAST_LONG, String.valueOf(lastLocation.getLongitude()));
                    getStoresNearBy();
                }
            }
        } else {
            if (type == Constants.MAP_TYPE_SHOW_NEAR_BY) {
                lastLocation = location;
                initCamera(lastLocation);
                getStoresNearBy();
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
        CameraPosition position = CameraPosition.builder()
                .target(new LatLng(location.getLatitude(),
                        location.getLongitude()))
                .zoom(13f)
                .bearing(0.0f)
                .tilt(0.0f)
                .build();

        mMap.animateCamera(CameraUpdateFactory
                .newCameraPosition(position), null);

        mMap.setTrafficEnabled(true);
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getPermission();

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setInfoWindowAdapter(this);
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

        mCallback = (Interfaces.MainActivityInterface) getActivity();
        offlineCallBack = (Interfaces.OfflineInterface) getActivity();

    }


    private void getStoresNearBy() {
        if (listOverLay.getVisibility() != View.VISIBLE) {
            listOverLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            showFade(listOverLay, true, 500);
        }
        progressBar.start();
        if (type == MAP_TYPE_SHOW_NEAR_BY) {
            NetworkRequests.getRequest(STORESLIST_NEARBY + lastLocation.getLatitude() + "/"
                    + lastLocation.getLongitude() + "/" + DIFAULT_DISTANCE, this, DOWNLOAD);
        }
    }


    @Override
    public View getInfoWindow(Marker marker) {

        // Getting view from the layout file info_window_layout
        View view = getLayoutInflater(null).inflate(R.layout.info_window_layout, null);
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/theme.ttf");

        final StoreModel model = storeModelList.get(Integer.parseInt(marker.getSnippet()));

        TextView textView = (TextView) view.findViewById(R.id.title);
        textView.setText(model.getsName());
        textView.setTypeface(tf);


        textView = (TextView) view.findViewById(R.id.workHour);
        if (model.getsHour() != null && model.getsHour().length() > 1) {
            textView.setText("ساعات کار: " + model.getsHour());
            textView.setTypeface(tf);
        } else {
            textView.setVisibility(View.GONE);
        }
        textView = (TextView) view.findViewById(R.id.tell);
        if (model.getsTel1() != null && model.getsTel1().length() > 1) {
            textView.setText("تلفن: " + Constants.persianNumbers(model.getsTel1()));
            textView.setTypeface(tf);
        } else {
            textView.setVisibility(View.GONE);
        }
        textView = (TextView) view.findViewById(R.id.address);
        if (model.getsAddress() != null && model.getsAddress().length() > 1) {
            textView.setText("آدرس: " + model.getsAddress());
            textView.setTypeface(tf);
        } else {
            textView.setVisibility(View.GONE);
        }

        ImageView image = (ImageView) view.findViewById(R.id.image);
        if (model.getsVerified().equals(Constants.YES)){
            if (model.getsDiscount() > 0){
                image.setImageResource(R.drawable.discountverified);
            } else {
                image.setImageResource(R.drawable.verified);
            }
        } else {
            if (model.getsDiscount() > 0){
                image.setImageResource(R.drawable.discount);
            } else {
                image.setVisibility(View.GONE);
            }
        }

        final ImageView brandLogo = (ImageView) view.findViewById(R.id.brandLogo);
        Glide.with(this)
                .load(Uri.parse("file:///android_asset/logos/" + BrandModel.getBrandLogo(model.getbName()) + ".png"))
                .listener(new RequestListener<Uri, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, Uri uriModel, Target<GlideDrawable> target, boolean isFirstResource) {
                        Glide.with(MapNearByFragment.this).load(Constants.BRAND_LOGO_URL +
                                BrandModel.getBrandLogo(model.getbName()) + ".png").into(brandLogo);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(brandLogo);


        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }


    @Override
    public void onResponse(String response, String tag) {

        view.findViewById(R.id.offlineLay).setVisibility(View.GONE);
        offlineCallBack.offlineMode(false);

        storeModelList.clear();
        for (Marker marker : markerList) {
            marker.remove();
        }
        markerList.clear();
        try {
            storeModelList.addAll(JSON.parseArray(response, StoreDiscountModel.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (listOverLay.getVisibility() == View.VISIBLE) {
            showFade(listOverLay, false, 500);
        }
        progressBar.stop();
        for (int i = 0; i < storeModelList.size(); i++) {
            StoreModel store = storeModelList.get(i);
            LatLng position = new LatLng(Double.parseDouble(store.getsLat()), Double.parseDouble(store.getsLong()));
            markerList.add(mMap.addMarker(new MarkerOptions().position(position).title(store.getsName()).snippet(String.valueOf(i)).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin_store))));
        }
    }



    @Override
    public void onError(VolleyError error, String tag) {

    }

    @Override
    public void onOffline(String tag) {
        progressBar.stop();
        view.findViewById(R.id.offlineLay).setVisibility(View.VISIBLE);
        offlineCallBack.offlineMode(true);
    }

}

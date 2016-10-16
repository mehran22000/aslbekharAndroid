package com.aslbekhar.aslbekharandroid.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.aslbekhar.aslbekharandroid.R;
import com.aslbekhar.aslbekharandroid.adapters.BrandListAdapter;
import com.aslbekhar.aslbekharandroid.adapters.CityListAdapter;
import com.aslbekhar.aslbekharandroid.models.BrandModel;
import com.aslbekhar.aslbekharandroid.models.CityModel;
import com.aslbekhar.aslbekharandroid.models.UserModel;
import com.aslbekhar.aslbekharandroid.utilities.Interfaces;
import com.aslbekhar.aslbekharandroid.utilities.NetworkRequests;
import com.aslbekhar.aslbekharandroid.utilities.Snippets;
import com.github.florent37.viewanimator.AnimationListener;
import com.github.florent37.viewanimator.ViewAnimator;
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
import com.google.android.gms.maps.model.MarkerOptions;
import com.rey.material.widget.ProgressView;

import java.util.List;

import static com.aslbekhar.aslbekharandroid.utilities.Constants.CREATE_USER_OR_EDIT;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.EMAIL;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.FALSE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.GPS_ON_OR_OFF;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LAST_CITY_CODE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LAST_LAT;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LAST_LONG;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.PASSWORD;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.POSITION;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.REGISTER_USER;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.RESULT;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.TRANSITION_DURATION;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.TRUE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.UPDATE_USER_URL;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.USER_INFO;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.getDisplayWidth;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.getSP;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.getSPboolean;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.setSP;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.setupUI;
import static com.aslbekhar.aslbekharandroid.utilities.StaticData.getBrandModelList;
import static com.aslbekhar.aslbekharandroid.utilities.StaticData.getCityModelList;

public class RegisterActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        GoogleMap.OnMapLongClickListener,
        GoogleMap.OnMapClickListener,
        OnMapReadyCallback,
        LocationListener,
        Interfaces.NetworkListeners {

    View emailLay;
    View cityLay;
    View brandLay;
    View storeLay;
    View locationLay;
    View storeTelLay;
    View eulaLay;
    View successLay;
    View nextBtn;
    boolean nextBtnEnable = false;
    int width;
    int statusStep = 1;
    UserModel registerUserModel = new UserModel();
    CityListAdapter cityListAdapter;
    BrandListAdapter brandListAdapter;
    List<CityModel> cityModelList = getCityModelList();
    List<BrandModel> brandModelList = getBrandModelList();
    GoogleApiClient googleApiClient;
    GoogleMap mMap;
    MapView mMapView;
    private boolean gpsAvailableOrNot = false;
    private Location lastLocation;
    boolean locationAccess = false;
    boolean cameraAnimatedBefore = false;
    CameraPosition cameraPosition;
    private static final int GPS_SETTING_REQUEST_CODE = 9002;
    String cityCode;

    boolean createOrEditUser = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);

        emailLay = findViewById(R.id.emailLay);
        cityLay = findViewById(R.id.cityLay);
        brandLay = findViewById(R.id.brandLay);
        storeLay = findViewById(R.id.storeLay);
        locationLay = findViewById(R.id.locationLay);
        storeTelLay = findViewById(R.id.storeTelLay);
        eulaLay = findViewById(R.id.eulaLay);
        successLay = findViewById(R.id.successLay);

        nextBtn = findViewById(R.id.next);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nextButton();
            }
        });
        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.toolbarBack).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        width = getDisplayWidth(this);

        initEditTexts();

        cityCode = getSP(LAST_CITY_CODE);
        if (cityCode.equals(FALSE)) {
            cityCode = "021";
        }
        gpsAvailableOrNot = getSPboolean(GPS_ON_OR_OFF);

        mMapView = (MapView) findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMap = mMapView.getMap();

        if (gpsAvailableOrNot) {
            findViewById(R.id.GPSOffLay).setVisibility(View.GONE);
        } else {
            findViewById(R.id.GPSOffLay).setVisibility(View.VISIBLE);
            findViewById(R.id.GPSOffLay).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    turnOnGPS();
                }
            });
        }
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        createOrEditUser = getIntent().getExtras().getBoolean(CREATE_USER_OR_EDIT, true);
        if (createOrEditUser) {
            emailLay.setAlpha(1);
            cityLay.setAlpha(0);
            brandLay.setAlpha(0);
            storeLay.setAlpha(0);
            locationLay.setAlpha(0);
            storeTelLay.setAlpha(0);
            eulaLay.setAlpha(0);
            emailLay.bringToFront();
            mMapView.setEnabled(false);
        } else {
            showEditMode();
        }

        setupUI(this, findViewById(R.id.root));
    }

    private void showEditMode() {
        registerUserModel = JSON.parseObject(getIntent().getExtras().getString(USER_INFO, FALSE), UserModel.class);
        statusStep = getIntent().getExtras().getInt(POSITION, 2);
        ((TextView) findViewById(R.id.next)).setText(getString(R.string.save));
        switch (statusStep) {
            case 2:
                populateCityList();
                emailLay.setAlpha(0);
                cityLay.setAlpha(1);
                brandLay.setAlpha(0);
                storeLay.setAlpha(0);
                locationLay.setAlpha(0);
                storeTelLay.setAlpha(0);
                eulaLay.setAlpha(0);
                cityLay.bringToFront();
                mMapView.setEnabled(false);
                break;

            case 3:
                populateBrandList();
                emailLay.setAlpha(0);
                cityLay.setAlpha(0);
                brandLay.setAlpha(1);
                storeLay.setAlpha(0);
                locationLay.setAlpha(0);
                storeTelLay.setAlpha(0);
                eulaLay.setAlpha(0);
                brandLay.bringToFront();
                mMapView.setEnabled(false);
                break;

            case 4:
                populateStoreDetail();
                emailLay.setAlpha(0);
                cityLay.setAlpha(0);
                brandLay.setAlpha(0);
                storeLay.setAlpha(1);
                locationLay.setAlpha(0);
                storeTelLay.setAlpha(0);
                eulaLay.setAlpha(0);
                storeLay.bringToFront();
                mMapView.setEnabled(false);
                break;

            case 6:
                populateStoreTel();
                emailLay.setAlpha(0);
                cityLay.setAlpha(0);
                brandLay.setAlpha(0);
                storeLay.setAlpha(1);
                locationLay.setAlpha(0);
                storeTelLay.setAlpha(0);
                eulaLay.setAlpha(0);
                storeLay.bringToFront();
                mMapView.setEnabled(false);
                break;


        }
    }

    private void initEditTexts() {
        ((EditText) findViewById(R.id.email)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 5 && ((TextView) findViewById(R.id.password)).getText().length() > 5) {
                    if (!nextBtnEnable) {
                        enableNextBtn(true);
                    }
                } else {
                    if (nextBtnEnable) {
                        enableNextBtn(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ((EditText) findViewById(R.id.password)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 5 && ((TextView) findViewById(R.id.email)).getText().length() > 5) {
                    if (!nextBtnEnable) {
                        enableNextBtn(true);
                    }
                } else {
                    if (nextBtnEnable) {
                        enableNextBtn(false);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ((EditText) findViewById(R.id.storeName)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (validateStoreDetail().equals(TRUE)) {
                    enableNextBtn(true);
                } else {
                    enableNextBtn(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ((EditText) findViewById(R.id.storeWorkHour)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (validateStoreDetail().equals(TRUE)) {
                    enableNextBtn(true);
                } else {
                    enableNextBtn(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ((EditText) findViewById(R.id.storeAddress)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (validateStoreDetail().equals(TRUE)) {
                    enableNextBtn(true);
                } else {
                    enableNextBtn(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ((EditText) findViewById(R.id.telCode)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (validateStoreTel().equals(TRUE)) {
                    enableNextBtn(true);
                } else {
                    enableNextBtn(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ((EditText) findViewById(R.id.telValue)).addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (validateStoreTel().equals(TRUE)) {
                    enableNextBtn(true);
                } else {
                    enableNextBtn(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/theme.ttf");
        ((EditText) findViewById(R.id.password)).setTypeface(tf);
        ((EditText) findViewById(R.id.email)).setTypeface(tf);
        ((EditText) findViewById(R.id.storeName)).setTypeface(tf);
        ((EditText) findViewById(R.id.storeAddress)).setTypeface(tf);
        ((EditText) findViewById(R.id.storeWorkHour)).setTypeface(tf);
        ((EditText) findViewById(R.id.storeDistributor)).setTypeface(tf);
        ((TextInputLayout) findViewById(R.id.emailInputLay)).setTypeface(tf);
        ((TextInputLayout) findViewById(R.id.passwordInputLay)).setTypeface(tf);
        ((TextInputLayout) findViewById(R.id.storeNameInputLay)).setTypeface(tf);
        ((TextInputLayout) findViewById(R.id.storeAddressInputLay)).setTypeface(tf);
        ((TextInputLayout) findViewById(R.id.storeWorkHourInputLay)).setTypeface(tf);
        ((TextInputLayout) findViewById(R.id.storeDistributorInputLay)).setTypeface(tf);

        Snippets.setFontForActivity(findViewById(R.id.root), tf);
    }

    private String validateStoreDetail() {
        if (((TextView) findViewById(R.id.storeName)).getText().length() < 3) {
            return getString(R.string.store_name_validation);
        } else if (((TextView) findViewById(R.id.storeAddress)).getText().length() < 3) {
            return getString(R.string.store_address_validation);
        } else if (((TextView) findViewById(R.id.storeWorkHour)).getText().length() < 3) {
            return getString(R.string.store_work_hour_validation);
        }
        return TRUE;
    }

    private String validateStoreTel() {
        if (((TextView) findViewById(R.id.telCode)).getText().length() < 2) {
            return getString(R.string.store_name_validation);
        } else if (((TextView) findViewById(R.id.telValue)).getText().length() < 7) {
            return getString(R.string.store_address_validation);
        }
        return TRUE;
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

    private void enableNextBtn(boolean enable) {

        if (enable) {
            if (!nextBtnEnable) {
                nextBtnEnable = true;
                ViewAnimator.animate(nextBtn)
                        .backgroundColor(getResources().getColor(R.color.white),
                                getResources().getColor(R.color.colorPrimary))
                        .textColor(getResources().getColor(R.color.colorPrimary),
                                getResources().getColor(R.color.white))
                        .duration(200)
                        .start();
            }
        } else {
            if (nextBtnEnable) {
                nextBtnEnable = false;
                ViewAnimator.animate(nextBtn)
                        .backgroundColor(getResources().getColor(R.color.colorPrimary),
                                getResources().getColor(R.color.white))
                        .textColor(getResources().getColor(R.color.white),
                                getResources().getColor(R.color.gray))
                        .duration(200)
                        .start();
            }
        }
    }


    private void turnOnGPS() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, GPS_SETTING_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GPS_SETTING_REQUEST_CODE && resultCode == 0) {
            if (isGpsEnabled(this)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.gps_turned_on_succesfully))
                        .setIcon(getResources().getDrawable(R.drawable.icon))
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                findViewById(R.id.GPSOffLay).setVisibility(View.GONE);
                            }
                        })
                        .show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
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

    private void nextButton() {

        if (!nextBtnEnable) {
            return;
        }

        if (!createOrEditUser) {
            if (statusStep == 4) {
                hideStoreShowLocation(true);
            } else {
                registerUser();
            }
            return;
        }

        switch (statusStep) {

            case 1:
                hideEmailShowCity(true);
                break;


            case 2:
                hideCityShowBrand(true);
                break;

            case 3:
                hideBrandShowStore(true);
                break;

            case 4:
                hideStoreShowLocation(true);
                break;


            case 5:
                hideLocationShowStoreTelLay(true);
                break;

            case 6:
                if (createOrEditUser) {
                    hideStoreTelLayShowEulaLay(true);
                } else {
                    registerUser();
                }
                break;

            case 7:
                registerUser();
                break;

        }
    }

    private void registerUser() {
        ((ProgressView) findViewById(R.id.registerProgress)).start();
        if (createOrEditUser) {
            NetworkRequests.postRequest(REGISTER_USER, this, REGISTER_USER, JSON.toJSONString(registerUserModel));
        } else {
            NetworkRequests.postRequest(UPDATE_USER_URL, this, UPDATE_USER_URL, JSON.toJSONString(registerUserModel));
        }
    }


    @Override
    public void onBackPressed() {

        if (!createOrEditUser){
            finish();
        }

        Intent returnIntent;
        switch (statusStep) {
            case 1:
                returnIntent = new Intent();
                returnIntent.putExtra(RESULT, FALSE);
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
                break;

            case 2:
                hideEmailShowCity(false);
                break;

            case 3:
                hideCityShowBrand(false);
                break;

            case 4:
                hideBrandShowStore(false);
                break;

            case 5:
                hideStoreShowLocation(false);
                break;

            case 6:
                hideLocationShowStoreTelLay(false);
                break;

            case 7:
                hideStoreTelLayShowEulaLay(false);
                break;

            case 8:
                returnIntent = new Intent();
                returnIntent.putExtra(RESULT, TRUE);
                returnIntent.putExtra(EMAIL, registerUserModel.getBuEmail());
                returnIntent.putExtra(PASSWORD, registerUserModel.getBuPassword());
                returnIntent.putExtra(USER_INFO, JSON.toJSONString(registerUserModel));
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
                break;
        }
    }

    private void hideEmailShowCity(boolean showOrHide) {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(emailLay.getWindowToken(), 0);
        if (showOrHide) {
            if (validateEmail().equals(TRUE)) {
                populateCityList();
                formNext(emailLay, cityLay);
                if (registerUserModel.getBuCityName() == null) {
                    enableNextBtn(false);
                } else {
                    enableNextBtn(true);
                }
                statusStep = 2;
            } else {
                showError(validateEmail());
            }
        } else {
            formBack(cityLay, emailLay, 1);
        }
    }

    private void hideCityShowBrand(boolean showOrHide) {
        if (showOrHide) {
            populateBrandList();
            formNext(cityLay, brandLay);
            if (registerUserModel.getBuBrandId() == null) {
                enableNextBtn(false);
            } else {
                enableNextBtn(true);
            }
            statusStep = 3;
        } else {
            formBack(brandLay, cityLay, 2);
        }
    }

    private void hideBrandShowStore(boolean showOrHide) {
        if (showOrHide) {
            populateStoreDetail();
            formNext(brandLay, storeLay);
            if (!validateStoreDetail().equals(TRUE)) {
                enableNextBtn(false);
            }
            statusStep = 4;
        } else {
            formBack(storeLay, brandLay, 3);
        }
    }

    private void hideStoreShowLocation(boolean showOrHide) {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(emailLay.getWindowToken(), 0);
        if (showOrHide) {
            registerUserModel.setBuStoreName(((TextView) findViewById(R.id.storeName)).getText().toString());
            registerUserModel.setBuStoreAddress(((TextView) findViewById(R.id.storeAddress)).getText().toString());
            registerUserModel.setBuStoreHours(((TextView) findViewById(R.id.storeWorkHour)).getText().toString());
            registerUserModel.setBuDistributor(((TextView) findViewById(R.id.storeDistributor)).getText().toString());
            formNext(storeLay, locationLay);
            if (registerUserModel.getBuStoreLat() == null || registerUserModel.getBuStoreLat().length() == 0) {
                enableNextBtn(false);
            }
            statusStep = 5;
            mMapView.setEnabled(true);
        } else {
            mMapView.setEnabled(false);
            formBack(locationLay, storeLay, 4);
            if (validateStoreDetail().equals(FALSE)) {
                enableNextBtn(true);
            }
        }
    }

    private void hideLocationShowStoreTelLay(boolean showOrHide) {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(emailLay.getWindowToken(), 0);
        if (showOrHide) {
            populateStoreTel();
            formNext(locationLay, storeTelLay);
            if (!validateStoreTel().equals(TRUE)) {
                enableNextBtn(false);
            }
            statusStep = 6;
        } else {
            formBack(storeTelLay, locationLay, 5);
            if (!validateStoreTel().equals(TRUE)) {
                enableNextBtn(false);
            }
        }
    }


    private void hideStoreTelLayShowEulaLay(boolean showOrHide) {
        InputMethodManager imm =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(emailLay.getWindowToken(), 0);
        if (showOrHide) {
            registerUserModel.setBuTel(((TextView) findViewById(R.id.telValue)).getText().toString());
            formNext(storeTelLay, eulaLay);
            ViewAnimator.animate(findViewById(R.id.next)).duration(600)
                    .backgroundColor(getResources().getColor(R.color.colorPrimary),
                            getResources().getColor(R.color.green)).start();
            ((TextView) findViewById(R.id.next)).setText(getString(R.string.i_agree));
            statusStep = 7;
        } else {
            formBack(eulaLay, storeTelLay, 6);
            ViewAnimator.animate(findViewById(R.id.next)).duration(600)
                    .backgroundColor(getResources().getColor(R.color.green),
                            getResources().getColor(R.color.colorPrimary)).start();
            ((TextView) findViewById(R.id.next)).setText(getString(R.string.next));
            if (!validateStoreDetail().equals(TRUE)) {
                enableNextBtn(false);
            }
        }
    }

    private void populateStoreTel() {
        if (registerUserModel.getBuAreaCode() != null) {
            ((TextView) findViewById(R.id.telCode)).setText(registerUserModel.getBuAreaCode());
        }
        if (registerUserModel.getBuTel() != null) {
            ((TextView) findViewById(R.id.telValue)).setText(registerUserModel.getBuTel());
        }
    }

    private void populateCityList() {
        if (cityListAdapter == null) {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.cityRecyclerView);

            recyclerView.setHasFixedSize(true);

            LinearLayoutManager layoutManager = new GridLayoutManager(this, 3);

            // setting the layout manager of recyclerView
            recyclerView.setLayoutManager(layoutManager);

            cityListAdapter = new CityListAdapter(cityModelList, this, this, registerUserModel.getBuCityName());
            recyclerView.setAdapter(cityListAdapter);
        }
    }

    private void populateBrandList() {
        if (brandListAdapter == null) {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.brandRecyclerView);

            recyclerView.setHasFixedSize(true);

            LinearLayoutManager layoutManager = new LinearLayoutManager(this);

            // setting the layout manager of recyclerView
            recyclerView.setLayoutManager(layoutManager);

            brandListAdapter = new BrandListAdapter(brandModelList, this, this, registerUserModel.getBuBrandId());
            recyclerView.setAdapter(brandListAdapter);
        }
    }

    private void populateStoreDetail() {
        if (registerUserModel.getBuStoreName() != null) {
            ((TextView) findViewById(R.id.storeName)).setText(registerUserModel.getBuStoreName());
        }
        if (registerUserModel.getBuStoreAddress() != null) {
            ((TextView) findViewById(R.id.storeAddress)).setText(registerUserModel.getBuStoreAddress());
        }
        if (registerUserModel.getBuStoreHours() != null) {
            ((TextView) findViewById(R.id.storeWorkHour)).setText(registerUserModel.getBuStoreHours());
        }
        if (registerUserModel.getBuDistributor() != null) {
            ((TextView) findViewById(R.id.storeDistributor)).setText(registerUserModel.getBuDistributor());
        }
    }

    private void showError(String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setMessage(errorMessage);
        builder.setPositiveButton(R.string.ok, null);
        builder.show();
    }

    private String validateEmail() {
        registerUserModel = new UserModel(((TextView) findViewById(R.id.email)).getText().toString(),
                ((TextView) findViewById(R.id.password)).getText().toString());

        if (registerUserModel.getBuEmail().length() < 5 || !registerUserModel.getBuEmail().contains("@")
                || !registerUserModel.getBuEmail().contains("."))
            return getString(R.string.incorrectEmail);


        if (registerUserModel.getBuPassword().length() < 6)
            return getString(R.string.password_too_short);


        return TRUE;
    }

    private void formNext(View hideView, View showView) {

        ViewAnimator.animate(hideView)
                .translationX(0, +width)
                .scaleY(1f, 0.6f)
                .duration(TRANSITION_DURATION)
                .alpha(1, 0)
                .andAnimate(showView)
                .translationX(-width, 0)
                .alpha(0, 1)
                .start();
        showView.bringToFront();

    }

    private void formBack(View hideView, View showView, final int nextStatus) {

        ViewAnimator.animate(hideView)
                .translationX(0, -width)
                .duration(TRANSITION_DURATION)
                .alpha(1, 0)
                .andAnimate(showView)
                .translationX(+width, 0)
                .scaleY(0.6f, 1f)
                .duration(TRANSITION_DURATION)
                .alpha(0, 1)
                .onStop(new AnimationListener.Stop() {
                    @Override
                    public void onStop() {
                        statusStep = nextStatus;
                        enableNextBtn(true);
                    }
                })
                .start();
        showView.bringToFront();

    }

    public void onCityClicked(CityModel cityModel) {
        registerUserModel.setBuAreaCode(cityModel.getId());
        registerUserModel.setBuCityName(cityModel.getEnglishName());
        registerUserModel.setBuCityNameFa(cityModel.getPersianName());
        enableNextBtn(true);
    }

    public void onBrandClicked(BrandModel model) {
        registerUserModel.setBuBrandId(model.getbId());
        registerUserModel.setBuBrandName(model.getbName());
        registerUserModel.setBuBrandCategory(model.getcName());
        registerUserModel.setBuBrandLogoName(model.getbLogo());
        enableNextBtn(true);
    }

    @Override
    public void onResponse(String response, String tag) {

        ((ProgressView) findViewById(R.id.registerProgress)).stop();
        if (response.toLowerCase().contains("success")) {
            ((TextView) findViewById(R.id.successTxt)).setText(R.string.update_succesful);
            if (createOrEditUser) {
                statusStep = 8;
                formNext(eulaLay, successLay);
            } else {
                switch (statusStep){
                    case 2:
                        formNext(cityLay, successLay);
                        break;

                    case 3:
                        formNext(brandLay, successLay);
                        break;

                    case 4:
                        formNext(storeLay, successLay);
                        break;

                    case 6:
                        formNext(storeTelLay, successLay);
                        break;

                }
            }
            findViewById(R.id.successBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (createOrEditUser) {
                        onBackPressed();
                    } else {
                        Intent returnIntent = new Intent();
                        returnIntent.putExtra(RESULT, TRUE);
                        returnIntent.putExtra(EMAIL, registerUserModel.getBuEmail());
                        returnIntent.putExtra(PASSWORD, registerUserModel.getBuPassword());
                        returnIntent.putExtra(USER_INFO, JSON.toJSONString(registerUserModel));
                        setResult(Activity.RESULT_OK, returnIntent);
                        finish();
                    }
                }
            });
            ViewAnimator.animate(findViewById(R.id.bottomLay)).duration(700).translationY(0, 300).alpha(1, 0).start();
        } else {
            Snackbar.make(findViewById(R.id.root), R.string.connection_error, Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onError(VolleyError error, String tag) {
        ((ProgressView) findViewById(R.id.registerProgress)).stop();
        Snackbar.make(findViewById(R.id.root), R.string.connection_error, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onOffline(String tag) {
        ((ProgressView) findViewById(R.id.registerProgress)).stop();
        Snackbar.make(findViewById(R.id.root), R.string.you_are_offline, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        if (registerUserModel == null || registerUserModel.getBuStoreLat() == null || registerUserModel.getBuStoreLat().length() == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (
                    ActivityCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            || ActivityCompat.checkSelfPermission(this,
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
        } else {
            lastLocation = new Location("");
            lastLocation.setLatitude(Double.parseDouble(registerUserModel.getBuStoreLat()));
            lastLocation.setLongitude(Double.parseDouble(registerUserModel.getBuStoreLon()));
            MarkerOptions markerOptions = new MarkerOptions();

            LatLng latLng = new LatLng(lastLocation.getLatitude(), lastLocation.getLongitude());
            markerOptions.position(latLng);

            markerOptions.title(latLng.latitude + " : " + latLng.longitude);

            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),
                    R.drawable.map_pin_store)));

            mMap.clear();

            mMap.addMarker(markerOptions);

            registerUserModel.setBuStoreLat(String.valueOf(latLng.latitude));
            registerUserModel.setBuStoreLon(String.valueOf(latLng.longitude));

            enableNextBtn(true);
        }
        initCamera(lastLocation);

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

        mMap.setTrafficEnabled(true);
        if (locationAccess) {
            mMap.setMyLocationEnabled(true);
        }
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng latLng) {

                MarkerOptions markerOptions = new MarkerOptions();

                markerOptions.position(latLng);

                markerOptions.title(latLng.latitude + " : " + latLng.longitude);

                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),
                        R.drawable.map_pin_store)));

                mMap.clear();

                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));

                mMap.addMarker(markerOptions);

                registerUserModel.setBuStoreLat(String.valueOf(latLng.latitude));
                registerUserModel.setBuStoreLon(String.valueOf(latLng.longitude));

                enableNextBtn(true);
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
}

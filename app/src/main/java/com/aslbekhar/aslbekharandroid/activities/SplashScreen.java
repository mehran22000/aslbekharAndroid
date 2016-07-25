package com.aslbekhar.aslbekharandroid.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.android.volley.VolleyError;
import com.aslbekhar.aslbekharandroid.R;
import com.aslbekhar.aslbekharandroid.models.BrandModel;
import com.aslbekhar.aslbekharandroid.models.CityModel;
import com.aslbekhar.aslbekharandroid.models.StoreModel;
import com.aslbekhar.aslbekharandroid.utilities.Interfaces;
import com.aslbekhar.aslbekharandroid.utilities.Snippets;
import com.aslbekhar.aslbekharandroid.utilities.StaticData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.rey.material.widget.ProgressView;

import static android.provider.Settings.Secure;
import static android.provider.Settings.SettingNotFoundException;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CHECK_VERSION;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CITY_LIST;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.DATA_PROCESSED_OR_NOT;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.DEVICE_ID;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.FALSE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.GPS_ON_OR_OFF;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.IMAGE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.IS_FROM_NOTIFICATION;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LOCATION_PERMISSION;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.LOG_TAG;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.PLAY_SERVICES_ON_OR_OFF;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.TRUE;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.getSP;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.setSP;

/**
 * Created by Amin on 11/30/2015.
 * <p/>
 * this activity is the starting point of this app, and the
 */
public class SplashScreen extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, Interfaces.NetworkListeners {

    GoogleApiClient googleApiClient;
    private static final int PERMISSION_REQUEST = 0;
    private static final int GPS_SETTING_REQUEST_CODE = 9002;
    private boolean mResolvingError = false;
    private static int REQUEST_RESOLVE_ERROR = 9003;
    private static final String DIALOG_ERROR = "dialog_error";
    Dialog dialog;

    boolean checkForNewVersionTimer = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.splash_layout);
        ProgressView progressView = (ProgressView) findViewById(R.id.progressBar);


        if (!Snippets.getSP(DEVICE_ID).equals(FALSE)) {
            android.util.Log.d(LOG_TAG, "firebase token: " + Snippets.getSP(DEVICE_ID));
        }

        progressView.start();
        checkForPermission();

    }


    private void requestForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST);
        }
    }

    private void checkForPermission() {
        // we will continue with checking for the permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (
                ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {

            requestForPermission();

        } else {
            // if permissions we are already ok, we will check for if gps is on or not
            continueToCheckGps(true);
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
                continueToCheckGps(true);
            } else {
                continueToCheckGps(false);
            }
        } else {
            continueToCheckGps(false);
        }
    }


    private void continueToCheckGps(boolean permissionLocation) {
        if (permissionLocation) {
            Snippets.setSP(LOCATION_PERMISSION, TRUE);
        } else {
            Snippets.setSP(LOCATION_PERMISSION, FALSE);
        }

        if (!isGpsEnabled(this)) {
//            AlertDialog.Builder builder = new AlertDialog.Builder(this);
//            builder.setTitle(getString(R.string.gps_is_off))
//                    .setMessage(getString(R.string.you_need_yo_turn_on_gps))
//                    .setIcon(getResources().getDrawable(R.drawable.icon))
//                    .setPositiveButton(getString(R.string.ok_lets_turn_it_on), new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            turnOnGPS();
//                        }
//                    })
//                    .setNegativeButton(getString(R.string.no_idont_want), new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            continueToCheckForPlayServices(false);
//                        }
//                    })
//                    .setCancelable(false)
//                    .show();

            continueToCheckForPlayServices(false);
        } else {
            continueToCheckForPlayServices(true);
        }
    }

    private void continueToCheckForPlayServices(boolean GPSOnOrOFF) {
        if (GPSOnOrOFF) {
            Snippets.setSP(GPS_ON_OR_OFF, TRUE);
        } else {
            Snippets.setSP(GPS_ON_OR_OFF, FALSE);
        }
//        googleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API)
//                .build();
//        googleApiClient.connect();
        continueToCheckForData(true);
    }

    private void continueToCheckForData(boolean playServiceOnOrOff) {
        if (playServiceOnOrOff) {
            Snippets.setSP(PLAY_SERVICES_ON_OR_OFF, TRUE);
        } else {
            Snippets.setSP(PLAY_SERVICES_ON_OR_OFF, FALSE);
        }

        if (getSP(DATA_PROCESSED_OR_NOT).equals(FALSE)) {

            DataProcessing dataProcessing = new DataProcessing();
            dataProcessing.execute();
        } else {
            openMainActivity();
        }

    }

    private void openMainActivity() {
        if (getIntent().getExtras() != null && getIntent().getExtras().getBoolean(IS_FROM_NOTIFICATION, false)) {
            Intent intent = new Intent(SplashScreen.this, NotificationFullScreenActivity.class);
            intent.putExtra(IS_FROM_NOTIFICATION, true);
            intent.putExtra(IMAGE, getIntent().getExtras().getString(IMAGE, FALSE));
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GPS_SETTING_REQUEST_CODE && resultCode == 0) {
            if (isGpsEnabled(this)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.gps_turned_on_succesfully))
                        .setIcon(getResources().getDrawable(R.drawable.icon))
                        .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                continueToCheckForPlayServices(true);
                            }
                        })
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                continueToCheckForPlayServices(true);
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
                                continueToCheckForPlayServices(false);
                            }
                        })
                        .show();
            }
        }

        if (requestCode == REQUEST_RESOLVE_ERROR) {
            mResolvingError = false;
            if (resultCode == RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!googleApiClient.isConnecting() &&
                        !googleApiClient.isConnected()) {
                    googleApiClient.connect();
                }
            }
        }
    }

    public static boolean isGpsEnabled(Context context) {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            String providers = Secure.getString(context.getContentResolver(),
                    Secure.LOCATION_PROVIDERS_ALLOWED);
            if (TextUtils.isEmpty(providers)) {
                return false;
            }
            return providers.contains(LocationManager.GPS_PROVIDER);
        } else {
            final int locationMode;
            try {
                locationMode = Secure.getInt(context.getContentResolver(),
                        Secure.LOCATION_MODE);
            } catch (SettingNotFoundException e) {
                e.printStackTrace();
                return false;
            }
            switch (locationMode) {

                case Secure.LOCATION_MODE_HIGH_ACCURACY:
                case Secure.LOCATION_MODE_SENSORS_ONLY:
                case Secure.LOCATION_MODE_BATTERY_SAVING:
                    return true;
                case Secure.LOCATION_MODE_OFF:
                default:
                    return false;
            }
        }
    }

    private void turnOnGPS() {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, GPS_SETTING_REQUEST_CODE);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        continueToCheckForData(true);
    }

    @Override
    public void onConnectionSuspended(int i) {
        continueToCheckForData(true);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (!mResolvingError) {
            if (connectionResult.hasResolution()) {
                try {
                    mResolvingError = true;
                    connectionResult.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
                } catch (IntentSender.SendIntentException e) {
                    // There was an error with the resolution intent. Try again.
                    googleApiClient.connect();
                }
            } else {
                // Show dialog using GoogleApiAvailability.getErrorDialog()
                showErrorDialog(connectionResult.getErrorCode());
                mResolvingError = true;
            }
        }
    }

    /* Creates a dialog for an error message */
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }

    /* Called from ErrorDialogFragment when the dialog is dismissed. */
    public void onDialogDismissed() {
        mResolvingError = false;
        continueToCheckForData(false);
    }

    @Override
    public void onResponse(String response, String tag) {

    }

    @Override
    public void onError(VolleyError error, String tag) {
        if (tag.equals(CHECK_VERSION) && checkForNewVersionTimer) {
            checkForNewVersionTimer = false;
            checkForPermission();
        }
    }

    @Override
    public void onOffline(String tag) {
        if (tag.equals(CHECK_VERSION) && checkForNewVersionTimer) {
            checkForNewVersionTimer = false;
            checkForPermission();
        }
    }

    /* A fragment to display an error dialog */
    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() {
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, SplashScreen.REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((SplashScreen) getActivity()).onDialogDismissed();
        }
    }

    private class DataProcessing extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {


            String json = getSP(CITY_LIST);
            if (json.equals(FALSE)) {
                StaticData.getCityModelList().add(new CityModel("021", "تهران", "Tehran", "32.629198", "51.684084"));
                StaticData.getCityModelList().add(new CityModel("031", "اصفهان", "Isfahan", "32.6546270", "51.667983"));
                StaticData.getCityModelList().add(new CityModel("076", "کیش", "Kish","26.543289", "53.999226"));
                StaticData.getCityModelList().add(new CityModel("071", "شیراز", "Shiraz", "29.591768", "52.583698"));
                StaticData.getCityModelList().add(new CityModel("051", "مشهد", "Mashhad", "36.260462", "59.616755"));
                StaticData.getCityModelList().add(new CityModel("041", "تبريز", "Tabriz", "38.078940", "46.296548"));
                StaticData.getCityModelList().add(new CityModel("026", "کرج", "Karaj", "35.840019", "50.939091"));
                setSP(CITY_LIST, JSON.toJSONString(StaticData.getCityModelList()));
            } else {
                StaticData.setCityModelList(JSON.parseArray(json, CityModel.class));
            }

            StaticData.setBrandModelList(BrandModel.getBrandListFromAssets());

            StoreModel.getCatAndStoreListFromAssets();

            setSP(DATA_PROCESSED_OR_NOT, TRUE);

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            openMainActivity();
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }
}

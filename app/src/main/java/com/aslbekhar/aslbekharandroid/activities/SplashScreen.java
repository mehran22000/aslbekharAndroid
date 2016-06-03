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
import com.aslbekhar.aslbekharandroid.R;
import com.aslbekhar.aslbekharandroid.models.BrandModel;
import com.aslbekhar.aslbekharandroid.models.CityModel;
import com.aslbekhar.aslbekharandroid.models.StoreModel;
import com.aslbekhar.aslbekharandroid.utilities.Snippets;
import com.aslbekhar.aslbekharandroid.utilities.StaticData;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.rey.material.widget.ProgressView;

import static android.provider.Settings.Secure;
import static android.provider.Settings.SettingNotFoundException;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.CITY_LIST;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.DATA_PROCESSED_OR_NOT;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.FALSE;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.GPS_ON_OR_OFF;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.PLAY_SERVICES_ON_OR_OFF;
import static com.aslbekhar.aslbekharandroid.utilities.Constants.TRUE;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.getSP;
import static com.aslbekhar.aslbekharandroid.utilities.Snippets.setSP;

/**
 * Created by Amin on 11/30/2015.
 * <p>
 * this activity is the starting point of this app, and the
 */
public class SplashScreen extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    GoogleApiClient googleApiClient;
    private static final int PERMISSION_REQUEST = 0;
    private static final int GPS_SETTING_REQUEST_CODE = 9002;
    private boolean mResolvingError = false;
    private static int REQUEST_RESOLVE_ERROR = 9003;
    private static final String DIALOG_ERROR = "dialog_error";
    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);
        // we will start off by checking for the permissions
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && (
                ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        || ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            requestForPermission();

        } else {
            // if permissions we are already ok, we will check for if gps is on or not
            continueToCheckGps();
        }
    }

    private void requestForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_REQUEST);
        }
    }

    private void continueToCheckGps() {
        if (!isGpsEnabled(this)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.gps_is_off))
                    .setMessage(getString(R.string.you_need_yo_turn_on_gps))
                    .setIcon(getResources().getDrawable(R.drawable.icon))
                    .setPositiveButton(getString(R.string.ok_lets_turn_it_on), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            turnOnGPS();
                        }
                    })
                    .setNegativeButton(getString(R.string.no_idont_want), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            continueToCheckForPlayServices(false);
                        }
                    })
                    .setCancelable(false)
                    .show();

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
        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        googleApiClient.connect();
    }

    private void continueToCheckForData(boolean playServiceOnOrOff) {
        if (playServiceOnOrOff) {
            Snippets.setSP(PLAY_SERVICES_ON_OR_OFF, TRUE);
        } else {
            Snippets.setSP(PLAY_SERVICES_ON_OR_OFF, FALSE);
        }

        if (getSP(DATA_PROCESSED_OR_NOT).equals(FALSE)){
            ProgressView progressView = (ProgressView) findViewById(R.id.progressBar);
            progressView.start();

            DataProcessing dataProcessing = new DataProcessing();
            dataProcessing.execute();
        } else {
            openMainActivity();
        }

    }

    private void openMainActivity() {
        Intent intent = new Intent(SplashScreen.this, MainActivity.class);
        startActivity(intent);
        finish();
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

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST) {
            if (grantResults.length >= 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                continueToCheckGps();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getString(R.string.location_access_denied))
                        .setIcon(getResources().getDrawable(R.drawable.icon))
                        .setPositiveButton(getString(R.string.ok_i_try_again), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestForPermission();
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
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getString(R.string.location_access_denied))
                    .setIcon(getResources().getDrawable(R.drawable.icon))
                    .setPositiveButton(getString(R.string.ok_i_try_again), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestForPermission();
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

    /* A fragment to display an error dialog */
    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() { }

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
                StaticData.getCityModelList().add(new CityModel("021", "تهران", "Tehran"));
                StaticData.getCityModelList().add(new CityModel("031", "اصفهان", "Isfahan"));
                StaticData.getCityModelList().add(new CityModel("076", "کیش", "Kish"));
                StaticData.getCityModelList().add(new CityModel("071", "شیراز", "Shiraz"));
                StaticData.getCityModelList().add(new CityModel("041", "تبريز", "Tabriz"));
                StaticData.getCityModelList().add(new CityModel("051", "مشهد", "Mashhad"));
                setSP(CITY_LIST, JSON.toJSONString(StaticData.getCityModelList()));
            } else {
                StaticData.setCityModelList(JSON.parseArray(json, CityModel.class));
            }

            StaticData.setBrandModelList(BrandModel.getBrandListFromAssets());

            StoreModel.getStoreListFromAssets();

            setSP(DATA_PROCESSED_OR_NOT, TRUE);

            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            openMainActivity();
        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }
}

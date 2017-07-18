package com.example.n55.jsonandvally;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.n55.jsonandvally.app.AppController;
import com.example.n55.jsonandvally.helper.SQLiteHandler;
import com.example.n55.jsonandvally.helper.SessionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.example.n55.jsonandvally.Constants.LOGIN_LINK;

public class LoginActivity extends AppCompatActivity {

    private Button sign_in_button;
    private TextView input_address;
    private TextView input_email;
    private TextView input_password;
    private ProgressDialog pDialog;
    private SessionManager session;
    private SQLiteHandler db;


    //api/login/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sign_in_button = (Button) findViewById(R.id.sign_in_button);
        input_address = (TextView) findViewById(R.id.address);
        input_email = (TextView) findViewById(R.id.email);
        input_password = (TextView) findViewById(R.id.password);

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);


        //session
//        // SQLite database handler
//        db = new SQLiteHandler(getApplicationContext());
//
//        // Session manager
//        session = new SessionManager(getApplicationContext());
//
//        // Check if user is already logged in or not
//        if (session.isLoggedIn()) {
//            // User is already logged in. Take him to main activity
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//            finish();
//        }

        // Login button Click Event
        sign_in_button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String address = input_address.getText().toString().trim();
                String email = input_email.getText().toString().trim();
                String password = input_password.getText().toString().trim();

                // Check for empty data in the form
                if (!email.isEmpty() && !password.isEmpty() && !address.isEmpty()) {
                    // login user
                    checkLogin( email, password ,address );
                } else {
                    // Prompt user to enter credentials
                    Toast.makeText(getApplicationContext(),
                            "Please enter the credentials!", Toast.LENGTH_LONG)
                            .show();
                }
            }

        });


//        sign_in_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });

    }


    /**
     * function to verify login details in mysql db
     * */
    private void checkLogin( final String email, final String password ,final String address) {

        // Tag used to cancel the request
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                LOGIN_LINK, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("TAG", "Login Response: " + response.toString());
                hideDialog();
                //Toast.makeText(getBaseContext(),response.toString() , Toast.LENGTH_LONG).show();

                try {

                    if (!response .equals(null)) {

                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject obj = jsonArray.getJSONObject(0);
                        String apikey = obj.getString("apikey");
                        String secret = obj.getString("secret");

                        //session
                        // user successfully logged in
                        // Create login session
                        //session.setLogin(true);

                        final  MySharedPreferences sharedpreapi = new MySharedPreferences(getBaseContext());
                        sharedpreapi.setStringapikey(apikey);
                        sharedpreapi.setStringsecretkey(secret);
                        sharedpreapi.setStringurl(address);

                        // Launch main activity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        // Error in login. Get the error message
//                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getApplicationContext(),
                                "information in not valid", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    //Toast.makeText(getApplicationContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    Toast.makeText(getApplicationContext(), "information in not valid", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                //params.put("json_login" ,json);

                params.put("UserName", email);
                params.put("Password",password);
                params.put("Url", address);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
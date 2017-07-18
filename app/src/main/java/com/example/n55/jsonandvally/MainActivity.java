//
package com.example.n55.jsonandvally;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.n55.jsonandvally.TabFragments.TabEightFragment;
import com.example.n55.jsonandvally.TabFragments.TabFiveFragment;
import com.example.n55.jsonandvally.TabFragments.TabFourFragment;
import com.example.n55.jsonandvally.TabFragments.TabOneFragment;
import com.example.n55.jsonandvally.TabFragments.TabSevenFragment;
import com.example.n55.jsonandvally.TabFragments.TabThreeFragment;
import com.example.n55.jsonandvally.TabFragments.TabTwoFragment;
import com.example.n55.jsonandvally.TabFragments.TabsixFragment;
import com.example.n55.jsonandvally.adater.ViewPagerAdapter;
import com.example.n55.jsonandvally.app.AppController;
import com.example.n55.jsonandvally.helper.SQLiteHandler;
import com.example.n55.jsonandvally.helper.SessionManager;
import com.example.n55.jsonandvally.model.Kala;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static com.example.n55.jsonandvally.Snippets.setFontForActivity;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    //get login info
    private SQLiteHandler db;
    private SessionManager session;

    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();

//    // Kala json url
//    private static final String WEB_SERVER="http://solooxygen.ir";
//    private static final String API_KEY="1a5a3f0a-1838-4c16-b961-798cd975c21c";


    private static String url=null;
    private static  String url2 =null;
    private ProgressDialog pDialog;
    private List<Kala> kalaList = new ArrayList<Kala>();
    private List<Kala> kalaList_towtab = new ArrayList<Kala>();
    private List<Kala> kalaList_threetab = new ArrayList<Kala>();
    private List<Kala> kalaList_fourtab = new ArrayList<Kala>();
    private List<Kala> kalaList_fivetab = new ArrayList<Kala>();
    private List<Kala> kalaList_sixtab = new ArrayList<Kala>();
    private List<Kala> kalaList_seventab = new ArrayList<Kala>();
    private List<Kala> kalaList_eightab = new ArrayList<Kala>();

    private ListView listView;
    //private CustomListAdapter adapter;

    private String[] tabs;
    private int count_tab=0;


    String API_KEY;
    String WEB_SERVER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFontForActivity(findViewById(R.id.rootLayout));


        final  MySharedPreferences sharedpreapi = new MySharedPreferences(getBaseContext());
        //sharedpreapi.setStringapikey(apikey);
        API_KEY = sharedpreapi.getStringapikey();
        WEB_SERVER = "http://"+sharedpreapi.getStringurl();

        //url= WEB_SERVER+"/api/groups.ashx?count=1000&do=get&parentid=0&apikey="+API_KEY;

        url = WEB_SERVER + "/api/groups.ashx?count=1000&do=get&parentid=0&apikey="+API_KEY;

//        // SqLite database handler
//        db = new SQLiteHandler(getApplicationContext());
//
//        // session manager
//        session = new SessionManager(getApplicationContext());
//
//        if (!session.isLoggedIn()) {
//            logoutUser();
//        }
//
//        // Fetching user details from SQLite
//        HashMap<String, String> user = db.getUserDetails();
//
//        String name = user.get("name");
//        String email = user.get("email");
//
//        Button sign_out_button = (Button) findViewById(R.id.sign_out_button);
//
//        // Logout button click event
//        sign_out_button.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                logoutUser();
//            }
//        });




//        listView = (ListView) findViewById(R.id.list);
//        adapter = new CustomListAdapter(this, kalaList);
//        listView.setAdapter(adapter);

        pDialog = new ProgressDialog(this);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        // Creating volley request obj

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d(TAG, response.toString());
                        hidePDialog();
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = response.getJSONArray("valueList");

                            tabs=new String[jsonArray.length()];
                            for(int i=0; i<jsonArray.length(); i++){

                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                                if(jsonObject.getInt("parentId")==0 &&! jsonObject.getString("title").equals("سایر"))
                                {
                                    tabs[count_tab]=jsonObject.getString("title");
                                    //Log.d("payam",tabs[count_tab]);
                                    showkala(jsonObject.getInt("id") , count_tab);
                                    count_tab++;
                                }
                                //Log.d("LOG id", jsonObject.getString("id"));
                            }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(req);
    }

    private void logoutUser() {
        session.setLogin(false);

        db.deleteUsers();

        // Launching the login activity
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    private void showkala(int id , final int count_tab)
    {
        //http://solooxygen.ir/api/products.ashx?groupid=826&apikey=1a5a3f0a-1838-4c16-b961-798cd975c21c
        url2=WEB_SERVER+"/api/products.ashx?groupid="+id+"&apikey="+API_KEY;

        JsonObjectRequest kalareq = new JsonObjectRequest(Request.Method.GET, url2, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d(TAG, response.toString());
                        hidePDialog();
                        JSONArray jsonArray = null;
                        try {
                            jsonArray = response.getJSONArray("valueList");
                            for(int i=0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                                Kala kala = new Kala(getBaseContext());
                                kala.setTitle(jsonObject.getString("title"));
                                kala.setid(jsonObject.getInt("id"));
                                kala.setenglishTitle(jsonObject.getString("englishTitle"));
                                kala.setprice(jsonObject.getInt("price"));
                                kala.setdesc(jsonObject.getString("desc"));
                                URI uri = new URI(jsonObject.getString("thumbnail").replace(" ", "%20"));
                                //kala.setimage(uri.toString());
                                kala.setthumbnail(uri.toString());
                                kalaList.add(kala);

                                Log.d("count", String.valueOf(count_tab));
                                if (count_tab==0)
                                {
                                    kalaList_towtab.add(kala);
                                }
                                else if(count_tab==1)
                                {
                                    kalaList_threetab.add(kala);
                                }
                                else if(count_tab==2)
                                {
                                    kalaList_fourtab.add(kala);
                                }
                                else if(count_tab==3)
                                {
                                    kalaList_fivetab.add(kala);
                                }
                                else if(count_tab==4)
                                {
                                    kalaList_sixtab.add(kala);
                                }
                                else if(count_tab==5)
                                {
                                    kalaList_seventab.add(kala);
                                }
                                else if(count_tab==6)
                                {
                                    kalaList_eightab.add(kala);
                                }

//                                Kala kala = new Kala();
//                                kala.setTitle(jsonObject.getString("title"));
//                                String uri = new String(jsonObject.getString("image").replace(" ", "%20"));
//                                uri = URLEncoder.encode(uri, "utf-8");
//                                kala.setimage(uri);
//                                kalaList.add(kala);
                            }

                            toolbar = (Toolbar) findViewById(R.id.toolbar);
                            setSupportActionBar(toolbar);

                            viewPager = (ViewPager) findViewById(R.id.viewpager);
                            setupViewPager(viewPager, tabs , kalaList ,kalaList_towtab,kalaList_threetab,kalaList_fourtab,kalaList_fivetab,kalaList_sixtab,kalaList_seventab,kalaList_eightab);

                            tabLayout = (TabLayout) findViewById(R.id.tablayout);
                            tabLayout.setupWithViewPager(viewPager);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        //adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "Error: " + error.getMessage());
            }
        });


        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(kalareq);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    private void setupViewPager(ViewPager viewPager,String [] tabs ,List<Kala> kalaList,List<Kala> kalaList_towtab,List<Kala> kalaList_threetab,List<Kala> kalaList_fourtab,List<Kala> kalaList_fivetab,List<Kala> kalaList_sixtab,List<Kala> kalaList_seventab,List<Kala> kalaList_eightab) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TabOneFragment(kalaList), "همه دسته بندی ها");
        adapter.addFragment(new TabTwoFragment(kalaList_towtab),tabs[0]);
        adapter.addFragment(new TabThreeFragment(kalaList_threetab),tabs[1]);
        adapter.addFragment(new TabFourFragment(kalaList_fourtab),tabs[2]);
        adapter.addFragment(new TabFiveFragment(kalaList_fivetab),tabs[3]);
        adapter.addFragment(new TabsixFragment(kalaList_sixtab),tabs[4]);
        adapter.addFragment(new TabSevenFragment(kalaList_seventab),tabs[5]);
        adapter.addFragment(new TabEightFragment(kalaList_eightab),tabs[6]);
        viewPager.setAdapter(adapter);
        viewPager.setRotationY(180);

    }
}

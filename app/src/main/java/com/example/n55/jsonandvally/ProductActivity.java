package com.example.n55.jsonandvally;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.n55.jsonandvally.app.AppController;
import com.example.n55.jsonandvally.model.KalaEdit;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;

import static com.example.n55.jsonandvally.Constants.PRODUCT_LINK;
import static com.example.n55.jsonandvally.Snippets.setFontForActivity;

public class ProductActivity extends AppCompatActivity {
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private NetworkImageView ivImage;
    private String userChoosenTask;
    private ImageView imgV_usecamera;
    //private Bundle bundle;
    private TextView persiantitle;
    private TextView englishtitle;
    private TextView pricevalue;
    private TextView shortDescription;
    private byte[] byteArray;
    private String kSepId;

    String API_KEY;
    String WEB_SERVER;
    String url;

    private ProgressDialog pDialog;

    private MyApiInterface myApiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);


        final  MySharedPreferences sharedpreapi = new MySharedPreferences(getBaseContext());
        WEB_SERVER = "http://"+sharedpreapi.getStringurl();
        API_KEY = sharedpreapi.getStringapikey();
        url = WEB_SERVER + PRODUCT_LINK;

        setFontForActivity(findViewById(R.id.productlayout));

        persiantitle = (TextView) findViewById(R.id.persiantitle);
        englishtitle = (TextView) findViewById(R.id.englishtitle);
        pricevalue = (TextView) findViewById(R.id.priceValue);
        shortDescription = (TextView) findViewById(R.id.shortDescription);

        persiantitle.setText(getIntent().getExtras().getString("persianname"));
        englishtitle.setText(getIntent().getExtras().getString("englishname"));
        pricevalue.setText(String.valueOf(getIntent().getExtras().getInt("price")));
        shortDescription.setText(getIntent().getExtras().getString("desc"));
        kSepId = String.valueOf(getIntent().getExtras().getInt("id"));


        ivImage = (NetworkImageView) findViewById(R.id.headerImage);
        ImageLoader imageLoader = AppController.getInstance().getImageLoader();
        ivImage.setImageUrl(getIntent().getExtras().getString("tumbnail"), imageLoader);


        imgV_usecamera = (ImageView) findViewById(R.id.ImageView_usecamera);

        imgV_usecamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });



        Button saveBtn = (Button) findViewById(R.id.saveBtn);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Update_Product();
            }
        });
    }

    private void Update_Product() {


        myApiInterface = MyApiClient.getClient(getBaseContext());

        KalaEdit tmp = new KalaEdit();
        tmp.Id = 0;
        tmp.kId = Integer.valueOf(kSepId);
        tmp.kSepId = Integer.valueOf(kSepId);
        //tmp.kCode = "defult";
        tmp.kName = persiantitle.getText().toString();
        tmp.kEName = englishtitle.getText().toString();
        tmp.kPrice = Integer.valueOf(pricevalue.getText().toString());
        tmp.kDesc = shortDescription.getText().toString();
        //tmp.kLongDecs = "asdasd";
        tmp.kMarkName = "0";
        tmp.kEnable = true;
        tmp.kInvId = "0";


        //tmp.listDimention =null;
        //tmp.kAksAdd = "sdfsdf";

        if(byteArray !=null)
        {

           //Toast.makeText(ProductActivity.this, byteArray.toString() , Toast.LENGTH_SHORT).show();

            //tmp.kBinaryImage = byteArray.toString();

            String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
            tmp.kBinaryImage = encodedImage;

            Log.d("image" ,encodedImage );

            //Toast.makeText(ProductActivity.this, encodedImage , Toast.LENGTH_SHORT).show();

        }


        Call<KalaEdit> call = myApiInterface.myMethod(API_KEY, "update", tmp);
        call.enqueue(new Callback<KalaEdit>() {
            @Override
            public void onResponse(Call<KalaEdit> call, retrofit2.Response<KalaEdit> response) {

                Log.e("response", String.valueOf(response));
                Log.e("response body", String.valueOf(response.body()));
                //   try {
                //  Log.e("error body" , response.errorBody().string());
                //  } catch (IOException e) {
                //  e.printStackTrace();
                //   }
                // JsonObject body = response.body();

                // parse json

            }

            @Override
            public void onFailure(Call<KalaEdit> call, Throwable t) {
                Log.e("error", String.valueOf(t));
            }
        });


        Intent intent = new Intent(ProductActivity.this, MainActivity.class);
        startActivity(intent);
    }


    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(ProductActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask = "Choose from Library";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        byteArray = bytes.toByteArray();


        Log.d("imageinfo" ,byteArray.toString() );

//        Toast.makeText(ProductActivity.this,
//              String.valueOf(byteArray.length) , Toast.LENGTH_SHORT).show();
//
//        shortDescription.setText(byteArray.toString());

        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ivImage.setImageBitmap(thumbnail);
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                byteArray = bytes.toByteArray();

//                Toast.makeText(ProductActivity.this,
//                        String.valueOf(byteArray.length) , Toast.LENGTH_SHORT).show();
//
//                shortDescription.setText(byteArray.toString());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ivImage.setImageBitmap(bm);
    }

}
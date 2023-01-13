package com.example.upload_image_on_moodle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button browseImage, uploadImage;
    ImageView imagePreview;
    TextView resText;
    Bitmap bitmap;
    String encodeImageString;
    String TAG = "";
//    private static final String url="http://202.131.126.214/webservice/rest/server.php";
    private RequestQueue requestQueue;
    private StringRequest stringRequest;
    private static final String url="http://202.131.126.214/webservice/rest/server.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        browseImage = findViewById(R.id.btnBrowse);
        imagePreview = findViewById(R.id.userImg);
        resText = findViewById(R.id.response);
        uploadImage = findViewById(R.id.btnUpload);

        browseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
        uploadImage.setOnClickListener(view -> uploaddatatodb());
    }

    void imageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 200);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//            if (requestCode == 200) {
//                Uri selectedImageUri = data.getData();
//                if (null != selectedImageUri) {
//                    imagePreview.setImageURI(selectedImageUri);
//                }
//            }
//        }
        if(requestCode==200 && resultCode==RESULT_OK && data != null)
        {
            Uri filepath=data.getData();
            try
            {
                InputStream inputStream=getContentResolver().openInputStream(filepath);
                bitmap= BitmapFactory.decodeStream(inputStream);
                imagePreview.setImageBitmap(bitmap);
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private String encodeBitmapImage(Bitmap bitmap)
    {
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] bytesofimage=byteArrayOutputStream.toByteArray();
        encodeImageString=Base64.encodeToString(bytesofimage, Base64.DEFAULT);
        return encodeImageString;
    }
    private void uploaddatatodb()
    {
        StringRequest request=new StringRequest(Request.Method.POST, url, response -> {
            try {
                JSONObject jsonArray = new JSONObject(response);
                resText.setText(jsonArray.toString());
                Log.i(TAG,jsonArray.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            try {
                JSONObject jsonArray = new JSONObject(error.toString());
                resText.setText(jsonArray.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> params= new HashMap<>();
                String imageData = encodeBitmapImage(bitmap);
                params.put("wstoken","4f3c9f8f0404a7db50825391c295937e");
                params.put("wsfunction","core_files_upload");
                params.put("moodlewsrestformat","json");
                params.put("component","admin");
                params.put("filearea","draft");
                params.put("itemid","0");
                params.put("filepath","/");
                params.put("filename","21012022022.jpg");
                params.put("filecontent",imageData);
                params.put("contextlevel","admin");
                params.put("instanceid","5");
                return params;
            }
        };

        RequestQueue queue= Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }
}
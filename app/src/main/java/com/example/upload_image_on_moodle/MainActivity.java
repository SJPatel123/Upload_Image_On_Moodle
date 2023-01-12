package com.example.upload_image_on_moodle;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
//import android.widget.Toast;
//import org.json.JSONException;
//import org.json.JSONObject;
//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.StringRequest;
//import com.android.volley.toolbox.Volley;
//import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {
    Button browseImage, uploadImage;
    ImageView imagePreview;
    TextView resText;

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
    }

    void imageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 200);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 200) {
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    imagePreview.setImageURI(selectedImageUri);
                }
            }
        }
    }
}
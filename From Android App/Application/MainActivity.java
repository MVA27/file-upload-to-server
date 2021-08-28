package com.android.test;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button buttonUpload;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonUpload = findViewById(R.id.buttonUpload);
        textView = findViewById(R.id.textView);

        buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                intent = Intent.createChooser(intent,"Select a file");
                startActivityForResult(intent,100);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == RESULT_OK) ;
        {
            //STEP 1 : After selecting an image its path will be stored in Intent object. Therefore get that path
            Uri path = data.getData();
            textView.setText(path.toString());

            try{
                //STEP 2 : Create an InputStream and read that path data and store it in a buffer
                InputStream inputStream = MainActivity.this.getContentResolver().openInputStream(path);
                byte[] bytes = new byte[inputStream.available()];
                inputStream.read(bytes);

                //STEP 3 : Encode the bytes in android.util.Base64 methods (java class wont work)
                final String encodedData = Base64.encodeToString(bytes,Base64.NO_WRAP);

                //Private server URL : add 'android:usesCleartextTraffic="true" ' in menifest file to support HTTP protocol
                String url = "http://192.168.43.211:9494/WebApplication1/androidPattern";

                //Volley Library is used to send and receive data
                /**
                 * 1st arg : Type of HTTP Method
                 * 2nd arg : Url of server
                 * 3rd arg : Object Ref. of the class that overrides onResponse method of Response.Listener<String> class
                 * 4th arg : Object Ref. of the class that overrides onErrorResponse method of Response.ErrorListener() class
                 */
                StringRequest request = new StringRequest(Request.Method.POST, url,

                        new Response.Listener<String>()
                        {
                            @Override
                            public void onResponse(String response) {
                                Toast.makeText(MainActivity.this, "Response Received : "+response, Toast.LENGTH_SHORT).show();
                            }
                        }

                ,
                        new Response.ErrorListener()
                        {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(MainActivity.this, "Response Error : "+error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                )
                {
                    @Nullable
                    @Override
                    //Extend StringRequest class and override getParams() method to put data in HTTP packet's body
                    protected Map<String,String> getParams() throws AuthFailureError {

                        HashMap<String,String> param = new HashMap<>();
                        param.put("key",encodedData);

                        return param;
                    }
                };

                RequestQueue queue = Volley.newRequestQueue(this);
                queue.add(request);
                queue.start();
            }
            catch(Exception e){
                Toast.makeText(this, "ERROR : "+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }
}
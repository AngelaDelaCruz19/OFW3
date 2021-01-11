package com.example.ofw;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SignUp extends AppCompatActivity {
    Spinner spinner;
    ArrayList<String> catList,codList;
    String category,code;
    int position;
    ImageView img_flag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        catList=new ArrayList<String>();
        spinner=findViewById(R.id.spn_country);
        img_flag=findViewById(R.id.img_flag);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                category=spinner.getItemAtPosition(spinner.getSelectedItemPosition()).toString();
                position=catList.indexOf(category);
                code=codList.get(position);
                String uri = "@drawable/"+code;  // where myresource (without the extension) is the file

                int imageResource = getResources().getIdentifier(uri, null, getPackageName());

                img_flag=findViewById(R.id.img_flag);
                Drawable res = getResources().getDrawable(imageResource);
                img_flag.setImageDrawable(res);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // DO Nothing here
            }
        });

        spinner=findViewById(R.id.spn_country);

        OkHttpClient client = new OkHttpClient();

        FormBody body = new FormBody.Builder()
                .add("tag", "country")
                .build();

        Request request = new Request.Builder()
                .url("https://webclinicapp.000webhostapp.com/ofw_webservice.php")
                .post(body)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()){
                    final String myResponse = response.body().string();
                    SignUp.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(SignUp.this, myResponse, Toast.LENGTH_SHORT).show();
                            if(myResponse.equals("pasok")) {
//                                Toast.makeText(SignUp.this, myResponse, Toast.LENGTH_SHORT).show();
                            }else if(myResponse.equals("Wrong Password. Please Try again")) {
//                                Toast.makeText(SignUp.this, myResponse, Toast.LENGTH_SHORT).show();
                            } else {
//                                Toast.makeText(SignUp.this, myResponse, Toast.LENGTH_SHORT).show();
                                try {
                                    catList=new ArrayList<>();
                                    codList=new ArrayList<>();
                                    JSONArray jArray = new JSONArray(myResponse);
                                    for (int i = 0; i < jArray.length(); i++) {
                                        JSONObject jsonobject = jArray.getJSONObject(i);
                                        catList.add(jsonobject.getString("country"));
                                        codList.add(jsonobject.getString("code"));
                                    }
                                    spinner.setAdapter(new ArrayAdapter<>(SignUp.this,
                                            android.R.layout.simple_spinner_dropdown_item, catList));
//                                          Toast.makeText(getApplicationContext(),String.valueOf(catList),Toast.LENGTH_LONG).show();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                        }
                    });
                }
            }
        });

    }
}
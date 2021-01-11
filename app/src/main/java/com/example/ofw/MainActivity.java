package com.example.ofw;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EditText Username,Password;
        Button btn_login;
        TextView sign_up;
        ImageView img_sign;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkConnection();

        Username=findViewById(R.id.et_username);
        Password=findViewById(R.id.et_password);
        btn_login=findViewById(R.id.btn_login);
        sign_up=findViewById(R.id.tv_signup);
        img_sign=findViewById(R.id.img_sign_up);

        img_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent (MainActivity.this,SignUp.class);
                startActivity(intent);
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent (MainActivity.this,SignUp.class);
                startActivity(intent);
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Username.getText().toString();
                String password = Password.getText().toString();
//                Toast.makeText(MainActivity.this, username, Toast.LENGTH_SHORT).show();

                OkHttpClient client = new OkHttpClient();

                FormBody body = new FormBody.Builder()
                        .add("tag", "login")
                        .add("email", email)
                        .add("password", password)
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
                            MainActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(MainActivity.this, myResponse, Toast.LENGTH_SHORT).show();
                                    if(myResponse.equals("pasok")) {
                                        loginmessage1();
                                    }else if(myResponse.equals("Wrong Password. Please Try again")) {
                                        loginmessage3();
                                    } else {
//                                      Toast.makeText(User_Login.this, myResponse, Toast.LENGTH_SHORT).show();
                                        try {
                                            JSONArray jArray = new JSONArray(myResponse);
                                            for (int i = 0; i < jArray.length(); i++) {
                                                JSONObject jsonobject = jArray.getJSONObject(i);
                                                String aJsonString = jsonobject.getString("username");
                                                String aJsonString1 = jsonobject.getString("name");
                                                String aJsonString2 = jsonobject.getString("position");
                                                String aJsonString3 = jsonobject.getString("school_name");
                                                String aJsonString4 = jsonobject.getString("school_id");
//                                                SharedPreferences.Editor editor = preferences.edit();
//                                                editor.putString("username", aJsonString);
//                                                editor.putString("full_name", aJsonString1);
//                                                editor.putString("position", aJsonString2);
//                                                editor.putString("school_name", aJsonString3);
//                                                editor.putString("school_id", aJsonString4);
//                                                editor.apply();
                                                loginmessage2();
                                            }
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
        });
    }
    public void loginmessage1(){
        Toast.makeText(MainActivity.this, "wala", Toast.LENGTH_SHORT).show();
    }
    public void loginmessage2(){
        Toast.makeText(MainActivity.this, "pasok", Toast.LENGTH_SHORT).show();
    }
    public void loginmessage3(){
        Toast.makeText(MainActivity.this, "mali", Toast.LENGTH_SHORT).show();
    }
    public void checkConnection(){
        ConnectivityManager manager=(ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork=manager.getActiveNetworkInfo();

        if(null!=activeNetwork){
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI){

            }
            else if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE){

            }
            else{
                Toast.makeText(this,"No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
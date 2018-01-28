package com.devdost.nyaay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    TextView registerUser;
    TextView header;
    TextView tag;
    EditText username, password;
    Button loginButton;
    String user, pass;
    Typeface bold;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        registerUser = (TextView) findViewById(R.id.register);
        header = (TextView) findViewById(R.id.header);
        tag = (TextView) findViewById(R.id.tag);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        username.setText("Rishabh");
        password.setText("12345");
        loginButton = (Button) findViewById(R.id.loginButton);
        bold = Typeface.createFromAsset(getAssets(), "fonts/semibold.ttf");
        header.setTypeface(bold);
        tag.setTypeface(bold);

        registerUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Register.class));
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();

                if (user.equals("")) {
                    username.setError("Enter Username");
                } else if (pass.equals("")) {
                    password.setError("Enter Password");
                } else {
                    String url = "https://nyaay-hack.firebaseio.com/users.json";
                    loading = new ProgressDialog(Login.this, R.style.MyTheme);
                    loading.setCancelable(false);
                    loading.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                    loading.show();

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            if (s.equals("null")) {
                                Toast.makeText(Login.this, "Incorrect Username/Password", Toast.LENGTH_LONG).show();
                            } else {
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if (!obj.has(user)) {
                                        Toast.makeText(Login.this, "Incorrect Username/Password", Toast.LENGTH_LONG).show();
                                    } else if (obj.getJSONObject(user).getString("password").equals(pass)) {
                                        UserDetails.username = user;
                                        UserDetails.password = pass;
                                        UserDetails.profile_url = obj.getJSONObject(user).getString("profile_url");
                                        startActivity(new Intent(Login.this, Users.class));
                                    } else {
                                        Toast.makeText(Login.this, "Incorrect Username/Password", Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            loading.dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            System.out.println("" + volleyError);
                            loading.dismiss();
                        }
                    });

                    RequestQueue rQueue = Volley.newRequestQueue(Login.this);
                    rQueue.add(request);
                }

            }
        });
    }
}

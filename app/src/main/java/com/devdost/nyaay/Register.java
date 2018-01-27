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
import com.firebase.client.Firebase;

import org.json.JSONException;
import org.json.JSONObject;

public class Register extends AppCompatActivity {
    EditText username, password;
    Button registerButton;
    String user, pass;
    TextView login;
    TextView header;
    TextView tag;
    Typeface bold;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        registerButton = (Button) findViewById(R.id.registerButton);
        header = (TextView) findViewById(R.id.header);
        tag = (TextView) findViewById(R.id.tag);
        login = (TextView) findViewById(R.id.login);
        bold = Typeface.createFromAsset(getAssets(), "fonts/semibold.ttf");
        header.setTypeface(bold);
        tag.setTypeface(bold);

        Firebase.setAndroidContext(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass = password.getText().toString();

                if (user.equals("")) {
                    username.setError("Enter Username");
                } else if (pass.equals("")) {
                    password.setError("Enter Password");
                } else if (!user.matches("[A-Za-z0-9]+")) {
                    username.setError("Only Alphabet & Digits Allowed");
                } else if (user.length() < 5) {
                    username.setError("At Least 5 Characters Long");
                } else if (pass.length() < 5) {
                    password.setError("At Least 5 Characters Long");
                } else {
                    loading = new ProgressDialog(Register.this, R.style.MyTheme);
                    loading.setCancelable(false);
                    loading.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
                    loading.show();

                    String url = "https://nyaay-hack.firebaseio.com/users.json";

                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Firebase reference = new Firebase("https://nyaay-hack.firebaseio.com/users");

                            if (s.equals("null")) {
                                reference.child(user).child("password").setValue(pass);
                                Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_LONG).show();
                            } else {
                                try {
                                    JSONObject obj = new JSONObject(s);

                                    if (!obj.has(user)) {
                                        reference.child(user).child("password").setValue(pass);
                                        Toast.makeText(Register.this, "Registration Successful", Toast.LENGTH_LONG).show();
                                    } else {
                                        Toast.makeText(Register.this, "Username Already Exists", Toast.LENGTH_LONG).show();
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

                    RequestQueue rQueue = Volley.newRequestQueue(Register.this);
                    rQueue.add(request);
                }
            }
        });
    }
}
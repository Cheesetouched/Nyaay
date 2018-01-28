package com.devdost.nyaay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.devdost.nyaay.Adapters.ChatsAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Users extends AppCompatActivity {
    TextView noUsersText;
    TextView headertext;
    private RecyclerView chats_recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;
    ArrayList<String> al = new ArrayList<>();
    ArrayList<Integer> random=new ArrayList<Integer>();
    ArrayList<Integer> status=new ArrayList<Integer>();
    int totalUsers = 0;
    Typeface bold;
    ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        random.add(0);
        random.add(1);
        noUsersText = (TextView) findViewById(R.id.noUsersText);
        chats_recycler = (RecyclerView) findViewById(R.id.usersList);
        bold = Typeface.createFromAsset(getAssets(), "fonts/semibold.ttf");
        headertext = findViewById(R.id.headertext);
        headertext.setTypeface(bold);

        loading = new ProgressDialog(Users.this, R.style.MyTheme);
        loading.setCancelable(false);
        loading.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        loading.show();

        String url = "https://nyaay-hack.firebaseio.com/users.json";

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                doOnSuccess(s);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                System.out.println("" + volleyError);
            }
        });

        RequestQueue rQueue = Volley.newRequestQueue(Users.this);
        rQueue.add(request);

    }

    public void doOnSuccess(String s) {
        try {
            JSONObject obj = new JSONObject(s);

            Iterator i = obj.keys();
            String key = "";

            while (i.hasNext()) {
                key = i.next().toString();

                if (!key.equals(UserDetails.username)) {
                    Collections.shuffle(random);
                    al.add(key);
                    status.add(random.get(0));
                }

                totalUsers++;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (totalUsers <= 1) {
            noUsersText.setVisibility(View.VISIBLE);
            chats_recycler.setVisibility(View.GONE);
        } else {
            noUsersText.setVisibility(View.GONE);
            chats_recycler.setVisibility(View.VISIBLE);
            manager = new LinearLayoutManager(getApplicationContext());
            chats_recycler.setLayoutManager(manager);
            adapter = new ChatsAdapter(Users.this, al, status);
            chats_recycler.setAdapter(adapter);

            chats_recycler.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onSingleTapUp(MotionEvent e) {
                        return true;
                    }
                });

                @Override
                public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                    View child = rv.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && gestureDetector.onTouchEvent(e)) {
                        int position = rv.getChildAdapterPosition(child);
                        UserDetails.chatWith = al.get(position);
                        startActivity(new Intent(Users.this, Chat.class));

                    }
                    return false;
                }

                @Override
                public void onTouchEvent(RecyclerView rv, MotionEvent e) {
                }

                @Override
                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                }
            });

        }

        loading.dismiss();
    }
}
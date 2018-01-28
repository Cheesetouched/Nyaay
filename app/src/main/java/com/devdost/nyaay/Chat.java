package com.devdost.nyaay;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.devdost.nyaay.API.models.responses.ReportRes;
import com.devdost.nyaay.API.services.ApiClient;
import com.devdost.nyaay.API.services.ApiInterface;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Chat extends AppCompatActivity implements View.OnClickListener {

    String SIGHTENGINE_API_USER = "1033368812";
    String SIGHTENGINE_API_SECRET = "nnFftX2d2vWeyn9f4ZRa";
    String download_url;
    ImageView selectPhoto;
    LinearLayout layout;
    RelativeLayout layout_2;
    ImageView sendButton;
    EditText messageArea;
    TextView headertext;
    ScrollView scrollView;
    Firebase reference1, reference2;
    Typeface bold;
    ProgressDialog loading;
    ApiInterface api;


    private static final int GALLERY_PICK = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        getWindow().setBackgroundDrawableResource(R.drawable.chat_back);
        bold = Typeface.createFromAsset(getAssets(), "fonts/semibold.ttf");
        headertext = (TextView) findViewById(R.id.headertext);
        headertext.setText("Chat with " + UserDetails.chatWith);
        headertext.setTypeface(bold);
        layout = (LinearLayout) findViewById(R.id.layout1);
        layout_2 = (RelativeLayout) findViewById(R.id.layout2);
        sendButton = (ImageView) findViewById(R.id.sendButton);
        selectPhoto = findViewById(R.id.selectPhoto);
        selectPhoto.setOnClickListener(this);
        messageArea = (EditText) findViewById(R.id.messageArea);
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        api = ApiClient.getClient().create(ApiInterface.class);

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("https://nyaay-hack.firebaseio.com/messages/" + UserDetails.username + "_" + UserDetails.chatWith);
        reference2 = new Firebase("https://nyaay-hack.firebaseio.com/messages/" + UserDetails.chatWith + "_" + UserDetails.username);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if (!messageText.equals("")) {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    map.put("user", UserDetails.username);
                    reference1.push().setValue(map);
                    reference2.push().setValue(map);
                    messageArea.setText("");
                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Map map = dataSnapshot.getValue(Map.class);
                String message = map.get("message").toString();
                String userName = map.get("user").toString();

                if (message.contains("message_images")) {
                    if (loading != null) {
                        loading.dismiss();
                    }
                    if (userName.equals(UserDetails.username)) {
                        addMessageBoxImage(message, 1);
                    } else {
                        addMessageBoxImage(message, 2);
                    }
                } else {

                    if (userName.equals(UserDetails.username)) {
                        addMessageBox(message, 1);
                    } else {
                        addMessageBox(message, 2);
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public void alert(String message) {
        Toast.makeText(Chat.this, message, Toast.LENGTH_LONG).show();
    }

    public void addMessageBox(String message, int type) {
        TextView textView = new TextView(Chat.this);
        textView.setText(message);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if (type == 1) {
            lp2.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.bubble_in);
        } else {
            lp2.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bubble_out);
        }
        textView.setLayoutParams(lp2);
        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }

    public void addMessageBoxImage(String message, int type) {
        ImageView img = new ImageView(Chat.this);
        Picasso.with(getApplicationContext()).load(message).resize(500, 500).centerCrop().into(img);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 1.0f;

        if (type == 1) {
            lp2.gravity = Gravity.RIGHT;
            img.setBackgroundResource(R.drawable.bubble_in);
        } else {
            lp2.gravity = Gravity.LEFT;
            img.setBackgroundResource(R.drawable.bubble_out);
        }
        img.setLayoutParams(lp2);
        layout.addView(img);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }

    public void analysePhoto(Response<ReportRes> response) {

        boolean weapon_found = false;
        boolean alcohol_found = false;
        boolean drugs_found = false;
        boolean nudity_found = false;
        boolean offensive_found = false;

        double weapon = response.body().getWeapon();
        double alcohol = response.body().getAlcohol();
        double drugs = response.body().getDrugs();
        double raw = response.body().getRaw();
        double partial = response.body().getPartial();
        double safe = response.body().getSafe();
        double prob = response.body().getProb();



    }

    @Override
    public void onClick(View view) {

        if (view == selectPhoto) {

            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_PICK);

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {

            loading = new ProgressDialog(Chat.this, R.style.MyTheme);
            loading.setCancelable(false);
            loading.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
            loading.show();

            Uri imageuri = data.getData();

            long Timestamp = System.currentTimeMillis();

            StorageReference Image_storage_ref = FirebaseStorage.getInstance().getReference().child("message_images").child(UserDetails.username).child(String.valueOf(Timestamp) + ".jpg");

            Image_storage_ref.putFile(imageuri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    if (task.isSuccessful()) {
                        download_url = task.getResult().getDownloadUrl().toString();

                        api.checkReport("nudity,wad,offensive", SIGHTENGINE_API_USER, SIGHTENGINE_API_SECRET, download_url).enqueue(new Callback<ReportRes>() {
                            @Override
                            public void onResponse(Call<ReportRes> call, Response<ReportRes> response) {

                                if (response.isSuccessful()) {

                                    if (response.body().getStatus().equalsIgnoreCase("success")) {
                                        analysePhoto(response);
                                    }

                                    else {
                                        loading.dismiss();
                                        alert("Status: Some error occurred");
                                    }

                                }
                                else {
                                    loading.dismiss();
                                    alert("Failed: Some error occurred");
                                }
                            }

                            @Override
                            public void onFailure(Call<ReportRes> call, Throwable t) {
                                loading.dismiss();
                                alert(t.toString());
                            }
                        });


                    }

                }
            });
        }

    }
}
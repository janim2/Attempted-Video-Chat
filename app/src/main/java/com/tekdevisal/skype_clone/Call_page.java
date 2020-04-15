package com.tekdevisal.skype_clone;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class Call_page extends AppCompatActivity {

    private TextView nameContact;
    private ImageView profileimage;
    private ImageView cancelcall_button, acceptcall_button;

    private String receiverID = "",receiverUserImage = "", receiverUsername = "";

    private String senderID = "",senderUserImage = "",senderUsername = "", checker="";
    private String callingID = "", ringingID = "";
    private FirebaseAuth firebaseAuth;
    private DatabaseReference usersRef;

    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_page);

        receiverID = getIntent().getStringExtra("reciver_id");
        firebaseAuth  = FirebaseAuth.getInstance();
        senderID = firebaseAuth.getCurrentUser().getUid();

        mediaPlayer = MediaPlayer.create(this,R.raw.tone);

        nameContact = findViewById(R.id.name_calling);
        profileimage = findViewById(R.id.profile_image_calling);
        cancelcall_button = findViewById(R.id.cancel_call);
        acceptcall_button = findViewById(R.id.make_call);

        usersRef = FirebaseDatabase.getInstance().getReference("users");

        cancelcall_button.setOnClickListener(v -> {
            mediaPlayer.stop();
            checker = "clicked";
            cancelCallingUser();
        });

        acceptcall_button.setOnClickListener(v -> {

            mediaPlayer.stop();

            final HashMap<String, Object> callingPickupMap = new HashMap<>();
            callingPickupMap.put("picked", "picked");

            usersRef.child(senderID).child("Ringing")
                    .updateChildren(callingPickupMap)
                    .addOnCompleteListener(task -> {
                        if(task.isComplete()){
                            startActivity(new Intent(Call_page.this, MainActivity.class));
                        }
                    });
        });

        getandSetUserprofileInfo();
    }

    private void getandSetUserprofileInfo() {
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(receiverID).exists()){
                    receiverUserImage = dataSnapshot.child(receiverID).child("image").getValue().toString();
                    receiverUsername = dataSnapshot.child(receiverID).child("name").getValue().toString();
                    nameContact.setText(receiverUsername);
                    Picasso.get().load(receiverUserImage).placeholder(R.drawable.profile_image).into(profileimage);
                }
                if(dataSnapshot.child(senderID).exists()){
                    senderUserImage = dataSnapshot.child(senderID).child("image").getValue().toString();
                    senderUsername = dataSnapshot.child(senderID).child("name").getValue().toString();
                    nameContact.setText(receiverUsername);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mediaPlayer.start();

        if(!checker.equals("clicked")){
            usersRef.child(receiverID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if(!dataSnapshot.hasChild("Calling") && !dataSnapshot.hasChild("Ringing")){

                        final HashMap<String, Object > callingInfo = new HashMap<>();
                        callingInfo.put("calling", receiverID);

                        usersRef.child(senderID)
                                .child("Calling").updateChildren(callingInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    final HashMap<String, Object> ringingInfo = new HashMap<>();
                                    ringingInfo.put("ringing", senderID);

                                    usersRef.child(receiverID)
                                            .child("Ringing")
                                            .updateChildren(ringingInfo);

                                }
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            usersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.child(senderID).hasChild("Ringing") && !dataSnapshot.child(senderID).hasChild("Calling")){
                        acceptcall_button.setVisibility(View.VISIBLE);
                    }

                    if(dataSnapshot.child(receiverID).child("Ringing").hasChild("picked")){
                        mediaPlayer.stop();
                        startActivity(new Intent(Call_page.this, MainActivity.class));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void cancelCallingUser() {
        //canceling call on senders(caller) side of the application
        usersRef.child(senderID)
                .child("Calling")
                .addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.hasChild("calling")){
                    callingID = dataSnapshot.child("calling").getValue().toString();
                    Toast.makeText(Call_page.this, callingID, Toast.LENGTH_LONG).show();

                    usersRef.child(callingID)
                            .child("Ringing")
                            .removeValue()
                            .addOnCompleteListener(task -> {

                                if(task.isSuccessful()){
                                    usersRef.child(senderID)
                                            .child("Calling").removeValue()
                                            .addOnCompleteListener(task1 -> {
                                                startActivity(new Intent(Call_page.this, Home.class));
                                                finish();
                                            });
                                }

                            });
                }else{
                    startActivity(new Intent(Call_page.this, Home.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        //canceling call on receiver side of the application
        usersRef.child(senderID)
                .child("Ringing").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() && dataSnapshot.hasChild("ringing")){
                    ringingID = dataSnapshot.child("ringing").getValue().toString();

                    usersRef.child(ringingID)
                            .child("Calling")
                            .removeValue()
                            .addOnCompleteListener(task -> {

                                if(task.isSuccessful()){
                                    usersRef.child(senderID).child("Ringing").removeValue()
                                            .addOnCompleteListener(task1 -> {
                                                startActivity(new Intent(Call_page.this, Home.class));
                                                finish();
                                            });
                                }

                            });
                }else{
                    startActivity(new Intent(Call_page.this, Home.class));
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

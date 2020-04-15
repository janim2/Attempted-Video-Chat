package com.tekdevisal.skype_clone;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Home extends AppCompatActivity {
    private DatabaseReference usersRef;
    private FirebaseAuth mauth;
    private String calledBy="";

    @Override
    protected void onStart() {
        super.onStart();
        checkforReceivingcalls();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mauth = FirebaseAuth.getInstance();

        findViewById(R.id.open_dialer).setOnClickListener(v -> {
            Intent dialer = new Intent(this, Call_page.class);
            dialer.putExtra("reciver_id","xFV7Ne4oaGVF7uMcIkmvUFljhge2");
            startActivity(dialer);
        });


    }

    private void checkforReceivingcalls() {
        usersRef = FirebaseDatabase.getInstance()
                .getReference("users")
                .child(mauth.getCurrentUser().getUid()).child("Ringing");

        usersRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild("ringing")){
                            calledBy = dataSnapshot.child("ringing").getValue().toString();
                            Intent call=new Intent(Home.this, Call_page.class);
                            call.putExtra("reciver_id",calledBy);
                            startActivity(call);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

}

package com.example.muhammadzubair.ema;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class activityConfirmationList extends AppCompatActivity implements View.OnClickListener {

    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authListener;
    DatabaseReference databaseReference;

    TextView venue, catering, entertainment, guest, title;
    String mVenue, mCatering, mEntertainment, mGuest;
    Toolbar mToolbar;
    String mID;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_list);

        mID = getIntent().getStringExtra("vid");


        mToolbar = findViewById(R.id.backToolbar);
        setSupportActionBar(mToolbar);
        title = mToolbar.findViewById(R.id.tootbartitle);
        ImageView backpress = mToolbar.findViewById(R.id.backArrow);
        title.setText(mID);

        venue = findViewById(R.id.venue_list);
        catering = findViewById(R.id.catering_list);
        entertainment = findViewById(R.id.entertainment_list);
        guest = findViewById(R.id.guest_list);

        mVenue = venue.getText().toString();
        mCatering = catering.getText().toString();
        mEntertainment = entertainment.getText().toString();
        mGuest = guest.getText().toString();

        venue.setVisibility(View.GONE);
        catering.setVisibility(View.GONE);
        entertainment.setVisibility(View.GONE);
        guest.setVisibility(View.GONE);

        firebaseAuth=FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("objectUsers").child(firebaseAuth.getCurrentUser().getUid()).child("Confirmations").child(mID);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("Venue")) {
                        venue.setVisibility(View.VISIBLE);
                    }

                    if (dataSnapshot.hasChild("Catering")) {
                        catering.setVisibility(View.VISIBLE);
                    }

                    if (dataSnapshot.hasChild("Entertainment")) {
                        entertainment.setVisibility(View.VISIBLE);
                    }

                    if (dataSnapshot.hasChild("Guests")) {
                        guest.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    Toast.makeText(activityConfirmationList.this, "No Confirmation List Aavailable", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        venue.setOnClickListener(this);
        catering.setOnClickListener(this);
        entertainment.setOnClickListener(this);
        guest.setOnClickListener(this);
        backpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    @Override
    public void onClick (View v)
    {
        if (v.getId() == R.id.venue_list) {
            Intent i = new Intent(activityConfirmationList.this, activityViewConfirmations.class);
            i.putExtra("vid", mID);
            i.putExtra("list_id", mVenue );
            startActivity(i);
        }
        else if (v.getId() == R.id.catering_list) {
            Intent i = new Intent(activityConfirmationList.this, activityViewConfirmations.class);
            i.putExtra("vid", mID);
            i.putExtra("list_id", mCatering );
            startActivity(i);
        }
        else if (v.getId() == R.id.entertainment_list) {
            Intent i = new Intent(activityConfirmationList.this, activityViewConfirmations.class);
            i.putExtra("vid", mID);
            i.putExtra("list_id", mEntertainment );
            startActivity(i);
        }
        else if (v.getId() == R.id.guest_list) {
            Intent i=new Intent(activityConfirmationList.this, activityViewConfirmations.class);
            i.putExtra("vid", mID);
            i.putExtra("list_id", mGuest );
            startActivity(i);
        }
    }

//    @Override
//    public void onPointerCaptureChanged(boolean hasCapture) {
//
//    }
}

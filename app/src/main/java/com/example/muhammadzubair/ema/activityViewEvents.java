package com.example.muhammadzubair.ema;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.muhammadzubair.ema.model.objectEvents;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


/**
 * A simple {@link Fragment} subclass.
 */
public class activityViewEvents extends AppCompatActivity
{
    TextView title, mEdit, mDelete;
    Toolbar mToolbar;
    String mID, mMsg, tvTitle,tvDetails,tvDate, tvTime, tvVenue;
    Button mSend;
    TextView mTitle,mDetails,mDate,mTime,mVenue;
    DatabaseReference mUsersRef;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_events);
        mID = getIntent().getStringExtra("vid");

        mUsersRef= FirebaseDatabase.getInstance().getReference().child("objectUsers");

        mTitle = findViewById(R.id.event_title);
        mDetails = findViewById(R.id.event_details);
        mDate = findViewById(R.id.event_date);
        mTime = findViewById(R.id.event_time);
        mVenue = findViewById(R.id.event_venue);
        mSend = findViewById(R.id.btn_send_sms);
        mEdit = findViewById(R.id.btn_edit);
        mDelete = findViewById(R.id.btn_delete);

        mToolbar = findViewById(R.id.backToolbar);
        setSupportActionBar(mToolbar);
        title = mToolbar.findViewById(R.id.tootbartitle);
        ImageView backpress = mToolbar.findViewById(R.id.backArrow);
        title.setText(mID);

        backpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(activityViewEvents.this, activitySendMessage.class);
                i.putExtra("id", mID);
                i.putExtra("msg", mMsg);
                startActivity(i);
            }
        });

        mEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i=new Intent(activityViewEvents.this, activityEditEvents.class);
                i.putExtra("id", mID);
                startActivity(i);
            }
        });

        mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                mUsersRef.child(FirebaseAuth.getInstance().getUid()).child("objectEvents").child(mID).removeValue();
                Toast.makeText(activityViewEvents.this, "Event deleted successfully! ", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        getEventDetails();
    }

    private void getEventDetails()
    {
        if(FirebaseAuth.getInstance().getUid()!=null)
        {
            mUsersRef.child(FirebaseAuth.getInstance().getUid()).child("objectEvents").child(mID).addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    objectEvents mCurrentUser = dataSnapshot.getValue(objectEvents.class);
                    Log.i("asdasd",""+dataSnapshot);
                    if (mCurrentUser != null)
                    {
                        mTitle.setText(mCurrentUser.getETitle());
                        mDetails.setText(mCurrentUser.getEDetails());
                        mDate.setText(mCurrentUser.getEDate());
                        mTime.setText(mCurrentUser.getETime());
                        mVenue.setText(mCurrentUser.getEVenue());

                        tvTitle = mTitle.getText().toString();
                        tvDetails = mDetails.getText().toString();
                        tvDate = mDate.getText().toString();
                        tvTime = mTime.getText().toString();
                        tvVenue = mVenue.getText().toString();

                        mMsg = ""+ tvTitle + "\n" + "" + tvDetails + "\n" + "Date: " + tvDate + "\n" + "Time: " + tvTime + "\n" + "Venue: " + tvVenue;
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError)
                {

                }
            });
        }
    }
}

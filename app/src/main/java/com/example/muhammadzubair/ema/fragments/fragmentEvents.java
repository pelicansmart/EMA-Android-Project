package com.example.muhammadzubair.ema.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.muhammadzubair.ema.R;
import com.example.muhammadzubair.ema.activitySetupEvents;
import com.example.muhammadzubair.ema.activityViewEvents;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragmentEvents extends Fragment implements View.OnClickListener
{

    TextView mAddEvent, mEma001, mEma002, mEma003, mEma004, mEma005, mMsg;
    String eID, mEMA001, mEMA002, mEMA003, mEMA004, mEMA005;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener authListener;
    DatabaseReference mEventRef;

    public fragmentEvents()
    {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        View view=  inflater.inflate(R.layout.fragment_view_event, container, false);

        mAddEvent = view.findViewById(R.id.add_event);
        mEma001 = view.findViewById(R.id.ema001);
        mEMA001 = mEma001.getText().toString();
        mEma002 = view.findViewById(R.id.ema002);
        mEMA002 = mEma002.getText().toString();
        mEma003 = view.findViewById(R.id.ema003);
        mEMA003 = mEma003.getText().toString();
        mEma004 = view.findViewById(R.id.ema004);
        mEMA004 = mEma004.getText().toString();
        mEma005 = view.findViewById(R.id.ema005);
        mEMA005 = mEma005.getText().toString();
        mMsg = view.findViewById(R.id.msg);

        mEma001.setVisibility(View.GONE);
        mEma002.setVisibility(View.GONE);
        mEma003.setVisibility(View.GONE);
        mEma004.setVisibility(View.GONE);
        mEma005.setVisibility(View.GONE);
        mMsg.setVisibility(View.GONE);

        mAuth=FirebaseAuth.getInstance();
        mEventRef= FirebaseDatabase.getInstance().getReference().child("objectUsers")
                .child(mAuth.getCurrentUser().getUid()).child("objectEvents");

        mEventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("EMA001")) {
                        mEma001.setVisibility(View.VISIBLE);
                        eID = "EMA002";
                        mMsg.setVisibility(View.GONE);
                    }

                    if (dataSnapshot.hasChild("EMA002")) {
                        mEma002.setVisibility(View.VISIBLE);
                        eID = "EMA003";
                    }

                    if (dataSnapshot.hasChild("EMA003")) {
                        mEma003.setVisibility(View.VISIBLE);
                        eID = "EMA004";
                    }

                    if (dataSnapshot.hasChild("EMA004")) {
                        mEma004.setVisibility(View.VISIBLE);
                        eID = "EMA005";
                    }

                    if (dataSnapshot.hasChild("EMA005")) {
                        mEma005.setVisibility(View.VISIBLE);
                       mAddEvent.setVisibility(View.GONE);
                    }
                   // mAddEvent.setVisibility(View.INVISIBLE);
                }
                else {
                    mMsg.setVisibility(View.VISIBLE);
                    eID = "EMA001";
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

//        mAddEvent.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//               // startActivity(new Intent(getActivity(), activitySetupEvents.class));
//
//                Intent intent = new Intent(getContext(), activitySetupEvents.class);
//                intent.putExtra("id",eID);
//                startActivity(intent);
//            }
//        });
        mEma001.setOnClickListener(this);
        mEma002.setOnClickListener(this);
        mEma003.setOnClickListener(this);
        mEma004.setOnClickListener(this);
        mEma005.setOnClickListener(this);
        mAddEvent.setOnClickListener(this);
        return view;
    }
    @Override
    public void onClick (View v)
    {
        if (v.getId() == R.id.add_event) {
            Intent i=new Intent(getContext(), activitySetupEvents.class);
            i.putExtra("id", eID);
            startActivity(i);
        }
        else if (v.getId() == R.id.ema001) {
            Intent i = new Intent(getContext(), activityViewEvents.class);
            i.putExtra("vid",mEMA001);
            startActivity(i);
        }
        else if (v.getId() == R.id.ema002) {
            Intent i = new Intent(getContext(), activityViewEvents.class);
            i.putExtra("vid",mEMA002);
            startActivity(i);
        }
        else if (v.getId() == R.id.ema003) {
            Intent i = new Intent(getContext(), activityViewEvents.class);
            i.putExtra("vid",mEMA003);
            startActivity(i);
        }
        else if (v.getId() == R.id.ema004) {
            Intent i=new Intent(getContext(), activityViewEvents.class);
            i.putExtra("vid", mEMA004);
            startActivity(i);
        }
        else if (v.getId() == R.id.ema005) {
            Intent i=new Intent(getActivity(), activityViewEvents.class);
            i.putExtra("vid", mEMA005);
            startActivity(i);
        }
    }
}

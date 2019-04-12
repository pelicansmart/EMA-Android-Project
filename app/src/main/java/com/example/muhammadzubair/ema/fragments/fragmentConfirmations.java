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
import com.example.muhammadzubair.ema.activityConfirmationList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragmentConfirmations extends Fragment implements View.OnClickListener
{

    TextView mEma001, mEma002, mEma003, mEma004, mEma005, mMsg;
    String listID, mEMA001, mEMA002, mEMA003, mEMA004, mEMA005;

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener authListener;
    DatabaseReference mEventRef;

    public fragmentConfirmations()
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

        View view=  inflater.inflate(R.layout.fragment_view_confirmation, container, false);

        mEma001 = view.findViewById(R.id.ema001);
        mEma002 = view.findViewById(R.id.ema002);
        mEma003 = view.findViewById(R.id.ema003);
        mEma004 = view.findViewById(R.id.ema004);
        mEma005 = view.findViewById(R.id.ema005);
        mMsg = view.findViewById(R.id.msg);

        mEMA001 = mEma001.getText().toString();
        mEMA002 = mEma002.getText().toString();
        mEMA003 = mEma003.getText().toString();
        mEMA004 = mEma004.getText().toString();
        mEMA005 = mEma005.getText().toString();

        mEma001.setVisibility(View.GONE);
        mEma002.setVisibility(View.GONE);
        mEma003.setVisibility(View.GONE);
        mEma004.setVisibility(View.GONE);
        mEma005.setVisibility(View.GONE);
        mMsg.setVisibility(View.GONE);

        mAuth=FirebaseAuth.getInstance();
        mEventRef= FirebaseDatabase.getInstance().getReference().child("objectUsers").child(mAuth.getCurrentUser().getUid()).child("Confirmations");

        mEventRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    if (dataSnapshot.hasChild("EMA001")) {
                        mEma001.setVisibility(View.VISIBLE);
                        mMsg.setVisibility(View.GONE);
                       // listID = "EMA002";
                    }

                    if (dataSnapshot.hasChild("EMA002")) {
                        mEma002.setVisibility(View.VISIBLE);
                      //  listID = "EMA003";
                    }

                    if (dataSnapshot.hasChild("EMA003")) {
                        mEma003.setVisibility(View.VISIBLE);
                       // listID = "EMA004";
                    }

                    if (dataSnapshot.hasChild("EMA004")) {
                        mEma004.setVisibility(View.VISIBLE);
                       // listID = "EMA005";
                    }

                    if (dataSnapshot.hasChild("EMA005")) {
                        mEma005.setVisibility(View.VISIBLE);
                    }
                }
                else {
                    mMsg.setVisibility(View.VISIBLE);
                    //listID = "EMA001";
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        mEma001.setOnClickListener(this);
        mEma002.setOnClickListener(this);
        mEma003.setOnClickListener(this);
        mEma004.setOnClickListener(this);
        mEma005.setOnClickListener(this);
        return view;
    }
    @Override
    public void onClick (View v)
    {
        if (v.getId() == R.id.ema001) {
            Intent i = new Intent(getContext(), activityConfirmationList.class);
            i.putExtra("vid",mEMA001);
            startActivity(i);
        }
        else if (v.getId() == R.id.ema002) {
            Intent i = new Intent(getContext(), activityConfirmationList.class);
            i.putExtra("vid",mEMA002);
            startActivity(i);
        }
        else if (v.getId() == R.id.ema003) {
            Intent i = new Intent(getContext(), activityConfirmationList.class);
            i.putExtra("vid",mEMA003);
            startActivity(i);
        }
        else if (v.getId() == R.id.ema004) {
            Intent i=new Intent(getContext(), activityConfirmationList.class);
            i.putExtra("vid", mEMA004);
            startActivity(i);
        }
        else if (v.getId() == R.id.ema005) {
            Intent i=new Intent(getActivity(), activityConfirmationList.class);
            i.putExtra("vid", mEMA005);
            startActivity(i);
        }
    }
}

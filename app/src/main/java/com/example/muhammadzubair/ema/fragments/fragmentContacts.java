package com.example.muhammadzubair.ema.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.muhammadzubair.ema.activityContactPicker;

import com.example.muhammadzubair.ema.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragmentContacts extends Fragment implements View.OnClickListener {

    TextView venue, catering, entertainment, guest;

    String mVenue, mCatering, mEntertainment, mGuest;

    public fragmentContacts() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_view_contacts, container, false);
         venue = view.findViewById(R.id.venue_list);
         mVenue = venue.getText().toString();
         catering = view.findViewById(R.id.catering_list);
         mCatering = catering.getText().toString();
         entertainment = view.findViewById(R.id.entertainment_list);
         mEntertainment = entertainment.getText().toString();
         guest = view.findViewById(R.id.guest_list);
         mGuest = guest.getText().toString();

        venue.setOnClickListener(this);
        catering.setOnClickListener(this);
        entertainment.setOnClickListener(this);
        guest.setOnClickListener(this);
        return view;
    }

        @Override
        public void onClick (View v)

        {
            if (v.getId() == R.id.venue_list) {
                Intent i = new Intent(getActivity(), activityContactPicker.class);
                i.putExtra("Title",mVenue);
                startActivity(i);
            } else if (v.getId() == R.id.catering_list) {
                Intent i = new Intent(getActivity(), activityContactPicker.class);
                i.putExtra("Title",mCatering);
                startActivity(i);
            } else if (v.getId() == R.id.entertainment_list) {
                Intent i = new Intent(getActivity(), activityContactPicker.class);
                i.putExtra("Title",mEntertainment);
                startActivity(i);
            } else if (v.getId() == R.id.guest_list) {
                Intent i=new Intent(getActivity(), activityContactPicker.class);
                i.putExtra("Title", mGuest);
                startActivity(i);
            }
        }
}
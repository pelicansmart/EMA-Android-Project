package com.example.muhammadzubair.ema.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.muhammadzubair.ema.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class fragmentHome extends Fragment
        implements View.OnClickListener
{

    TextView mContacts, mEvents, mConfirmation, mProfile;

    public fragmentHome()
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

        View view=  inflater.inflate(R.layout.fragment_view_home, container, false);

        mContacts = view.findViewById(R.id.btn_contacts);
        mEvents = view.findViewById(R.id.btn_events);
        mConfirmation = view.findViewById(R.id.btn_confirmations);
        mProfile = view.findViewById(R.id.btn_profile);

        mContacts.setOnClickListener(this);
        mEvents.setOnClickListener(this);
        mConfirmation.setOnClickListener(this);
        mProfile.setOnClickListener(this);

//        mContacts.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v)
//            {
//                fragmentContacts fragment2 = new fragmentContacts();
//                android.app.FragmentManager fragmentManager = getActivity().getFragmentManager();
//                android.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
//                fragmentTransaction.replace(R.id.toolbar_container,fragment2,"tag");
//                fragmentTransaction.addToBackStack(null);
//                fragmentTransaction.commit();
//                //startActivity(new Intent(getContext(), fragmentContacts.class));
//            }
//        });


        return view;
    }
    @Override
    public void onClick(View view) {
//        Fragment fragment = null;
//        switch (view.getId()) {
//            case R.id.btn_contacts:
//                fragment = new fragmentContacts();
//                replaceFragment(fragment);
//                break;
//
//            case R.id.btn_events:
//                fragment = new fragmentEvents();
//                replaceFragment(fragment);
//                break;
//        }
    }

//    public void replaceFragment(Fragment someFragment) {
//        FragmentTransaction transaction = getFragmentManager().beginTransaction();
//        transaction.replace(R.id.action_contacts, someFragment);
//        transaction.addToBackStack(null);
//        transaction.commit();
//    }


//    @Override
//    public void onClick(View v) {
//            if (v.getId() == R.id.btn_contacts) {
//                Intent i = new Intent(getActivity(), fragmentContacts.class);
//               // i.putExtra("Title",mVenue);
//                startActivity(i);
//            } else if (v.getId() == R.id.btn_events) {
//                Intent i = new Intent(getActivity(), fragmentEvents.class);
//               // i.putExtra("Title",mCatering);
//                startActivity(i);
//            } else if (v.getId() == R.id.btn_confirmations) {
//                Intent i = new Intent(getActivity(), fragmentContacts.class);
//               // i.putExtra("Title",mEntertainment);
//                startActivity(i);
//            } else if (v.getId() == R.id.btn_profile) {
//                Intent i=new Intent(getActivity(), fragmentProfile.class);
//               // i.putExtra("Title", mGuest);
//                startActivity(i);
//            }
//    }
}

package com.example.muhammadzubair.ema.fragments;



import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.muhammadzubair.ema.activityEditProfile;
import com.example.muhammadzubair.ema.R;
import com.example.muhammadzubair.ema.activityLoginSignup;
import com.example.muhammadzubair.ema.model.objectUsers;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;



/**
 * A simple {@link Fragment} subclass.
 */
public class fragmentProfile extends Fragment
{
    TextView mFullName,mEmail,mPhone, editProfile, mDeleteAccount;
    CircleImageView mProfileImage;

    DatabaseReference mUsersRef;


    public fragmentProfile()
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

        final View view=  inflater.inflate(R.layout.fragment_view_profile, container, false);
        mUsersRef= FirebaseDatabase.getInstance().getReference().child("objectUsers");
        mFullName= view.findViewById(R.id.et_fullname);
        mEmail= view.findViewById(R.id.tv_email);
        mPhone= view.findViewById(R.id.et_phone);
        mProfileImage= view.findViewById(R.id.profile_image);
        editProfile = view.findViewById(R.id.edit_profile);
        mDeleteAccount = view.findViewById(R.id.delete_account);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getActivity(), activityEditProfile.class));
            }
        });

        mDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                mUsersRef.child(FirebaseAuth.getInstance().getUid()).removeValue();
                                startActivity(new Intent(getActivity(), activityLoginSignup.class));
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                //return view;
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener).setNegativeButton("No", dialogClickListener).show();
            }
        });
        getProfileDetails();
        return view;
    }
    private void getProfileDetails()
    {
        if(FirebaseAuth.getInstance().getUid()!=null)
        {
            mUsersRef.child(FirebaseAuth.getInstance().getUid()).addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                {
                    objectUsers mCurrentUser=dataSnapshot.getValue(objectUsers.class);
                    Log.i("asdasd",""+dataSnapshot);
                    if (mCurrentUser != null)
                    {
                        Log.i("asdasd",""+mCurrentUser.getUserEmail());

                        mFullName.setText(mCurrentUser.getFullName());
                        mEmail.setText(mCurrentUser.getUserEmail());
                        mPhone.setText(mCurrentUser.getUserPhone());
                        Picasso.get().load(mCurrentUser.getProfilePicUrl()).fit().into(mProfileImage);
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

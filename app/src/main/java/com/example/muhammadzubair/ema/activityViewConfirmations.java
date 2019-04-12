package com.example.muhammadzubair.ema;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class activityViewConfirmations extends AppCompatActivity
{
    TextView title, mListName,response_total;
    Toolbar mToolbar;
    String listID, mID;
    ListView mList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private  int responses=0;
    List<Map<String, String>> data1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_confirmations);
        mID = getIntent().getStringExtra("vid");
        listID = getIntent().getStringExtra("list_id");

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("objectUsers").child(firebaseAuth.getCurrentUser().getUid()).child("Confirmations").child(mID).child(listID);
        data1 = new ArrayList<Map<String, String>>();

        response_total = findViewById(R.id.total);
        mList = findViewById(R.id.confirmation_list);
        mListName = findViewById(R.id.list_name);
        mListName.setText(listID);

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

        getConfirmationDetails();
        receiveSMS();
    }

    private void getConfirmationDetails()
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren()) {

                    int i = 1;
                    for ( DataSnapshot items : dataSnapshot.getChildren()) {

                        Map<String, String> contacts = new HashMap<>(4);
                        String name = (String) items.child("Name").getValue();
                        String number = (String) items.child("Number").getValue();
                        String response = (String) items.child("Response").getValue();
                        if(response.equals("YES")){
                            responses+=1;
                        }
                        String quantity = String.valueOf(i);
                        contacts.put("Quantity",quantity);
                        contacts.put("Name", name);
                        contacts.put("Number", number);
                        contacts.put("Response", response);
                        data1.add(contacts);
                        i++;
                    }
                    response_total.setText("Total Confirmed Guests: "+Integer.toString(responses));
                    SimpleAdapter arrayAdapter = new SimpleAdapter(activityViewConfirmations.this, data1, R.layout.custom_confirmation_list, new String[]{"Quantity", "Name", "Number", "Response"}, new int[]{R.id.textView1, R.id.text,R.id.text2, R.id.textView2});
                    mList.setAdapter(arrayAdapter);
                }
//                else{
//                    mMsg.setVisibility(View.VISIBLE);
//                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError)
            {

            }
        });
    }

    private void receiveSMS(){

    }
}

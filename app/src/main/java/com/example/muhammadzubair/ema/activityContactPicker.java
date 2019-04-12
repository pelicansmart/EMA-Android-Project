package com.example.muhammadzubair.ema;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wafflecopter.multicontactpicker.ContactResult;
import com.wafflecopter.multicontactpicker.LimitColumn;
import com.wafflecopter.multicontactpicker.MultiContactPicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class activityContactPicker extends AppCompatActivity {

    TextView title, mMsg;
    Toolbar mToolbar;
    String mTitle;
    private ListView contact_list;
    private static final int CONTACT_PICKER_REQUEST = 991;
    private ArrayList<ContactResult> results = new ArrayList<>();
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    List<Map<String, String>> data1;
    List<Map<String, String>> data2;
    DatabaseReference databaseReference2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_picker);
        mTitle = getIntent().getStringExtra("Title");
        final TextView btnOpenPicker = (TextView) findViewById(R.id.btnOpenPicker);
        mMsg = (TextView) findViewById(R.id.msg);
        mMsg.setVisibility(View.INVISIBLE);
        contact_list = (ListView) findViewById(R.id.contact_list);
        mToolbar = findViewById(R.id.backToolbar);
        setSupportActionBar(mToolbar);
        title = mToolbar.findViewById(R.id.tootbartitle);
        ImageView backpress = mToolbar.findViewById(R.id.backArrow);
        title.setText(mTitle);
        final Button BtnSave = findViewById(R.id.btn_save);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("objectUsers");

        if(mTitle.equals("Venue")) {
            databaseReference2 = FirebaseDatabase.getInstance().getReference().child("objectUsers").child(firebaseAuth.getCurrentUser().getUid()).child("objectContacts").child("Venue");
        }
        if(mTitle.equals("Entertainment")){
            databaseReference2 = FirebaseDatabase.getInstance().getReference().child("objectUsers").child(firebaseAuth.getCurrentUser().getUid()).child("objectContacts").child("Entertainment");
        }
        if(mTitle.equals("Catering")){
            databaseReference2 = FirebaseDatabase.getInstance().getReference().child("objectUsers").child(firebaseAuth.getCurrentUser().getUid()).child("objectContacts").child("Catering");
        }
        if(mTitle.equals("Guests")){
            databaseReference2 = FirebaseDatabase.getInstance().getReference().child("objectUsers").child(firebaseAuth.getCurrentUser().getUid()).child("objectContacts").child("Guests");
        }

        data1 = new ArrayList<Map<String, String>>();
        data2 = new ArrayList<Map<String, String>>();

        BtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data1.size() == 0) {
                    Toast.makeText(activityContactPicker.this, "Please Add objectContacts", Toast.LENGTH_SHORT).show();
                } else {
                    if (mTitle.equals("Venue")) {
                        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("objectContacts").child("Venue").setValue(data1);
                        Toast.makeText(activityContactPicker.this, "Venue List added.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (mTitle.equals("Entertainment")) {
                        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("objectContacts").child("Entertainment").setValue(data1);
                        Toast.makeText(activityContactPicker.this, "Entertainment List added.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (mTitle.equals("Catering")) {
                        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("objectContacts").child("Catering").setValue(data1);
                        Toast.makeText(activityContactPicker.this, "Catering List added.", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (mTitle.equals("Guests")) {
                        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("objectContacts").child("Guests").setValue(data1);
                        Toast.makeText(activityContactPicker.this, "Guests List added.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });

        backpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnOpenPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(activityContactPicker.this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
                    new MultiContactPicker.Builder(activityContactPicker.this) //Activity/fragment context
                            .hideScrollbar(false) //Optional - default: false
                            .showTrack(true) //Optional - default: true
                            .searchIconColor(Color.WHITE) //Optional - default: White
                            .setChoiceMode(MultiContactPicker.CHOICE_MODE_MULTIPLE) //Optional - default: CHOICE_MODE_MULTIPLE
                            .handleColor(ContextCompat.getColor(activityContactPicker.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                            .bubbleColor(ContextCompat.getColor(activityContactPicker.this, R.color.colorPrimary)) //Optional - default: Azure Blue
                            .bubbleTextColor(Color.WHITE) //Optional - default: White
                            .setTitleText("Select objectContacts") //Optional - only use if required
                            .setSelectedContacts(results) //Optional - will pre-select contacts of your choice. String... or List<ContactResult>
                            .setLoadingType(MultiContactPicker.LOAD_ASYNC) //Optional - default LOAD_ASYNC (wait till all loaded vs stream results)
                            .limitToColumn(LimitColumn.NONE) //Optional - default NONE (Include phone + email, limiting to one can improve loading time)
                            .showPickerForResult(CONTACT_PICKER_REQUEST);
                } else {
                    Toast.makeText(activityContactPicker.this, "Remember to go into settings and enable the contacts permission.", Toast.LENGTH_LONG).show();
                }
            }
        });

    databaseReference2.addValueEventListener(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            if(dataSnapshot.hasChildren()) {
                for (DataSnapshot items : dataSnapshot.getChildren()) {
                    BtnSave.setVisibility(View.GONE);
                    btnOpenPicker.setVisibility(View.GONE);

                    Map<String, String> contacts = new HashMap<>(2);
                    String name = (String) items.child("Name").getValue();
                    String number = (String) items.child("Number").getValue();
                    contacts.put("Name", name);
                    contacts.put("Number", number);
                    data2.add(contacts);
                }
                SimpleAdapter arrayAdapter = new SimpleAdapter(activityContactPicker.this, data2, android.R.layout.simple_list_item_2, new String[]{"Name", "Number"}, new int[]{android.R.id.text1, android.R.id.text2});
                contact_list.setAdapter(arrayAdapter);
            }
            else{
                mMsg.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError)
        {

        }
    });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CONTACT_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                results.addAll(MultiContactPicker.obtainResult(data));
                if (results.size() > 0) {
                    for (int i = 0; i < results.size(); i++) {
                        Map<String, String> datum = new HashMap<String, String>(2);
                        datum.put("Name", results.get(i).getDisplayName().toString());
                        datum.put("Number", results.get(i).getPhoneNumbers().get(0).toString());
                        data1.add(datum);
                    }
                    SimpleAdapter adapter = new SimpleAdapter(this, data1, android.R.layout.simple_list_item_2, new String[]{"Name", "Number"}, new int[]{android.R.id.text1, android.R.id.text2});
                    contact_list.setAdapter(adapter);
                    mMsg.setVisibility(View.INVISIBLE);
                }
            } else if (resultCode == RESULT_CANCELED) {
                System.out.println("User closed the picker without selecting items.");
            }
        }
    }
}
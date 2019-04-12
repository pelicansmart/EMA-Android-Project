package com.example.muhammadzubair.ema;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class activitySendMessage extends AppCompatActivity {

    final Calendar c = Calendar.getInstance();
    private DatePickerDialog mDatePickerDialog;

    List<Map<String, String>> data1;
    List<Map<String, String>> data2;

    private FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference2;
    DatabaseReference databaseReference;
    FirebaseAuth firebaseAuth;

    TextView title;
    Toolbar mToolbar;
    String mID, mMsg, mMsg2, mDueDate, mListName, mListName2, mResponse;

    Button mSendSms;
    TextView mMsgTV,mDateTV;

    Spinner spinnerDropDown;
    String[] cList = {"Venue","Entertainment","Catering","Guests"};

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_send_message);
        mID = getIntent().getStringExtra("id");
        mMsg = getIntent().getStringExtra("msg");

        mSendSms = findViewById(R.id.btn_send_sms);
        mMsgTV = findViewById(R.id.msgTV);
        mDateTV = findViewById(R.id.due_data);

        mMsgTV.setText(mMsg);

        mToolbar = findViewById(R.id.backToolbar);
        setSupportActionBar(mToolbar);
        title = mToolbar.findViewById(R.id.tootbartitle);
        ImageView backpress = mToolbar.findViewById(R.id.backArrow);
        title.setText("Send Invitations");

        spinnerDropDown =(Spinner)findViewById(R.id.spinner1);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference().child("objectUsers");

        backpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item ,cList);
        spinnerDropDown.setAdapter(adapter);
        spinnerDropDown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                // Get select item
                int sid = spinnerDropDown.getSelectedItemPosition();
                Toast.makeText(getBaseContext(), "You have selected : " + cList[sid],Toast.LENGTH_SHORT).show();
                mListName = spinnerDropDown.getSelectedItem().toString();

                if(mListName.equals("Venue")) {
                    databaseReference2 = FirebaseDatabase.getInstance().getReference().child("objectUsers").child(firebaseAuth.getCurrentUser().getUid()).child("objectContacts").child("Venue");
                }
                if(mListName.equals("Entertainment")){
                    databaseReference2 = FirebaseDatabase.getInstance().getReference().child("objectUsers").child(firebaseAuth.getCurrentUser().getUid()).child("objectContacts").child("Entertainment");
                }
                if(mListName.equals("Catering")){
                    databaseReference2 = FirebaseDatabase.getInstance().getReference().child("objectUsers").child(firebaseAuth.getCurrentUser().getUid()).child("objectContacts").child("Catering");
                }
                if(mListName.equals("Guests")){
                    databaseReference2 = FirebaseDatabase.getInstance().getReference().child("objectUsers").child(firebaseAuth.getCurrentUser().getUid()).child("objectContacts").child("Guests");
                }
                data2 = new ArrayList<Map<String, String>>();
                databaseReference2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChildren()) {
                            for (DataSnapshot items : dataSnapshot.getChildren()) {

                                Map<String, String> contacts = new HashMap<>(3);
                                String name = (String) items.child("Name").getValue();
                                String number = (String) items.child("Number").getValue();
                                String response = "NO";
                               // String quantity = (String) items.getKey();
                                contacts.put("Name", name);
                                contacts.put("Number", number);
                                contacts.put("Response", response);
                          //      contacts.put("Quantity", quantity);
                                data2.add(contacts);
                            }
//                    data2 = data1;
//                    SimpleAdapter arrayAdapter = new SimpleAdapter(activityContactPicker.this, data2, android.R.layout.simple_list_item_2, new String[]{"Name", "Number"}, new int[]{android.R.id.text1, android.R.id.text2});
//                    contact_list.setAdapter(arrayAdapter);
                        }
                        else{
                            Toast.makeText(activitySendMessage.this, "list has no contacts.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError)
                    {

                    }
                });
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
        data1 = new ArrayList<Map<String, String>>();

        mSendSms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (data2.size() == 0) {
                    Toast.makeText(activitySendMessage.this, "Contact List is Empty", Toast.LENGTH_SHORT).show();
                } else {
                    if (mListName.equals("Venue")) {
                        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("Confirmations").child(mID).child("Venue").setValue(data2);
                        Toast.makeText(activitySendMessage.this, "Invitation Sent.", Toast.LENGTH_SHORT).show();
                    } else if (mListName.equals("Entertainment")) {
                        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("Confirmations").child(mID).child("Entertainment").setValue(data2);
                        Toast.makeText(activitySendMessage.this, "Invitation Sent.", Toast.LENGTH_SHORT).show();
                    } else if (mListName.equals("Catering")) {
                        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("Confirmations").child(mID).child("Catering").setValue(data2);
                        Toast.makeText(activitySendMessage.this, "Invitation Sent.", Toast.LENGTH_SHORT).show();
                    } else if (mListName.equals("Guests")) {
                        databaseReference.child(firebaseAuth.getCurrentUser().getUid()).child("Confirmations").child(mID).child("Guests").setValue(data2);
                        Toast.makeText(activitySendMessage.this, "Invitation Sent.", Toast.LENGTH_SHORT).show();
                        //finish();
                    }

                    sendSMS();

                    Intent i = new Intent(activitySendMessage.this, activityViewConfirmations.class);
                    i.putExtra("vid", mID);
                    i.putExtra("list_id", mListName);
                    startActivity(i);
                }
            }
        });

        setDateField();
        mDateTV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDatePickerDialog.show();
                return false;
            }
        });
    }
    private void setDateField() {
        Calendar newCalendar = Calendar.getInstance();
        mDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                @SuppressLint("SimpleDateFormat") SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy");
                final Date startDate = newDate.getTime();
                String fdate = sd.format(startDate);

                mDateTV.setText(fdate);
                mDueDate = mDateTV.getText().toString().trim();
            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));mDatePickerDialog.getDatePicker();
    }

    private void sendSMS() {
//        Calendar newCalendar = Calendar.getInstance();
//        mDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
//
//            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//                Calendar newDate = Calendar.getInstance();
//                newDate.set(year, monthOfYear, dayOfMonth);
//                @SuppressLint("SimpleDateFormat") SimpleDateFormat sd = new SimpleDateFormat("dd-MM-yyyy");
//                final Date startDate = newDate.getTime();
//                String fdate = sd.format(startDate);
//
//                mDateTV.setText(fdate);
//                mDueDate = mDateTV.getText().toString().trim();
//            }
//        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));mDatePickerDialog.getDatePicker();
    }
}

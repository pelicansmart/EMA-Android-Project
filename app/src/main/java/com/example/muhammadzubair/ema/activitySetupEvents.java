package com.example.muhammadzubair.ema;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.util.Map;


public class activitySetupEvents extends AppCompatActivity {

    private DatabaseReference mUsersRef;

    TextView title, mSave;
    Toolbar mToolbar;
    String mID;

    final Calendar c = Calendar.getInstance();
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);

    private DatePickerDialog mDatePickerDialog;
    private TimePickerDialog timePickerDialog;

    EditText mTitle,mDetails,mDate,mTime,mVenue;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_events);

        mID = getIntent().getStringExtra("id");

        mTitle = findViewById(R.id.event_title);
        mDetails = findViewById(R.id.event_details);
        mDate = findViewById(R.id.event_date);
        mTime = findViewById(R.id.event_time);
        mVenue = findViewById(R.id.event_venue);
        mSave = findViewById(R.id.btn_save);

        mToolbar = findViewById(R.id.backToolbar);
        setSupportActionBar(mToolbar);
        title = mToolbar.findViewById(R.id.tootbartitle);
        ImageView backpress = mToolbar.findViewById(R.id.backArrow);
        title.setText(mID);

        mUsersRef = FirebaseDatabase.getInstance().getReference().child("objectUsers");

        backpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mSave.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(mTitle.getText().toString().isEmpty()||mDetails.getText().toString().isEmpty()||mDate.getText().toString().isEmpty()
                        ||mTime.getText().toString().isEmpty()||mVenue.getText().toString().isEmpty())
                {
                    Toast.makeText(activitySetupEvents.this, "Please Complete Information with all fields", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    checkTitle(mTitle.getText().toString());
                }
            }
        });

        setDateField();
        mDate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDatePickerDialog.show();
                return false;
            }
        });

        mTime.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setTimeField();
                timePickerDialog.show();
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

                mDate.setText(fdate);

            }
        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
        mDatePickerDialog.getDatePicker();//.setMaxDate(System.currentTimeMillis());

    }

    private void setTimeField() {
        timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        mTime.setText(hourOfDay + ":" + minute);
                    }
                }, hour, minute, false);
    }


    private void checkTitle(final String eTitle)
    {
        final ArrayList<String> mTitleList=new ArrayList<>();
        mTitleList.clear();
        mUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    String title =dataSnapshot1.child("eTitle").getValue(String.class);
                    mTitleList.add(title);
                }

                if(mTitleList.contains(eTitle))
                {
                    Toast.makeText(activitySetupEvents.this, "Title already exists", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    updateEvent();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateEvent()
    {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("eTitle", mTitle.getText().toString().trim());
        childUpdates.put("eDetails", mDetails.getText().toString().trim());
        childUpdates.put("eDate", mDate.getText().toString().trim());
        childUpdates.put("eTime", mTime.getText().toString().trim());
        childUpdates.put("eVenue", mVenue.getText().toString().trim());
        mUsersRef.child(FirebaseAuth.getInstance().getUid()).child("objectEvents").child(mID).updateChildren(childUpdates);
        Toast.makeText(activitySetupEvents.this, "Event added successfully! ", Toast.LENGTH_SHORT).show();
        finish();
    }
}

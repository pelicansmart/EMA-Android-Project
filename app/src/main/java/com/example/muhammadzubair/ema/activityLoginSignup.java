package com.example.muhammadzubair.ema;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hbb20.CountryCodePicker;

public class activityLoginSignup extends AppCompatActivity
{
    CountryCodePicker codePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.bar_center_simple);
        TextView title=(TextView)findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        title.setText("Registration");
        codePicker=findViewById(R.id.ccp);
        final EditText phonenum=findViewById(R.id.phoneTF);
        Button submitButton=findViewById(R.id.nextB);
        codePicker.registerCarrierNumberEditText(phonenum);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(!codePicker.isValidFullNumber())
                {
                    Toast.makeText(activityLoginSignup.this, "Please Input the valid number", Toast.LENGTH_SHORT).show();
                }

                else
                {
                    startActivity(new Intent(getBaseContext(),activityUserConfirmation.class).putExtra("Phone",codePicker.getFullNumberWithPlus().trim()));
                }
            }
        });
    }
}

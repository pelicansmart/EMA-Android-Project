package com.example.muhammadzubair.ema;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class activityUserConfirmation extends AppCompatActivity {


    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    String mVerificationId;
    private static final String TAG = "PhoneAuthActivity";
    private FirebaseAuth mAuth;
    Button ButtonConfirm;
    String pinCode="";
    String phoneNumber ="";
    TextView tvResend;
    boolean value;
    PinEntryEditText entryEditText;
    DatabaseReference mUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);
        phoneNumber = getIntent().getStringExtra("Phone");
        Log.i("phoneNum",phoneNumber);

        Toolbar mToolbar=findViewById(R.id.backToolbar);
        setSupportActionBar(mToolbar);
        TextView title= mToolbar.findViewById(R.id.tootbartitle);
        ImageView backpress= mToolbar.findViewById(R.id.backArrow);
        title.setText("Phone Verification");

        backpress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mUsers= FirebaseDatabase.getInstance().getReference().child("objectUsers");
        mUsers.keepSynced(true);
        ButtonConfirm = findViewById(R.id.confirmB);
        tvResend = findViewById(R.id.textViewResend);
        TextView displaynum=findViewById(R.id.tvdisplayNum);
        entryEditText=findViewById(R.id.et_pin_entry);
        displaynum.setText(""+phoneNumber);
        mAuth = FirebaseAuth.getInstance();
        tvResend.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                if (mResendToken != null)
                {
                    Toast.makeText(activityUserConfirmation.this, "Resending Code", Toast.LENGTH_SHORT).show();
                    resendVerificationCode(phoneNumber, mResendToken);
                }
            }
        });

        entryEditText.setOnPinEnteredListener(new PinEntryEditText.OnPinEnteredListener() {
            @Override
            public void onPinEntered(CharSequence str)
            {
                pinCode = str.toString();
            }
        });


        ButtonConfirm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)

            {
                if (pinCode.isEmpty())
                {
                    Toast.makeText(activityUserConfirmation.this, "Please enter the Pin code to proceed", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(activityUserConfirmation.this, "Please wait", Toast.LENGTH_SHORT).show();

                    verifyPhoneNumberWithCode(mVerificationId, pinCode);
                }

            }
        });

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks()
        {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential)
            {
                Log.d(TAG, "onVerificationCompleted:" + credential.getSmsCode());
                entryEditText.setText(credential.getSmsCode());
                signInWithPhoneAuthCredential(credential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.w(TAG, "onVerificationFailed", e);
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    Snackbar.make(findViewById(android.R.id.content), e.toString(),
                            Snackbar.LENGTH_SHORT).show();
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                            Snackbar.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                Log.d(TAG, "onCodeSent:" + verificationId);
                mVerificationId = verificationId;
                mResendToken = token;
            }
        };


    }
    @Override
    protected void onStart()
    {
        super.onStart();

        if(!phoneNumber.isEmpty())
        {
            startPhoneNumberVerification(phoneNumber);
        }
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
    {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull final Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            {
                                mUsers.addListenerForSingleValueEvent(new ValueEventListener()
                                {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot)
                                    {

                                        if(dataSnapshot.hasChild(mAuth.getUid()))
                                        {
                                            startActivity(new Intent(getBaseContext(),activityHomeMain.class).
                                                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                        }
                                        else
                                        {
                                            startActivity(new Intent(activityUserConfirmation.this, activitySetupProfile.class).
                                                    putExtra( "Phone", phoneNumber.toString() )
                                                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError)
                                    {
                                    }
                                });
                            }
                        }
                        else
                        {
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {

                            }
                        }
                    }
                });
    }


    private void startPhoneNumberVerification(String phoneNumber)
    {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                0,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,         // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithPhoneAuthCredential(credential);
    }

    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,               // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,        // Activity (for callback binding)
                mCallbacks,        // OnVerificationStateChangedCallbacks
                token);           // ForceResendingToken from callbacks
    }

}

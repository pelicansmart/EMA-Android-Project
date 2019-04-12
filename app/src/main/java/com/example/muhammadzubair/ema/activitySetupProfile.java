package com.example.muhammadzubair.ema;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import static android.content.Intent.ACTION_GET_CONTENT;


public class activitySetupProfile extends AppCompatActivity {

    Uri result;
    private static final int GALLERY_REQUEST = 0;
    private DatabaseReference mUsersRef;
    private StorageReference mStorageRef;
    CircleImageView mProfileImage;
    EditText mFullName,mEmail;
    TextView mPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_profile);

        Intent i = getIntent();

        mFullName=findViewById(R.id.et_fullname);
        mEmail=findViewById(R.id.et_email);

        mPhone=findViewById(R.id.et_phone);
        mPhone.setText(i.getStringExtra("Phone"));

        ImageButton mProfilePicChange=findViewById(R.id.profileChange);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.bar_center_simple);
        TextView title=(TextView)findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        title.setText("Setup Profile");
        mProfileImage=findViewById(R.id.profile_image);
        Button BtnSaveDetails=findViewById(R.id.btn_save);
        mUsersRef = FirebaseDatabase.getInstance().getReference().child("objectUsers");
        mStorageRef = FirebaseStorage.getInstance().getReference().child("Photos").child(FirebaseAuth.getInstance().getUid()+ ".jpg");

        mProfilePicChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, GALLERY_REQUEST);
            }
        });

        BtnSaveDetails.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(mFullName.getText().toString().isEmpty()||mEmail.getText()
                        .toString().isEmpty()||mPhone.getText().toString().isEmpty()||result==null)
                {
                    Toast.makeText(activitySetupProfile.this,
                            "Please Complete Information with all fields and picture", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    checkEmail(mEmail.getText().toString());
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK)
        {

            if(requestCode == GALLERY_REQUEST)
            {
                result= data.getData();
                // filePath = getPath(result);
                mProfileImage.setImageURI(result);

            }

        }

    }


    private void checkEmail(final String userEmail)
    {
        final ArrayList<String> mEmailList=new ArrayList<>();
        mEmailList.clear();
        mUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren())
                {
                    String email=dataSnapshot1.child("userEmail").getValue(String.class);
                    mEmailList.add(email);
                }
                if(mEmailList.contains(userEmail))
                {
                    Toast.makeText(activitySetupProfile.this,
                            "Email already exists", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    updateUserPhoto();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void updateUserPhoto()
    {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading");
        progressDialog.setMessage("Please Wait");
        progressDialog.setIndeterminate(true);
        progressDialog.show();
        StorageMetadata metadata = new StorageMetadata.Builder() // Create file metadata with property to delete
                .setContentType(null)
                .setContentLanguage("en")
                .build();
        UploadTask uploadTask = mStorageRef.putFile(result);

        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>()
        {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful())
                {
                    throw task.getException();
                }
                return mStorageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>()
        {
            @Override
            public void onComplete(@NonNull Task<Uri> task)
            {
                if (task.isSuccessful())
                {
                    progressDialog.dismiss();
                    Uri downloadUri = task.getResult();
                    Map<String, Object> childUpdates = new HashMap<>();
                    childUpdates.put("fullName", mFullName.getText().toString().trim());
                    childUpdates.put("userEmail", mEmail.getText().toString().trim());
                    childUpdates.put("userPhone", mPhone.getText().toString().trim());
                    childUpdates.put("profilePicUrl", downloadUri.toString());
                    mUsersRef.child(FirebaseAuth.getInstance().getUid()).updateChildren(childUpdates);
                    startActivity(new Intent(getBaseContext(),activityHomeMain.class));
                    finish();
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(activitySetupProfile.this,
                            "Failed "+task.getException().getMessage(), Toast.LENGTH_SHORT).show();//error saving photo
                }
            }
        });
    }
}

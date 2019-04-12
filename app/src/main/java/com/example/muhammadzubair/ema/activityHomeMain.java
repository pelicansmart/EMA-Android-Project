package com.example.muhammadzubair.ema;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.muhammadzubair.ema.fragments.fragmentHome;
import com.example.muhammadzubair.ema.fragments.fragmentContacts;
import com.example.muhammadzubair.ema.fragments.fragmentEvents;
import com.example.muhammadzubair.ema.fragments.fragmentConfirmations;
import com.example.muhammadzubair.ema.fragments.fragmentProfile;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class activityHomeMain extends AppCompatActivity
{
    static
    {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListener;
    ViewPager viewPager;
    DatabaseReference mUsersRef;
    BottomNavigationView bottomNavigationView;
    MenuItem prevMenuItem;
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth=FirebaseAuth.getInstance();
        mUsersRef= FirebaseDatabase.getInstance().getReference().child("objectUsers");
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null)
                {
                    startActivity(new Intent(activityHomeMain.this, activityLoginSignup.class));
                    finish();
                }
            }
        };

        viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(4);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId())
                        {
                            case R.id.action_home:
                                getSupportActionBar().setTitle("Home");
                                viewPager.setCurrentItem(0);
                                break;
                            case R.id.action_contacts:
                                getSupportActionBar().setTitle("Contacts");
                                viewPager.setCurrentItem(1);
                                break;
                            case R.id.action_events:
                                getSupportActionBar().setTitle("Events");
                                viewPager.setCurrentItem(2);
                                break;
                            case R.id.action_confirmations:
                                getSupportActionBar().setTitle("Confirmations");
                                viewPager.setCurrentItem(3);
                                break;
                            case R.id.action_profile_details:
                                getSupportActionBar().setTitle("Profile");
                                viewPager.setCurrentItem(4);
                                break;
                        }
                        return false;
                    }
                });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {

            }

            @Override
            public void onPageSelected(int position) {
                if (prevMenuItem != null) {
                    prevMenuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }


                Log.d("page", "onPageSelected: " + position);
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
                prevMenuItem = bottomNavigationView.getMenu().getItem(position);


                if (position == 0)
                {
                    title.setText("Home");
                }

                else if(position==1)
                {
                    title.setText("Contacts");
                }
                else if(position==2)
                {
                    title.setText("Events");
                }
                else if(position==3)
                {
                    title.setText("Confirmations");
                }
                else
                {
                    title.setText("Profile");
                }

            }

            @Override
            public void onPageScrollStateChanged(int state)
            {

            }
        });

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.bar_center);
        title=(TextView)findViewById(getResources().getIdentifier("action_bar_title", "id", getPackageName()));
        title.setText("Home");
        setupViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_logout)
        {
            signOut();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setupViewPager(ViewPager viewPager)
    {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new fragmentHome());
        adapter.addFragment(new fragmentContacts());
        adapter.addFragment(new fragmentEvents());
        adapter.addFragment(new fragmentConfirmations());
        adapter.addFragment(new fragmentProfile());

        viewPager.setAdapter(adapter);
    }

    //sign out method
    public void signOut() {
        mAuth.signOut();
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            mAuth.removeAuthStateListener(authListener);
        }
    }
}
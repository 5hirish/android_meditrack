package com.alleviate.meditrack;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.alleviate.meditrack.adapter.SectionsPagerAdapter;
import com.alleviate.meditrack.constants.Constants;

import static com.alleviate.meditrack.constants.Constants.REQUEST_CODE;

public class DashboardActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    public static SectionsPagerAdapter mSectionsPagerAdapter;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_today:
                    mViewPager.setCurrentItem(0);
                    //setTitle(Constants.tab_title_today);

                    return true;
                case R.id.navigation_all:
                    mViewPager.setCurrentItem(1);
                    //setTitle(Constants.tab_title_all);

                    return true;
                case R.id.navigation_user:
                    mViewPager.setCurrentItem(2);
                    //setTitle(Constants.tab_title_user);

                    return true;
            }
            return false;
        }

    };

    private ViewPager.OnPageChangeListener mOnPageChangeListener
            = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container_fragments);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_sos) {

            SharedPreferences parent_alarm_sp = getSharedPreferences(Constants.sp_medi_file, MODE_PRIVATE);
            String sos_contact = parent_alarm_sp.getString(Constants.sp_sos_number, "911");


            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CODE);

            } else {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + sos_contact));
                startActivity(intent);
            }
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        int nav_page_id = mViewPager.getCurrentItem();
        if (nav_page_id == 2) {
            mViewPager.setCurrentItem(1);
        } else if (nav_page_id == 1) {
            mViewPager.setCurrentItem(0);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    SharedPreferences parent_alarm_sp = getSharedPreferences(Constants.sp_medi_file, MODE_PRIVATE);
                    String sos_contact = parent_alarm_sp.getString(Constants.sp_sos_number, "911");

                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + sos_contact));

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        startActivity(intent);
                    }

                } else {

                    Toast.makeText(getApplicationContext()," To make SOS calls, Grant permission.",Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}

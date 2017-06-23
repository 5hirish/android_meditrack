package com.alleviate.meditrack;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.alleviate.meditrack.adapter.SectionsPagerAdapter;

public class DashboardActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_today:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_all:
                    mViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_user:
                    mViewPager.setCurrentItem(2);
                    return true;
            }
            return false;
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

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onBackPressed() {
        int nav_page_id = mViewPager.getCurrentItem();
        if (nav_page_id == 2){
            mViewPager.setCurrentItem(1);
        } else if (nav_page_id == 1){
            mViewPager.setCurrentItem(0);
        } else {
            super.onBackPressed();
        }
    }


}

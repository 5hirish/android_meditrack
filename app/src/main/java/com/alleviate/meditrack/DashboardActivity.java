package com.alleviate.meditrack;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.alleviate.meditrack.adapter.SectionsPagerAdapter;
import com.alleviate.meditrack.constants.Constants;

public class DashboardActivity extends AppCompatActivity {

    private ViewPager mViewPager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_today:
                    mViewPager.setCurrentItem(0);
                    setTitle(Constants.tab_title_today);
                    search_meds.setVisible(false);

                    return true;
                case R.id.navigation_all:
                    mViewPager.setCurrentItem(1);
                    setTitle(Constants.tab_title_today);
                    search_meds.setVisible(true);
                    return true;
                case R.id.navigation_user:
                    mViewPager.setCurrentItem(2);
                    setTitle(Constants.tab_title_today);
                    search_meds.setVisible(false);
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
            if (mViewPager.getCurrentItem() == 1){
                search_meds.setVisible(true);
            } else {
                search_meds.setVisible(false);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container_fragments);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);

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

    private MenuItem search_meds;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        search_meds = menu.findItem(R.id.all_search);
        if (mViewPager.getCurrentItem() == 1){
            search_meds.setVisible(true);
        } else {
            search_meds.setVisible(false);
        }                                                              // Hide the Time Based reminder icon

        return true;
    }



}

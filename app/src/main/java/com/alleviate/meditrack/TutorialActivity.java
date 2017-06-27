package com.alleviate.meditrack;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.alleviate.meditrack.fragments.TutorialAFragment;
import com.alleviate.meditrack.fragments.TutorialBFragment;
import com.alleviate.meditrack.fragments.TutorialCFragment;


public class TutorialActivity extends AppCompatActivity{

    static int num_pages = 3;
    static ViewPager viewPager;
    PagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        viewPager = (ViewPager) findViewById(R.id.tutorial_view);
        pagerAdapter = new TutorialPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    public static void flipPage() {
        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
    }

    private class TutorialPagerAdapter extends FragmentStatePagerAdapter {
        TutorialPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            Fragment tutorial_frag = new TutorialAFragment();

            switch (position){
                case 0:
                    tutorial_frag = new TutorialAFragment();
                    break;
                case 1:
                    tutorial_frag = new TutorialBFragment();
                    break;
                case 2:
                    tutorial_frag = new TutorialCFragment();
                    break;
                default:
                    tutorial_frag = new TutorialAFragment();
                    break;
            }
            return tutorial_frag;
        }

        @Override
        public int getCount() {
            return num_pages;
        }
    }
}

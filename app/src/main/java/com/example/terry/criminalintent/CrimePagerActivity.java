package com.example.terry.criminalintent;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;
import java.util.UUID;

/**
 * Created by Terry on 9/29/2017.
 */

public class CrimePagerActivity extends AppCompatActivity{
    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    private static final String EXTRA_CRIME_ID = "com.example.terry.criminalintent.crime_id";
    private Button mFirstButton;
    private Button mLastButton;

    public static Intent newIntent(final Context packageContext, UUID crimeId){
        Intent intent = new Intent(packageContext, CrimePagerActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_pager);
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        mViewPager = (ViewPager) findViewById(R.id.crime_view_pager);
        mCrimes = CrimeLab.get(this).getCrime();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
              Crime crime = mCrimes.get(position);
                return CrimeFragment.newInstance(crime.getId());

            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
       for( int i = 0; i < mCrimes.size(); i++){
           if(mCrimes.get(i).getId().equals(crimeId)){
               mViewPager.setCurrentItem(i);
               break;
           }
       }
       mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
           @Override
           public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
               // Do nothing
           }

           @Override
           public void onPageSelected(int position) {
               mFirstButton.setEnabled(position != 0);
               mLastButton.setEnabled(position != (mCrimes.size() - 1));
           }

           @Override
           public void onPageScrollStateChanged(int state) {
               // Do nothing
           }
       });
       mFirstButton = (Button) findViewById(R.id.first_crime);
        mFirstButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mViewPager.setCurrentItem(0);
            }
        });
        mLastButton = (Button) findViewById(R.id.last_crime);
        mLastButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewPager.setCurrentItem(mCrimes.size()-1);
            }
        });
    }

}

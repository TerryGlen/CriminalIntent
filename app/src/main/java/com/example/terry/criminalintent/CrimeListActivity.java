package com.example.terry.criminalintent;

import android.support.v4.app.Fragment;

/**
 * Created by Terry on 9/27/2017.
 */

public class CrimeListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}

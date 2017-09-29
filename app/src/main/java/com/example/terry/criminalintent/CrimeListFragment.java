package com.example.terry.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Terry on 9/27/2017.
 */

public class CrimeListFragment extends Fragment {
    private RecyclerView mCrimerecyclerView;
    private CrimeAdapter mAdapter;

    //Intialized to - 1 to easily check if position has been updated
    private int mLastUpdatedPosition = -1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimerecyclerView = (RecyclerView) view.findViewById(R.id.crime_recycler_view);
        mCrimerecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        updateUI();
        return view;

    }
    @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }
    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes = crimeLab.getCrime();
        if(mAdapter == null){
            mAdapter = new CrimeAdapter(crimes);
            mCrimerecyclerView.setAdapter(mAdapter);
        }else{
            if (mLastUpdatedPosition > -1) {
                mAdapter.notifyItemChanged(mLastUpdatedPosition);
                mLastUpdatedPosition = -1;
            } else {
                mAdapter.notifyDataSetChanged();
            }
        }

    }



    private class AbstractCrimeHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private Crime mCrime;
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private ImageView mSolvedImageView;
        private DateFormat mDateFormat = new SimpleDateFormat("EEE, MMM d, yyyy");

        public AbstractCrimeHolder(LayoutInflater inflater, ViewGroup parent, int layoutId){
            super(inflater.inflate(layoutId, parent, false));
            itemView.setOnClickListener(this);


            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date);
            mSolvedImageView =(ImageView) itemView.findViewById(R.id.crime_solved);


        }


        @Override
        public void onClick(View view) {
            Toast.makeText(getActivity(), mCrime.getTitle() + " clicked!", Toast.LENGTH_SHORT)
                    .show();
            Intent intent = CrimeActivity.newIntent(getActivity(),mCrime.getId());
            mLastUpdatedPosition = this.getAdapterPosition(); //Challenge: Efficient RecyclerView Reloading
            startActivity(intent);
        }

        public void bind(Crime crime) {
            mCrime = crime;

            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mDateFormat.format(mCrime.getDate()));
            mSolvedImageView.setVisibility(crime.isSolved() ? View.VISIBLE : View.GONE);
        }
    }
    private class CrimeHolder extends AbstractCrimeHolder{
        public CrimeHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater, parent, R.layout.list_item_crime);

        }
    }
    private class PoliceCrimeHolder extends AbstractCrimeHolder{
        public PoliceCrimeHolder(LayoutInflater inflater, ViewGroup parent){
            super(inflater, parent, R.layout.list_item_crime_police);
        }
    }
    private class CrimeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Crime> mCrimes;
        private static final int LIST_ITEM_CRIME = 0;
        private static final int LIST_ITEM_CRIME_POLICE = 1;

        public CrimeAdapter(List<Crime> crimes) {
            mCrimes = crimes;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            if(viewType == LIST_ITEM_CRIME){
                return new CrimeHolder(layoutInflater, parent);
            }
            else if(viewType == LIST_ITEM_CRIME_POLICE){
                return new PoliceCrimeHolder(layoutInflater, parent);
            }else{
                return null;
            }

        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            if (holder instanceof CrimeHolder) {
                ((CrimeHolder) holder).bind(crime);
            } else if (holder instanceof PoliceCrimeHolder) {
                ((PoliceCrimeHolder) holder).bind(crime);
            }
        }

        @Override
        public int getItemCount() {
            return mCrimes.size();
        }
        public int getItemViewType(int position){
            int return_value;
            boolean requiresPolice = mCrimes.get(position).isRequiresPolice();
            return_value = requiresPolice ? LIST_ITEM_CRIME_POLICE : LIST_ITEM_CRIME;
            return return_value;
        }

    }

}

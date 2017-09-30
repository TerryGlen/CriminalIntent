package com.example.terry.criminalintent;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static android.os.Build.VERSION.SDK_INT;

/**
 * Created by Terry on 9/29/2017.
 */

public class TimePickerFragment extends DialogFragment {
    private static final String ARG_DATE = "Date";
    public static final String EXTRA_TIME = "com.example.terry.criminalintent";
    private TimePicker mTimePicker;
    private Calendar mCalendar;

    public static TimePickerFragment newInstance(Date date){
        Bundle args = new Bundle();
        args.putSerializable(ARG_DATE,  date);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;

    }

    public Dialog onCreateDialog(Bundle savedTnstanceState){
        Date date = (Date) getArguments().getSerializable(ARG_DATE);
        mCalendar = Calendar.getInstance();
        mCalendar.setTime(date);

        int hour = mCalendar.get(Calendar.HOUR);
        int minute = mCalendar.get(Calendar.MINUTE);

        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_time, null);
        mTimePicker = (TimePicker) v.findViewById(R.id.dialog_date_picker);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            mTimePicker.setHour(hour);
            mTimePicker.setMinute(minute);
        }else{
            mTimePicker.setCurrentHour(hour);
            mTimePicker.setCurrentMinute(minute);
        }

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.date_picker_title)
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                int hour , minute;

                                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                                    hour = mTimePicker.getHour();
                                    minute = mTimePicker.getMinute();
                                }
                                else {
                                    hour = mTimePicker.getCurrentHour();
                                    minute = mTimePicker.getCurrentMinute();
                                }

                                mCalendar.set(Calendar.HOUR_OF_DAY, hour);
                                mCalendar.set(Calendar.MINUTE, minute);

                                sendResult(Activity.RESULT_OK, mCalendar.getTime());
                            }
                        })
                .create();
    }
    private void sendResult(int resultCode, Date date){
        if(getTargetFragment() == null){
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME, date);
        getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);

    }
    }


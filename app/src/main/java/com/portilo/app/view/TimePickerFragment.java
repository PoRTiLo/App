package com.portilo.app.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import com.portilo.app.AddNewRecordActivity;

import java.util.Calendar;
import java.util.StringTokenizer;

public class TimePickerFragment extends DialogFragment {

  private static final String MOVE_IN_DATE_KEY = "time";

  private int hour;
  private int minute;

  public static TimePickerFragment newInstance(String time) {
    TimePickerFragment pickerFragment = new TimePickerFragment();

    //Pass the date in a bundle.
    Bundle bundle = new Bundle();
    bundle.putSerializable(MOVE_IN_DATE_KEY, time);
    pickerFragment.setArguments(bundle);
    return pickerFragment;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {

    String initialTime = (String) getArguments().getSerializable(MOVE_IN_DATE_KEY);

    if (initialTime != null) {
      StringTokenizer tokens = new StringTokenizer(initialTime, ":");
      if (tokens.countTokens() == 2) {
        hour = Integer.parseInt(tokens.nextToken().trim());
        minute = Integer.parseInt(tokens.nextToken().trim());
      } else {
        setActualTime();
      }
    } else {
      setActualTime();
    }

    // Create a new instance of TimePickerDialog and return it
    return new TimePickerDialog(getActivity(), (AddNewRecordActivity) getActivity(), hour, minute, DateFormat.is24HourFormat(getActivity()));
  }

  // Use the current time as the default values for the picker
  private void setActualTime() {
    final Calendar c = Calendar.getInstance();
    hour = c.get(Calendar.HOUR_OF_DAY);
    minute = c.get(Calendar.MINUTE);
  }
}

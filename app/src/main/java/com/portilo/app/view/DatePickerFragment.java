package com.portilo.app.view;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import com.portilo.app.AddNewRecordActivity;

import java.util.Calendar;
import java.util.StringTokenizer;

public class DatePickerFragment extends DialogFragment  {

  private static final String MOVE_IN_DATE_KEY = "date";

  private int year;
  private int month;
  private int day;

  public static DatePickerFragment newInstance(String date) {
    DatePickerFragment pickerFragment = new DatePickerFragment();

    //Pass the date in a bundle.
    Bundle bundle = new Bundle();
    bundle.putSerializable(MOVE_IN_DATE_KEY, date);
    pickerFragment.setArguments(bundle);
    return pickerFragment;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    // Use the current date as the default date in the picker

    String initialDate = (String) getArguments().getSerializable(MOVE_IN_DATE_KEY);
    if (initialDate != null) {
      StringTokenizer tokens = new StringTokenizer(initialDate, ".");
      if (tokens.countTokens() == 3) {
        day = Integer.parseInt(tokens.nextToken().trim());
        month = Integer.parseInt(tokens.nextToken().trim()) - 1;
        year = Integer.parseInt(tokens.nextToken().trim());
      } else {
        setActualDate();
      }
    } else {
      setActualDate();
    }

    // Create a new instance of DatePickerDialog and return it
    return new DatePickerDialog(getActivity(), (AddNewRecordActivity) getActivity(), year, month, day);
  }

  private void setActualDate() {
    final Calendar c = Calendar.getInstance();
    year = c.get(Calendar.YEAR);
    month = c.get(Calendar.MONTH);
    day = c.get(Calendar.DAY_OF_MONTH);
  }
}
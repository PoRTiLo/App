package com.portilo.app.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

import com.portilo.app.AddNewItemActivity;
import com.portilo.app.R;

import java.util.Calendar;

public class TimePickerFragment extends DialogFragment {

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    // Use the current time as the default values for the picker
    final Calendar c = Calendar.getInstance();
    int hour = c.get(Calendar.HOUR_OF_DAY);
    int minute = c.get(Calendar.MINUTE);

    // Create a new instance of TimePickerDialog and return it
    return new TimePickerDialog(getActivity(), (AddNewItemActivity) getActivity(), hour, minute, DateFormat.is24HourFormat(getActivity()));
  }
}

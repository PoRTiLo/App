package com.portilo.app;

/**
 * Created by HC on 15.02.2015.
 *
 */

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.portilo.app.model.Record;
import com.portilo.app.model.Vehicle;
import com.portilo.app.view.DatePickerFragment;
import com.portilo.app.view.TimePickerFragment;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Calendar;

@EActivity(R.layout.activity_add_new_item)
public class AddNewRecordActivity extends ActionBarActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

  @ViewById(R.id.timeButton)
  Button dateButton;

  @ViewById(R.id.dateButton)
  Button timeButton;

  @ViewById(R.id.odometerEditText)
  EditText odometerEditText;

  @ViewById(R.id.tankEditText)
  EditText tankEditText;

  @ViewById(R.id.volumeEditText)
  EditText volumeEditText;

  @ViewById(R.id.locationEditText)
  EditText locationEditText;

  private Record mRecord;
  private Calendar calendar;
  private static final String LOGGER = "AddNewRecordActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Intent intent = getIntent();
    mRecord = intent.getParcelableExtra(RecordsFragment.UPDATE_RECORD.toString());
    init();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.menu_add_new_item, menu);
    ActionBar actionBar = getSupportActionBar();
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    actionBar.setDisplayShowTitleEnabled(true);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    //noinspection SimplifiableIfStatement
    if (id == R.id.menu_record_save) {
      // send update date back
      if (!fillRecordFromEditor()) {
        showErrors();
      } else {
        Intent dataBackIntent = new Intent();
        dataBackIntent.putExtra(RecordsFragment.CREATE_RECORD.toString(), mRecord);
        Log.i(LOGGER, mRecord.toString());
        setResult(Activity.RESULT_OK, dataBackIntent);

        Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.record_added), Toast.LENGTH_SHORT);
        toast.show();
        finish();
      }
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void showErrors() {
    Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.required_warning), Toast.LENGTH_SHORT);
    toast.show();

    if (odometerEditText.getText().toString().isEmpty()) {
      odometerEditText.setError(getString(R.string.required));
    }

    if (tankEditText.getText().toString().isEmpty()) {
      tankEditText.setError(getString(R.string.required));
    }

    if (volumeEditText.getText().toString().isEmpty()) {
      volumeEditText.setError(getString(R.string.required));
    }
  }

  @AfterViews
  protected void init() {
    volumeEditText.setError(volumeEditText.getText().toString().length() <= 0 ? getString(R.string.required) : null);
    volumeEditText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (volumeEditText.getText().length() <= 0 || volumeEditText.getText() == null ||
                volumeEditText.getText().toString().trim().equalsIgnoreCase("")) {
          volumeEditText.setError(getString(R.string.required));
        } else {
          volumeEditText.setError(null);
        }
      }
      @Override
      public void afterTextChanged(Editable s) {}

    });

    tankEditText.setText(Vehicle.getInstance(getParent()).getTankVolume().toString());
    tankEditText.setError(tankEditText.getText().toString().length() <= 0 ? getString(R.string.required) : null);
    tankEditText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (tankEditText.getText().toString().trim().equals("")) {
          tankEditText.setError(getString(R.string.required));
        } else {
          tankEditText.setError(null);
        }
      }
      @Override
      public void afterTextChanged(Editable s) {}
    });

    odometerEditText.setError(odometerEditText.getText().toString().length() <= 0 ? getString(R.string.required) : null);
    odometerEditText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (odometerEditText.getText().toString().trim().equals("")) {
          odometerEditText.setError(getString(R.string.required));
        } else {
          odometerEditText.setError(null);
        }
      }
      @Override
      public void afterTextChanged(Editable s) {}
    });

    calendar = Calendar.getInstance();
    if (mRecord != null) {
      locationEditText.setText(mRecord.getLocation());
      odometerEditText.setText(Integer.toString(mRecord.getOdometer()));
      tankEditText.setText(Double.toString(mRecord.getTank()));
      volumeEditText.setText(Double.toString(mRecord.getVolume()));
      calendar.setTimeInMillis(mRecord.getDate());
    } else {
      mRecord = new Record();
    }

    timeButton.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + (calendar.get(Calendar.MINUTE) > 9 ? calendar.get(Calendar.MINUTE) : "0" + calendar.get(Calendar.MINUTE)));
    dateButton.setText(calendar.get(Calendar.DAY_OF_MONTH) + ". " + (calendar.get(Calendar.MONTH) + 1) + ". " + calendar.get(Calendar.YEAR));
  }

  private boolean fillRecordFromEditor() {
    if (mRecord == null) {
      mRecord = new Record();
    }

    mRecord.setDate(calendar.getTimeInMillis());
    mRecord.setLocation(locationEditText.getText().toString());
    if (!odometerEditText.getText().toString().isEmpty()) {
      mRecord.setOdometer(Integer.valueOf(odometerEditText.getText().toString()));
    } else {
      return false;
    }

    if (!tankEditText.getText().toString().isEmpty()) {
      mRecord.setTank(Double.parseDouble(tankEditText.getText().toString()));
    } else {
      return false;
    }

    if (!volumeEditText.getText().toString().isEmpty()) {
      mRecord.setVolume(Double.parseDouble(volumeEditText.getText().toString()));
    } else {
      return false;
    }
    return true;
  }

  public void showTimePickerDialog(View v) {
    Button time = (Button) v.findViewById(R.id.timeButton);
    DialogFragment newFragment = TimePickerFragment.newInstance(time.getText().toString());
    newFragment.show(getFragmentManager(), "timePicker");
  }

  public void showDatePickerDialog(View v) {
    Button date = (Button) v.findViewById(R.id.dateButton);
    DialogFragment newFragment = DatePickerFragment.newInstance(date.getText().toString());
    newFragment.show(getFragmentManager(), "datePicker");
  }

  public void onDateSet(DatePicker view, int year, int month, int day) {
    dateButton.setText(day + ". " + (month + 1) + ". " + year);
    calendar.set(year, month, day);
  }

  public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
    timeButton.setText(hourOfDay + ":" + (minute > 9 ? minute : "0" + minute));
    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), hourOfDay, minute);
  }
}

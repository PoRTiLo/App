package com.portilo.app;

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

import com.portilo.app.model.Record;
import com.portilo.app.view.DatePickerFragment;
import com.portilo.app.view.TimePickerFragment;

import java.util.Calendar;
import java.util.Date;


public class AddNewItemActivity extends ActionBarActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

  private long id;
  private Button dateButton;
  private Button timeButton;
  private EditText odometerEditText;
  private EditText tankEditText;
  private EditText volumeEditText;
  private EditText locationEditText;

  private Record mRecord;
  private Calendar calendar;
  private static final String LOGGER = "AddNewItemActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_new_item);
    Intent intent = getIntent();
    mRecord = intent.getParcelableExtra(ItemFragment.UPDATE_RECORD.toString());
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
    if (id == R.id.menu_item_new) {
      // send update date back
      fillRecordFromEditor();
      Intent dataBackIntent = new Intent();
      dataBackIntent.putExtra(ItemFragment.CREATE_RECORD.toString(), mRecord);
      Log.i(LOGGER, mRecord.toString());
      setResult(Activity.RESULT_OK, dataBackIntent);

      finish();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  private void init() {
    timeButton = (Button) findViewById(R.id.timeButton);
    dateButton = (Button) findViewById(R.id.dateButton);
    locationEditText = (EditText) findViewById(R.id.locationEditText);
    odometerEditText = (EditText) findViewById(R.id.odometerEditText);
    volumeEditText = (EditText) findViewById(R.id.volumeEditText);
    tankEditText = (EditText) findViewById(R.id.tankEditText);

    volumeEditText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.i("aa",volumeEditText.getText().toString() );
        if (volumeEditText.getText().toString().trim().length() <= 0) {
          volumeEditText.setError("Volume is required!");
        } else {
          volumeEditText.setError(null);
        }
      }
      @Override
      public void afterTextChanged(Editable s) {
        Log.i("aa",volumeEditText.getText().toString() );
        if (volumeEditText.getText().toString().trim().length() <= 0) {
          volumeEditText.setError("Volume is required!");
        } else {
          volumeEditText.setError(null);
        }
      }
    });

    tankEditText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (tankEditText.getText().toString().trim().equals("")) {
          tankEditText.setError("Tank is required!");
        } else {
          tankEditText.setError(null);
        }
      }
      @Override
      public void afterTextChanged(Editable s) {}
    });

    odometerEditText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (odometerEditText.getText().toString().trim().equals("")) {
          odometerEditText.setError("Odometer is required!");
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

    //TODO;validace
    timeButton.setText(calendar.get(Calendar.HOUR_OF_DAY) + ":" + (calendar.get(Calendar.MINUTE) > 9 ? calendar.get(Calendar.MINUTE) : "0" + calendar.get(Calendar.MINUTE)));
    dateButton.setText(calendar.get(Calendar.DAY_OF_MONTH) + ". " + (calendar.get(Calendar.MONTH) + 1) + ". " + calendar.get(Calendar.YEAR));
  }

  private void fillRecordFromEditor() {
    if (mRecord == null) {
      mRecord = new Record();
    }

    mRecord.setDate(calendar.getTimeInMillis());
    mRecord.setLocation(locationEditText.getText().toString());
    mRecord.setOdometer(Integer.valueOf(odometerEditText.getText().toString()));
    mRecord.setTank(Double.parseDouble(tankEditText.getText().toString()));
    mRecord.setVolume(Double.parseDouble(volumeEditText.getText().toString()));
  }

  public void showTimePickerDialog(View v) {
    DialogFragment newFragment = new TimePickerFragment();
    newFragment.show(getFragmentManager(), "timePicker");
  }

  public void showDatePickerDialog(View v) {
    DialogFragment newFragment = new DatePickerFragment();
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

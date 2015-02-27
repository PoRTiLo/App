package com.portilo.app;

import android.app.Activity;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.portilo.app.model.Vehicle;


public class VehicleFragment extends Fragment {

  /**
   * Returns a new instance of this fragment for the given section number.
   */
  public static VehicleFragment newInstance() {
    return new VehicleFragment();
  }

  private EditText vehicleNameEditText;
  private EditText registrationEditText;
  private EditText initialOdometerEditText;
  private EditText initialVolumeEditText;
  private EditText tankVolumeEditText;

  public static final String PREFS_NAME = "vehicle";

  private boolean enableEdit = false;

  private Vehicle vehicle;

  public VehicleFragment() {}

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setHasOptionsMenu(true);
    vehicle = vehicle == null ? new Vehicle() : vehicle;
    loadVehicle();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_vehicle, container, false);
    init(view);
    return view;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    ((MainActivity) activity).onSectionAttached(1);
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.menu_vehicle, menu);
//    ((ActionBarActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string.title_section1));
    super.onCreateOptionsMenu(menu,inflater);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    // noinspection SimplifiableIfStatement
    if (id == R.id.menu_vehicle) {
      if (enableEdit) {
        if (validateAndFill()) {
          enableEdit = false;
          saveVehicle();
          disableEditTexts();
          item.setIcon(R.drawable.ic_action_edit);
          Toast toast = Toast.makeText(getActivity(), getString(R.string.vehicle_added), Toast.LENGTH_SHORT);
          toast.show();
        } else {
          showErrors();
          item.setIcon(R.drawable.ic_action_accept);
        }
      } else {
        enableEdit = true;
        disableEditTexts();
        item.setIcon(R.drawable.ic_action_accept);

      }
    }

    return super.onOptionsItemSelected(item);
  }

  private void loadVehicle() {
    SharedPreferences vehiclePref = getActivity().getSharedPreferences(PREFS_NAME, 0);
    vehicle.setVehicleName(vehiclePref.getString("vehicle", ""));
    vehicle.setInitialOdometer(vehiclePref.getFloat("initialOdometer", 0.0f));
    vehicle.setInitialVolume(vehiclePref.getFloat("initialVolume", 0.0f));
    vehicle.setRegistration(vehiclePref.getString("registration", ""));
    vehicle.setTankVolume(vehiclePref.getFloat("tankVolume", 0.0f));
  }

  private void showErrors() {
    Toast toast = Toast.makeText(getActivity(), getString(R.string.required_warning), Toast.LENGTH_SHORT);
    toast.show();

    if (TextUtils.isEmpty(vehicleNameEditText.getText())) {
      vehicleNameEditText.setError(getString(R.string.required));
    }

    if (TextUtils.isEmpty(registrationEditText.getText())) {
      registrationEditText.setError(getString(R.string.required));
    }

    if (TextUtils.isEmpty(initialOdometerEditText.getText())) {
      initialOdometerEditText.setError(getString(R.string.required));
    }

    if (TextUtils.isEmpty(initialVolumeEditText.getText())) {
      initialVolumeEditText.setError(getString(R.string.required));
    }

    if (TextUtils.isEmpty(tankVolumeEditText.getText())) {
      tankVolumeEditText.setError(getString(R.string.required));
    }
  }

  private boolean validateAndFill() {
    if (TextUtils.isEmpty(vehicleNameEditText.getText())) {
      vehicleNameEditText.setError(getString(R.string.required));
      return false;
    }
    vehicle.setVehicleName(vehicleNameEditText.getText().toString());

    if (TextUtils.isEmpty(registrationEditText.getText())) {
      registrationEditText.setError(getString(R.string.required));
      return false;
    }
    vehicle.setRegistration(registrationEditText.getText().toString());

    if (TextUtils.isEmpty(initialOdometerEditText.getText())) {
      initialOdometerEditText.setError(getString(R.string.required));
      return false;
    }
    vehicle.setInitialOdometer(Float.parseFloat(initialOdometerEditText.getText().toString()));

    if (TextUtils.isEmpty(initialVolumeEditText.getText())) {
      initialVolumeEditText.setError(getString(R.string.required));
      return false;
    }
    vehicle.setInitialVolume(Float.parseFloat(initialVolumeEditText.getText().toString()));

    if (TextUtils.isEmpty(tankVolumeEditText.getText())) {
      tankVolumeEditText.setError(getString(R.string.required));
      return false;
    }
    vehicle.setTankVolume(Float.parseFloat(tankVolumeEditText.getText().toString()));

    return true;
  }

  private void saveVehicle() {
    SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.putString("vehicle", vehicle.getVehicleName());
    editor.putString("registration", vehicle.getRegistration());
    editor.putFloat("initialOdometer", vehicle.getInitialOdometer());
    editor.putFloat("tankVolume", vehicle.getTankVolume());
    editor.putFloat("initialVolume", vehicle.getInitialVolume());

    // Commit the edits!
    editor.apply();
  }

  private void init(View view) {
    vehicleNameEditText = (EditText) view.findViewById(R.id.vehicleNameEditText);
    registrationEditText = (EditText) view.findViewById(R.id.registrationEditText);
    initialOdometerEditText = (EditText) view.findViewById(R.id.initialOdometerEditText);
    initialVolumeEditText = (EditText) view.findViewById(R.id.initialVolumeEditText);
    tankVolumeEditText = (EditText) view.findViewById(R.id.tankVolumeEditText);

    disableEditTexts();

    vehicleNameEditText.setText(vehicle.getVehicleName());
    vehicleNameEditText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        vehicleNameEditText.setError(TextUtils.isEmpty(vehicleNameEditText.getText()) ? getString(R.string.required) : null);
      }
      @Override
      public void afterTextChanged(Editable s) {}
    });

    registrationEditText.setText(vehicle.getRegistration());
    registrationEditText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        registrationEditText.setError(TextUtils.isEmpty(registrationEditText.getText()) ? getString(R.string.required) : null);
      }

      @Override
      public void afterTextChanged(Editable s) {
      }
    });

    initialOdometerEditText.setText(vehicle.getInitialOdometer().toString());
    initialOdometerEditText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        initialOdometerEditText.setError(TextUtils.isEmpty(initialOdometerEditText.getText()) ? getString(R.string.required) : null);
      }
      @Override
      public void afterTextChanged(Editable s) {}
    });

    initialVolumeEditText.setText(vehicle.getInitialVolume().toString());
    initialVolumeEditText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        initialVolumeEditText.setError(TextUtils.isEmpty(initialVolumeEditText.getText()) ? getString(R.string.required) : null);
      }
      @Override
      public void afterTextChanged(Editable s) {}
    });

    tankVolumeEditText.setText(vehicle.getTankVolume().toString());
    tankVolumeEditText.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
        tankVolumeEditText.setError(TextUtils.isEmpty(tankVolumeEditText.getText()) ? getString(R.string.required) : null);
      }
      @Override
      public void afterTextChanged(Editable s) {}
    });
  }

  private void disableEditTexts() {
    vehicleNameEditText.setEnabled(enableEdit);
    registrationEditText.setEnabled(enableEdit);
    initialOdometerEditText.setEnabled(enableEdit);
    initialVolumeEditText.setEnabled(enableEdit);
    tankVolumeEditText.setEnabled(enableEdit);
    vehicleNameEditText.setError(TextUtils.isEmpty(vehicleNameEditText.getText()) && vehicleNameEditText.isEnabled() ? getString(R.string.required) : null);
    registrationEditText.setError(TextUtils.isEmpty(registrationEditText.getText()) && registrationEditText.isEnabled() ? getString(R.string.required) : null);
    tankVolumeEditText.setError(TextUtils.isEmpty(tankVolumeEditText.getText()) && tankVolumeEditText.isEnabled() ? getString(R.string.required) : null);
    initialVolumeEditText.setError(TextUtils.isEmpty(initialVolumeEditText.getText()) && initialVolumeEditText.isEnabled() ? getString(R.string.required) : null);
    initialOdometerEditText.setError(TextUtils.isEmpty(initialOdometerEditText.getText()) && initialOdometerEditText.isEnabled() ? getString(R.string.required) : null);
  }
}

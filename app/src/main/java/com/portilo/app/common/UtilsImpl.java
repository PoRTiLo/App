package com.portilo.app.common;

import android.app.Activity;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.portilo.app.VehicleFragment;
import com.portilo.app.model.Vehicle;

/**
 * Created by HC on 15.02.2015.
 *
 */

public class UtilsImpl implements Utils {

  private static final String PREFS_NAME = "vehicle";

  public boolean vehicleExist(Activity activity) {
    SharedPreferences vehiclePref = activity.getSharedPreferences(PREFS_NAME, 0);
    String vehicleName = vehiclePref.getString("vehicle", "");
    return !TextUtils.isEmpty(vehicleName);
  }

  @Override
  public Vehicle loadVehicle(Activity activity) {
    SharedPreferences vehiclePref = activity.getSharedPreferences(PREFS_NAME, 0);
    Vehicle vehicle = new Vehicle();
    vehicle.setVehicleName(vehiclePref.getString("vehicle", ""));
    vehicle.setInitialOdometer(vehiclePref.getInt("initialOdometer", 0));
    vehicle.setInitialVolume(vehiclePref.getFloat("initialVolume", 0.0f));
    vehicle.setRegistration(vehiclePref.getString("registration", ""));
    vehicle.setTankVolume(vehiclePref.getFloat("tankVolume", 0.0f));

    return vehicle;
  }

  @Override
  public void saveVehicle(Activity activity, Vehicle vehicle) {
    SharedPreferences settings = activity.getSharedPreferences(PREFS_NAME, 0);
    SharedPreferences.Editor editor = settings.edit();
    editor.putString("vehicle", vehicle.getVehicleName());
    editor.putString("registration", vehicle.getRegistration());
    editor.putInt("initialOdometer", vehicle.getInitialOdometer());
    editor.putFloat("tankVolume", vehicle.getTankVolume());
    editor.putFloat("initialVolume", vehicle.getInitialVolume());

    // Commit the edits!
    editor.apply();
  }

  @Override
  public boolean notNull(Object obj) {
    return obj != null ? true : false;
  }
}

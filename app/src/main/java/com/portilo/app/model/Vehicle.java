package com.portilo.app.model;

/**
 * Created by HC on 15.02.2015.
 *
 */

import android.app.Activity;
import android.content.SharedPreferences;

public class Vehicle {
  private String vehicleName;
  private String registration;
  private Integer initialOdometer;
  private Float initialVolume;
  private Float tankVolume;

  private static Vehicle vehicle;

  private static final String PREFS_NAME = "vehicle";

  public static Vehicle getInstance(Activity activity) {
    if (vehicle == null) {
      vehicle = new Vehicle();
      loadVehicle(activity, vehicle);
    }
    return vehicle;
  }

  private Vehicle() {
  }

  private static void loadVehicle(Activity activity, Vehicle vehicle) {
    SharedPreferences vehiclePref = activity.getSharedPreferences(PREFS_NAME, 0);
    vehicle.setVehicleName(vehiclePref.getString("vehicle", ""));
    vehicle.setInitialOdometer(vehiclePref.getInt("initialOdometer", 0));
    vehicle.setInitialVolume(vehiclePref.getFloat("initialVolume", 0.0f));
    vehicle.setRegistration(vehiclePref.getString("registration", ""));
    vehicle.setTankVolume(vehiclePref.getFloat("tankVolume", 0.0f));
  }

  public void saveVehicle(Activity activity) {
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

  public void setVehicleName(String vehicleName) {
    this.vehicleName = vehicleName;
  }

  public void setRegistration(String registration) {
    this.registration = registration;
  }

  public void setInitialOdometer(Integer initialOdometer) {
    this.initialOdometer = initialOdometer;
  }

  public void setInitialVolume(Float initialVolume) {
    this.initialVolume = initialVolume;
  }

  public void setTankVolume(Float tankVolume) {
    this.tankVolume = tankVolume;
  }

  public String getVehicleName() {
    return vehicleName;
  }

  public String getRegistration() {
    return registration;
  }

  public Integer getInitialOdometer() {
    return initialOdometer;
  }

  public Float getInitialVolume() {
    return initialVolume;
  }

  public Float getTankVolume() {
    return tankVolume;
  }
}

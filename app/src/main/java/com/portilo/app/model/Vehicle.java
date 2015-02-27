package com.portilo.app.model;

import android.widget.EditText;

/**
 * Created by portilo on 2/26/2015.
 */
public class Vehicle {
  private String vehicleName;
  private String registration;
  private Float initialOdometer;
  private Float initialVolume;
  private Float tankVolume;

  public void setVehicleName(String vehicleName) {
    this.vehicleName = vehicleName;
  }

  public void setRegistration(String registration) {
    this.registration = registration;
  }

  public void setInitialOdometer(Float initialOdometer) {
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

  public Float getInitialOdometer() {
    return initialOdometer;
  }

  public Float getInitialVolume() {
    return initialVolume;
  }

  public Float getTankVolume() {
    return tankVolume;
  }
}

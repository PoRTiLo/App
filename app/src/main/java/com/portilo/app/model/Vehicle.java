package com.portilo.app.model;

/**
 * Created by HC on 15.02.2015.
 *
 */

import android.widget.EditText;

public class Vehicle {
  private String vehicleName;
  private String registration;
  private Integer initialOdometer;
  private Float initialVolume;
  private Float tankVolume;

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

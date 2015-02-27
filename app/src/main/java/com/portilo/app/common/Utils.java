package com.portilo.app.common;


import android.app.Activity;

import com.portilo.app.model.Vehicle;

public interface Utils {

  boolean vehicleExist(Activity activity);

  Vehicle loadVehicle(Activity activity);

  void saveVehicle(Activity activity, Vehicle vehicle);

  boolean notNull(Object obj);
}

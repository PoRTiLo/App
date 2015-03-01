package com.portilo.app.common;

/**
 * Created by HC on 15.02.2015.
 *
 */

import android.app.Activity;

import com.portilo.app.model.Vehicle;

public interface Utils {

  boolean vehicleExist(Activity activity);

  Vehicle loadVehicle(Activity activity);

  void saveVehicle(Activity activity, Vehicle vehicle);

  boolean notNull(Object obj);
}

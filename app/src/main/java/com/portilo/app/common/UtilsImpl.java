package com.portilo.app.common;

import android.app.Activity;
import android.content.SharedPreferences;
import android.text.TextUtils;

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

  public static String round(Double number) {
    return String.format("%.2f", number);
  }
}

package com.portilo.app.db;

/**
 * Created by HC on 11.01.2015.
 *
 *  This class is responsible for creating the database. The onUpgrade() method will simply delete
 *  all existing data and re-create the table. It also defines several constants for the table name
 *  and the table columns.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteHelper extends SQLiteOpenHelper {

  private static final String DATABASE_NAME = "tankovani.db";
  private static final int DATABASE_VERSION = 3;

  public static final String TABLE_FUELING = "fueling";

  public static final String COLUMN_NAME_ID       = "_id";      // index
  public static final String COLUMN_NAME_DATE     = "date";     // datum a čas
  public static final String COLUMN_NAME_LOCATION = "location"; // místo
  public static final String COLUMN_NAME_VOLUME   = "volume";   // natankováno l
  public static final String COLUMN_NAME_TANK     = "tank";     // stav l
  public static final String COLUMN_NAME_ODOMETER = "odometer"; // stav km

  // Database creation sql statement
  private static final String DATABASE_CREATE = "create table "
          + TABLE_FUELING        + "("
          + COLUMN_NAME_ID       + " integer primary key autoincrement, "
          + COLUMN_NAME_DATE     + " integer, "
          + COLUMN_NAME_LOCATION + " text, "
          + COLUMN_NAME_VOLUME   + " real, "
          + COLUMN_NAME_TANK     + " real, "
          + COLUMN_NAME_ODOMETER + " integer);";

  public MySQLiteHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase database) {
    database.execSQL(DATABASE_CREATE);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.w(MySQLiteHelper.class.getName(),
            "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_FUELING);
    onCreate(db);
  }
}
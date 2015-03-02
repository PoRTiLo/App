package com.portilo.app.persistence;

/**
 * Created by HC on 11.01.2015.
 *
 * This class is our DAO. It maintains the database connection and supports adding new comments and
 * fetching all comments.
 */

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.portilo.app.model.Consumption;
import com.portilo.app.model.Record;
import com.portilo.app.model.Vehicle;

public class RecordsDataSource {

  private static final String LOGGER = "RecordsDataSource";

  // Database fields
  private SQLiteDatabase database;
  private MySQLiteHelper dbHelper;
  private String[] allColumns = { MySQLiteHelper.COLUMN_NAME_ID,
          MySQLiteHelper.COLUMN_NAME_DATE,
          MySQLiteHelper.COLUMN_NAME_LOCATION,
          MySQLiteHelper.COLUMN_NAME_VOLUME,
          MySQLiteHelper.COLUMN_NAME_TANK,
          MySQLiteHelper.COLUMN_NAME_ODOMETER};

  // TODO: get max records
  private final static int MAX_RECORDS = 40;


  public RecordsDataSource(Context context) {
    dbHelper = new MySQLiteHelper(context);
  }

  public void open() throws SQLException {

    // Get reference to writable DB
    database = dbHelper.getWritableDatabase();
  }

  public void close() {
    dbHelper.close();
  }

  public Record createRecord(Record record) {

    ContentValues values = mapRecord2ORM(record);

    // Insert
    long insertId = database.insert(MySQLiteHelper.TABLE_FUELING, // table
            null,                         // nullColumnHack
            values);                      // key/value

    Cursor cursor = database.query(MySQLiteHelper.TABLE_FUELING,
            allColumns, MySQLiteHelper.COLUMN_NAME_ID + " = " + insertId, null,
            null, null, null);
    cursor.moveToFirst();
    Record newRecord = cursorToRecord(cursor);
    cursor.close();
    return newRecord;
  }

  private ContentValues mapRecord2ORM(Record record) {
    // Create ContentValues to add key "column"/value
    ContentValues values = new ContentValues();

    values.put(MySQLiteHelper.COLUMN_NAME_DATE, record.getDate()); // get date
    values.put(MySQLiteHelper.COLUMN_NAME_LOCATION, record.getLocation());   // get location
    values.put(MySQLiteHelper.COLUMN_NAME_VOLUME, record.getVolume());       // get volume
    values.put(MySQLiteHelper.COLUMN_NAME_TANK, record.getTank());           // get tank
    values.put(MySQLiteHelper.COLUMN_NAME_ODOMETER, record.getOdometer());   // get odometer

    return values;
  }

  public int updateRecord(Record record) {

    ContentValues values = mapRecord2ORM(record);

    long id = record.getId();
    Log.i(LOGGER, "Record update with id: " + id);
    return database.update(MySQLiteHelper.TABLE_FUELING, values, MySQLiteHelper.COLUMN_NAME_ID + " = " + id, null);
  }

  public int deleteRecord(Record record) {
    long id = record.getId();
    System.out.println("Record deleted with id: " + id);
    return database.delete(MySQLiteHelper.TABLE_FUELING, MySQLiteHelper.COLUMN_NAME_ID + " = " + id, null);
  }

  public List<Record> getAllRecords(Activity activity) {
    List<Record> records = new ArrayList<>();

    Cursor cursor = database.query(MySQLiteHelper.TABLE_FUELING,
            allColumns, null, null, null, null, MySQLiteHelper.COLUMN_NAME_DATE + " ASC");

    cursor.moveToFirst();
    int numberRecords = 0;
    Record previous = new Record();
    Vehicle vehicle = Vehicle.getInstance(activity);
    if (vehicle != null) {
      previous.setOdometer(vehicle.getInitialOdometer() == null ? 0 : vehicle.getInitialOdometer());
      previous.setTank(vehicle.getInitialVolume() == null ? 0.0 : vehicle.getInitialVolume());
    }
    while (!cursor.isAfterLast() && numberRecords < MAX_RECORDS) {
      Record record = cursorToRecord(cursor);
      record.setPreviousRecord(previousRecord(previous));
      records.add(record);
      previous = record;
      numberRecords++;
      cursor.moveToNext();
    }

    // Make sure to close the cursor
    cursor.close();
    return records;
  }

  private Record previousRecord(Record previous) {
    return previous == null ? null : previous;
  }

  private Record cursorToRecord(Cursor cursor) {
    Record record = new Record();

    record.setId(cursor.getLong(0));
    record.setDate(cursor.getLong(1));
    record.setLocation(cursor.getString(2));
    record.setVolume(cursor.getDouble(3));
    record.setTank(cursor.getDouble(4));
    record.setOdometer(cursor.getInt(5));

    return record;
  }

  public Double totalVolume(Activity activity) {
    Cursor cursor = database.query(MySQLiteHelper.TABLE_FUELING, new String[]{MySQLiteHelper.COLUMN_NAME_VOLUME}, null, null, null, null, null);

    cursor.moveToFirst();
    Double totalVolume = 0.0;
    while (!cursor.isAfterLast()) {
      Double volume = cursor.getDouble(cursor.getColumnIndex(MySQLiteHelper.COLUMN_NAME_VOLUME));
      totalVolume += volume;
      cursor.moveToNext();
    }
    totalVolume += Vehicle.getInstance(activity).getInitialVolume();
    // Make sure to close the cursor
    cursor.close();
    return totalVolume;
  }

  public Double minimalVolume() {
    Cursor cursor = database.query(MySQLiteHelper.TABLE_FUELING,
            new String[]{ "min(" + MySQLiteHelper.COLUMN_NAME_VOLUME + ")" }, null, null, null, null, null);
    cursor.moveToFirst();
    Double minVolume = !cursor.isAfterLast() ? cursor.getDouble(0) : null;
    cursor.close();
    return minVolume;
  }

  public Double maximalVolume() {
    Cursor cursor = database.query(MySQLiteHelper.TABLE_FUELING,
            new String[]{ "max(" + MySQLiteHelper.COLUMN_NAME_VOLUME + ")" }, null, null, null, null, null);
    cursor.moveToFirst();
    Double maxVolume = !cursor.isAfterLast() ? cursor.getDouble(0) : null;
    // Make sure to close the cursor
    cursor.close();
    return maxVolume;
  }

  public Long totalVolumes() {
    String sql = "SELECT COUNT(*) FROM " + MySQLiteHelper.TABLE_FUELING;
    SQLiteStatement statement = database.compileStatement(sql);
    return statement.simpleQueryForLong();
  }

  public Record getNewestRecord() {
    String sql = "SELECT * FROM " + MySQLiteHelper.TABLE_FUELING + " ORDER BY " + MySQLiteHelper.COLUMN_NAME_DATE + " DESC LIMIT 1;";
    Cursor cursor = database.rawQuery(sql, null);
    cursor.moveToFirst();
    Record record = !cursor.isAfterLast() ? cursorToRecord(cursor) : null;
    cursor.close();
    return record;
  }

  public Double getMinimalConsumption(Activity activity) {
    List<Record> records = getAllRecords(activity);
    Double minimalConsumption = 0.0;
    Vehicle vehicle = Vehicle.getInstance(activity);
    if (vehicle == null) {
      return minimalConsumption;
    }
    Record last = new Record();
    last.setOdometer(vehicle.getInitialOdometer());
    last.setTank(vehicle.getTankVolume());
    for (Record record : records) {
      Double consumption = Record.countConsumption(last, record);
      minimalConsumption = consumption > minimalConsumption ? minimalConsumption : consumption;
      last = record;
    }

    return minimalConsumption;
  }

  public Double getMaximalConsumption(Activity activity) {
    List<Record> records = getAllRecords(activity);
    Double maximalConsumption = 0.0;
    Vehicle vehicle = Vehicle.getInstance(activity);
    if (vehicle == null) {
      return maximalConsumption;
    }
    Record last = new Record();
    last.setOdometer(vehicle.getInitialOdometer());
    last.setTank(vehicle.getTankVolume());
    for (Record record : records) {
      Double consumption = Record.countConsumption(last, record);
      maximalConsumption = consumption > maximalConsumption ? consumption : maximalConsumption;
      last = record;
    }

    return maximalConsumption;
  }

  public Consumption getConsumption(Activity activity) {
    List<Record> records = getAllRecords(activity);
    Double aveConsumption = 0.0;
    Record newestRecord = getNewestRecord();
    if (newestRecord != null) {
      int distance = newestRecord.getOdometer()- Vehicle.getInstance(activity).getInitialOdometer();
      aveConsumption = (Math.abs(totalVolume(activity) - newestRecord.getTank()))/distance * 100;
    }

    Double minConsumption = 0.0;
    Double maxConsumption = 0.0;
    Vehicle vehicle = Vehicle.getInstance(activity);
    if (vehicle == null) {
      return null;
    }
    Record last = new Record();
    last.setOdometer(vehicle.getInitialOdometer());
    last.setTank(vehicle.getInitialVolume());
    boolean first = true;
    for (Record record : records) {
      Double consumption = Record.countConsumption(last, record);
      maxConsumption = consumption > maxConsumption ? consumption : maxConsumption;
      if (first) {
        minConsumption = consumption;
        first = false;
      } else {
        minConsumption = consumption > minConsumption ? minConsumption : consumption;
      }

      last = record;
    }

    return new Consumption(minConsumption, maxConsumption, aveConsumption);
  }
}


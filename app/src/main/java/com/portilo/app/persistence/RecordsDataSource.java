package com.portilo.app.persistence;

/**
 * Created by HC on 11.01.2015.
 *
 * This class is our DAO. It maintains the database connection and supports adding new comments and
 * fetching all comments.
 */

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.portilo.app.model.Record;

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

  public List<Record> getAllRecords() {
    List<Record> records = new ArrayList<>();

    Cursor cursor = database.query(MySQLiteHelper.TABLE_FUELING,
            allColumns, null, null, null, null, MySQLiteHelper.COLUMN_NAME_DATE + " ASC");

    cursor.moveToFirst();
    int numberRecords = 0;
    while (!cursor.isAfterLast() && numberRecords < MAX_RECORDS) {
      Record record = cursorToRecord(cursor);
      records.add(record);
      numberRecords++;
      cursor.moveToNext();
    }

    // Make sure to close the cursor
    cursor.close();
    return records;
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

  public Double totalVolume() {
    Cursor cursor = database.query(MySQLiteHelper.TABLE_FUELING, new String[]{MySQLiteHelper.COLUMN_NAME_VOLUME}, null, null, null, null, null);

    cursor.moveToFirst();
    Double totalVolume = 0.0;
    while (!cursor.isAfterLast()) {
      Double volume = cursor.getDouble(cursor.getColumnIndex(MySQLiteHelper.COLUMN_NAME_VOLUME));
      totalVolume += volume;
      cursor.moveToNext();
    }
    // Make sure to close the cursor
    cursor.close();
    return totalVolume;
  }

  public Double minimalVolume() {
    Cursor cursor = database.query(MySQLiteHelper.TABLE_FUELING,
            new String[]{ "min(" + MySQLiteHelper.COLUMN_NAME_VOLUME + ")" }, null, null, null, null, null);
    cursor.moveToFirst();
    int numberRecords = 0;
    Double minVolume = null;
    while (!cursor.isAfterLast() && numberRecords < MAX_RECORDS) {
        minVolume = cursor.getDouble(0);
        // Make sure to close the cursor
    }
    cursor.close();
    return minVolume;
  }

  public Double maximalVolume() {
    Cursor cursor = database.query(MySQLiteHelper.TABLE_FUELING,
            new String[]{ "max(" + MySQLiteHelper.COLUMN_NAME_VOLUME + ")" }, null, null, null, null, null);
    cursor.moveToFirst();
      int numberRecords = 0;
      Double maxVolume = null;
      while (!cursor.isAfterLast() && numberRecords < MAX_RECORDS) {
          maxVolume = cursor.getDouble(0);
      }
    // Make sure to close the cursor
    cursor.close();
    return maxVolume;
  }

  public Long totalVolumes() {
    String sql = "SELECT COUNT(*) FROM " + MySQLiteHelper.TABLE_FUELING;
    SQLiteStatement statement = database.compileStatement(sql);
    long count = statement.simpleQueryForLong();
    return count;
  }

  public Record getNewestRecord() {
    String sql = "SELECT * FROM " + MySQLiteHelper.TABLE_FUELING + " ORDER BY " + MySQLiteHelper.COLUMN_NAME_DATE + " DESC LIMIT 1;";
    Cursor cursor = database.rawQuery(sql, null);
      cursor.moveToFirst();
      int numberRecords = 0;
      Record record = null;
      while (!cursor.isAfterLast() && numberRecords < MAX_RECORDS) {

          record = cursorToRecord(cursor);
      }
      cursor.close();
    return record;
  }
}


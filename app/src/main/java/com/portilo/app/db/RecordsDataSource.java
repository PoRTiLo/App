package com.portilo.app.db;

/**
 * Created by HC on 11.01.2015.
 *
 * This class is our DAO. It maintains the database connection and supports adding new comments and
 * fetching all comments.
 */

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
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
    List<Record> records = new ArrayList<Record>();

    Cursor cursor = database.query(MySQLiteHelper.TABLE_FUELING,
            allColumns, null, null, null, null, null);

    cursor.moveToFirst();
    while (!cursor.isAfterLast()) {
      Record record = cursorToRecord(cursor);
      records.add(record);
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

}


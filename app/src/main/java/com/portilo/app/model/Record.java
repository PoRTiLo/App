package com.portilo.app.model;

/**
 * Created by HC on 11.01.2015.
 *
 * This class is our model and contains the data we will save in the database and show in the user
 * interface.
 */

import java.util.Date;
import android.os.Parcel;
import android.os.Parcelable;


public class Record implements Parcelable {
  private long id;
  private long date;
  private String location;
  private double volume;
  private double tank;
  private int odometer;
  private Record previousRecord;

  public void setPreviousRecord(Record previousRecord) {
    this.previousRecord = previousRecord;
  }

  public Record getPreviousRecord() {
    return previousRecord;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public long getDate() {
    return date;
  }

  public void setDate(long date) {
    this.date = date;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public double getVolume() {
    return volume;
  }

  public void setVolume(double volume) {
    this.volume = volume;
  }

  public double getTank() {
    return tank;
  }

  public void setTank(double tank) {
    this.tank = tank;
  }

  public int getOdometer() {
    return odometer;
  }

  public void setOdometer(int odometer) {
    this.odometer = odometer;
  }

  public static double countConsumption(Record last, Record actual) {
    // TODO: validate data?
    if (last.getOdometer() > actual.getOdometer()) {
      //error data
    }

    int distance = actual.getOdometer() - last.getOdometer();
    double consumption;
    if (distance < 1) {
      consumption = 0.0;
    } else {
      consumption = (Math.abs(actual.getVolume() + last.getTank() - actual.getTank())) / distance * 100.0;
      consumption = Math.round(consumption * 100.0) / 100.0;
    }

    return consumption;
  }

  // Will be used by the ArrayAdapter in the ListView
  @Override
  public String toString() {
    String returnString;
    String stringFormat = "%td.%<tm.%<tY %<tR\t%s\n" +
            "%,d km\n" +
            "%.2f l (%.2f l)\n";

    returnString = String.format(stringFormat, new Date(date), location, odometer, volume, tank);

    return returnString;
  }


  public Record() {

  }
  protected Record(Parcel in) {
    id = in.readLong();
    date = in.readLong();
    location = in.readString();
    volume = in.readDouble();
    tank = in.readDouble();
    odometer = in.readInt();
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeLong(id);
    dest.writeLong(date);
    dest.writeString(location);
    dest.writeDouble(volume);
    dest.writeDouble(tank);
    dest.writeInt(odometer);
  }

  @SuppressWarnings("unused")
  public static final Parcelable.Creator<Record> CREATOR = new Parcelable.Creator<Record>() {
    @Override
    public Record createFromParcel(Parcel in) {
      return new Record(in);
    }

    @Override
    public Record[] newArray(int size) {
      return new Record[size];
    }
  };
}
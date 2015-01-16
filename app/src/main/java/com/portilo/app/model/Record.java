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

  static private int previousOdometer = 0;
  static private int previousTank = 70;

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

  // Will be used by the ArrayAdapter in the ListView
  @Override
  public String toString() {

//    int distance = odometer - previousOdometer;
//
//    double consumption;
//
//    if (distance < 1) {
//      consumption = 0.0;
//      distance = 0;
//    } else {
//      consumption = (volume + previousTank - tank) / distance * 100.0;
//
//      consumption = Math.round(consumption * 100.0) / 100.0;
//    }
//
//    String returnString;
//    String stringFormat = "%td.%<tm.%<tY %<tR\t%s\n" +
//            "%,d km (+ %,d km)\n" +
//            "%.2f l (%.2f l)\n" +
//            "l/100km: %.2f";
//
//    returnString = String.format(stringFormat,
//            new Date(), location, odometer, distance, volume, tank, consumption);
//
//    previousOdometer = odometer;
    String returnString = ", location:" + location + ", volume:" + volume + ", odometer:" + odometer + ", tank:" + tank + ", date:" + new Date(date).toString();
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
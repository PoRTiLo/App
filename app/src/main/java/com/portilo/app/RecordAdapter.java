package com.portilo.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.portilo.app.model.Record;

import java.util.Date;
import java.util.List;

/**
 * Created by portilo on 1/23/2015.
 */
public class RecordAdapter extends ArrayAdapter<Record> {

  private final Context context;
  private final List<Record> recordsArrayList;

  public RecordAdapter(Context context, List<Record> recordsArrayList) {

    super(context, R.layout.fragment_records_list_row, recordsArrayList);

    this.context = context;
    this.recordsArrayList = recordsArrayList;
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {

    // 1. Create inflater
    LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    // 2. Get rowView from inflater
    View rowView = inflater.inflate(R.layout.fragment_records_list_row, parent, false);

    // 3. Get the text view from the rowView
    TextView dateLocation = (TextView) rowView.findViewById(R.id.row_date_location);
    TextView odometerDistance = (TextView) rowView.findViewById(R.id.row_odometer_distance);
    TextView volumeTank = (TextView) rowView.findViewById(R.id.row_volume_tank);
    TextView consumption = (TextView) rowView.findViewById(R.id.row_consumption);
    CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.checkBox);

    // TODO: now hide check box
    checkBox.setVisibility(View.INVISIBLE);

    // 4. Set the text
    Record mRecord = recordsArrayList.get(position);
    String formatDate = String.format("%td.%<tm.%<tY %<tR", new Date(mRecord.getDate()));
    dateLocation.setText(formatDate + " " + mRecord.getLocation());
    odometerDistance.setText(mRecord.getOdometer() + " km (+ " + mRecord.getDistance() + ") km");
    volumeTank.setText(mRecord.getVolume() + " l (" + mRecord.getTank() + "l)");
    consumption.setText("1/100 km: " + mRecord.getConsumption());

    // 5. return rowView
    return rowView;
  }
}

package com.portilo.app;

/**
 * Created by HC on 15.02.2015.
 *
 */

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.portilo.app.common.UtilsImpl;
import com.portilo.app.model.Consumption;
import com.portilo.app.model.Vehicle;
import com.portilo.app.persistence.RecordsDataSource;
import com.portilo.app.model.Record;

public class StatisticFragment extends Fragment {

  /**
   * Returns a new instance of this fragment for the given section number.
   */
  public static StatisticFragment newInstance() {
    return new StatisticFragment();
  }

  private RecordsDataSource dataSource;
  public StatisticFragment() {}

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    dataSource = new RecordsDataSource(getActivity());
    dataSource.open();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_statistic, container, false);
    init(view);
    return view;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    ((MainActivity) activity).onSectionAttached(1);
  }

  private void init(View view) {
    TextView totalTachometerTextView = (TextView) view.findViewById(R.id.totalTachometerTextView);
    TextView totalDistanceTextView = (TextView) view.findViewById(R.id.totalDistanceTextView);
    TextView totalVolumeTextView = (TextView) view.findViewById(R.id.totalVolumeTextView);
    TextView totalRefuelingTextView = (TextView) view.findViewById(R.id.totalRefuelingTextView);
    TextView maximalVolumeTextView = (TextView) view.findViewById(R.id.maximalVolumeTextView);
    TextView minimalVolumeTextView = (TextView) view.findViewById(R.id.minimalVolumeTextView);
    TextView averageFuelEconomyTextView = (TextView) view.findViewById(R.id.averageFuelEconomyTextView);
    TextView lowestFuelEconomyTextView = (TextView) view.findViewById(R.id.lowestFuelEconomyTextView);
    TextView highestFuelEconomyTextView = (TextView) view.findViewById(R.id.highestFuelEconomyTextView);

    Double totalVolume = dataSource.totalVolume(getActivity());
    totalVolumeTextView.setText(totalVolume == null ? null : UtilsImpl.round(totalVolume));

    Double minimalVolume = dataSource.minimalVolume();
    minimalVolumeTextView.setText(minimalVolume == null ? null : UtilsImpl.round(minimalVolume));

    Double maximalVolume = dataSource.maximalVolume();
    maximalVolumeTextView.setText(maximalVolume == null ? null : UtilsImpl.round(maximalVolume));

    Long totalRefueling = dataSource.totalVolumes();
    totalRefuelingTextView.setText(totalRefueling == null ? null : totalRefueling.toString());

    Record record = dataSource.getNewestRecord();
    if (record != null) {
      totalTachometerTextView.setText(record.getOdometer() + "");
      Vehicle vehicle = Vehicle.getInstance(getActivity());
      if (vehicle != null) {
        Integer totalDistance = record.getOdometer() - vehicle.getInitialOdometer();
        totalDistanceTextView.setText(totalDistance.toString());
      } else {
        totalDistanceTextView.setText("");
      }
    } else {
      totalTachometerTextView.setText("");
      totalDistanceTextView.setText("");
    }

    Consumption consumption = dataSource.getConsumption(getActivity());
    if (consumption != null) {
      lowestFuelEconomyTextView.setText(UtilsImpl.round(consumption.getMinConsumption()));
      highestFuelEconomyTextView.setText(UtilsImpl.round(consumption.getMaxConsumption()));
      averageFuelEconomyTextView.setText(UtilsImpl.round(consumption.getAveConsumption()));
    } else {
      lowestFuelEconomyTextView.setText("");
      highestFuelEconomyTextView.setText("");
      averageFuelEconomyTextView.setText("");
    }
  }
}

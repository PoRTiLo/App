package com.portilo.app;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.portilo.app.persistence.RecordsDataSource;
import com.portilo.app.model.Record;

public class StatisticFragment extends Fragment {

  /**
   * Returns a new instance of this fragment for the given section number.
   */
  public static StatisticFragment newInstance() {
    return new StatisticFragment();
  }

  private TextView totalTachometerTextView;
  private TextView totalDistanceTextView;
  private TextView totalVolumeTextView;
  private TextView totalRefuelingTextView;
  private TextView maximalVolumeTextView;
  private TextView minimalVolumeTextView;
  private TextView averageFuelEconomyTextView;
  private TextView lowestFuelEconomyTextView;
  private TextView highestFuelEconomyTextView;

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
    totalTachometerTextView = (TextView) view.findViewById(R.id.totalTachometerTextView);
    totalDistanceTextView = (TextView) view.findViewById(R.id.totalDistanceTextView);
    totalVolumeTextView = (TextView) view.findViewById(R.id.totalVolumeTextView);
    totalRefuelingTextView = (TextView) view.findViewById(R.id.totalRefuelingTextView);
    maximalVolumeTextView = (TextView) view.findViewById(R.id.maximalVolumeTextView);
    minimalVolumeTextView = (TextView) view.findViewById(R.id.minimalVolumeTextView);
    averageFuelEconomyTextView = (TextView) view.findViewById(R.id.averageFuelEconomyTextView);
    lowestFuelEconomyTextView = (TextView) view.findViewById(R.id.lowestFuelEconomyTextView);
    highestFuelEconomyTextView = (TextView) view.findViewById(R.id.highestFuelEconomyTextView);

    Double totalVolume = dataSource.totalVolume();
    totalVolumeTextView.setText(totalVolume == null ? null : totalVolume.toString());

    Double minimalVolume = dataSource.minimalVolume();
    minimalVolumeTextView.setText(minimalVolume == null ? null : minimalVolume.toString());

    Double maximalVolume = dataSource.maximalVolume();
    maximalVolumeTextView.setText(maximalVolume == null ? null : maximalVolume.toString());

    Long totalRefueling = dataSource.totalVolumes();
    totalRefuelingTextView.setText(totalRefueling == null ? null : totalRefueling.toString());

    Record record = dataSource.getNewestRecord();
    totalTachometerTextView.setText(record.getDistance() + "");
  }
}

package com.portilo.app;

import android.app.Activity;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StatisticFragment extends Fragment {

  /**
   * Returns a new instance of this fragment for the given section number.
   */
  public static StatisticFragment newInstance() {
    StatisticFragment fragment = new StatisticFragment();
    return fragment;
  }

  public StatisticFragment() {}

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_home, container, false);
    return rootView;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    ((MainActivity) activity).onSectionAttached(0);
  }

}

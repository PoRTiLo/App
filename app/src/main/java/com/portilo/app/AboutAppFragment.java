package com.portilo.app;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by portilo on 1/19/2015.
 */
public class AboutAppFragment extends Fragment {
  /**
   * Returns a new instance of this fragment for the given section number.
   */
  public static AboutAppFragment newInstance() {
    AboutAppFragment fragment = new AboutAppFragment();
    return fragment;
  }

  public AboutAppFragment() {}

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_about, container, false);

    return rootView;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    ((MainActivity) activity).onSectionAttached(1);
  }
}

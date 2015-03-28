package com.portilo.app;

/**
 * Created by HC on 15.02.2015.
 *
 */

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidannotations.annotations.EFragment;

@EFragment
public class AboutAppFragment extends Fragment {
  /**
   * Returns a new instance of this fragment for the given section number.
   */
  public static AboutAppFragment newInstance() {
    return new AboutAppFragment();
  }

  public AboutAppFragment() {}

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_about, container, false);
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    ((MainActivity) activity).onSectionAttached(2);
  }
}

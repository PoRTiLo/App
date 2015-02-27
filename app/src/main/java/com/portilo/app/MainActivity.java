package com.portilo.app;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;

import com.portilo.app.view.NavigationDrawerFragment;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks, RecordsFragment.OnFragmentInteractionListener {

  // Fragment managing the behaviors, interactions and presentation of the navigation drawer.
  private NavigationDrawerFragment mNavigationDrawerFragment;

  // Used to store the last screen title. For use in {@link #restoreActionBar()}.
  private CharSequence mTitle;

  private static final String LOGGER = "MainActivity";

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);
    mTitle = getTitle();

    // Set up the drawer.
    mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));
  }

  @Override
  public void onNavigationDrawerItemSelected(int position) {

    onSectionAttached(position);
    // TODO not work goods
    restoreActionBar();
    // update the menu content by replacing fragments
    FragmentManager fragmentManager = getFragmentManager();
    if (position == 0) {
      fragmentManager.beginTransaction().replace(R.id.container, VehicleFragment.newInstance()).commit();
    } else if (position == 1) {
      fragmentManager.beginTransaction().replace(R.id.container, RecordsFragment.newInstance()).commit();
    } else if (position == 2) {
      fragmentManager.beginTransaction().replace(R.id.container, StatisticFragment.newInstance()).commit();
    } else if (position == 3 ) {
      fragmentManager.beginTransaction().replace(R.id.container, AboutAppFragment.newInstance()).commit();
    }

  }

  public void onSectionAttached(int number) {
    Log.i(LOGGER, "onSectionAttached: " + number);
    switch (number) {
      case 0:
        mTitle = getString(R.string.title_section4);
        break;
      case 1:
        mTitle = getString(R.string.title_section1);
        break;
      case 2:
        mTitle = getString(R.string.title_section2);
        break;
      case 3:
        mTitle = getString(R.string.title_section3);
        break;
    }
  }

  public void restoreActionBar() {
    ActionBar actionBar = getSupportActionBar();
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
    actionBar.setDisplayShowTitleEnabled(true);
    actionBar.setTitle(mTitle);
  }

  @Override
  public void onFragmentInteraction(String id) {
  }

  /**
   * A placeholder fragment containing a simple view.
   */
  public static class PlaceholderFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static PlaceholderFragment newInstance(int sectionNumber) {
      PlaceholderFragment fragment = new PlaceholderFragment();
      Bundle args = new Bundle();
      args.putInt(ARG_SECTION_NUMBER, sectionNumber);
      fragment.setArguments(args);
      return fragment;
    }

    public PlaceholderFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
      View rootView = inflater.inflate(R.layout.fragment_main, container, false);
      return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
      super.onAttach(activity);
      ((MainActivity) activity).onSectionAttached(getArguments().getInt(ARG_SECTION_NUMBER));
    }
  }

}

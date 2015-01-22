package com.portilo.app;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;


import com.portilo.app.db.RecordsDataSource;
import com.portilo.app.model.Record;
import com.portilo.app.view.DeleteDialog;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class RecordsFragment extends Fragment implements AbsListView.OnItemClickListener,
        AbsListView.OnItemLongClickListener, DeleteDialog.NoticeDialogListener {

  private OnFragmentInteractionListener mListener;

  // The fragment's ListView/GridView.
  private AbsListView mListView;

  // The Adapter which will be used to populate the ListView/GridView with Views.
  private ArrayAdapter<Record> mAdapter;

  private RecordsDataSource dataSource;

  private List<Record> values;

  private int selectedRecordPosition;

  public static final String LOGGER = "RecordsFragment";
  public static final Integer CREATE_RECORD = 2;
  public static final Integer UPDATE_RECORD = 3;
  public static final Integer DELETE_RECORD = 4;

  public static RecordsFragment newInstance() {
    RecordsFragment fragment = new RecordsFragment();
    return fragment;
  }

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public RecordsFragment() {}

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    dataSource = new RecordsDataSource(getActivity());
    dataSource.open();
    values = dataSource.getAllRecords();
    // Adapter to display your content
    mAdapter = new ArrayAdapter<Record>(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, values);
    setHasOptionsMenu(true);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_item, container, false);

    // Set the adapter
    mListView = (AbsListView) view.findViewById(android.R.id.list);
    ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

    // Set OnItemClickListener so we can be notified on item clicks
    mListView.setOnItemClickListener(this);
    mListView.setOnItemLongClickListener(this);

    return view;
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    try {
      mListener = (OnFragmentInteractionListener) activity;
    } catch (ClassCastException e) {
      throw new ClassCastException(activity.toString() + " must implement OnFragmentInteractionListener");
    }
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mListener = null;
  }


  @Override
  public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    if (null != mListener) {
      // Notify the active callbacks interface (the activity, if the
      // fragment is attached to one) that an item has been selected.
      Record mRecord = values.get(position);
      mListener.onFragmentInteraction(mRecord.getLocation());
      Log.i(LOGGER, "Position in list : " + position + ", record :" + mRecord);
      selectedRecordPosition = position;
      Intent intent = new Intent(getActivity(), AddNewRecordActivity.class);
      intent.putExtra(UPDATE_RECORD.toString(), mRecord);
      startActivityForResult(intent, UPDATE_RECORD);
    }
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    inflater.inflate(R.menu.main, menu);
    super.onCreateOptionsMenu(menu,inflater);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    int id = item.getItemId();

    // noinspection SimplifiableIfStatement
    if (id == R.id.menu_item_new) {
      Intent intent = new Intent(getActivity(), AddNewRecordActivity.class);
      startActivityForResult(intent, CREATE_RECORD);
      return true;
    }

    return super.onOptionsItemSelected(item);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == getActivity().RESULT_OK) {
      Record tempRecord = (Record) data.getParcelableExtra(CREATE_RECORD.toString());
      if (requestCode == CREATE_RECORD) {
        addNewRecord(tempRecord);
      } else if (requestCode == UPDATE_RECORD) {
        updateRecord(tempRecord);
      }
    }
  }

  private void addNewRecord(Record mRecord) {
    Log.i(LOGGER, "New created record: " + mRecord.toString());
    Record record = dataSource.createRecord(mRecord);
    values.add(record);
    mAdapter.notifyDataSetChanged();
    Log.i(LOGGER, "Update list");
  }

  private void updateRecord(Record mRecord) {
    Log.i(LOGGER, "Updated record: " + mRecord.toString());
    int result = dataSource.updateRecord(mRecord);
    if (result > 0) {
      values.remove(selectedRecordPosition);
      values.add(selectedRecordPosition, mRecord);
      mAdapter.notifyDataSetChanged();
      Log.i(LOGGER, "Update list");
    }
  }
  /**
   * The default content for this Fragment has a TextView that is shown when
   * the list is empty. If you would like to change the text, call this method
   * to supply the text it should use.
   */
  public void setEmptyText(CharSequence emptyText) {
    View emptyView = mListView.getEmptyView();

    if (emptyView instanceof TextView) {
      ((TextView) emptyView).setText(emptyText);
    }
  }

  @Override
  public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
    DeleteDialog deleteDialog = new DeleteDialog();
    deleteDialog.setTargetFragment(this, DELETE_RECORD);
    deleteDialog.show(getFragmentManager(), DELETE_RECORD.toString());
    selectedRecordPosition = position;
    return true;
  }

  @Override
  public void onDialogPositiveClick() {
    if (selectedRecordPosition >= 0 && selectedRecordPosition < values.size()) {
      Record record = values.get(selectedRecordPosition);
      int result = dataSource.deleteRecord(record);
      if (result > 0) {
        values.remove(selectedRecordPosition);
        Log.i(LOGGER, "Deleted record: " + record);
        mAdapter.notifyDataSetChanged();
      }
      selectedRecordPosition = -1;
    } else {
      Log.i(LOGGER, "No Deleted record");
    }
  }

  @Override
  public void onDialogNegativeClick() {
    Log.i(LOGGER, "Canceled deleted record");
  }

  /**
   * This interface must be implemented by activities that contain this
   * fragment to allow an interaction in this fragment to be communicated
   * to the activity and potentially other fragments contained in that
   * activity.
   * <p/>
   * See the Android Training lesson <a href=
   * "http://developer.android.com/training/basics/fragments/communicating.html"
   * >Communicating with Other Fragments</a> for more information.
   */
  public interface OnFragmentInteractionListener {
    // TODO: Update argument type and name
    public void onFragmentInteraction(String id);
  }

}

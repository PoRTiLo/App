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
import android.widget.Toast;


import com.portilo.app.db.RecordsDataSource;
import com.portilo.app.model.Record;

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
public class ItemFragment extends Fragment implements AbsListView.OnItemClickListener,
        AbsListView.OnItemLongClickListener, DeleteDialog.NoticeDialogListener {

  private OnFragmentInteractionListener mListener;

  // The fragment's ListView/GridView.
  private AbsListView mListView;

  // The Adapter which will be used to populate the ListView/GridView with Views.
  private ArrayAdapter<Record> mAdapter;

  private RecordsDataSource dataSource;

  private List<Record> values;

  private int selectedRecord;

  public static ItemFragment newInstance() {
    ItemFragment fragment = new ItemFragment();
    return fragment;
  }

  /**
   * Mandatory empty constructor for the fragment manager to instantiate the
   * fragment (e.g. upon screen orientation changes).
   */
  public ItemFragment() {}

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
      mListener.onFragmentInteraction(values.get(position).getLocation());
      Log.i("click", position + ":" + id);
      Intent intent = new Intent(getActivity(), AddNewItemActivity.class);
      intent.putExtra("1", values.get(position));
      startActivity(intent);
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

    //noinspection SimplifiableIfStatement
    if (id == R.id.menu_item_new) {
      addNewItem();
      return true;
    }

    return super.onOptionsItemSelected(item);
  }
  public void addNewItem() {
    Intent intent = new Intent(getActivity(), AddNewItemActivity.class);
    startActivityForResult(intent, 1);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode == getActivity().RESULT_OK && requestCode == 1) {
      Record tempRecord = (Record) data.getParcelableExtra("1");
      Log.i("data", tempRecord.toString());

      Record record = dataSource.createRecord(tempRecord);
      values.add(record);
      mAdapter.notifyDataSetChanged();
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
    Toast.makeText(getActivity().getApplicationContext(), "You have pressed it long :)", Toast.LENGTH_SHORT).show();
    Log.i("onItemLongClick", position + ":" + id);
    DeleteDialog deleteDialog = new DeleteDialog();
    deleteDialog.setTargetFragment(this, 0);
    deleteDialog.show(getFragmentManager(), "delete");
    selectedRecord = position;
    return true;
  }

  @Override
  public void onDialogPositiveClick() {
    Log.i("onDialogPositiveClick", "onDialogPositiveClick");
    if (selectedRecord > 0 && selectedRecord < values.size()) {
      Record record = values.get(selectedRecord);
      int result = dataSource.deleteRecord(record);
      if (result > 0) {
        values.remove(selectedRecord);
        Log.i("onDialogPositiveClick", "delete");
        mAdapter.notifyDataSetChanged();
      }
      selectedRecord = -1;
    } else {
      Log.i("onDialogPositiveClick", "NO delete");
    }
  }

  @Override
  public void onDialogNegativeClick() {
    Log.i("onDialogNegativeClick", "onDialogNegativeClick");
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

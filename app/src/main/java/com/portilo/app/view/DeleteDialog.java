package com.portilo.app.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

public class DeleteDialog extends DialogFragment {


  /* The activity that creates an instance of this dialog fragment must
     * implement this interface in order to receive event callbacks.
     * Each method passes the DialogFragment in case the host needs to query it. */
  public interface NoticeDialogListener {
    public void onDialogPositiveClick();
    public void onDialogNegativeClick();
  }

  // Use this instance of the interface to deliver action events
  NoticeDialogListener mListener;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    try {
      mListener = (NoticeDialogListener) getTargetFragment();
    } catch (ClassCastException e) {
      throw new ClassCastException("Calling Fragment must implement NoticeDialogListener");
    }
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    // Use the Builder class for convenient dialog construction
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setMessage("Remove roceord?")
            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                mListener.onDialogPositiveClick();
              }
            })
            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
              public void onClick(DialogInterface dialog, int id) {
                mListener.onDialogNegativeClick();
                DeleteDialog.this.getDialog().cancel();
              }
            });
    // Create the AlertDialog object and return it
    return builder.create();
  }
}
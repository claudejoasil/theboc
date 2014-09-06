package org.theBOC.theboc;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class VersionUnavailableDialFrag extends DialogFragment {
	private String dialogMessage;
	public interface VersionUnavailableDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
	VersionUnavailableDialogListener mListener;
	public String getDialogMessage()
	{
		return this.dialogMessage;
	}
	public void setDialogMessage(String message)
	{
		this.dialogMessage = message;
	}
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (VersionUnavailableDialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement VersionUnavailableDialogListener");
        }
    }
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(this.dialogMessage)
               .setPositiveButton(R.string.download, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       mListener.onDialogPositiveClick(VersionUnavailableDialFrag.this);
                   }
               })
               .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   mListener.onDialogNegativeClick(VersionUnavailableDialFrag.this);
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

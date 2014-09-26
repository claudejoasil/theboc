package org.theBOC.bocbible;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

public class BOCDialogFrag extends DialogFragment {
	private static String dialogMessage;
	private static String btnPositiveText;
	private static String btnNegativeText;
	public interface BOCDialogFragListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
	private static BOCDialogFragListener mListener;
	public void setListener(BOCDialogFragListener listener)
	{
		mListener = listener;
	}
	public String getDialogMessage()
	{
		return dialogMessage;
	}
	public void setDialogMessage(String message)
	{
		dialogMessage = message;
	}
	public String getBtnPositiveText()
	{
		return btnPositiveText;
	}
	public void setBtnPositiveText(String positiveText)
	{
		btnPositiveText = positiveText;
	}
	public String getBtnNegativeText()
	{
		return btnNegativeText;
	}
	public void setBtnNegativeText(String negativeText)
	{
		btnNegativeText = negativeText;
	}
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if(mListener == null) 
        {
	        // Verify that the host activity implements the callback interface
	        try {
	            // Instantiate the NoticeDialogListener so we can send events to the host
	            mListener = (BOCDialogFragListener) activity;
	        } catch (ClassCastException e) {
	            // The activity doesn't implement the interface, throw exception
	            throw new ClassCastException(activity.toString()
	                    + " must implement BOCDialogFragListener");
	        }
        }
    }
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(dialogMessage)
               .setPositiveButton(btnPositiveText, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       mListener.onDialogPositiveClick(BOCDialogFrag.this);
                   }
               })
               .setNegativeButton(btnNegativeText, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                	   mListener.onDialogNegativeClick(BOCDialogFrag.this);
                   }
               });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}

package org.theBOC.bocbible;

import java.util.ArrayList;
import org.theBOC.bocbible.Adapters.NoteListAdapter;
import org.theBOC.bocbible.common.BibleHelper;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
//import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

public class NoteFragment extends Fragment {
	private static org.theBOC.bocbible.database.Note noteDB;
	private BibleHelper bibleHelper;
	private ListView lstView;
	private TextView txtView;
	private static ProgressDialog mProgressDialog;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.notes_fragment, container, false);
		bibleHelper = BibleHelper.getInstance(this.getActivity());
		noteDB = new org.theBOC.bocbible.database.Note(this.getActivity());
		lstView = (ListView) rootView.findViewById(R.id.lst_notes);
		txtView = (TextView) rootView.findViewById(R.id.no_notes);
        return rootView;
    }
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);  
	 }
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.note, menu);
		//this.menu = menu;
		super.onCreateOptionsMenu(menu, inflater);
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // Handle action buttons
        switch(item.getItemId()) {
        case R.id.action_add:
        	//Intent bookIntent = new Intent(Bible.this.getActivity(), Books.class);
        	//bookIntent.putExtra("ACTIVITY_TITLE", this.currentBookObj.getName() + " " +  bibleHelper.getCurrentChapter(1));
    		//startActivity(bookIntent);
            return true;
        case R.id.action_bible_version:
        	//Intent versionIntent = new Intent(Bible.this.getActivity(), Versions.class);
    		//startActivity(versionIntent);
            return true; 
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	private class NotesTask extends AsyncTask<String, Integer, ArrayList<org.theBOC.bocbible.Models.Note>> {

	    private Context context;
	    //private org.theBOC.bocbible.Models.Version version;
	    public NotesTask(Context context) {
	        this.context = context;
	    }

	    @Override
	    protected ArrayList<org.theBOC.bocbible.Models.Note> doInBackground(String... params) 
	    {
	    	return noteDB.getNotes();
	    }
	    
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        mProgressDialog.show();
	       
	    }

	    @Override
	    protected void onProgressUpdate(Integer... progress) {
	        super.onProgressUpdate(progress);
	        mProgressDialog.setIndeterminate(false);
	        mProgressDialog.setMax(100);
	        mProgressDialog.setProgress(progress[0]);
	    }

	    @Override
	    protected void onPostExecute(ArrayList<org.theBOC.bocbible.Models.Note> notes) 
	    {
	    	mProgressDialog.dismiss();
	    	if(notes.isEmpty())  
	    	{
	    		lstView.setVisibility(ListView.GONE);
	    		txtView.setVisibility(TextView.VISIBLE);
	    		return;
	    	}
	    	lstView.setVisibility(ListView.VISIBLE);
    		txtView.setVisibility(TextView.GONE);
    		NoteListAdapter adt = new NoteListAdapter(this.context, notes);
			lstView.setAdapter(adt);
    	}
	}
}

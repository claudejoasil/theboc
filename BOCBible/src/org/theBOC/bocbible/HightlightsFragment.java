package org.theBOC.bocbible;

import java.util.ArrayList;

import org.theBOC.bocbible.Adapters.HighlightListAdapter;
import org.theBOC.bocbible.Adapters.VersionListAdapter;
import org.theBOC.bocbible.common.BibleHelper;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class HightlightsFragment extends Fragment {
	private static org.theBOC.bocbible.database.Bible bibleDB;
	private BibleHelper bibleHelper;
	private ListView lstView;
	private TextView txtView;
	private static ProgressDialog mProgressDialog;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.hightlights_fragment, container, false);
		bibleHelper = BibleHelper.getInstance(this.getActivity());
		bibleDB = new org.theBOC.bocbible.database.Bible(this.getActivity());
		lstView = (ListView) rootView.findViewById(R.id.lst_bible_highlights);
		txtView = (TextView) rootView.findViewById(R.id.no_highlights);
		Spinner spinVersions = (Spinner) rootView.findViewById(R.id.spin_versions);
		org.theBOC.bocbible.database.Version versionDB = new org.theBOC.bocbible.database.Version(this.getActivity());
		final ArrayList<org.theBOC.bocbible.Models.Version> versions = versionDB.getVersions(null, false, true);
		VersionListAdapter adt = new VersionListAdapter(this.getActivity(), versions, true);
		spinVersions.setAdapter(adt);
		spinVersions.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				final HighlightsTask highlightsTask = new HighlightsTask(HightlightsFragment.this.getActivity(), versions.get(position));
				mProgressDialog = new ProgressDialog(HightlightsFragment.this.getActivity());
				mProgressDialog.setMessage("please wait...");
				mProgressDialog.setIndeterminate(true);
				mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mProgressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
				highlightsTask.execute("");
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				// TODO Auto-generated method stub
				
			}
		});
		spinVersions.setSelection(adt.getItemPositionById(bibleHelper.getCurrentVersionId(4)), true);
        return rootView;
    }
	
	private class HighlightsTask extends AsyncTask<String, Integer, ArrayList<org.theBOC.bocbible.Models.Bible>> {

	    private Context context;
	    private org.theBOC.bocbible.Models.Version version;
	    public HighlightsTask(Context context, org.theBOC.bocbible.Models.Version version) {
	        this.context = context;
	        this.version = version;
	    }

	    @Override
	    protected ArrayList<org.theBOC.bocbible.Models.Bible> doInBackground(String... params) 
	    {
	    	return bibleDB.getHighlightVerses(version);
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
	    protected void onPostExecute(ArrayList<org.theBOC.bocbible.Models.Bible> verses) 
	    {
	    	mProgressDialog.dismiss();
	    	if(verses.isEmpty())  
	    	{
	    		lstView.setVisibility(ListView.GONE);
	    		txtView.setVisibility(TextView.VISIBLE);
	    		return;
	    	}
	    	lstView.setVisibility(ListView.VISIBLE);
    		txtView.setVisibility(TextView.GONE);
	    	HighlightListAdapter adt = new HighlightListAdapter(this.context, verses);
			lstView.setAdapter(adt);
    	}
	}
}

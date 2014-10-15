package org.theBOC.bocbible;

import java.util.ArrayList;
import org.theBOC.bocbible.Adapters.HighlightListAdapter;
import org.theBOC.bocbible.common.BibleHelper;
import org.theBOC.bocbible.database.Bible;
import org.theBOC.bocbible.database.Version;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class SearchFragment extends Fragment {
	private static org.theBOC.bocbible.database.Bible bibleDB;
	private static org.theBOC.bocbible.database.Version versionDB;
	private BibleHelper bibleHelper;
	private ListView lstView;
	private TextView txtView;
	private Button btnSearch;
	private EditText editSearch;
	private TextView txtResultForText;
	private TextView txtSearchingVersion;
	private static ProgressDialog mProgressDialog;
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		bibleDB = new Bible(this.getActivity());
		versionDB = new Version(this.getActivity());
		bibleHelper = BibleHelper.getInstance(this.getActivity());
		View rootView = inflater.inflate(R.layout.activity_search, container, false);
		this.initControls(rootView);
		return rootView;
	}
	private void initControls(View rootView)
	{
		lstView = (ListView) rootView.findViewById(R.id.lst_bible_search);
		txtView = (TextView) rootView.findViewById(R.id.no_result);
		btnSearch = (Button) rootView.findViewById(R.id.btn_search);
		editSearch = (EditText) rootView.findViewById(R.id.edit_search);
		txtResultForText = (TextView) rootView.findViewById(R.id.txtResultForText);
		txtSearchingVersion = (TextView) rootView.findViewById(R.id.txtSearchingVersion);
		txtSearchingVersion.setText("Searching version " + bibleHelper.getCurrentVersionName("KJV"));
		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(editSearch.getWindowToken(),  0);
				String query = editSearch.getText().toString();
				if(query.trim().equals(""))
					return;
				org.theBOC.bocbible.Models.Version version = versionDB.getVersion(bibleHelper.getCurrentVersionId(4), null);
				final SearchTask SearchTask = new SearchTask(SearchFragment.this.getActivity(), version, 0);
				mProgressDialog = new ProgressDialog(SearchFragment.this.getActivity());
				mProgressDialog.setMessage("please wait...");
				mProgressDialog.setIndeterminate(true);
				mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
				mProgressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
				SearchTask.execute(query);
			}
		});
	}
	
	@Override
	public void onStop(){
        super.onStop();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editSearch.getWindowToken(),  0);
    }
	@Override
	public void onPause() {
        super.onPause();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editSearch.getWindowToken(),  0);
    }
	
	private class SearchTask extends AsyncTask<String, Integer, ArrayList<org.theBOC.bocbible.Models.Bible>> {
	    private Context context;
	    private org.theBOC.bocbible.Models.Version version;
	    private int testament;
	    public SearchTask(Context context, org.theBOC.bocbible.Models.Version version, int testament) {
	        this.context = context;
	        this.version = version;
	        this.testament = testament;
	    }

	    @Override
	    protected ArrayList<org.theBOC.bocbible.Models.Bible> doInBackground(String... params) 
	    {
	    	String query = params[0];
	    	return bibleDB.search(query, testament, version);
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
	    		txtResultForText.setVisibility(TextView.GONE);
	    		return;
	    	}
	    	lstView.setVisibility(ListView.VISIBLE);
    		txtView.setVisibility(TextView.GONE);
    		txtResultForText.setText("Search results for '" + editSearch.getText().toString() + "'");
    		editSearch.setText("");
    		txtResultForText.setVisibility(TextView.VISIBLE);
	    	HighlightListAdapter adt = new HighlightListAdapter(this.context, verses, true);
			lstView.setAdapter(adt);
    	}
	}
}

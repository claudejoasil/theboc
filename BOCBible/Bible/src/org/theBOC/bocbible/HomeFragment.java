package org.theBOC.bocbible;


import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.theBOC.bocbible.R;
import org.theBOC.bocbible.common.BibleHelper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeFragment extends Fragment {

	private static org.theBOC.bocbible.database.Bible bibleDB;
	private BibleHelper bibleHelper;
	private static TextView txtWeeklyVerse;
	private static TextView txtWeeklyVerseReference;
	private static int weekNumber;
	public HomeFragment() {
    }
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.home_fragment, container, false);
		bibleHelper = BibleHelper.getInstance(this.getActivity());
		bibleDB = new org.theBOC.bocbible.database.Bible(this.getActivity());
		txtWeeklyVerse = (TextView) rootView.findViewById(R.id.txt_weeklyVerse);
		txtWeeklyVerseReference = (TextView) rootView.findViewById(R.id.txt_weeklyVerseReference);
		Calendar calendar = new GregorianCalendar();
	    calendar.setTime(new Date());
	    weekNumber = calendar.get(Calendar.WEEK_OF_YEAR);
		GetWeeklyVerse();
		String currentBookName = bibleHelper.getCurrentBookName("");
		if(!currentBookName.equalsIgnoreCase(""))
		{
			int currentChapter = bibleHelper.getCurrentChapter(1);
			int currentVerseInt = bibleHelper.getCurrentVerse(1);
			TextView txtView = (TextView) rootView.findViewById(R.id.latest_bible_reading);
	        txtView.setText("Where you left off: " +  currentBookName + " " + currentChapter + ":" + currentVerseInt);
		}
		else
		{
			LinearLayout reading_box = (LinearLayout) rootView.findViewById(R.id.latest_reading_box);
			reading_box.setVisibility(LinearLayout.GONE);
		}
        return rootView;
    }
	@Override
	public void onStop(){
       super.onStop();
       bibleHelper.persistCurrentWeeklyValues();
   }
	@Override
	public void onPause() {
       super.onPause();
       bibleHelper.persistCurrentWeeklyValues();
   }
	private void GetWeeklyVerse()
	{
		if(bibleHelper.getCurrentWeeklyBookId(4) > 0 && bibleHelper.getCurrentWeeklyVerseWeek(0) == weekNumber && bibleHelper.getCurrentWeeklyVerseVersion("KJV") == bibleHelper.getCurrentVersionName("KJV"))
		{
			txtWeeklyVerse.setText(bibleHelper.getCurrentWeeklyVerseText(""));
			txtWeeklyVerseReference.setText(bibleHelper.getCurrentWeeklyBookName("") + 
					" " + bibleHelper.getCurrentWeeklyChapter(0) + ":" + bibleHelper.getCurrentWeeklyVerse(0));
		}
		else
		{
			final WeeklyVerseTask weeklyVerseTask = new WeeklyVerseTask(HomeFragment.this.getActivity());
			weeklyVerseTask.execute("");
		}
	}
	private class WeeklyVerseTask extends AsyncTask<String, Integer, org.theBOC.bocbible.Models.Bible> {

	    //private Context context;
	    public WeeklyVerseTask(Context context) {
	        //this.context = context;
	    }

	    @Override
	    protected org.theBOC.bocbible.Models.Bible doInBackground(String... params) 
	    {
	    	return bibleDB.getWeeklyVerse(bibleHelper.getCurrentVersionName("KJV"), weekNumber);
	    }
	    
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	       
	    }

	    @Override
	    protected void onProgressUpdate(Integer... progress) {
	        super.onProgressUpdate(progress);
	    }

	    @Override
	    protected void onPostExecute(org.theBOC.bocbible.Models.Bible result) 
	    {
	    	txtWeeklyVerse.setText(result.getVerseText());
			txtWeeklyVerseReference.setText(result.getBook() + " " + result.getChapter() + ":" + result.getVerse());
			bibleHelper.setCurrentWeeklyBookId(result.getBookId());
			bibleHelper.setCurrentWeeklyBookName(result.getBook());
			bibleHelper.setCurrentWeeklyChapter(result.getChapter());
			bibleHelper.setCurrentWeeklyVerse(result.getVerse());
			bibleHelper.setCurrentWeeklyVerseText(result.getVerseText());
			bibleHelper.setCurrentWeeklyVerseWeek(weekNumber);
    	}
	}
}

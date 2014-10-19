package org.theBOC.bocbible;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.theBOC.bocbible.R;
import org.theBOC.bocbible.Adapters.HighlightListAdapter;
import org.theBOC.bocbible.Models.Bible;
import org.theBOC.bocbible.Models.Version;
import org.theBOC.bocbible.common.BibleHelper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class HomeFragment extends Fragment {

	private static org.theBOC.bocbible.database.Bible bibleDB;
	private BibleHelper bibleHelper;
	private static ListView lstView;
	private static int weekNumber;
	public HomeFragment() {
    }
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.home_fragment, container, false);
		bibleHelper = BibleHelper.getInstance(this.getActivity());
		bibleDB = new org.theBOC.bocbible.database.Bible(this.getActivity());
		lstView = (ListView) rootView.findViewById(R.id.lst_bible_weekly_verse);
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
			Bible bibleVerse = new Bible();
			bibleVerse.setBookId(bibleHelper.getCurrentWeeklyBookId(4));
			bibleVerse.setBook(bibleHelper.getCurrentWeeklyBookName(""));
			bibleVerse.setVerseText(bibleHelper.getCurrentWeeklyVerseText(""));
			bibleVerse.setVerse(bibleHelper.getCurrentWeeklyVerse(0));
			bibleVerse.setChapter(bibleHelper.getCurrentWeeklyChapter(0));
			Version version = new Version();
			version.setLanguage(bibleHelper.getCurrentLanguage("English"));
			version.setShortName(bibleHelper.getCurrentVersionName("KJV"));
			version.setId(bibleHelper.getCurrentVersionId(4));
			bibleVerse.setVersion(version);
			ArrayList<Bible> verses = new ArrayList<Bible>();
			verses.add(bibleVerse);
			HighlightListAdapter adt = new HighlightListAdapter(this.getActivity(), verses, true, this.getResources().getString(R.string.verse_of_the_week));
			lstView.setAdapter(adt);
		}
		else
		{
			final WeeklyVerseTask weeklyVerseTask = new WeeklyVerseTask(HomeFragment.this.getActivity());
			weeklyVerseTask.execute("");
		}
	}
	private class WeeklyVerseTask extends AsyncTask<String, Integer, org.theBOC.bocbible.Models.Bible> {

	    private Context context;
	    public WeeklyVerseTask(Context context) {
	        this.context = context;
	    }

	    @Override
	    protected org.theBOC.bocbible.Models.Bible doInBackground(String... params) 
	    {
	    	Version version = new Version();
	    	version.setLanguage(bibleHelper.getCurrentLanguage("English"));
			version.setShortName(bibleHelper.getCurrentVersionName("KJV"));
			version.setId(bibleHelper.getCurrentVersionId(4));
	    	return bibleDB.getWeeklyVerse(version, weekNumber);
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
	    	bibleHelper.setCurrentWeeklyBookId(result.getBookId());
			bibleHelper.setCurrentWeeklyBookName(result.getBook());
			bibleHelper.setCurrentWeeklyChapter(result.getChapter());
			bibleHelper.setCurrentWeeklyVerse(result.getVerse());
			bibleHelper.setCurrentWeeklyVerseText(result.getVerseText());
			bibleHelper.setCurrentWeeklyVerseWeek(weekNumber);
			ArrayList<Bible> verses = new ArrayList<Bible>();
			verses.add(result);
			HighlightListAdapter adt = new HighlightListAdapter(this.context, verses, true, this.context.getResources().getString(R.string.verse_of_the_week));
			lstView.setAdapter(adt);
    	}
	}
}

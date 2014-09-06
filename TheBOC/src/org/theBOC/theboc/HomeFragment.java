package org.theBOC.theboc;

import org.theBOC.theboc.common.BibleHelper;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HomeFragment extends Fragment {
	public HomeFragment() {
    }
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.home_fragment, container, false);
		BibleHelper bibleHelper = BibleHelper.getInstance(this.getActivity());
		String currentBookName = bibleHelper.getCurrentBookName("");
		if(!currentBookName.equalsIgnoreCase(""))
		{
			int currentChapter = bibleHelper.getCurrentChapter(1);
			int currentVerseInt = bibleHelper.getCurrentVerse(1);
			TextView txtView = (TextView) rootView.findViewById(R.id.latest_bible_reading);
	        txtView.setText("Where you left off: " +  currentBookName + " " + currentChapter + " : " + currentVerseInt);
		}
		else
		{
			LinearLayout reading_box = (LinearLayout) rootView.findViewById(R.id.latest_reading_box);
			reading_box.setVisibility(LinearLayout.GONE);
		}
        return rootView;
    }
}

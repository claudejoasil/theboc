package org.theBOC.theboc;

import java.util.ArrayList;

import org.theBOC.theboc.Adapters.VerseListAdapter;

import android.os.Build;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class Bible extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bible);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setIcon(R.drawable.ic_bible);
		ListView lstView = (ListView) findViewById(R.id.lst_bible_verses);
		lstView.setDivider(null);
		ArrayList<org.theBOC.theboc.Models.Bible> verses = new ArrayList<org.theBOC.theboc.Models.Bible>();
		org.theBOC.theboc.Models.Bible bible = new org.theBOC.theboc.Models.Bible();
		bible.setVerse(1);
		bible.setVerseText("Au commencement etait la parole, la parole etait avec Dieu");
		verses.add(bible);
		bible.setVerse(1);
		bible.setVerseText("Au commencement etait la parole, la parole etait avec Dieu");
		verses.add(bible);
		bible.setVerse(1);
		bible.setVerseText("Au commencement etait la parole, la parole etait avec Dieu");
		verses.add(bible);
		bible.setVerse(1);
		bible.setVerseText("Au commencement etait la parole, la parole etait avec Dieu");
		verses.add(bible);
		bible.setVerse(1);
		bible.setVerseText("Au commencement etait la parole, la parole etait avec Dieu");
		verses.add(bible);
		VerseListAdapter adt = new VerseListAdapter(this, verses);
		lstView.setAdapter(adt);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bible, menu);
		return true;
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // Handle action buttons
        switch(item.getItemId()) {
        case R.id.action_bible_book_chapter:
        	Toast.makeText(this, "Bible BOOK Chapter", Toast.LENGTH_LONG).show();
            return true;
        case R.id.action_bible_version:
        	Toast.makeText(this, "Bible Versions", Toast.LENGTH_LONG).show();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}

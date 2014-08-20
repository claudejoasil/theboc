package org.theBOC.theboc;

import java.util.ArrayList;

import org.theBOC.theboc.Adapters.VerseListAdapter;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.database.Cursor;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class Bible extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bible);
		BOCdb db = new BOCdb(this);
		Cursor cur = db.getVerses();
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setIcon(R.drawable.ic_bible);
		ListView lstView = (ListView) findViewById(R.id.lst_bible_verses);
		lstView.setDivider(null);
		ArrayList<org.theBOC.theboc.Models.Bible> verses = new ArrayList<org.theBOC.theboc.Models.Bible>();
		if (cur.moveToFirst()) {
            do {
            	org.theBOC.theboc.Models.Bible bible = new org.theBOC.theboc.Models.Bible();
                bible.setVerse(Integer.parseInt(cur.getString(0)));
                bible.setVerseText(cur.getString(1));
                verses.add(bible);
            } while (cur.moveToNext());
        }
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

package org.theBOC.theboc;

import java.util.ArrayList;

import org.theBOC.theboc.Adapters.VerseListAdapter;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class Bible extends Activity {
	public static final String currentValues = "BibleCurrentValues";
	public static final String Book = "bookKey"; 
	public static final String BookId = "bookIdKey"; 
	public static final String Chapter = "chapterKey"; 
	public static final String Verse = "verseKey"; 
	public static final String Version = "versionKey";
	public String currentBook;
	public int currentBookId;
	public int currentChapter;
	public int currentVerse;
	public String currentVersion;
	private SharedPreferences sharedpreferences;
	private Menu menu;
	private ListView lstView;
	private static org.theBOC.theboc.database.Bible bibleDB;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bible);
		if(this.currentBookId <= 0)
			this.setCurrentValues();
		lstView = (ListView) findViewById(R.id.lst_bible_verses);
		bibleDB = new org.theBOC.theboc.database.Bible(this);		
		ArrayList<org.theBOC.theboc.Models.Bible> verses = bibleDB.getVerses(this.currentBookId, this.currentChapter, this.currentVersion);
		
		this.bindBible(verses);
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setIcon(R.drawable.ic_bible);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bible, menu);
		this.menu = menu;
		if(this.currentBookId <= 0)
			this.setCurrentValues();
		this.updateActionTitles();
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
        case R.id.action_bible_next_chapter:
        	if(bibleDB != null)
        		bibleDB = new org.theBOC.theboc.database.Bible(this);
        	this.currentChapter++; //Not really need to check if last chapter...
    		ArrayList<org.theBOC.theboc.Models.Bible> verses = bibleDB.getVerses(this.currentBookId, this.currentChapter, this.currentVersion);
    		this.bindBible(verses);
    		this.updateActionTitles();
            return true;
        case R.id.action_bible_previous_chapter:
        	if(bibleDB != null)
        		bibleDB = new org.theBOC.theboc.database.Bible(this);
        	this.currentChapter--; //Not really need to check if last chapter...
    		ArrayList<org.theBOC.theboc.Models.Bible> verses1 = bibleDB.getVerses(this.currentBookId, this.currentChapter, this.currentVersion);
    		this.bindBible(verses1);
    		this.updateActionTitles();
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	private void setCurrentValues()
	{
		sharedpreferences = getSharedPreferences(currentValues, Context.MODE_PRIVATE);
		this.currentBook = sharedpreferences.getString(Book, "GEN");
		this.currentBookId = sharedpreferences.getInt(BookId, 1);
		this.currentChapter = sharedpreferences.getInt(Chapter, 1);
		this.currentVerse = sharedpreferences.getInt(Verse, 1);
		this.currentVersion = sharedpreferences.getString(Version, "KJV");
	}
	private void updateActionTitles()
	{
		if(this.menu != null)
		{
			MenuItem itemBookChapter = menu.findItem(R.id.action_bible_book_chapter);
			MenuItem itemVersion = menu.findItem(R.id.action_bible_version);
			itemBookChapter.setTitle(this.currentBook + " " +  this.currentChapter);
			itemVersion.setTitle(this.currentVersion);
		}
	}
	private void bindBible(ArrayList<org.theBOC.theboc.Models.Bible> verses)
	{
		lstView.setDivider(null);		
		VerseListAdapter adt = new VerseListAdapter(this, verses);
		lstView.setAdapter(adt);
	}
}

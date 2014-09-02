package org.theBOC.theboc;

import java.util.ArrayList;

import org.theBOC.theboc.Adapters.VerseListAdapter;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class Bible extends Activity {
	public static final String currentValues = "BibleCurrentValues";
	public static final String BookId = "bookIdKey"; 
	public static final String Chapter = "chapterKey"; 
	public static final String Verse = "verseKey"; 
	public static final String VersionId = "versionIdKey";
	public static final String Language = "languageKey";
	public static final String TextSize = "textSizeKey";
	public static final int lastBookId = 66;
	public int currentBookId;
	public int currentChapter;
	public int currentVerse;
	public int currentVersionId;
	public org.theBOC.theboc.Models.Book currentBookObj;
	public org.theBOC.theboc.Models.Version currentVersionObj;
	private SharedPreferences sharedpreferences;
	private Menu menu;
	private ListView lstView;
	private static org.theBOC.theboc.database.Bible bibleDB;
	private static org.theBOC.theboc.database.Book bookDB;
	private static org.theBOC.theboc.database.Version versionDB;
	private static ArrayList<org.theBOC.theboc.Models.Bible> m_verses;
	private static int m_textSize;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bible);
		lstView = (ListView) findViewById(R.id.lst_bible_verses);
		this.initDB();	
		sharedpreferences = getSharedPreferences(currentValues, Context.MODE_PRIVATE);
		m_textSize = m_textSize <= 0 ? 
				sharedpreferences.getInt(TextSize, (int)this.getResources().getDimension(R.dimen.bible_Default_text_size)) : 
				m_textSize;
		ActionBar actionBar = getActionBar();
		//actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setIcon(R.drawable.ic_bible);		
	}
    @Override
    protected void onResume() {
    	super.onResume();
    	this.setCurrentValues(-1);
    	ArrayList<org.theBOC.theboc.Models.Bible> verses = bibleDB.getVerses(this.currentBookId, this.currentChapter, this.currentVersionObj.getShortName());		
    	this.updateActionTitles();
    	this.bindBible(verses, 0);
    }
	@Override
	 protected void onStop(){
        super.onStop();
        SharedPreferences.Editor ed = sharedpreferences.edit();
        ed.putInt(BookId, this.currentBookId);
        ed.putInt(Chapter, this.currentChapter);
        ed.putInt(Verse, this.currentVerse);
        ed.putInt(VersionId, this.currentVersionId);
        ed.putInt(TextSize, m_textSize);
        ed.commit();
    }
	@Override
	protected void onPause() {
        super.onPause();
        SharedPreferences.Editor ed = sharedpreferences.edit();
        ed.putInt(BookId, this.currentBookId);
        ed.putInt(Chapter, this.currentChapter);
        ed.putInt(Verse, this.currentVerse);
        ed.putInt(VersionId, this.currentVersionId);
        ed.putInt(TextSize, m_textSize);
        ed.commit();
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bible, menu);
		this.menu = menu;
		if(this.currentBookId <= 0)
			this.setCurrentValues(-1);
		this.updateActionTitles();
		return true;
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // Handle action buttons
        switch(item.getItemId()) {
        case R.id.action_bible_book_chapter:
        	Intent bookIntent = new Intent(Bible.this, Books.class);
        	bookIntent.putExtra("ACTIVITY_TITLE", this.currentBookObj.getName() + " " +  this.currentChapter);
    		startActivity(bookIntent);
            return true;
        case R.id.action_bible_version:
        	Intent versionIntent = new Intent(Bible.this, Versions.class);
    		startActivity(versionIntent);
            return true;   
        case R.id.action_previous:
        	if(bibleDB == null)
        		bibleDB = new org.theBOC.theboc.database.Bible(this);
        	if(this.currentChapter > 1)
        		this.currentChapter--; 
        	else
        		this.setCurrentValues(this.currentBookObj.getBookId() - 1);
    		ArrayList<org.theBOC.theboc.Models.Bible> verses1 = bibleDB.getVerses(this.currentBookId, this.currentChapter, this.currentVersionObj.getShortName());
    		this.bindBible(verses1, 0);
    		this.updateActionTitles();
    		return true;
        case R.id.action_next:
        	if(bibleDB == null)
        		bibleDB = new org.theBOC.theboc.database.Bible(this);
        	if(this.currentChapter + 1 <= this.currentBookObj.getNumChapters())
        		this.currentChapter++;
        	else
        		this.setCurrentValues(this.currentBookObj.getBookId() + 1);        		 
    		ArrayList<org.theBOC.theboc.Models.Bible> verses = bibleDB.getVerses(this.currentBookId, this.currentChapter, this.currentVersionObj.getShortName());
    		this.bindBible(verses, 0);
    		this.updateActionTitles();
    		return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	public void bottomButtonClick(View view) 
	{
		switch(view.getId())
		{
			case R.id.btnMagnifyMinus:
	        	this.bindBible(null, -2);
	            break;
	        case R.id.btnMagnifyPlus: 
	        	this.bindBible(null, 2);
	            break;
		}
	}
	private void setCurrentValues(int bookId)
	{
		this.currentVersionId = sharedpreferences.getInt(VersionId, 4);
		if(versionDB == null)
			versionDB = new org.theBOC.theboc.database.Version(this);
		this.currentVersionObj = versionDB.getVersion(this.currentVersionId, null);
		
		if(bookDB == null)
			bookDB = new org.theBOC.theboc.database.Book(this);
		if(bookId == -1)
		{
			this.currentBookId = sharedpreferences.getInt(BookId, 1);
			this.currentBookObj = bookDB.getBook(this.currentBookId, this.currentVersionObj.getLanguage());
			this.currentChapter = sharedpreferences.getInt(Chapter, 1);
			this.currentVerse = sharedpreferences.getInt(Verse, 1);
		}			
		else
		{			
			if(bookId > lastBookId)
			{
				bookId = 1;
				this.currentBookObj = bookDB.getBook(bookId, this.currentVersionObj.getLanguage());
				this.currentChapter = 1;
			}
			else if(bookId <= 0)
			{
				bookId = lastBookId;
				this.currentBookObj = bookDB.getBook(bookId, this.currentVersionObj.getLanguage());
				this.currentChapter = this.currentBookObj.getNumChapters();
			}
			else
			{
				this.currentBookObj = bookDB.getBook(bookId, this.currentVersionObj.getLanguage());
				this.currentChapter = 1;
			}
			this.currentBookId = bookId;
			this.currentVerse = 1;
		}		
	}
	private void updateActionTitles()
	{
		if(this.menu != null)
		{
			MenuItem itemBookChapter = menu.findItem(R.id.action_bible_book_chapter);
			MenuItem itemVersion = menu.findItem(R.id.action_bible_version);
			itemBookChapter.setTitle(this.currentBookObj.getName() + " " +  this.currentChapter);
			itemVersion.setTitle(this.currentVersionObj.getShortName());
		}
	}
	private void bindBible(ArrayList<org.theBOC.theboc.Models.Bible> verses, int textSizeIncrement)
	{
		if(verses == null)
		{
			verses = m_verses;
		}
		else
		{
			m_verses = verses;
		}
		m_textSize += textSizeIncrement;
		lstView.setDivider(null);		
		VerseListAdapter adt = new VerseListAdapter(this, verses, (float)m_textSize);
		lstView.setAdapter(adt);
	}
	private void initDB()
	{
		bibleDB = new org.theBOC.theboc.database.Bible(this);
		bookDB = new org.theBOC.theboc.database.Book(this);
		versionDB = new org.theBOC.theboc.database.Version(this);
	}
}

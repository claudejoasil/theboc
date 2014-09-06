package org.theBOC.theboc;

import java.util.ArrayList;

import org.theBOC.theboc.Adapters.VerseListAdapter;
import org.theBOC.theboc.common.BibleHelper;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Bible extends Activity {
	public static final String currentValues = "BibleCurrentValues";
	public static final int lastBookId = 66;
	public static final int lastOldTestamentBookId = 39;
	public org.theBOC.theboc.Models.Book currentBookObj;
	public org.theBOC.theboc.Models.Version currentVersionObj;
	private BibleHelper bibleHelper;
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
		bibleHelper = BibleHelper.getInstance(this);
		lstView = (ListView) findViewById(R.id.lst_bible_verses);
		lstView.setOnItemClickListener(new OnItemClickListener() 
		{
	          public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	          {
	        	  org.theBOC.theboc.Models.Bible verseObj = m_verses.get(position);
	        	  bibleHelper.setCurrentVerse(verseObj.getVerse());
	          }
		});
		this.initDB();	
		m_textSize = m_textSize <= 0 ? 
				bibleHelper.getCurrentTextSize((int)this.getResources().getDimension(R.dimen.bible_Default_text_size)) : 
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
    	ArrayList<org.theBOC.theboc.Models.Bible> verses = 
    			bibleDB.getVerses(bibleHelper.getCurrentBookId(1), bibleHelper.getCurrentChapter(1), this.currentVersionObj.getShortName());		
    	this.updateActionTitles();
    	this.bindBible(verses, 0);
    }
	@Override
	 protected void onStop(){
        super.onStop();
        bibleHelper.persistCurrentValues();
    }
	@Override
	protected void onPause() {
        super.onPause();
        bibleHelper.persistCurrentValues();
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bible, menu);
		this.menu = menu;
		if(bibleHelper.getCurrentBookId(0) <= 0)
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
        	bookIntent.putExtra("ACTIVITY_TITLE", this.currentBookObj.getName() + " " +  bibleHelper.getCurrentChapter(1));
    		startActivity(bookIntent);
            return true;
        case R.id.action_bible_version:
        	Intent versionIntent = new Intent(Bible.this, Versions.class);
    		startActivity(versionIntent);
            return true;   
        case R.id.action_previous:
        	if(bibleDB == null)
        		bibleDB = new org.theBOC.theboc.database.Bible(this);
        	if(bibleHelper.getCurrentChapter(1) > 1)
        		bibleHelper.incrementChapter(-1);
        	else
        	{
        		bibleHelper.incrementChapter(-2); //SET Chapter to -1 at this point chapter is 1
        		if(this.currentBookObj.getBookId() == lastOldTestamentBookId + 1)
        			bibleHelper.setCurrentTestament(1);
        		this.setCurrentValues(this.currentBookObj.getBookId() - 1);
        	}
    		ArrayList<org.theBOC.theboc.Models.Bible> verses1 = 
    				bibleDB.getVerses(bibleHelper.getCurrentBookId(0), bibleHelper.getCurrentChapter(0), this.currentVersionObj.getShortName());
    		this.bindBible(verses1, 0);
    		this.updateActionTitles();
    		return true;
        case R.id.action_next:
        	if(bibleDB == null)
        		bibleDB = new org.theBOC.theboc.database.Bible(this);
        	if(bibleHelper.getCurrentChapter(0) + 1 <= this.currentBookObj.getNumChapters())
        		bibleHelper.incrementChapter(1);
        	else
        	{
        		if(this.currentBookObj.getBookId() == lastOldTestamentBookId)
        			bibleHelper.setCurrentTestament(2);
        		this.setCurrentValues(this.currentBookObj.getBookId() + 1);  
        	}
    		ArrayList<org.theBOC.theboc.Models.Bible> verses = 
    				bibleDB.getVerses(bibleHelper.getCurrentBookId(0), bibleHelper.getCurrentChapter(0), this.currentVersionObj.getShortName());
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
		if(versionDB == null)
			versionDB = new org.theBOC.theboc.database.Version(this);
		this.currentVersionObj = versionDB.getVersion(bibleHelper.getCurrentVersionId(4), null);
		
		if(bookDB == null)
			bookDB = new org.theBOC.theboc.database.Book(this);
		if(bookId == -1)
		{
			bibleHelper.getCurrentBookId(1);
			this.currentBookObj = bookDB.getBook(bibleHelper.getCurrentBookId(1), this.currentVersionObj.getLanguage());
			bibleHelper.getCurrentChapter(1);
			bibleHelper.getCurrentVerse(1);
		}			
		else
		{			
			if(bookId > lastBookId)
			{
				bookId = 1;
				bibleHelper.setCurrentTestament(1);
				this.currentBookObj = bookDB.getBook(bookId, this.currentVersionObj.getLanguage());
				bibleHelper.setCurrentChapter(1);
			}
			else if(bookId <= 0)
			{
				bookId = lastBookId;
				bibleHelper.setCurrentTestament(2);
				this.currentBookObj = bookDB.getBook(bookId, this.currentVersionObj.getLanguage());
				bibleHelper.setCurrentChapter(this.currentBookObj.getNumChapters());
			}
			else if(bibleHelper.getCurrentChapter(1) == -1)
			{
				this.currentBookObj = bookDB.getBook(bookId, this.currentVersionObj.getLanguage());
				bibleHelper.setCurrentChapter(this.currentBookObj.getNumChapters());
			}
			else
			{
				this.currentBookObj = bookDB.getBook(bookId, this.currentVersionObj.getLanguage());
				bibleHelper.setCurrentChapter(1);
			}
			bibleHelper.setCurrentBookId(bookId);
			bibleHelper.setCurrentVerse(1);
		}
		bibleHelper.setCurrentBookName(this.currentBookObj.getName());
	}
	private void updateActionTitles()
	{
		if(this.menu != null)
		{
			MenuItem itemBookChapter = menu.findItem(R.id.action_bible_book_chapter);
			MenuItem itemVersion = menu.findItem(R.id.action_bible_version);
			itemBookChapter.setTitle(this.currentBookObj.getName() + " " +  bibleHelper.getCurrentChapter(1));
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

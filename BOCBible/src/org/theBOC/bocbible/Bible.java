package org.theBOC.bocbible;
import java.util.ArrayList;

import org.theBOC.bocbible.R;
import org.theBOC.bocbible.Adapters.VerseListAdapter;
import org.theBOC.bocbible.common.BibleHelper;
import org.theBOC.bocbible.common.MiscHelper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Bible extends Activity {
	public static final String currentValues = "BibleCurrentValues";
	public static final int lastBookId = 66;
	public static final int lastOldTestamentBookId = 39;
	public org.theBOC.bocbible.Models.Book currentBookObj;
	public static org.theBOC.bocbible.Models.Version currentVersionObj;
	private BibleHelper bibleHelper;
	private MiscHelper miscHelper;
	private Menu menu;
	private ListView lstView;
	private static org.theBOC.bocbible.database.Bible bibleDB;
	private static org.theBOC.bocbible.database.Book bookDB;
	private static org.theBOC.bocbible.database.Version versionDB;
	private static ArrayList<org.theBOC.bocbible.Models.Bible> m_verses;
	private static int m_textSize;
	ProgressDialog mProgressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bible);
		bibleHelper = BibleHelper.getInstance(this);
		miscHelper = MiscHelper.getInstance(this);
		miscHelper.reloadBiblePage = true;
		lstView = (ListView) findViewById(R.id.lst_bible_verses);
		lstView.setOnItemClickListener(new OnItemClickListener() 
		{
	          public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	          {
	        	  org.theBOC.bocbible.Models.Bible verseObj = m_verses.get(position);
	        	  bibleHelper.setCurrentVerse(verseObj.getVerse());
	          }
		});
		lstView.setOnScrollListener(new OnScrollListener(){
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if(scrollState == OnScrollListener.SCROLL_STATE_IDLE)
				{
					bibleHelper.setCurrentVerse(view.getFirstVisiblePosition() + 1);
				}
				
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				
			}
		  });
		this.initDB();	
		m_textSize = m_textSize <= 0 ? 
				bibleHelper.getCurrentTextSize((int)this.getResources().getDimension(R.dimen.bible_Default_text_size)) : 
				m_textSize;
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setIcon(R.drawable.ic_bible);		
	}
    @Override
    protected void onResume() {
    	super.onResume();
    	if(miscHelper.reloadBiblePage)
    	{
	    	this.setCurrentValues(-1);
	    	this.updateActionTitles();
	    	this.bindBible(0);
    	}
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
        /*case R.id.action_previous:
        	if(bibleDB == null)
        		bibleDB = new org.theBOC.bocbible.database.Bible(this);
        	if(bibleHelper.getCurrentChapter(1) > 1)
        		bibleHelper.incrementChapter(-1);
        	else
        	{
        		bibleHelper.incrementChapter(-2); //SET Chapter to -1 at this point chapter is 1
        		if(this.currentBookObj.getBookId() == lastOldTestamentBookId + 1)
        			bibleHelper.setCurrentTestament(1);
        		this.setCurrentValues(this.currentBookObj.getBookId() - 1);
        	}
    		this.bindBible(0);
    		this.updateActionTitles();
    		return true;
        case R.id.action_next:
        	if(bibleDB == null)
        		bibleDB = new org.theBOC.bocbible.database.Bible(this);
        	if(bibleHelper.getCurrentChapter(0) + 1 <= this.currentBookObj.getNumChapters())
        		bibleHelper.incrementChapter(1);
        	else
        	{
        		if(this.currentBookObj.getBookId() == lastOldTestamentBookId)
        			bibleHelper.setCurrentTestament(2);
        		this.setCurrentValues(this.currentBookObj.getBookId() + 1);  
        	}
    		this.bindBible(0);
    		this.updateActionTitles();
    		return true; */
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	public void bottomButtonClick(View view) 
	{
		switch(view.getId())
		{
			case R.id.btnMagnifyMinus:
	        	this.bindBible(-2);
	            break;
	        case R.id.btnMagnifyPlus: 
	        	this.bindBible(2);
	            break;
	        case R.id.btnPreviousChapter:
	        	if(bibleDB == null)
	        		bibleDB = new org.theBOC.bocbible.database.Bible(this);
	        	if(bibleHelper.getCurrentChapter(1) > 1)
	        		bibleHelper.incrementChapter(-1);
	        	else
	        	{
	        		bibleHelper.incrementChapter(-2); //SET Chapter to -1 at this point chapter is 1
	        		if(this.currentBookObj.getBookId() == lastOldTestamentBookId + 1)
	        			bibleHelper.setCurrentTestament(1);
	        		this.setCurrentValues(this.currentBookObj.getBookId() - 1);
	        	}
	    		this.bindBible(0);
	    		this.updateActionTitles();
	            break;
	        case R.id.btnNextChapter: 
	        	if(bibleDB == null)
	        		bibleDB = new org.theBOC.bocbible.database.Bible(this);
	        	if(bibleHelper.getCurrentChapter(0) + 1 <= this.currentBookObj.getNumChapters())
	        		bibleHelper.incrementChapter(1);
	        	else
	        	{
	        		if(this.currentBookObj.getBookId() == lastOldTestamentBookId)
	        			bibleHelper.setCurrentTestament(2);
	        		this.setCurrentValues(this.currentBookObj.getBookId() + 1);  
	        	}
	    		this.bindBible(0);
	    		this.updateActionTitles();
	            break;
		}
	}
	private void setCurrentValues(int bookId)
	{
		if(versionDB == null)
			versionDB = new org.theBOC.bocbible.database.Version(this);
		currentVersionObj = versionDB.getVersion(bibleHelper.getCurrentVersionId(4), null);
		
		if(bookDB == null)
			bookDB = new org.theBOC.bocbible.database.Book(this);
		if(bookId == -1)
		{
			this.currentBookObj = bookDB.getBook(bibleHelper.getCurrentBookId(1), currentVersionObj.getLanguage());
			bibleHelper.getCurrentChapter(1);
			bibleHelper.getCurrentVerse(1);
		}			
		else
		{			
			if(bookId > lastBookId)
			{
				bookId = 1;
				bibleHelper.setCurrentTestament(1);
				this.currentBookObj = bookDB.getBook(bookId, currentVersionObj.getLanguage());
				bibleHelper.setCurrentChapter(1);
			}
			else if(bookId <= 0)
			{
				bookId = lastBookId;
				bibleHelper.setCurrentTestament(2);
				this.currentBookObj = bookDB.getBook(bookId, currentVersionObj.getLanguage());
				bibleHelper.setCurrentChapter(this.currentBookObj.getNumChapters());
			}
			else if(bibleHelper.getCurrentChapter(1) == -1)
			{
				this.currentBookObj = bookDB.getBook(bookId, currentVersionObj.getLanguage());
				bibleHelper.setCurrentChapter(this.currentBookObj.getNumChapters());
			}
			else
			{
				this.currentBookObj = bookDB.getBook(bookId, currentVersionObj.getLanguage());
				bibleHelper.setCurrentChapter(1);
			}
			bibleHelper.setCurrentBookId(bookId);
			bibleHelper.setCurrentVerse(1);
		}
		bibleHelper.setCurrentBookName(this.currentBookObj.getName());
		bibleHelper.setCurrentVersionName(currentVersionObj.getShortName());
	}
	private void updateActionTitles()
	{
		if(this.menu != null)
		{
			MenuItem itemBookChapter = menu.findItem(R.id.action_bible_book_chapter);
			MenuItem itemVersion = menu.findItem(R.id.action_bible_version);
			itemBookChapter.setTitle(this.currentBookObj.getName() + " " +  bibleHelper.getCurrentChapter(1));
			itemVersion.setTitle(currentVersionObj.getShortName());
		}
	}
	private void bindBible(int textSizeIncrement)
	{
		mProgressDialog = new ProgressDialog(Bible.this);
		mProgressDialog.setMessage("please wait...");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setCancelable(false);
		final GetBibleVersesTask getBibleVersesTask = new GetBibleVersesTask(Bible.this);
		getBibleVersesTask.execute(textSizeIncrement);
	}
	private void initDB()
	{
		bibleDB = new org.theBOC.bocbible.database.Bible(this);
		bookDB = new org.theBOC.bocbible.database.Book(this);
		versionDB = new org.theBOC.bocbible.database.Version(this);
	}
	
	private class GetBibleVersesTask extends AsyncTask<Integer, Integer, VerseListAdapter> {
	    private Context context;
	    public GetBibleVersesTask(Context context) {
	        this.context = context;
	    }

	    @Override
	    protected VerseListAdapter doInBackground(Integer... textSize) {
	        
	    	if(textSize[0] == 0)
				m_verses = bibleDB.getVerses(bibleHelper.getCurrentBookId(1), bibleHelper.getCurrentChapter(1), currentVersionObj.getShortName());
	    	m_textSize += textSize[0];
	    	VerseListAdapter adt = new VerseListAdapter(this.context, m_verses, (float)m_textSize, currentVersionObj.getShortName());
	    	return adt;
	    }
	    
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        mProgressDialog.show();
	    }

	    @Override
	    protected void onProgressUpdate(Integer... progress) {
	        super.onProgressUpdate(progress);
	        // if we get here, length is known, now set indeterminate to false
	        mProgressDialog.setIndeterminate(false);
	        mProgressDialog.setMax(100);
	        mProgressDialog.setProgress(progress[0]);
	    }

	    @Override
	    protected void onPostExecute(VerseListAdapter adt) {
	        mProgressDialog.dismiss();
	        lstView.setDivider(null);		
			lstView.setAdapter(adt);
			lstView.setSelection(bibleHelper.getCurrentVerse(1) - 1);
			//lstView.smoothScrollToPosition(bibleHelper.getCurrentVerse(1) - 1);
	    }
	}
}

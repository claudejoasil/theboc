package org.theBOC.bocbible;
import java.util.ArrayList;

import org.theBOC.bocbible.R;
import org.theBOC.bocbible.Adapters.VerseListAdapter;
import org.theBOC.bocbible.common.BibleHelper;
import org.theBOC.bocbible.common.MiscHelper;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Bible  extends Fragment {
	public static final String currentValues = "BibleCurrentValues";
	public static final int lastBookId = 66;
	public static final int lastOldTestamentBookId = 39;
	public org.theBOC.bocbible.Models.Book currentBookObj;
	public static org.theBOC.bocbible.Models.Version currentVersionObj;
	private BibleHelper bibleHelper;
	private MiscHelper miscHelper;
	private Menu menu;
	private ListView lstView;
	private ImageButton btnMagnifyMinus;
	private ImageButton btnMagnifyPlus;
	private ImageButton btnNextChapter;
	private ImageButton btnPreviousChapter;
	/*private int autoScrollSpeed = 0;
	private CountDownTimer autoScrollCountDown;
	private final int autoScrollSpeedIncrement = 20;
	private final long totalScrollTime = Long.MAX_VALUE; */
	private static org.theBOC.bocbible.database.Bible bibleDB;
	private static org.theBOC.bocbible.database.Book bookDB;
	private static org.theBOC.bocbible.database.Version versionDB;
	private static ArrayList<org.theBOC.bocbible.Models.Bible> m_verses;
	private static int m_textSize;
	ProgressDialog mProgressDialog;
	View rootView;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.activity_bible, container, false);
		super.onCreate(savedInstanceState);
		bibleHelper = BibleHelper.getInstance(this.getActivity());
		miscHelper = MiscHelper.getInstance(this.getActivity());
		miscHelper.reloadBiblePage = true;
		this.initControls();
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
		ActionBar actionBar = this.getActivity().getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setIcon(R.drawable.ic_bible);	
		return rootView;
	}
	
	
	@Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);  
	 }
    @Override
	public void onResume() {
    	super.onResume();
    	if(miscHelper.reloadBiblePage)
    	{
	    	this.setCurrentValues(-1);
	    	this.updateActionTitles();
	    	this.bindBible(0);
    	}
    }
	@Override
	public void onStop(){
        super.onStop();
        bibleHelper.persistCurrentValues();
    }
	@Override
	public void onPause() {
        super.onPause();
        bibleHelper.persistCurrentValues();
    }
	
	@Override
	public void onDestroy() {
	    super.onDestroy();
	    if (bibleDB != null) {
	    	bibleDB.close();
	    }
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.bible, menu);
		this.menu = menu;
		if(bibleHelper.getCurrentBookId(0) <= 0)
			this.setCurrentValues(-1);
		this.updateActionTitles();
		super.onCreateOptionsMenu(menu, inflater);
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
         // Handle action buttons
        switch(item.getItemId()) {
        case R.id.action_bible_book_chapter:
        	Intent bookIntent = new Intent(Bible.this.getActivity(), Books.class);
        	bookIntent.putExtra("ACTIVITY_TITLE", this.currentBookObj.getName() + " " +  bibleHelper.getCurrentChapter(1));
    		startActivity(bookIntent);
            return true;
        case R.id.action_bible_version:
        	Intent versionIntent = new Intent(Bible.this.getActivity(), Versions.class);
    		startActivity(versionIntent);
            return true; 
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	public void bottomButtonClick(View view) 
	{
		activeButtons(false);
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
	        		bibleDB = new org.theBOC.bocbible.database.Bible(this.getActivity());
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
	        		bibleDB = new org.theBOC.bocbible.database.Bible(this.getActivity());
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
	        /*case R.id.btnScrollPlus:
	        	this.autoScrollSpeed += this.autoScrollSpeedIncrement;
	        	this.autoScroll();
	        	break;
	        case R.id.btnScrollMinus:
	        	this.autoScrollSpeed = this.autoScrollSpeed > this.autoScrollSpeedIncrement ? 
	        			this.autoScrollSpeed - this.autoScrollSpeedIncrement : 0;
	        	if(this.autoScrollSpeed > this.autoScrollSpeedIncrement)
	        	{
	        		this.autoScrollSpeed -= this.autoScrollSpeedIncrement;
	        		this.autoScroll();
	        	}
	        	else
	        	{
	        		autoScrollCountDown.cancel();
	        	}
	        	
	        	break;*/
		}
	}
	private void setCurrentValues(int bookId)
	{
		if(versionDB == null)
			versionDB = new org.theBOC.bocbible.database.Version(this.getActivity());
		currentVersionObj = versionDB.getVersion(bibleHelper.getCurrentVersionId(4), null);
		
		if(bookDB == null)
			bookDB = new org.theBOC.bocbible.database.Book(this.getActivity());
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
			String bookName = this.currentBookObj != null ? this.currentBookObj.getName() : "";
			String versionName = currentVersionObj != null ? currentVersionObj.getShortName() : "";
			itemBookChapter.setTitle(bookName + " " +  bibleHelper.getCurrentChapter(1));
			itemVersion.setTitle(versionName);
		}
	}
	private void bindBible(int textSizeIncrement)
	{
		mProgressDialog = new ProgressDialog(Bible.this.getActivity());
		mProgressDialog.setMessage("please wait...");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setCancelable(false);
		mProgressDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		final GetBibleVersesTask getBibleVersesTask = new GetBibleVersesTask(Bible.this.getActivity());
		getBibleVersesTask.execute(textSizeIncrement);
	}
	private void initDB()
	{
		bibleDB = new org.theBOC.bocbible.database.Bible(this.getActivity());
		bookDB = new org.theBOC.bocbible.database.Book(this.getActivity());
		versionDB = new org.theBOC.bocbible.database.Version(this.getActivity());
	}

	private void initControls()
	{
		lstView = (ListView) rootView.findViewById(R.id.lst_bible_verses);
		btnMagnifyMinus = (ImageButton) rootView.findViewById(R.id.btnMagnifyMinus);
		btnMagnifyMinus.setOnClickListener(new bottomButtonClickListener());
		btnMagnifyPlus = (ImageButton) rootView.findViewById(R.id.btnMagnifyPlus);
		btnMagnifyPlus.setOnClickListener(new bottomButtonClickListener());
		btnNextChapter = (ImageButton) rootView.findViewById(R.id.btnNextChapter);
		btnNextChapter.setOnClickListener(new bottomButtonClickListener());
		btnPreviousChapter = (ImageButton) rootView.findViewById(R.id.btnPreviousChapter);
		btnPreviousChapter.setOnClickListener(new bottomButtonClickListener());
	}
	
	public class bottomButtonClickListener implements OnClickListener
	{
		@Override
		public void onClick(View v) {
			bottomButtonClick(v);	
		}
	}
	/*private void autoScroll()
	{
		
		autoScrollCountDown = new CountDownTimer(totalScrollTime, autoScrollSpeed ) {
            public void onTick(long millisUntilFinished) {
            	lstView.scrollBy(0, autoScrollSpeed);
            	if(lstView.getLastVisiblePosition() == lstView.getCount())
            		autoScrollCountDown.cancel();
            }

	        public void onFinish() {
	            //you can add code for restarting timer here
	        }
	    };
	    
		lstView.post(new Runnable() {
            @Override
            public void run() {
            	autoScrollCountDown.start();
            }
        });
	}*/
	private class GetBibleVersesTask extends AsyncTask<Integer, Integer, VerseListAdapter> {
	    private Context context;
	    public GetBibleVersesTask(Context context) {
	        this.context = context;
	    }

	    @Override
	    protected VerseListAdapter doInBackground(Integer... textSize) {
	        String[] dualVersions = new String[2];
	        dualVersions[0] = currentVersionObj.getShortName();
	        if(bibleHelper.getCurrentVersionId2() > 0)
			{
				dualVersions[1] = bibleHelper.getCurrentVersionName2();
			}
	    	if(textSize[0] == 0)
	    	{
				if(bibleHelper.getCurrentVersionId2() > 0)
				{
					m_verses = bibleDB.getDualVersionVerses(bibleHelper.getCurrentBookId(1), bibleHelper.getCurrentChapter(1), dualVersions);
	    		}
				else
				{
					m_verses = bibleDB.getVerses(bibleHelper.getCurrentBookId(1), bibleHelper.getCurrentChapter(1), dualVersions[0]);
				}
	    	}
	    	m_textSize += textSize[0];
	    	VerseListAdapter adt = new VerseListAdapter(this.context, m_verses, (float)m_textSize, dualVersions);
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
			activeButtons(true);
			//lstView.smoothScrollToPosition(bibleHelper.getCurrentVerse(1) - 1);
	    }
	}
	private void activeButtons(boolean activate)
	{
		btnMagnifyMinus.setActivated(activate);
		btnMagnifyPlus.setActivated(activate);
		btnNextChapter.setActivated(activate);
		btnPreviousChapter.setActivated(activate);
	}
}

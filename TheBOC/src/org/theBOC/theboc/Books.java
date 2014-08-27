package org.theBOC.theboc;

import java.util.ArrayList;

import org.theBOC.theboc.Adapters.BookListAdapter;
import org.theBOC.theboc.Models.Book;

import android.os.Bundle;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class Books extends Activity {
	public static Context appContext;
	private static org.theBOC.theboc.database.Book bookDB;
	private ArrayList<Book> books;
	private SharedPreferences sharedpreferences;
	public static final String currentValues = "BibleCurrentValues";
	public static final String BookId = "bookIdKey";
	public static final String Chapter = "chapterKey";
	public static final String Language = "languageKey";
	/** Called when the activity is first created. */
	@Override
		public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_books);
		appContext = this;
		//ActionBar gets initiated
		ActionBar actionbar = getActionBar();
		//Tell the ActionBar we want to use Tabs.
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionbar.setDisplayShowHomeEnabled(false);
		actionbar.setTitle("Bible Books");
		//initiating both tabs and set text to it.
		ActionBar.Tab NTTab = actionbar.newTab().setText("New Testament");
		ActionBar.Tab OTTab = actionbar.newTab().setText("Old Testament");
	
		//create the two fragments we want to use for display content
		Fragment NTFragment = new TestamentFragment(2);
		Fragment OTFragment = new TestamentFragment(1);
	
		//set the Tab listener. Now we can listen for clicks.
		NTTab.setTabListener(new MyTabsListener(NTFragment));
		OTTab.setTabListener(new MyTabsListener(OTFragment));
		actionbar.addTab(OTTab);
		actionbar.addTab(NTTab);
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
			// Inflate the menu; this adds items to the action bar if it is present.
			getMenuInflater().inflate(R.menu.boc_pup, menu);
			return true;
		}
	
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			// Handle action bar item clicks here. The action bar will
			// automatically handle clicks on the Home/Up button, so long
			// as you specify a parent activity in AndroidManifest.xml.
			int id = item.getItemId();
			if (id == R.id.action_settings) {
				return true;
			}
			if (id == R.id.action_cancel) {
				Intent bibleIntent = new Intent(Books.this, Bible.class);
	        	startActivity(bibleIntent);
				return true;
			}
			return super.onOptionsItemSelected(item);
		}
		
		class TestamentFragment extends Fragment {
			private ListView lstView;
			private int theTestament;
			public TestamentFragment(int value){
				this.theTestament = value;
			}
		    @Override
		    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		        // Inflate the layout for this fragment
		    	if(bookDB == null)
		    		bookDB = new org.theBOC.theboc.database.Book(appContext);
		    	sharedpreferences = getSharedPreferences(currentValues, Context.MODE_PRIVATE);
		    	String language = sharedpreferences.getString(Language, "");
				books = bookDB.getBooks(this.theTestament, language);
				View frag = inflater.inflate(R.layout.testament_fragment, container, false);
				lstView = (ListView) frag.findViewById(R.id.lst_books);
				int[] colors = {0, 0xFFCCCCCC, 0}; 
				lstView.setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
				lstView.setDividerHeight(1);
				lstView.setOnItemClickListener(new OnItemClickListener() 
				{
			          public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			          {
			        	  Book book = books.get(position);
			        	  sharedpreferences.edit().putInt(BookId, book.getBookId()).apply();
			        	  Intent bibleIntent = new Intent(Books.this, Bible.class);
			        	  startActivity(bibleIntent);
			          }
				});
				BookListAdapter adt = new BookListAdapter(appContext, books);
				lstView.setAdapter(adt);
				return frag;
		    }

	 	}
	}

	class MyTabsListener implements ActionBar.TabListener {
		public Fragment fragment;

		public MyTabsListener(Fragment fragment) {
			this.fragment = fragment;
		}

	    @Override
	    public void onTabReselected(Tab tab, FragmentTransaction ft) {
	    
	    }
	
	    @Override
	    public void onTabSelected(Tab tab, FragmentTransaction ft) {
	    ft.replace(R.id.books_container, fragment);
	    }
	
	    @Override
	    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	    ft.remove(fragment);
	    }
    }  

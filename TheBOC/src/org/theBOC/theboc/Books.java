package org.theBOC.theboc;


import android.os.Bundle;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;

public class Books extends Activity {
	public static Context appContext;
	public static final String BookId = "bookIdKey";
	public static final String Chapter = "chapterKey";
	public static final String Language = "languageKey";
	public static final String testament = "testamentKey";
	private SharedPreferences sharedpreferences;
	public static final String currentValues = "BibleCurrentValues";
	/** Called when the activity is first created. */
	@Override
		public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_books);
		sharedpreferences = getSharedPreferences(currentValues, Context.MODE_PRIVATE);
		int currentTestament = sharedpreferences.getInt(testament, 1);
		appContext = this;
		//ActionBar gets initiated
		ActionBar actionbar = getActionBar();
		//Tell the ActionBar we want to use Tabs.
		actionbar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionbar.setDisplayShowHomeEnabled(false);
		Intent i = getIntent();
		String title = i.getStringExtra("ACTIVITY_TITLE");
		actionbar.setTitle(title != null && !title.equals("") ? title : "Bible Books");
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
		actionbar.setSelectedNavigationItem(currentTestament - 1);
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
				this.finish();
				//Intent bibleIntent = new Intent(Books.this, Bible.class);
	        	//startActivity(bibleIntent);
				return true;
			}
			return super.onOptionsItemSelected(item);
		}
	}

	class MyTabsListener implements ActionBar.TabListener {
		public Fragment fragment;
		public MyTabsListener() {
		}
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

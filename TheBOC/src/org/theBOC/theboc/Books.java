package org.theBOC.theboc;

import android.os.Bundle;
import android.app.Activity;
import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.view.Menu;

public class Books extends Activity implements
ActionBar.TabListener{
	private static final String STATE_SELECTED_NAVIGATION_ITEM = "selected_navigation_item";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_books);
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    actionBar.addTab(actionBar.newTab().setText(R.string.new_testament)
	        .setTabListener(this));
	    actionBar.addTab(actionBar.newTab().setText(R.string.old_testament)
	        .setTabListener(this));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.books, menu);
		return true;
	}
	@Override
	  public void onRestoreInstanceState(Bundle savedInstanceState) {
	    // Restore the previously serialized current tab position.
	    if (savedInstanceState.containsKey(STATE_SELECTED_NAVIGATION_ITEM)) {
	      getActionBar().setSelectedNavigationItem(savedInstanceState.getInt(STATE_SELECTED_NAVIGATION_ITEM));
	    }
	  }
	@Override
	  public void onSaveInstanceState(Bundle outState) {
	    // Serialize the current tab position.
	    outState.putInt(STATE_SELECTED_NAVIGATION_ITEM, getActionBar()
	        .getSelectedNavigationIndex());
	  }
	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) 
	{
	    // When the given tab is selected, show the tab contents in the
	    // container view.
	    
	  }

	  @Override
	  public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) 
	  {
	  }

	  @Override
	  public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) 
	  {
	  }
}

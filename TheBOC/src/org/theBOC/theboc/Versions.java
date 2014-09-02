package org.theBOC.theboc;

import java.util.ArrayList;

import org.theBOC.theboc.Adapters.VersionListAdapter;
import org.theBOC.theboc.Models.Version;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.view.View;

public class Versions extends Activity {
	private static org.theBOC.theboc.database.Version versionDB;
	private ListView lstView;
	private ArrayList<Version> versions;
	private SharedPreferences sharedpreferences;
	public static final String currentValues = "BibleCurrentValues";
	public static final String VersionId = "versionIdKey";
	public static final String Language = "languageKey";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_versions);
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setTitle("Bible Versions");
		if(versionDB == null)
			versionDB = new org.theBOC.theboc.database.Version(this);
		versions = versionDB.getVersions(null, true);
		lstView = (ListView) findViewById(R.id.lst_bible_versions);
		int[] colors = {0, 0xFFCCCCCC, 0}; 
		lstView.setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
		lstView.setDividerHeight(1);
		final Context context = this;
		lstView.setOnItemClickListener(new OnItemClickListener() 
		{
	          public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	          {
	        	  Version version = versions.get(position);
	        	  if(!version.getIsGroupHeader())
	        	  {
	        		  sharedpreferences = getSharedPreferences(currentValues, Context.MODE_PRIVATE);
	        		  sharedpreferences.edit().putInt(VersionId, version.getId()).apply();
	        		  sharedpreferences.edit().putString(Language, version.getLanguage()).apply();
	        		  ((Activity) context).finish();
	        	  }
	          }
		});
		VersionListAdapter adt = new VersionListAdapter(this, versions);
		lstView.setAdapter(adt);
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
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}

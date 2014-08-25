package org.theBOC.theboc;

import java.util.ArrayList;

import org.theBOC.theboc.Adapters.VersionListAdapter;
import org.theBOC.theboc.Models.Version;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

public class Versions extends Activity {
	private static org.theBOC.theboc.database.Version versionDB;
	private ListView lstView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_versions);
		if(versionDB == null)
			versionDB = new org.theBOC.theboc.database.Version(this);
		ArrayList<Version> versions = versionDB.getVersions(null, true);
		lstView = (ListView) findViewById(R.id.lst_bible_versions);
		lstView.setDivider(null);		
		VersionListAdapter adt = new VersionListAdapter(this, versions);
		lstView.setAdapter(adt);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.versions, menu);
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
		return super.onOptionsItemSelected(item);
	}
}

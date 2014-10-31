package org.theBOC.bocbible;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.theBOC.bocbible.BOCDialogFrag.BOCDialogFragListener;
import org.theBOC.bocbible.R;
import org.theBOC.bocbible.Adapters.VersionListAdapter;
import org.theBOC.bocbible.Adapters.VersionListAdapter.ParallelVersionsListener;
import org.theBOC.bocbible.Models.Version;
import org.theBOC.bocbible.common.BibleHelper;
import org.theBOC.bocbible.common.MiscHelper;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;

public class Versions extends FragmentActivity {
	private static org.theBOC.bocbible.database.Version versionDB;
	private ListView lstView;
	private LinearLayout parallelVersionControls;
	private TextView txtVersion_1;
	private TextView txtVersion_2;
	private Button btnSwitch;
	private Button btnDone;
	private RadioButton radioSingleVersion;
	private RadioButton radioParallelVersions;
	private RadioGroup radioGroupVersions;
	private ArrayList<Version> versions;
	private BibleHelper bibleHelper;
	private MiscHelper miscHelper;
	private Version m_clickedVersion;
	private static ProgressDialog mProgressDialog;
	private static int singleVersion = -1; // Value is not set yet that takes care of phone orientation change which would have an undesirable behavior
	private static Version mVersion_1 = null;
	private static Version mVersion_2 = null;
	private int numVersionsSelected = 1; // The currentVersion is selected by default
	private VersionListAdapter adt;
	public static final String versionBaseUrl = "http://www.theboc.org/downloads/bible";
	private boolean hideVersionOptions;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		hideVersionOptions = intent.getBooleanExtra("HIDEVERSIONOPTIONS", false);
		setContentView(R.layout.activity_versions);
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setTitle("Bible Versions");
		bibleHelper = BibleHelper.getInstance(this);
		miscHelper = MiscHelper.getInstance(this);
		this.initControls();
		int[] colors = {0, 0xFFCCCCCC, 0}; 
		lstView.setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
		lstView.setDividerHeight(1);
		lstView.setOnItemClickListener(new OnItemClickListener() 
		{
	          public void onItemClick(AdapterView<?> parent, final View view, int position, long id) 
	          {
	        	  CheckBox chk = (CheckBox) view.findViewById(R.id.version_select);
	        	  if(chk != null && chk.isShown())
	        	  {
	        		  if(chk.isEnabled()) 
	        			  chk.toggle();
	        		  return;
	        	  }
	        	  m_clickedVersion = versions.get(position);
	        	  if(!m_clickedVersion.getIsGroupHeader())
	        	  {
	        		  if(m_clickedVersion.getIsAvailable())
	        		  {
		        		  bibleHelper.setCurrentVersionId(m_clickedVersion.getId());
		        		  bibleHelper.setCurrentVersionName(m_clickedVersion.getShortName());
		        		  bibleHelper.setCurrentLanguage(m_clickedVersion.getLanguage());
		        		  bibleHelper.setCurrentVersionId2(-1);
		        		  bibleHelper.setCurrentVersionName2("");
		        		  miscHelper.reloadBiblePage = true;
		        		  
		        		  unsetValuesAndFinish();
	        		  }
	        		  else
	        		  {
	        			  BOCDialogFrag dialFrag = new BOCDialogFrag();
	        			  String message = getResources().getString(R.string.version_unavailable);
	        			  dialFrag.setDialogMessage(String.format(message, m_clickedVersion.getName()));
	        			  dialFrag.setBtnPositiveText(getResources().getString(R.string.download));
	        			  dialFrag.setBtnNegativeText(getResources().getString(R.string.cancel));
	        			  BOCDialogFragListener listener = new BOCDialogFragListener() {
							@Override
							public void onDialogPositiveClick(DialogFragment dialog) {
								final InstallNewVersionTask newVersionTask = new InstallNewVersionTask(Versions.this, view);
								  String versionUrl = versionBaseUrl + m_clickedVersion.getShortName() + ".sql";
								  newVersionTask.execute(versionUrl);

								  mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
								      @Override
								      public void onCancel(DialogInterface dialog) {
								    	  newVersionTask.cancel(true);
								      }
								  });
							}
							
							@Override
							public void onDialogNegativeClick(DialogFragment dialog) {
								// TODO Auto-generated method stub
								
							}
						};
						dialFrag.setListener(listener);
	        			FragmentManager fragmentManager = getSupportFragmentManager();
	        			dialFrag.show(fragmentManager, "VERSION_NOT_AVAILABLE");
	        		  }
	        	  }
	          }
		});
		if(radioParallelVersions.isChecked())
		{
			parallelVersionControls.setVisibility(LinearLayout.VISIBLE);
		}
		this.bindVersions();
		mProgressDialog = new ProgressDialog(Versions.this);
		mProgressDialog.setMessage("Installing please wait...");
		mProgressDialog.setIndeterminate(true);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setCancelable(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.boc_pup, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == R.id.action_cancel) {
			miscHelper.reloadBiblePage = false;
			unsetValuesAndFinish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	private void bindVersions()
	{
		if(versions == null) 
		{
			if(versionDB == null)
				versionDB = new org.theBOC.bocbible.database.Version(this);
			versions = versionDB.getVersions(null, true, false);
		}
		
		if(!hideVersionOptions)
		{
			ParallelVersionsListener listener = new ParallelVersionsListener() {
				@Override
				public void onCheckedChanged(Version version, CompoundButton buttonView, boolean isChecked) {
					if(!isChecked)
					{
						if(version.getShortName().equalsIgnoreCase((String) txtVersion_1.getText()))
						{
							txtVersion_1.setText("?");
							mVersion_1 = null;
						}
						if(version.getShortName().equalsIgnoreCase((String) txtVersion_2.getText()))
						{
							txtVersion_2.setText("?");
							mVersion_2 = null;
						}
						if(numVersionsSelected == 2)
							enableAll();
						numVersionsSelected --;
					}
					else
					{
						if(txtVersion_1.getText().equals("?"))
						{
							txtVersion_1.setText(version.getShortName());
							mVersion_1 = version;
						}
						else if(txtVersion_2.getText().equals("?"))
						{
							txtVersion_2.setText(version.getShortName());
							mVersion_2 = version;
						}
						numVersionsSelected ++;
						if(numVersionsSelected >= 2)
						{
							disableUnchecked();
						}
					}
					if(adt != null)
					{
						adt.setVersionNames((String)txtVersion_1.getText(), (String)txtVersion_2.getText());
					}
				}
			};
			adt = new VersionListAdapter(this, this.versions, radioSingleVersion.isChecked(), (String)txtVersion_1.getText(), (String)txtVersion_2.getText(), listener);
		}
		else
			adt = new VersionListAdapter(this, this.versions, true, null, null, null);
		lstView.setAdapter(adt);
	}
	
	private void initControls()
	{
		parallelVersionControls = (LinearLayout) findViewById(R.id.parallel_versions_controls);
		lstView = (ListView) findViewById(R.id.lst_bible_versions);
		radioSingleVersion = (RadioButton) findViewById(R.id.radio_single_version);
		radioSingleVersion.setOnClickListener(new VersionsControlsClickListener());
		radioParallelVersions = (RadioButton) findViewById(R.id.radio_parallel_versions);
		radioParallelVersions.setOnClickListener(new VersionsControlsClickListener());
		txtVersion_1 = (TextView) findViewById(R.id.txt_version_1);
		txtVersion_2 = (TextView) findViewById(R.id.txt_version_2);
		radioGroupVersions = (RadioGroup) findViewById(R.id.rad_group_versions);
		if(hideVersionOptions)
		{
			radioGroupVersions.setVisibility(RadioGroup.GONE);
		}
		else
		{
			if(singleVersion == -1) // Fix orientation change issue
			{
				radioSingleVersion.setChecked(bibleHelper.getCurrentVersionId2() <= 0);
				radioParallelVersions.setChecked(bibleHelper.getCurrentVersionId2() > 0);
			}
			else
			{
				radioSingleVersion.setChecked(singleVersion == 1);
				radioParallelVersions.setChecked(singleVersion == 0);
			}
			
			btnDone = (Button) findViewById(R.id.btnDone);
			btnSwitch = (Button) findViewById(R.id.btnSwitchVersion);
			if(versionDB == null)
				versionDB = new org.theBOC.bocbible.database.Version(this);
			if(mVersion_1 == null)
			{
				mVersion_1 = versionDB.getVersion(bibleHelper.getCurrentVersionId(4), null);
			}
			if(mVersion_2 == null)
			{
				mVersion_2 = versionDB.getVersion(bibleHelper.getCurrentVersionId2(), null);
			}
			txtVersion_1.setText(mVersion_1.getShortName());
			if(mVersion_2 == null || mVersion_2.getShortName().equals(""))
			{
				txtVersion_2.setText("?");
			}
			else
			{
				txtVersion_2.setText(mVersion_2.getShortName());
				numVersionsSelected = 2;
			}
			btnDone.setOnClickListener(new VersionsControlsClickListener());
			btnSwitch.setOnClickListener(new VersionsControlsClickListener());
		}
	}
	public void disableUnchecked()
	{
		if(adt == null) return;
		for(int i = 0; i < adt.getCount(); i++)
		{
			adt.chkIsEnable[i] = false;
		}
		adt.notifyDataSetChanged();
	}
	public void enableAll()
	{
		if(adt == null) return;
		for(int i = 0; i < adt.getCount(); i++)
		{
			adt.chkIsEnable[i] = true;
		}
		adt.notifyDataSetChanged();

	}
	public class VersionsControlsClickListener implements OnClickListener
	{
		public VersionsControlsClickListener()
		{
			
		}
		@Override
		public void onClick(View v) {
			switch(v.getId())
			{
				case R.id.radio_single_version:
					parallelVersionControls.setVisibility(LinearLayout.GONE);
					singleVersion = 1;
					bindVersions();
					break;
				case R.id.radio_parallel_versions:
					parallelVersionControls.setVisibility(LinearLayout.VISIBLE);
					singleVersion = 0;
					bindVersions();
					break;
				case R.id.btnSwitchVersion:
					if(txtVersion_1 != null && txtVersion_2 != null)
					{
						Version tempVersion = mVersion_1;
						mVersion_1 = mVersion_2;
						mVersion_2 = tempVersion;
						txtVersion_1.setText(mVersion_1 == null ? "?" : mVersion_1.getShortName());
						txtVersion_2.setText(mVersion_2 == null ? "?" : mVersion_2.getShortName());
					}
					break;
				case R.id.btnDone:
					if(mVersion_1 != null && mVersion_2 != null)
					{
						bibleHelper.setCurrentVersionId(mVersion_1.getId());
						bibleHelper.setCurrentVersionName(mVersion_1.getShortName());
						bibleHelper.setCurrentLanguage(mVersion_1.getLanguage());
						bibleHelper.setCurrentVersionId2(mVersion_2.getId());
						bibleHelper.setCurrentVersionName2(mVersion_2.getShortName());
						miscHelper.reloadBiblePage = true;
					}
					else
					{
						miscHelper.reloadBiblePage = false;
					}
					unsetValuesAndFinish();
	        		break;
			}
		}
	}
	private void unsetValuesAndFinish()
	{
		singleVersion = -1;
		mVersion_1 = null;
		mVersion_2 = null;
		this.finish();
	}
	private class InstallNewVersionTask extends AsyncTask<String, Integer, String> {

	    private Context context;
	    private PowerManager.WakeLock mWakeLock;
	    private View view;

	    public InstallNewVersionTask(Context context, View view) {
	        this.context = context;
	        this.view = view;
	    }

	    @Override
	    protected String doInBackground(String... sUrl) {
	        InputStream input = null;
	        HttpURLConnection connection = null;
	        try {
	            URL url = new URL(sUrl[0]);
	            connection = (HttpURLConnection) url.openConnection();
	            connection.connect();
	            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
	                return "Server returned HTTP " + connection.getResponseCode()
	                        + " " + connection.getResponseMessage();
	            }
	            input = connection.getInputStream();
	            BufferedReader reader = new BufferedReader(new InputStreamReader(input));
	            String str = "";
	            if (input!=null) {   
	            	versionDB.beginTransaction();
	            	try
	            	{
	            		versionDB.createBibleVersion(m_clickedVersion.getShortName());
		            	 String sql = "INSERT INTO ZBible" + m_clickedVersion.getShortName() + 
		            			 " (ZbookId, Zbook, Zchapter, Zverse, Zversetext) VALUES %1$s;";
		                 while ((str = reader.readLine()) != null) { 
		                	if (isCancelled()) {
			                    input.close();
			                    Toast.makeText(context,"Install was cancelled", Toast.LENGTH_LONG).show();
			                    return null;
			                }
		                	String query = String.format(sql, str);
		                	versionDB.executeQuery(query);
		                }
		                versionDB.setTransactionSuccessful();
	            	}
	            	catch(Exception e){
	            		
	            	}
	            	finally
	            	{
	            		versionDB.endTransaction();
	            	}
	            }
	        } catch (Exception e) {
	            return e.toString();
	        } finally {
	            try {
	                if (input != null)
	                    input.close();
	            } catch (IOException ignored) {
	            }

	            if (connection != null)
	                connection.disconnect();
	        }
	        return null;
	    }
	    
	    @Override
	    protected void onPreExecute() {
	        super.onPreExecute();
	        // take CPU lock to prevent CPU from going off if the user 
	        // presses the power button during download
	        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
	        mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
	             getClass().getName());
	        mWakeLock.acquire();
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
	    protected void onPostExecute(String result) {
	        mWakeLock.release();
	        mProgressDialog.dismiss();
	        if (result != null)
	            Toast.makeText(context,"An error occurred. Please try again later", Toast.LENGTH_LONG).show();
	        else
	        {
	        	versionDB.setVersionAvailable(m_clickedVersion.getId());
	        	if(!radioSingleVersion.isChecked() && !hideVersionOptions && adt != null)
	        	{
	        		adt.setVersionAvailable(m_clickedVersion.getId());
	        		adt.notifyDataSetChanged();
	        		if(this.view != null && numVersionsSelected < 2)
	        		{
	        			CheckBox viewChkBox = (CheckBox) view.findViewById(R.id.version_select);
	        			if(viewChkBox != null)
	        			{
	        				viewChkBox.performClick();
	        			}
	        		}
	                Toast.makeText(context, m_clickedVersion.getName() + " installed!", Toast.LENGTH_LONG).show();
	        	}
	        	else
	        	{
	                bibleHelper.setCurrentVersionId(m_clickedVersion.getId());
	                bibleHelper.setCurrentVersionName(m_clickedVersion.getShortName());
	                bibleHelper.setCurrentLanguage(m_clickedVersion.getLanguage());
	                miscHelper.reloadBiblePage = true;
	                unsetValuesAndFinish();
	                Toast.makeText(context, m_clickedVersion.getName() + " installed!", Toast.LENGTH_LONG).show();
	        	}
	        }
	    }
	}
}

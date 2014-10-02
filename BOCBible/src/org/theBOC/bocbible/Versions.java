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
import org.theBOC.bocbible.Models.Version;
import org.theBOC.bocbible.common.BibleHelper;
import org.theBOC.bocbible.common.MiscHelper;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ListView;
import android.widget.Toast;
import android.view.View;

public class Versions extends FragmentActivity {
	private static org.theBOC.bocbible.database.Version versionDB;
	private ListView lstView;
	private ArrayList<Version> versions;
	private BibleHelper bibleHelper;
	private MiscHelper miscHelper;
	private Version m_clickedVersion;
	private static ProgressDialog mProgressDialog;
	public static final String versionBaseUrl = "http://www.theboc.org/downloads/bible";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_versions);
		final ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setTitle("Bible Versions");
		if(versionDB == null)
			versionDB = new org.theBOC.bocbible.database.Version(this);
		versions = versionDB.getVersions(null, true, false);
		lstView = (ListView) findViewById(R.id.lst_bible_versions);
		int[] colors = {0, 0xFFCCCCCC, 0}; 
		lstView.setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
		lstView.setDividerHeight(1);
		final Context context = this;
		miscHelper = MiscHelper.getInstance(context);
		lstView.setOnItemClickListener(new OnItemClickListener() 
		{
	          public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	          {
	        	  m_clickedVersion = versions.get(position);
	        	  bibleHelper = BibleHelper.getInstance(context);
	        	  if(!m_clickedVersion.getIsGroupHeader())
	        	  {
	        		  if(m_clickedVersion.getIsAvailable())
	        		  {
		        		  bibleHelper.setCurrentVersionId(m_clickedVersion.getId());
		        		  bibleHelper.setCurrentVersionName(m_clickedVersion.getShortName());
		        		  bibleHelper.setCurrentLanguage(m_clickedVersion.getLanguage());
		        		  miscHelper.reloadBiblePage = true;
		        		  ((Activity) context).finish();
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
								final InstallNewVersionTask newVersionTask = new InstallNewVersionTask(Versions.this);
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
		VersionListAdapter adt = new VersionListAdapter(this, versions, true);
		lstView.setAdapter(adt);
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
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		if (id == R.id.action_cancel) {
			miscHelper.reloadBiblePage = false;
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class InstallNewVersionTask extends AsyncTask<String, Integer, String> {

	    private Context context;
	    private PowerManager.WakeLock mWakeLock;

	    public InstallNewVersionTask(Context context) {
	        this.context = context;
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
                bibleHelper.setCurrentVersionId(m_clickedVersion.getId());
                bibleHelper.setCurrentVersionName(m_clickedVersion.getShortName());
                bibleHelper.setCurrentLanguage(m_clickedVersion.getLanguage());
                Toast.makeText(context, m_clickedVersion.getName() + " installed!", Toast.LENGTH_LONG).show();
                miscHelper.reloadBiblePage = true;
                ((Activity) context).finish();
	        }
	    }
	}
}

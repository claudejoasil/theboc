package org.theBOC.bocbible.Adapters;

import java.util.ArrayList;

import org.theBOC.bocbible.R;
import org.theBOC.bocbible.Models.Version;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

public class VersionListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Version> versions;
	private boolean singleVersion;
	private String currentVersionName1;
	private String currentVersionName2;
	public boolean[] chkIsEnable;
	public interface ParallelVersionsListener {
        public void onCheckedChanged(Version version, CompoundButton buttonView, boolean isChecked);
    }
	private ParallelVersionsListener mListener;
	public VersionListAdapter(Context context, ArrayList<Version> versions, boolean singleVersion, String currentVersionName1, String currentVersionName2, ParallelVersionsListener listener){
		this.context = context;
		this.versions = versions;
		this.singleVersion = singleVersion;
		if(singleVersion)
			return;
		this.currentVersionName1 = currentVersionName1;
		this.currentVersionName2 = currentVersionName2;
		chkIsEnable = new boolean[versions.size()];
		if(currentVersionName1 == "" || currentVersionName2 == "" || currentVersionName1 == "?" || currentVersionName2 == "?")
		{
			for(int i = 0; i < versions.size(); i++)
			{
				chkIsEnable[i] = true;
			}
		}
		else
		{
			for(int i = 0; i < versions.size(); i++)
			{
				chkIsEnable[i] = false;
			}
		}
		this.mListener = listener;
	}
	
	public int getItemPositionById(int id)
	{
		for(int i = 0; i <= versions.size() - 1; i++)
		{
			if(versions.get(i).getId() == id)
				return i;
		}
		return -1;
	}
	public void setVersionNames(String versionName1, String versionName2)
	{
		this.currentVersionName1 = versionName1;
		this.currentVersionName2 = versionName2;
	}
	public void setVersionAvailable(int id)
	{
		int position = getItemPositionById(id);
		versions.get(position).setIsAvailable(true);
	}
	@Override
	public int getCount() {
		return versions.size();
	}

	@Override
	public Object getItem(int position) {		
		return versions.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);        
        final Version version = versions.get(position);
		if(!version.getIsGroupHeader()) {
			if(this.singleVersion) {
				convertView = mInflater.inflate(R.layout.version_item, parent, false);
			}
			else {
				convertView = mInflater.inflate(R.layout.version_select_item, parent, false);
				CheckBox chkSelected = (CheckBox) convertView.findViewById(R.id.version_select); 
				ImageView imgVersionDownload = (ImageView) convertView.findViewById(R.id.img_version_download);
				if(version.getIsAvailable())
				{
					chkSelected.setTag(version.getId());
					chkSelected.setChecked(currentVersionName1.equalsIgnoreCase(version.getShortName()) || currentVersionName2.equalsIgnoreCase(version.getShortName()));
					chkSelected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					       @Override
					       public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					    	  if(mListener != null) 
					    		  mListener.onCheckedChanged(version, buttonView, isChecked);
					       }
					   }
					);  
					chkSelected.setEnabled(this.chkIsEnable[position] || chkSelected.isChecked());
					imgVersionDownload.setVisibility(ImageView.GONE);
				}
				else
				{
					chkSelected.setVisibility(CheckBox.GONE);
				}
			}
			TextView txtNameText = (TextView) convertView.findViewById(R.id.version_text); 
			TextView txtShortName = (TextView) convertView.findViewById(R.id.txt_short_name);
	        String VersionText = version.getName();
	        txtNameText.setText(VersionText);  
	        txtShortName.setText(version.getShortName());
		} else {
			convertView = mInflater.inflate(R.layout.version_header_item, parent, false);
			TextView titleView = (TextView) convertView.findViewById(R.id.item_header);
            titleView.setText(version.getName());
		}        
         return convertView;
	}

}

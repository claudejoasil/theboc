package org.theBOC.theboc.Adapters;

import java.util.ArrayList;

import org.theBOC.theboc.R;
import org.theBOC.theboc.Models.Version;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class VersionListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Version> versions;
	
	public VersionListAdapter(Context context, ArrayList<Version> versions){
		this.context = context;
		this.versions = versions;
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
        Version version = versions.get(position);
		if(!version.getIsGroupHeader())		{
			convertView = mInflater.inflate(R.layout.version_item, parent, false);
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

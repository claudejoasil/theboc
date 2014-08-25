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
		if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.version_item, parent, false);
        }  
		Version version = versions.get(position);
        TextView txtVerseText = (TextView) convertView.findViewById(R.id.version_text); 
        String VersionText = version.getName();
        txtVerseText.setText(VersionText);   
         return convertView;
	}

}

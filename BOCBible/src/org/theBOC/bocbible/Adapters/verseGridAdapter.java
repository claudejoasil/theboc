package org.theBOC.bocbible.Adapters;

import java.util.ArrayList;
import org.theBOC.bocbible.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class verseGridAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Integer> verses;
	
	public verseGridAdapter(Context context, int numVerses){
		this.context = context;
		verses = new ArrayList<Integer>();
		for(int i = 1; i <= numVerses; i++)
  	  	{
			verses.add(i);
  	  	}
	}

	@Override
	public int getCount() {
		return verses.size();
	}

	@Override
	public Object getItem(int position) {		
		return verses.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);        
        int chapter = verses.get(position);
		if(convertView == null)
			convertView = mInflater.inflate(R.layout.chapter_item, parent, false);
		TextView txtText = (TextView) convertView.findViewById(R.id.chapter_text); 
        txtText.setText(Integer.toString(chapter));
        return convertView;
	}

}

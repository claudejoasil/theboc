package org.theBOC.bocbible.Adapters;

import java.util.ArrayList;
import org.theBOC.bocbible.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ChapterGridAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Integer> chapters;
	private int selectedChapter;
	
	public ChapterGridAdapter(Context context, int numChapters, int chapter){
		this.context = context;
		chapters = new ArrayList<Integer>();
		for(int i = 1; i <= numChapters; i++)
  	  	{
			chapters.add(i);
  	  	}
		this.selectedChapter = chapter;
	}

	@Override
	public int getCount() {
		return chapters.size();
	}

	@Override
	public Object getItem(int position) {		
		return chapters.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);        
        int chapter = chapters.get(position);
		if(convertView == null)
			convertView = mInflater.inflate(R.layout.chapter_item, parent, false);
		TextView txtText = (TextView) convertView.findViewById(R.id.chapter_text); 
        txtText.setText(Integer.toString(chapter));  
        if(selectedChapter - 1 == position)
        {
        	convertView.setBackgroundColor(0xFFFAFAFA);
        	txtText.setTypeface(null, Typeface.BOLD);
        	txtText.setTextColor(0xFF813f15);
        }
        return convertView;
	}

}

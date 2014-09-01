package org.theBOC.theboc.Adapters;

import java.util.ArrayList;

import org.theBOC.theboc.R;
import org.theBOC.theboc.Models.Bible;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class VerseListAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<Bible> verses;
	private float textSize;
	
	public VerseListAdapter(Context context, ArrayList<Bible> verses, float textSize){
		this.context = context;
		this.verses = verses;
		this.textSize = textSize;
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
		if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.verse_item, parent, false);
        }  
		Bible bible = verses.get(position);
        //TextView txtVerse = (TextView) convertView.findViewById(R.id.verse_number);
        TextView txtVerseText = (TextView) convertView.findViewById(R.id.verse_text); 
        String VerseText = "<font color='#813f15'>" + 
        						Integer.toString(bible.getVerse()) + 
						   "</font> " + 
    						bible.getVerseText();
        txtVerseText.setText(Html.fromHtml(VerseText));    
        txtVerseText.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.textSize);
        //txtVerse.setText();
        if(position == 0)
        {
        	convertView.setPadding((int)context.getResources().getDimension(R.dimen.activity_horizontal_margin), 
        						  (int)context.getResources().getDimension(R.dimen.activity_vertical_margin), 
        						  (int)context.getResources().getDimension(R.dimen.activity_horizontal_margin), 
        						  0);
        }
         return convertView;
	}

}

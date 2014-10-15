package org.theBOC.bocbible.Adapters;

import java.util.ArrayList;

import org.theBOC.bocbible.R;
import org.theBOC.bocbible.Models.Bible;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.util.TypedValue;
import android.view.ActionMode;
import android.view.ActionMode.Callback;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class VerseListAdapter extends BaseAdapter {
	
	private Context context;
	private ArrayList<Bible> verses;
	private static String[] versions;
	private float textSize;
	private boolean singleVersion;
	private static final int HIGHTLIGHT = R.id.highLight_menu_item;
	public VerseListAdapter(Context context, ArrayList<Bible> verses, float textSize, String[] theVersions){
		this.context = context;
		this.verses = verses;
		this.textSize = textSize;
		versions = theVersions;
		singleVersion = versions[1] == null;
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
		if(this.singleVersion)
			return this.getSingleVersionView(position, convertView, parent);
		else
			return this.getDualVersionView(position, convertView, parent);
	}
	
	private View getSingleVersionView(int position, View convertView, ViewGroup parent)
	{
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
        String highLights = bible.getHighLights();
        if(position == 0)
        {
        	convertView.setPadding((int)context.getResources().getDimension(R.dimen.activity_horizontal_margin), 
        						  (int)context.getResources().getDimension(R.dimen.activity_vertical_margin), 
        						  (int)context.getResources().getDimension(R.dimen.activity_horizontal_margin), 
        						  0);
        }
        txtVerseText.setText(Html.fromHtml(VerseText)); 
        txtVerseText.setTag(position);
        txtVerseText.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.textSize);
        //txtVerse.setText();
        
        if(highLights != null && !highLights.trim().equals(""))
        	this.highLightText(txtVerseText, highLights);
        this.handleSelectedText(txtVerseText, 0);
        return convertView;
	}
	
	private View getDualVersionView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.dual_version_verse_item, parent, false);
        }  
		Bible bible = verses.get(position);
        //TextView txtVerse = (TextView) convertView.findViewById(R.id.verse_number);
        TextView txtVerseText1 = (TextView) convertView.findViewById(R.id.verse_text_1); 
        TextView txtVerseText2 = (TextView) convertView.findViewById(R.id.verse_text_2); 
        String VerseText = bible.getVerseText() != null && !bible.getVerseText().trim().equals("") ? 
        				   "<font color='#813f15'>" + 
        						Integer.toString(bible.getVerse()) + 
						   "</font> " + 
    						bible.getVerseText() : "";
        String highLights = bible.getHighLights();
        
        String VerseText2 = bible.getVerseText2() != null && !bible.getVerseText2().trim().equals("") ? 
        		"<font color='#813f15'>" + 
					Integer.toString(bible.getVerse()) + 
			   "</font> " + 
			bible.getVerseText2() : "";
        String highLights2 = bible.getHighLights2();
        if(position == 0)
        {
        	convertView.setPadding((int)context.getResources().getDimension(R.dimen.activity_horizontal_margin), 
        						  (int)context.getResources().getDimension(R.dimen.activity_vertical_margin), 
        						  (int)context.getResources().getDimension(R.dimen.activity_horizontal_margin), 
        						  0);
        }
        txtVerseText1.setText(Html.fromHtml(VerseText)); 
        txtVerseText1.setTag(position);
        txtVerseText1.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.textSize);
        if(highLights != null && !highLights.trim().equals(""))
        	this.highLightText(txtVerseText1, highLights);
        this.handleSelectedText(txtVerseText1, 0);
        
        txtVerseText2.setText(Html.fromHtml(VerseText2)); 
        txtVerseText2.setTag(position);
        txtVerseText2.setTextSize(TypedValue.COMPLEX_UNIT_PX, this.textSize);
        if(highLights2 != null && !highLights2.trim().equals(""))
        	this.highLightText(txtVerseText2, highLights2);
        this.handleSelectedText(txtVerseText2, 1);
        return convertView;
	}
	private void handleSelectedText(final TextView mTextView, final int versionOffset)
	{
		mTextView.setCustomSelectionActionModeCallback(new Callback() {

			@Override
	        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
				menu.add(0, HIGHTLIGHT, 0, "HighLight").setIcon(R.drawable.ic_highlight);
	            return true;
	        }

			@Override
	        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
	            menu.removeItem(android.R.id.selectAll);
	            menu.removeItem(android.R.id.cut);
	            mode.setTitle(null);
	            return true;
	        }

			@Override
	        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
	            switch (item.getItemId()) {
	                case HIGHTLIGHT:
	                    int min = 0;
	                    int max = mTextView.getText().length();
	                    if (mTextView.isFocused()) {
	                        final int selStart = mTextView.getSelectionStart();
	                        final int selEnd = mTextView.getSelectionEnd();

	                        min = Math.max(0, Math.min(selStart, selEnd));
	                        max = Math.max(0, Math.max(selStart, selEnd));
	                    }
	                    String hightLights = "." + min + "," + max;
	                    try
	                    {
	                    	Integer position = (Integer) mTextView.getTag();
	                    	Bible bible = verses.get(position);
	                    	int id = bible.getPk();
	                    	String existingHighlights = bible.getHighLights();
	                    	String newHighLights = hightLights;
	                    	if(existingHighlights != null && !existingHighlights.trim().equals(""))
	                    	{
	                    		newHighLights = existingHighlights + hightLights;
	                    	}
	                    	verses.get(position).setHighLights(newHighLights);
	                    	org.theBOC.bocbible.database.Bible bibleDB = new org.theBOC.bocbible.database.Bible(context);
	                    	bibleDB.HightLightVerse(id, hightLights, versions[versionOffset]);
	                    }
	                    catch(Exception e)
	                    {
	                    	// An error occurred
	                    }
	                    Spannable newVerse = new SpannableString(mTextView.getText());  
	                    newVerse.setSpan(new BackgroundColorSpan(Color.YELLOW), min, max, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
	                    mTextView.setText(newVerse);
	                    mode.finish();
	                    return true;
	                default:
	                    break;
	            }
	            return false;
	        }

			@Override
			public void onDestroyActionMode(ActionMode mode) {
				// TODO Auto-generated method stub
				
			}
	    });
	}
	private void highLightText(final TextView mTextView, String highLights)
	{
		int min = 0;
        int max = mTextView.getText().length();
        Spannable newVerse = new SpannableString(mTextView.getText());  
        String[] highLightsArr = highLights.split("\\.");
        for(int i = 0; i < highLightsArr.length; i++)
        {
        	String highLight = highLightsArr[i];
        	if(highLight != null && !highLight.trim().equals(""))
        	{
        		String[] minMax = highLight.split(",");
        		try
        		{
    	        	int selStart = Integer.parseInt(minMax[0]);
    	        	int selEnd = Integer.parseInt(minMax[1]);
    	        	min = Math.max(0, Math.min(selStart, selEnd));
    	        	max = Math.max(0, Math.max(selStart, selEnd));
    	        	newVerse.setSpan(new BackgroundColorSpan(Color.YELLOW), min, max, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        		}
        		catch(Exception ex)
        		{
        			
        		}
        	}
        }
        mTextView.setText(newVerse);
	}
}

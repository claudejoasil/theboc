package org.theBOC.bocbible.Adapters;

import java.util.ArrayList;

import org.theBOC.bocbible.BOCDialogFrag4Activity;
import org.theBOC.bocbible.BOCDialogFrag4Activity.BOCDialogFrag4ActivityListener;
import org.theBOC.bocbible.R;
import org.theBOC.bocbible.Models.Bible;
import org.theBOC.bocbible.common.BibleHelper;

import android.app.Activity;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.BackgroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class HighlightListAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<Bible> verses;
	private BOCDialogFrag4Activity dialFrag;
	//private static String version;
	
	public HighlightListAdapter(Context context, ArrayList<Bible> verses){
		this.context = context;
		this.verses = verses;
		//version = theVersion;
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
	public View getView(final int position, View convertView, final ViewGroup parent) {
		if (convertView == null) {
            LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.highlight_item, parent, false);
        }  
		//mCurrentBibleObj = verses.get(position);
        TextView txtHighLightVerse = (TextView) convertView.findViewById(R.id.txt_highlight_verse);
        TextView txtHighLightVerseReference = (TextView) convertView.findViewById(R.id.txt_highlight_verse_reference); 
        Button btnUnhighLight = (Button) convertView.findViewById(R.id.btnDeleteHighlight);
        Button btnReadOn = (Button) convertView.findViewById(R.id.btnReadChapter);
        btnUnhighLight.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialFrag = new BOCDialogFrag4Activity();
				dialFrag.setBtnNegativeText(context.getResources().getString(R.string.cancel));
				dialFrag.setBtnPositiveText(context.getResources().getString(R.string.yes));
				dialFrag.setDialogMessage("Are you sure you want to unhighlight this verse");
				BOCDialogFrag4ActivityListener listener = new BOCDialogFrag4ActivityListener() {
					
					@Override
					public void onDialogPositiveClick(DialogFragment dialog) {
						org.theBOC.bocbible.database.Bible bibleDB = new org.theBOC.bocbible.database.Bible(context);
					  	bibleDB.UnHightLightVerse(verses.get(position).getPk(), verses.get(position).getVersion().getShortName());
					  	verses.remove(position);
					  	notifyDataSetChanged();
					}
					
					@Override
					public void onDialogNegativeClick(DialogFragment dialog) {
						// TODO Auto-generated method stub
						
					}
				};
				dialFrag.setListener(listener);
				FragmentManager fragmentManager = ((Activity)context).getFragmentManager();
				dialFrag.show(fragmentManager, "UNHIGHLIGHT_VERSE");
			}
		});
        
        btnReadOn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Bible mCurrentBibleObj = verses.get(position);
				BibleHelper bibleHelper = BibleHelper.getInstance((Activity)context);
				bibleHelper.setCurrentBookId(mCurrentBibleObj.getBookId());
				bibleHelper.setCurrentChapter(mCurrentBibleObj.getChapter());
				bibleHelper.setCurrentVerse(mCurrentBibleObj.getVerse());
				bibleHelper.setCurrentVersionName(mCurrentBibleObj.getVersion().getShortName());
				bibleHelper.setCurrentVersionId(mCurrentBibleObj.getVersion().getId());
				bibleHelper.setCurrentTestament(mCurrentBibleObj.getBookId() <= 39 ? 1 : 2);
				((org.theBOC.bocbible.Home) context).gotoBible(null);
			}
		});
        txtHighLightVerse.setText(verses.get(position).getVerseText()); 
        txtHighLightVerseReference.setText(verses.get(position).getBook() + " " + verses.get(position).getChapter() + ":" + verses.get(position).getVerse());
        String highLights = verses.get(position).getHighLights();
        if(highLights != null && !highLights.trim().equals(""))
        	this.highLightText(txtHighLightVerse, highLights);
        return convertView;
	}
	private void highLightText(final TextView mTextView, String highLights)
	{
		int minValue = 0;
        int maxValue = mTextView.getText().length();
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
    	        	int selStart = Integer.parseInt(minMax[0]) - 2; // 2: the verse and the space in from of the verse
    	        	int selEnd = Integer.parseInt(minMax[1]) - 2;
    	        	newVerse.setSpan(new BackgroundColorSpan(Color.YELLOW), 
    	        			Math.max(minValue, Math.min(selStart, selEnd)), 
    	        			Math.min(maxValue, Math.max(selStart, selEnd)), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        		}
        		catch(Exception ex)
        		{
        			
        		}
        	}
        }
        mTextView.setText(newVerse);
	}
}

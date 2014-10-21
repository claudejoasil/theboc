package org.theBOC.bocbible.Adapters;

import java.util.ArrayList;
import org.theBOC.bocbible.R;
import org.theBOC.bocbible.Models.Note;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class NoteListAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<Note> notes;
	
	public NoteListAdapter(Context context, ArrayList<Note> notes){
		this.context = context;
		this.notes = notes;
	}
	
	@Override
	public int getCount() {
		return notes.size();
	}

	@Override
	public Object getItem(int position) {		
		return notes.get(position);
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
            convertView = mInflater.inflate(R.layout.note_item, parent, false);
        }  
		//mCurrentBibleObj = verses.get(position);
        TextView txtNoteTitle = (TextView) convertView.findViewById(R.id.note_title);
        Button btnEdit = (Button) convertView.findViewById(R.id.btn_edit);
        Button btnDelete = (Button) convertView.findViewById(R.id.btn_delete);
        btnEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Note mCurrentNoteObj = notes.get(position);
			}
		});
        btnDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Note mCurrentNoteObj = notes.get(position);
			}
		});
        return convertView;
	}
}

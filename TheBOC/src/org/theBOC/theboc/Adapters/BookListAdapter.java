package org.theBOC.theboc.Adapters;

import java.util.ArrayList;
import java.util.HashMap;

import org.theBOC.theboc.Bible;
import org.theBOC.theboc.R;
import org.theBOC.theboc.Models.Book;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.TextView;

public class BookListAdapter extends BaseExpandableListAdapter {
	private Context context;
	private ArrayList<Book> books;
	private HashMap<org.theBOC.theboc.Models.Book, ArrayList<Integer>> bookChapters;
	private SharedPreferences sharedpreferences;
	public static final String currentValues = "BibleCurrentValues";
	public static final String BookId = "bookIdKey";
	public static final String Chapter = "chapterKey";
	public static final String Language = "languageKey";
	public BookListAdapter(Context context, ArrayList<Book> books, HashMap<Book, ArrayList<Integer>> bookChapters)
	{
		this.context = context;
		this.books = books;
		this.bookChapters = bookChapters;
	}

	@Override
	public int getGroupCount() {
		return this.books.size();
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		//return this.bookChapters.get(this.books.get(groupPosition)).size();
		return 1;
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this.books.get(groupPosition);
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return this.bookChapters.get(this.books.get(groupPosition)).get(childPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
		Book book = (Book) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.book_item, parent, false);
        }
 
        TextView lblListHeader = (TextView) convertView.findViewById(R.id.book_text);
        //lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(book.getName());
        return convertView;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
		final Book book = (Book) getGroup(groupPosition);
		final Context theContext = this.context;
		int numChapters = book.getNumChapters();
		GridView chapterGridView;
		 //if (convertView == null) {
			 final int SPACING = 1;
			 final int COL_WIDTH = (int) parent.getContext().getResources().getDimension(R.dimen.boc_default_gridItem_width);
			 final int ROW_HEIGHT = (int) parent.getContext().getResources().getDimension(R.dimen.boc_default_gridItem_height);
			 
			 LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			 convertView = (ViewGroup) inflater.inflate(R.layout.activity_chapters, parent, false);
			 chapterGridView = (GridView) convertView.findViewById(R.id.gridview);
			 chapterGridView.setVerticalSpacing(SPACING);
			 chapterGridView.setHorizontalSpacing(SPACING);
			 chapterGridView.setAdapter(new ChapterGridAdapter(parent.getContext(), numChapters));
			 
			 final int colCount = (int)Math.floor((parent.getWidth() - (2 * SPACING)) / (COL_WIDTH + SPACING));
			 final int rowCount = (int)Math.ceil(numChapters / colCount) + 1;
			 final int gridHeight = Math.round(rowCount * (ROW_HEIGHT + SPACING));
			 sharedpreferences = this.context.getSharedPreferences(currentValues, Context.MODE_PRIVATE);
			 chapterGridView.setOnItemClickListener(new OnItemClickListener() 
			 {
		          public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
		          {
		        	  sharedpreferences.edit().putInt(BookId, book.getBookId()).apply();
		        	  sharedpreferences.edit().putInt(Chapter, position + 1).apply();
		        	  Intent bibleIntent = new Intent(theContext, Bible.class);
		        	  parent.getContext().startActivity(bibleIntent);
		          } 
			 });
			 chapterGridView.getLayoutParams().height = gridHeight; 
		// }
        return convertView;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

	/*
	@Override
	public int getCount() {
		return books.size();
	}

	@Override
	public Object getItem(int position) {		
		return books.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);        
        Book book = books.get(position);
		if(convertView == null)
			convertView = mInflater.inflate(R.layout.book_item, parent, false);
		TextView txtNameText = (TextView) convertView.findViewById(R.id.book_text); 
        txtNameText.setText(book.getName());  
        return convertView;
	}
	*/

}

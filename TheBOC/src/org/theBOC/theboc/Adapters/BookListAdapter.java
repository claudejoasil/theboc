package org.theBOC.theboc.Adapters;

import java.util.ArrayList;
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
	private SharedPreferences sharedpreferences;
	public static final String currentValues = "BibleCurrentValues";
	public static final String BookId = "bookIdKey";
	public static final String Chapter = "chapterKey";
	public static final String Language = "languageKey";
	public static final String testament = "testamentKey";
	
	public BookListAdapter(Context context, ArrayList<Book> books)
	{
		this.context = context;
		this.books = books;
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
		return null;
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
        //if(isExpanded)
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
		 final int SPACING = (int) parent.getContext().getResources().getDimension(R.dimen.boc_default_gridItem_spacing);
		 final int COL_WIDTH = (int) parent.getContext().getResources().getDimension(R.dimen.chapter_column_width);
		 final int ROW_HEIGHT = (int) parent.getContext().getResources().getDimension(R.dimen.boc_default_gridItem_height);
		 final int PADDING = 0; //(int) parent.getContext().getResources().getDimension(R.dimen.activity_horizontal_margin);
		 
		 LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 convertView = (ViewGroup) inflater.inflate(R.layout.activity_chapters, parent, false);
		 chapterGridView = (GridView) convertView.findViewById(R.id.gridview);
		 chapterGridView.setVerticalSpacing(SPACING);
		 chapterGridView.setHorizontalSpacing(SPACING);
		 sharedpreferences = this.context.getSharedPreferences(currentValues, Context.MODE_PRIVATE);
		 int currentChapter = sharedpreferences.getInt(Chapter, 0);
		 int currentBookId = sharedpreferences.getInt(BookId, 0);
		 if(book.getBookId() != currentBookId)
		 {
			 currentChapter = 0;
		 }
		 chapterGridView.setAdapter(new ChapterGridAdapter(parent.getContext(), numChapters, currentChapter));
		 int parentWidth = parent.getWidth();
		 int colCount;
		 int rowCount;
		 int gridHeight;
		 int gridWidth;
		 if(parentWidth <= numChapters * COL_WIDTH)
		 {
			 colCount = (int)Math.floor(parentWidth / (COL_WIDTH + SPACING));
			 rowCount = (int)Math.ceil(numChapters / colCount);
			 gridHeight = Math.round(rowCount * (ROW_HEIGHT + SPACING) + ROW_HEIGHT + SPACING);
			 gridWidth = Math.round(colCount * (COL_WIDTH + SPACING) + SPACING);
		 }
		 else
		 {
			 colCount = numChapters;
			 rowCount = 1;
			 gridHeight = ROW_HEIGHT + SPACING;
			 gridWidth = Math.round(colCount * (COL_WIDTH + SPACING) + SPACING);
		 }
		 chapterGridView.setOnItemClickListener(new OnItemClickListener() 
		 {
	          public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	          {
	        	  sharedpreferences.edit().putInt(BookId, book.getBookId()).apply();
	        	  sharedpreferences.edit().putInt(Chapter, position + 1).apply();
	        	  sharedpreferences.edit().putInt(testament, book.getTestament()).apply();
	        	  Intent bibleIntent = new Intent(theContext, Bible.class);
	        	  parent.getContext().startActivity(bibleIntent);
	          } 
		 });
		 chapterGridView.getLayoutParams().height = gridHeight; 
		 chapterGridView.getLayoutParams().width = gridWidth;  
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

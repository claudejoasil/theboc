package org.theBOC.bocbible.Adapters;

import java.util.ArrayList;
import org.theBOC.bocbible.R;
import org.theBOC.bocbible.Models.Book;
import org.theBOC.bocbible.common.BibleHelper;
import org.theBOC.bocbible.common.MiscHelper;

import android.app.Activity;
import android.content.Context;
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
	private BibleHelper bibleHelper;
	private MiscHelper miscHelper;
	public static final String BookId = "bookIdKey";
	public static final String Chapter = "chapterKey";
	public static final String Language = "languageKey";
	public static final String Testament = "testamentKey";
	
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
		 //final int PADDING = (int) parent.getContext().getResources().getDimension(R.dimen.activity_horizontal_margin);
		 
		 LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		 convertView = (ViewGroup) inflater.inflate(R.layout.activity_chapters, parent, false);
		 chapterGridView = (GridView) convertView.findViewById(R.id.gridview);
		 chapterGridView.setVerticalSpacing(SPACING);
		 chapterGridView.setHorizontalSpacing(SPACING);
		 bibleHelper = BibleHelper.getInstance(this.context);
		 int currentChapter = bibleHelper.getCurrentChapter(0);
		 int currentBookId = bibleHelper.getCurrentBookId(0);
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
	        	  bibleHelper.setCurrentBookId(book.getBookId());
	              bibleHelper.setCurrentChapter(position + 1);
	              bibleHelper.setCurrentVerse(1);
	              bibleHelper.setCurrentTestament(book.getTestament());
	              miscHelper = MiscHelper.getInstance(theContext);
	              miscHelper.reloadBiblePage = true;
	        	  ((Activity)theContext).finish();
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

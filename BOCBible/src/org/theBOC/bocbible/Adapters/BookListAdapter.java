package org.theBOC.bocbible.Adapters;

import java.util.ArrayList;
import org.theBOC.bocbible.R;
import org.theBOC.bocbible.Models.Book;
import org.theBOC.bocbible.common.BibleHelper;
import org.theBOC.bocbible.common.MiscHelper;
import org.theBOC.bocbible.database.Bible;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class BookListAdapter extends BaseExpandableListAdapter {
	private Context context;
	private ArrayList<Book> books;
	private BibleHelper bibleHelper;
	private MiscHelper miscHelper;
	private Bible bibleDB;
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
		int numChapters = book.getNumChapters();
		GridView chapterGridView;
		 final int SPACING = (int) parent.getContext().getResources().getDimension(R.dimen.boc_default_gridItem_spacing);
		 final int COL_WIDTH = (int) parent.getContext().getResources().getDimension(R.dimen.chapter_column_width);
		 final int ROW_HEIGHT = (int) parent.getContext().getResources().getDimension(R.dimen.boc_default_gridItem_height);
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
	        	 showPopup(view, book, position);
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
	
	public void showPopup(View anchorView, final Book book, int position) {

	    View popupView = ((Activity) context).getLayoutInflater().inflate(R.layout.activity_verse_grid, null, false);

	    final PopupWindow popupWindow = new PopupWindow(popupView, 
	                           LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

	    TextView txtText = (TextView) popupView.findViewById(R.id.txtVerseInt);
    	String tvString = book.getName() + " " + (position + 1) + ":" + "?";
    	txtText.setText(tvString);
	    txtText.setTypeface(null, Typeface.BOLD);
    	txtText.setTextColor(0xFF813f15);
    	final int SPACING = (int) ((Activity) context).getResources().getDimension(R.dimen.boc_default_gridItem_spacing);
    	GridView verseGridView;
    	verseGridView = (GridView) popupView.findViewById(R.id.verseGridview);
    	verseGridView.setVerticalSpacing(SPACING);
    	verseGridView.setHorizontalSpacing(SPACING);
		 bibleHelper = BibleHelper.getInstance(this.context);
		 final int selectedChapter = position + 1;
		 bibleDB = new Bible(this.context);
		 int numVerses = bibleDB.GetNumVerses(book.getBookId(), selectedChapter, bibleHelper.getCurrentVersionName("KJV"));
		 verseGridView.setAdapter(new verseGridAdapter(context, numVerses));
		 verseGridView.setOnItemClickListener(new OnItemClickListener() 
		 {
	          public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
	          {
	        	  bibleHelper.setCurrentBookId(book.getBookId());
	              bibleHelper.setCurrentChapter(selectedChapter);
	              bibleHelper.setCurrentVerse(position + 1);
	              bibleHelper.setCurrentTestament(book.getTestament());
	              miscHelper = MiscHelper.getInstance(context);
	              miscHelper.reloadBiblePage = true;
	              popupWindow.dismiss();
	        	  ((Activity) context).finish();
	          } 
		 });

	    // If the PopupWindow should be focusable
	    popupWindow.setFocusable(true);

	    // If you need the PopupWindow to dismiss when when touched outside 
	    popupWindow.setBackgroundDrawable(new ColorDrawable());
	    popupWindow.showAtLocation(anchorView, Gravity.CENTER_VERTICAL, 0, 0);
	}


}

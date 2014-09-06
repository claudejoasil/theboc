package org.theBOC.theboc;

import java.util.ArrayList;

import org.theBOC.theboc.Adapters.BookListAdapter;
import org.theBOC.theboc.common.BibleHelper;

import android.app.Fragment;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnGroupExpandListener;

public class TestamentFragment extends Fragment {
	private ExpandableListView lstView;
	private int theTestament;
	private static org.theBOC.theboc.database.Book bookDB;
	private ArrayList<org.theBOC.theboc.Models.Book> books;
	private BibleHelper bibleHelper;
	private int lastExpandedPosition = -1;
	public TestamentFragment(){
		
	}
	public TestamentFragment(int value){
		this.theTestament = value;
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    	int NUM_OLD_TESTAMENT_BOOKS = 39;
    	if(bookDB == null)
    		bookDB = new org.theBOC.theboc.database.Book(this.getActivity());
    	bibleHelper = BibleHelper.getInstance(this.getActivity());
    	String language = bibleHelper.getCurrentLanguage("");
    	//int currentBookId = bibleHelper.getCurrentBookId(0);
		books = bookDB.getBooks(this.theTestament, language);
		View frag = inflater.inflate(R.layout.testament_fragment, container, false);
		lstView = (ExpandableListView) frag.findViewById(R.id.lst_books);
		
		lstView.setOnGroupExpandListener(new OnGroupExpandListener() {
			@Override
		    public void onGroupExpand(int groupPosition) {
	            if (lastExpandedPosition != -1
	                    && groupPosition != lastExpandedPosition) {
	            	lstView.collapseGroup(lastExpandedPosition);
	            }
	            lastExpandedPosition = groupPosition;
		    }
		});
		int[] colors = {0, 0xFFCCCCCC, 0}; 
		lstView.setDivider(new GradientDrawable(Orientation.RIGHT_LEFT, colors));
		lstView.setDividerHeight(1);
		
		BookListAdapter adt = new BookListAdapter(this.getActivity(), books);
		lstView.setAdapter(adt);
		if(bibleHelper.getCurrentBookId(0) > 0) {
			int position;
			if(bibleHelper.getCurrentBookId(0) > NUM_OLD_TESTAMENT_BOOKS && bibleHelper.getCurrentTestament(0) == this.theTestament)
			{
				position = bibleHelper.getCurrentBookId(0) - NUM_OLD_TESTAMENT_BOOKS - 1;
				lstView.expandGroup(position, true);
			}
			if(bibleHelper.getCurrentBookId(0) <= NUM_OLD_TESTAMENT_BOOKS && bibleHelper.getCurrentTestament(0) == this.theTestament)
			{
				position = bibleHelper.getCurrentBookId(0) - 1;
				lstView.expandGroup(position, true);
			}
		}
		return frag;
    }

	}
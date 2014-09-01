package org.theBOC.theboc;

import java.util.ArrayList;

import org.theBOC.theboc.Adapters.BookListAdapter;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
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
	private SharedPreferences sharedpreferences;
	public static final String currentValues = "BibleCurrentValues";
	private int lastExpandedPosition = -1;
	public static final String Language = "languageKey";
	public static final String BookId = "bookIdKey"; 
	public static final String Testament = "testamentKey"; 
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
    	sharedpreferences = this.getActivity().getSharedPreferences(currentValues, Context.MODE_PRIVATE);
    	String language = sharedpreferences.getString(Language, "");
    	int currentBookId = sharedpreferences.getInt(BookId, 0);
    	int currentTestament = sharedpreferences.getInt(Testament, 0);
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
		if(currentBookId > 0) {
			int position;
			if(currentBookId > NUM_OLD_TESTAMENT_BOOKS && currentTestament == this.theTestament)
			{
				position = currentBookId - NUM_OLD_TESTAMENT_BOOKS - 1;
				lstView.expandGroup(position, true);
			}
			if(currentBookId <= NUM_OLD_TESTAMENT_BOOKS && currentTestament == this.theTestament)
			{
				position = currentBookId - 1;
				lstView.expandGroup(position, true);
			}
		}
		return frag;
    }

	}
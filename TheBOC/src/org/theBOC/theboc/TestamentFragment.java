package org.theBOC.theboc;

import java.util.ArrayList;
import java.util.HashMap;

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
	private HashMap<org.theBOC.theboc.Models.Book, ArrayList<Integer>> books;
	private SharedPreferences sharedpreferences;
	public static final String currentValues = "BibleCurrentValues";
	private int lastExpandedPosition = -1;
	public static final String Language = "languageKey";
	public TestamentFragment(){
		
	}
	public TestamentFragment(int value){
		this.theTestament = value;
	}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    	if(bookDB == null)
    		bookDB = new org.theBOC.theboc.database.Book(this.getActivity());
    	sharedpreferences = this.getActivity().getSharedPreferences(currentValues, Context.MODE_PRIVATE);
    	String language = sharedpreferences.getString(Language, "");
		books = bookDB.getBooksWithChapterList(this.theTestament, language);
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
		
		BookListAdapter adt = new BookListAdapter(this.getActivity(), bookDB.booksList, books);
		lstView.setAdapter(adt);
		return frag;
    }

	}
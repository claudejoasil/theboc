package org.theBOC.theboc.Adapters;

import java.util.ArrayList;

import org.theBOC.theboc.R;
import org.theBOC.theboc.Models.Book;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BookListAdapter extends BaseAdapter {
	private Context context;
	private ArrayList<Book> books;
	
	public BookListAdapter(Context context, ArrayList<Book> books){
		this.context = context;
		this.books = books;
	}

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

}

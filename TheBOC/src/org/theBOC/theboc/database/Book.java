package org.theBOC.theboc.database;
import java.util.ArrayList;

import org.theBOC.theboc.BOCdb;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
	public class Book {	
		private BOCdb bocDB;
		public Book(Context context)
		{
			bocDB = BOCdb.getInstance(context);
		}
		public ArrayList<org.theBOC.theboc.Models.Book> getBooks(int testament, String language) 
		{
			SQLiteDatabase db = bocDB.getReadableDatabase();
			SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
			if(language == null || language == "")
			{
				language = "ENGLISH";
			}
			String [] sqlSelect = {"ZBOOKID", "ZNUMCHAPTERS", "ZNAME", "ZSHORTNAME"}; 
			String sqlTables = "ZBIBLEBOOKS" + language;
			String where = "ZTESTAMENT=" + testament;
			qb.setTables(sqlTables);
			Cursor c = qb.query(db, sqlSelect, where, null, null, null, null);
			c.moveToFirst();
			ArrayList<org.theBOC.theboc.Models.Book> books = new ArrayList<org.theBOC.theboc.Models.Book>();
			if (c.moveToFirst()) {
	            do {
	            	org.theBOC.theboc.Models.Book book = new org.theBOC.theboc.Models.Book();
	                book.setBookId(Integer.parseInt(c.getString(0)));
	                book.setNumChapters(Integer.parseInt(c.getString(1)));
	                book.setTestament(testament);
	                book.setName(c.getString(2));
	                book.setShortName(c.getString(3));
	                books.add(book);
	            } while (c.moveToNext());
	        }
			return books;
		}
		public org.theBOC.theboc.Models.Book getBook(int bookId, String language) 
		{
			SQLiteDatabase db = bocDB.getReadableDatabase();
			SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
			if(language == null || language == "")
			{
				language = "ENGLISH";
			}
			String [] sqlSelect = {"ZBOOKID", "ZNUMCHAPTERS", "ZTESTAMENT", "ZNAME", "ZSHORTNAME"}; 
			String sqlTables = "ZBIBLEBOOKS" + language;
			String where = "ZBOOKID=" + bookId;
			qb.setTables(sqlTables);
			Cursor c = qb.query(db, sqlSelect, where, null, null, null, null);
			c.moveToFirst();
			org.theBOC.theboc.Models.Book book = new org.theBOC.theboc.Models.Book();
			if (c.moveToFirst()) {	           
	            	book = new org.theBOC.theboc.Models.Book();
	                book.setBookId(Integer.parseInt(c.getString(0)));
	                book.setNumChapters(Integer.parseInt(c.getString(1)));
	                book.setTestament(Integer.parseInt(c.getString(2)));
	                book.setName(c.getString(3));
	                book.setShortName(c.getString(4));
	        }
			return book;
		}		
	}

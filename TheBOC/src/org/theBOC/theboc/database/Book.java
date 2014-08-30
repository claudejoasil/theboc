package org.theBOC.theboc.database;
import java.util.ArrayList;
import java.util.HashMap;

import org.theBOC.theboc.BOCdb;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
	public class Book {	
		private BOCdb bocDB;
		public ArrayList<org.theBOC.theboc.Models.Book> booksList; //TODO I DON'T KNOW
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
		
		public HashMap<org.theBOC.theboc.Models.Book, ArrayList<Integer>> getBooksWithChapterList(int testament, String language) 
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
			HashMap<org.theBOC.theboc.Models.Book, ArrayList<Integer>> books = new HashMap<org.theBOC.theboc.Models.Book, ArrayList<Integer>>();
			if (c.moveToFirst()) {
				this.booksList = new ArrayList<org.theBOC.theboc.Models.Book>();
	            do {
	            	org.theBOC.theboc.Models.Book book = new org.theBOC.theboc.Models.Book();
	            	int numChapters = Integer.parseInt(c.getString(1));
	                book.setBookId(Integer.parseInt(c.getString(0)));
	                book.setNumChapters(numChapters);
	                book.setTestament(testament);
	                book.setName(c.getString(2));
	                book.setShortName(c.getString(3));
	                this.booksList.add(book);
	                ArrayList<Integer> chapters = new ArrayList<Integer>();
	                for(int i = 1; i <= numChapters; i++)
	                {
	                	chapters.add(i);
	                }
	                books.put(book, chapters);
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

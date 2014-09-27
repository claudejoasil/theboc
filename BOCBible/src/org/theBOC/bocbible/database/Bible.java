package org.theBOC.bocbible.database;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;

public class Bible extends DbBase {
	public Bible(Context context)
	{
		super(context);
	}
	public ArrayList<org.theBOC.bocbible.Models.Bible> getVerses(int bookId, int chapter, String version) 
	{
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		String [] sqlSelect = {"Z_PK", "ZVERSE", "ZVERSETEXT, ZHIGHLIGHT"};
		String sqlTables = "ZBIBLE" + version;
		String where = "ZBOOKID=" + bookId + " AND ZCHAPTER=" + chapter;
		ArrayList<org.theBOC.bocbible.Models.Bible> verses = new ArrayList<org.theBOC.bocbible.Models.Bible>();
		qb.setTables(sqlTables);
		try
		{
			Cursor c = qb.query(DB, sqlSelect, where, null, null, null, null);
			c.moveToFirst();
			
			if (c.moveToFirst()) {
	            do {
	            	org.theBOC.bocbible.Models.Bible bible = new org.theBOC.bocbible.Models.Bible();
	            	bible.setPk(c.getInt(0));
	                bible.setVerse(Integer.parseInt(c.getString(1)));
	                bible.setVerseText(c.getString(2));
	                bible.setHighLights(c.getString(3));
	                verses.add(bible);
	            } while (c.moveToNext());
	        }
		}
		catch(Exception e)
		{
			// THIS Version doesn't exist yet
		}
		return verses;
	}
	
	public org.theBOC.bocbible.Models.Bible getWeeklyVerse(String version, int weekNumber) 
	{
		String  sqlSelect = "SELECT A.ZBOOKID, A.ZBOOK, A.ZCHAPTER, A.ZVERSE, A.ZVERSETEXT FROM ZBIBLE" + version + " A " +
				"INNER JOIN ZWEEKLYVERSE B ON A.ZBOOKID=B.ZBOOKID AND A.ZCHAPTER=B.ZCHAPTER AND A.ZVERSE = B.ZVERSE " +
				"WHERE B.ZID = " + weekNumber;
		
		org.theBOC.bocbible.Models.Bible bible = new org.theBOC.bocbible.Models.Bible();
		try
		{
			Cursor c = DB.rawQuery(sqlSelect, null);
			c.moveToFirst();
			if (c.moveToFirst()) {	            
	            	bible.setBookId(c.getInt(0));
	            	bible.setBook(c.getString(1));
	            	bible.setChapter(c.getInt(2));
	                bible.setVerse(c.getInt(3));
	                bible.setVerseText(c.getString(4));
	        }
		}
		catch(Exception e)
		{
			// THIS Version doesn't exist yet
		}
		return bible;
	}
	public int GetNumVerses(int bookId, int chapter, String version)
	{
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		String [] sqlSelect = {"count(*)"};
		String sqlTables = "ZBIBLE" + version;
		String where = "ZBOOKID=" + bookId + " AND ZCHAPTER=" + chapter;
		qb.setTables(sqlTables);
		try
		{
			Cursor c = qb.query(DB, sqlSelect, where, null, null, null, null);
			c.moveToFirst();
			if (c.moveToFirst())
				return c.getInt(0);
		}
		catch(Exception e)
		{
			// Error
		}
		return 0;
	}
	
	public void HightLightVerse(int id, String highLight, String version)
	{
		String query = "UPDATE ZBIBLE" + version + " SET ZHIGHLIGHT=COALESCE(ZHIGHLIGHT, '') || '" + highLight + "'" +
				" WHERE Z_PK=" + id;
		try
		{
			DB.execSQL(query);
		}
		catch(Exception e)
		{
			// Not updated!
		}
	}
	
	public void UnHightLightVerse(int id, String version)
	{
		String query = "UPDATE ZBIBLE" + version + " SET ZHIGHLIGHT=''" +
				" WHERE Z_PK=" + id;
		try
		{
			DB.execSQL(query);
		}
		catch(Exception e)
		{
			// Not updated!
		}
	}
	
	public ArrayList<org.theBOC.bocbible.Models.Bible> getHighlightVerses(org.theBOC.bocbible.Models.Version version)
	{
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		String [] sqlSelect = {"Z_PK", "ZVERSE", "ZVERSETEXT, ZHIGHLIGHT, ZBOOK, ZCHAPTER, ZBOOKID"};
		String sqlTables = "ZBIBLE" + version.getShortName();
		String where = "ZHIGHLIGHT IS NOT NULL and ZHIGHLIGHT != ''";
		ArrayList<org.theBOC.bocbible.Models.Bible> verses = new ArrayList<org.theBOC.bocbible.Models.Bible>();
		qb.setTables(sqlTables);
		try
		{
			Cursor c = qb.query(DB, sqlSelect, where, null, null, null, null);
			c.moveToFirst();
			
			if (c.moveToFirst()) {
	            do {
	            	org.theBOC.bocbible.Models.Bible bible = new org.theBOC.bocbible.Models.Bible();
	            	bible.setPk(c.getInt(0));
	                bible.setVerse(c.getInt(1));
	                bible.setVerseText(c.getString(2));
	                bible.setHighLights(c.getString(3));
	                bible.setBook(c.getString(4));
	                bible.setChapter(c.getInt(5));
	                bible.setBookId(c.getInt(6));
	                bible.setVersion(version.getShortName());
	                bible.setVersionId(version.getId());
	                verses.add(bible);
	            } while (c.moveToNext());
	        }
		}
		catch(Exception e)
		{
			
		}
		return verses;
	}
}

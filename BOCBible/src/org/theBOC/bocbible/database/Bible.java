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
	
	public ArrayList<org.theBOC.bocbible.Models.Bible> getDualVersionVerses(int bookId, int chapter, String[] version) 
	{

		int numChapter1 = this.GetNumVerses(bookId, chapter, version[0]);
		int numChapter2 = this.GetNumVerses(bookId, chapter, version[1]);
		String query = "SELECT B1.Z_PK, B1.ZVERSE, B1.ZVERSETEXT, B2.ZVERSETEXT, B1.ZHIGHLIGHT, B2.ZHIGHLIGHT " +
				" FROM ZBIBLE" + version[0] + " B1 " +
				" LEFT JOIN ZBIBLE" + version[1] + " B2 ON B1.ZBOOKID=B2.ZBOOKID AND B1.ZCHAPTER=B2.ZCHAPTER AND B1.ZVERSE = B2.ZVERSE " +
				" WHERE B1.ZBOOKID=" + bookId + " AND B1.ZCHAPTER=" + chapter;
		if(numChapter1 < numChapter2)
		{
			query = "SELECT B2.Z_PK, B2.ZVERSE, B1.ZVERSETEXT, B2.ZVERSETEXT, B1.ZHIGHLIGHT, B2.ZHIGHLIGHT " +
					" FROM ZBIBLE" + version[1] + " B2 " +
					" LEFT JOIN ZBIBLE" + version[0] + " B1 ON B2.ZBOOKID=B1.ZBOOKID AND B2.ZCHAPTER=B1.ZCHAPTER AND B2.ZVERSE = B1.ZVERSE " +
					" WHERE B2.ZBOOKID=" + bookId + " AND B2.ZCHAPTER=" + chapter;
		}
		ArrayList<org.theBOC.bocbible.Models.Bible> verses = new ArrayList<org.theBOC.bocbible.Models.Bible>();
		try
		{
			Cursor c = DB.rawQuery(query, null);
			c.moveToFirst();
			
			if (c.moveToFirst()) {
	            do {
	            	org.theBOC.bocbible.Models.Bible bible = new org.theBOC.bocbible.Models.Bible();
	            	bible.setPk(c.getInt(0));
	                bible.setVerse(c.getInt(1));
	                bible.setVerseText(c.getString(2));
	                bible.setVerseText2(c.getString(3));
	                bible.setHighLights(c.getString(4));
	                bible.setHighLights2(c.getString(5));
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
	                bible.setVersion(version);
	                verses.add(bible);
	            } while (c.moveToNext());
	        }
		}
		catch(Exception e)
		{
			
		}
		return verses;
	}
	
	public ArrayList<org.theBOC.bocbible.Models.Bible> search(String query, int testament, org.theBOC.bocbible.Models.Version version)
	{
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		String sqlTables;
		//TODO: Where we have more versions this solution will not be the best. 
		//Keep it like this for now, indexing each bible version will make the app too big (****)
		org.theBOC.bocbible.enums.Version enumVersion = org.theBOC.bocbible.enums.Version.valueOf(version.getShortName());
		switch(enumVersion)
		{
			case ASV:
			case WEB:
			case ENDby:
			case KJV:
			case YLT:
				sqlTables = "ZBIBLEKJV_FTS";
				break;
			case SAG:
				sqlTables = "ZBIBLESAG_FTS";
				break;
			case LSG:
			case FRDby:
			case OSTER:
			case Martin:
				sqlTables = "ZBIBLELSG_FTS";
				break;
			case HCV:
				sqlTables = "ZBIBLEHCV_FTS";
				break;
			default:
				sqlTables = "ZBIBLE" + version.getShortName() + "_FTS";
		}
		String [] sqlSelect = {"docid", "ZVERSE", "ZVERSETEXT, ZBOOK, ZCHAPTER, ZBOOKID"};
		//String sqlTables = "ZBIBLE" + version.getShortName() + "_FTS"; //****
		String where = " ZVERSETEXT MATCH '*" + query + "*' ";
		if(testament == 2)
		{
			where += " AND ZBOOKID > 39 ";  
		}
		if(testament == 1)
		{
			where += " AND ZBOOKID <= 39 ";  
		}
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
	                //bible.setHighLights(c.getString(3));
	                bible.setBook(c.getString(3));
	                bible.setChapter(c.getInt(4));
	                bible.setBookId(c.getInt(5));
	                bible.setVersion(version);
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

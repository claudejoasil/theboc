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
	public ArrayList<org.theBOC.bocbible.Models.Bible> getVerses(int bookId, int chapter, org.theBOC.bocbible.Models.Version version) 
	{
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		String [] sqlSelect = {"B.Z_PK", "B.ZVERSE", "B.ZVERSETEXT, H.ZHIGHLIGHT_RANGE, B.ZBOOKID, B.ZBOOK, B.ZCHAPTER "};
		String sqlTables = "ZBIBLE" + version.getShortName() + " B LEFT JOIN ZHIGHLIGHT H ON B.Z_PK = H.ZVERSEID AND H.ZVERSIONID = " + version.getId();
		String where = "B.ZBOOKID=" + bookId + " AND B.ZCHAPTER=" + chapter;
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
	                bible.setBookId(c.getInt(4));
	                bible.setBook(c.getString(5));
	                bible.setChapter(c.getInt(6));
	                bible.setVersion(version);
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
	
	public ArrayList<org.theBOC.bocbible.Models.Bible> getDualVersionVerses(int bookId, int chapter, ArrayList<org.theBOC.bocbible.Models.Version> version) 
	{

		int numChapter1 = this.GetNumVerses(bookId, chapter, version.get(0).getShortName());
		int numChapter2 = this.GetNumVerses(bookId, chapter, version.get(1).getShortName());
		String query =  "SELECT B1.Z_PK, B1.ZVERSE, B1.ZVERSETEXT, B2.ZVERSETEXT, B1.ZHIGHLIGHT_RANGE, B2.ZHIGHLIGHT_RANGE " +
						" FROM (" +
						"SELECT Z_PK, ZVERSE, ZVERSETEXT, ZBOOKID, ZCHAPTER, ZHIGHLIGHT_RANGE FROM ZBIBLE" + version.get(0).getShortName() + " B LEFT JOIN ZHIGHLIGHT H ON B.Z_PK = H.ZVERSEID AND H.ZVERSIONID = " + version.get(0).getId() + " WHERE B.ZBOOKID=" + bookId + " AND B.ZCHAPTER=" + chapter + 
						") B1 LEFT JOIN (" +
						"SELECT Z_PK, ZVERSE, ZVERSETEXT, ZBOOKID, ZCHAPTER, ZHIGHLIGHT_RANGE FROM ZBIBLE" + version.get(1).getShortName() + " B LEFT JOIN ZHIGHLIGHT H ON B.Z_PK = H.ZVERSEID AND H.ZVERSIONID = " + version.get(1).getId() + " WHERE B.ZBOOKID=" + bookId + " AND B.ZCHAPTER=" + chapter +
						") B2 ON B1.ZBOOKID=B2.ZBOOKID AND B1.ZCHAPTER=B2.ZCHAPTER AND B1.ZVERSE = B2.ZVERSE ";
		
		if(numChapter1 < numChapter2)
		{
			query = "SELECT B2.Z_PK, B2.ZVERSE, B1.ZVERSETEXT, B2.ZVERSETEXT, B1.ZHIGHLIGHT_RANGE, B2.ZHIGHLIGHT_RANGE " +
					" FROM (" +
					"SELECT Z_PK, ZVERSE, ZVERSETEXT, ZBOOKID, ZCHAPTER, ZHIGHLIGHT_RANGE FROM ZBIBLE" + version.get(1).getShortName() + " B LEFT JOIN ZHIGHLIGHT H ON B.Z_PK = H.ZVERSEID AND H.ZVERSIONID = " + version.get(1).getId() + " WHERE B.ZBOOKID=" + bookId + " AND B.ZCHAPTER=" + chapter + 
					") B2 LEFT JOIN (" +
					"SELECT Z_PK, ZVERSE, ZVERSETEXT, ZBOOKID, ZCHAPTER, ZHIGHLIGHT_RANGE FROM ZBIBLE" + version.get(0).getShortName() + " B LEFT JOIN ZHIGHLIGHT H ON B.Z_PK = H.ZVERSEID AND H.ZVERSIONID = " + version.get(0).getId() + " WHERE B.ZBOOKID=" + bookId + " AND B.ZCHAPTER=" + chapter +
					") B1 ON B1.ZBOOKID=B2.ZBOOKID AND B1.ZCHAPTER=B2.ZCHAPTER AND B1.ZVERSE = B2.ZVERSE ";
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
	public org.theBOC.bocbible.Models.Bible getWeeklyVerse(org.theBOC.bocbible.Models.Version version, int weekNumber) 
	{
		String  sqlSelect = "SELECT A.ZBOOKID, A.ZBOOK, A.ZCHAPTER, A.ZVERSE, A.ZVERSETEXT FROM ZBIBLE" + version.getShortName() + " A " +
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
	                bible.setVersion(version);
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
	
	public void HightLightVerse(int id, String highLight, org.theBOC.bocbible.Models.Version version)
	{
		String query = "INSERT OR REPLACE INTO ZHIGHLIGHT (ZID, ZVERSIONID, ZVERSEID, ZHIGHLIGHT_RANGE) " +
				       "values (" +
				       "(SELECT ZID FROM ZHIGHLIGHT WHERE ZVERSEID = " + id + " AND ZVERSIONID = " + version.getId() + "), " +
				       version.getId() + ", " +
				       id + ", " + 
				       "COALESCE((SELECT ZHIGHLIGHT_RANGE FROM ZHIGHLIGHT WHERE ZVERSEID = " + id + " AND ZVERSIONID = " + version.getId() + "), '') || '" + highLight + "')";
		try
		{
			DB.execSQL(query);
		}
		catch(Exception e)
		{
			// Not updated!
		}
	}
	
	public void UnHightLightVerse(int id, org.theBOC.bocbible.Models.Version version)
	{
		String query = "DELETE FROM ZHIGHLIGHT WHERE ZVERSEID=" + id + " AND ZVERSIONID = " + version.getId();
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
		String [] sqlSelect = {"B.Z_PK", "B.ZVERSE", "B.ZVERSETEXT, H.ZHIGHLIGHT_RANGE, B.ZBOOK, B.ZCHAPTER, B.ZBOOKID"};
		String sqlTables = "ZBIBLE" + version.getShortName() + " B INNER JOIN ZHIGHLIGHT H ON B.Z_PK = H.ZVERSEID AND H.ZVERSIONID = " + version.getId();
		String where = "H.ZHIGHLIGHT_RANGE IS NOT NULL and H.ZHIGHLIGHT_RANGE != ''";
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
		String[] parts = query.split("\\|", -1);
		String where = "";
		if(testament == 2)
		{
			where += "ZBOOKID > 39 AND ZVERSETEXT MATCH (";  
		}
		else if(testament == 1)
		{
			where += "ZBOOKID <= 39 AND ZVERSETEXT MATCH (";  
		}
		else
		{
			where += "ZVERSETEXT MATCH ('";  
		}
		for(int i = 0; i < parts.length; i++)
		{
			if(!parts[i].trim().equals(""))
			{
				if(i < parts.length - 1)
					where += "\"*" + parts[i].trim() + "*\" OR ";
				else
					where += "\"*" + parts[i].trim() + "*\"";
			}
		}
		where += "')";
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

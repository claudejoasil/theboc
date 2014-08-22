package org.theBOC.theboc.database;
import java.util.ArrayList;

import org.theBOC.theboc.BOCdb;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

public class Bible {
	private BOCdb bocDB;
	public Bible(Context context)
	{
		bocDB = BOCdb.getInstance(context);
	}
	public ArrayList<org.theBOC.theboc.Models.Bible> getVerses(int bookId, int chapter, String version) 
	{
		SQLiteDatabase db = bocDB.getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String [] sqlSelect = {"ZVERSE", "ZVERSETEXT"}; 
		String sqlTables = "ZBIBLE" + version;
		String where = "ZBOOKID=" + bookId + " AND ZCHAPTER=" + chapter;
		qb.setTables(sqlTables);
		Cursor c = qb.query(db, sqlSelect, where, null, null, null, null);
		c.moveToFirst();
		ArrayList<org.theBOC.theboc.Models.Bible> verses = new ArrayList<org.theBOC.theboc.Models.Bible>();
		if (c.moveToFirst()) {
            do {
            	org.theBOC.theboc.Models.Bible bible = new org.theBOC.theboc.Models.Bible();
                bible.setVerse(Integer.parseInt(c.getString(0)));
                bible.setVerseText(c.getString(1));
                verses.add(bible);
            } while (c.moveToNext());
        }
		return verses;
	}
}

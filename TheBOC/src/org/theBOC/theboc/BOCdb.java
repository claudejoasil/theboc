package org.theBOC.theboc;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

public class BOCdb extends SQLiteAssetHelper{
	private static final String DATABASE_NAME = "theBOC.db";
	private static final int DATABASE_VERSION = 1;
	
	public BOCdb(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		
		// you can use an alternate constructor to specify a database location 
		// (such as a folder on the sd card)
		// you must ensure that this folder is available and you have permission
		// to write to it
		//super(context, DATABASE_NAME, context.getExternalFilesDir(null).getAbsolutePath(), null, DATABASE_VERSION);
		
	}

	public Cursor getVerses() {

		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		String [] sqlSelect = {"ZVERSE", "ZVERSETEXT"}; 
		String sqlTables = "ZBIBLELSG";
		String where = "ZBOOKID=1 AND ZCHAPTER=1";
		qb.setTables(sqlTables);
		Cursor c = qb.query(db, sqlSelect, where, null, null, null, null);

		c.moveToFirst();
		return c;

	}
}

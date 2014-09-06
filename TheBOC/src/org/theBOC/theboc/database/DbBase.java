package org.theBOC.theboc.database;

import org.theBOC.theboc.BOCdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DbBase {
	protected BOCdb bocDB;
	protected SQLiteDatabase DB;
	public DbBase(Context context)
	{
		bocDB = BOCdb.getInstance(context);
		DB = bocDB.getReadableDatabase();
	}
	public void beginTransaction()
	{
		DB.beginTransaction();
	}
	public void setTransactionSuccessful()
	{
		DB.setTransactionSuccessful();
	}
	public void endTransaction()
	{
		DB.endTransaction();
	}
	public void executeQuery(String sql)
	{
		DB.execSQL(sql);
	}
}

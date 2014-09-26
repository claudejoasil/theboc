package org.theBOC.bocbible.database;

import org.theBOC.bocbible.BOCdb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

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
	public SQLiteStatement compileStatement(String sql)
	{
		return DB.compileStatement(sql);
	}
	public void close()
	{
		DB.close();
	}
}

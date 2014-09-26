package org.theBOC.bocbible;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

import android.content.Context;

public class BOCdb extends SQLiteAssetHelper{
	private static final String DATABASE_NAME = "theBOC.db";
	private static final int DATABASE_VERSION = 2;
	private static BOCdb dbInstance;
	
	private BOCdb(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);		
		//setForcedUpgrade();
	}
	public static BOCdb getInstance(Context context)
	{
		if(dbInstance != null)
			return dbInstance;
		else
			return new BOCdb(context);
	}
}

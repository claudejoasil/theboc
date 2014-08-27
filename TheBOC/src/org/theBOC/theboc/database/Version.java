package org.theBOC.theboc.database;
import java.util.ArrayList;

import org.theBOC.theboc.BOCdb;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
public class Version {
	private BOCdb bocDB;
	public Version(Context context)
	{
		bocDB = BOCdb.getInstance(context);
	}
	
	public ArrayList<org.theBOC.theboc.Models.Version> getVersions(String language, boolean withHeader) 
	{
		SQLiteDatabase db = bocDB.getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		String [] sqlSelect = {"ZID", "ZLANGUAGE", "ZNAME", "ZSHORTNAME"}; 
		String sqlTables = "ZBIBLEVERSIONS";
		String where = null;
		if(language != null && language != "")
		{
			where = "ZLANGUAGE=" + language;
		}
		qb.setTables(sqlTables);
		Cursor c = qb.query(db, sqlSelect, where, null, null, null, null);
		c.moveToFirst();
		ArrayList<org.theBOC.theboc.Models.Version> versions = new ArrayList<org.theBOC.theboc.Models.Version>();
		if (c.moveToFirst()) {
			if(withHeader)
			{
				String currentLanguage = "";
				do {
	            	org.theBOC.theboc.Models.Version version = new org.theBOC.theboc.Models.Version();
	            	if(!currentLanguage.equalsIgnoreCase(c.getString(1)))
	            	{
	            		version.setIsGroupHeader(true);
	            		version.setName(c.getString(1));
	            		versions.add(version);
	            		currentLanguage = c.getString(1);
	            	}
	            	version = new org.theBOC.theboc.Models.Version();
	                version.setId(Integer.parseInt(c.getString(0)));
	                version.setLanguage(c.getString(1));
	                version.setName(c.getString(2));
	                version.setShortName(c.getString(3));
	                versions.add(version);
	            } while (c.moveToNext());
			} else {			
	            do {
	            	org.theBOC.theboc.Models.Version version = new org.theBOC.theboc.Models.Version();
	                version.setId(Integer.parseInt(c.getString(0)));
	                version.setLanguage(c.getString(1));
	                version.setName(c.getString(2));
	                version.setShortName(c.getString(3));
	                versions.add(version);
	            } while (c.moveToNext());
			}
        }
		return versions;
	}
	public org.theBOC.theboc.Models.Version getVersion(int Id, String ShortName) 
	{
		SQLiteDatabase db = bocDB.getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		String [] sqlSelect = {"ZID", "ZLANGUAGE", "ZNAME", "ZSHORTNAME"}; 
		String sqlTables = "ZBIBLEVERSIONS";
		String where = null;
		if(Id > 0)
		{
			where = "ZID=" + Id;
		}
		else if(ShortName != null && ShortName != "")
		{
			where = "ZSHORTNAME=" + ShortName;
		}
		else
		{
			return null;
		}
		qb.setTables(sqlTables);
		Cursor c = qb.query(db, sqlSelect, where, null, null, null, null);
		c.moveToFirst();
		org.theBOC.theboc.Models.Version version = new org.theBOC.theboc.Models.Version();
		if (c.moveToFirst()) {	           
            	version = new org.theBOC.theboc.Models.Version();
            	version.setId(Integer.parseInt(c.getString(0)));
                version.setLanguage(c.getString(1));
                version.setName(c.getString(2));
                version.setShortName(c.getString(3));        }
		return version;
	}
}

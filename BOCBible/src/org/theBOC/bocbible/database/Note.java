package org.theBOC.bocbible.database;
import java.util.ArrayList;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteQueryBuilder;
public class Note extends DbBase {
	public Note(Context context)
	{
		super(context);
	}
	
	public ArrayList<org.theBOC.bocbible.Models.Note> getNotes() 
	{
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		String [] sqlSelect = {"ZID", "ZISOPEN", "ZNOTE_TEXT"}; 
		String sqlTables = "ZNOTE";
		String where = null;
		qb.setTables(sqlTables);
		Cursor c = qb.query(DB, sqlSelect, where, null, null, null, "ZLANGUAGE");
		c.moveToFirst();
		ArrayList<org.theBOC.bocbible.Models.Note> notes = new ArrayList<org.theBOC.bocbible.Models.Note>();
		if (c.moveToFirst()) {
						
	            do {
	            	org.theBOC.bocbible.Models.Note note = new org.theBOC.bocbible.Models.Note();
	                note.setId(c.getInt(0));
	                note.setIsOpen(c.getInt(1) == 0 ? false : true);
	                note.setTxt(c.getString(2));
	                notes.add(note);
	            } while (c.moveToNext());
			}
        
		return notes;
	}
	public org.theBOC.bocbible.Models.Note getNote(int Id) 
	{
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		String [] sqlSelect = {"ZID", "ZISOPEN", "ZNOTE_TEXT"}; 
		String sqlTables = "ZNOTE";
		String where = null;
		where = "ZID=" + Id;
		qb.setTables(sqlTables);
		Cursor c = qb.query(DB, sqlSelect, where, null, null, null, null);
		c.moveToFirst();
		org.theBOC.bocbible.Models.Note note = new org.theBOC.bocbible.Models.Note();
		if (c.moveToFirst()) {	           
            	note = new org.theBOC.bocbible.Models.Note();
            	note.setId(c.getInt(0));
            	note.setIsOpen(c.getInt(1) == 0 ? false : true);
                note.setTxt(c.getString(2));
        }
		return note;
	}

	public void deleteNote(int id)
	{
		String query = "DELETE FROM ZNOTE WHERE ZID=" + id;
		try
		{
			DB.execSQL(query);
		}
		catch(Exception ex)
		{
			//Error occurred
		}
	}
}


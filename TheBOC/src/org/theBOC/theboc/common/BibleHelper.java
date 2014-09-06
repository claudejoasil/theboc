package org.theBOC.theboc.common;

import android.content.Context;
import android.content.SharedPreferences;

public class BibleHelper {
	public static final String BookId = "bookIdKey"; 
	public static final String BookName = "bookNameKey";
	public static final String Chapter = "chapterKey"; 
	public static final String Verse = "verseKey"; 
	public static final String VersionId = "versionIdKey";
	public static final String Language = "languageKey";
	public static final String Testament = "testamentKey";
	public static final String TextSize = "textSizeKey";
	
	private int currentBookId;
	private String currentBookName;
	private int currentChapter;
	private int currentVerse;
	private int currentVersionId;
	private String currentLanguage;
	private int currentTestament;
	private int currentTextSize;
	
	private static BibleHelper instance;
	private SharedPreferences sharedpreferences;
	public static final String currentValues = "BibleCurrentValues";
	private BibleHelper() 
	{
		
	}
	public static BibleHelper getInstance(Context context)
	{
		if(instance == null)
		{
			instance = new BibleHelper();
			instance.sharedpreferences = context.getSharedPreferences(currentValues, Context.MODE_PRIVATE);
		}
		instance.currentBookId = 0; 
		instance.currentBookName = ""; 
		instance.currentChapter = 0; 
		instance.currentVerse = 0;
		instance.currentLanguage = "";
		instance.currentVersionId = 0;
		instance.currentTestament = 0;
		instance.currentTextSize = 0;
		return instance;
	}
	
	public int getCurrentBookId(int defaultVal)
	{
		if(this.currentBookId == 0)
			this.currentBookId = sharedpreferences.getInt(BookId, defaultVal);
		return this.currentBookId;
	}
	public void setCurrentBookId(int value)
	{
		this.currentBookId = value;
	}
	
	public String getCurrentBookName(String defaultVal)
	{
		if(this.currentBookName == "")
			this.currentBookName = sharedpreferences.getString(BookName, defaultVal);
		return this.currentBookName;
	}
	public void setCurrentBookName(String value)
	{
		this.currentBookName = value;
	}
	
	public int getCurrentChapter(int defaultVal)
	{
		if(this.currentChapter == 0)
			this.currentChapter = sharedpreferences.getInt(Chapter, defaultVal);
		return this.currentChapter;
	}
	public void setCurrentChapter(int value)
	{
		this.currentChapter = value;
	}
	public void incrementChapter(int increment)
	{
		this.currentChapter = this.currentChapter + increment;
	}
	
	public int getCurrentVerse(int defaultVal)
	{
		if(this.currentVerse == 0)
			this.currentVerse = sharedpreferences.getInt(Verse, defaultVal);
		return this.currentVerse;
	}
	public void setCurrentVerse(int value)
	{
		this.currentVerse = value;
	}
	
	public int getCurrentVersionId(int defaultVal)
	{
		if(this.currentVersionId == 0)
			this.currentVersionId = sharedpreferences.getInt(VersionId, defaultVal);
		return this.currentVersionId;
	}
	public void setCurrentVersionId(int value)
	{
		this.currentVersionId = value;
	}
	
	public String getCurrentLanguage(String defaultVal)
	{
		if(this.currentLanguage == "")
			this.currentLanguage = sharedpreferences.getString(Language, defaultVal);
		return this.currentLanguage;
	}
	public void setCurrentLanguage(String value)
	{
		this.currentLanguage = value;
	}
	
	public int getCurrentTestament(int defaultVal)
	{
		if(this.currentTestament == 0)
			this.currentTestament = sharedpreferences.getInt(Testament, defaultVal);
		return this.currentTestament;
	}
	public void setCurrentTestament(int value)
	{
		this.currentTestament = value;
	}
	
	public int getCurrentTextSize(int defaultVal)
	{
		if(this.currentTextSize == 0)
			this.currentTextSize = sharedpreferences.getInt(TextSize, defaultVal);
		return this.currentTextSize;
	}
	public void setCurrentTextSize(int value)
	{
		this.currentTextSize = value;
	}
	
	public void persistCurrentValues()
	{
		if(this.currentBookId <= 0 && 
				this.currentBookName.equalsIgnoreCase("") && 
				this.currentChapter <= 0 && 
				this.currentVerse <= 0 &&
				this.currentLanguage.equalsIgnoreCase("") &&
				this.currentVersionId <= 0 &&
				this.currentTestament <= 0 &&
				this.currentTextSize <= 0)
			return;
		SharedPreferences.Editor ed = sharedpreferences.edit();
		if(this.currentBookId > 0)
			ed.putInt(BibleHelper.BookId, this.currentBookId);
		
		if(!this.currentBookName.equalsIgnoreCase(""))
			ed.putString(BibleHelper.BookName, this.currentBookName);
		
		if(this.currentChapter > 0)
			ed.putInt(BibleHelper.Chapter, this.currentChapter);
		
		if(this.currentVerse > 0)
			ed.putInt(BibleHelper.Verse, this.currentVerse);
		
		if(!this.currentLanguage.equalsIgnoreCase(""))
			ed.putString(BibleHelper.Language, this.currentLanguage);
		
		if(this.currentVersionId > 0)
			ed.putInt(BibleHelper.VersionId, this.currentVersionId);
		
		if(this.currentTestament > 0)
			ed.putInt(BibleHelper.Testament, this.currentTestament);
		
		if(this.currentTextSize > 0)
			ed.putInt(BibleHelper.TextSize, this.currentTextSize);
        ed.commit();
	}
}

package org.theBOC.bocbible.common;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;

public class BibleHelper {
	public static final String BookId = "bookIdKey"; 
	public static final String BookName = "bookNameKey";
	public static final String Chapter = "chapterKey"; 
	public static final String Verse = "verseKey"; 
	public static final String VersionId = "versionIdKey";
	public static final String VersionName = "versionNameKey";
	public static final String VersionId2 = "versionId2Key";
	public static final String VersionName2 = "versionName2Key";
	public static final String Language = "languageKey";
	public static final String Testament = "testamentKey";
	public static final String TextSize = "textSizeKey";
	public static final String WeeklyVerseText = "weeklyVerseTextKey";
	public static final String WeeklyVerse = "weeklyVerseKey";
	public static final String WeeklyChapter = "weeklyChapterKey";
	public static final String WeeklyBookName = "weeklyBookNameKey";
	public static final String WeeklyBookId = "weeklyBookIdKey";
	public static final String WeeklyVerseWeek = "weeklyVerseWeek";
	public static final String WeeklyVerseVersion = "weeklyVerseVersion";
			
	private int currentBookId;
	private String currentBookName;
	private int currentChapter;
	private int currentVerse;
	private int currentVersionId;
	private int currentVersionId2;
	private String currentVersionName;
	private String currentVersionName2;
	private String currentLanguage;
	private int currentTestament;
	private int currentTextSize;
	private int currentWeeklyBookId;
	private int currentWeeklyChapter;
	private int currentWeeklyVerse;
	private int currentWeeklyVerseWeek;
	private String currentWeeklyBookName;
	private String currentWeeklyVerseText;
	private String currentWeeklyVerseVersion;
	public static ArrayList<Integer> unHighlighted = null;
 	
	private static BibleHelper instance;
	private SharedPreferences sharedpreferences;
	public static final String currentValues = "BibleCurrentValues";
	private BibleHelper() 
	{
		this.currentBookId = 0; 
		this.currentBookName = ""; 
		this.currentChapter = 0; 
		this.currentVerse = 0;
		this.currentLanguage = "";
		this.currentVersionId = 0;
		this.currentVersionId2 = 0;
		this.currentVersionName2 = "";
		this.currentVersionName = "";
		this.currentTestament = 0;
		this.currentTextSize = 0;
		this.currentWeeklyBookId = 0;
		this.currentWeeklyBookName = "";
		this.currentWeeklyChapter = 0;
		this.currentWeeklyVerse = 0;
		this.currentWeeklyVerseText = "";
		this.currentWeeklyVerseWeek = 0;
	}
	public static BibleHelper getInstance(Context context)
	{
		if(instance == null)
		{
			instance = new BibleHelper();
			instance.sharedpreferences = context.getSharedPreferences(currentValues, Context.MODE_PRIVATE);
		}
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
		this.currentVerse = 1;
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
	public int getCurrentVersionId2()
	{
		if(this.currentVersionId2 == 0)
			this.currentVersionId2 = sharedpreferences.getInt(VersionId2, 0);
		return this.currentVersionId2;
	}
	public void setCurrentVersionId2(int value)
	{
		this.currentVersionId2 = value;
	}
	public String getCurrentVersionName(String defaultVal)
	{
		if(this.currentVersionName == "")
			this.currentVersionName = sharedpreferences.getString(VersionName, defaultVal);
		return this.currentVersionName;
	}
	public void setCurrentVersionName(String value)
	{
		this.currentVersionName = value;
	}
	public String getCurrentVersionName2()
	{
		if(this.currentVersionName2 == "")
			this.currentVersionName2 = sharedpreferences.getString(VersionName2, "");
		return this.currentVersionName2;
	}
	public void setCurrentVersionName2(String value)
	{
		this.currentVersionName2 = value;
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
				this.currentVersionName.equalsIgnoreCase("") &&
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
		
		if(!this.currentVersionName.equalsIgnoreCase(""))
			ed.putString(BibleHelper.VersionName, this.currentVersionName);
		
		ed.putInt(BibleHelper.VersionId2, this.currentVersionId2);
		ed.putString(BibleHelper.VersionName2, this.currentVersionName2);
		
		if(this.currentTestament > 0)
			ed.putInt(BibleHelper.Testament, this.currentTestament);
		
		if(this.currentTextSize > 0)
			ed.putInt(BibleHelper.TextSize, this.currentTextSize);
        ed.commit();
	}
	
	public int getCurrentWeeklyBookId(int defaultVal)
	{
		if(this.currentWeeklyBookId == 0)
			this.currentWeeklyBookId = sharedpreferences.getInt(WeeklyBookId, defaultVal);
		return this.currentWeeklyBookId;
	}
	public void setCurrentWeeklyBookId(int value)
	{
		this.currentWeeklyBookId = value;
	}
	
	public int getCurrentWeeklyChapter(int defaultVal)
	{
		if(this.currentWeeklyChapter == 0)
			this.currentWeeklyChapter = sharedpreferences.getInt(WeeklyChapter, defaultVal);
		return this.currentWeeklyChapter;
	}
	public void setCurrentWeeklyChapter(int value)
	{
		this.currentWeeklyChapter = value;
	}
	
	public int getCurrentWeeklyVerse(int defaultVal)
	{
		if(this.currentWeeklyVerse == 0)
			this.currentWeeklyVerse = sharedpreferences.getInt(WeeklyVerse, defaultVal);
		return this.currentWeeklyVerse;
	}
	public void setCurrentWeeklyVerse(int value)
	{
		this.currentWeeklyVerse = value;
	}
	
	public int getCurrentWeeklyVerseWeek(int defaultVal)
	{
		if(this.currentWeeklyVerseWeek == 0)
			this.currentWeeklyVerseWeek = sharedpreferences.getInt(WeeklyVerseWeek, defaultVal);
		return this.currentWeeklyVerseWeek;
	}
	public void setCurrentWeeklyVerseWeek(int value)
	{
		this.currentWeeklyVerseWeek = value;
	}
	
	public String getCurrentWeeklyBookName(String defaultVal)
	{
		if(this.currentWeeklyBookName == "")
			this.currentWeeklyBookName = sharedpreferences.getString(WeeklyBookName, defaultVal);
		return this.currentWeeklyBookName;
	}
	public void setCurrentWeeklyBookName(String value)
	{
		this.currentWeeklyBookName = value;
	}
	
	public String getCurrentWeeklyVerseText(String defaultVal)
	{
		if(this.currentWeeklyVerseText == "")
			this.currentWeeklyVerseText = sharedpreferences.getString(WeeklyVerseText, defaultVal);
		return this.currentWeeklyVerseText;
	}
	public void setCurrentWeeklyVerseText(String value)
	{
		this.currentWeeklyVerseText = value;
	}
	public String getCurrentWeeklyVerseVersion(String defaultVal)
	{
		if(this.currentWeeklyVerseVersion == "")
			this.currentWeeklyVerseVersion = sharedpreferences.getString(WeeklyVerseVersion, defaultVal);
		return this.currentWeeklyVerseVersion;
	}
	public void persistCurrentWeeklyValues()
	{
		if(this.currentWeeklyBookId <= 0 ||
				this.currentWeeklyBookName.equalsIgnoreCase("") || 
				this.currentWeeklyChapter <= 0 ||
				this.currentWeeklyVerse <= 0 ||
				this.currentWeeklyVerseWeek <= 0 ||
				this.currentWeeklyVerseText.equalsIgnoreCase("")
				)
			return;
		SharedPreferences.Editor ed = sharedpreferences.edit();
		ed.putInt(BibleHelper.WeeklyBookId, this.currentWeeklyBookId);
		ed.putString(BibleHelper.WeeklyBookName, this.currentWeeklyBookName);		
		ed.putInt(BibleHelper.WeeklyChapter, this.currentWeeklyChapter);
		ed.putInt(BibleHelper.WeeklyVerse, this.currentWeeklyVerse);
		ed.putString(BibleHelper.WeeklyVerseText, this.currentWeeklyVerseText);
		ed.putString(BibleHelper.WeeklyVerseVersion, this.currentVersionName);
		ed.putInt(BibleHelper.WeeklyVerseWeek, this.currentWeeklyVerseWeek);
        ed.commit();
	}
}

package org.theBOC.theboc.common;

import android.content.SharedPreferences;

public class BibleHelper {
	private String currentValues = "BibleCurrentValues";
	private String BookId = "bookIdKey"; 
	private String Chapter = "chapterKey"; 
	private String Verse = "verseKey"; 
	private String VersionId = "versionIdKey";
	private String Language = "languageKey";
	private String Testament = "testamentKey";
	private SharedPreferences sharedpreferences;
	//= this.getSharedPreferences(currentValues, Context.MODE_PRIVATE);
	//sharedpreferences = this.context.getSharedPreferences(currentValues, Context.MODE_PRIVATE);
	public int getCurrentBookId()
	{
		return sharedpreferences.getInt(BookId, 1);
	}
	public void setCurrentBookId(int bookId)
	{
		sharedpreferences.edit().putInt(BookId, bookId).apply();
	}
	public int getCurrentChapter()
	{
		return sharedpreferences.getInt(Chapter, 1);
	}
	public void setCurrentChapter(int chapter)
	{
		sharedpreferences.edit().putInt(Chapter, chapter).apply();
	}
	public int getCurrentVerse()
	{
		return sharedpreferences.getInt(Verse, 1);
	}
	public void setCurrentVerse(int chapter)
	{
		sharedpreferences.edit().putInt(Verse, chapter).apply();
	}
	public int getCurrentVersion()
	{
		return sharedpreferences.getInt(VersionId, 4);
	}
	public void setCurrentVersion(int versionId)
	{
		sharedpreferences.edit().putInt(VersionId, versionId).apply();
	}
	public String getCurrentLanguage()
	{
		return sharedpreferences.getString(Language, "");
	}
	public void setCurrentLanguage(String language)
	{
		sharedpreferences.edit().putString(Language, language).apply();
	}
	public int getCurrentTestament()
	{
		return sharedpreferences.getInt(Testament, 1);
	}
	public void setCurrentTestament(String testament)
	{
		sharedpreferences.edit().putString(Testament, testament).apply();
	}
}

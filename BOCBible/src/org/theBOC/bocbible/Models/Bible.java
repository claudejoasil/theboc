package org.theBOC.bocbible.Models;

public class Bible {
	private int pk;
	private String book;
	private int bookId;
	private int chapter;
	private String versetext;
	private int verse;
	private String version; //TODO use a version object here instead. so versionId will not be needed
	private int versionId;
	private String highLights;
	public Bible()
	{
	
	}
	public int getPk()
	{
		return pk;
	}
	public void setPk(int pk)
	{
		this.pk = pk;
	}
	public String getBook()
	{
		return book;
	}
	public void setBook(String book)
	{
		this.book = book;
	}
	public int getBookId()
	{
		return bookId;
	}
	public void setBookId(int bookId)
	{
		this.bookId = bookId;
	}
	public int getVerse()
	{
		return verse;
	}
	public void setVerse(int verse)
	{
		this.verse = verse;
	}
	public String getVerseText()
	{
		return versetext;
	}
	public void setVerseText(String verseText)
	{
		this.versetext = verseText;
	}
	public int getChapter()
	{
		return chapter;
	}
	public void setChapter(int chapter)
	{
		this.chapter = chapter;
	}
	public String getVersion()
	{
		return version;
	}
	public void setVersion(String version)
	{
		this.version = version;
	}
	public int getVersionId()
	{
		return versionId;
	}
	public void setVersionId(int versionId)
	{
		this.versionId = versionId;
	}
	public String getHighLights()
	{
		return highLights;
	}
	public void setHighLights(String highLights)
	{
		this.highLights = highLights;
	}
}

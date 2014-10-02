package org.theBOC.bocbible.Models;

public class Bible {
	private int pk;
	private String book;
	private int bookId;
	private int chapter;
	private String versetext;
	private String versetext2;
	private int verse;
	private Version version;
	private Version version2;
	private String highLights;
	private String highLights2;
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
		this.versetext = this.cleanVerseText(verseText);
	}
	public String getVerseText2()
	{
		return versetext2;
	}
	public void setVerseText2(String verseText2)
	{
		this.versetext2 = this.cleanVerseText(verseText2);
	}
	public int getChapter()
	{
		return chapter;
	}
	public void setChapter(int chapter)
	{
		this.chapter = chapter;
	}
	public Version getVersion()
	{
		return version;
	}
	public void setVersion(Version version)
	{
		this.version = version;
	}
	public Version getVersion2()
	{
		return version2;
	}
	public void setVersion2(Version version2)
	{
		this.version2 = version2;
	}
	public String getHighLights()
	{
		return highLights;
	}
	public void setHighLights(String highLights)
	{
		this.highLights = highLights;
	}
	public String getHighLights2()
	{
		return highLights2;
	}
	public void setHighLights2(String highLights2)
	{
		this.highLights2 = highLights2;
	}
	private String cleanVerseText(String txt)
	{
		if(txt != null)
		{
			txt = txt.replace("\\r", "");
		}
		else
		{
			txt = "";
		}
		return txt;
	}
}

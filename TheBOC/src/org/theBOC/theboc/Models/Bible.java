package org.theBOC.theboc.Models;

public class Bible {
	private String book;
	private int bookId;
	private int chapter;
	private String versetext;
	private int verse;
	private String version;
	public Bible()
	{
	
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
}

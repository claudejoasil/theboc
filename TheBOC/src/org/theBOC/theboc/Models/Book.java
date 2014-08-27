package org.theBOC.theboc.Models;

public class Book {
	private int bookId;
	private int numChapters;
	private int testament;
	private String name;
	private String shortName;
	
	public Book()
	{
		
	}
	public int getBookId()
	{
		return this.bookId;				
	}
	public void setBookId(int bookId)
	{
		this.bookId = bookId;				
	}
	public int getNumChapters()
	{
		return this.numChapters;
	}
	public void setNumChapters(int numChapters)
	{
		this.numChapters = numChapters;
	}
	public int getTestament()
	{
		return this.testament;
	}
	public void setTestament(int testament)
	{
		this.testament = testament;
	}
	public String getName()
	{
		return this.name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getShortName()
	{
		if(this.shortName != null && !this.shortName.trim().equals(""))
			return this.shortName;
		else
			return this.name.substring(0, 3);			
	}
	public void setShortName(String shortName)
	{
		this.shortName = shortName;
	}
}

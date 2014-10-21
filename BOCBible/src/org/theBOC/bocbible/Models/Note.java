package org.theBOC.bocbible.Models;

public class Note {
	private int id;
	private boolean isOpen;
	private String title;
	private String txt;
	
	public int getId()
	{
		return this.id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public boolean getIsOpen()
	{
		return this.isOpen;
	}
	public void setIsOpen(boolean isOpen)
	{
		this.isOpen = isOpen;
	}
	public String getTitle()
	{
		return this.title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getTxt()
	{
		return this.txt;
	}
	public void setTxt(String txt)
	{
		this.txt = txt;
	}
}

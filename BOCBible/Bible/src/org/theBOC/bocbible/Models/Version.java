package org.theBOC.bocbible.Models;

public class Version {
	private int id;
	private String language;
	private String name;
	private String shortName;
	private boolean isAvailable;
	private boolean isGroupHeader;
	public Version(){}
	public int getId()
	{
		return this.id;
	}
	public void setId(int id)
	{
		this.id = id;
	}
	public String getLanguage()
	{
		return this.language;
	}
	public void setLanguage(String language)
	{
		this.language = language;
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
		return this.shortName;
	}
	public void setShortName(String shortName)
	{
		this.shortName = shortName;
	}
	public boolean getIsGroupHeader()
	{
		return this.isGroupHeader;
	}
	public void setIsGroupHeader(boolean isGroupHeader)
	{
		this.isGroupHeader = isGroupHeader;
	}
	public boolean getIsAvailable()
	{
		return this.isAvailable;
	}
	public void setIsAvailable(boolean isAvailable)
	{
		this.isAvailable = isAvailable;
	}
}

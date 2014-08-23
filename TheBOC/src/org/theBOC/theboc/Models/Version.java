package org.theBOC.theboc.Models;

public class Version {
	private int id;
	private String language;
	private String name;
	private String shortName;
	public Version()
	{
		
	}
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
}

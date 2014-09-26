package org.theBOC.bocbible.common;

import android.content.Context;

public class MiscHelper {
	private static MiscHelper instance;
	public boolean reloadBiblePage;
	public boolean reloadHighLightsPage;
	
	private MiscHelper() 
	{
		this.reloadBiblePage = false; 
		this.reloadHighLightsPage = false; 
	}
	
	public static MiscHelper getInstance(Context context)
	{
		if(instance == null)
		{
			instance = new MiscHelper();
		}
		return instance;
	}
	public static boolean isInteger(String str)
	{
		try  
		{  
			Integer.parseInt(str);  
		}  
		catch(NumberFormatException nfe)  
		{  
		    return false;  
		}  
		return true;  
	}
}

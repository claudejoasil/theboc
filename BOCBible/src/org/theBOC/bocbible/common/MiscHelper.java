package org.theBOC.bocbible.common;

import android.content.Context;
import org.theBOC.bocbible.enums.Language;

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
	public static String prepareSearchQuery(Language language, String query)
	{
		switch(language)
		{
			case Creole:
				break;
			case English:
				break;
			case French:
				break;
			case Spanish:
				break;
			default:
				return query;
		}
		return query;
	}
}

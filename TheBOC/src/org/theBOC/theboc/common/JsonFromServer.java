package org.theBOC.theboc.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import org.json.JSONObject;

public class JsonFromServer {	
	private String sourceUrl;
	public JsonFromServer() {
        // Empty constructor required for fragment subclasses
    }
	public String SetSourceUrl(){
		return this.sourceUrl;
	}
	public void SetStringUrl(String url){
		this.sourceUrl = url;	
	}
	public JSONObject GetJSON(){
		JSONObject json = null;
		try {
	        URL url = new URL(this.sourceUrl);
	        URLConnection conn = url.openConnection();
	        HttpURLConnection httpConn = (HttpURLConnection) conn;
	        httpConn.setAllowUserInteraction(false);
	        httpConn.setInstanceFollowRedirects(true);
	        httpConn.setRequestMethod("GET");
	        httpConn.connect();
	        InputStream is = httpConn.getInputStream();
	        String parsedString = convertinputStreamToString(is);
	        json = new JSONObject(parsedString);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
		return json;
	}	
	public static String convertinputStreamToString(InputStream ists)
	    throws IOException {
	    if (ists != null) {
	        StringBuilder sb = new StringBuilder();
	        String line;
	        try {
	            BufferedReader r1 = new BufferedReader(new InputStreamReader(
	                    ists, "UTF-8"));
	            while ((line = r1.readLine()) != null) {
	                sb.append(line).append("\n");
	            }
	        } finally {
	            ists.close();
	        }
	        return sb.toString();
	    } else {
	        return "";
	    }
	}
}

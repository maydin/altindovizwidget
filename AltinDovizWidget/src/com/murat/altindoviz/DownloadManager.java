package com.murat.altindoviz;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

public class DownloadManager {

	public static InputStream getAltinStream(String urlString) throws Exception
	{	
		URL url = new URL(urlString);
		HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
		urlConnection.setReadTimeout(10000);
		urlConnection.setConnectTimeout(10000);
		urlConnection.connect();
		InputStream inpStream = urlConnection.getInputStream();
		return inpStream;
	}
	
	public static AltinKaynak getAltinKaynak(String urlString) throws Exception
	{
		InputStream stream = getAltinStream(urlString);
		Serializer serializer = new Persister();
		AltinKaynak altin = serializer.read(AltinKaynak.class, stream);		
		return altin;
	}
	
}

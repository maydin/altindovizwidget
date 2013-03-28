package com.murat.altindoviz;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
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
	
	public static InputStream connect(String url)
    {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url); 
        HttpResponse response;
        try {
            response = httpclient.execute(httpget);

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                return instream;
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        }
        return null;
    }
	
	public static AltinKaynak getAltinKaynak(String urlString) throws Exception
	{
		InputStream stream = connect(urlString);
		Serializer serializer = new Persister();
		AltinKaynak altin = serializer.read(AltinKaynak.class, stream);
		stream.close();
		return altin;
	}
	
}

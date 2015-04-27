package com.archangel.project.converters;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import android.util.Log;

public class GoogleCurrencyConverter {	
	
	private static String googleCurrencyInfo = "http://quote.yahoo.com/d/quotes.csv?s=";
	
	public double convert(String currencyFrom, String currencyTo) throws Exception{
		
		String url = googleCurrencyInfo + currencyFrom + currencyTo + "=X&f=l1&e=.csv";
		Log.i("CurrencyConverterURL", url);
		InputStream source = retrieveStream(url);
		BufferedReader reader = new BufferedReader(new InputStreamReader(source, "UTF-8"));
		StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line + "\n");
        }
		String conversionResult = sb.toString();
		reader.close();
		source.close();

		
		Log.i("CurrencyConverter", conversionResult);
		return Double.parseDouble(conversionResult);
	}
	
	private InputStream retrieveStream(String url) {
		DefaultHttpClient client = new DefaultHttpClient();

		HttpPost httpPost = new HttpPost(url);
		
		try {
			
			HttpResponse getResponse = client.execute(httpPost);
			
			final int statusCode = getResponse.getStatusLine().getStatusCode();
			
			if (statusCode != HttpStatus.SC_OK) {
				Log.w(getClass().getSimpleName(),  "Error " + statusCode + " for URL " + url);
				return null;
			}
			
			HttpEntity getResponseEntity = getResponse.getEntity();
			return getResponseEntity.getContent();

		} catch (IOException e) {

			Log.w(getClass().getSimpleName(), "Error for URL " + url, e);
		}
		
		return null;
	}
	
}


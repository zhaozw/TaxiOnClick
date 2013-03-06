/*
 * JSONParser.java
 *
 * Created on December 3, 2012
 */

/**
 * @author  Felipe Calderon Barragan
 * @since  1.0
 * @company WideTech S.A
 */

package com.widetech.mobile.mitaxiapp.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import android.util.Log;

/**
 * Clase para crear peticiones HTTP POST o GET
 * */
public class RequestServer {

	static InputStream is = null; // El stream o flujo que se recibe del
									// servidor
	static String dataRequest = "";

	static int statusCodeRequest = 0;

	public RequestServer() {

	}

	// function get json from url
	// by making HTTP POST or GET mehtod
	public static String makeHttpRequest(String url, String method,
			List<NameValuePair> params) {

		// Making HTTP request
		try {

			// check for request method
			if (method == "POST") {
				// request method is POST
				// defaultHttpClient
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(params));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				System.out.println("code: "
						+ httpResponse.getStatusLine().getStatusCode());
				statusCodeRequest = httpResponse.getStatusLine()
						.getStatusCode();
				if (httpResponse.getStatusLine().getStatusCode() != 200) {
					dataRequest = null;
					return dataRequest;
				} else
					is = httpEntity.getContent();

			} else if (method == "GET") {
				// request method is GET
				DefaultHttpClient httpClient = new DefaultHttpClient();
				String paramString = URLEncodedUtils.format(params, "utf-8");
				url += "?" + paramString;
				System.out.println("la url de el request: " + url);
				HttpGet httpGet = new HttpGet(url);

				HttpResponse httpResponse = httpClient.execute(httpGet);
				HttpEntity httpEntity = httpResponse.getEntity();
				is = httpEntity.getContent();
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			// BufferedReader reader = new BufferedReader(new InputStreamReader(
			// is, "iso-8859-1"), 8);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			dataRequest = sb.toString();
		} catch (Exception e) {
			Log.e("Buffer Error detail",
					"Error converting result to Request String" + e.toString());

			dataRequest = null;
		}

		// Return JSON String
		return dataRequest;
	}

	public int getstatusCodeResponse() {
		return statusCodeRequest;
	}

}

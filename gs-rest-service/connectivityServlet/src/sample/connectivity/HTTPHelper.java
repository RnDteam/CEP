/*
 * Licensed Materials - Property of IBM
 * 5725-B69 
 * Copyright IBM Corp. 1987, 2015. All Rights Reserved.
 *
 * U.S. Government Users Restricted Rights: 
 * Use, duplication or disclosure restricted by GSA ADP Schedule 
 * Contract with IBM Corp.
 */
package sample.connectivity;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sample.utility.Parameters;
import sample.utility.StringHelper;

/** 
 * 
 * Helper class for the HTTP connection.
 *
 */
public class HTTPHelper {
    private static String httpResponse = null;
	
 	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// A request occurs on an HTTP output: the current request is stored in httpResponse.
		StringHelper.trace(this.getClass().getName() + ".doPost",
				"Post received");
		httpResponse = StringHelper.getBody(request.getInputStream());
		response.setStatus(HttpServletResponse.SC_OK);
	 }

	public void doGet(Parameters parameters, HttpServletResponse response)  throws ServletException, IOException {
		// post the event using HTTP
		postHTTPMessage(parameters);
		// wait for the response to be posted to the servlet
		// shows it when it occurs.
		synchronized (this) {
		  while (httpResponse == null) {
			try {
				wait(1000);
			} catch (Exception e) {
			  e.printStackTrace();
			}
		  }
		}
		StringHelper.displayResponse(response, httpResponse);
	}  
	
	protected boolean postHTTPMessage(Parameters parameters) {
		// Creates an HTTP connection, writes an event inside.
		boolean success;
        httpResponse = null;
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(
					                        getInputURL()).openConnection();
			try {
				connection.setRequestProperty("Content-Type", "application/xml");
				connection.setDoInput(true);
				connection.setDoOutput(true);
				String xmlEvent = StringHelper.createEventFromXML(parameters);
				InputStream input = new ByteArrayInputStream(
						xmlEvent.getBytes("UTF-8"));
				try {
					OutputStream requestStream = connection.getOutputStream();
					byte buffer[] = new byte[128];
					while (true) {
						int length = input.read(buffer);
						if (length < 0) {
							break;
						}
						requestStream.write(buffer, 0, length);
					}
					requestStream.close();
				} finally {
					input.close();
				}

				int responseCode = connection.getResponseCode();
				StringHelper.trace(this.getClass().getName() + ".sendHTTPMessage",
						"Response code: " + responseCode);
				success = (responseCode == 200);
			} finally {
				connection.disconnect();
			}
		} catch (IOException e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}

	protected String getInputURL() {
		return "http://localhost:9080/connectivity/Transaction";
	}

}

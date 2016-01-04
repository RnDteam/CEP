import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/** 
 * 
 * Helper class for the HTTP connection.
 *
 */
public class HTTPHelper {
	
    public void sendHttpGet(Parameters parameters) {
    	try {
    		System.out.println("GET");
			HttpURLConnection connection = (HttpURLConnection) new URL(getOutputUrl(parameters)).openConnection();
			String httpResponse = StringHelper.getBody(connection.getInputStream());
			StringHelper.trace(this.getClass().getName() + ".sendHttpGet", httpResponse);
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	public boolean postHTTPMessage(Parameters parameters) {
		// Creates an HTTP connection, writes an event inside.
		boolean success;
		
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
				System.out.println("POST");
				StringHelper.trace(this.getClass().getName() + ".sendHTTPMessage",
						"Response code: " + responseCode);
				success = (responseCode == 200);

				// TODO test
				/*
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

				while (in.readLine() != null) {
				}
				
				String responseMessage = connection.getResponseMessage();
				System.out.println(responseMessage);
				
				in.close();
				*/
				// TODO end of test
				
			} finally {
				connection.disconnect();
			}
		} catch (IOException e) {
			e.printStackTrace();
			success = false;
		}
		return success;
	}

	public static String getInputURL() {
		return "http://localhost:9080/connectivity/Transaction";
	}
	
	public static String getOutputUrl(Parameters parameters) {
		//return "http://localhost:9080/eventSender/response";
		return "http://localhost:9080/eventSender/response?accountId=" + parameters.getAccountId() +
				"&country=" + parameters.getCountry() +
				"&amount=" + parameters.getAmount() +
				"&merchantId=" + parameters.getMerchantId() +
				"&merchantType=" + parameters.getMerchantType() +
				"&merchantLocation=" + parameters.getMerchantLocation() +
				"&event-method=" + parameters.getEventMethod();
	}
}
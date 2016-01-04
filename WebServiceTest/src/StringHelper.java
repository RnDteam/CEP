import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/** 
 * 
 * Utility class for all string and stream use.
 *
 */
public class StringHelper {

	public static  void trace(String method, String message) {
		String timeStamp = new SimpleDateFormat("yyyy MM dd HHmmss")
				.format(Calendar.getInstance().getTime());
		String output = timeStamp + " " + method + " " + message;
		System.out.println(output);
	}

    public static String getBody(InputStream inputStream) throws IOException {

	String body = null;
	StringBuilder stringBuilder = new StringBuilder();
	BufferedReader bufferedReader = null;

	try {
	    if (inputStream != null) {
		bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
		char[] charBuffer = new char[128];
		int bytesRead = -1;
		while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
		    stringBuilder.append(charBuffer, 0, bytesRead);
		}
	    } else {
		stringBuilder.append("");
	    }
	} catch (IOException ex) {
	    throw ex;
	} finally {
	    if (bufferedReader != null) {
		try {
		    bufferedReader.close();
		} catch (IOException ex) {
		    throw ex;
		}
	    }
	}

	body = stringBuilder.toString();
	return body;
    }
    
    /*
	public static void displayResponse(HttpServletResponse response, String message) {
		try {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();

			out.println("<html>");
			out.println("<head>");
			out.println("<title>Connectivity sample response</title>");
			out.println("</head>");
			out.println("<body bgcolor=\"white\">");
			out.println("<p>" + message + "</p>");
			out.println("<a href=\"/eventSender/\">Back To event Sender</a>");
			out.println("</body>");
			out.println("</html>");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	*/

	public static String createEventFromXML(Parameters parameters) {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<event:Transaction")
				.append(" xmlns:event=\"http://www.ibm.com/ia/xmlns/default/ConnectivitySolutionBOM/model\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.ibm.com/ia/xmlns/default/ConnectivitySolutionBOM/model model.xsd \">");
		sb.append(" <amount>" + parameters.getAmount() + "</amount>");
		sb.append(" <countryCode>" + parameters.getCountry()
				+ "</countryCode>");
		sb.append(" <account>" + parameters.getAccountId() + "</account>");
		sb.append(" <merchantId>" + parameters.getMerchantId() + "</merchantId>");
		sb.append(" <merchantType>" + parameters.getMerchantType() + "</merchantType>");
		sb.append(" <location>" + parameters.getMerchantLocation() + "</location>");
		sb.append("</event:Transaction>");
		String result = sb.toString();
		trace("createTransactionEventFromXML", result);
		return result;
	}
	
	public static String createEventWithNoTransformationFromXML(Parameters parameters) {
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		sb.append("<m:")
				.append(parameters.getEventName())
				.append(" xmlns:m=\"http://www.ibm.com/ia/xmlns/default/ConnectivitySolutionBOM/model\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"http://www.ibm.com/ia/xmlns/default/ConnectivitySolutionBOM/model model.xsd \">");
		sb.append(" <m:amount>" + parameters.getAmount() + "</m:amount>");
		sb.append(" <m:countryCode>" + parameters.getCountry()
				+ "</m:countryCode>");
		sb.append(" <m:account>" + parameters.getAccountId() + "</m:account>");
		sb.append(" <merchant>");
		sb.append(" <m:id>" + parameters.getMerchantId() + "</m:id>");
		sb.append(" <m:type>" + parameters.getMerchantType() + "</m:type>");
		sb.append(" <m:location>" + parameters.getMerchantLocation() + "</m:location>");
		sb.append( "</merchant>");
		sb.append("</m:" + parameters.getEventName() + ">");
		String result = sb.toString();
		trace("createTransactionEventFromXML", result);
		return result;
	}

}
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Map;


public class Main {
	
	private static HTTPHelper httpHelper = null;
	
	private static String httpResponse = null;
	
	public static void main(String[] args) {
		
		httpHelper = new HTTPHelper();
		System.out.println("Sending ...");
		sendMessage();
	}
	
	private static void connectToDB() {
		
		String databaseUrl = "jdbc:mysql://localhost:3306/cep_try";
		String user = "root";
		String password = "Passw0rd";
		
		Connection connection = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("mysql jdbc driver is missing");
		}
		
		try {
			connection = DriverManager.getConnection(databaseUrl, user, password);
			
			if	(connection != null) {
				
				Statement statement = connection.createStatement();
				ResultSet rs = statement.executeQuery("select * from vehicles");
				
				while (rs.next()) {
					System.out.println(rs.toString());
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void sendMessage() {
		//Parameters parameters = new Parameters();
		//parameters.generateAll();
		
		Parameters p1 = new Parameters();
		Parameters p2 = new Parameters();
		
		p1.setEventName("Transaction");
		p1.setAccountId("accountId1");
		p1.setCountry("FR");
		p1.setAmount("2");
		p1.setMerchantId("Super");
		p1.setMerchantLocation("Valbonne");
		p1.setMerchantType("Store");
		p1.setEventMethod("http");
		
		p2.setEventName("Transaction");
		p2.setAccountId("accountId2");
		p2.setCountry("IL");
		p2.setAmount("10000");
		p2.setMerchantId("Super");
		p2.setMerchantLocation("Ashdod");
		p2.setMerchantType("Store");
		p2.setEventMethod("http");
		
		Parameters[] params = new Parameters[] { p1, p2 };
		
		for (int i = 0; i < params.length; i++) {
					
			httpHelper.postHTTPMessage(params[i]);
			httpHelper.sendHttpGet(params[i]);
		}
		
		//boolean status = httpHelper.postHTTPMessage(parameters);
		//httpHelper.sendHttpGet(parameters);
		
		/*
		if (status) {
			try {
				httpHelper.sendGet();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		*/
		
	}
	
	
	private static void test() {
		 try {
			 
	            URL obj = new URL("http://localhost:9080/eventSender/response");
	            URLConnection conn = obj.openConnection();
	            Map<String, List<String>> map = conn.getHeaderFields();
	 
	            System.out.println("Printing All Response Header for URL: "
	                    + obj.toString() + "\n");
	 
	            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
	                System.out.println(entry.getKey() + " : " + entry.getValue());
	            }
	 
	            System.out.println("\nGet Response Header By Key ...\n");
	            List<String> contentLength = map.get("Content-Length");
	 
	            if (contentLength == null) {
	                System.out
	                .println("'Content-Length' doesn't present in Header!");
	            } else {
	                for (String header : contentLength) {
	                    System.out.println("Content-Lenght: " + header);
	                }
	            }
	 
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	}
	
}

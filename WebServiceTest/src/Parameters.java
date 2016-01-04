import java.util.Random;

/** 
 * 
 * Utility class to decode and store the request parameters.
 *
 */
public class Parameters {
	private static final String TRANSACTION_ACCOUNTID = "accountId";
	private static final String TRANSACTION_COUNTRY = "country";
	private static final String TRANSACTION_AMOUNT = "amount";
	private static final String TRANSACTION_MERCHANT_ID = "merchantId";
	private static final String TRANSACTION_MERCHANT_TYPE = "merchantType";
	private static final String TRANSACTION_MERCHANT_LOCATION = "merchantLocation";

	private static final String EVENT_METHOD = "event-method";
	private static final String EVENT_HTTP_METHOD = "http";
	private static final String EVENT_JMS_METHOD = "jms";

	private String eventName;
	private String accountId;
	private String country;
	private String amount;
	private String merchantId;
	private String merchantType;
	private String merchantLocation;
	private String eventMethod;
	
	// getters
	public String getEventName() {
		return this.eventName;
	}
	
	public String getAccountId() {
		return this.accountId;
	}
	
	public String getCountry() {
		return this.country;
	}
	
	public String getAmount() {
		return this.amount;
	}

	public String getMerchantId() {
		return this.merchantId;
	}
	public String getMerchantType() {
		return this.merchantType;
	}
	public String getMerchantLocation() {
		return this.merchantLocation;
	}
	
	public String getEventMethod() {
		return this.eventMethod;
	}
	
	// setters
	public void setEventName(String str) {
		this.eventName = str;
	}
	
	public void setAccountId(String str) {
		this.accountId = str;
	}
	
	public void setCountry(String str) {
		this.country = str;
	}
	
	public void setAmount(String str) {
		this.amount = str;
	}

	public void setMerchantId(String str) {
		this.merchantId = str;
	}
	public void setMerchantType(String str) {
		this.merchantType = str;
	}
	public void setMerchantLocation(String str) {
		this.merchantLocation = str;
	}
	
	public void setEventMethod(String str) {
		this.eventMethod = str;
	}
	
	public void generateAll() {
		
		Random random = new Random();
		
		this.eventName = "Transaction";
		this.accountId = "accountId1";
		this.country = "FR";
		this.amount = "2";
		this.merchantId = "Super";
		this.merchantType = "Store";
		this.merchantLocation = "Valbonne";
		this.eventMethod = "http";
	}
	
	//http://localhost:9080/eventSender/response?accountId=accountId1&country=FR&amount=10000&merchantId=Super&merchantType=Store&merchantLocation=Valbonne&event-method=http
	
	/*
	public void decode(HttpServletRequest request) {
		eventName = "Transaction";
		accountId = request.getParameter(TRANSACTION_ACCOUNTID);
		country = request.getParameter(TRANSACTION_COUNTRY);
		amount = request.getParameter(TRANSACTION_AMOUNT);
		merchantId = request.getParameter(TRANSACTION_MERCHANT_ID);
		merchantType = request.getParameter(TRANSACTION_MERCHANT_TYPE);
		merchantLocation = request.getParameter(TRANSACTION_MERCHANT_LOCATION);
		eventMethod = request.getParameter(EVENT_METHOD);
	}
	*/
	
	public boolean isJMS() {
		return (eventMethod != null && eventMethod.compareTo(EVENT_JMS_METHOD) == 0);
	}
	
	public boolean isHTTP() {
		return  (eventMethod != null && eventMethod.compareTo(EVENT_HTTP_METHOD) == 0);
	}

	public String display() {
		String message = "";
		message = message.concat("Account: " + accountId);
		message = message.concat(" Country: " + country);
		message = message.concat(" Amount: " + amount);
		return message;
	}
}
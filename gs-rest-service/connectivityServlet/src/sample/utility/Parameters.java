/*
* Licensed Materials - Property of IBM
* 5725-B69 
* Copyright IBM Corp. 1987, 2015. All Rights Reserved.
*
* U.S. Government Users Restricted Rights: 
* Use, duplication or disclosure restricted by GSA ADP Schedule 
* Contract with IBM Corp.
*/
package sample.utility;

import javax.servlet.http.HttpServletRequest;

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
	
	public String getEventName() {
		return eventName;
	}
	
	public String getAccountId() {
		return accountId;
	}
	
	public String getCountry() {
		return country;
	}
	
	public String getAmount() {
		return amount;
	}

	public String getMerchantId() {
		return merchantId;
	}
	public String getMerchantType() {
		return merchantType;
	}
	public String getMerchantLocation() {
		return merchantLocation;
	}

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

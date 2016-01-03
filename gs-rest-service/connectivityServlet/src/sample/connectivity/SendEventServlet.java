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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import sample.utility.Parameters;
import sample.utility.StringHelper;

/** 
 * 
 * Servlet class redirects the request to JMSHelper and/or HttpHelper
 * It is declared in the web application descriptor web.xml.
 *
 */
public class SendEventServlet extends HttpServlet {

	private static final long serialVersionUID = 992983165817176086L;
	private HTTPHelper httpHelper = null;
	private JMSHelper jmsHelper = null;
	
   
    public SendEventServlet() {
    	httpHelper = new HTTPHelper();
    	jmsHelper = new JMSHelper();	
    }
    
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		httpHelper.doPost(request, response);
	 }
		   

	@Override
	protected void doGet(HttpServletRequest request,
		                 HttpServletResponse response) throws ServletException, IOException {
		Parameters parameters = new Parameters();
		parameters.decode(request);
		if (parameters.isJMS()) {
			jmsHelper.doGet(parameters, response);
		} else if (parameters.isHTTP()) {
			httpHelper.doGet(parameters, response);
		} else {
			StringHelper.trace(this.getClass().getName() + ".execute",
					"ERROR Unknown protocol");
		}
	}


	




}



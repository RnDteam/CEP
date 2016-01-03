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

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletResponse;

import sample.utility.Parameters;
import sample.utility.StringHelper;

/** 
 * 
 * Helper class for the JMS connection.
 *
 */
public class JMSHelper {
	private InitialContext initialContext;
	private Connection connection = null;
	private MessageProducer producer = null;
	private MessageConsumer consumer = null;
	private Session session = null;
	private String postJNDIQueueName = "jms/queue/JMSTransactionEventQueue";
	private String receivedJNDIQueueName = "jms/queue/JMSAuthorizationResponseOutputQueue";
 

	public void doGet(Parameters parameters, HttpServletResponse httpResponse) {
		try {
			StringHelper.trace(this.getClass().getName() + ".sendJMSMessage",
					parameters.display());
			Message responseMessage;
			try {
				initJMSSession();
				sendJMSEvent(parameters);
				responseMessage = receiveJMSEvent();
			} finally {
				terminateJMSSession();
			}
			StringHelper.displayResponse(httpResponse, messageText(responseMessage));
		} catch (JMSException e) {
			e.printStackTrace();
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}


	private void initJMSSession() throws JMSException, NamingException {
		// initializes all connection, queues and Message Producer and Message Consumers
		// Starts the connection
		try {
			initialContext = new InitialContext();
			ConnectionFactory cf = (ConnectionFactory) initialContext
					.lookup("jms/queue/JMSAuthorizationResponseEndpointConnectionFactory");

			connection = cf.createConnection();

			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			Queue postQueue = (Queue) initialContext.lookup(postJNDIQueueName);
			producer = session.createProducer(postQueue);
			Queue srcQueue = (Queue) initialContext
					.lookup(receivedJNDIQueueName);
			consumer = session.createConsumer(srcQueue);

			connection.start();
		} catch (Exception e) {
			throw new RuntimeException("Cannot initialize JMS session ", e);
		}

	}

	private void sendJMSEvent(Parameters parameters)
			throws JMSException {
		// creates the message and send it.
		try {
			TextMessage message = session.createTextMessage();
			String xmlEvent = StringHelper.createEventFromXML(parameters);
			message.setText(new String(xmlEvent.getBytes(), "UTF-8"));
			producer.send(message);
		} catch (Exception e) {
			throw new RuntimeException(
					"Cannot submit event through JMS to queue "
							+ postJNDIQueueName, e);
		} finally {
			if (producer != null)
				producer.close();
		}

	}

	private Message receiveJMSEvent() throws JMSException {
		Message response = null;
		Message responseEnd = null;
		try {
			do {
			      response = responseEnd;
			      responseEnd = consumer.receive(30000);
			} while (responseEnd != null);
			
		} catch (Exception e) {
			throw new RuntimeException(
					"Cannot receive event through JMS to queue "
							+ receivedJNDIQueueName, e);
		} finally {
			if (consumer != null)
				consumer.close();
		}
		if (response == null) {
			throw new RuntimeException("No message received at JMS queue "
					+ receivedJNDIQueueName + " within given timeout (" + 30000
					+ " ms)");
		}
		return response;
	}

	private void terminateJMSSession() throws JMSException, NamingException {
		if (initialContext != null)
			initialContext.close();
		if (connection != null) {
			connection.close();
		}
		if (session != null)
			session.close();

	}

	private String messageText(Message message) throws JMSException {
		String result;
		if (message instanceof TextMessage) {
			result = ((TextMessage) message).getText();
		} else {
			throw new RuntimeException(
					"Non text message recieved by JMS endpoint.");
		}
		return result;
	}


}

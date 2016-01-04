/*
* Licensed Materials - Property of IBM
* 5725-B69 
* Copyright IBM Corp. 1987, 2015. All Rights Reserved.
*
* U.S. Government Users Restricted Rights: 
* Use, duplication or disclosure restricted by GSA ADP Schedule 
* Contract with IBM Corp.
*/

package test;

import static org.junit.Assert.fail;
import org.junit.Assert;

import org.junit.Before;
import org.junit.Test;
import java.time.ZonedDateTime;

import com.ibm.ia.common.RoutingStatus;
import com.ibm.ia.common.debug.DebugInfo;
import com.ibm.ia.model.Event;
import com.ibm.ia.model.Relationship;
import com.ibm.ia.testdriver.DebugReceiver;
import com.ibm.ia.testdriver.TestDriver;

import creditcard.Account;
import creditcard.AccountInitialization;
import creditcard.AuthorizationResponse;
import creditcard.ConceptFactory;
import creditcard.Customer;
import creditcard.Transaction;

public class CreditCardTest {
	private TestDriver driver;
	private MyReceiver receiver;
	
	@Before
	public void initTestDriver() {
		
		try {
			driver = new TestDriver();
			driver.connect();
			receiver = new MyReceiver(driver);
			driver.addDebugReceiver(receiver);
						
		} catch (Exception exc) {
			exc.printStackTrace();
			fail("Exception connecting to test driver: " + exc.getMessage());
		}
	}
	
	@Test
	public void testCreateAccount() throws Exception {
		driver.deleteAllEntities();
		// test account creation: call BMI instructions
		emitInitializeAccountAndCheck("accountId1", "customer1");
	}

	private void emitInitializeAccountAndCheck(String accountId,
			String customerId) throws Exception {
		// build an event
		ConceptFactory factory = driver.getConceptFactory(ConceptFactory.class);
		AccountInitialization initializeAccountEvent = factory
				.createAccountInitialization(ZonedDateTime.now());
		Relationship<Account> accountRelationship = driver.getEventFactory()
				.createRelationship(Account.class, accountId);
		initializeAccountEvent.setAccount(accountRelationship);
		initializeAccountEvent.setBalance(100.0);
		Relationship<Customer> customerRelationship = driver.getEventFactory()
				.createRelationship(Customer.class, customerId);
		initializeAccountEvent.setCustomer(customerRelationship);

		// submit an event
		RoutingStatus status = driver.getSolutionGateway().submit(initializeAccountEvent);
		Assert.assertEquals(RoutingStatus.STATUS_OK, status);
		// Wait for the account with id accountId to have 100 as balance.
		if (driver.waitUntilSolutionIdle(30) == true) {
			// check the entity is created
			Account account = driver.fetchEntity(Account.class, accountId);
			Assert.assertNotNull(account);
			Assert.assertEquals(accountId, account.getId());
			// check the balance value
			Assert.assertEquals(100.0, account.getBalance(), 0);
		}
	}

	@Test 
	public void testUpdateAccount() throws Exception {
		emitTransactionAndCheck("accountId1", "US", 50,  0, null);
	}
	
	@Test 
	public void testLargeTransaction() throws Exception {
		emitTransactionAndCheck("accountId1", "US", 6000,  2, "R04");
	}

	private void emitTransactionAndCheck(String accountId, String country,
			double amount, int count, String code) throws Exception {
		// Start the recording- this can be done using the REST API
		// http://localhost:9080/ibm/insights/rest/recording/start/MyCreditCardSolution
		driver.startRecording();
		try {
			// build an event
			ConceptFactory factory = driver.getConceptFactory(ConceptFactory.class);
			Transaction transaction = factory.createTransaction(ZonedDateTime.now());
			transaction.setCountryCode(country);
			transaction.setAmount(amount);
			Relationship<Account> accountRelationship = driver
					.getEventFactory().createRelationship(Account.class,
							accountId);
			transaction.setAccount(accountRelationship);
			// get the account
			boolean accountBalanceKnown = false;
			double accountBalance = 0;
			Account account = driver.fetchEntity(Account.class, accountId);
			if (account != null) {
				accountBalanceKnown = true;
				accountBalance = account.getBalance();
			}
			// submit an event
			RoutingStatus status = driver.getSolutionGateway().submit(
					transaction);

			Assert.assertEquals(RoutingStatus.STATUS_OK, status);

			if ( accountBalanceKnown ) {
				// Wait for the account with id accountId to have its balance decremented of amount.
				if (driver.waitUntilSolutionIdle(30) == true) {
				// check the entity is created
				account = driver.fetchEntity(Account.class, accountId);
				Assert.assertNotNull(account);
				Assert.assertEquals(accountId, account.getId());
				// check the balance value
				Assert.assertEquals(accountBalance - amount, account.getBalance(), 0);
				}
			}

			if ( code!=null ) {
				if (driver.waitUntilSolutionIdle(30) == true) {
				// Wait to receive an AuthorizationResponse event with provided code.
				Assert.assertEquals(count, receiver.getAuthorizationCount());
				creditcard.Exception exc = receiver.getException();
				System.out.println("code " + exc.getCode());
				Assert.assertEquals(code, exc.getCode());
				}
			}

		} finally {
			//
			driver.stopRecording();
		}
	}
	
	private class MyReceiver implements DebugReceiver
	{
		// Receiver class to get the events and check they are the expected ones.
		private int eventCount = 0;
		private creditcard.Exception exception;
		private TestDriver _driver;

		public MyReceiver(TestDriver driver)
		{
			_driver = driver;
		}

		private int getAuthorizationCount() {
			return eventCount;
		}
		
		private creditcard.Exception getException() {
			return exception;
		}
		
			
		@Override
		public void addDebugInfo(DebugInfo debugInfo, String sourceAgent) 
		{
			System.out.println("Debug info received: " + sourceAgent + ", " + debugInfo.getAgentName() + ", " + debugInfo.getDebugNote() + ", " + debugInfo.getEventId() + ", " + debugInfo.getSolutionName());
			Event event = _driver.getAgentEvent(debugInfo);
			System.out.println("Event: " + event);
			if (event instanceof AuthorizationResponse) {
				AuthorizationResponse response = (AuthorizationResponse) event;
				exception = response.getException();
				eventCount++;
			}
			else 
				System.out.println("Invalid event type");
		}


	}

	
}

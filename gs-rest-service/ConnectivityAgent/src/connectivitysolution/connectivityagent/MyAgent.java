package connectivitysolution.connectivityagent;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.ibm.ia.common.AgentException;
import com.ibm.ia.agent.EntityAgent;
import com.ibm.ia.model.Event;
import com.ibm.ia.model.Relationship;

import creditcard.Account;
import creditcard.Customer;
import creditcard.Transaction;

public class MyAgent extends EntityAgent<Account> {
   
	@Override
	public void process(Event event) throws AgentException {

		if (event instanceof Transaction) {
			Account account = getBoundEntity();

			if (account == null) {
				Transaction transaction = (Transaction) event;

				// the account id from the transaction event. Note that another
				// option to get the id is
				// to call createBoundEntity and then use the id field which
				// will be set
				String accountId = transaction.getAccount().getKey();

				printToLog("Creating new account with id: " + accountId);

				// first create the account entity object. All the fields will
				// initially be empty, except
				// for the id
				account = (Account) createBoundEntity();

				// update the account entity from a data store
				initializeAccountFromStore(accountId, account);

				// save the account entity
				updateBoundEntity(account);
			}
		}
	}

	/**
	 * Initialize an account entity from a store. The store would normally be a
	 * database, but for this sample the account is initialized from a static
	 * table.
	 * 
	 * @param - the account to initialize
	 * 
	 */
	private void initializeAccountFromStore(String accountId, Account account) {

		String customerId = accountMap.get(accountId);
		Relationship<Customer> customerRelationship = createRelationship(Customer.class, customerId);
   		account.setCustomer(customerRelationship);
 		// normally the balance would come from a data store
 		account.setBalance(100.0);
 	}

	private static final Map<String, String> accountMap;
 	static {
 		Map<String, String> map = new HashMap<String, String>();
 		map.put("accountId1", "customerId1");
 		map.put("accountId2", "customerId2");
 		map.put("accountId3", "customerId3");
 		accountMap = Collections.unmodifiableMap(map);
 	}
 }
package mycreditcardsolution.creditcardjavaagent;

import com.ibm.ia.agent.EntityAgent;
import com.ibm.ia.common.AgentException;
import com.ibm.ia.model.Event;

import creditcard.Account;
import creditcard.Transaction;

public class MyAgent extends EntityAgent<Account> {
   @Override
   public void process(Event event) throws AgentException {

      if (event instanceof Transaction) {
         Account account = getBoundEntity();
         String accountId = account.getId();
         String url = getSolutionProperty("getting_started_external_service_url");
         printToLog("Invoking external service at url: " + url
            + " for account with id: " + accountId);
      }
   }
}
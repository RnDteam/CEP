package InitServer;

import com.ibm.ia.common.EventCapacityExceededException;
import com.ibm.ia.common.GatewayException;
import com.ibm.ia.common.RoutingException;
import com.ibm.ia.gateway.SolutionGateway;
import com.ibm.ia.model.Event;

public class SubmitEvent implements Runnable{

	private SolutionGateway gateway;
	private Event eventToRun;
	
	public SubmitEvent(SolutionGateway gateway, Event eventToRun) {
		this.gateway = gateway;
		this.eventToRun = eventToRun;
		
	}
	@Override
	public void run() {
		try {
			gateway.submit(eventToRun);
			Thread.sleep(100);
		} catch(EventCapacityExceededException e) {
			System.out.println("********************Too much********************************");
			e.printStackTrace();
		}
		catch (GatewayException e) {
			e.printStackTrace();
		} catch (RoutingException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

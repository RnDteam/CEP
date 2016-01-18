package blur.testjavaagent;

import java.time.ZonedDateTime;

import blur.model.Alert;
import blur.model.ConceptFactory;
import blur.model.TrafficCameraReport;
import blur.model.Vehicle;

import com.ibm.ia.agent.EntityAgent;
import com.ibm.ia.common.AgentException;
import com.ibm.ia.model.Event;

public class TestJavaAgent extends EntityAgent<Vehicle> {

	@Override
	public void process(Event event) throws AgentException {

		Vehicle v = getBoundEntity();

		if (v != null) {

			if (event instanceof TrafficCameraReport) {
				ZonedDateTime now = ZonedDateTime.now();
				Alert alert = getConceptFactory(ConceptFactory.class)
						.createAlert(now);
				alert.setMessage("Vehicle " + v.get$Id() + " received a traffic report.");
				alert.setRule(this.getClass().getName());
				alert.setVehicle(createRelationship(v));
				emit(alert);
				schedule(now.plusMinutes(10),
						"Vehicle " + v.get$Id() + " received traffic report 10 minutes ago.");
				printToLog("JavaAgent for " + v.get$Id()
						+ " emitted alert and scheduled callback.");
			}

			// updateBoundEntity(v);
		} else {
			System.out.println( "Bound entity for JavaAgent is null." );
			// v = createBoundEntity();
		}
	}

	@Override
	public void process(String key, String cookie) throws AgentException {

		Vehicle v = getBoundEntity();

		if (v != null) {
			printToLog("JavaAgent for " + v.get$Id() + " received callback.");

			ZonedDateTime now = ZonedDateTime.now();
			Alert alert = getConceptFactory(ConceptFactory.class).createAlert(
					now);
			alert.setMessage(cookie);
			alert.setRule(this.getClass().getName());
			alert.setVehicle(createRelationship(v));
			emit(alert);
		}
		else {
			System.out.println( "Bound entity for JavaAgent is null." );
		}
	}
}
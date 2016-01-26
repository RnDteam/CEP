package EventsHandler.Events;

import java.util.Set;

import com.ibm.ia.gateway.SolutionGateway;
import com.ibm.ia.model.Relationship;

import blur.model.LocationBasedEvent;
import blur.model.Person;
import EventsHandler.EventCreation;

public abstract class AbsLocationBasedEvent<T> extends EventCreation<LocationBasedEvent> {

	public Set<Relationship<Person>> getAllPersons(SolutionGateway gateway) {
		
		
		return null;
	}
}

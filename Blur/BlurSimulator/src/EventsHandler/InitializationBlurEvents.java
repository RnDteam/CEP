package EventsHandler;

import EventsHandler.EntitiesInitialization.BuildingExternalInit;
import EventsHandler.EntitiesInitialization.BuildingInternalInit;
import EventsHandler.EntitiesInitialization.OrganizationInit;
import EventsHandler.EntitiesInitialization.OrganizationRoleInit;
import EventsHandler.EntitiesInitialization.PersonExternalInit;
import EventsHandler.EntitiesInitialization.PersonInternalInit;

import com.ibm.ia.model.Event;

public enum InitializationBlurEvents {
	Organization(new OrganizationInit()),
	OrganizationRole(new OrganizationRoleInit()),
	PersonExternal(new PersonExternalInit()),
	PersonInternal(new PersonInternalInit()),
	BuildingExternal(new BuildingExternalInit()),
	BuildingInternal(new BuildingInternalInit());
	
	private EventCreation<Event> event;

	private InitializationBlurEvents(EventCreation newEvent) {
		this.event = newEvent;
	}
	
	public EventCreation<Event> getEventCreation() {
		return this.event;
	}
}

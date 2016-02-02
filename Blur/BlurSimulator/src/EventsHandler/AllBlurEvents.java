package EventsHandler;

import com.ibm.ia.model.Event;
import EventsHandler.EntitiesInitialization.*;
import EventsHandler.Events.*;

public enum AllBlurEvents {
//	Organization(new OrganizationInit()),
//	OrganizationRole(new OrganizationRoleInit()),
//	PersonExternal(new PersonExternalInit()),
	PersonInternal(new PersonInternalInit());
//	BuildingExternal(new BuildingExternalInit()),
//	BuildingInternal(new BuildingInternalInit()),
//	
//	TrafficCameraReport(new TrafficCameraReportEvent());
//	CellularCallReport(new CellularCallReportEvent());
//	CellularReport(new CellularReportEvent());
	
	private EventCreation<Event> event;

	private AllBlurEvents(EventCreation newEvent) {
		this.event = newEvent;
	}
	
	public EventCreation<Event> getEventCreation() {
		return this.event;
	}
}

package EventsHandler;

import EventsHandler.Events.CellularCallReportEvent;
import EventsHandler.Events.CellularReportEvent;
import EventsHandler.Events.TrafficCameraReportEvent;

import com.ibm.ia.model.Event;

public enum SimulationBlurEvents {
	TrafficCameraReport(new TrafficCameraReportEvent()),
	CellularCallReport(new CellularCallReportEvent()),
	CellularReport(new CellularReportEvent());

	private EventCreation<Event> event;

	private SimulationBlurEvents(EventCreation newEvent) {
		this.event = newEvent;
	}

	public EventCreation<Event> getEventCreation() {
		return this.event;
	}
}
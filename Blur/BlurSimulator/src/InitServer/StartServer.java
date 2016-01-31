package InitServer;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import DBHandler.ConverterUtility;
import DBHandler.DBReader;
import EventsHandler.AllBlurEvents;
import EventsHandler.EntitiesInitialization.BuildingExternalInit;
import EventsHandler.EntitiesInitialization.BuildingInternalInit;
import EventsHandler.EntitiesInitialization.OrganizationInit;
import EventsHandler.EntitiesInitialization.OrganizationRoleInit;
import EventsHandler.EntitiesInitialization.PersonExternalInit;
import EventsHandler.EntitiesInitialization.PersonInternalInit;
import EventsHandler.Events.CellularReportEvent;
import EventsHandler.Events.TrafficCameraReportEvent;
import blur.model.BuildingInitialization;
import blur.model.BuildingUpdate;
import blur.model.CellularReport;
import blur.model.OrganizationInitialization;
import blur.model.OrganizationRoleInitialization;
import blur.model.PersonInitialization;
import blur.model.PersonUpdate;
import blur.model.TrafficCameraReport;

import com.ibm.ia.common.GatewayException;
import com.ibm.ia.common.RoutingException;
import com.ibm.ia.gateway.GridConfiguration;
import com.ibm.ia.gateway.GridConnection;
import com.ibm.ia.gateway.GridConnectionFactory;
import com.ibm.ia.gateway.SolutionChangedException;
import com.ibm.ia.gateway.SolutionGateway;
import com.ibm.ia.gateway.client.GatewayClient;
import com.ibm.ia.model.Event;
import com.ibm.ia.testdriver.TestDriver;


public class StartServer {

	public static void main(String[] args) {
		startServer();
		System.exit(0);
	}

	private static void startServer() {
		TestDriver testDriver = new  TestDriver();
		try {
			testDriver.connect();
			testDriver.deleteAllEntities();
			testDriver.resetSolutionState();
			testDriver.startRecording();
		} catch (GatewayException e1) {
			e1.printStackTrace();
		}
		
		GridConnection connection = null;
		try {
			GatewayClient.init();
			GridConfiguration gridConfig = new GridConfiguration("localhost:2809");
			
			connection = GridConnectionFactory.createGridConnection(gridConfig);
			SolutionGateway gateway = connection.getSolutionGateway("Blur");
			
//			PersonInitialization personInit1 = gateway.getEventFactory().createEvent(PersonInitialization.class);
//			personInit1.setPerson(gateway.createRelationship(Person.class, "123"));
//			personInit1.setName("NameTRY");
//			personInit1.setTimestamp(ZonedDateTime.now().minusDays(5));
//			gateway.submit(personInit1);
//			
//			Thread.sleep(1000L);
//			
//			PersonInitialization personInit2 = gateway.getEventFactory().createEvent(PersonInitialization.class);
//			personInit2.setPerson(gateway.createRelationship(Person.class, "123"));
//			personInit2.setProfession("ProffesionTry");
//			personInit2.setTimestamp(ZonedDateTime.now().minusDays(1));
//			gateway.submit(personInit2);
			
			getDataAndSendEvents(gateway);

		} catch (Exception e) {
			System.out.println("Failed!");
			e.printStackTrace();
		}
		finally {
			if (connection != null) {
				try {
					connection.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		testDriver.stopRecording();
		testDriver.disconnect();
		
		System.out.println("finished!");
	}

	public static void getDataAndSendEvents(SolutionGateway gateway)
			throws SolutionChangedException, GatewayException, RoutingException {
		HashMap<ZonedDateTime, List<Event>> eventsMap = new HashMap<>();
		
//		// Entities initialization events
//		addOrganizationInitialization(eventsMap, gateway);
//		addOrganizationRoleInitialization(eventsMap, gateway);
//		addPersonsInitialization(eventsMap, gateway);
//		addBuildingInitialization(eventsMap, gateway);
//		
//		// Events
//		addTrafficCamersReports(eventsMap, gateway);
//		addCellularReports(eventsMap, gateway);
		
		addAllEventsToMap(eventsMap, gateway);
		
		submitEventsAsync(eventsMap, gateway);
	}

	private static void addAllEventsToMap(
			HashMap<ZonedDateTime, List<Event>> eventsMap,
			SolutionGateway gateway) {
		for (AllBlurEvents event : AllBlurEvents.values()) {
			List<Event> allEventInstances = event.getEventCreation().getAllEntities(gateway);
			
			for (Event currEvent : allEventInstances) {
				ZonedDateTime timestamp = currEvent.get$Timestamp();
				
				if(eventsMap.get(timestamp) == null) {
					eventsMap.put(timestamp, new ArrayList<Event>());
				}
				
				eventsMap.get(timestamp).add(currEvent);
			}
		}
		
		DBReader.closeConnection();
	}

	private static void submitEventsAsync(
			HashMap<ZonedDateTime, List<Event>> eventsMap,
			SolutionGateway gateway) {
		ExecutorService executor = Executors.newFixedThreadPool(4);
		
		Set<ZonedDateTime> keySet = eventsMap.keySet();
		ArrayList<ZonedDateTime> sortedEvents = new ArrayList<ZonedDateTime>(keySet);
		Collections.sort(sortedEvents);
		
		ZonedDateTime lastTimeStamp = sortedEvents.get(0);
		
		for (ZonedDateTime currTime : sortedEvents) {
			for (Event eventToSubmit : eventsMap.get(currTime)) {
				executor.execute(new SubmitEvent(gateway, eventToSubmit));
			}
			
			try {
				if(currTime.isAfter(ConverterUtility.absDate.minusDays(1))) {
//					Thread.sleep(((calculateDiffernceBetweenDates(currTime, lastTimeStamp) * 1000) / 5) * 2);
					Thread.sleep(1000);
				}
				
				lastTimeStamp = currTime;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static long calculateDiffernceBetweenDates(ZonedDateTime currTime,ZonedDateTime lastTimeStamp){
		return ChronoUnit.MINUTES.between(lastTimeStamp.toLocalDateTime(), currTime.toLocalDateTime());
		}

}

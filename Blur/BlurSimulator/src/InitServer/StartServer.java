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
//			testDriver.deleteAllEntities();
//			testDriver.resetSolutionState();
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
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			try {
				if(currTime.isAfter(ConverterUtility.absDate.minusDays(1))) {
//					Thread.sleep(((calculateDiffernceBetweenDates(currTime, lastTimeStamp) * 1000) / 5) * 2);
					Thread.sleep(200);
				}
				else {
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

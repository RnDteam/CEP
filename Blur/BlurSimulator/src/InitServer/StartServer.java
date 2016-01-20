package InitServer;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;

import DBHandler.DBReader;
import blur.model.TrafficCameraReport;
import blur.model.Vehicle;

import com.ibm.geolib.geom.Point;
import com.ibm.geolib.st.SpatioTemporalService;
import com.ibm.ia.common.GatewayException;
import com.ibm.ia.common.RoutingException;
import com.ibm.ia.gateway.GridConfiguration;
import com.ibm.ia.gateway.GridConnection;
import com.ibm.ia.gateway.GridConnectionFactory;
import com.ibm.ia.gateway.SolutionChangedException;
import com.ibm.ia.gateway.SolutionGateway;
import com.ibm.ia.gateway.client.GatewayClient;
import com.ibm.ia.model.Event;
import com.ibm.ia.model.Relationship;
import com.ibm.ia.testdriver.TestDriver;
import com.ibm.ws.objectgrid.thread.Executor;


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

	private static void getDataAndSendEvents(SolutionGateway gateway)
			throws SolutionChangedException, GatewayException, RoutingException {
		List<TrafficCameraReport> trafficCamersReportList = DBReader.getTrafficCameraReportEvent().getAllEntities(gateway);
		HashMap<ZonedDateTime, List<Event>> eventsMap = createTimeStampedEventsMap(trafficCamersReportList);
		
		submitEventsAsync(eventsMap, gateway);
//		sendTrafficCameraReportEvents(gateway, trafficCamersReportList);
	}

	private static void submitEventsAsync(
			HashMap<ZonedDateTime, List<Event>> eventsMap,
			SolutionGateway gateway) {
		ExecutorService executor = Executors.newFixedThreadPool(4);
		
		for (ZonedDateTime time : eventsMap.keySet()) {
			for (Event eventToSubmit : eventsMap.get(time)) {
				executor.execute(new SubmitEvent(gateway, eventToSubmit));
			}
			
			try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private static HashMap<ZonedDateTime, List<Event>> createTimeStampedEventsMap(
			List<TrafficCameraReport> trafficCamersReportList) {
		HashMap<ZonedDateTime, List<Event>> eventMap = new HashMap<>();
		
		// Add the traffic camera reports.
		for (TrafficCameraReport trafficCameraReport : trafficCamersReportList) {
			ZonedDateTime trafficTimestamp = trafficCameraReport.getTimestamp();
			
			if(eventMap.get(trafficTimestamp) == null) {
				eventMap.put(trafficTimestamp, new ArrayList<Event>());
			}
			
			eventMap.get(trafficTimestamp).add(trafficCameraReport);
		}
		
		return eventMap;
	}

	private static void sendTrafficCameraReportEvents(SolutionGateway gateway, List<TrafficCameraReport> trafficCamersReportList) throws GatewayException, RoutingException {
		for (TrafficCameraReport trafficCameraReport : trafficCamersReportList) {
			gateway.submit(trafficCameraReport);
			try {
				Thread.sleep(800L);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

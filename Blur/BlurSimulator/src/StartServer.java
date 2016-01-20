import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.List;

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
import com.ibm.ia.model.Relationship;
import com.ibm.ia.testdriver.TestDriver;


public class StartServer {

	public static void main(String[] args) {
		TestDriver testDriver = new  TestDriver();
		try {
			testDriver.connect();
			testDriver.startRecording();
		} catch (GatewayException e1) {
			e1.printStackTrace();
		}
		
		GatewayClient.init();
		GridConfiguration gridConfig = new GridConfiguration("localhost:2809");
		GridConnection connection = null;
		SolutionGateway gateway = null;
		try {
			connection = GridConnectionFactory.createGridConnection(gridConfig);
			gateway = connection.getSolutionGateway("Blur");
			
			sendEvents(gateway);
			
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
		
		System.out.println("finished!");
	}

	private static void sendEvents(SolutionGateway gateway)
			throws SolutionChangedException, GatewayException, RoutingException {
		DBReader reader = new DBReader();
		List<TrafficCameraReport> trafficCamersReportList = reader.getAllTrafficCameraReports();
		sendTrafficCameraReportEvents(gateway);
		sendCellularReportEvents(gateway);
	}

	private static void sendCellularReportEvents(SolutionGateway gateway) {
		// TODO Auto-generated method stub
		
	}

	private static void sendTrafficCameraReportEvents(SolutionGateway gateway) throws IllegalArgumentException, GatewayException, RoutingException {
		TrafficCameraReport event = gateway.getEventFactory().createEvent(TrafficCameraReport.class);
		
		event.setCameraId(1);
		Point point = SpatioTemporalService.getService().getGeometryFactory().getPoint( 32, 35);
		event.setCameraLocation(point);
		Relationship<Vehicle> createRelationship = gateway.createRelationship(Vehicle.class, "123");
		event.setVehicle(createRelationship);
		ZonedDateTime defaultTime = ZonedDateTime.now(gateway.getSolutionZoneId());
		gateway.submit(event, defaultTime);
		
	}

}

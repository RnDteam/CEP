package Events;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import com.ibm.ia.gateway.SolutionGateway;

import DBHandler.ConverterUtility;
import DBHandler.DBReader;
import DBHandler.IDBInteraction;
import blur.model.TrafficCameraReport;
import blur.model.Vehicle;

public class TrafficCameraReportEvent implements IDBInteraction<TrafficCameraReport>{

	private final String getAllEvents ="SELECT * FROM traffic_camera_reports";
	
	@Override
	public List<TrafficCameraReport> getAllEntities(SolutionGateway gateway) {
		Connection dbConnection = DBReader.getDBConnection();
		ArrayList<TrafficCameraReport> trafficCameraReportList = new ArrayList<>();
		try {
			Statement statement = dbConnection.createStatement();
			ResultSet vehicleResult = statement.executeQuery(getAllEvents);
			
			while (vehicleResult.next()) {
				trafficCameraReportList.add(converDBRowToObject(vehicleResult, gateway));
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		DBReader.closeConnection(dbConnection);
		return trafficCameraReportList;
	}

	@Override
	public TrafficCameraReport converDBRowToObject(ResultSet resultSet, SolutionGateway gateway) {
		TrafficCameraReport trafficCameraReportEvent = null;
		try {
			trafficCameraReportEvent = gateway.getEventFactory().createEvent(TrafficCameraReport.class);
			
			String cameraId = resultSet.getString(1);
			String reportTime = resultSet.getString(2);
			String logntitude = resultSet.getString(3);
			String latitude = resultSet.getString(4);
			String licensePlate = resultSet.getString(5);
			
			trafficCameraReportEvent.setCameraId(Integer.parseInt(cameraId));
			trafficCameraReportEvent.setCameraLocation(ConverterUtility.getPointFromString(logntitude, latitude));
			trafficCameraReportEvent.setVehicle(gateway.createRelationship(Vehicle.class, licensePlate));
			trafficCameraReportEvent.setTimestamp(ZonedDateTime.now().minusDays(5).plusMinutes(Integer.parseInt(reportTime)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return trafficCameraReportEvent;
	}

}

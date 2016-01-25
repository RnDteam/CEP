package Events;

import java.sql.ResultSet;
import com.ibm.ia.gateway.SolutionGateway;

import DBHandler.ConverterUtility;
import DBHandler.DBReader;
import DBHandler.EventCreation;
import blur.model.TrafficCameraReport;
import blur.model.Vehicle;

public class TrafficCameraReportEvent extends EventCreation<TrafficCameraReport>{
	

	@Override
	public TrafficCameraReport convertDBRowToObject(ResultSet resultSet, SolutionGateway gateway) {
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
			trafficCameraReportEvent.setTimestamp(ConverterUtility.absDate.plusMinutes(Integer.parseInt(reportTime)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return trafficCameraReportEvent;
	}

	@Override
	public String getTableName() {
		return "traffic_camera_reports";
	}
}

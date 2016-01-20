package DBHandler;

import java.sql.ResultSet;
import java.time.ZonedDateTime;

import blur.model.TrafficCameraReport;
import blur.model.Vehicle;

import com.ibm.geolib.geom.Point;
import com.ibm.geolib.st.SpatioTemporalService;
import com.ibm.ia.gateway.SolutionGateway;

public class ConverterUtility {

	public static Point getPointFromString(String logntitude, String latitude) {
		
		return SpatioTemporalService.getService().getGeometryFactory().
				getPoint(Double.parseDouble(logntitude), Double.parseDouble(latitude));
		
	}

}

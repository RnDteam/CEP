package DBHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.ibm.geolib.geom.Point;
import com.ibm.geolib.st.MovingGeometry;
import com.ibm.geolib.st.SpatioTemporalService;

public class ConverterUtility {

	public static final ZonedDateTime absDate = ZonedDateTime.of(2016, 1, 1, 0, 0, 0, 0, ZoneId.systemDefault());
	
	public static final ZonedDateTime initDate = absDate.minusYears(1);
	
	public static Point getPointFromString(String logntitude, String latitude) {
		double logt = Double.parseDouble(logntitude);
		double lat = Double.parseDouble(latitude);
		return getPointFromDouble(logt, lat);
		
	}
	
	public static MovingGeometry<Point> getMovingGeometryFromString(String logntitude, String latitude, ZonedDateTime reportTime) {
		MovingGeometry<Point> location = SpatioTemporalService.getService().getMovingGeometryFactory().getMovingGeometry();
		Point point = getPointFromString(logntitude, latitude);
		location.setGeometryAtTime(point, reportTime);
		
		return location;
	}

	public static Point getPointFromDouble(double longt, double lat) {
		return SpatioTemporalService.getService().getGeometryFactory().
		getPoint(longt, lat);
	}

}

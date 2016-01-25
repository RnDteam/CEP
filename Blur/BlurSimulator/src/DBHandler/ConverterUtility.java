package DBHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import com.ibm.geolib.geom.Point;
import com.ibm.geolib.st.SpatioTemporalService;

public class ConverterUtility {

	public static final ZonedDateTime absDate = ZonedDateTime.of(2016, 1, 1, 0, 0, 0, 0, ZoneId.systemDefault());
	
	public static final ZonedDateTime initDate = absDate.minusYears(1);
	
	public static Point getPointFromString(String logntitude, String latitude) {
		
		return SpatioTemporalService.getService().getGeometryFactory().
				getPoint(Double.parseDouble(logntitude), Double.parseDouble(latitude));
		
	}

}

package blurservices;
import blur.model.Vehicle;

import com.ibm.geolib.geom.Polygon;

public interface IRangeCalculator {
	public Polygon getRange(Vehicle vehicle, int minutes);

}

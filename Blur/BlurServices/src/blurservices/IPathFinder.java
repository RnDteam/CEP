package blurservices;

import com.ibm.geolib.geom.Polygon;

import blur.model.Person;
import blur.model.Vehicle;

public interface IPathFinder {
	public boolean isTherePath(String ownerId, int depth);
	public boolean isGood( Person person, int depth );
	public Polygon getRange(Vehicle vehicle, int minutes);
}
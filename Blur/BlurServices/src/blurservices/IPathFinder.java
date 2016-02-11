package blurservices;

import java.util.Set;

import com.ibm.geolib.geom.Polygon;
import com.ibm.ia.model.Relationship;

import blur.model.Person;
import blur.model.Vehicle;

public interface IPathFinder {
	public boolean isTherePath(String ownerId, int depth);
	public boolean isGood( Person person, int depth );
	public Polygon getRange(Vehicle vehicle, int minutes);
	public int getRelationshipDepth(Person first, Person second);
	Set<Relationship<Person>> getNearbyPeople(Person person);
}
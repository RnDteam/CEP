package blurservices;

import java.util.ArrayList;

import blur.model.Person;
import blur.model.Vehicle;

import com.ibm.geolib.GeoSpatialService;
import com.ibm.geolib.geom.GeometryFactory;
import com.ibm.geolib.geom.LinearRing;
import com.ibm.geolib.geom.Point;
import com.ibm.geolib.geom.Polygon;

public class PathFinder implements IPathFinder {
	@Override
	public boolean isTherePath(String ownerId, int depth) {
		System.out.println("********************************* PathFinder *****************************");
		return true;
	}

	@Override
	public boolean isGood( Person person, int depth ) {
		if(depth < 3) {
			return person.getName().contains("Dan");			
		}
		else {
			return false;
		}
	}
	
	@Override
	public Polygon getRange(Vehicle vehicle, int minutes) {
		
		System.out.println("********************************* Get Range *****************************");
		
		GeometryFactory geometryFactory = GeoSpatialService.getService().getGeometryFactory();
	
		Point vehicleLocation = vehicle.getLocation();
		double[] coord = vehicleLocation.getCoordinates();
		
		double shift = 1;
		
		Point point1 = geometryFactory.getPoint(coord[0] - shift, coord[1] - shift);
		Point point2 = geometryFactory.getPoint(coord[0] - shift, coord[1] + shift);
		Point point3 = geometryFactory.getPoint(coord[0] + shift, coord[1] + shift);
		Point point4 = geometryFactory.getPoint(coord[0] + shift, coord[1] - shift);
		
		ArrayList<Point> pointsList = new ArrayList<Point>();
		pointsList.add(point1);
		pointsList.add(point2);
		pointsList.add(point3);
		pointsList.add(point4);

		LinearRing linearRing = geometryFactory.getLinearRing(pointsList);
		Polygon polygon = geometryFactory.getPolygon(linearRing);
		return polygon;
	}
}

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
		
		GeometryFactory geometryFactory = GeoSpatialService.getService().getGeometryFactory();
		
	//	Point point = vehicle.getLocation();
				
		Point point1 = geometryFactory.getPoint(0, 0);
		Point point2 = geometryFactory.getPoint(3, 0);
		Point point3 = geometryFactory.getPoint(3, 3);
		Point point4 = geometryFactory.getPoint(0, 3);
		
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

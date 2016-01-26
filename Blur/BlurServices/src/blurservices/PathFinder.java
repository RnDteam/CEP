package blurservices;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import blur.model.Person;
import blur.model.Vehicle;
import blur.model.VehicleDetails;

import com.ibm.geolib.GeoSpatialService;
import com.ibm.geolib.geom.GeometryFactory;
import com.ibm.geolib.geom.LinearRing;
import com.ibm.geolib.geom.Point;
import com.ibm.geolib.geom.Polygon;

public class PathFinder implements IPathFinder {
	
	private static final String MY_SQL_DB_URL = "jdbc:mysql://localhost:3306/cep_try";
	private static final String USER_NAME = "root";
	private static final String PASSWORD = "root";
	private static Connection dbConnection = null;
	private static final String personLinkTableName = "person_link";


	public static void closeConnection(Connection dbConnection) {
		try {
			if(dbConnection != null) {
				dbConnection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static Connection getDBConnection() {
		if(dbConnection == null) {
			try {
				dbConnection = DriverManager.getConnection(MY_SQL_DB_URL, USER_NAME, PASSWORD);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return dbConnection;
	}

	public boolean getPersonLinkToCriminalFromDB(VehicleDetails vehicle_details) {
		String personIdColumn = "Id";
		String personLinkColumn = "Link";
		String getPersonLinkQuery = "SELECT " + personLinkColumn + " FROM " + personLinkTableName + " WHERE ";
		
		getPersonLinkQuery += personIdColumn + "='" + vehicle_details.getMaker() + "'";
		
		String personLinkString = null;
		try {
			Statement statement = dbConnection.createStatement();
			ResultSet resultSet = statement.executeQuery(getPersonLinkQuery);
			
			while (resultSet.next()) {
				if (personLinkString != null){
					System.out.println("Two rows with the same person Id");
				}
				personLinkString = resultSet.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeConnection(dbConnection);
		
		return (personLinkString != null && personLinkString.equals("1"));
	}
	
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

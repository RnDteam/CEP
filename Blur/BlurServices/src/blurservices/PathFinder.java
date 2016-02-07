package blurservices;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import blur.model.Person;
import blur.model.Vehicle;

import com.ibm.geolib.GeoSpatialService;
import com.ibm.geolib.geom.GeometryFactory;
import com.ibm.geolib.geom.LinearRing;
import com.ibm.geolib.geom.Point;
import com.ibm.geolib.geom.Polygon;
import com.ibm.geolib.st.MovingGeometry;

public class PathFinder implements IPathFinder {

	// private static final String MY_SQL_DB_URL =
	// "jdbc:mysql://localhost:3306/cep_try";
	// private static final String USER_NAME = "root";
	// private static final String PASSWORD = "root";
	private static final String personLinkTableName = "ob_links";

	public synchronized Connection getConnection() throws NamingException,
			SQLException {

		Context ctx = new InitialContext();
		System.out.println("PathFinder: Looking up datasource...");
		DataSource dataSource = (DataSource) ctx.lookup("jdbc/mysql");
		if (dataSource != null) {
			System.out.println("PathFinder: Got datasource: " + dataSource);
			Connection con = dataSource.getConnection();
			if (con != null) {
				System.out.println("PathFinder: Got connection: " + con);
			}
			return con;
		} else {
			System.out
					.println("PathFinder: Got null datasource: " + dataSource);
		}

		throw new IllegalStateException("PathFinder: null data source");
	}

	public boolean getPersonLinkToCriminalFromDB(String personId) {
		String personIdColumn = "Person_ID";
		String personLinkColumn = "Have_link_to_criminal";
		String getPersonLinkQuery = "SELECT " + personLinkColumn + " FROM "
				+ personLinkTableName + " WHERE ";

		getPersonLinkQuery += personIdColumn + "='" + personId + "'";

		String personLinkString = null;
		try {
			Connection con = getConnection();
			Statement statement = con.createStatement();
			ResultSet resultSet = statement.executeQuery(getPersonLinkQuery);

			while (resultSet.next()) {
				if (personLinkString != null) {
					System.out.println("PathFinder: Two rows with the same person Id");
				}
				personLinkString = resultSet.getString(1);
			}

			resultSet.close();
			statement.close();
			con.close();
		} catch (SQLException | NamingException e) {
			e.printStackTrace();
		}

		boolean result = "1".equals(personLinkString);
		System.out.println( "PathFinder: Person with id " +  personId + " has link to criminal: " + result );
		return result;
	}

	@Override
	public boolean isTherePath(String ownerId, int depth) {
		System.out
				.println("********************************* PathFinder *****************************");
		return getPersonLinkToCriminalFromDB(ownerId);
	}

	@Override
	public boolean isGood(Person person, int depth) {
		if (depth < 3) {
			return person.getName().contains("Dan");
		} else {
			return false;
		}
	}

	@Override
	public Polygon getRange(Vehicle vehicle, int minutes) {

		System.out
				.println("********************************* Get Range *****************************");

		GeometryFactory geometryFactory = GeoSpatialService.getService()
				.getGeometryFactory();

		MovingGeometry<Point> vehicleLocation = vehicle.getLocation();
		double[] coord = vehicleLocation.getLastObservedGeometry()
				.getCoordinates();

		double shift = 1;

		Point point1 = geometryFactory.getPoint(coord[0] - shift, coord[1]
				- shift);
		Point point2 = geometryFactory.getPoint(coord[0] - shift, coord[1]
				+ shift);
		Point point3 = geometryFactory.getPoint(coord[0] + shift, coord[1]
				+ shift);
		Point point4 = geometryFactory.getPoint(coord[0] + shift, coord[1]
				- shift);

		ArrayList<Point> pointsList = new ArrayList<Point>();
		pointsList.add(point1);
		pointsList.add(point2);
		pointsList.add(point3);
		pointsList.add(point4);

		LinearRing linearRing = geometryFactory.getLinearRing(pointsList);
		Polygon polygon = geometryFactory.getPolygon(linearRing);
		return polygon;
	}

	@Override
	public int getRelationshipDepth(Person first, Person second) {
		boolean areKnowEachOther = first.getRole().resolve().getOrganization()
				.getKey()
				.equals(second.getRole().resolve().getOrganization().getKey());

		if (areKnowEachOther) {
			return 0;
		}
		return 2;
	}
}

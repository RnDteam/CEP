package blur.extensions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

import blur.model.BuildingType;
import blur.model.ConceptFactory;
import blur.model.Person;
import blur.model.TrafficCameraReport;
import blur.model.Vehicle;
import blur.model.VehicleDetails;
import blur.model.VehicleStatus;
import blur.model.VehicleType;

import com.ibm.geolib.geom.Point;
import com.ibm.geolib.st.MovingGeometry;
import com.ibm.geolib.st.SpatioTemporalService;
import com.ibm.ia.common.ComponentException;
import com.ibm.ia.extension.EntityInitializer;
import com.ibm.ia.extension.annotations.EntityInitializerDescriptor;
import com.ibm.ia.model.Event;
import com.ibm.ia.model.Relationship;

@EntityInitializerDescriptor(entityType = Vehicle.class)
public class VehicleInitializer extends EntityInitializer<Vehicle> {

	private static Random random;
	private ConceptFactory conceptFactory;
	private static final String MY_SQL_DB_URL = "jdbc:mysql://localhost:3306/cep_try";
	private static final String USER_NAME = "root";
	private static final String PASSWORD = "root";
	private Connection dbConnection = null;
	private static final String vehicleTableName = "DB_Vehicles";
	private static final String vehicleTypeTableName = "dn_vehicle_type";

	public void closeConnection() {
		try {
			if(dbConnection != null) {
				dbConnection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Connection getDBConnection() {
		if(dbConnection == null) {
			try {
				Class.forName("com.mysql.jdbc.Driver").newInstance();
				dbConnection = DriverManager.getConnection(MY_SQL_DB_URL, USER_NAME, PASSWORD);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return dbConnection;
	}
	
	public void setVehicleTypeSpeedFromDB(VehicleDetails vehicle_details) {
		String vehicleMakerColumn = "Maker";
		String vehicleModelColumn = "Model";
		String vehicleYearColumn = "Year";
		String vehicleMaxSpeedColumn = "Max_Speed";
		String vehicleTypeColumn = "Type";
		String getVehicleTypeQuery = "SELECT " + vehicleMaxSpeedColumn +"," + vehicleTypeColumn + " FROM " + vehicleTypeTableName + " WHERE ";
		
		getVehicleTypeQuery += vehicleMakerColumn + "='" + vehicle_details.getMaker() + "' AND ";
		getVehicleTypeQuery += vehicleModelColumn + "='" + vehicle_details.getModel() + "' AND ";
		getVehicleTypeQuery += vehicleYearColumn + "='" + vehicle_details.getYear() + "';";
		
		String vehicleTypeString = null;
		double vehicleMaxSpeedString = 0.0;

//		System.out.println(getVehicleTypeQuery);
		try {
			Statement statement = dbConnection.createStatement();
			ResultSet resultSet = statement.executeQuery(getVehicleTypeQuery);
			while (resultSet.next()) {
				if (vehicleTypeString != null){
					System.out.println("Two rows with the same Vehicle Details");
				}
//				System.out.println(resultSet.getString(1)+resultSet.getString(2));
				vehicleMaxSpeedString = Double.parseDouble(resultSet.getString(1));
				vehicleTypeString = resultSet.getString(2);
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		
		if (vehicleTypeString != null){
			vehicle_details.setType(convertVehicleType(vehicleTypeString));
		}
		
		vehicle_details.setMaximumSpeed(vehicleMaxSpeedString);

	}
	
	public void setVehicleFromDB(Vehicle vehicle) {
		String vehicleLicensePlateColumn = "Plate_num";
		String vehicleMakerColumn = "Maker";
		String vehicleModelColumn = "Model";
		String vehicleYearColumn = "Year";
		String ownerIdColumn = "Owner_ID";

		String getVehicleQuery = "SELECT " 
				+ vehicleMakerColumn + "," + vehicleModelColumn + "," + vehicleYearColumn + "," + ownerIdColumn
				+ " FROM " + vehicleTableName 
				+ " WHERE " + vehicleLicensePlateColumn + "='" + vehicle.getLicensePlateNumber() + "';";
		
		String personLinkString = null;
		try {
			Statement statement = dbConnection.createStatement();
			ResultSet resultSet = statement.executeQuery(getVehicleQuery);
			while (resultSet.next()) {
				if (personLinkString != null){
					System.out.println("Two rows with the same vehicle license plate");
				}
				convertDBRowToEntity(resultSet, vehicle);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void convertDBRowToEntity(ResultSet row, Vehicle vehicle){
		random = new Random();
		conceptFactory = getConceptFactory(ConceptFactory.class);

		vehicle.setLastSeen(null);
		vehicle.setStatus(VehicleStatus.INACTIVE);
		vehicle.setSuspicious(false);

		VehicleDetails details = conceptFactory.createVehicleDetails();
		try {
			details.setMaker(row.getString(1));
			details.setModel(row.getString(2));
			details.setYear(row.getString(3));
			setVehicleTypeSpeedFromDB(details);
			
			String ownerId = row.getString(4);
			vehicle.setOwner(getOwnerFromES(ownerId)); //TODO : check if works
//			vehicle.setOrganization();

			
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public Vehicle createEntityFromEvent(Event event) throws ComponentException {
		if (!(event instanceof TrafficCameraReport))
			return null;
		
		System.out.println( "***** VehicleInitializer createEntityFromEvent ****** " );
		TrafficCameraReport report = (TrafficCameraReport) event;
		Vehicle entity = super.createEntityFromEvent(event);

		MovingGeometry<Point> location = SpatioTemporalService.getService().getMovingGeometryFactory().getMovingGeometry();
		
		location.setGeometryAtTime(report.getCameraLocation(), report.getTimestamp());
		entity.setLocation(location);


		return entity;
	}

	@Override
	public void initializeEntity(Vehicle entity) throws ComponentException {
		super.initializeEntity(entity);
		System.out.println( "***** VehicleInitializer initializeEntity ****** " );
		getDBConnection();
		setVehicleFromDB(entity);
		closeConnection();
	}

	private Relationship<Person> getOwnerFromES(String ownerId) {
		return getModelFactory().createRelationship(Person.class, ownerId);
	}
	
	private VehicleType convertVehicleType(String type) {
		String lowerCase = type.toLowerCase();
		
		for (VehicleType vehicleType : VehicleType.values()) {
			if(lowerCase.equals(vehicleType.toString().toLowerCase())) {
				return vehicleType;
			}
		}
		
		return null;
	}
}
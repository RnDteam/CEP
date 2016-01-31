package blur.extensions;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Random;

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
	private static Connection dbConnection = null;
	private static final String vehicleTableName = "DB_Vehicles";
	private static final String vehicleTypeTableName = "dn_vehicle-type";



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

	/**
	public Relationship<Organization> getOrganizationFromDB(String ownerId) {
		String vehicleMakerColumn = "Maker";
		String vehicleModelColumn = "Model";
		String vehicleYearColumn = "Year";
		String vehicleMaxSpeedColumn = "Max_Speed";
		String vehicleTypeColumn = "Type";
		String getVehicleTypeQuery = "SELECT " + vehicleMaxSpeedColumn +"," + vehicleTypeColumn + " FROM " + vehicleTypeTableName + " WHERE ";
		
		getVehicleTypeQuery += vehicleMakerColumn + "='" + vehicle_details.getMaker() + "' AND ";
		getVehicleTypeQuery += vehicleModelColumn + "='" + vehicle_details.getModel() + "' AND ";
		getVehicleTypeQuery += vehicleYearColumn + "='" + vehicle_details.getYear() + "'";
		
		String vehicleTypeString = null;
		double vehicleMaxSpeedString = 0.0;
		try {
			Statement statement = dbConnection.createStatement();
			ResultSet resultSet = statement.executeQuery(getVehicleTypeQuery);
			
			while (resultSet.next()) {
				if (vehicleTypeString != null){
					System.out.println("Two rows with the same ownerId");
				}
				
				vehicleMaxSpeedString = Double.parseDouble(resultSet.getString(1));
				vehicleTypeString = resultSet.getString(2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeConnection(dbConnection);
		
		Organization myOrganization = conceptFactory.createOrganization("");
		Relationship<Organization> organizationRelationship = getModelFactory().createRelationship(myOrganization);
		return organizationRelationship;
	}
	**/
	
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
			System.out.println(1);
			ResultSet resultSet = statement.executeQuery(getVehicleTypeQuery);
			System.out.println(2);
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
		closeConnection(dbConnection);
		
		if (vehicleTypeString != null){
			vehicle_details.setType(VehicleType.valueOf(vehicleTypeString));
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
		
//		System.out.println("Statement:" + getVehicleQuery);
		String personLinkString = null;
		try {
			Statement statement = dbConnection.createStatement();
			ResultSet resultSet = statement.executeQuery(getVehicleQuery);
			while (resultSet.next()) {
				if (personLinkString != null){
					System.out.println("Two rows with the same vehicle license plate");
				}
//				System.out.println(resultSet.getString(1)+resultSet.getString(2)+resultSet.getString(3)+resultSet.getString(4));
				convertDBRowToEntity(resultSet, vehicle);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		closeConnection(dbConnection);

	}

	private void convertDBRowToEntity(ResultSet row, Vehicle vehicle){
		random = new Random();
		conceptFactory = getConceptFactory(ConceptFactory.class);

		vehicle.setLastSeen(null);
		vehicle.setStatus(VehicleStatus.INACTIVE);
		vehicle.setSuspicious(false);

		VehicleDetails details = vehicle.getDetails();
		try {
			details.setMaker(row.getString(1));
			details.setModel(row.getString(2));
			details.setYear(row.getString(3));
			setVehicleTypeSpeedFromDB(details);
			
			String ownerId = row.getString(4);
			vehicle.setOwner(getOwnerFromES(ownerId)); //TODO : check if works
//			vehicle.setOrganization();

			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
	}

	@Override
	public Vehicle createEntityFromEvent(Event event) throws ComponentException {
		Vehicle entity = super.createEntityFromEvent(event);

		if( event instanceof TrafficCameraReport ) {
			System.out.println( "***** VehicleInitializer createEntityFromEvent ****** " );
			MovingGeometry<Point> location = SpatioTemporalService.getService().getMovingGeometryFactory().getMovingGeometry();
			TrafficCameraReport report = (TrafficCameraReport) event;
			location.setGeometryAtTime(report.getCameraLocation(), report.getTimestamp());
			
			entity.setLocation(location);
		}

		return entity;
	}

	@Override
	public void initializeEntity(Vehicle entity) throws ComponentException {
		super.initializeEntity(entity);
		System.out.println( "***** VehicleInitializer initializeEntity ****** " );
		getDBConnection();
		setVehicleFromDB(entity);

	}

	private Relationship<Person> getOwnerFromES(String ownerId) {
		return getModelFactory().createRelationship(Person.class, ownerId);
	}

	/**
	private VehicleDetails getDetailsFromES(String licensePlateNumber) {
		VehicleDetails myDetails = conceptFactory.createVehicleDetails();
		//		myDetails.setType(generateStatus());
		myDetails.setType(VehicleType.MOTORCYCLE);
		myDetails.setMaker("Maker" + random.nextInt());
		myDetails.setMaximumSpeed(random.nextInt(100) + 150);
		Integer randomYear = (random.nextInt(2015 - 1980)) + 1980;
		myDetails.setYear(randomYear.toString());
		return myDetails;
	}
	private static VehicleType generateStatus() {
		int vehicleStatusSize = VehicleType.values().length;
		int randomStatusIndex = random.nextInt(vehicleStatusSize);
		return VehicleType.values()[randomStatusIndex];
	}
	private Relationship<Organization> getOrganizationFromES(
			String licensePlateNumber) {
		Organization myOrganization = conceptFactory.createOrganization("Organization-" + licensePlateNumber);
		myOrganization.setType(generateOrganizationType());
		Relationship<Organization> organizationRelationship = getModelFactory().createRelationship(myOrganization);
		return organizationRelationship;
	}
	private OrganizationType generateOrganizationType() {
		int organizationTypeSize = OrganizationType.values().length;
		int randomStatusIndex = random.nextInt(organizationTypeSize);
		return OrganizationType.values()[randomStatusIndex];
	}
	**/
}
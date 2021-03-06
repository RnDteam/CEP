package blur.extensions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

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

	private ConceptFactory conceptFactory;
//	private static final String MY_SQL_DB_URL = "jdbc:mysql://localhost:3306/cep_try";
//	private static final String USER_NAME = "root";
//	private static final String PASSWORD = "root";
	private static final String vehicleTableName = "db_vehicles";
	private static final String vehicleTypeTableName = "db_vehicletype";
	
	public synchronized Connection getConnection() throws NamingException, SQLException {
       
            Context ctx = new InitialContext();
        	System.out.println( "VehicleInitializer: Looking up datasource...");
            DataSource dataSource = (DataSource) ctx.lookup("jdbc/mysql");
            if (dataSource != null) {
 //           	System.out.println( "VehicleInitializer: Got datasource: " + dataSource );
                Connection con = dataSource.getConnection();
                if(con!=null) {
                	System.out.println( "VehicleInitializer: Got connection: " + con );
                }
                return con;
            }
            else {
            	System.out.println( "VehicleInitializer: Got null datasource: " + dataSource );
            }
			
            throw new IllegalStateException("null data source");
    }

	
	public VehicleDetails getVehicleTypeSpeedFromDB(Connection con, VehicleDetails vehicle_details) {
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
			Statement statement = con.createStatement();
			ResultSet resultSet = statement.executeQuery(getVehicleTypeQuery);
			while (resultSet.next()) {
				if (vehicleTypeString != null){
					System.out.println("VehicleInitializer: Two rows with the same Vehicle Details");
				}
//				System.out.println(resultSet.getString(1)+resultSet.getString(2));
				vehicleMaxSpeedString = Double.parseDouble(resultSet.getString(1));
//				System.out.println("VehicleInitializer: speed = " + vehicleMaxSpeedString );
				vehicleTypeString = resultSet.getString(2);
//				System.out.println("VehicleInitializer: type = " + vehicleTypeString);
			}
			
			resultSet.close();
			statement.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if (vehicleTypeString != null){
			vehicle_details.setType(convertVehicleType(vehicleTypeString));
		}
		
		vehicle_details.setMaximumSpeed(vehicleMaxSpeedString);
		return vehicle_details;
	}
	
	public void setVehicleFromDB(Connection con, Vehicle vehicle) {
		String vehicleLicensePlateColumn = "Plate_num";
		String vehicleMakerColumn = "Maker";
		String vehicleModelColumn = "Model";
		String vehicleYearColumn = "Year";
		String ownerIdColumn = "Owner_ID";

		String getVehicleQuery = "SELECT " 
				+ vehicleMakerColumn + "," + vehicleModelColumn + "," + vehicleYearColumn + "," + ownerIdColumn
				+ " FROM " + vehicleTableName 
				+ " WHERE " + vehicleLicensePlateColumn + "='" + vehicle.getLicensePlateNumber() + "';";
		
		System.out.println( "Running query: " + getVehicleQuery );
		
		String personLinkString = null;
		try {
			Statement statement = con.createStatement();
			ResultSet resultSet = statement.executeQuery(getVehicleQuery);
			while (resultSet.next()) {
				if (personLinkString != null){
					System.out.println("Two rows with the same vehicle license plate");
				}
				convertDBRowToEntity(con, resultSet, vehicle);
			}
			resultSet.close();
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void convertDBRowToEntity(Connection con, ResultSet row, Vehicle vehicle){
		conceptFactory = getConceptFactory(ConceptFactory.class);

		vehicle.setLastSeen(null);
		vehicle.setStatus(VehicleStatus.INACTIVE);
		vehicle.setSuspicious(false);
		vehicle.setAlertMessage("N/A");

		VehicleDetails details = conceptFactory.createVehicleDetails();
		try {
			details.setMaker(row.getString(1));
			details.setModel(row.getString(2));
			details.setYear(row.getString(3));
			details = getVehicleTypeSpeedFromDB(con, details);
			vehicle.setDetails(details);
			
			String ownerId = row.getString(4);
			vehicle.setOwner(getOwnerFromES(ownerId)); //TODO : check if works
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		System.out.println( "VehicleInitializer: set details: for vehicle " + vehicle.get$Id() + " " + details );
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
//		System.out.println( "***** VehicleInitializer initializeEntity ****** " );
		Connection con;
		try {
			con = getConnection();
			setVehicleFromDB(con, entity);
			
			// set the display attributes -- (a workaround for the map viewer)
			entity.setpMaker( entity.getDetails().getMaker() );
			entity.setpModel( entity.getDetails().getModel() );
			entity.setpYear( entity.getDetails().getYear() );
			entity.setpMaximumSpeed( entity.getDetails().getMaximumSpeed() );
			entity.setpType( entity.getDetails().getType() );
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
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
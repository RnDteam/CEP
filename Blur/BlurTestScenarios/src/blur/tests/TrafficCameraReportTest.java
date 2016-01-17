package blur.tests;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import blur.model.Building;
import blur.model.BuildingInitialization;
import blur.model.BuildingType;
import blur.model.BuildingUsageType;
import blur.model.ConceptFactory;
import blur.model.Organization;
import blur.model.OrganizationInitialization;
import blur.model.OrganizationRoleInitialization;
import blur.model.OrganizationType;
import blur.model.Person;
import blur.model.PersonInitialization;
import blur.model.PersonState;
import blur.model.VehicleInitialization;
import blur.model.VehicleStatus;
import blur.model.VehicleType;

import com.ibm.geolib.geom.Point;
import com.ibm.geolib.st.SpatioTemporalService;
import com.ibm.ia.common.GatewayException;
import com.ibm.ia.common.RoutingException;
import com.ibm.ia.common.SolutionException;
import com.ibm.ia.testdriver.TestDriver;

public class TrafficCameraReportTest {

	protected static TestDriver testDriver;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		// create Tables and Data for this test
		GenerateDBEvents.connectToDB();
		
		testDriver = new TestDriver();
		testDriver.connect();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		testDriver.disconnect();
		testDriver = null;
	}

	@Before
	public void setUp() throws Exception {
		testDriver.deleteAllEntities();
		testDriver.resetSolutionState();
		testDriver.startRecording();
	}

	@After
	public void tearDown() throws Exception {
		testDriver.endTest();
		Thread.sleep(5000);
		testDriver.stopRecording();
	}

	@Test
	public void test() throws SolutionException, GatewayException, RoutingException, InterruptedException {
		
		ConceptFactory conceptFactory = testDriver.getConceptFactory(ConceptFactory.class);
		ZonedDateTime timestamp = ZonedDateTime.now().minusYears(1);
		
		String databaseUrl = "jdbc:mysql://localhost:3306/cep_try";
		String user = "root";
		String password = "root";
		
		Connection connection = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("mysql jdbc driver is missing");
		}
		
		try {
			connection = DriverManager.getConnection(databaseUrl, user, password);
			if	(connection != null) {
				// generate data
				generateEntities(connection);
				
				deserializeAndSend(connection, testDriver);
				
			}
			System.out.println("Succeeded");
		} catch (Exception e) {
			
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void deserializeAndSend(Connection connection, TestDriver testDriver) {
	
		String getAllOrganizationQuery = "select * from organizations";
		String getAllBuildingsQuery = "select * from buildings";
		String getAllOrganizationRoleQuery = "select * from organization_roles";
		String getAllPersonsQuery = "select * from persons";
		String getAllVehiclesQuery = "select * from vehicles";
		String getAllVehicleDetailsQuery = "select * from vehicle_details";
		
		if (connection != null) {
			
			Statement statement;
			try {
				statement = connection.createStatement();
				
				executeOrganizationsInitiallization(testDriver, getAllOrganizationQuery, statement);
				
				executeOrganizationRoleInitiallization(testDriver, getAllOrganizationRoleQuery, statement);
				
				executePersonsInitiallization(testDriver, getAllPersonsQuery, statement);
				
				executeVehicleInitiallization(testDriver, getAllVehiclesQuery, statement);
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SolutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (GatewayException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (RoutingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}	
		}
		
	}
	
	private void executeVehicleInitiallization(TestDriver testDriver2,
			String getAllVehiclesQuery, Statement statement) throws SQLException, GatewayException, RoutingException, SolutionException {
		
		ResultSet vehicleResult = statement.executeQuery(getAllVehiclesQuery);
		
		ArrayList<VehicleInitialization> vehicles = new ArrayList<VehicleInitialization>();
		
		while (vehicleResult.next()) {
			vehicles.add(EntityDeserializer.getVehicleInitialization(vehicleResult, testDriver));
		}
		
		for (VehicleInitialization vehicleInitialization : vehicles) {
			testDriver.submitEvent(vehicleInitialization);
		}
	}
	
	private void executeOrganizationRoleInitiallization(TestDriver testDriver2,
			String getAllOrganizationRoleQuery, Statement statement) throws SQLException, GatewayException, RoutingException, SolutionException {
		ResultSet organizationRoleResult = statement.executeQuery(getAllOrganizationRoleQuery);
		
		ArrayList<OrganizationRoleInitialization> organizationRoles = new ArrayList<OrganizationRoleInitialization>();
		
		while (organizationRoleResult.next()) {
			organizationRoles.add(EntityDeserializer.getOrganizationRoleInitialization(organizationRoleResult, testDriver));
		}
		
		for (OrganizationRoleInitialization organizationRoleInitialization : organizationRoles) {
			testDriver.submitEvent(organizationRoleInitialization);
		}
	}

	private void executePersonsInitiallization(TestDriver testDriver2,
			String getAllPersonsQuery, Statement statement) throws SQLException, SolutionException, GatewayException, RoutingException {
		ResultSet personResult = statement.executeQuery(getAllPersonsQuery);
		
		ArrayList<PersonInitialization> persons = new ArrayList<PersonInitialization>();
		
		while (personResult.next()) {
			persons.add(EntityDeserializer.getPersonInitialization(personResult, testDriver));
		}
		
		for (PersonInitialization personInitialization : persons) {
			testDriver.submitEvent(personInitialization);
		}
	}

	private void executeOrganizationsInitiallization(TestDriver testDriver,
			String getAllOrganizationQuery, Statement statement)
			throws SQLException, SolutionException, GatewayException,
			RoutingException {
		ResultSet organizationResult = statement.executeQuery(getAllOrganizationQuery);
		
		ArrayList<OrganizationInitialization> organizations = new ArrayList<OrganizationInitialization>();
		
		while (organizationResult.next()) {
			organizations.add(EntityDeserializer.getOrganizationInitialization(organizationResult, testDriver));
		}
		
		for (OrganizationInitialization organizationInitialization : organizations) {
			testDriver.submitEvent(organizationInitialization);
		}
	}

	private static void generateEntities(Connection connection) {
		
		if	(connection != null) {
			
			String organization1 = "organization1";
			String organization2 = "organization2";
			String organization3 = "organization3";
			
			String person1 = "person1";
			String person2 = "person2";
			String person3 = "person3";
			
			String organizationRole1 = "organizationRole1";
			String organizationRole2 = "organizationRole2";
			String organizationRole3 = "organizationRole3";
			
			String vehicleDetails1 = "vehicleDetails1";
			String vehicleDetails2 = "vehicleDetails2";
			
			String vehicle1 = "vehicle1";
			String vehicle2 = "vehicle2";
			
			ArrayList<String> queries = new ArrayList<String>();
			
			String queryOrganization1 = EntityGenerator.generateOrganizationQuery(organization1, OrganizationType.CRIMINAL.toString());
			queries.add(queryOrganization1);
			String queryOrganization2 = EntityGenerator.generateOrganizationQuery(organization2, OrganizationType.CRIMINAL.toString());
			queries.add(queryOrganization2);
			String queryOrganization3 = EntityGenerator.generateOrganizationQuery(organization3, OrganizationType.COMMERCIAL.toString());
			queries.add(queryOrganization3);
			
			String queryOrganizationRole1 = EntityGenerator.generateOrganizationRoleQuery(organizationRole1, person1, organization1, "null");
			queries.add(queryOrganizationRole1);
			String queryOrganizationRole2 = EntityGenerator.generateOrganizationRoleQuery(organizationRole2, person2, organization2, "null");
			queries.add(queryOrganizationRole2);
			String queryOrganizationRole3 = EntityGenerator.generateOrganizationRoleQuery(organizationRole3, person3, organization3, "null");
			queries.add(queryOrganizationRole3);
			
			String queryPerson1 = EntityGenerator.generatePerson(person1, organizationRole1, "35,34", PersonState.ACTIVE.toString());
			queries.add(queryPerson1);
			String queryPerson2 = EntityGenerator.generatePerson(person2, organizationRole2, "35.1,34.2", PersonState.ACTIVE.toString());
			queries.add(queryPerson2);
			String queryPerson3 = EntityGenerator.generatePerson(person3, organizationRole3, "35.3,34.5", PersonState.ACTIVE.toString());
			queries.add(queryPerson3);
			
			String queryVehicleDetails1 = EntityGenerator.generateVehicleDetails(vehicleDetails1, "Maker1", "BMW", VehicleType.MOTORCYCLE.toString(), "2013", "200");
			queries.add(queryVehicleDetails1);
			String queryVehicleDetails2 = EntityGenerator.generateVehicleDetails(vehicleDetails2, "Maker2", "BMW", VehicleType.TRUCK.toString(), "2015", "111");
			queries.add(queryVehicleDetails2);
			
			String queryVehicle1 = EntityGenerator.generateVehicle(vehicle1, vehicleDetails1, person1, organization1, "35,34", VehicleStatus.INACTIVE.toString(), "null", "FALSE");
			queries.add(queryVehicle1);
			String queryVehicle2 = EntityGenerator.generateVehicle(vehicle2, vehicleDetails2, person2, organization2, "35.5, 34.2", VehicleStatus.INACTIVE.toString(), "null", "FALSE");
			queries.add(queryVehicle2);
			
			Statement statement;
			try {
				statement = connection.createStatement();
				
				connection.setAutoCommit(false);
				
				for (String query : queries) {
					statement.addBatch(query);
				}
				
				int[] results = statement.executeBatch();
				connection.commit();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}

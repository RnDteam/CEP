package blur.tests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Random;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import blur.model.ConceptFactory;
import blur.model.Organization;
import blur.model.OrganizationInitialization;
import blur.model.OrganizationRoleInitialization;
import blur.model.OrganizationType;
import blur.model.OrganizationalRole;
import blur.model.Person;
import blur.model.PersonInitialization;
import blur.model.PersonState;
import blur.model.TrafficCameraReport;
import blur.model.Vehicle;
import blur.model.VehicleDetails;
import blur.model.VehicleDetailsInitialization;
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
	private static String vehicle1;
	private static String vehicle2;
	
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
		String databaseUrl = "jdbc:mysql://localhost:3306/cep_try";
		String user = "root";
		String password = "root";
		
		Connection connection = null;
		
		try {
			connection = DriverManager.getConnection(databaseUrl, user, password);
			if	(connection != null) {
				// generate data
				generateEntities(connection);
				
				// deserialize from DB and initialize
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
	
	private void deserializeAndSend(Connection connection, TestDriver testDriver) throws InterruptedException, SolutionException, GatewayException, RoutingException {
	
		String getAllOrganizationQuery = "select * from organizations";
		String getAllOrganizationRoleQuery = "select * from organization_roles";
		String getAllPersonsQuery = "select * from persons";
		String getAllVehiclesQuery = "select * from vehicles";
		String getAllVehicleDetailsQuery = "select * from vehicle_details";
		
		ArrayList<OrganizationInitialization> executeOrganizationsInitiallization = null;
		ArrayList<OrganizationRoleInitialization> executeOrganizationRoleInitiallization = null;
		ArrayList<PersonInitialization> executePersonsInitiallization = null;
		ArrayList<VehicleDetailsInitialization> executeVehicleDetailsInitiallization = null;
		ArrayList<VehicleInitialization> executeVehicleInitiallization = null;
		if (connection != null) {
			
			Statement statement;
			try {
				statement = connection.createStatement();
				
				executeOrganizationsInitiallization = executeOrganizationsInitiallization(testDriver, getAllOrganizationQuery, statement);
				executeOrganizationRoleInitiallization = executeOrganizationRoleInitiallization(testDriver, getAllOrganizationRoleQuery, statement);
				executePersonsInitiallization = executePersonsInitiallization(testDriver, getAllPersonsQuery, statement);
				executeVehicleDetailsInitiallization = executeVehicleDetailsInitiallization(testDriver, getAllVehicleDetailsQuery, statement);
				executeVehicleInitiallization = executeVehicleInitiallization(testDriver, getAllVehiclesQuery, statement);
				
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
		
		// allow the server time to create the entities
		Thread.sleep(2000);
		
		entitiesExistsAssertions(testDriver, executeOrganizationsInitiallization,
				executeOrganizationRoleInitiallization,
				executePersonsInitiallization,
				executeVehicleDetailsInitiallization,
				executeVehicleInitiallization);
		
		emitEvents(testDriver, executeVehicleInitiallization);
	}

	private void emitEvents(TestDriver testDriver,
			ArrayList<VehicleInitialization> executeVehicleInitiallization)
			throws SolutionException, GatewayException, RoutingException {
		ConceptFactory conceptFactory = testDriver.getConceptFactory(ConceptFactory.class);
		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime oneDayAgo = now.minusDays(1);
		
		TrafficCameraReport trafficCameraReport = conceptFactory.createTrafficCameraReport(oneDayAgo);
		trafficCameraReport.setCameraId(1);
		Point location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		trafficCameraReport.setCameraLocation(location);
		trafficCameraReport.setVehicle(testDriver.createRelationship(Vehicle.class, vehicle1));
		testDriver.submitEvent(trafficCameraReport);
		
		TrafficCameraReport trafficCameraReport1 = conceptFactory.createTrafficCameraReport(oneDayAgo);
		trafficCameraReport1.setCameraId(2);
		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		trafficCameraReport1.setCameraLocation(location);
		trafficCameraReport1.setVehicle(testDriver.createRelationship(Vehicle.class, vehicle2));
		testDriver.submitEvent(trafficCameraReport1);

//		TrafficCameraReport trafficCameraReport3 = conceptFactory.createTrafficCameraReport(oneDayAgo);
//		trafficCameraReport3.setCameraId(3);
//		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
//		trafficCameraReport1.setCameraLocation(location);
//		trafficCameraReport1.setVehicle(testDriver.createRelationship(Vehicle.class, "vehicle3"));
//		testDriver.submitEvent(trafficCameraReport3);
	}

	private void entitiesExistsAssertions(
			TestDriver testDriver,
			ArrayList<OrganizationInitialization> executeOrganizationsInitiallization,
			ArrayList<OrganizationRoleInitialization> executeOrganizationRoleInitiallization,
			ArrayList<PersonInitialization> executePersonsInitiallization,
			ArrayList<VehicleDetailsInitialization> executeVehicleDetailsInitiallization,
			ArrayList<VehicleInitialization> executeVehicleInitiallization) {
		
		for (OrganizationInitialization organizationInitialization : executeOrganizationsInitiallization) {
			String key = organizationInitialization.getOrganization().getKey();
			Organization organization = testDriver.fetchEntity(Organization.class, key);
			Assert.assertNotNull(organization);
			Assert.assertEquals( key, organization.getId() );
		}
		
		for (OrganizationRoleInitialization organizationRoleInitialization : executeOrganizationRoleInitiallization) {
			String key = organizationRoleInitialization.getOrganizationalRole().getKey();
			OrganizationalRole organizationRole = testDriver.fetchEntity(OrganizationalRole.class, key);
			Assert.assertNotNull(organizationRole);
			Assert.assertEquals( key, organizationRole.getId() );
		}
		
		for (PersonInitialization personInitialization : executePersonsInitiallization) {
			String key = personInitialization.getPerson().getKey();
			Person person = testDriver.fetchEntity(Person.class, key);
			Assert.assertNotNull(person);
			Assert.assertEquals( key, person.getId() );
		}
		for (VehicleDetailsInitialization vehicleDetailsInitialization : executeVehicleDetailsInitiallization) {
			String key = vehicleDetailsInitialization.getVehicleDetails().getKey();
			VehicleDetails vehicleDetails = testDriver.fetchEntity(VehicleDetails.class, key);
			Assert.assertNotNull(vehicleDetails);
			Assert.assertEquals(key, vehicleDetails.getId() );
		}
		for (VehicleInitialization vehicleInitialization : executeVehicleInitiallization) {
			String key = vehicleInitialization.getVehicle().getKey();
			Vehicle vehicle = testDriver.fetchEntity(Vehicle.class, key);
			Assert.assertNotNull(vehicle);
			Assert.assertEquals( key, vehicle.getLicensePlateNumber() );
		}
	}
	
	private ArrayList<VehicleDetailsInitialization> executeVehicleDetailsInitiallization(TestDriver testDriver2,
			String getAllVehicleDetailsQuery, Statement statement) throws SQLException, GatewayException, RoutingException, SolutionException  {
		ResultSet vehicleDetailsResult = statement.executeQuery(getAllVehicleDetailsQuery);
		
		ArrayList<VehicleDetailsInitialization> vehicleDetails = new ArrayList<VehicleDetailsInitialization>();
		
		while (vehicleDetailsResult.next()) {
			vehicleDetails.add(EntityDeserializer.getVehicleDetailsInitialization(vehicleDetailsResult, testDriver));
		}
		
		for (VehicleDetailsInitialization vehicleDetailsInitialization : vehicleDetails) {
			testDriver.submitEvent(vehicleDetailsInitialization);
		}
		
		return vehicleDetails;
		
	}

	private ArrayList<VehicleInitialization> executeVehicleInitiallization(TestDriver testDriver2,
			String getAllVehiclesQuery, Statement statement) throws SQLException, GatewayException, RoutingException, SolutionException {
		
		ResultSet vehicleResult = statement.executeQuery(getAllVehiclesQuery);
		
		ArrayList<VehicleInitialization> vehicles = new ArrayList<VehicleInitialization>();
		
		while (vehicleResult.next()) {
			vehicles.add(EntityDeserializer.getVehicleInitialization(vehicleResult, testDriver));
		}
		
		for (VehicleInitialization vehicleInitialization : vehicles) {
			testDriver.submitEvent(vehicleInitialization);
		}
		
		return vehicles;
	}
	
	private ArrayList<OrganizationRoleInitialization> executeOrganizationRoleInitiallization(TestDriver testDriver2,
			String getAllOrganizationRoleQuery, Statement statement) throws SQLException, GatewayException, RoutingException, SolutionException {
		ResultSet organizationRoleResult = statement.executeQuery(getAllOrganizationRoleQuery);
		
		ArrayList<OrganizationRoleInitialization> organizationRoles = new ArrayList<OrganizationRoleInitialization>();
		
		while (organizationRoleResult.next()) {
			organizationRoles.add(EntityDeserializer.getOrganizationRoleInitialization(organizationRoleResult, testDriver));
		}
		
		for (OrganizationRoleInitialization organizationRoleInitialization : organizationRoles) {
			testDriver.submitEvent(organizationRoleInitialization);
		}
		
		return organizationRoles;
	}

	private ArrayList<PersonInitialization> executePersonsInitiallization(TestDriver testDriver2,
			String getAllPersonsQuery, Statement statement) throws SQLException, SolutionException, GatewayException, RoutingException {
		ResultSet personResult = statement.executeQuery(getAllPersonsQuery);
		
		ArrayList<PersonInitialization> persons = new ArrayList<PersonInitialization>();
		
		while (personResult.next()) {
			persons.add(EntityDeserializer.getPersonInitialization(personResult, testDriver));
		}
		
		for (PersonInitialization personInitialization : persons) {
			testDriver.submitEvent(personInitialization);
		}
		
		return persons;
	}

	private ArrayList<OrganizationInitialization> executeOrganizationsInitiallization(TestDriver testDriver,
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
		
		return organizations;
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
			
			vehicle1 = "vehicle1";
			vehicle2 = "vehicle2";
			
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
			
			String queryVehicle1 = EntityGenerator.generateVehicle(vehicle1, vehicleDetails1, person1, organization1, "35,34", VehicleStatus.INACTIVE.toString(), ZonedDateTime.now().minusDays(5).toString(), "FALSE");
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

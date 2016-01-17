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
import blur.model.OrganizationType;
import blur.model.Person;
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
		
		String databaseUrl = "jdbc:mysql://localhost:3306/mysql";
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
		
		
		// Organization Initialization
//		OrganizationInitialization organizationInitialization1 = conceptFactory.createOrganizationInitialization(timestamp);
//		organizationInitialization1.setType(OrganizationType.CRIMINAL);
//		organizationInitialization1.setOrganization(testDriver.createRelationship(Organization.class, "organization1"));
		
				
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
			
			String queryOrganization1 = EntityGenerator.generateOrganizationQuery(organization1);
			queries.add(queryOrganization1);
			String queryOrganization2 = EntityGenerator.generateOrganizationQuery(organization2);
			queries.add(queryOrganization2);
			String queryOrganization3 = EntityGenerator.generateOrganizationQuery(organization3);
			queries.add(queryOrganization3);
			
			String queryOrganizationRole1 = EntityGenerator.generateOrganizationRoleQuery(organizationRole1, person1, organization1, "null");
			queries.add(queryOrganizationRole1);
			String queryOrganizationRole2 = EntityGenerator.generateOrganizationRoleQuery(organizationRole2, person2, organization2, "null");
			queries.add(queryOrganizationRole2);
			String queryOrganizationRole3 = EntityGenerator.generateOrganizationRoleQuery(organizationRole3, person3, organization3, "null");
			queries.add(queryOrganizationRole3);
			
			String queryPerson1 = EntityGenerator.generatePerson(person1, organizationRole1);
			queries.add(queryPerson1);
			String queryPerson2 = EntityGenerator.generatePerson(person2, organizationRole2);
			queries.add(queryPerson2);
			String queryPerson3 = EntityGenerator.generatePerson(person3, organizationRole3);
			queries.add(queryPerson3);
			
			String queryVehicleDetails1 = EntityGenerator.generateVehicleDetails(vehicleDetails1, "Maker1", "BMW", VehicleType.MOTORCYCLE.toString(), "2013", "200");
			queries.add(queryVehicleDetails1);
			String queryVehicleDetails2 = EntityGenerator.generateVehicleDetails(vehicleDetails2, "Maker2", "BMW", VehicleType.TRUCK.toString(), "2015", "111");
			queries.add(queryVehicleDetails2);
			
			String queryVehicle1 = EntityGenerator.generateVehicle(vehicle1, vehicleDetails1, person1, organization1);
			queries.add(queryVehicle1);
			String queryVehicle2 = EntityGenerator.generateVehicle(vehicle2, vehicleDetails2, person2, organization2);
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

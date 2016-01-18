package blur.tests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.ZonedDateTime;
import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import blur.model.Building;
import blur.model.BuildingInitialization;
import blur.model.BuildingType;
import blur.model.BuildingUsageType;
import blur.model.CellularReport;
import blur.model.ConceptFactory;
import blur.model.Organization;
import blur.model.OrganizationInitialization;
import blur.model.OrganizationType;
import blur.model.Person;

import com.ibm.geolib.geom.Point;
import com.ibm.geolib.st.SpatioTemporalService;
import com.ibm.ia.common.DataFormat;
import com.ibm.ia.common.GatewayException;
import com.ibm.ia.common.RoutingException;
import com.ibm.ia.common.SolutionException;
import com.ibm.ia.common.debug.DebugInfo;
import com.ibm.ia.model.Event;
import com.ibm.ia.testdriver.IADebugReceiver;
import com.ibm.ia.testdriver.TestDriver;

public class CellularReportFromDbTest {
	
	protected static TestDriver testDriver;
	protected static IADebugReceiver debugReceiver = new IADebugReceiver();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		// create Tables and Data for this test
		GenerateDBEvents.connectToDB();
		
		testDriver = new TestDriver();
		testDriver.connect();
		testDriver.addDebugReceiver(debugReceiver);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		testDriver.disconnect();
		testDriver = null;
	}

	@Before
	public void setUp() throws Exception {
		debugReceiver.clearDebugInfo();
		testDriver.deleteAllEntities();
		testDriver.resetSolutionState();
		testDriver.startRecording();
	}

	@After
	public void tearDown() throws Exception {
		Thread.sleep(5000);
		testDriver.endTest();
		testDriver.stopRecording();
	}

	@Test
	public void test() throws SolutionException, GatewayException, RoutingException, InterruptedException{
		
		ConceptFactory conceptFactory = testDriver.getConceptFactory(ConceptFactory.class);
		ZonedDateTime now = ZonedDateTime.now();
		
		String getAllOrganizationQuery = "select * from organizations";
		String getAllBuildingsQuery = "select * from buildings";
		
<<<<<<< HEAD
		String databaseUrl = "jdbc:mysql://localhost:3306/cep_try";
=======
		String databaseUrl = "jdbc:mysql://localhost:3306/mysql";
>>>>>>> 2b9e66af4fcce98e137dd9f725fc95260bcd5f0f
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

<<<<<<< HEAD
				generateEntities(connection);
				
=======
>>>>>>> 2b9e66af4fcce98e137dd9f725fc95260bcd5f0f
				Statement statement = connection.createStatement();
				
				/*connection.setAutoCommit(false);
				statement.addBatch(getAllOrganizationQuery);
				statement.addBatch(getAllBuildingsQuery);
				
				int[] results = statement.executeBatch();
				connection.commit();*/
				
				//ResultSet rs = statement.executeQuery("select eventName, accountId, country, amount, merchantId, merchantType, merchantLocation, eventMethod from transactions");
				ResultSet rs1 = statement.executeQuery(getAllOrganizationQuery);

				ArrayList<OrganizationInitialization> organizations = new ArrayList<OrganizationInitialization>();
				
				while (rs1.next()) {
					OrganizationInitialization organizationInitialization1 = conceptFactory.createOrganizationInitialization(now);
<<<<<<< HEAD
					OrganizationType organizationType = rs1.getString(2).equals("CRIMINAL") ? OrganizationType.CRIMINAL : OrganizationType.COMMERCIAL;
=======
					OrganizationType organizationType = rs1.getString(2) == "CRIMINAL" ? OrganizationType.CRIMINAL : OrganizationType.COMMERCIAL;
>>>>>>> 2b9e66af4fcce98e137dd9f725fc95260bcd5f0f
					organizationInitialization1.setType(organizationType);
					organizationInitialization1.setOrganization(testDriver.createRelationship(Organization.class, rs1.getString(1)));
					organizations.add(organizationInitialization1);
					
					System.out.println(rs1.getString(1) + " " + rs1.getString(2));
				}
				
				ResultSet rs2 = statement.executeQuery(getAllBuildingsQuery);
				
				ArrayList<BuildingInitialization> buildings = new ArrayList<BuildingInitialization>();
				while (rs2.next()) {
					BuildingInitialization buildingInitialization1 = conceptFactory.createBuildingInitialization(now);
					buildingInitialization1.setBuilding(testDriver.createRelationship(Building.class, rs2.getString(1)));
<<<<<<< HEAD
					BuildingUsageType buildingUsageType = rs2.getString(4).equals("FURNITURE_STORE") ? BuildingUsageType.FURNITURE_STORE : BuildingUsageType.BANK_BRANCH;
					buildingInitialization1.setUsageType(buildingUsageType);
					BuildingType buildingType = rs2.getString(3).equals("WAREHOUSE") ? BuildingType.WAREHOUSE : (rs2.getString(3).equals("APPARTMENT") ? BuildingType.APPARTMENT : BuildingType.COMMERCIAL);
					buildingInitialization1.setType(buildingType);
					
					Point location = EntityDeserializer.getPointFromString(rs2.getString(2));
=======
					BuildingUsageType buildingUsageType = rs2.getString(4) == "FURNITURE_STORE" ? BuildingUsageType.FURNITURE_STORE : BuildingUsageType.BANK_BRANCH;
					buildingInitialization1.setUsageType(buildingUsageType);
					BuildingType buildingType = rs2.getString(3) == "WAREHOUSE" ? BuildingType.WAREHOUSE : (rs2.getString(3) == "APPARTMENT" ? BuildingType.APPARTMENT : BuildingType.COMMERCIAL);
					buildingInitialization1.setType(buildingType);
					
					double x = Double.parseDouble(rs2.getString(2).split(",")[0]);
					double y = Double.parseDouble(rs2.getString(2).split(",")[1]);
					
					Point location = SpatioTemporalService.getService().getGeometryFactory().getPoint( x, y);
>>>>>>> 2b9e66af4fcce98e137dd9f725fc95260bcd5f0f
					buildingInitialization1.setLocation(location);
					buildingInitialization1.setOwner(testDriver.createRelationship(Person.class, "123"));
					buildingInitialization1.addTo_organizations(testDriver.createRelationship(Organization.class, "organization1"));
					
					buildings.add(buildingInitialization1);
					
					System.out.println(rs2.getString(1) + " " + rs2.getString(2) + " " + rs2.getString(3) + " " + rs2.getString(4) + " " + rs2.getString(5) + " " + rs2.getString(6));
				}
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
<<<<<<< HEAD
	}
	
	public static void generateEntities(Connection connection) {
		String queryOrganization1 = EntityGenerator.generateOrganizationQuery("organization1");
		String queryOrganization2 = EntityGenerator.generateOrganizationQuery("organization2");
		String queryOrganization3 = EntityGenerator.generateOrganizationQuery("organization3");
		
		String queryBuilding1 = EntityGenerator.generateBuilding("buiding1", "person1", "organization1");
		String queryBuilding2 = EntityGenerator.generateBuilding("buiding2", "person1", "organization1");
		String queryBuilding3 = EntityGenerator.generateBuilding("buiding3", "person1", "organization3");
		
		System.out.println(queryOrganization1);
		System.out.println(queryOrganization2);
		System.out.println(queryOrganization3);
		System.out.println(queryBuilding1);
		System.out.println(queryBuilding2);
		System.out.println(queryBuilding3);

		Statement statement;
		try {
			statement = connection.createStatement();
			
			connection.setAutoCommit(false);
			statement.addBatch(queryOrganization1);
			statement.addBatch(queryOrganization2);
			statement.addBatch(queryOrganization3);
			statement.addBatch(queryBuilding1);
			statement.addBatch(queryBuilding2);
			statement.addBatch(queryBuilding3);
			
			int[] results = statement.executeBatch();
			connection.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
=======
		
	/*	// Organization Initialization
		OrganizationInitialization organizationInitialization1 = conceptFactory.createOrganizationInitialization(now);
		organizationInitialization1.setType(OrganizationType.CRIMINAL);
		organizationInitialization1.setOrganization(testDriver.createRelationship(Organization.class, "organization1"));
		
		OrganizationInitialization organizationInitialization2 = conceptFactory.createOrganizationInitialization(now);
		organizationInitialization2.setType(OrganizationType.CRIMINAL);
		organizationInitialization2.setOrganization(testDriver.createRelationship(Organization.class, "organization2"));
		
		OrganizationInitialization organizationInitialization3 = conceptFactory.createOrganizationInitialization(now);
		organizationInitialization3.setType(OrganizationType.COMMERCIAL);
		organizationInitialization3.setOrganization(testDriver.createRelationship(Organization.class, "organization3"));
		
		// Building Initialization
		Point location = null;
		
		BuildingInitialization buildingInitialization1 = conceptFactory.createBuildingInitialization(now);
		buildingInitialization1.setBuilding(testDriver.createRelationship(Building.class, BUILDING1));
		buildingInitialization1.setUsageType(BuildingUsageType.BANK_BRANCH);
		buildingInitialization1.setType(BuildingType.APPARTMENT);
		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		buildingInitialization1.setLocation(location);
		buildingInitialization1.setOwner(testDriver.createRelationship(Person.class, "123"));
		buildingInitialization1.addTo_organizations(testDriver.createRelationship(Organization.class, "organization1"));
		buildingInitialization1.addTo_organizations(testDriver.createRelationship(Organization.class, "organization3"));
		
		BuildingInitialization buildingInitialization2 = conceptFactory.createBuildingInitialization(now);
		buildingInitialization2.setBuilding(testDriver.createRelationship(Building.class, BUILDING2));
		buildingInitialization2.setUsageType(BuildingUsageType.FURNITURE_STORE);
		buildingInitialization2.setType(BuildingType.COMMERCIAL);
		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		buildingInitialization2.setLocation(location);
		buildingInitialization2.setOwner(testDriver.createRelationship(Person.class, "123"));
		buildingInitialization2.addTo_organizations(testDriver.createRelationship(Organization.class, "organization3"));

		testDriver.submitEvent(organizationInitialization1);
		testDriver.submitEvent(organizationInitialization2);
		testDriver.submitEvent(organizationInitialization3);
		
		Organization org1 = testDriver.fetchEntity(Organization.class, "organization1");
		Assert.assertNotNull(org1);
		Assert.assertEquals( "organization1", org1.getId() );
		
		testDriver.submitEvent(buildingInitialization1);
		testDriver.submitEvent(buildingInitialization2);
		
		// allow the server time to create the buildings
		Thread.sleep(2000);

		Building building1 = testDriver.fetchEntity(Building.class, BUILDING1);
		Assert.assertNotNull(building1);
		Building building2 = testDriver.fetchEntity(Building.class, BUILDING2);
		Assert.assertNotNull(building2);

		ZonedDateTime oneDayAgo = now.minusDays(1);
		
		for (int i = 0; i < 4; i++) {
			CellularReport cellularReport = conceptFactory.createCellularReport(oneDayAgo);
			cellularReport.setBuilding(testDriver.createRelationship(Building.class, BUILDING1));
			testDriver.submitEvent(cellularReport);
			System.out.println(String.format("Submitted cellular report number {0} from 4 for buiding 1", i));
			oneDayAgo = oneDayAgo.plusMinutes(50);
		}
	    oneDayAgo = oneDayAgo.plusHours(10);
		for (int i = 0; i < 3; i++) {
			CellularReport cellularReport = conceptFactory.createCellularReport(oneDayAgo);
			cellularReport.setBuilding(testDriver.createRelationship(Building.class, BUILDING1));
			testDriver.submitEvent(cellularReport);
			System.out.println(String.format("Submitted cellular report number {0} from 3 for buiding 1", i));
			oneDayAgo = oneDayAgo.plusMinutes(2);
		}
		
		oneDayAgo = now.minusDays(1);
		
		for (int i = 0; i < 4; i++) {
			CellularReport cellularReport = conceptFactory.createCellularReport(oneDayAgo);
			cellularReport.setBuilding(testDriver.createRelationship(Building.class, BUILDING2));
			testDriver.submitEvent(cellularReport);
			System.out.println(String.format("Submitted cellular report number {0} from 4 for building 2", i));
			oneDayAgo = oneDayAgo.plusMinutes(50);
		}
		
		// manually advance time
		testDriver.processPendingSchedules( oneDayAgo.plusHours(5));
		oneDayAgo = oneDayAgo.plusHours(10);
	    
		for (int i = 0; i < 3; i++) {
			CellularReport cellularReport = conceptFactory.createCellularReport(oneDayAgo);
			cellularReport.setBuilding(testDriver.createRelationship(Building.class, BUILDING2));
			testDriver.submitEvent(cellularReport);
			System.out.println(String.format("Submitted cellular report number {0} from 3 for building 2", i));
			oneDayAgo = oneDayAgo.plusMinutes(2);
		}	
		
		Thread.sleep(5000);
		
		building1 = testDriver.fetchEntity(Building.class, BUILDING1);
		Assert.assertEquals( "Should have 2 alerts", 2, building1.getAlerts().size() );
		
		DebugInfo[] debugInfos = debugReceiver.getDebugInfo( "buildingagent " );
		
		for (DebugInfo debugInfo : debugInfos) {
			System.out.println( "DebugInfo: " + debugInfo );
			
			Event event = debugInfo.getEvent();
			String eventXml = testDriver.getModelSerializer().serializeEvent(DataFormat.TYPED_XML, event );
			System.out.println( "Event as XML: " + eventXml );
		}*/
>>>>>>> 2b9e66af4fcce98e137dd9f725fc95260bcd5f0f
	}
}

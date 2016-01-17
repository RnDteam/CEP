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

				generateEntities(connection);
				
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
					OrganizationType organizationType = rs1.getString(2) == "CRIMINAL" ? OrganizationType.CRIMINAL : OrganizationType.COMMERCIAL;
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
					BuildingUsageType buildingUsageType = rs2.getString(4) == "FURNITURE_STORE" ? BuildingUsageType.FURNITURE_STORE : BuildingUsageType.BANK_BRANCH;
					buildingInitialization1.setUsageType(buildingUsageType);
					BuildingType buildingType = rs2.getString(3) == "WAREHOUSE" ? BuildingType.WAREHOUSE : (rs2.getString(3) == "APPARTMENT" ? BuildingType.APPARTMENT : BuildingType.COMMERCIAL);
					buildingInitialization1.setType(buildingType);
					
					double x = Double.parseDouble(rs2.getString(2).split(",")[0]);
					double y = Double.parseDouble(rs2.getString(2).split(",")[1]);
					
					Point location = SpatioTemporalService.getService().getGeometryFactory().getPoint( x, y);
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
	}
}

package blur.tests;

import static org.junit.Assert.*;

import java.time.ZonedDateTime;

import blur.model.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import blur.model.ConceptFactory;
import blur.model.Organization;
import blur.model.OrganizationInitialization;

import com.ibm.geolib.geom.Point;
import com.ibm.geolib.st.SpatioTemporalService;
import com.ibm.ia.common.DataFormat;
import com.ibm.ia.common.GatewayException;
import com.ibm.ia.common.RoutingException;
import com.ibm.ia.common.SolutionException;
import com.ibm.ia.common.debug.DebugInfo;
import com.ibm.ia.model.Event;
import com.ibm.ia.routing.RoutingError;
import com.ibm.ia.testdriver.DebugReceiver;
import com.ibm.ia.testdriver.IADebugReceiver;
import com.ibm.ia.testdriver.TestDriver;

public class CellularReportTest {

	private static final String BUILDING2 = "building2";
	private static final String BUILDING1 = "building1";
	protected static TestDriver testDriver;
	protected static IADebugReceiver debugReceiver = new IADebugReceiver();
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
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
		testDriver.endTest();
		Thread.sleep(5000);
		testDriver.stopRecording();
	}

	@Test
	public void test() throws SolutionException, GatewayException, RoutingException, InterruptedException{
		
		ConceptFactory conceptFactory = testDriver.getConceptFactory(ConceptFactory.class);
		ZonedDateTime now = ZonedDateTime.now();
		
		
		// Organization Initialization
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
		
		// allow the server time to create the orgs
		Thread.sleep(2000);
		
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
		
		DebugInfo[] debugInfos = debugReceiver.getDebugInfo( "buildingagent" );
		
		for (DebugInfo debugInfo : debugInfos) {
			System.out.println( "DebugInfo: " + debugInfo );
			
			Event event = debugInfo.getEvent();
			String eventXml = testDriver.getModelSerializer().serializeEvent(DataFormat.TYPED_XML, event );
			System.out.println( "Event as XML: " + eventXml );
		}
	}
}

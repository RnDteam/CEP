package blur.tests;

import java.time.ZonedDateTime;

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
import blur.model.CellularCallReport;
import blur.model.CellularReport;
import blur.model.ConceptFactory;
import blur.model.Organization;
import blur.model.OrganizationInitialization;
import blur.model.OrganizationRoleInitialization;
import blur.model.OrganizationType;
import blur.model.OrganizationalRole;
import blur.model.Person;
import blur.model.PersonInitialization;
import blur.model.PersonState;

import com.ibm.geolib.geom.Geometry;
import com.ibm.geolib.geom.Point;
import com.ibm.geolib.st.MovingGeometry;
import com.ibm.geolib.st.SpatioTemporalService;
import com.ibm.ia.common.DataFormat;
import com.ibm.ia.common.GatewayException;
import com.ibm.ia.common.RoutingException;
import com.ibm.ia.common.SolutionException;
import com.ibm.ia.common.debug.DebugInfo;
import com.ibm.ia.model.Event;
import com.ibm.ia.testdriver.IADebugReceiver;
import com.ibm.ia.testdriver.TestDriver;

public class Rule_131221 {

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
		Point location = null;
		ZonedDateTime oneDayAgo = now.minusDays(1);

		// Organization Initialization
		OrganizationInitialization organizationInitialization1 = conceptFactory.createOrganizationInitialization(oneDayAgo);
		organizationInitialization1.setType(OrganizationType.CRIMINAL);
		organizationInitialization1.setOrganization(testDriver.createRelationship(Organization.class, "organization1"));
		
		OrganizationInitialization organizationInitialization2 = conceptFactory.createOrganizationInitialization(oneDayAgo);
		organizationInitialization2.setType(OrganizationType.COMMERCIAL);
		organizationInitialization2.setOrganization(testDriver.createRelationship(Organization.class, "organization2"));
		
		// Organization role Initialization		
		OrganizationRoleInitialization organizationRoleInitialization1 = conceptFactory.createOrganizationRoleInitialization(oneDayAgo);
		organizationRoleInitialization1.setOrganizationalRole(testDriver.createRelationship(OrganizationalRole.class, "role1"));
		organizationRoleInitialization1.setOrganization(testDriver.createRelationship(Organization.class, "organization1"));
		
		OrganizationRoleInitialization organizationRoleInitialization2 = conceptFactory.createOrganizationRoleInitialization(oneDayAgo);
		organizationRoleInitialization2.setOrganizationalRole(testDriver.createRelationship(OrganizationalRole.class, "role2"));
		organizationRoleInitialization2.setOrganization(testDriver.createRelationship(Organization.class, "organization2"));
		
		// Person Initialization
		PersonInitialization personInitialization1 = conceptFactory.createPersonInitialization(oneDayAgo);
		personInitialization1.setPerson(testDriver.createRelationship(Person.class, "person1"));
		personInitialization1.setName("a1");
		personInitialization1.setProfession("pro1");
		personInitialization1.setRole(testDriver.createRelationship(OrganizationalRole.class, "role1"));
		personInitialization1.setState(PersonState.ACTIVE);
		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		personInitialization1.setLocation(location);
		
		PersonInitialization personInitialization2 = conceptFactory.createPersonInitialization(oneDayAgo);
		personInitialization2.setPerson(testDriver.createRelationship(Person.class, "person2"));
		personInitialization2.setName("b2");
		personInitialization2.setProfession("pro2");
		personInitialization2.setRole(testDriver.createRelationship(OrganizationalRole.class, "role2"));
		personInitialization2.setState(PersonState.ACTIVE);
		Point person2Location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		personInitialization2.setLocation(person2Location);
		
		Point buildingLocation = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		
		// Building Initialization	
		BuildingInitialization buildingInitialization1 = conceptFactory.createBuildingInitialization(oneDayAgo);
		buildingInitialization1.setBuilding(testDriver.createRelationship(Building.class, "building1"));
		buildingInitialization1.setUsageType(BuildingUsageType.BANK_BRANCH);
		buildingInitialization1.setType(BuildingType.APPARTMENT);
		//location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		buildingInitialization1.setLocation(buildingLocation);
		buildingInitialization1.setOwner(testDriver.createRelationship(Person.class, "123"));
		buildingInitialization1.addTo_organizations(testDriver.createRelationship(Organization.class, "organization1"));
		buildingInitialization1.addTo_organizations(testDriver.createRelationship(Organization.class, "organization2"));
	
		// Create Entities - Submit Events
		testDriver.submitEvent(organizationInitialization1);
		testDriver.submitEvent(organizationInitialization2);
		testDriver.submitEvent(organizationRoleInitialization1);
		testDriver.submitEvent(organizationRoleInitialization2);
		testDriver.submitEvent(personInitialization1);
		testDriver.submitEvent(personInitialization2);
		testDriver.submitEvent(buildingInitialization1);
//		Thread.sleep(3000);
		testDriver.waitUntilSolutionIdle();
		
		// Person2 (none criminal) is 60 minutes in the same building (Shared location) (1).
		Person person2 = testDriver.fetchEntity(Person.class, "person2");
		person2.getLocation().setGeometryAtTime(buildingLocation, oneDayAgo.plusMinutes(120));
		person2.getLocation().setGeometryAtTime(buildingLocation, oneDayAgo.plusMinutes(150));
		person2.getLocation().setGeometryAtTime(buildingLocation, oneDayAgo.plusMinutes(180));
		
		testDriver.updateEntity(person2);
		
//		CellularReport cellularReport = conceptFactory.createCellularReport(oneDayAgo.plusMinutes(180));
//		cellularReport.setBuilding(testDriver.createRelationship(Building.class, "building1"));
//		cellularReport.setPerson(testDriver.createRelationship(Person.class, "person2"));
//		testDriver.submitEvent(cellularReport);
		
		CellularCallReport cellCallReport = conceptFactory.createCellularCallReport(oneDayAgo.plusMinutes(220));
		cellCallReport.setCaller(testDriver.createRelationship(Person.class, "person1"));
		cellCallReport.setCallee(testDriver.createRelationship(Person.class, "person2"));
		cellCallReport.setEventLocation(buildingLocation);
		cellCallReport.addTo_buildings(testDriver.createRelationship(Building.class, "building1"));
		
		testDriver.submitEvent(cellCallReport);
//		Thread.sleep(3000);

//		Assert.assertTrue("Hit a timeout waiting fors idle", testDriver.waitUntilSolutionIdle(30));
		testDriver.waitUntilSolutionIdle();
		
		DebugInfo[] debugInfos = debugReceiver.getDebugInfo("PersonRuleAgent");
		
		for (DebugInfo debugInfo : debugInfos) {
			System.out.println( "DebugInfo: " + debugInfo );
			
			Event event = debugInfo.getEvent();
			String eventXml = testDriver.getModelSerializer().serializeEvent(DataFormat.TYPED_XML, event );
			System.out.println( "Event as XML: " + eventXml );
		}
	}

}

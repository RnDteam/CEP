package blur.tests;

import java.time.ZonedDateTime;

import org.junit.After;
import org.junit.AfterClass;
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
import blur.model.CriminalCellularCallReport;
import blur.model.Organization;
import blur.model.OrganizationInitialization;
import blur.model.OrganizationRoleInitialization;
import blur.model.OrganizationType;
import blur.model.OrganizationalRole;
import blur.model.Person;
import blur.model.PersonInitialization;
import blur.model.PersonState;

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
public class DetectCriminalCellularCall_131 {

	
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
		// Organization Initialization
		OrganizationInitialization organizationInitialization1 = conceptFactory.createOrganizationInitialization(now);
		organizationInitialization1.setType(OrganizationType.CRIMINAL);
		organizationInitialization1.setOrganization(testDriver.createRelationship(Organization.class, "organization1"));
		
		OrganizationInitialization organizationInitialization2 = conceptFactory.createOrganizationInitialization(now);
		organizationInitialization2.setType(OrganizationType.COMMERCIAL);
		organizationInitialization2.setOrganization(testDriver.createRelationship(Organization.class, "organization2"));
		
		// Organization role Initialization		
		OrganizationRoleInitialization organizationRoleInitialization1 = conceptFactory.createOrganizationRoleInitialization(now);
		organizationRoleInitialization1.setOrganizationalRole(testDriver.createRelationship(OrganizationalRole.class, "role1"));
		organizationRoleInitialization1.setOrganization(testDriver.createRelationship(Organization.class, "organization1"));
		
		OrganizationRoleInitialization organizationRoleInitialization2 = conceptFactory.createOrganizationRoleInitialization(now);
		organizationRoleInitialization2.setOrganizationalRole(testDriver.createRelationship(OrganizationalRole.class, "role2"));
		organizationRoleInitialization2.setOrganization(testDriver.createRelationship(Organization.class, "organization2"));
		
		// Person Initialization
		PersonInitialization personInitialization1 = conceptFactory.createPersonInitialization(now);
		personInitialization1.setPerson(testDriver.createRelationship(Person.class, "person1"));
		personInitialization1.setName("a1");
		personInitialization1.setProfession("pro1");
		personInitialization1.setRole(testDriver.createRelationship(OrganizationalRole.class, "role1"));
		personInitialization1.setState(PersonState.ACTIVE);
		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		personInitialization1.setLocation(location);
		
		PersonInitialization personInitialization2 = conceptFactory.createPersonInitialization(now);
		personInitialization2.setPerson(testDriver.createRelationship(Person.class, "person2"));
		personInitialization2.setName("b2");
		personInitialization2.setProfession("pro2");
		personInitialization2.setRole(testDriver.createRelationship(OrganizationalRole.class, "role2"));
		personInitialization2.setState(PersonState.ACTIVE);
		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		personInitialization2.setLocation(location);
		
		// Building Initialization	
		BuildingInitialization buildingInitialization1 = conceptFactory.createBuildingInitialization(now);
		buildingInitialization1.setBuilding(testDriver.createRelationship(Building.class, "building1"));
		buildingInitialization1.setUsageType(BuildingUsageType.BANK_BRANCH);
		buildingInitialization1.setType(BuildingType.APPARTMENT);
		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		buildingInitialization1.setLocation(location);
		buildingInitialization1.setOwner(testDriver.createRelationship(Person.class, "123"));
		buildingInitialization1.addTo_organizations(testDriver.createRelationship(Organization.class, "organization1"));
	
		// Create Entities - Submit Events
		testDriver.submitEvent(organizationInitialization1);
		testDriver.submitEvent(organizationInitialization2);
		testDriver.submitEvent(organizationRoleInitialization1);
		testDriver.submitEvent(organizationRoleInitialization2);
		testDriver.submitEvent(personInitialization1);
		testDriver.submitEvent(personInitialization2);
		testDriver.submitEvent(buildingInitialization1);
		Thread.sleep(3000);
		
		
		ZonedDateTime oneDayAgo = now.minusDays(1);
		// Criminal to commercial
		CellularCallReport cellularCallReport = conceptFactory.createCellularCallReport(oneDayAgo);
		cellularCallReport.setCaller(testDriver.createRelationship(Person.class, "person1"));
		cellularCallReport.setCallee(testDriver.createRelationship(Person.class, "person2"));
		testDriver.submitEvent(cellularCallReport);
		// Commercial to criminal
		oneDayAgo = oneDayAgo.plusMinutes(10);
		CellularCallReport cellularCallReport2 = conceptFactory.createCellularCallReport(oneDayAgo);
		cellularCallReport2.setCaller(testDriver.createRelationship(Person.class, "person2"));
		cellularCallReport2.setCallee(testDriver.createRelationship(Person.class, "person1"));
		testDriver.submitEvent(cellularCallReport2);
		
	
		testDriver.waitUntilSolutionIdle();
					
		System.out.println("********************************************************************");
		DebugInfo[] debugInfos = debugReceiver.getDebugInfo("PersonRuleAgent");
		System.out.println(debugInfos.length);
		for (DebugInfo debugInfo : debugInfos) {
			System.out.println( "DebugInfo: " + debugInfo );
			Event event = debugInfo.getEvent();
			System.out.println("********************************************************************");
			System.out.println(event.get$TypeName().toString());
			System.out.println(event.getClass().toString());
			System.out.println("********************************************************************");
			String eventXml = testDriver.getModelSerializer().serializeEvent(DataFormat.TYPED_XML, event );
			System.out.println( "Event as XML: " + eventXml );
		}	
	}
}

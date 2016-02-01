package blur.tests;

import static org.junit.Assert.*;

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

import com.ibm.geolib.geom.Point;
import com.ibm.geolib.st.SpatioTemporalService;
import com.ibm.ia.common.DataFormat;
import com.ibm.ia.common.GatewayException;
import com.ibm.ia.common.RoutingException;
import com.ibm.ia.common.SolutionException;
import com.ibm.ia.common.debug.DebugInfo;
import com.ibm.ia.model.Event;
import com.ibm.ia.model.Relationship;
import com.ibm.ia.testdriver.IADebugReceiver;
import com.ibm.ia.testdriver.TestDriver;

public class CellularReportWithMovingGeometryTest {

	private static final String BUILDING1 = "building1";
	private static final String BUILDING2 = "building2";
	private static final String BUILDING3 = "building3";
	private static final String BUILDING4 = "building4";

	
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
		Relationship<Organization> org1 = testDriver.createRelationship(Organization.class, "organization1");
		organizationInitialization1.setOrganization(org1);
		
		OrganizationInitialization organizationInitialization2 = conceptFactory.createOrganizationInitialization(now);
		organizationInitialization2.setType(OrganizationType.CRIMINAL);
		Relationship<Organization> org2 = testDriver.createRelationship(Organization.class, "organization2");
		organizationInitialization2.setOrganization(org2);

		OrganizationInitialization organizationInitialization3 = conceptFactory.createOrganizationInitialization(now);
		organizationInitialization3.setType(OrganizationType.COMMERCIAL);
		Relationship<Organization> org3 = testDriver.createRelationship(Organization.class, "organization3");
		organizationInitialization3.setOrganization(org3);
		
		// Organization role Initialization		
		OrganizationRoleInitialization organizationRoleInitialization1 = conceptFactory.createOrganizationRoleInitialization(now);
		organizationRoleInitialization1.setOrganizationalRole(testDriver.createRelationship(OrganizationalRole.class, "role1"));
		organizationInitialization1.setOrganization(testDriver.createRelationship(Organization.class, "organization1"));
		
		OrganizationRoleInitialization organizationRoleInitialization2 = conceptFactory.createOrganizationRoleInitialization(now);
		organizationRoleInitialization2.setOrganizationalRole(testDriver.createRelationship(OrganizationalRole.class, "role2"));
		organizationInitialization2.setOrganization(testDriver.createRelationship(Organization.class, "organization3"));
		
		Point location = null;
		
		// Person Initialization
		PersonInitialization personInitialization1 = conceptFactory.createPersonInitialization(now);
		personInitialization1.setPerson(testDriver.createRelationship(Person.class, "person1"));
		personInitialization1.setName("a1");
		personInitialization1.setProfession("pro1");
		personInitialization1.setRole(testDriver.createRelationship(OrganizationalRole.class, "role1")); //Criminal
		personInitialization1.setState(PersonState.ACTIVE);
		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		personInitialization1.setLocation(location);
		
		PersonInitialization personInitialization2 = conceptFactory.createPersonInitialization(now);
		personInitialization2.setPerson(testDriver.createRelationship(Person.class, "person2"));
		personInitialization2.setName("b2");
		personInitialization2.setProfession("pro2");
		personInitialization2.setRole(testDriver.createRelationship(OrganizationalRole.class, "role2")); // None Criminal
		personInitialization2.setState(PersonState.ACTIVE);
		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		personInitialization2.setLocation(location);
		
		
		
		// Building Initialization	
		// Building 1 - 1 criminal
		BuildingInitialization buildingInitialization1 = conceptFactory.createBuildingInitialization(now);
		buildingInitialization1.setBuilding(testDriver.createRelationship(Building.class, "building1"));
		buildingInitialization1.setUsageType(BuildingUsageType.BANK_BRANCH);
		buildingInitialization1.setType(BuildingType.APPARTMENT);
		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		buildingInitialization1.setLocation(location);
		buildingInitialization1.setOwner(testDriver.createRelationship(Person.class, "123"));
		buildingInitialization1.addTo_organizations(testDriver.createRelationship(Organization.class, "organization1"));

		// Building 2 - 1 none criminal
		BuildingInitialization buildingInitialization2 = conceptFactory.createBuildingInitialization(now);
		buildingInitialization2.setBuilding(testDriver.createRelationship(Building.class, "building2"));
		buildingInitialization2.setUsageType(BuildingUsageType.FURNITURE_STORE);
		buildingInitialization2.setType(BuildingType.COMMERCIAL);
		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		buildingInitialization2.setLocation(location);
		buildingInitialization2.setOwner(testDriver.createRelationship(Person.class, "123"));
		buildingInitialization2.addTo_organizations(testDriver.createRelationship(Organization.class, "organization3"));
		
		// Building 3 - 2 criminal
		BuildingInitialization buildingInitialization3 = conceptFactory.createBuildingInitialization(now);
		buildingInitialization3.setBuilding(testDriver.createRelationship(Building.class, "building3"));
		buildingInitialization3.setUsageType(BuildingUsageType.FURNITURE_STORE);
		buildingInitialization3.setType(BuildingType.COMMERCIAL);
		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		buildingInitialization3.setLocation(location);
		buildingInitialization3.setOwner(testDriver.createRelationship(Person.class, "123"));
		buildingInitialization3.addTo_organizations(testDriver.createRelationship(Organization.class, "organization1"));
		buildingInitialization3.addTo_organizations(testDriver.createRelationship(Organization.class, "organization2"));
		
		// Building 4 - 1 criminal & 1 none criminal
		BuildingInitialization buildingInitialization4 = conceptFactory.createBuildingInitialization(now);
		buildingInitialization4.setBuilding(testDriver.createRelationship(Building.class, "building4"));
		buildingInitialization4.setUsageType(BuildingUsageType.FURNITURE_STORE);
		buildingInitialization4.setType(BuildingType.COMMERCIAL);
		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		buildingInitialization4.setLocation(location);
		buildingInitialization4.setOwner(testDriver.createRelationship(Person.class, "123"));
		buildingInitialization4.addTo_organizations(testDriver.createRelationship(Organization.class, "organization2"));
		buildingInitialization4.addTo_organizations(testDriver.createRelationship(Organization.class, "organization3"));
		
		// Create Entities - Submit Events
		testDriver.submitEvent(organizationInitialization1);
		testDriver.submitEvent(organizationInitialization2);
		testDriver.submitEvent(organizationInitialization3);
		testDriver.submitEvent(organizationRoleInitialization1);
		testDriver.submitEvent(organizationRoleInitialization2);
		testDriver.submitEvent(personInitialization1);
		testDriver.submitEvent(personInitialization2);
		testDriver.submitEvent(buildingInitialization1);
		testDriver.submitEvent(buildingInitialization2);
		testDriver.submitEvent(buildingInitialization3);
		testDriver.submitEvent(buildingInitialization4);
		
		
		
/*		Building building1 = testDriver.fetchEntity(Building.class, "building1");
		Assert.assertNotNull(building1);
		Building building2 = testDriver.fetchEntity(Building.class, "building2");
		Assert.assertNotNull(building2);		
		Building building3 = testDriver.fetchEntity(Building.class, "building3");
		Assert.assertNotNull(building3);
		*/
		// allow the server time to create the entities
		Thread.sleep(3000);
/*		
		ZonedDateTime oneDayAgo = now.minusDays(1);
		
		for (int i = 0; i < 4; i++) {
			CellularReport cellularReport = conceptFactory.createCellularReport(oneDayAgo);
			cellularReport.setBuilding(testDriver.createRelationship(Building.class, BUILDING1));
			testDriver.submitEvent(cellularReport);
			System.out.println(String.format("Submitted cellular report number %d from 4 for buiding 1", i));
			oneDayAgo = oneDayAgo.plusMinutes(50);
		}
	    oneDayAgo = oneDayAgo.plusHours(10);
		for (int i = 0; i < 3; i++) {
			CellularReport cellularReport = conceptFactory.createCellularReport(oneDayAgo);
			cellularReport.setBuilding(testDriver.createRelationship(Building.class, BUILDING1));
			testDriver.submitEvent(cellularReport);
			System.out.println(String.format("Submitted cellular report number %d from 3 for buiding 1", i));
			oneDayAgo = oneDayAgo.plusMinutes(2);
		}
		
		oneDayAgo = now.minusDays(1);
		
		for (int i = 0; i < 4; i++) {
			CellularReport cellularReport = conceptFactory.createCellularReport(oneDayAgo);
			cellularReport.setBuilding(testDriver.createRelationship(Building.class, BUILDING2));
			testDriver.submitEvent(cellularReport);
			System.out.println(String.format("Submitted cellular report number %d from 4 for building 2", i));
			oneDayAgo = oneDayAgo.plusMinutes(50);
		}
	*/
		// manually advance time
		Thread.sleep(5000);		
	}
}

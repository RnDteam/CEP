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
import com.ibm.ia.common.GatewayException;
import com.ibm.ia.common.RoutingException;
import com.ibm.ia.common.SolutionException;
import com.ibm.ia.testdriver.IADebugReceiver;
import com.ibm.ia.testdriver.TestDriver;

public class CellularReportWithMovingGeometryTest {
	
	protected static TestDriver testDriver;
	protected static IADebugReceiver debugReceiver = new IADebugReceiver();
	
	
	private static final String ORGANIZATION1_CRIMINAL = "ORGANIZATION1_CRIMINAL";
	private static final String ORGANIZATION2_CRIMINAL = "ORGANIZATION2_CRIMINAL";
	private static final String ORGANIZATION3_COMMERCIAL = "ORGANIZATION3_COMMERCIAL";
	
	private static final String ROLE1_CRIMINAL = "ROLE1_CRIMINAL";
	private static final String ROLE2_COMMERCIAL_SHOULD_TURN_TO_CRIMINAL = "ROLE2_COMMERCIAL_SHOULD_TURN_TO_CRIMINAL";
	
	private static final String PERSON1_ROLE1 = "PERSON1_ROLE1";
	private static final String PERSON2_ROLE2 = "PERSON2_ROLE2";
	
	private static final String PERSON3_WITHOUT_ROLE = "PERSON3_WITHOUT_ROLE";
	
	private static final String BUILDING1_1_CRIMINAL = "BUILDING1_1_CRIMINAL";
	private static final String BUILDING2_1_COMMERCIAL = "BUILDING2_1_COMMERCIAL";
	private static final String BUILDING3_2_CRIMINALS = "BUILDING3_2_CRIMINALS";
	private static final String BUILDING4_1_CRIMINAL_1_COMMERCIAL = "BUILDING4_1_CRIMINAL_1_COMMERCIAL";
	
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
		organizationInitialization1.setOrganization(testDriver.createRelationship(Organization.class, ORGANIZATION1_CRIMINAL));
		
		OrganizationInitialization organizationInitialization2 = conceptFactory.createOrganizationInitialization(now);
		organizationInitialization2.setType(OrganizationType.CRIMINAL);
		organizationInitialization2.setOrganization(testDriver.createRelationship(Organization.class, ORGANIZATION2_CRIMINAL));

		OrganizationInitialization organizationInitialization3 = conceptFactory.createOrganizationInitialization(now);
		organizationInitialization3.setType(OrganizationType.COMMERCIAL);
		organizationInitialization3.setOrganization(testDriver.createRelationship(Organization.class, ORGANIZATION3_COMMERCIAL));
		
		// Organization role Initialization		
		OrganizationRoleInitialization organizationRoleInitialization1 = conceptFactory.createOrganizationRoleInitialization(now);
		organizationRoleInitialization1.setOrganizationalRole(testDriver.createRelationship(OrganizationalRole.class, ROLE1_CRIMINAL));
		organizationRoleInitialization1.setOrganization(testDriver.createRelationship(Organization.class, ORGANIZATION1_CRIMINAL));
		
		OrganizationRoleInitialization organizationRoleInitialization2 = conceptFactory.createOrganizationRoleInitialization(now);
		organizationRoleInitialization2.setOrganizationalRole(testDriver.createRelationship(OrganizationalRole.class, ROLE2_COMMERCIAL_SHOULD_TURN_TO_CRIMINAL));
		organizationRoleInitialization2.setOrganization(testDriver.createRelationship(Organization.class, ORGANIZATION3_COMMERCIAL));
		
		Point location = null;
		
		// Person Initialization
		PersonInitialization personInitialization1 = conceptFactory.createPersonInitialization(now);
		personInitialization1.setPerson(testDriver.createRelationship(Person.class, PERSON1_ROLE1));
		personInitialization1.setName("a1");
		personInitialization1.setProfession("pro1");
		personInitialization1.setRole(testDriver.createRelationship(OrganizationalRole.class, ROLE1_CRIMINAL)); //Criminal
		personInitialization1.setState(PersonState.ACTIVE);
		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		personInitialization1.setLocation(location);
		
		PersonInitialization personInitialization2 = conceptFactory.createPersonInitialization(now);
		personInitialization2.setPerson(testDriver.createRelationship(Person.class, PERSON2_ROLE2));
		personInitialization2.setName("b2");
		personInitialization2.setProfession("pro2");
		personInitialization2.setRole(testDriver.createRelationship(OrganizationalRole.class, ROLE2_COMMERCIAL_SHOULD_TURN_TO_CRIMINAL)); // None Criminal
		personInitialization2.setState(PersonState.ACTIVE);
		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		personInitialization2.setLocation(location);
		
		
		PersonInitialization personInitialization3 = conceptFactory.createPersonInitialization(now);
		personInitialization3.setPerson(testDriver.createRelationship(Person.class, "PERSON3_FOR_TEST"));
		personInitialization3.setName("b2");
		personInitialization3.setProfession("pro2");
		personInitialization3.setRole(testDriver.createRelationship(OrganizationalRole.class, ROLE1_CRIMINAL)); // Criminal
		personInitialization3.setState(PersonState.ACTIVE);
		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		personInitialization3.setLocation(location);
		
		// Building Initialization	
		// Building 1 - 1 criminal
		BuildingInitialization buildingInitialization1 = conceptFactory.createBuildingInitialization(now);
		buildingInitialization1.setBuilding(testDriver.createRelationship(Building.class, BUILDING1_1_CRIMINAL));
		buildingInitialization1.setUsageType(BuildingUsageType.BANK_BRANCH);
		buildingInitialization1.setType(BuildingType.APPARTMENT);
		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		buildingInitialization1.setLocation(location);
		buildingInitialization1.setOwner(testDriver.createRelationship(Person.class, "123"));
		buildingInitialization1.addTo_organizations(testDriver.createRelationship(Organization.class, ORGANIZATION1_CRIMINAL));

		// Building 2 - 1 none criminal
		BuildingInitialization buildingInitialization2 = conceptFactory.createBuildingInitialization(now);
		buildingInitialization2.setBuilding(testDriver.createRelationship(Building.class, BUILDING2_1_COMMERCIAL));
		buildingInitialization2.setUsageType(BuildingUsageType.FURNITURE_STORE);
		buildingInitialization2.setType(BuildingType.COMMERCIAL);
		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		buildingInitialization2.setLocation(location);
		buildingInitialization2.setOwner(testDriver.createRelationship(Person.class, "123"));
		buildingInitialization2.addTo_organizations(testDriver.createRelationship(Organization.class, ORGANIZATION3_COMMERCIAL));
		
		// Building 3 - 2 criminal
		BuildingInitialization buildingInitialization3 = conceptFactory.createBuildingInitialization(now);
		buildingInitialization3.setBuilding(testDriver.createRelationship(Building.class, BUILDING3_2_CRIMINALS));
		buildingInitialization3.setUsageType(BuildingUsageType.FURNITURE_STORE);
		buildingInitialization3.setType(BuildingType.COMMERCIAL);
		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		buildingInitialization3.setLocation(location);
		buildingInitialization3.setOwner(testDriver.createRelationship(Person.class, "123"));
		buildingInitialization3.addTo_organizations(testDriver.createRelationship(Organization.class, ORGANIZATION1_CRIMINAL));
		buildingInitialization3.addTo_organizations(testDriver.createRelationship(Organization.class, ORGANIZATION2_CRIMINAL));
		
		// Building 4 - 1 criminal & 1 none criminal
		BuildingInitialization buildingInitialization4 = conceptFactory.createBuildingInitialization(now);
		buildingInitialization4.setBuilding(testDriver.createRelationship(Building.class, BUILDING4_1_CRIMINAL_1_COMMERCIAL));
		buildingInitialization4.setUsageType(BuildingUsageType.FURNITURE_STORE);
		buildingInitialization4.setType(BuildingType.COMMERCIAL);
		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		buildingInitialization4.setLocation(location);
		buildingInitialization4.setOwner(testDriver.createRelationship(Person.class, "123"));
		buildingInitialization4.addTo_organizations(testDriver.createRelationship(Organization.class, ORGANIZATION2_CRIMINAL));
		buildingInitialization4.addTo_organizations(testDriver.createRelationship(Organization.class, ORGANIZATION3_COMMERCIAL));
		
		// Create Entities - Submit Events
		testDriver.submitEvent(organizationInitialization1);
		testDriver.submitEvent(organizationInitialization2);
		testDriver.submitEvent(organizationInitialization3);
		testDriver.submitEvent(organizationRoleInitialization1);
		testDriver.submitEvent(organizationRoleInitialization2);
		testDriver.submitEvent(personInitialization1);
		testDriver.submitEvent(personInitialization2);
		testDriver.submitEvent(personInitialization3);
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
		
		ZonedDateTime oneDayAgo = now.minusDays(1);
		
//		Cellular Reports
		CellularReport cellularReport = conceptFactory.createCellularReport(oneDayAgo);
		cellularReport.setBuilding(testDriver.createRelationship(Building.class, BUILDING1_1_CRIMINAL));
		cellularReport.setPerson(testDriver.createRelationship(Person.class, PERSON1_ROLE1));
		testDriver.submitEvent(cellularReport);
		oneDayAgo = oneDayAgo.plusMinutes(1);
		
		cellularReport = conceptFactory.createCellularReport(oneDayAgo);
		cellularReport.setBuilding(testDriver.createRelationship(Building.class, BUILDING2_1_COMMERCIAL));
		cellularReport.setPerson(testDriver.createRelationship(Person.class, PERSON1_ROLE1));
		testDriver.submitEvent(cellularReport);
		oneDayAgo = oneDayAgo.plusMinutes(1);

		cellularReport = conceptFactory.createCellularReport(oneDayAgo);
		cellularReport.setBuilding(testDriver.createRelationship(Building.class, BUILDING3_2_CRIMINALS));
		cellularReport.setPerson(testDriver.createRelationship(Person.class, PERSON1_ROLE1));
		testDriver.submitEvent(cellularReport);
		oneDayAgo = oneDayAgo.plusMinutes(1);

		cellularReport = conceptFactory.createCellularReport(oneDayAgo);
		cellularReport.setBuilding(testDriver.createRelationship(Building.class, BUILDING4_1_CRIMINAL_1_COMMERCIAL));
		cellularReport.setPerson(testDriver.createRelationship(Person.class, PERSON1_ROLE1));
		//testDriver.submitEvent(cellularReport);
		oneDayAgo = oneDayAgo.plusMinutes(1);

		cellularReport = conceptFactory.createCellularReport(oneDayAgo);
		cellularReport.setBuilding(testDriver.createRelationship(Building.class, BUILDING1_1_CRIMINAL));
		cellularReport.setPerson(testDriver.createRelationship(Person.class, PERSON2_ROLE2));
		testDriver.submitEvent(cellularReport);
		oneDayAgo = oneDayAgo.plusMinutes(1);

		cellularReport = conceptFactory.createCellularReport(oneDayAgo);
		cellularReport.setBuilding(testDriver.createRelationship(Building.class, BUILDING2_1_COMMERCIAL));
		cellularReport.setPerson(testDriver.createRelationship(Person.class, PERSON2_ROLE2));
		testDriver.submitEvent(cellularReport);
		oneDayAgo = oneDayAgo.plusMinutes(1);

		cellularReport = conceptFactory.createCellularReport(oneDayAgo);
		cellularReport.setBuilding(testDriver.createRelationship(Building.class, BUILDING3_2_CRIMINALS));
		cellularReport.setPerson(testDriver.createRelationship(Person.class, PERSON2_ROLE2));
		testDriver.submitEvent(cellularReport);
		oneDayAgo = oneDayAgo.plusMinutes(1);

		cellularReport = conceptFactory.createCellularReport(oneDayAgo);
		cellularReport.setBuilding(testDriver.createRelationship(Building.class, BUILDING4_1_CRIMINAL_1_COMMERCIAL));
		cellularReport.setPerson(testDriver.createRelationship(Person.class, PERSON2_ROLE2));
		testDriver.submitEvent(cellularReport);
		
		// person without role
/*		PersonInitialization personWithoutRoleInitialization = conceptFactory.createPersonInitialization(now);
		personWithoutRoleInitialization.setPerson(testDriver.createRelationship(Person.class, PERSON3_WITHOUT_ROLE));
		personWithoutRoleInitialization.setName("Person without role");
		personWithoutRoleInitialization.setProfession("Profession of the person without role");
		personWithoutRoleInitialization.setState(PersonState.ACTIVE);
		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		personWithoutRoleInitialization.setLocation(location);
		testDriver.submitEvent(personWithoutRoleInitialization);
		
		CellularReport cellularReportPersonMissingRole = conceptFactory.createCellularReport(oneDayAgo);
		cellularReportPersonMissingRole.setBuilding(testDriver.createRelationship(Building.class, BUILDING1_1_CRIMINAL));
		cellularReportPersonMissingRole.setPerson(testDriver.createRelationship(Person.class, PERSON3_WITHOUT_ROLE));
		testDriver.submitEvent(cellularReportPersonMissingRole);*/
		
		Thread.sleep(5000);	
	}
}

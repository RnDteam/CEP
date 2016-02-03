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
import com.ibm.ia.testdriver.IADebugReceiver;
import com.ibm.ia.testdriver.TestDriver;

public class ChangePersonLocationTest {
	
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
		
		// Organization role Initialization		
		OrganizationRoleInitialization organizationRoleInitialization1 = conceptFactory.createOrganizationRoleInitialization(now);
		organizationRoleInitialization1.setOrganizationalRole(testDriver.createRelationship(OrganizationalRole.class, "role1"));
		organizationRoleInitialization1.setOrganization(testDriver.createRelationship(Organization.class, "organization1"));
		
		// Person Initialization
		PersonInitialization personInitialization1 = conceptFactory.createPersonInitialization(now);
		personInitialization1.setPerson(testDriver.createRelationship(Person.class, "person1"));
		personInitialization1.setName("a1");
		personInitialization1.setProfession("pro1");
		personInitialization1.setRole(testDriver.createRelationship(OrganizationalRole.class, "role1"));
		personInitialization1.setState(PersonState.ACTIVE);
		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		personInitialization1.setLocation(location);
		
		// Building Initialization	
		BuildingInitialization buildingInitialization1 = conceptFactory.createBuildingInitialization(now);
		buildingInitialization1.setBuilding(testDriver.createRelationship(Building.class, "building1"));
		buildingInitialization1.setUsageType(BuildingUsageType.BANK_BRANCH);
		buildingInitialization1.setType(BuildingType.APPARTMENT);
		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		buildingInitialization1.setLocation(location);
		buildingInitialization1.setOwner(testDriver.createRelationship(Person.class, "123"));
		buildingInitialization1.addTo_organizations(testDriver.createRelationship(Organization.class, "organization1"));
		
		// Building Initialization	
		BuildingInitialization buildingInitialization2 = conceptFactory.createBuildingInitialization(now);
		buildingInitialization2.setBuilding(testDriver.createRelationship(Building.class, "building2"));
		buildingInitialization2.setUsageType(BuildingUsageType.BANK_BRANCH);
		buildingInitialization2.setType(BuildingType.APPARTMENT);
		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		buildingInitialization2.setLocation(location);
		buildingInitialization2.setOwner(testDriver.createRelationship(Person.class, "123"));
		buildingInitialization2.addTo_organizations(testDriver.createRelationship(Organization.class, "organization1"));

		
		// Create Entities - Submit Events
		testDriver.submitEvent(organizationInitialization1);
		testDriver.submitEvent(organizationRoleInitialization1);
		testDriver.submitEvent(personInitialization1);
		testDriver.submitEvent(buildingInitialization1);
		testDriver.submitEvent(buildingInitialization2);
		Thread.sleep(3000);
		
		
		ZonedDateTime oneDayAgo = now.minusDays(1);
		// Cellular Reports
		// 5 reports from building 1
		CellularReport cellularReport = conceptFactory.createCellularReport(oneDayAgo);
		cellularReport.setBuilding(testDriver.createRelationship(Building.class, "building1"));
		cellularReport.setPerson(testDriver.createRelationship(Person.class, "person1"));
		testDriver.submitEvent(cellularReport);
		oneDayAgo = oneDayAgo.plusMinutes(10);
		
		CellularReport cellularReport2 = conceptFactory.createCellularReport(oneDayAgo);
		cellularReport2.setBuilding(testDriver.createRelationship(Building.class, "building1"));
		cellularReport2.setPerson(testDriver.createRelationship(Person.class, "person1"));
		testDriver.submitEvent(cellularReport2);
		oneDayAgo = oneDayAgo.plusMinutes(10);
		
		CellularReport cellularReport3 = conceptFactory.createCellularReport(oneDayAgo);
		cellularReport3.setBuilding(testDriver.createRelationship(Building.class, "building1"));
		cellularReport3.setPerson(testDriver.createRelationship(Person.class, "person1"));
		testDriver.submitEvent(cellularReport3);
		oneDayAgo = oneDayAgo.plusMinutes(10);
		
		CellularReport cellularReport4 = conceptFactory.createCellularReport(oneDayAgo);
		cellularReport4.setBuilding(testDriver.createRelationship(Building.class, "building1"));
		cellularReport4.setPerson(testDriver.createRelationship(Person.class, "person1"));
		testDriver.submitEvent(cellularReport4);
		oneDayAgo = oneDayAgo.plusMinutes(10);
		
		CellularReport cellularReport5 = conceptFactory.createCellularReport(oneDayAgo);
		cellularReport5.setBuilding(testDriver.createRelationship(Building.class, "building1"));
		cellularReport5.setPerson(testDriver.createRelationship(Person.class, "person1"));
		testDriver.submitEvent(cellularReport5);
		oneDayAgo = oneDayAgo.plusMinutes(10);
		
		
		// 1 reports from building 2
		CellularReport cellularReport6 = conceptFactory.createCellularReport(oneDayAgo);
		cellularReport6.setBuilding(testDriver.createRelationship(Building.class, "building2"));
		cellularReport6.setPerson(testDriver.createRelationship(Person.class, "person1"));
		testDriver.submitEvent(cellularReport6);
		oneDayAgo = oneDayAgo.plusMinutes(10);
		
		// 1 reports from building 1
		CellularReport cellularReport7 = conceptFactory.createCellularReport(oneDayAgo);
		cellularReport7.setBuilding(testDriver.createRelationship(Building.class, "building1"));
		cellularReport7.setPerson(testDriver.createRelationship(Person.class, "person1"));
		testDriver.submitEvent(cellularReport7);
		oneDayAgo = oneDayAgo.plusMinutes(10);
	
		
		// 3 reports from building 2
		CellularReport cellularReport8 = conceptFactory.createCellularReport(oneDayAgo);
		cellularReport8.setBuilding(testDriver.createRelationship(Building.class, "building2"));
		cellularReport8.setPerson(testDriver.createRelationship(Person.class, "person1"));
		testDriver.submitEvent(cellularReport8);
		oneDayAgo = oneDayAgo.plusMinutes(10);
		
		CellularReport cellularReport9 = conceptFactory.createCellularReport(oneDayAgo);
		cellularReport9.setBuilding(testDriver.createRelationship(Building.class, "building2"));
		cellularReport9.setPerson(testDriver.createRelationship(Person.class, "person1"));
		testDriver.submitEvent(cellularReport9);
		oneDayAgo = oneDayAgo.plusMinutes(10);
		
		CellularReport cellularReport10 = conceptFactory.createCellularReport(oneDayAgo);
		cellularReport10.setBuilding(testDriver.createRelationship(Building.class, "building2"));
		cellularReport10.setPerson(testDriver.createRelationship(Person.class, "person1"));
		testDriver.submitEvent(cellularReport10);
		oneDayAgo = oneDayAgo.plusMinutes(10);
	
		
		// 1 reports from building 1	
		CellularReport cellularReport11 = conceptFactory.createCellularReport(oneDayAgo);
		cellularReport11.setBuilding(testDriver.createRelationship(Building.class, "building1"));
		cellularReport11.setPerson(testDriver.createRelationship(Person.class, "person1"));
		testDriver.submitEvent(cellularReport11);
		oneDayAgo = oneDayAgo.plusMinutes(10);
	
		
		
		Thread.sleep(3000);
		Building building1 = testDriver.fetchEntity(Building.class, "building1");
		Assert.assertNotNull(building1);
		
		Person person1 = testDriver.fetchEntity(Person.class, "person1");
		Assert.assertNotNull(person1);
		
		Assert.assertTrue(building1.getLocation().intersects(person1.getLocation()));

	}
}

package blur.tests;

import static org.junit.Assert.*;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

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
import blur.model.ConceptFactory;
import blur.model.CriminalCellularCallReport;
import blur.model.Organization;
import blur.model.OrganizationInitialization;
import blur.model.OrganizationRoleInitialization;
import blur.model.OrganizationRoleType;
import blur.model.OrganizationType;
import blur.model.OrganizationalRole;
import blur.model.Person;
import blur.model.PersonInitialization;
import blur.model.PersonState;
import blur.model.PotentialBankCrimeReport;

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

public class Rule_13111 {
	protected static TestDriver testDriver;
	protected static IADebugReceiver debugReceiver = new IADebugReceiver();

	private static final String ORGANIZATION_1_CRIMINAL = "ORGANIZATION_1_CRIMINAL";
	private static final String ORGANIZATION_2_CRIMINAL = "ORGANIZATION_2_CRIMINAL";
	private static final String ORGANIZATION_3_COMMERCIAL = "ORGANIZATION_3_COMMERCIAL";

	private static final String CALLER_CRIMINAL_ROLE = "CALLER_CRIMINAL_ROLE";
	private static final String CALLEE_CRIMINAL_ROLE = "CALLEE_CRIMINAL_ROLE";
	private static final String ANOTHER_CRIMINAL_ROLE_1 = "ANOTHER_CRIMINAL_ROLE_1";
	private static final String ANOTHER_CRIMINAL_ROLE_2 = "ANOTHER_CRIMINAL_ROLE_2";
	private static final String ANOTHER_NON_CIRMINAL_ROLE_1 = "ANOTHER_COMMERCIAL_ROLE_3";
	private static final String ANOTHER_NON_CIRMINAL_ROLE_2 = "ANOTHER_COMMERCIAL_ROLE_4";

	private static final String PERSON_CRIMINAL_CALLEE = "PERSON_CRIMINAL_CALLEE";
	private static final String PERSON_CRIMINAL_CALLER = "PERSON_CRIMINAL_CALLER";
	private static final String PERSON_ANOTHER_CRIMINAL_1 = "PERSON_ANOTHER_CRIMINAL_1";
	private static final String PERSON_ANOTHER_CRIMINAL_2 = "PERSON_ANOTHER_CRIMINAL_2";
	private static final String PERSON_ANOTHER_NON_CRIMINAL_1 = "PERSON_ANOTHER_NON_CRIMINAL_1";
	private static final String PERSON_ANOTHER_NON_CRIMINAL_2 = "PERSON_ANOTHER_NON_CRIMINAL_2";

	private static final String BUILDING_1_BANK = "BUILDING_1_BANK";
	private static final String BUILDING_2_BANK = "BUILDING_2_BANK";
	private static final String BUILDING_3_BANK = "BUILDING_3_BANK";

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
		OrganizationInitialization organizationInitialization1Criminal = conceptFactory.createOrganizationInitialization(now);
		organizationInitialization1Criminal.setType(OrganizationType.CRIMINAL);
		organizationInitialization1Criminal.setOrganization(testDriver.createRelationship(Organization.class, ORGANIZATION_1_CRIMINAL));
		testDriver.submitEvent(organizationInitialization1Criminal);

		OrganizationInitialization organizationInitialization2Criminal = conceptFactory.createOrganizationInitialization(now);
		organizationInitialization2Criminal.setType(OrganizationType.CRIMINAL);
		organizationInitialization2Criminal.setOrganization(testDriver.createRelationship(Organization.class, ORGANIZATION_2_CRIMINAL));
		testDriver.submitEvent(organizationInitialization2Criminal);

		OrganizationInitialization organizationInitialization3Commercial = conceptFactory.createOrganizationInitialization(now);
		organizationInitialization3Commercial.setType(OrganizationType.COMMERCIAL);
		organizationInitialization3Commercial.setOrganization(testDriver.createRelationship(Organization.class, ORGANIZATION_3_COMMERCIAL));
		testDriver.submitEvent(organizationInitialization3Commercial);

		// Organization Role Initialization		
		OrganizationRoleInitialization callerCriminalRoleInitialization = conceptFactory.createOrganizationRoleInitialization(now);
		callerCriminalRoleInitialization.setOrganizationalRole(testDriver.createRelationship(OrganizationalRole.class, CALLER_CRIMINAL_ROLE));
		callerCriminalRoleInitialization.setOrganization(testDriver.createRelationship(Organization.class, ORGANIZATION_1_CRIMINAL));
		callerCriminalRoleInitialization.setType(OrganizationRoleType.MANAGER);
		testDriver.submitEvent(callerCriminalRoleInitialization);

		OrganizationRoleInitialization calleCriminalRoleInitialization = conceptFactory.createOrganizationRoleInitialization(now);
		calleCriminalRoleInitialization.setOrganizationalRole(testDriver.createRelationship(OrganizationalRole.class, CALLEE_CRIMINAL_ROLE));
		calleCriminalRoleInitialization.setOrganization(testDriver.createRelationship(Organization.class, ORGANIZATION_1_CRIMINAL));
		calleCriminalRoleInitialization.setType(OrganizationRoleType.EMPLOYEE);
		testDriver.submitEvent(calleCriminalRoleInitialization);

		OrganizationRoleInitialization anotherCriminalRoleInitialization1 = conceptFactory.createOrganizationRoleInitialization(now);
		anotherCriminalRoleInitialization1.setOrganizationalRole(testDriver.createRelationship(OrganizationalRole.class, ANOTHER_CRIMINAL_ROLE_1));
		anotherCriminalRoleInitialization1.setOrganization(testDriver.createRelationship(Organization.class, ORGANIZATION_1_CRIMINAL));
		anotherCriminalRoleInitialization1.setType(OrganizationRoleType.EMPLOYEE);
		testDriver.submitEvent(anotherCriminalRoleInitialization1);

		OrganizationRoleInitialization anotherCriminalRoleInitialization2 = conceptFactory.createOrganizationRoleInitialization(now);
		anotherCriminalRoleInitialization2.setOrganizationalRole(testDriver.createRelationship(OrganizationalRole.class, ANOTHER_CRIMINAL_ROLE_2));
		anotherCriminalRoleInitialization2.setOrganization(testDriver.createRelationship(Organization.class, ORGANIZATION_1_CRIMINAL));
		anotherCriminalRoleInitialization2.setType(OrganizationRoleType.EMPLOYEE);
		testDriver.submitEvent(anotherCriminalRoleInitialization2);

		OrganizationRoleInitialization anotherNonCriminalRoleInitialization1 = conceptFactory.createOrganizationRoleInitialization(now);
		anotherNonCriminalRoleInitialization1.setOrganizationalRole(testDriver.createRelationship(OrganizationalRole.class, ANOTHER_NON_CIRMINAL_ROLE_1));
		anotherNonCriminalRoleInitialization1.setOrganization(testDriver.createRelationship(Organization.class, ORGANIZATION_3_COMMERCIAL));
		anotherNonCriminalRoleInitialization1.setType(OrganizationRoleType.EMPLOYEE);
		testDriver.submitEvent(anotherNonCriminalRoleInitialization1);

		OrganizationRoleInitialization anotherNonCriminalRoleInitialization2 = conceptFactory.createOrganizationRoleInitialization(now);
		anotherNonCriminalRoleInitialization2.setOrganizationalRole(testDriver.createRelationship(OrganizationalRole.class, ANOTHER_NON_CIRMINAL_ROLE_2));
		anotherNonCriminalRoleInitialization2.setOrganization(testDriver.createRelationship(Organization.class, ORGANIZATION_3_COMMERCIAL));
		anotherNonCriminalRoleInitialization2.setType(OrganizationRoleType.EMPLOYEE);
		testDriver.submitEvent(anotherNonCriminalRoleInitialization2);

		//Point location = null;

		// Building Initialization
		BuildingInitialization buildingInitialization1 = conceptFactory.createBuildingInitialization(now);
		buildingInitialization1.setBuilding(testDriver.createRelationship(Building.class, BUILDING_1_BANK));
		buildingInitialization1.setUsageType(BuildingUsageType.BANK_BRANCH);
		buildingInitialization1.setType(BuildingType.APPARTMENT);
		Point buildingInitialization1_location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		buildingInitialization1.setLocation(buildingInitialization1_location);
		buildingInitialization1.setOwner(testDriver.createRelationship(Person.class, "123"));
		buildingInitialization1.addTo_organizations(testDriver.createRelationship(Organization.class, ORGANIZATION_3_COMMERCIAL));
		testDriver.submitEvent(buildingInitialization1);

		BuildingInitialization buildingInitialization2 = conceptFactory.createBuildingInitialization(now);
		buildingInitialization2.setBuilding(testDriver.createRelationship(Building.class, BUILDING_2_BANK));
		buildingInitialization2.setUsageType(BuildingUsageType.BANK_BRANCH);
		buildingInitialization2.setType(BuildingType.WAREHOUSE);
		Point buildingInitialization2_location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		buildingInitialization2.setLocation(buildingInitialization2_location);
		buildingInitialization2.setOwner(testDriver.createRelationship(Person.class, "123"));
		buildingInitialization2.addTo_organizations(testDriver.createRelationship(Organization.class, ORGANIZATION_3_COMMERCIAL));
		testDriver.submitEvent(buildingInitialization2);

		BuildingInitialization buildingInitialization3 = conceptFactory.createBuildingInitialization(now);
		buildingInitialization3.setBuilding(testDriver.createRelationship(Building.class, BUILDING_3_BANK));
		buildingInitialization3.setUsageType(BuildingUsageType.BANK_BRANCH);
		buildingInitialization3.setType(BuildingType.COMMERCIAL);
		Point buildingInitialization3_location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		buildingInitialization3.setLocation(buildingInitialization3_location);
		buildingInitialization3.setOwner(testDriver.createRelationship(Person.class, "123"));
		buildingInitialization3.addTo_organizations(testDriver.createRelationship(Organization.class, ORGANIZATION_3_COMMERCIAL));
		testDriver.submitEvent(buildingInitialization3);

		// Person Initialization
		// callee
		PersonInitialization calleeInitialization = conceptFactory.createPersonInitialization(now);
		calleeInitialization.setPerson(testDriver.createRelationship(Person.class, PERSON_CRIMINAL_CALLEE));
		calleeInitialization.setName(PERSON_CRIMINAL_CALLEE);
		calleeInitialization.setProfession(PERSON_CRIMINAL_CALLEE + "s Profession");
		calleeInitialization.setRole(testDriver.createRelationship(OrganizationalRole.class, CALLEE_CRIMINAL_ROLE));
		calleeInitialization.setState(PersonState.ACTIVE);
		calleeInitialization.setLocation(buildingInitialization1_location);
		testDriver.submitEvent(calleeInitialization);

		// caller
		PersonInitialization callerInitialization = conceptFactory.createPersonInitialization(now);
		callerInitialization.setPerson(testDriver.createRelationship(Person.class, PERSON_CRIMINAL_CALLER));
		callerInitialization.setName(PERSON_CRIMINAL_CALLER);
		callerInitialization.setProfession(PERSON_CRIMINAL_CALLER + "s Profession");
		callerInitialization.setRole(testDriver.createRelationship(OrganizationalRole.class, CALLER_CRIMINAL_ROLE));
		callerInitialization.setState(PersonState.ACTIVE);
		callerInitialization.setLocation(buildingInitialization1_location);
		testDriver.submitEvent(callerInitialization);

		// criminal 1
		PersonInitialization anotherCriminalPersonInitialization1 = conceptFactory.createPersonInitialization(now);
		anotherCriminalPersonInitialization1.setPerson(testDriver.createRelationship(Person.class, PERSON_ANOTHER_CRIMINAL_1));
		anotherCriminalPersonInitialization1.setName(PERSON_ANOTHER_CRIMINAL_1);
		anotherCriminalPersonInitialization1.setProfession(PERSON_ANOTHER_CRIMINAL_1 + "s Profession");
		anotherCriminalPersonInitialization1.setRole(testDriver.createRelationship(OrganizationalRole.class, ANOTHER_CRIMINAL_ROLE_1));
		anotherCriminalPersonInitialization1.setState(PersonState.ACTIVE);
		anotherCriminalPersonInitialization1.setLocation(buildingInitialization1_location);
		testDriver.submitEvent(anotherCriminalPersonInitialization1);

		// criminal 2
		PersonInitialization anotherCriminalPersonInitialization2 = conceptFactory.createPersonInitialization(now);
		anotherCriminalPersonInitialization2.setPerson(testDriver.createRelationship(Person.class, PERSON_ANOTHER_CRIMINAL_2));
		anotherCriminalPersonInitialization2.setName(PERSON_ANOTHER_CRIMINAL_2);
		anotherCriminalPersonInitialization2.setProfession(PERSON_ANOTHER_CRIMINAL_2 + "s Profession");
		anotherCriminalPersonInitialization2.setRole(testDriver.createRelationship(OrganizationalRole.class, ANOTHER_CRIMINAL_ROLE_2));
		anotherCriminalPersonInitialization2.setState(PersonState.ACTIVE);
		anotherCriminalPersonInitialization2.setLocation(buildingInitialization1_location);
		testDriver.submitEvent(anotherCriminalPersonInitialization2);

		// non criminal 1
		PersonInitialization anotherNonCriminalPersonInitialization1 = conceptFactory.createPersonInitialization(now);
		anotherNonCriminalPersonInitialization1.setPerson(testDriver.createRelationship(Person.class, PERSON_ANOTHER_NON_CRIMINAL_1));
		anotherNonCriminalPersonInitialization1.setName(PERSON_ANOTHER_NON_CRIMINAL_1);
		anotherNonCriminalPersonInitialization1.setProfession(PERSON_ANOTHER_NON_CRIMINAL_1 + "s Profession");
		anotherNonCriminalPersonInitialization1.setRole(testDriver.createRelationship(OrganizationalRole.class, ANOTHER_NON_CIRMINAL_ROLE_1));
		anotherNonCriminalPersonInitialization1.setState(PersonState.ACTIVE);
		anotherNonCriminalPersonInitialization1.setLocation(buildingInitialization2_location);
		testDriver.submitEvent(anotherNonCriminalPersonInitialization1);

		// non criminal 2
		PersonInitialization anotherNonCriminalPersonInitialization2 = conceptFactory.createPersonInitialization(now);
		anotherNonCriminalPersonInitialization2.setPerson(testDriver.createRelationship(Person.class, PERSON_ANOTHER_NON_CRIMINAL_2));
		anotherNonCriminalPersonInitialization2.setName(PERSON_ANOTHER_NON_CRIMINAL_2);
		anotherNonCriminalPersonInitialization2.setProfession(PERSON_ANOTHER_NON_CRIMINAL_2 + "s Profession");
		anotherNonCriminalPersonInitialization2.setRole(testDriver.createRelationship(OrganizationalRole.class, ANOTHER_NON_CIRMINAL_ROLE_2));
		anotherNonCriminalPersonInitialization2.setState(PersonState.ACTIVE);
		anotherNonCriminalPersonInitialization2.setLocation(buildingInitialization3_location);
		testDriver.submitEvent(anotherNonCriminalPersonInitialization2);

		// allow the server time to create the entities
		testDriver.waitUntilSolutionIdle();

		/*		Building building1 = testDriver.fetchEntity(Building.class, "building1");
		Assert.assertNotNull(building1);
		Building building2 = testDriver.fetchEntity(Building.class, "building2");
		Assert.assertNotNull(building2);		
		Building building3 = testDriver.fetchEntity(Building.class, "building3");
		Assert.assertNotNull(building3);
		 */

		Building building1 = testDriver.fetchEntity(Building.class, BUILDING_1_BANK);
		Assert.assertNotNull(building1);
		Building building2 = testDriver.fetchEntity(Building.class, BUILDING_2_BANK);
		Assert.assertNotNull(building2);		
		Building building3 = testDriver.fetchEntity(Building.class, BUILDING_3_BANK);
		Assert.assertNotNull(building3);

		Person person1 = testDriver.fetchEntity(Person.class, PERSON_CRIMINAL_CALLEE);
		Assert.assertNotNull(person1);
		Person person2 = testDriver.fetchEntity(Person.class, PERSON_CRIMINAL_CALLER);
		Assert.assertNotNull(person2);
		Person person3 = testDriver.fetchEntity(Person.class, PERSON_ANOTHER_CRIMINAL_1);
		Assert.assertNotNull(person3);
		Person person4 = testDriver.fetchEntity(Person.class, PERSON_ANOTHER_CRIMINAL_2);
		Assert.assertNotNull(person4);
		Person person5 = testDriver.fetchEntity(Person.class, PERSON_ANOTHER_NON_CRIMINAL_1);
		Assert.assertNotNull(person5);
		Person person6 = testDriver.fetchEntity(Person.class, PERSON_ANOTHER_NON_CRIMINAL_2);
		Assert.assertNotNull(person6);

		//

		ZonedDateTime oneDayAfter = now.plusDays(1);

//		// fires
//		CellularCallReport cellularCallReport1 = conceptFactory.createCellularCallReport(oneDayAfter);
//
//		List<Relationship<Person>> criminal_persons1 = new ArrayList<Relationship<Person>>();
//		criminal_persons1.add(testDriver.createRelationship(Person.class, PERSON_ANOTHER_CRIMINAL_1));
//		criminal_persons1.add(testDriver.createRelationship(Person.class, PERSON_ANOTHER_CRIMINAL_2));
//		cellularCallReport1.set_persons(criminal_persons1);
//
//		List<Relationship<Building>> buildings1 = new ArrayList<Relationship<Building>>();
//		buildings1.add(testDriver.createRelationship(Building.class, BUILDING_1_BANK));
//		buildings1.add(testDriver.createRelationship(Building.class, BUILDING_2_BANK));
//		buildings1.add(testDriver.createRelationship(Building.class, BUILDING_3_BANK));
//		cellularCallReport1.set_buildings(buildings1);
//
//		cellularCallReport1.setCallee(testDriver.createRelationship(Person.class, PERSON_CRIMINAL_CALLEE));
//		cellularCallReport1.setCaller(testDriver.createRelationship(Person.class, PERSON_CRIMINAL_CALLER));
//
//		testDriver.submitEvent(cellularCallReport1);
		
//		// callee and caller are switched
//		CellularCallReport cellularCallReport2 = conceptFactory.createCellularCallReport(oneDayAfter);
//
//		List<Relationship<Person>> criminal_persons2 = new ArrayList<Relationship<Person>>();
//		criminal_persons2.add(testDriver.createRelationship(Person.class, PERSON_ANOTHER_CRIMINAL_1));
//		criminal_persons2.add(testDriver.createRelationship(Person.class, PERSON_ANOTHER_CRIMINAL_2));
//		cellularCallReport2.set_persons(criminal_persons2);
//
//		List<Relationship<Building>> buildings2 = new ArrayList<Relationship<Building>>();
//		buildings2.add(testDriver.createRelationship(Building.class, BUILDING_1_BANK));
//		buildings2.add(testDriver.createRelationship(Building.class, BUILDING_2_BANK));
//		buildings2.add(testDriver.createRelationship(Building.class, BUILDING_3_BANK));
//		cellularCallReport2.set_buildings(buildings2);
//
//		cellularCallReport2.setCallee(testDriver.createRelationship(Person.class, PERSON_CRIMINAL_CALLER));
//		cellularCallReport2.setCaller(testDriver.createRelationship(Person.class, PERSON_CRIMINAL_CALLEE));
//
//		testDriver.submitEvent(cellularCallReport2);
		
//		// won't fire (only one person is there)
//		CellularCallReport cellularCallReport3 = conceptFactory.createCellularCallReport(oneDayAfter);
//
//		List<Relationship<Person>> criminal_persons2 = new ArrayList<Relationship<Person>>();
//		criminal_persons2.add(testDriver.createRelationship(Person.class, PERSON_ANOTHER_CRIMINAL_1));
//		criminal_persons2.add(testDriver.createRelationship(Person.class, PERSON_ANOTHER_CRIMINAL_2));
//		cellularCallReport3.set_persons(criminal_persons2);
//
//		List<Relationship<Building>> buildings3 = new ArrayList<Relationship<Building>>();
//		buildings3.add(testDriver.createRelationship(Building.class, BUILDING_1_BANK));
//		buildings3.add(testDriver.createRelationship(Building.class, BUILDING_2_BANK));
//		buildings3.add(testDriver.createRelationship(Building.class, BUILDING_3_BANK));
//		cellularCallReport3.set_buildings(buildings3);
//
//		cellularCallReport3.setCallee(testDriver.createRelationship(Person.class, PERSON_ANOTHER_NON_CRIMINAL_1));
//		cellularCallReport3.setCaller(testDriver.createRelationship(Person.class, PERSON_CRIMINAL_CALLER));
//
//		testDriver.submitEvent(cellularCallReport3);
		
		// won't fire (only one person is there)
		CellularCallReport cellularCallReport4 = conceptFactory.createCellularCallReport(oneDayAfter);

		List<Relationship<Person>> criminal_persons4 = new ArrayList<Relationship<Person>>();
		criminal_persons4.add(testDriver.createRelationship(Person.class, PERSON_ANOTHER_CRIMINAL_1));
		criminal_persons4.add(testDriver.createRelationship(Person.class, PERSON_ANOTHER_CRIMINAL_2));
		cellularCallReport4.set_persons(criminal_persons4);

		List<Relationship<Building>> buildings4 = new ArrayList<Relationship<Building>>();
		buildings4.add(testDriver.createRelationship(Building.class, BUILDING_1_BANK));
		buildings4.add(testDriver.createRelationship(Building.class, BUILDING_2_BANK));
		buildings4.add(testDriver.createRelationship(Building.class, BUILDING_3_BANK));
		cellularCallReport4.set_buildings(buildings4);

		cellularCallReport4.setCallee(testDriver.createRelationship(Person.class, PERSON_ANOTHER_NON_CRIMINAL_1));
		cellularCallReport4.setCaller(testDriver.createRelationship(Person.class, PERSON_CRIMINAL_CALLER));

		testDriver.submitEvent(cellularCallReport4);

		testDriver.waitUntilSolutionIdle();

		System.out.println("********************************************************************");
		DebugInfo[] debugInfos = debugReceiver.getDebugInfo("BuildingRuleAgent");
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
		Thread.sleep(5000);		
	}
}
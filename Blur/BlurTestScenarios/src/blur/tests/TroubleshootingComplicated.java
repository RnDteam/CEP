package blur.tests;

import java.time.ZoneId;
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
import blur.model.CellularCallReport;
import blur.model.CellularReport;
import blur.model.ConceptFactory;
import blur.model.Organization;
import blur.model.OrganizationType;
import blur.model.OrganizationalRole;
import blur.model.Person;

import com.ibm.ia.common.DataFormat;
import com.ibm.ia.common.GatewayException;
import com.ibm.ia.common.RoutingException;
import com.ibm.ia.common.SolutionException;
import com.ibm.ia.common.debug.DebugInfo;
import com.ibm.ia.model.Event;
import com.ibm.ia.model.Relationship;
import com.ibm.ia.testdriver.IADebugReceiver;
import com.ibm.ia.testdriver.TestDriver;

public class TroubleshootingComplicated {

	protected static TestDriver testDriver;
	protected static IADebugReceiver debugReceiver = new IADebugReceiver();
	
	private static String CRIMINAL_ORGANIZATION = "CRIMINAL_ORGANIZATION";
	private static String COMMERCIAL_ORGANIZATION = "COMMERCIAL_ORGANIZATION";
	private static String CRIMINAL_ORGANIZATION_ROLE = "CRIMINAL_ORGANIZATION_ROLE";
	private static String NON_CRIMINAL_ORGANIZATION_ROLE = "NON_CRIMINAL_ORGANIZATION_ROLE";
	private static String POTENTIAL_COLLABORATOR = "CRIMINAL_PERSON";
	private static String SUSPECT_PERSON = "NON_CRIMINAL_PERSON";
	
	private static final String BUILDING_1_BANK = "BUILDING_1_BANK";
	private static final String BUILDING_2_BANK = "BUILDING_2_BANK";
	private static final String BUILDING_3_BANK = "BUILDING_3_BANK";
	
	private static final String PERSON_82 = "ppl-Txe-82"; // good guy
	private static final String PERSON_271 = "ppl-Txm-271"; // good guy
	private static final String PERSON_152 = "ppl-Txe-152"; // good guy
	private static final String PERSON_182 = "ppl-Txe-182"; // good guy
	
	private static final String BUILDING_18 = "bld-Tx-18"; // criminal from the beginning
	private static final String BUILDING_32 = "bld-Tx-32"; // criminal from the beginning
	private static final String BUILDING_52 = "bld-To-52"; // not criminal from the beginning
	private static final String BUILDING_80 = "bld-To-80"; // not criminal from the beginning

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
//		debugReceiver.clearDebugInfo();
//		testDriver.deleteAllEntities();
//		testDriver.resetSolutionState();
//		testDriver.startRecording();
	}

	@After
	public void tearDown() throws Exception {
//		testDriver.endTest();
//		Thread.sleep(5000);
//		testDriver.stopRecording();
	}
	
	@Test
	public void Test_Only8_5Hours()  throws SolutionException, GatewayException, RoutingException, InterruptedException {
		ConceptFactory conceptFactory = testDriver.getConceptFactory(ConceptFactory.class);
		
		ZonedDateTime absoluteTime = ZonedDateTime.of(2016, 1, 1, 0, 0, 0, 0, ZoneId.systemDefault());
		
//		double initialPoint0 = 30.0;
//		double initialPoint1 = 30.0;
//
//		// Organization Initialization
//		OrganizationInitialization criminalOrganization1 = conceptFactory.createOrganizationInitialization(oneDayAgo);
//		criminalOrganization1.setType(OrganizationType.CRIMINAL);
//		criminalOrganization1.setOrganization(testDriver.createRelationship(Organization.class, CRIMINAL_ORGANIZATION));
//		testDriver.submitEvent(criminalOrganization1);
//		
//		OrganizationInitialization commercialOrganization1 = conceptFactory.createOrganizationInitialization(oneDayAgo);
//		commercialOrganization1.setType(OrganizationType.COMMERCIAL);
//		commercialOrganization1.setOrganization(testDriver.createRelationship(Organization.class, COMMERCIAL_ORGANIZATION));
//		testDriver.submitEvent(commercialOrganization1);
//		
//		// Organization role Initialization
//		OrganizationRoleInitialization criminalOrganizationRole1 = conceptFactory.createOrganizationRoleInitialization(oneDayAgo);
//		criminalOrganizationRole1.setOrganizationalRole(testDriver.createRelationship(OrganizationalRole.class, CRIMINAL_ORGANIZATION_ROLE));
//		criminalOrganizationRole1.setOrganization(testDriver.createRelationship(Organization.class, CRIMINAL_ORGANIZATION));
//		testDriver.submitEvent(criminalOrganizationRole1);
//			
//		OrganizationRoleInitialization noncriminalOrganizationRole1 = conceptFactory.createOrganizationRoleInitialization(oneDayAgo);
//		noncriminalOrganizationRole1.setOrganizationalRole(testDriver.createRelationship(OrganizationalRole.class, NON_CRIMINAL_ORGANIZATION_ROLE));
//		noncriminalOrganizationRole1.setOrganization(testDriver.createRelationship(Organization.class, COMMERCIAL_ORGANIZATION));
//		testDriver.submitEvent(noncriminalOrganizationRole1);
//		
//		Point defaultLocation = SpatioTemporalService.getService().getGeometryFactory().getPoint(initialPoint0, initialPoint1);
//		
//		// Person Initialization
//		PersonInitialization PERSON_82_initialization = conceptFactory.createPersonInitialization(oneDayAgo);
//		PERSON_82_initialization.setPerson(testDriver.createRelationship(Person.class, PERSON_82));
//		PERSON_82_initialization.setName("PERSON_82");
//		PERSON_82_initialization.setProfession("PERSON_82 Profession");
//		PERSON_82_initialization.setRole(testDriver.createRelationship(OrganizationalRole.class, NON_CRIMINAL_ORGANIZATION_ROLE));
//		PERSON_82_initialization.setState(PersonState.ACTIVE);
//		Point PERSON_82_location = defaultLocation;
//		PERSON_82_initialization.setLocation(PERSON_82_location);
//		testDriver.submitEvent(PERSON_82_initialization);
//		
//		PersonInitialization PERSON_271_initialization = conceptFactory.createPersonInitialization(oneDayAgo);
//		PERSON_271_initialization.setPerson(testDriver.createRelationship(Person.class, PERSON_271));
//		PERSON_271_initialization.setName(PERSON_271);
//		PERSON_271_initialization.setProfession(PERSON_271 + " Profession");
//		PERSON_271_initialization.setRole(testDriver.createRelationship(OrganizationalRole.class, NON_CRIMINAL_ORGANIZATION_ROLE));
//		PERSON_271_initialization.setState(PersonState.ACTIVE);
//		Point PERSON_271_location = defaultLocation;
//		PERSON_271_initialization.setLocation(PERSON_271_location);
//		testDriver.submitEvent(PERSON_271_initialization);
//		
//		PersonInitialization PERSON_152_initialization = conceptFactory.createPersonInitialization(oneDayAgo);
//		PERSON_152_initialization.setPerson(testDriver.createRelationship(Person.class, PERSON_152));
//		PERSON_152_initialization.setName(PERSON_152);
//		PERSON_152_initialization.setProfession(PERSON_152 + " Profession");
//		PERSON_152_initialization.setRole(testDriver.createRelationship(OrganizationalRole.class, NON_CRIMINAL_ORGANIZATION_ROLE));
//		PERSON_152_initialization.setState(PersonState.ACTIVE);
//		Point PERSON_152_location = defaultLocation;
//		PERSON_152_initialization.setLocation(PERSON_152_location);
//		testDriver.submitEvent(PERSON_152_initialization);
//		
//		PersonInitialization PERSON_182_initialization = conceptFactory.createPersonInitialization(oneDayAgo);
//		PERSON_182_initialization.setPerson(testDriver.createRelationship(Person.class, PERSON_182));
//		PERSON_182_initialization.setName(PERSON_182);
//		PERSON_182_initialization.setProfession(PERSON_182 + " Profession");
//		PERSON_182_initialization.setRole(testDriver.createRelationship(OrganizationalRole.class, NON_CRIMINAL_ORGANIZATION_ROLE));
//		PERSON_182_initialization.setState(PersonState.ACTIVE);
//		Point PERSON_182_location = defaultLocation;
//		PERSON_182_initialization.setLocation(PERSON_182_location);
//		testDriver.submitEvent(PERSON_182_initialization);
//		
//		// Building Initialization
//		BuildingInitialization BANK_18_initialization = conceptFactory.createBuildingInitialization(oneDayAgo);
//		BANK_18_initialization.setBuilding(testDriver.createRelationship(Building.class, BANK_18));
//		BANK_18_initialization.setUsageType(BuildingUsageType.BANK_BRANCH);
//		BANK_18_initialization.setType(BuildingType.WAREHOUSE);
//		Point BANK_18_location = SpatioTemporalService.getService().getGeometryFactory().getPoint(34.7967, 32.0695);
//		BANK_18_initialization.setLocation(BANK_18_location);
//		BANK_18_initialization.setOwner(testDriver.createRelationship(Person.class, "ppl-Txe-82"));
//		BANK_18_initialization.addTo_organizations(testDriver.createRelationship(Organization.class, CRIMINAL_ORGANIZATION));
//		testDriver.submitEvent(BANK_18_initialization);
//
//		BuildingInitialization BANK_32_initialization = conceptFactory.createBuildingInitialization(oneDayAgo);
//		BANK_32_initialization.setBuilding(testDriver.createRelationship(Building.class, BANK_32));
//		BANK_32_initialization.setUsageType(BuildingUsageType.BANK_BRANCH);
//		BANK_32_initialization.setType(BuildingType.COMMERCIAL);
//		Point BANK_32_location = SpatioTemporalService.getService().getGeometryFactory().getPoint(34.7947, 32.0915);
//		BANK_32_initialization.setLocation(BANK_32_location);
//		BANK_32_initialization.setOwner(testDriver.createRelationship(Person.class, "ppl-Txe-152"));
//		BANK_32_initialization.addTo_organizations(testDriver.createRelationship(Organization.class, CRIMINAL_ORGANIZATION));
//		testDriver.submitEvent(BANK_32_initialization);
//
//		BuildingInitialization BBANK_52_initialization = conceptFactory.createBuildingInitialization(oneDayAgo);
//		BBANK_52_initialization.setBuilding(testDriver.createRelationship(Building.class, BANK_52));
//		BBANK_52_initialization.setUsageType(BuildingUsageType.BANK_BRANCH);
//		BBANK_52_initialization.setType(BuildingType.APPARTMENT);
//		Point BANK_52_location = SpatioTemporalService.getService().getGeometryFactory().getPoint(34.7857, 32.0895);
//		BBANK_52_initialization.setLocation(BANK_52_location);
//		BBANK_52_initialization.setOwner(testDriver.createRelationship(Person.class, "ppl-To-72"));
//		BBANK_52_initialization.addTo_organizations(testDriver.createRelationship(Organization.class, COMMERCIAL_ORGANIZATION));
//		testDriver.submitEvent(BBANK_52_initialization);
//		
//		BuildingInitialization BANK_80_initialization = conceptFactory.createBuildingInitialization(oneDayAgo);
//		BANK_80_initialization.setBuilding(testDriver.createRelationship(Building.class, BANK_80));
//		BANK_80_initialization.setUsageType(BuildingUsageType.BANK_BRANCH);
//		BANK_80_initialization.setType(BuildingType.APPARTMENT);
//		Point BANK_80_location = SpatioTemporalService.getService().getGeometryFactory().getPoint(34.7747, 32.0695);
//		BANK_80_initialization.setLocation(BANK_80_location);
//		BANK_80_initialization.setOwner(testDriver.createRelationship(Person.class, "ppl-To-32"));
//		BANK_80_initialization.addTo_organizations(testDriver.createRelationship(Organization.class, COMMERCIAL_ORGANIZATION));
//		testDriver.submitEvent(BANK_80_initialization);
//		
//		testDriver.waitUntilSolutionIdle();
		
		List<Relationship<Person>> persons = new ArrayList<Relationship<Person>>();
		persons.add(testDriver.createRelationship(Person.class, PERSON_82));
		persons.add(testDriver.createRelationship(Person.class, PERSON_152));
		persons.add(testDriver.createRelationship(Person.class, PERSON_182));
		persons.add(testDriver.createRelationship(Person.class, PERSON_271));
		
		List<Relationship<Building>> buildings = new ArrayList<Relationship<Building>>();
		buildings.add(testDriver.createRelationship(Building.class, BUILDING_18));
		buildings.add(testDriver.createRelationship(Building.class, BUILDING_32));
		buildings.add(testDriver.createRelationship(Building.class, BUILDING_52));
		buildings.add(testDriver.createRelationship(Building.class, BUILDING_80));
			
		CellularCallReport call_at_10 = conceptFactory.createCellularCallReport(absoluteTime.plusMinutes(10));
		call_at_10.setCaller(testDriver.createRelationship(Person.class, PERSON_82));
		call_at_10.setCallee(testDriver.createRelationship(Person.class, PERSON_271));
		call_at_10.set_persons(persons);
		call_at_10.set_buildings(buildings);
		testDriver.submitEvent(call_at_10);
		
		CellularReport cell_at_15_for_person_82_and_building_18 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(15));
		cell_at_15_for_person_82_and_building_18.setPerson(testDriver.createRelationship(Person.class, PERSON_82));
		cell_at_15_for_person_82_and_building_18.setBuilding((testDriver.createRelationship(Building.class, BUILDING_18)));
		testDriver.submitEvent(cell_at_15_for_person_82_and_building_18);
		
		CellularReport cell_at_15_for_person_152_and_building_32 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(15));
		cell_at_15_for_person_152_and_building_32.setPerson(testDriver.createRelationship(Person.class, PERSON_152));
		cell_at_15_for_person_152_and_building_32.setBuilding((testDriver.createRelationship(Building.class, BUILDING_32)));
		testDriver.submitEvent(cell_at_15_for_person_152_and_building_32);
		
//		CellularReport cell_at_20_for_person_271_and_building_80 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(20));
//		cell_at_20_for_person_271_and_building_80.setPerson(testDriver.createRelationship(Person.class, PERSON_271));
//		cell_at_20_for_person_271_and_building_80.setBuilding((testDriver.createRelationship(Building.class, BUILDING_80)));
//		testDriver.submitEvent(cell_at_20_for_person_271_and_building_80);
		
		testDriver.waitUntilSolutionIdle();
		
		// check
		
		Person person_82 = testDriver.fetchEntity(Person.class, PERSON_82);
		Assert.assertNotNull(person_82);
		Relationship<OrganizationalRole> person_82_role_relation = person_82.getRole();
		Assert.assertNotNull(person_82_role_relation);
		OrganizationalRole person_82_organization_role = testDriver.fetchEntity(OrganizationalRole.class, person_82_role_relation.getKey());
		Assert.assertNotNull(person_82_organization_role);
		Organization person_82_organization = testDriver.fetchEntity(Organization.class, person_82_organization_role.getOrganization().getKey());
		Assert.assertNotNull(person_82_organization);
		Assert.assertTrue(OrganizationType.CRIMINAL == person_82_organization.getType());
		
		Person person_152 = testDriver.fetchEntity(Person.class, PERSON_152);
		Assert.assertNotNull(person_152);
		Relationship<OrganizationalRole> person_152_relation = person_152.getRole();
		Assert.assertNotNull(person_152_relation);
		OrganizationalRole person_152_organization_role = testDriver.fetchEntity(OrganizationalRole.class, person_152_relation.getKey());
		Assert.assertNotNull(person_152_organization_role);
		Organization person_152_organization = testDriver.fetchEntity(Organization.class, person_152_organization_role.getOrganization().getKey());
		Assert.assertNotNull(person_152_organization);
		Assert.assertTrue(OrganizationType.CRIMINAL == person_152_organization.getType());
		
		// check
		
		
		
		
		DebugInfo[] debugInfos = debugReceiver.getDebugInfo("PersonRuleAgent");
		
		for (DebugInfo debugInfo : debugInfos) {
			System.out.println( "DebugInfo: " + debugInfo );
			
			Event event = debugInfo.getEvent();
			String eventXml = testDriver.getModelSerializer().serializeEvent(DataFormat.TYPED_XML, event );
			System.out.println( "Event as XML: " + eventXml );
		}
	}
	
	
//	
//	@Test
//	public void Test_Only7_5Hours()  throws SolutionException, GatewayException, RoutingException, InterruptedException {
//		ConceptFactory conceptFactory = testDriver.getConceptFactory(ConceptFactory.class);
//		ZonedDateTime now = ZonedDateTime.now();
//		ZonedDateTime oneDayAgo = now.minusDays(1);
//		
//		double initialPoint0 = 34.781768;
//		double initialPoint1 = 32.085300;
//
//		// Organization Initialization
//		OrganizationInitialization criminalOrganization1 = conceptFactory.createOrganizationInitialization(oneDayAgo);
//		criminalOrganization1.setType(OrganizationType.CRIMINAL);
//		criminalOrganization1.setOrganization(testDriver.createRelationship(Organization.class, CRIMINAL_ORGANIZATION));
//		
//		OrganizationInitialization commercialOrganization1 = conceptFactory.createOrganizationInitialization(oneDayAgo);
//		commercialOrganization1.setType(OrganizationType.COMMERCIAL);
//		commercialOrganization1.setOrganization(testDriver.createRelationship(Organization.class, COMMERCIAL_ORGANIZATION));
//		
//		// Organization role Initialization		
//		OrganizationRoleInitialization criminalOrganizationRole1 = conceptFactory.createOrganizationRoleInitialization(oneDayAgo);
//		criminalOrganizationRole1.setOrganizationalRole(testDriver.createRelationship(OrganizationalRole.class, CRIMINAL_ORGANIZATION_ROLE));
//		criminalOrganizationRole1.setOrganization(testDriver.createRelationship(Organization.class, CRIMINAL_ORGANIZATION));
//			
//		OrganizationRoleInitialization commercialOrganizationRole1 = conceptFactory.createOrganizationRoleInitialization(oneDayAgo);
//		commercialOrganizationRole1.setOrganizationalRole(testDriver.createRelationship(OrganizationalRole.class, NON_CRIMINAL_ORGANIZATION_ROLE));
//		commercialOrganizationRole1.setOrganization(testDriver.createRelationship(Organization.class, COMMERCIAL_ORGANIZATION));
//		
//		// Person Initialization
//		PersonInitialization criminalPersonInitialization1 = conceptFactory.createPersonInitialization(oneDayAgo);
//		criminalPersonInitialization1.setPerson(testDriver.createRelationship(Person.class, POTENTIAL_COLLABORATOR));
//		criminalPersonInitialization1.setName("a1");
//		criminalPersonInitialization1.setProfession("Potential");
//		criminalPersonInitialization1.setRole(testDriver.createRelationship(OrganizationalRole.class, CRIMINAL_ORGANIZATION_ROLE));
//		criminalPersonInitialization1.setState(PersonState.ACTIVE);
//		Point suspectLocation = SpatioTemporalService.getService().getGeometryFactory().getPoint(initialPoint0 + Math.random(), initialPoint1 + Math.random());
//		criminalPersonInitialization1.setLocation(suspectLocation);
//		
//		PersonInitialization nonCriminalPersonInitialization1 = conceptFactory.createPersonInitialization(oneDayAgo);
//		nonCriminalPersonInitialization1.setPerson(testDriver.createRelationship(Person.class, SUSPECT_PERSON));
//		nonCriminalPersonInitialization1.setName("b2");
//		nonCriminalPersonInitialization1.setProfession("Suspect");
//		nonCriminalPersonInitialization1.setRole(testDriver.createRelationship(OrganizationalRole.class, NON_CRIMINAL_ORGANIZATION_ROLE));
//		nonCriminalPersonInitialization1.setState(PersonState.ACTIVE);
//		Point potentialCollaboratorLocation = SpatioTemporalService.getService().getGeometryFactory().getPoint(initialPoint0 + Math.random(), initialPoint1 + Math.random());
//		nonCriminalPersonInitialization1.setLocation(potentialCollaboratorLocation);
//	
//		// Create Entities - Submit Events
//		testDriver.submitEvent(criminalOrganization1);
//		testDriver.submitEvent(commercialOrganization1);
//		testDriver.submitEvent(criminalOrganizationRole1);
//		testDriver.submitEvent(commercialOrganizationRole1);
//		testDriver.submitEvent(criminalPersonInitialization1);
//		testDriver.submitEvent(nonCriminalPersonInitialization1);
//		testDriver.waitUntilSolutionIdle();
//		
//		Person suspectPerson1 = testDriver.fetchEntity(Person.class, SUSPECT_PERSON);
//		Person potentialCollaborator = testDriver.fetchEntity(Person.class, POTENTIAL_COLLABORATOR);
//		MovingGeometry potentialCollaboratorMovingGeometry = potentialCollaborator.getLocation();
//		MovingGeometry suspectPersonMovingGeometry = suspectPerson1.getLocation();
//		
//		Point nextLocation = SpatioTemporalService.getService().getGeometryFactory().getPoint(initialPoint0 + Math.random(), initialPoint1 + Math.random());
//		potentialCollaboratorMovingGeometry.setGeometryAtTime(nextLocation, oneDayAgo.plusMinutes(100));
//		suspectPersonMovingGeometry.setGeometryAtTime(nextLocation, oneDayAgo.plusMinutes(100));
//		
//		nextLocation = SpatioTemporalService.getService().getGeometryFactory().getPoint(initialPoint0 + Math.random(), initialPoint1 + Math.random());
//		potentialCollaboratorMovingGeometry.setGeometryAtTime(nextLocation, oneDayAgo.plusMinutes(110));
//		suspectPersonMovingGeometry.setGeometryAtTime(nextLocation, oneDayAgo.plusMinutes(110));
//		
//		nextLocation = SpatioTemporalService.getService().getGeometryFactory().getPoint(initialPoint0 + Math.random(), initialPoint1 + Math.random());
//		potentialCollaboratorMovingGeometry.setGeometryAtTime(nextLocation, oneDayAgo.plusMinutes(200));
//		suspectPersonMovingGeometry.setGeometryAtTime(nextLocation, oneDayAgo.plusMinutes(200));
//		
//		nextLocation = SpatioTemporalService.getService().getGeometryFactory().getPoint(initialPoint0 + Math.random(), initialPoint1 + Math.random());
//		potentialCollaboratorMovingGeometry.setGeometryAtTime(nextLocation, oneDayAgo.plusMinutes(300));
//		suspectPersonMovingGeometry.setGeometryAtTime(nextLocation, oneDayAgo.plusMinutes(300));
//		
//		nextLocation = SpatioTemporalService.getService().getGeometryFactory().getPoint(initialPoint0 + Math.random(), initialPoint1 + Math.random());
//		potentialCollaboratorMovingGeometry.setGeometryAtTime(nextLocation, oneDayAgo.plusMinutes(550));
//		suspectPersonMovingGeometry.setGeometryAtTime(nextLocation, oneDayAgo.plusMinutes(550));
//		
//		testDriver.updateEntity(suspectPerson1);
//		testDriver.updateEntity(potentialCollaborator);
//		
//		DwellingWithCriminalReport dwellingWithCriminalReport = conceptFactory.createDwellingWithCriminalReport(oneDayAgo.plusMinutes(600));
//		dwellingWithCriminalReport.setNearbyDistanceInMeters(10);
//		dwellingWithCriminalReport.setSuspect(testDriver.createRelationship(Person.class, SUSPECT_PERSON));
//		dwellingWithCriminalReport.setPotentialCollaborator(testDriver.createRelationship(Person.class, POTENTIAL_COLLABORATOR));
//		
//		testDriver.submitEvent(dwellingWithCriminalReport);
//		testDriver.waitUntilSolutionIdle();
//		
//		DebugInfo[] debugInfos = debugReceiver.getDebugInfo("PersonRuleAgent");
//		
//		for (DebugInfo debugInfo : debugInfos) {
//			System.out.println( "DebugInfo: " + debugInfo );
//			
//			Event event = debugInfo.getEvent();
//			String eventXml = testDriver.getModelSerializer().serializeEvent(DataFormat.TYPED_XML, event );
//			System.out.println( "Event as XML: " + eventXml );
//		}
//	}
//	
//
//	@Test
//	public void TestShouldSeeAlert()  throws SolutionException, GatewayException, RoutingException, InterruptedException {
//		ConceptFactory conceptFactory = testDriver.getConceptFactory(ConceptFactory.class);
//		ZonedDateTime now = ZonedDateTime.now();
//		ZonedDateTime oneDayAgo = now.minusDays(1);
//		
//		double initialPoint0 = 34.781768;
//		double initialPoint1 = 32.085300;
//
//		// Organization Initialization
//		OrganizationInitialization criminalOrganization1 = conceptFactory.createOrganizationInitialization(oneDayAgo);
//		criminalOrganization1.setType(OrganizationType.CRIMINAL);
//		criminalOrganization1.setOrganization(testDriver.createRelationship(Organization.class, CRIMINAL_ORGANIZATION));
//		
//		OrganizationInitialization commercialOrganization1 = conceptFactory.createOrganizationInitialization(oneDayAgo);
//		commercialOrganization1.setType(OrganizationType.COMMERCIAL);
//		commercialOrganization1.setOrganization(testDriver.createRelationship(Organization.class, COMMERCIAL_ORGANIZATION));
//		
//		// Organization role Initialization		
//		OrganizationRoleInitialization criminalOrganizationRole1 = conceptFactory.createOrganizationRoleInitialization(oneDayAgo);
//		criminalOrganizationRole1.setOrganizationalRole(testDriver.createRelationship(OrganizationalRole.class, CRIMINAL_ORGANIZATION_ROLE));
//		criminalOrganizationRole1.setOrganization(testDriver.createRelationship(Organization.class, CRIMINAL_ORGANIZATION));
//			
//		OrganizationRoleInitialization commercialOrganizationRole1 = conceptFactory.createOrganizationRoleInitialization(oneDayAgo);
//		commercialOrganizationRole1.setOrganizationalRole(testDriver.createRelationship(OrganizationalRole.class, NON_CRIMINAL_ORGANIZATION_ROLE));
//		commercialOrganizationRole1.setOrganization(testDriver.createRelationship(Organization.class, COMMERCIAL_ORGANIZATION));
//		
//		// Person Initialization
//		PersonInitialization criminalPersonInitialization1 = conceptFactory.createPersonInitialization(oneDayAgo);
//		criminalPersonInitialization1.setPerson(testDriver.createRelationship(Person.class, POTENTIAL_COLLABORATOR));
//		criminalPersonInitialization1.setName("a1");
//		criminalPersonInitialization1.setProfession("Potential");
//		criminalPersonInitialization1.setRole(testDriver.createRelationship(OrganizationalRole.class, CRIMINAL_ORGANIZATION_ROLE));
//		criminalPersonInitialization1.setState(PersonState.ACTIVE);
//		Point suspectLocation = SpatioTemporalService.getService().getGeometryFactory().getPoint(initialPoint0 + Math.random(), initialPoint1 + Math.random());
//		criminalPersonInitialization1.setLocation(suspectLocation);
//		
//		PersonInitialization nonCriminalPersonInitialization1 = conceptFactory.createPersonInitialization(oneDayAgo);
//		nonCriminalPersonInitialization1.setPerson(testDriver.createRelationship(Person.class, SUSPECT_PERSON));
//		nonCriminalPersonInitialization1.setName("b2");
//		nonCriminalPersonInitialization1.setProfession("Suspect");
//		nonCriminalPersonInitialization1.setRole(testDriver.createRelationship(OrganizationalRole.class, NON_CRIMINAL_ORGANIZATION_ROLE));
//		nonCriminalPersonInitialization1.setState(PersonState.ACTIVE);
//		Point potentialCollaboratorLocation = SpatioTemporalService.getService().getGeometryFactory().getPoint(initialPoint0 + Math.random(), initialPoint1 + Math.random());
//		nonCriminalPersonInitialization1.setLocation(potentialCollaboratorLocation);
//	
//		// Create Entities - Submit Events
//		testDriver.submitEvent(criminalOrganization1);
//		testDriver.submitEvent(commercialOrganization1);
//		testDriver.submitEvent(criminalOrganizationRole1);
//		testDriver.submitEvent(commercialOrganizationRole1);
//		testDriver.submitEvent(criminalPersonInitialization1);
//		testDriver.submitEvent(nonCriminalPersonInitialization1);
//		testDriver.waitUntilSolutionIdle();
//		
//		Person suspectPerson1 = testDriver.fetchEntity(Person.class, SUSPECT_PERSON);
//		Person potentialCollaborator = testDriver.fetchEntity(Person.class, POTENTIAL_COLLABORATOR);
//		MovingGeometry potentialCollaboratorMovingGeometry = potentialCollaborator.getLocation();
//		MovingGeometry suspectPersonMovingGeometry = suspectPerson1.getLocation();
//		
//		Point nextLocation = SpatioTemporalService.getService().getGeometryFactory().getPoint(initialPoint0 + Math.random(), initialPoint1 + Math.random());
//		potentialCollaboratorMovingGeometry.setGeometryAtTime(nextLocation, oneDayAgo.plusMinutes(100));
//		suspectPersonMovingGeometry.setGeometryAtTime(nextLocation, oneDayAgo.plusMinutes(100));
//		
//		nextLocation = SpatioTemporalService.getService().getGeometryFactory().getPoint(initialPoint0 + Math.random(), initialPoint1 + Math.random());
//		potentialCollaboratorMovingGeometry.setGeometryAtTime(nextLocation, oneDayAgo.plusMinutes(110));
//		suspectPersonMovingGeometry.setGeometryAtTime(nextLocation, oneDayAgo.plusMinutes(110));
//		
//		nextLocation = SpatioTemporalService.getService().getGeometryFactory().getPoint(initialPoint0 + Math.random(), initialPoint1 + Math.random());
//		potentialCollaboratorMovingGeometry.setGeometryAtTime(nextLocation, oneDayAgo.plusMinutes(200));
//		suspectPersonMovingGeometry.setGeometryAtTime(nextLocation, oneDayAgo.plusMinutes(200));
//		
//		nextLocation = SpatioTemporalService.getService().getGeometryFactory().getPoint(initialPoint0 + Math.random(), initialPoint1 + Math.random());
//		potentialCollaboratorMovingGeometry.setGeometryAtTime(nextLocation, oneDayAgo.plusMinutes(300));
//		suspectPersonMovingGeometry.setGeometryAtTime(nextLocation, oneDayAgo.plusMinutes(300));
//		
//		nextLocation = SpatioTemporalService.getService().getGeometryFactory().getPoint(initialPoint0 + Math.random(), initialPoint1 + Math.random());
//		potentialCollaboratorMovingGeometry.setGeometryAtTime(nextLocation, oneDayAgo.plusMinutes(580));
//		suspectPersonMovingGeometry.setGeometryAtTime(nextLocation, oneDayAgo.plusMinutes(580));
//		
//		testDriver.updateEntity(suspectPerson1);
//		testDriver.updateEntity(potentialCollaborator);
//		
//		DwellingWithCriminalReport dwellingWithCriminalReport = conceptFactory.createDwellingWithCriminalReport(oneDayAgo.plusMinutes(600));
//		dwellingWithCriminalReport.setNearbyDistanceInMeters(10);
//		dwellingWithCriminalReport.setSuspect(testDriver.createRelationship(Person.class, SUSPECT_PERSON));
//		dwellingWithCriminalReport.setPotentialCollaborator(testDriver.createRelationship(Person.class, POTENTIAL_COLLABORATOR));
//		
//		testDriver.submitEvent(dwellingWithCriminalReport);
//		testDriver.waitUntilSolutionIdle();
//		
//		DebugInfo[] debugInfos = debugReceiver.getDebugInfo("PersonRuleAgent");
//		
//		for (DebugInfo debugInfo : debugInfos) {
//			System.out.println( "DebugInfo: " + debugInfo );
//			
//			Event event = debugInfo.getEvent();
//			String eventXml = testDriver.getModelSerializer().serializeEvent(DataFormat.TYPED_XML, event );
//			System.out.println( "Event as XML: " + eventXml );
//		}
//	}
	
}

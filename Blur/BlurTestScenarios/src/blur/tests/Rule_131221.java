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
import blur.model.DwelledBuildingReport;
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
	
	private static String CRIMINAL_ORGANIZATION = "CRIMINAL_ORGANIZATION";
	private static String COMMERCIAL_ORGANIZATION = "COMMERCIAL_ORGANIZATION";
	private static String CRIMINAL_BUILDING = "CRIMINAL_BUILDING";
	private static String CRIMINAL_ORGANIZATION_ROLE = "CRIMINAL_ORGANIZATION_ROLE";
	private static String NON_CRIMINAL_ORGANIZATION_ROLE = "NON_CRIMINAL_ORGANIZATION_ROLE";
	private static String CRIMINAL_PERSON = "CRIMINAL_PERSON";
	private static String NON_CRIMINAL_PERSON = "NON_CRIMINAL_PERSON";

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
//	
//	@Test
//	public void Test_LessThan40Mins() throws SolutionException, GatewayException, RoutingException, InterruptedException{
//		
//		ConceptFactory conceptFactory = testDriver.getConceptFactory(ConceptFactory.class);
//		ZonedDateTime now = ZonedDateTime.now();
//		Point location = null;
//		ZonedDateTime oneDayAgo = now.minusDays(1);
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
//		PersonInitialization criminalPerson1 = conceptFactory.createPersonInitialization(oneDayAgo);
//		criminalPerson1.setPerson(testDriver.createRelationship(Person.class, CRIMINAL_PERSON));
//		criminalPerson1.setName("a1");
//		criminalPerson1.setProfession("pro1");
//		criminalPerson1.setRole(testDriver.createRelationship(OrganizationalRole.class, CRIMINAL_ORGANIZATION_ROLE));
//		criminalPerson1.setState(PersonState.ACTIVE);
//		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
//		criminalPerson1.setLocation(location);
//		
//		PersonInitialization nonCriminalPerson1 = conceptFactory.createPersonInitialization(oneDayAgo);
//		nonCriminalPerson1.setPerson(testDriver.createRelationship(Person.class, NON_CRIMINAL_PERSON));
//		nonCriminalPerson1.setName("b2");
//		nonCriminalPerson1.setProfession("pro2");
//		nonCriminalPerson1.setRole(testDriver.createRelationship(OrganizationalRole.class, NON_CRIMINAL_ORGANIZATION_ROLE));
//		nonCriminalPerson1.setState(PersonState.ACTIVE);
//		Point person2Location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
//		nonCriminalPerson1.setLocation(person2Location);
//		
//		Point buildingLocation = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
//		
//		// Building Initialization	
//		BuildingInitialization criminalBuilding1 = conceptFactory.createBuildingInitialization(oneDayAgo);
//		criminalBuilding1.setBuilding(testDriver.createRelationship(Building.class, CRIMINAL_BUILDING));
//		criminalBuilding1.setUsageType(BuildingUsageType.BANK_BRANCH);
//		criminalBuilding1.setType(BuildingType.APPARTMENT);
//		criminalBuilding1.setLocation(buildingLocation);
//		criminalBuilding1.setOwner(testDriver.createRelationship(Person.class, "123"));
//		criminalBuilding1.addTo_organizations(testDriver.createRelationship(Organization.class, CRIMINAL_ORGANIZATION));
//		criminalBuilding1.addTo_organizations(testDriver.createRelationship(Organization.class, COMMERCIAL_ORGANIZATION));
//	
//		// Create Entities - Submit Events
//		testDriver.submitEvent(criminalOrganization1);
//		testDriver.submitEvent(commercialOrganization1);
//		testDriver.submitEvent(criminalOrganizationRole1);
//		testDriver.submitEvent(commercialOrganizationRole1);
//		testDriver.submitEvent(criminalPerson1);
//		testDriver.submitEvent(nonCriminalPerson1);
//		testDriver.submitEvent(criminalBuilding1);
//		testDriver.waitUntilSolutionIdle();
//		
//		Person person2 = testDriver.fetchEntity(Person.class, NON_CRIMINAL_PERSON);
//		
//		MovingGeometry movingGeometry = person2.getLocation();
//		movingGeometry.setGeometryAtTime(buildingLocation, oneDayAgo.plusMinutes(200));
//		
//		testDriver.updateEntity(person2);
//		
//		DwelledBuildingReport dwelledBuildingReport = conceptFactory.createDwelledBuildingReport(oneDayAgo.plusMinutes(220));
//		dwelledBuildingReport.setBuilding(testDriver.createRelationship(Building.class, CRIMINAL_BUILDING));
//		dwelledBuildingReport.setNearbyDistanceInMeters(10);
//		dwelledBuildingReport.setPerson(testDriver.createRelationship(Person.class, NON_CRIMINAL_PERSON));
//		
//		testDriver.submitEvent(dwelledBuildingReport);
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

//	@Test
//	public void Test_MoreThen2Hours() throws SolutionException, GatewayException, RoutingException, InterruptedException{
//		
//		ConceptFactory conceptFactory = testDriver.getConceptFactory(ConceptFactory.class);
//		ZonedDateTime now = ZonedDateTime.now();
//		Point location = null;
//		ZonedDateTime oneDayAgo = now.minusDays(1);
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
//		PersonInitialization criminalPerson1 = conceptFactory.createPersonInitialization(oneDayAgo);
//		criminalPerson1.setPerson(testDriver.createRelationship(Person.class, CRIMINAL_PERSON));
//		criminalPerson1.setName("a1");
//		criminalPerson1.setProfession("pro1");
//		criminalPerson1.setRole(testDriver.createRelationship(OrganizationalRole.class, CRIMINAL_ORGANIZATION_ROLE));
//		criminalPerson1.setState(PersonState.ACTIVE);
//		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
//		criminalPerson1.setLocation(location);
//		
//		PersonInitialization nonCriminalPerson1 = conceptFactory.createPersonInitialization(oneDayAgo);
//		nonCriminalPerson1.setPerson(testDriver.createRelationship(Person.class, NON_CRIMINAL_PERSON));
//		nonCriminalPerson1.setName("b2");
//		nonCriminalPerson1.setProfession("pro2");
//		nonCriminalPerson1.setRole(testDriver.createRelationship(OrganizationalRole.class, NON_CRIMINAL_ORGANIZATION_ROLE));
//		nonCriminalPerson1.setState(PersonState.ACTIVE);
//		Point person2Location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
//		nonCriminalPerson1.setLocation(person2Location);
//		
//		Point buildingLocation = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
//		
//		// Building Initialization	
//		BuildingInitialization criminalBuilding1 = conceptFactory.createBuildingInitialization(oneDayAgo);
//		criminalBuilding1.setBuilding(testDriver.createRelationship(Building.class, CRIMINAL_BUILDING));
//		criminalBuilding1.setUsageType(BuildingUsageType.BANK_BRANCH);
//		criminalBuilding1.setType(BuildingType.APPARTMENT);
//		criminalBuilding1.setLocation(buildingLocation);
//		criminalBuilding1.setOwner(testDriver.createRelationship(Person.class, "123"));
//		criminalBuilding1.addTo_organizations(testDriver.createRelationship(Organization.class, CRIMINAL_ORGANIZATION));
//		criminalBuilding1.addTo_organizations(testDriver.createRelationship(Organization.class, COMMERCIAL_ORGANIZATION));
//	
//		// Create Entities - Submit Events
//		testDriver.submitEvent(criminalOrganization1);
//		testDriver.submitEvent(commercialOrganization1);
//		testDriver.submitEvent(criminalOrganizationRole1);
//		testDriver.submitEvent(commercialOrganizationRole1);
//		testDriver.submitEvent(criminalPerson1);
//		testDriver.submitEvent(nonCriminalPerson1);
//		testDriver.submitEvent(criminalBuilding1);
//		testDriver.waitUntilSolutionIdle();
//		
//		Person person2 = testDriver.fetchEntity(Person.class, NON_CRIMINAL_PERSON);
//		
//		MovingGeometry movingGeometry = person2.getLocation();
//		movingGeometry.setGeometryAtTime(buildingLocation, oneDayAgo.plusMinutes(90));
//		movingGeometry.setGeometryAtTime(buildingLocation, oneDayAgo.plusMinutes(110));
//		movingGeometry.setGeometryAtTime(buildingLocation, oneDayAgo.plusMinutes(140));
//		movingGeometry.setGeometryAtTime(buildingLocation, oneDayAgo.plusMinutes(151));
//		movingGeometry.setGeometryAtTime(buildingLocation, oneDayAgo.plusMinutes(120));
//		movingGeometry.setGeometryAtTime(buildingLocation, oneDayAgo.plusMinutes(180));
//		
//		testDriver.updateEntity(person2);
//		
//		DwelledBuildingReport dwelledBuildingReport = conceptFactory.createDwelledBuildingReport(oneDayAgo.plusMinutes(220));
//		dwelledBuildingReport.setBuilding(testDriver.createRelationship(Building.class, CRIMINAL_BUILDING));
//		dwelledBuildingReport.setNearbyDistanceInMeters(10);
//		dwelledBuildingReport.setPerson(testDriver.createRelationship(Person.class, NON_CRIMINAL_PERSON));
//		
//		testDriver.submitEvent(dwelledBuildingReport);
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
	
	@Test
	public void TestShouldSeeAlert() throws SolutionException, GatewayException, RoutingException, InterruptedException{
		
		ConceptFactory conceptFactory = testDriver.getConceptFactory(ConceptFactory.class);
		ZonedDateTime now = ZonedDateTime.now();
		Point location = null;
		ZonedDateTime oneDayAgo = now.minusDays(1);

		// Organization Initialization
		OrganizationInitialization criminalOrganization1 = conceptFactory.createOrganizationInitialization(oneDayAgo);
		criminalOrganization1.setType(OrganizationType.CRIMINAL);
		criminalOrganization1.setOrganization(testDriver.createRelationship(Organization.class, CRIMINAL_ORGANIZATION));
		
		OrganizationInitialization commercialOrganization1 = conceptFactory.createOrganizationInitialization(oneDayAgo);
		commercialOrganization1.setType(OrganizationType.COMMERCIAL);
		commercialOrganization1.setOrganization(testDriver.createRelationship(Organization.class, COMMERCIAL_ORGANIZATION));
		
		// Organization role Initialization		
		OrganizationRoleInitialization criminalOrganizationRole1 = conceptFactory.createOrganizationRoleInitialization(oneDayAgo);
		criminalOrganizationRole1.setOrganizationalRole(testDriver.createRelationship(OrganizationalRole.class, CRIMINAL_ORGANIZATION_ROLE));
		criminalOrganizationRole1.setOrganization(testDriver.createRelationship(Organization.class, CRIMINAL_ORGANIZATION));
			
		OrganizationRoleInitialization commercialOrganizationRole1 = conceptFactory.createOrganizationRoleInitialization(oneDayAgo);
		commercialOrganizationRole1.setOrganizationalRole(testDriver.createRelationship(OrganizationalRole.class, NON_CRIMINAL_ORGANIZATION_ROLE));
		commercialOrganizationRole1.setOrganization(testDriver.createRelationship(Organization.class, COMMERCIAL_ORGANIZATION));
		
		// Person Initialization
		PersonInitialization criminalPerson1 = conceptFactory.createPersonInitialization(oneDayAgo);
		criminalPerson1.setPerson(testDriver.createRelationship(Person.class, CRIMINAL_PERSON));
		criminalPerson1.setName("a1");
		criminalPerson1.setProfession("pro1");
		criminalPerson1.setRole(testDriver.createRelationship(OrganizationalRole.class, CRIMINAL_ORGANIZATION_ROLE));
		criminalPerson1.setState(PersonState.ACTIVE);
		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		criminalPerson1.setLocation(location);
		
		PersonInitialization nonCriminalPerson1 = conceptFactory.createPersonInitialization(oneDayAgo);
		nonCriminalPerson1.setPerson(testDriver.createRelationship(Person.class, NON_CRIMINAL_PERSON));
		nonCriminalPerson1.setName("b2");
		nonCriminalPerson1.setProfession("pro2");
		nonCriminalPerson1.setRole(testDriver.createRelationship(OrganizationalRole.class, NON_CRIMINAL_ORGANIZATION_ROLE));
		nonCriminalPerson1.setState(PersonState.ACTIVE);
		Point person2Location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		nonCriminalPerson1.setLocation(person2Location);
		
		Point buildingLocation = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		
		// Building Initialization	
		BuildingInitialization criminalBuilding1 = conceptFactory.createBuildingInitialization(oneDayAgo);
		criminalBuilding1.setBuilding(testDriver.createRelationship(Building.class, CRIMINAL_BUILDING));
		criminalBuilding1.setUsageType(BuildingUsageType.BANK_BRANCH);
		criminalBuilding1.setType(BuildingType.APPARTMENT);
		criminalBuilding1.setLocation(buildingLocation);
		criminalBuilding1.setOwner(testDriver.createRelationship(Person.class, "123"));
		criminalBuilding1.addTo_organizations(testDriver.createRelationship(Organization.class, CRIMINAL_ORGANIZATION));
		criminalBuilding1.addTo_organizations(testDriver.createRelationship(Organization.class, COMMERCIAL_ORGANIZATION));
	
		// Create Entities - Submit Events
		testDriver.submitEvent(criminalOrganization1);
		testDriver.submitEvent(commercialOrganization1);
		testDriver.submitEvent(criminalOrganizationRole1);
		testDriver.submitEvent(commercialOrganizationRole1);
		testDriver.submitEvent(criminalPerson1);
		testDriver.submitEvent(nonCriminalPerson1);
		testDriver.submitEvent(criminalBuilding1);
		testDriver.waitUntilSolutionIdle();
		
		Person person2 = testDriver.fetchEntity(Person.class, NON_CRIMINAL_PERSON);
		
		MovingGeometry movingGeometry = person2.getLocation();
		movingGeometry.setGeometryAtTime(buildingLocation, oneDayAgo.plusMinutes(100));
		movingGeometry.setGeometryAtTime(buildingLocation, oneDayAgo.plusMinutes(110));
		movingGeometry.setGeometryAtTime(buildingLocation, oneDayAgo.plusMinutes(140));
		movingGeometry.setGeometryAtTime(person2Location, oneDayAgo.plusMinutes(150));
		movingGeometry.setGeometryAtTime(buildingLocation, oneDayAgo.plusMinutes(151));
		movingGeometry.setGeometryAtTime(buildingLocation, oneDayAgo.plusMinutes(120));
		movingGeometry.setGeometryAtTime(buildingLocation, oneDayAgo.plusMinutes(180));
		
		testDriver.updateEntity(person2);
		
		DwelledBuildingReport dwelledBuildingReport = conceptFactory.createDwelledBuildingReport(oneDayAgo.plusMinutes(220));
		dwelledBuildingReport.setBuilding(testDriver.createRelationship(Building.class, CRIMINAL_BUILDING));
		dwelledBuildingReport.setNearbyDistanceInMeters(10);
		dwelledBuildingReport.setPerson(testDriver.createRelationship(Person.class, NON_CRIMINAL_PERSON));
		
		testDriver.submitEvent(dwelledBuildingReport);
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

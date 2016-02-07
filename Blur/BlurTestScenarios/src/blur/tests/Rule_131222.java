package blur.tests;

import static org.junit.Assert.*;

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
import blur.model.ConceptFactory;
import blur.model.DwelledBuildingReport;
import blur.model.DwellingWithCriminalReport;
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

public class Rule_131222 {

	protected static TestDriver testDriver;
	protected static IADebugReceiver debugReceiver = new IADebugReceiver();
	
	private static String CRIMINAL_ORGANIZATION = "CRIMINAL_ORGANIZATION";
	private static String COMMERCIAL_ORGANIZATION = "COMMERCIAL_ORGANIZATION";
	private static String CRIMINAL_ORGANIZATION_ROLE = "CRIMINAL_ORGANIZATION_ROLE";
	private static String NON_CRIMINAL_ORGANIZATION_ROLE = "NON_CRIMINAL_ORGANIZATION_ROLE";
	private static String POTENTIAL_COLLABORATOR = "CRIMINAL_PERSON";
	private static String SUSPECT_PERSON = "NON_CRIMINAL_PERSON";

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
	public void Test_Only8_5Hours()  throws SolutionException, GatewayException, RoutingException, InterruptedException {
		ConceptFactory conceptFactory = testDriver.getConceptFactory(ConceptFactory.class);
		ZonedDateTime now = ZonedDateTime.now();
		ZonedDateTime oneDayAgo = now.minusDays(1);
		
		double initialPoint0 = 34.781768;
		double initialPoint1 = 32.085300;

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
		PersonInitialization criminalPersonInitialization1 = conceptFactory.createPersonInitialization(oneDayAgo);
		criminalPersonInitialization1.setPerson(testDriver.createRelationship(Person.class, POTENTIAL_COLLABORATOR));
		criminalPersonInitialization1.setName("a1");
		criminalPersonInitialization1.setProfession("Potential");
		criminalPersonInitialization1.setRole(testDriver.createRelationship(OrganizationalRole.class, CRIMINAL_ORGANIZATION_ROLE));
		criminalPersonInitialization1.setState(PersonState.ACTIVE);
		Point suspectLocation = SpatioTemporalService.getService().getGeometryFactory().getPoint(initialPoint0 + Math.random(), initialPoint1 + Math.random());
		criminalPersonInitialization1.setLocation(suspectLocation);
		
		PersonInitialization nonCriminalPersonInitialization1 = conceptFactory.createPersonInitialization(oneDayAgo);
		nonCriminalPersonInitialization1.setPerson(testDriver.createRelationship(Person.class, SUSPECT_PERSON));
		nonCriminalPersonInitialization1.setName("b2");
		nonCriminalPersonInitialization1.setProfession("Suspect");
		nonCriminalPersonInitialization1.setRole(testDriver.createRelationship(OrganizationalRole.class, NON_CRIMINAL_ORGANIZATION_ROLE));
		nonCriminalPersonInitialization1.setState(PersonState.ACTIVE);
		Point potentialCollaboratorLocation = SpatioTemporalService.getService().getGeometryFactory().getPoint(initialPoint0 + Math.random(), initialPoint1 + Math.random());
		nonCriminalPersonInitialization1.setLocation(potentialCollaboratorLocation);
	
		// Create Entities - Submit Events
		testDriver.submitEvent(criminalOrganization1);
		testDriver.submitEvent(commercialOrganization1);
		testDriver.submitEvent(criminalOrganizationRole1);
		testDriver.submitEvent(commercialOrganizationRole1);
		testDriver.submitEvent(criminalPersonInitialization1);
		testDriver.submitEvent(nonCriminalPersonInitialization1);
		testDriver.waitUntilSolutionIdle();
		
		Person suspectPerson1 = testDriver.fetchEntity(Person.class, SUSPECT_PERSON);
		Person potentialCollaborator = testDriver.fetchEntity(Person.class, POTENTIAL_COLLABORATOR);
		MovingGeometry potentialCollaboratorMovingGeometry = potentialCollaborator.getLocation();
		MovingGeometry suspectPersonMovingGeometry = suspectPerson1.getLocation();
		
		Point nextLocation = SpatioTemporalService.getService().getGeometryFactory().getPoint(initialPoint0 + Math.random(), initialPoint1 + Math.random());
		potentialCollaboratorMovingGeometry.setGeometryAtTime(nextLocation, oneDayAgo.plusMinutes(100));
		suspectPersonMovingGeometry.setGeometryAtTime(nextLocation, oneDayAgo.plusMinutes(100));
		
		nextLocation = SpatioTemporalService.getService().getGeometryFactory().getPoint(initialPoint0 + Math.random(), initialPoint1 + Math.random());
		potentialCollaboratorMovingGeometry.setGeometryAtTime(nextLocation, oneDayAgo.plusMinutes(110));
		suspectPersonMovingGeometry.setGeometryAtTime(nextLocation, oneDayAgo.plusMinutes(110));
		
		nextLocation = SpatioTemporalService.getService().getGeometryFactory().getPoint(initialPoint0 + Math.random(), initialPoint1 + Math.random());
		potentialCollaboratorMovingGeometry.setGeometryAtTime(nextLocation, oneDayAgo.plusMinutes(200));
		suspectPersonMovingGeometry.setGeometryAtTime(nextLocation, oneDayAgo.plusMinutes(200));
		
		nextLocation = SpatioTemporalService.getService().getGeometryFactory().getPoint(initialPoint0 + Math.random(), initialPoint1 + Math.random());
		potentialCollaboratorMovingGeometry.setGeometryAtTime(nextLocation, oneDayAgo.plusMinutes(300));
		suspectPersonMovingGeometry.setGeometryAtTime(nextLocation, oneDayAgo.plusMinutes(300));
		
		nextLocation = SpatioTemporalService.getService().getGeometryFactory().getPoint(initialPoint0 + Math.random(), initialPoint1 + Math.random());
		potentialCollaboratorMovingGeometry.setGeometryAtTime(nextLocation, oneDayAgo.plusMinutes(640));
		suspectPersonMovingGeometry.setGeometryAtTime(nextLocation, oneDayAgo.plusMinutes(640));
		
		testDriver.updateEntity(suspectPerson1);
		testDriver.updateEntity(potentialCollaborator);
		
		DwellingWithCriminalReport dwellingWithCriminalReport = conceptFactory.createDwellingWithCriminalReport(oneDayAgo.plusMinutes(700));
		dwellingWithCriminalReport.setNearbyDistanceInMeters(10);
		dwellingWithCriminalReport.setSuspect(testDriver.createRelationship(Person.class, SUSPECT_PERSON));
		dwellingWithCriminalReport.setPotentialCollaborator(testDriver.createRelationship(Person.class, POTENTIAL_COLLABORATOR));
		
		testDriver.submitEvent(dwellingWithCriminalReport);
		testDriver.waitUntilSolutionIdle();
		
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

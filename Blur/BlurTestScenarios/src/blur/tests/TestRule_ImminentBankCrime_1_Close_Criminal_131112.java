package blur.tests;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

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
import blur.model.Organization;
import blur.model.OrganizationInitialization;
import blur.model.OrganizationRoleInitialization;
import blur.model.OrganizationType;
import blur.model.OrganizationalRole;
import blur.model.Person;
import blur.model.PersonInitialization;
import blur.model.PersonState;
import blur.model.PotentialBankCrimeReport;

import com.ibm.geolib.geom.Point;
import com.ibm.geolib.st.SpatioTemporalService;
import com.ibm.ia.common.GatewayException;
import com.ibm.ia.common.RoutingException;
import com.ibm.ia.common.SolutionException;
import com.ibm.ia.model.Relationship;
import com.ibm.ia.testdriver.IADebugReceiver;
import com.ibm.ia.testdriver.TestDriver;

public class TestRule_ImminentBankCrime_1_Close_Criminal_131112 {
	
	protected static TestDriver testDriver;
	protected static IADebugReceiver debugReceiver = new IADebugReceiver();
	
	
	private static final String ORGANIZATION1_CRIMINAL = "ORGANIZATION1_CRIMINAL";
	private static final String ORGANIZATION2_CRIMINAL = "ORGANIZATION2_CRIMINAL";
	
	private static final String ROLE1_CRIMINAL = "ROLE1_CRIMINAL";
	private static final String ROLE2_CRIMINAL = "ROLE2_CRIMINAL";
	
	private static final String PERSON_CRIMINAL_1 = "PERSON_CRIMINAL_1";
	private static final String PERSON_CRIMINAL_2 = "PERSON_CRIMINAL_2";	
	private static final String PERSON_CRIMINAL_3 = "PERSON_CRIMINAL_3";
	private static final String PERSON_CALLEE = "PERSON_CALLEE";
	private static final String PERSON_CALLER = "PERSON_CALLER";

	private static final String BUILDING1_BANK_CRIMINAL = "BUILDING1_BANK_CRIMINAL";

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
			
		Point location_building1 = SpatioTemporalService.getService().getGeometryFactory().getPoint(34.7925683, 32.1000012);
		Point location_close_to_building1 = SpatioTemporalService.getService().getGeometryFactory().getPoint(34.7912541, 32.0995513);
		Point location_far_from_building1 = SpatioTemporalService.getService().getGeometryFactory().getPoint(34.7800317, 32.0985697);

		
		//System.out.println(location_building1.getDistance(location_person1_close_to_building1, LengthUnit.METER));
		//System.out.println(location_building1.getDistance(location_person1_far_from_building1, LengthUnit.METER));

		// Organization Initialization
		OrganizationInitialization organizationInitialization1 = conceptFactory.createOrganizationInitialization(now);
		organizationInitialization1.setType(OrganizationType.CRIMINAL);
		organizationInitialization1.setOrganization(testDriver.createRelationship(Organization.class, ORGANIZATION1_CRIMINAL));
		
		OrganizationInitialization organizationInitialization2 = conceptFactory.createOrganizationInitialization(now);
		organizationInitialization2.setType(OrganizationType.CRIMINAL);
		organizationInitialization2.setOrganization(testDriver.createRelationship(Organization.class, ORGANIZATION2_CRIMINAL));

//		OrganizationInitialization organizationInitialization3 = conceptFactory.createOrganizationInitialization(now);
//		organizationInitialization3.setType(OrganizationType.COMMERCIAL);
//		organizationInitialization3.setOrganization(testDriver.createRelationship(Organization.class, ORGANIZATION3_COMMERCIAL));
		
		// Organization role Initialization		
		OrganizationRoleInitialization organizationRoleInitialization1 = conceptFactory.createOrganizationRoleInitialization(now);
		organizationRoleInitialization1.setOrganizationalRole(testDriver.createRelationship(OrganizationalRole.class, ROLE1_CRIMINAL));
		organizationRoleInitialization1.setOrganization(testDriver.createRelationship(Organization.class, ORGANIZATION1_CRIMINAL));
		
		OrganizationRoleInitialization organizationRoleInitialization2 = conceptFactory.createOrganizationRoleInitialization(now);
		organizationRoleInitialization2.setOrganizationalRole(testDriver.createRelationship(OrganizationalRole.class, ROLE2_CRIMINAL));
		organizationRoleInitialization2.setOrganization(testDriver.createRelationship(Organization.class, ORGANIZATION2_CRIMINAL));
		
		Point location = null;
		
		
		// Building Initialization	
		// Building 1 - 1 criminal
		BuildingInitialization buildingInitialization1 = conceptFactory.createBuildingInitialization(now);
		buildingInitialization1.setBuilding(testDriver.createRelationship(Building.class, BUILDING1_BANK_CRIMINAL));
		buildingInitialization1.setUsageType(BuildingUsageType.BANK_BRANCH);
		buildingInitialization1.setType(BuildingType.APPARTMENT);
		buildingInitialization1.setLocation(location_building1);
		buildingInitialization1.setOwner(testDriver.createRelationship(Person.class, "123"));
		buildingInitialization1.addTo_organizations(testDriver.createRelationship(Organization.class, ORGANIZATION1_CRIMINAL));

		// Person Initialization
		PersonInitialization personInitialization1 = conceptFactory.createPersonInitialization(now);
		personInitialization1.setPerson(testDriver.createRelationship(Person.class, PERSON_CRIMINAL_1));
		personInitialization1.setName("a1");
		personInitialization1.setProfession("pro1");
		personInitialization1.setRole(testDriver.createRelationship(OrganizationalRole.class, ROLE1_CRIMINAL)); //Criminal
		personInitialization1.setState(PersonState.ACTIVE);
		personInitialization1.setLocation(location_far_from_building1);
		
		PersonInitialization personInitialization2 = conceptFactory.createPersonInitialization(now);
		personInitialization2.setPerson(testDriver.createRelationship(Person.class, PERSON_CRIMINAL_2));
		personInitialization2.setName("b2");
		personInitialization2.setProfession("pro2");
		personInitialization2.setRole(testDriver.createRelationship(OrganizationalRole.class, ROLE2_CRIMINAL)); // Criminal
		personInitialization2.setState(PersonState.ACTIVE);
		personInitialization2.setLocation(location_close_to_building1);
		
		PersonInitialization personInitialization3 = conceptFactory.createPersonInitialization(now);
		personInitialization3.setPerson(testDriver.createRelationship(Person.class, PERSON_CRIMINAL_3));
		personInitialization3.setName("b2");
		personInitialization3.setProfession("pro2");
		personInitialization3.setRole(testDriver.createRelationship(OrganizationalRole.class, ROLE1_CRIMINAL)); // Criminal
		personInitialization3.setState(PersonState.ACTIVE);
		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		personInitialization3.setLocation(location_far_from_building1);
		
		PersonInitialization personInitialization4 = conceptFactory.createPersonInitialization(now);
		personInitialization4.setPerson(testDriver.createRelationship(Person.class, PERSON_CALLEE));
		personInitialization4.setName("mycallee");
		personInitialization4.setProfession("pro4");
		personInitialization4.setRole(testDriver.createRelationship(OrganizationalRole.class, ROLE1_CRIMINAL)); //Criminal
		personInitialization4.setState(PersonState.ACTIVE);
		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		personInitialization4.setLocation(location);
		
		PersonInitialization personInitialization5 = conceptFactory.createPersonInitialization(now);
		personInitialization5.setPerson(testDriver.createRelationship(Person.class, PERSON_CALLER));
		personInitialization5.setName("theCaller");
		personInitialization5.setProfession("pro5");
		personInitialization5.setRole(testDriver.createRelationship(OrganizationalRole.class, ROLE1_CRIMINAL)); //Criminal
		personInitialization5.setState(PersonState.ACTIVE);
		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		personInitialization5.setLocation(location);
		
		// Create Entities - Submit Events
		testDriver.submitEvent(organizationInitialization1);
		testDriver.submitEvent(organizationInitialization2);
//		testDriver.submitEvent(organizationInitialization3);
		testDriver.submitEvent(organizationRoleInitialization1);
		testDriver.submitEvent(organizationRoleInitialization2);
		testDriver.submitEvent(personInitialization1);
		testDriver.submitEvent(personInitialization2);
		testDriver.submitEvent(personInitialization3);
		testDriver.submitEvent(personInitialization4);
		testDriver.submitEvent(personInitialization5);
		testDriver.submitEvent(buildingInitialization1);

		// allow the server time to create the entities
		Thread.sleep(3000);
		
		ZonedDateTime oneDayAgo = now.minusDays(1);
		
		// Potential bank crime report
		PotentialBankCrimeReport potentialBankCrimeReport = conceptFactory.createPotentialBankCrimeReport(oneDayAgo);
		List< Relationship<Person>> criminal_persons = new ArrayList<Relationship<Person>>();
		criminal_persons.add(testDriver.createRelationship(Person.class, PERSON_CRIMINAL_1));
		criminal_persons.add(testDriver.createRelationship(Person.class, PERSON_CRIMINAL_2));
		criminal_persons.add(testDriver.createRelationship(Person.class, PERSON_CRIMINAL_3));
		potentialBankCrimeReport.set_persons(criminal_persons);
		potentialBankCrimeReport.setBuilding(testDriver.createRelationship(Building.class,BUILDING1_BANK_CRIMINAL));
		potentialBankCrimeReport.setCallee(testDriver.createRelationship(Person.class, PERSON_CALLEE));
		potentialBankCrimeReport.setCaller(testDriver.createRelationship(Person.class, PERSON_CALLER));
		potentialBankCrimeReport.setRadiusInMeters(500);

		testDriver.submitEvent(potentialBankCrimeReport);
		
		
		// Cellular Reports
		Thread.sleep(5000);		
	}
}

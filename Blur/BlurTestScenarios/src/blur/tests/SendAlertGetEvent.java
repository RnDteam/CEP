package blur.tests;

import java.time.ZonedDateTime;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

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

public class SendAlertGetEvent {

	protected static TestDriver testDriver;
	protected static IADebugReceiver debugReceiver = new IADebugReceiver();


	private static final String ORGANIZATION_1 = "org_bx";

	private static final String ROLE_1 = "ROLE_1";

	private static final String PERSON_1 = "ppl-Txe-182";

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
		organizationInitialization1.setType(OrganizationType.COMMERCIAL);
		organizationInitialization1.setOrganization(testDriver.createRelationship(Organization.class, ORGANIZATION_1));

		// Organization role Initialization		
		OrganizationRoleInitialization organizationRoleInitialization1 = conceptFactory.createOrganizationRoleInitialization(now);
		organizationRoleInitialization1.setOrganizationalRole(testDriver.createRelationship(OrganizationalRole.class, ROLE_1));
		organizationRoleInitialization1.setOrganization(testDriver.createRelationship(Organization.class, ORGANIZATION_1));

		Point location = null;

		// Person Initialization
		PersonInitialization personInitialization1 = conceptFactory.createPersonInitialization(now);
		personInitialization1.setPerson(testDriver.createRelationship(Person.class, PERSON_1));
		personInitialization1.setName("a1");
		personInitialization1.setProfession("pro1");
		personInitialization1.setRole(testDriver.createRelationship(OrganizationalRole.class, ROLE_1));
		personInitialization1.setState(PersonState.ACTIVE);
		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		personInitialization1.setLocation(location);

		// Create Entities - Submit Events
		testDriver.submitEvent(organizationInitialization1);
		testDriver.submitEvent(organizationRoleInitialization1);
		testDriver.submitEvent(personInitialization1);
		
		//Time to create the entities.
		Thread.sleep(7000);
		
		CellularReport cellularReport = conceptFactory.createCellularReport(now.minusDays(1));
		cellularReport.setPerson(testDriver.createRelationship(Person.class, PERSON_1));
		
		testDriver.submitEvent(cellularReport);
		
		testDriver.processPendingSchedules(now);
		Thread.sleep(10000);
		testDriver.waitUntilSolutionIdle();
	}
}

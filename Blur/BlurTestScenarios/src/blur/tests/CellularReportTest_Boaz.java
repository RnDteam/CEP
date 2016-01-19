package blur.tests;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Set;

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
import blur.model.OrganizationType;
import blur.model.Person;
import blur.model.impl.Alert;

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
import com.jhlabs.map.proj.StereographicAzimuthalProjection;

public class CellularReportTest_Boaz {

	private static final String BUILDING2 = "building2";
	private static final String BUILDING1 = "building1";
	private static final int MINUTES_IN_A_DAY = 24 * 60;
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
		Thread.sleep(5000);
		testDriver.endTest();
		testDriver.stopRecording();
	}

	@Test
	public void test() throws SolutionException, GatewayException, RoutingException, InterruptedException{
		
		ConceptFactory conceptFactory = testDriver.getConceptFactory(ConceptFactory.class);
		ZonedDateTime now = ZonedDateTime.now();
		
		// Organization Initialization
		OrganizationInitialization organizationInitialization1 = conceptFactory.createOrganizationInitialization(now);
		organizationInitialization1.setType(OrganizationType.CRIMINAL);
		organizationInitialization1.setOrganization(testDriver.createRelationship(Organization.class, "organization1"));
		
		OrganizationInitialization organizationInitialization2 = conceptFactory.createOrganizationInitialization(now);
		organizationInitialization2.setType(OrganizationType.CRIMINAL);
		organizationInitialization2.setOrganization(testDriver.createRelationship(Organization.class, "organization2"));
		
		OrganizationInitialization organizationInitialization3 = conceptFactory.createOrganizationInitialization(now);
		organizationInitialization3.setType(OrganizationType.COMMERCIAL);
		organizationInitialization3.setOrganization(testDriver.createRelationship(Organization.class, "organization3"));
		
		// Building Initialization
		Point location = null;
		
		BuildingInitialization buildingInitialization1 = conceptFactory.createBuildingInitialization(now);
		buildingInitialization1.setBuilding(testDriver.createRelationship(Building.class, BUILDING1));
		buildingInitialization1.setUsageType(BuildingUsageType.BANK_BRANCH);
		buildingInitialization1.setType(BuildingType.APPARTMENT);
		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		buildingInitialization1.setLocation(location);
		buildingInitialization1.setOwner(testDriver.createRelationship(Person.class, "123"));
		buildingInitialization1.addTo_organizations(testDriver.createRelationship(Organization.class, "organization1"));
		buildingInitialization1.addTo_organizations(testDriver.createRelationship(Organization.class, "organization3"));
		
		BuildingInitialization buildingInitialization2 = conceptFactory.createBuildingInitialization(now);
		buildingInitialization2.setBuilding(testDriver.createRelationship(Building.class, BUILDING2));
		buildingInitialization2.setUsageType(BuildingUsageType.FURNITURE_STORE);
		buildingInitialization2.setType(BuildingType.COMMERCIAL);
		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + Math.random(), 32.085300 + Math.random());
		buildingInitialization2.setLocation(location);
		buildingInitialization2.setOwner(testDriver.createRelationship(Person.class, "123"));
		buildingInitialization2.addTo_organizations(testDriver.createRelationship(Organization.class, "organization3"));

		testDriver.submitEvent(organizationInitialization1);
		testDriver.submitEvent(organizationInitialization2);
		testDriver.submitEvent(organizationInitialization3);	
		testDriver.submitEvent(buildingInitialization1);
		testDriver.submitEvent(buildingInitialization2);
		
		// allow the server time to create the buildings
		Thread.sleep(2000);		

		// 48 reports in 24 hours
		ZonedDateTime oneDayAgo = now.minusDays(7);
		System.out.println(oneDayAgo);
		for (int i = 0; i < 48; i += 1) {
			CellularReport cellularReport = conceptFactory.createCellularReport(oneDayAgo);
			cellularReport.setBuilding(testDriver.createRelationship(Building.class, BUILDING1));
			testDriver.submitEvent(cellularReport);
			System.out.println(String.format("Submitted cellular report number %d from 48 for building 1", i + 1));
			oneDayAgo = oneDayAgo.plusMinutes(30);
		}
		
		//Test Rule 8
		testDriver.processPendingSchedules(oneDayAgo.plusHours(5));
		ZonedDateTime oneEventTime = oneDayAgo.plusHours(3);
		CellularReport cellularReport1 = conceptFactory.createCellularReport(oneEventTime);
		cellularReport1.setBuilding(testDriver.createRelationship(Building.class, BUILDING1));
		testDriver.submitEvent(cellularReport1);
		System.out.println(String.format("Submitted one cellular report"));
		
		oneDayAgo = oneDayAgo.plusDays(1);
		
		// 24 reports in 24 hours
		for (int i = 0; i < 24; i += 1) {
			CellularReport cellularReport = conceptFactory.createCellularReport(oneDayAgo);
			cellularReport.setBuilding(testDriver.createRelationship(Building.class, BUILDING1));
			testDriver.submitEvent(cellularReport);
			System.out.println(String.format("Submitted cellular report number %d from 24 for building 1", i + 1));
			oneDayAgo = oneDayAgo.plusHours(1);
		}
		
		// 5 reports in 5 minutes
		for (int i = 0; i < 5; i += 1) {
			CellularReport cellularReport = conceptFactory.createCellularReport(oneDayAgo);
			cellularReport.setBuilding(testDriver.createRelationship(Building.class, BUILDING1));
			testDriver.submitEvent(cellularReport);
			System.out.println(String.format("Submitted cellular resport number %d from 5 for building 1", i + 1));
			oneDayAgo = oneDayAgo.plusMinutes(1);
		}
		
		oneDayAgo = oneDayAgo.plusHours(3);
		
		// 5 reports in 10 hours
		for (int i = 0; i < 5; i += 1) {
			CellularReport cellularReport = conceptFactory.createCellularReport(oneDayAgo);
			cellularReport.setBuilding(testDriver.createRelationship(Building.class, BUILDING1));
			testDriver.submitEvent(cellularReport);
			System.out.println(String.format("Submitted cellular report number %d from 5 for building 1", i + 1));
			oneDayAgo = oneDayAgo.plusHours(2);
		}
		
		oneDayAgo = oneDayAgo.plusHours(10);
		// 5 reports in 5 minutes
		for (int i = 0; i < 5; i += 1) {
			CellularReport cellularReport = conceptFactory.createCellularReport(oneDayAgo);
			cellularReport.setBuilding(testDriver.createRelationship(Building.class, BUILDING1));
			testDriver.submitEvent(cellularReport);
			System.out.println(String.format("Submitted cellular resport number %d from 5 for building 1", i + 1));
			oneDayAgo = oneDayAgo.plusMinutes(1);
		}
	}
	
	
}

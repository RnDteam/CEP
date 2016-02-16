package blur.tests;

import java.time.ZoneId;
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
import com.ibm.ia.time.dataie.ZoneIdIE;
import com.ibm.security.util.calendar.ZoneInfo;
import com.jhlabs.map.proj.StereographicAzimuthalProjection;

public class CellularReportTest_24hours {

	private static final String BUILDING_2 = "building2";
	private static final String BUILDING_1 = "building1";
	private static final String OGANIZATION_1 = "Criminal_organization_1";
	private static final String OGANIZATION_2= "Criminal_organization_2";
	private static final String OGANIZATION_3 = "Commercial_organization_3";
	private static final String PERSON_1 = "Dad";

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
		ZoneId zId = ZoneId.of("+2");
		ZonedDateTime now = ZonedDateTime.of(2016, 1, 10, 9, 0, 0, 0, zId);
		
		// Organization Initialization
		OrganizationInitialization organizationInitialization1 = conceptFactory.createOrganizationInitialization(now);
		organizationInitialization1.setType(OrganizationType.CRIMINAL);
		organizationInitialization1.setOrganization(testDriver.createRelationship(Organization.class, OGANIZATION_1));
		
		OrganizationInitialization organizationInitialization2 = conceptFactory.createOrganizationInitialization(now);
		organizationInitialization2.setType(OrganizationType.CRIMINAL);
		organizationInitialization2.setOrganization(testDriver.createRelationship(Organization.class, OGANIZATION_2));
		
		OrganizationInitialization organizationInitialization3 = conceptFactory.createOrganizationInitialization(now);
		organizationInitialization3.setType(OrganizationType.COMMERCIAL);
		organizationInitialization3.setOrganization(testDriver.createRelationship(Organization.class, OGANIZATION_3));
		
		// Building Initialization
		Point location = null;
		double location_lon = 34.781768;
		double location_lat = 32.085300;
		
		BuildingInitialization buildingInitialization1 = conceptFactory.createBuildingInitialization(now);
		buildingInitialization1.setBuilding(testDriver.createRelationship(Building.class, BUILDING_1));
		buildingInitialization1.setUsageType(BuildingUsageType.BANK_BRANCH);
		buildingInitialization1.setType(BuildingType.APPARTMENT);
		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( location_lon + Math.random(), location_lat + Math.random());
		buildingInitialization1.setLocation(location);
		buildingInitialization1.setOwner(testDriver.createRelationship(Person.class, PERSON_1));
		buildingInitialization1.addTo_organizations(testDriver.createRelationship(Organization.class, OGANIZATION_1));
		buildingInitialization1.addTo_organizations(testDriver.createRelationship(Organization.class, OGANIZATION_3));
		
		BuildingInitialization buildingInitialization2 = conceptFactory.createBuildingInitialization(now);
		buildingInitialization2.setBuilding(testDriver.createRelationship(Building.class, BUILDING_2));
		buildingInitialization2.setUsageType(BuildingUsageType.FURNITURE_STORE);
		buildingInitialization2.setType(BuildingType.COMMERCIAL);
		location = SpatioTemporalService.getService().getGeometryFactory().getPoint( location_lon + Math.random(), location_lat + Math.random());
		buildingInitialization2.setLocation(location);
		buildingInitialization2.setOwner(testDriver.createRelationship(Person.class, PERSON_1));
		buildingInitialization2.addTo_organizations(testDriver.createRelationship(Organization.class, OGANIZATION_3));
		
		//Submit Initialization Events
		testDriver.submitEvent(organizationInitialization1);
		testDriver.submitEvent(organizationInitialization2);
		testDriver.submitEvent(organizationInitialization3);	
		testDriver.submitEvent(buildingInitialization1);
		testDriver.submitEvent(buildingInitialization2);
		
		testDriver.waitUntilSolutionIdle();

		ZonedDateTime beginTime = ZonedDateTime.of(2016, 1, 1, 0, 0, 0, 0, ZoneId.systemDefault());
		System.out.println(beginTime);
		
		for (int i = 0; i < 24; i++) {
			CellularReport cellularReport = conceptFactory.createCellularReport(beginTime);
			cellularReport.setBuilding(testDriver.createRelationship(Building.class, BUILDING_1));
			testDriver.submitEvent(cellularReport);
			System.out.println(String.format("Submitted cellular report number %d from 24 for %s", i + 1, BUILDING_1));
			beginTime = beginTime.plusMinutes(15);
		}
		
		beginTime = beginTime.minusMinutes(50);
		
		for (int i = 0; i < 10; i++) {
			CellularReport cellularReport = conceptFactory.createCellularReport(beginTime);
			cellularReport.setBuilding(testDriver.createRelationship(Building.class, BUILDING_1));
			testDriver.submitEvent(cellularReport);
			System.out.println(String.format("Submitted cellular report number %d from 10 ADDITIONAL ONES for %s", i + 1, BUILDING_1));
			beginTime = beginTime.plusMinutes(5);
		}
	}	
}

package blur.tests;

import static org.junit.Assert.*;

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
import blur.model.BuildingInitialization;
import blur.model.BuildingType;
import blur.model.BuildingUsageType;
import blur.model.CellularCallReport;
import blur.model.CellularReport;
import blur.model.ConceptFactory;
import blur.model.Organization;
import blur.model.OrganizationInitialization;
import blur.model.OrganizationRoleInitialization;
import blur.model.OrganizationRoleType;
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

		//		CellularReport cell_at_20_for_person_271_and_building_80 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(20));
		//		cell_at_20_for_person_271_and_building_80.setPerson(testDriver.createRelationship(Person.class, PERSON_271));
		//		cell_at_20_for_person_271_and_building_80.setBuilding((testDriver.createRelationship(Building.class, BUILDING_80)));
		//		testDriver.submitEvent(cell_at_20_for_person_271_and_building_80);

		// cellular reports for person 152
		CellularReport cell_at_15_for_person_152_and_building_32 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(15));
		cell_at_15_for_person_152_and_building_32.setPerson(testDriver.createRelationship(Person.class, PERSON_152));
		cell_at_15_for_person_152_and_building_32.setBuilding((testDriver.createRelationship(Building.class, BUILDING_32)));
//		testDriver.submitEvent(cell_at_15_for_person_152_and_building_32);

		//cam-T8-ww at 20
		CellularReport cell_at_20_for_person_152_and_building_cam_T8_ww = conceptFactory.createCellularReport(absoluteTime.plusMinutes(20));
		cell_at_20_for_person_152_and_building_cam_T8_ww.setPerson(testDriver.createRelationship(Person.class, PERSON_152));
		cell_at_20_for_person_152_and_building_cam_T8_ww.setBuilding((testDriver.createRelationship(Building.class, "cam-T8-ww")));
//		testDriver.submitEvent(cell_at_20_for_person_152_and_building_cam_T8_ww);

		//cam-T4-ne at 30
		CellularReport cell_at_30_for_person_152_and_building_cam_T4_ne = conceptFactory.createCellularReport(absoluteTime.plusMinutes(30));
		cell_at_30_for_person_152_and_building_cam_T4_ne.setPerson(testDriver.createRelationship(Person.class, PERSON_152));
		cell_at_30_for_person_152_and_building_cam_T4_ne.setBuilding((testDriver.createRelationship(Building.class, "cam-T4-ne")));
//		testDriver.submitEvent(cell_at_30_for_person_152_and_building_cam_T4_ne);

		//cam-T4-ne at 40
		CellularReport cell_at_40_for_person_152_and_building_cam_T4_ne = conceptFactory.createCellularReport(absoluteTime.plusMinutes(40));
		cell_at_40_for_person_152_and_building_cam_T4_ne.setPerson(testDriver.createRelationship(Person.class, PERSON_152));
		cell_at_40_for_person_152_and_building_cam_T4_ne.setBuilding((testDriver.createRelationship(Building.class, "cam-T4-ne")));
//		testDriver.submitEvent(cell_at_40_for_person_152_and_building_cam_T4_ne);

		//cam-T4-ne at 50
		CellularReport cell_at_50_for_person_152_and_building_cam_T4_ne = conceptFactory.createCellularReport(absoluteTime.plusMinutes(50));
		cell_at_50_for_person_152_and_building_cam_T4_ne.setPerson(testDriver.createRelationship(Person.class, PERSON_152));
		cell_at_50_for_person_152_and_building_cam_T4_ne.setBuilding((testDriver.createRelationship(Building.class, "cam-T4-ne")));
//		testDriver.submitEvent(cell_at_50_for_person_152_and_building_cam_T4_ne);

		CellularReport cell_at_55_for_person_152_and_building_32 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(55));
		cell_at_55_for_person_152_and_building_32.setPerson(testDriver.createRelationship(Person.class, PERSON_152));
		cell_at_55_for_person_152_and_building_32.setBuilding((testDriver.createRelationship(Building.class, BUILDING_32)));
//		testDriver.submitEvent(cell_at_55_for_person_152_and_building_32);

		CellularReport cell_at_70_for_person_152_and_building_32 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(70));
		cell_at_70_for_person_152_and_building_32.setPerson(testDriver.createRelationship(Person.class, PERSON_152));
		cell_at_70_for_person_152_and_building_32.setBuilding((testDriver.createRelationship(Building.class, BUILDING_32)));
//		testDriver.submitEvent(cell_at_70_for_person_152_and_building_32);

		CellularReport cell_at_85_for_person_152_and_building_32 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(85));
		cell_at_85_for_person_152_and_building_32.setPerson(testDriver.createRelationship(Person.class, PERSON_152));
		cell_at_85_for_person_152_and_building_32.setBuilding((testDriver.createRelationship(Building.class, BUILDING_32)));
//		testDriver.submitEvent(cell_at_85_for_person_152_and_building_32);

		CellularReport cell_at_100_for_person_152_and_building_32 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(100));
		cell_at_100_for_person_152_and_building_32.setPerson(testDriver.createRelationship(Person.class, PERSON_152));
		cell_at_100_for_person_152_and_building_32.setBuilding((testDriver.createRelationship(Building.class, BUILDING_32)));
//		testDriver.submitEvent(cell_at_100_for_person_152_and_building_32);

		CellularReport cell_at_115_for_person_152_and_building_32 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(115));
		cell_at_115_for_person_152_and_building_32.setPerson(testDriver.createRelationship(Person.class, PERSON_152));
		cell_at_115_for_person_152_and_building_32.setBuilding((testDriver.createRelationship(Building.class, BUILDING_32)));
//		testDriver.submitEvent(cell_at_115_for_person_152_and_building_32);

		CellularReport cell_at_130_for_person_152_and_building_32 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(130));
		cell_at_130_for_person_152_and_building_32.setPerson(testDriver.createRelationship(Person.class, PERSON_152));
		cell_at_130_for_person_152_and_building_32.setBuilding((testDriver.createRelationship(Building.class, BUILDING_32)));
//		testDriver.submitEvent(cell_at_130_for_person_152_and_building_32);

		CellularReport cell_at_145_for_person_152_and_building_32 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(145));
		cell_at_145_for_person_152_and_building_32.setPerson(testDriver.createRelationship(Person.class, PERSON_152));
		cell_at_145_for_person_152_and_building_32.setBuilding((testDriver.createRelationship(Building.class, BUILDING_32)));
//		testDriver.submitEvent(cell_at_145_for_person_152_and_building_32);

		CellularReport cell_at_160_for_person_152_and_building_32 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(160));
		cell_at_160_for_person_152_and_building_32.setPerson(testDriver.createRelationship(Person.class, PERSON_152));
		cell_at_160_for_person_152_and_building_32.setBuilding((testDriver.createRelationship(Building.class, BUILDING_32)));
//		testDriver.submitEvent(cell_at_160_for_person_152_and_building_32);

		CellularReport cell_at_175_for_person_152_and_building_32 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(175));
		cell_at_175_for_person_152_and_building_32.setPerson(testDriver.createRelationship(Person.class, PERSON_152));
		cell_at_175_for_person_152_and_building_32.setBuilding((testDriver.createRelationship(Building.class, BUILDING_32)));
//		testDriver.submitEvent(cell_at_175_for_person_152_and_building_32);

		CellularReport cell_at_190_for_person_152_and_building_32 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(190));
		cell_at_190_for_person_152_and_building_32.setPerson(testDriver.createRelationship(Person.class, PERSON_152));
		cell_at_190_for_person_152_and_building_32.setBuilding((testDriver.createRelationship(Building.class, BUILDING_32)));
//		testDriver.submitEvent(cell_at_190_for_person_152_and_building_32);

		CellularReport cell_at_205_for_person_152_and_building_32 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(205));
		cell_at_205_for_person_152_and_building_32.setPerson(testDriver.createRelationship(Person.class, PERSON_152));
		cell_at_205_for_person_152_and_building_32.setBuilding((testDriver.createRelationship(Building.class, BUILDING_32)));
//		testDriver.submitEvent(cell_at_205_for_person_152_and_building_32);

		CellularReport cell_at_220_for_person_152_and_building_32 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(220));
		cell_at_220_for_person_152_and_building_32.setPerson(testDriver.createRelationship(Person.class, PERSON_152));
		cell_at_220_for_person_152_and_building_32.setBuilding((testDriver.createRelationship(Building.class, BUILDING_32)));
//		testDriver.submitEvent(cell_at_220_for_person_152_and_building_32);

		CellularReport cell_at_235_for_person_152_and_building_32 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(235));
		cell_at_235_for_person_152_and_building_32.setPerson(testDriver.createRelationship(Person.class, PERSON_152));
		cell_at_235_for_person_152_and_building_32.setBuilding((testDriver.createRelationship(Building.class, BUILDING_32)));
//		testDriver.submitEvent(cell_at_235_for_person_152_and_building_32);

		CellularReport cell_at_250_for_person_152_and_building_32 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(250));
		cell_at_250_for_person_152_and_building_32.setPerson(testDriver.createRelationship(Person.class, PERSON_152));
		cell_at_250_for_person_152_and_building_32.setBuilding((testDriver.createRelationship(Building.class, BUILDING_32)));
//		testDriver.submitEvent(cell_at_250_for_person_152_and_building_32);

		CellularReport cell_at_265_for_person_152_and_building_32 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(265));
		cell_at_265_for_person_152_and_building_32.setPerson(testDriver.createRelationship(Person.class, PERSON_152));
		cell_at_265_for_person_152_and_building_32.setBuilding((testDriver.createRelationship(Building.class, BUILDING_32)));
//		testDriver.submitEvent(cell_at_265_for_person_152_and_building_32);

		CellularReport cell_at_280_for_person_152_and_building_32 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(280));
		cell_at_280_for_person_152_and_building_32.setPerson(testDriver.createRelationship(Person.class, PERSON_152));
		cell_at_280_for_person_152_and_building_32.setBuilding((testDriver.createRelationship(Building.class, BUILDING_32)));
//		testDriver.submitEvent(cell_at_280_for_person_152_and_building_32);

		CellularReport cell_at_295_for_person_152_and_building_32 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(295));
		cell_at_295_for_person_152_and_building_32.setPerson(testDriver.createRelationship(Person.class, PERSON_152));
		cell_at_295_for_person_152_and_building_32.setBuilding((testDriver.createRelationship(Building.class, BUILDING_32)));
//		testDriver.submitEvent(cell_at_295_for_person_152_and_building_32);

		CellularReport cell_at_310_for_person_152_and_building_32 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(310));
		cell_at_310_for_person_152_and_building_32.setPerson(testDriver.createRelationship(Person.class, PERSON_152));
		cell_at_310_for_person_152_and_building_32.setBuilding((testDriver.createRelationship(Building.class, BUILDING_32)));
//		testDriver.submitEvent(cell_at_310_for_person_152_and_building_32);

		CellularReport cell_at_325_for_person_152_and_building_32 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(325));
		cell_at_325_for_person_152_and_building_32.setPerson(testDriver.createRelationship(Person.class, PERSON_152));
		cell_at_325_for_person_152_and_building_32.setBuilding((testDriver.createRelationship(Building.class, BUILDING_32)));
//		testDriver.submitEvent(cell_at_325_for_person_152_and_building_32);

		CellularReport cell_at_340_for_person_152_and_building_32 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(340));
		cell_at_340_for_person_152_and_building_32.setPerson(testDriver.createRelationship(Person.class, PERSON_152));
		cell_at_340_for_person_152_and_building_32.setBuilding((testDriver.createRelationship(Building.class, BUILDING_32)));
//		testDriver.submitEvent(cell_at_340_for_person_152_and_building_32);

		// cellular reports for person 82

		//cam-T5-ss at 15
		CellularReport cell_at_10_for_person_82_at_building_cam_T5_ss = conceptFactory.createCellularReport(absoluteTime.plusMinutes(10));
		cell_at_10_for_person_82_at_building_cam_T5_ss.setPerson(testDriver.createRelationship(Person.class, PERSON_82));
		cell_at_10_for_person_82_at_building_cam_T5_ss.setBuilding((testDriver.createRelationship(Building.class, "cam-T5-ss")));
//		testDriver.submitEvent(cell_at_10_for_person_82_at_building_cam_T5_ss);		

		CellularReport cell_at_15_for_person_82_and_building_18 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(15));
		cell_at_15_for_person_82_and_building_18.setPerson(testDriver.createRelationship(Person.class, PERSON_82));
		cell_at_15_for_person_82_and_building_18.setBuilding((testDriver.createRelationship(Building.class, BUILDING_18)));
//		testDriver.submitEvent(cell_at_15_for_person_82_and_building_18);

		//cam-T3-ee at 20
		CellularReport cell_at_20_for_person_82_at_building_cam_T3_ee = conceptFactory.createCellularReport(absoluteTime.plusMinutes(20));
		cell_at_20_for_person_82_at_building_cam_T3_ee.setPerson(testDriver.createRelationship(Person.class, PERSON_82));
		cell_at_20_for_person_82_at_building_cam_T3_ee.setBuilding((testDriver.createRelationship(Building.class, "cam-T3-ee")));
//		testDriver.submitEvent(cell_at_20_for_person_82_at_building_cam_T3_ee);	

		//cam-T3-ee at 30
		CellularReport cell_at_30_for_person_82_at_building_cam_T3_ee = conceptFactory.createCellularReport(absoluteTime.plusMinutes(30));
		cell_at_30_for_person_82_at_building_cam_T3_ee.setPerson(testDriver.createRelationship(Person.class, PERSON_82));
		cell_at_30_for_person_82_at_building_cam_T3_ee.setBuilding((testDriver.createRelationship(Building.class, "cam-T3-ee")));
//		testDriver.submitEvent(cell_at_30_for_person_82_at_building_cam_T3_ee);

		//cam-T4-ne at 40
		CellularReport cell_at_40_for_person_82_at_building_cam_T4_ne = conceptFactory.createCellularReport(absoluteTime.plusMinutes(40));
		cell_at_40_for_person_82_at_building_cam_T4_ne.setPerson(testDriver.createRelationship(Person.class, PERSON_82));
		cell_at_40_for_person_82_at_building_cam_T4_ne.setBuilding((testDriver.createRelationship(Building.class, "cam-T4-ne")));
//		testDriver.submitEvent(cell_at_40_for_person_82_at_building_cam_T4_ne);

		CellularReport cell_at_55_for_person_82_and_building_18 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(55));
		cell_at_55_for_person_82_and_building_18.setPerson(testDriver.createRelationship(Person.class, PERSON_82));
		cell_at_55_for_person_82_and_building_18.setBuilding((testDriver.createRelationship(Building.class, BUILDING_18)));
//		testDriver.submitEvent(cell_at_55_for_person_82_and_building_18);

		CellularReport cell_at_70_for_person_82_and_building_18 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(70));
		cell_at_70_for_person_82_and_building_18.setPerson(testDriver.createRelationship(Person.class, PERSON_82));
		cell_at_70_for_person_82_and_building_18.setBuilding((testDriver.createRelationship(Building.class, BUILDING_18)));
//		testDriver.submitEvent(cell_at_70_for_person_82_and_building_18);

		CellularReport cell_at_85_for_person_82_and_building_18 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(85));
		cell_at_85_for_person_82_and_building_18.setPerson(testDriver.createRelationship(Person.class, PERSON_82));
		cell_at_85_for_person_82_and_building_18.setBuilding((testDriver.createRelationship(Building.class, BUILDING_18)));
//		testDriver.submitEvent(cell_at_85_for_person_82_and_building_18);

		CellularReport cell_at_100_for_person_82_and_building_18 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(100));
		cell_at_100_for_person_82_and_building_18.setPerson(testDriver.createRelationship(Person.class, PERSON_82));
		cell_at_100_for_person_82_and_building_18.setBuilding((testDriver.createRelationship(Building.class, BUILDING_18)));
//		testDriver.submitEvent(cell_at_100_for_person_82_and_building_18);

		CellularReport cell_at_115_for_person_82_and_building_18 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(115));
		cell_at_115_for_person_82_and_building_18.setPerson(testDriver.createRelationship(Person.class, PERSON_82));
		cell_at_115_for_person_82_and_building_18.setBuilding((testDriver.createRelationship(Building.class, BUILDING_18)));
//		testDriver.submitEvent(cell_at_115_for_person_82_and_building_18);

		CellularReport cell_at_130_for_person_82_and_building_18 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(130));
		cell_at_130_for_person_82_and_building_18.setPerson(testDriver.createRelationship(Person.class, PERSON_82));
		cell_at_130_for_person_82_and_building_18.setBuilding((testDriver.createRelationship(Building.class, BUILDING_18)));
//		testDriver.submitEvent(cell_at_130_for_person_82_and_building_18);

		CellularReport cell_at_145_for_person_82_and_building_18 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(145));
		cell_at_145_for_person_82_and_building_18.setPerson(testDriver.createRelationship(Person.class, PERSON_82));
		cell_at_145_for_person_82_and_building_18.setBuilding((testDriver.createRelationship(Building.class, BUILDING_18)));
//		testDriver.submitEvent(cell_at_145_for_person_82_and_building_18);

		CellularReport cell_at_160_for_person_82_and_building_18 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(160));
		cell_at_160_for_person_82_and_building_18.setPerson(testDriver.createRelationship(Person.class, PERSON_82));
		cell_at_160_for_person_82_and_building_18.setBuilding((testDriver.createRelationship(Building.class, BUILDING_18)));
//		testDriver.submitEvent(cell_at_160_for_person_82_and_building_18);

		CellularReport cell_at_175_for_person_82_and_building_18 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(175));
		cell_at_175_for_person_82_and_building_18.setPerson(testDriver.createRelationship(Person.class, PERSON_82));
		cell_at_175_for_person_82_and_building_18.setBuilding((testDriver.createRelationship(Building.class, BUILDING_18)));
//		testDriver.submitEvent(cell_at_175_for_person_82_and_building_18);

		CellularReport cell_at_190_for_person_82_and_building_18 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(190));
		cell_at_190_for_person_82_and_building_18.setPerson(testDriver.createRelationship(Person.class, PERSON_82));
		cell_at_190_for_person_82_and_building_18.setBuilding((testDriver.createRelationship(Building.class, BUILDING_18)));
//		testDriver.submitEvent(cell_at_190_for_person_82_and_building_18);

		CellularReport cell_at_205_for_person_82_and_building_18 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(205));
		cell_at_205_for_person_82_and_building_18.setPerson(testDriver.createRelationship(Person.class, PERSON_82));
		cell_at_205_for_person_82_and_building_18.setBuilding((testDriver.createRelationship(Building.class, BUILDING_18)));
//		testDriver.submitEvent(cell_at_205_for_person_82_and_building_18);

		CellularReport cell_at_220_for_person_82_and_building_18 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(220));
		cell_at_220_for_person_82_and_building_18.setPerson(testDriver.createRelationship(Person.class, PERSON_82));
		cell_at_220_for_person_82_and_building_18.setBuilding((testDriver.createRelationship(Building.class, BUILDING_18)));
//		testDriver.submitEvent(cell_at_220_for_person_82_and_building_18);

		CellularReport cell_at_235_for_person_82_and_building_18 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(235));
		cell_at_235_for_person_82_and_building_18.setPerson(testDriver.createRelationship(Person.class, PERSON_82));
		cell_at_235_for_person_82_and_building_18.setBuilding((testDriver.createRelationship(Building.class, BUILDING_18)));
//		testDriver.submitEvent(cell_at_235_for_person_82_and_building_18);

		CellularReport cell_at_250_for_person_82_and_building_18 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(250));
		cell_at_250_for_person_82_and_building_18.setPerson(testDriver.createRelationship(Person.class, PERSON_82));
		cell_at_250_for_person_82_and_building_18.setBuilding((testDriver.createRelationship(Building.class, BUILDING_18)));
//		testDriver.submitEvent(cell_at_250_for_person_82_and_building_18);

		CellularReport cell_at_265_for_person_82_and_building_18 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(265));
		cell_at_265_for_person_82_and_building_18.setPerson(testDriver.createRelationship(Person.class, PERSON_82));
		cell_at_265_for_person_82_and_building_18.setBuilding((testDriver.createRelationship(Building.class, BUILDING_18)));
//		testDriver.submitEvent(cell_at_265_for_person_82_and_building_18);

		CellularReport cell_at_280_for_person_82_and_building_18 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(280));
		cell_at_280_for_person_82_and_building_18.setPerson(testDriver.createRelationship(Person.class, PERSON_82));
		cell_at_280_for_person_82_and_building_18.setBuilding((testDriver.createRelationship(Building.class, BUILDING_18)));
//		testDriver.submitEvent(cell_at_280_for_person_82_and_building_18);

		CellularReport cell_at_295_for_person_82_and_building_18 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(295));
		cell_at_295_for_person_82_and_building_18.setPerson(testDriver.createRelationship(Person.class, PERSON_82));
		cell_at_295_for_person_82_and_building_18.setBuilding((testDriver.createRelationship(Building.class, BUILDING_18)));
//		testDriver.submitEvent(cell_at_295_for_person_82_and_building_18);

		CellularReport cell_at_310_for_person_82_and_building_18 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(310));
		cell_at_310_for_person_82_and_building_18.setPerson(testDriver.createRelationship(Person.class, PERSON_82));
		cell_at_310_for_person_82_and_building_18.setBuilding((testDriver.createRelationship(Building.class, BUILDING_18)));
//		testDriver.submitEvent(cell_at_310_for_person_82_and_building_18);

		CellularReport cell_at_325_for_person_82_and_building_18 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(325));
		cell_at_325_for_person_82_and_building_18.setPerson(testDriver.createRelationship(Person.class, PERSON_82));
		cell_at_325_for_person_82_and_building_18.setBuilding((testDriver.createRelationship(Building.class, BUILDING_18)));
//		testDriver.submitEvent(cell_at_325_for_person_82_and_building_18);

		CellularReport cell_at_340_for_person_82_and_building_18 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(340));
		cell_at_340_for_person_82_and_building_18.setPerson(testDriver.createRelationship(Person.class, PERSON_82));
		cell_at_340_for_person_82_and_building_18.setBuilding((testDriver.createRelationship(Building.class, BUILDING_18)));
//		testDriver.submitEvent(cell_at_340_for_person_82_and_building_18);

		// cellular reports for person 271

		//cam-T8-ww at 10
		CellularReport cell_at_10_for_person_271_and_building_cam_T8_ww = conceptFactory.createCellularReport(absoluteTime.plusMinutes(10));
		cell_at_10_for_person_271_and_building_cam_T8_ww.setPerson(testDriver.createRelationship(Person.class, PERSON_271));
		cell_at_10_for_person_271_and_building_cam_T8_ww.setBuilding((testDriver.createRelationship(Building.class, "cam-T8-ww")));
//		testDriver.submitEvent(cell_at_10_for_person_271_and_building_cam_T8_ww);

		//cam-T4-ne at 20
		CellularReport cell_at_20_for_person_271_and_building_cam_T4_ne = conceptFactory.createCellularReport(absoluteTime.plusMinutes(20));
		cell_at_20_for_person_271_and_building_cam_T4_ne.setPerson(testDriver.createRelationship(Person.class, PERSON_271));
		cell_at_20_for_person_271_and_building_cam_T4_ne.setBuilding((testDriver.createRelationship(Building.class, "cam-T4-ne")));
//		testDriver.submitEvent(cell_at_20_for_person_271_and_building_cam_T4_ne);

		//cam-T4-ne at 30
		CellularReport cell_at_30_for_person_271_and_building_cam_T4_ne = conceptFactory.createCellularReport(absoluteTime.plusMinutes(30));
		cell_at_30_for_person_271_and_building_cam_T4_ne.setPerson(testDriver.createRelationship(Person.class, PERSON_271));
		cell_at_30_for_person_271_and_building_cam_T4_ne.setBuilding((testDriver.createRelationship(Building.class, "cam-T4-ne")));
//		testDriver.submitEvent(cell_at_30_for_person_271_and_building_cam_T4_ne);

		//cam-T4-ne at 40
		CellularReport cell_at_40_for_person_271_and_building_cam_T4_ne = conceptFactory.createCellularReport(absoluteTime.plusMinutes(40));
		cell_at_40_for_person_271_and_building_cam_T4_ne.setPerson(testDriver.createRelationship(Person.class, PERSON_271));
		cell_at_40_for_person_271_and_building_cam_T4_ne.setBuilding((testDriver.createRelationship(Building.class, "cam-T4-ne")));
//		testDriver.submitEvent(cell_at_40_for_person_271_and_building_cam_T4_ne);

		//bld-To-74 at 45
		String building_74 = "bld-To-74";

		CellularReport cell_at_45_for_person_271_and_building_74 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(45));
		cell_at_45_for_person_271_and_building_74.setPerson(testDriver.createRelationship(Person.class, PERSON_271));
		cell_at_45_for_person_271_and_building_74.setBuilding((testDriver.createRelationship(Building.class, building_74)));
//		testDriver.submitEvent(cell_at_45_for_person_271_and_building_74);

		// entering BUILDING_80
		CellularReport cell_at_50_for_person_271_and_building_80 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(50));
		cell_at_50_for_person_271_and_building_80.setPerson(testDriver.createRelationship(Person.class, PERSON_271));
		cell_at_50_for_person_271_and_building_80.setBuilding((testDriver.createRelationship(Building.class, BUILDING_80)));
//		testDriver.submitEvent(cell_at_50_for_person_271_and_building_80);

		CellularReport cell_at_70_for_person_271_and_building_80 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(70));
		cell_at_70_for_person_271_and_building_80.setPerson(testDriver.createRelationship(Person.class, PERSON_271));
		cell_at_70_for_person_271_and_building_80.setBuilding((testDriver.createRelationship(Building.class, BUILDING_80)));
//		testDriver.submitEvent(cell_at_70_for_person_271_and_building_80);

		CellularReport cell_at_75_for_person_271_and_building_80 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(75));
		cell_at_75_for_person_271_and_building_80.setPerson(testDriver.createRelationship(Person.class, PERSON_271));
		cell_at_75_for_person_271_and_building_80.setBuilding((testDriver.createRelationship(Building.class, BUILDING_80)));
//		testDriver.submitEvent(cell_at_75_for_person_271_and_building_80);

		CellularReport cell_at_80_for_person_271_and_building_80 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(80));
		cell_at_80_for_person_271_and_building_80.setPerson(testDriver.createRelationship(Person.class, PERSON_271));
		cell_at_80_for_person_271_and_building_80.setBuilding((testDriver.createRelationship(Building.class, BUILDING_80)));
//		testDriver.submitEvent(cell_at_80_for_person_271_and_building_80);

		CellularReport cell_at_100_for_person_271_and_building_80 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(100));
		cell_at_100_for_person_271_and_building_80.setPerson(testDriver.createRelationship(Person.class, PERSON_271));
		cell_at_100_for_person_271_and_building_80.setBuilding((testDriver.createRelationship(Building.class, BUILDING_80)));
//		testDriver.submitEvent(cell_at_100_for_person_271_and_building_80);

		CellularReport cell_at_120_for_person_271_and_building_80 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(120));
		cell_at_120_for_person_271_and_building_80.setPerson(testDriver.createRelationship(Person.class, PERSON_271));
		cell_at_120_for_person_271_and_building_80.setBuilding((testDriver.createRelationship(Building.class, BUILDING_80)));
//		testDriver.submitEvent(cell_at_120_for_person_271_and_building_80);

		CellularReport cell_at_140_for_person_271_and_building_80 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(140));
		cell_at_140_for_person_271_and_building_80.setPerson(testDriver.createRelationship(Person.class, PERSON_271));
		cell_at_140_for_person_271_and_building_80.setBuilding((testDriver.createRelationship(Building.class, BUILDING_80)));
//		testDriver.submitEvent(cell_at_140_for_person_271_and_building_80);

		// entering BUILDING_52
		CellularReport cell_at_200_for_person_271_and_building_52 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(200));
		cell_at_200_for_person_271_and_building_52.setPerson(testDriver.createRelationship(Person.class, PERSON_271));
		cell_at_200_for_person_271_and_building_52.setBuilding((testDriver.createRelationship(Building.class, BUILDING_52)));
//		testDriver.submitEvent(cell_at_200_for_person_271_and_building_52);

		CellularReport cell_at_220_for_person_271_and_building_52 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(220));
		cell_at_220_for_person_271_and_building_52.setPerson(testDriver.createRelationship(Person.class, PERSON_271));
		cell_at_220_for_person_271_and_building_52.setBuilding((testDriver.createRelationship(Building.class, BUILDING_52)));
//		testDriver.submitEvent(cell_at_220_for_person_271_and_building_52);

		CellularReport cell_at_240_for_person_271_and_building_52 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(240));
		cell_at_240_for_person_271_and_building_52.setPerson(testDriver.createRelationship(Person.class, PERSON_271));
		cell_at_240_for_person_271_and_building_52.setBuilding((testDriver.createRelationship(Building.class, BUILDING_52)));
//		testDriver.submitEvent(cell_at_240_for_person_271_and_building_52);

		CellularReport cell_at_300_for_person_271_and_building_52 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(300));
		cell_at_300_for_person_271_and_building_52.setPerson(testDriver.createRelationship(Person.class, PERSON_271));
		cell_at_300_for_person_271_and_building_52.setBuilding((testDriver.createRelationship(Building.class, BUILDING_52)));
//		testDriver.submitEvent(cell_at_300_for_person_271_and_building_52);

		CellularReport cell_at_400_for_person_271_and_building_52 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(400));
		cell_at_400_for_person_271_and_building_52.setPerson(testDriver.createRelationship(Person.class, PERSON_271));
		cell_at_400_for_person_271_and_building_52.setBuilding((testDriver.createRelationship(Building.class, BUILDING_52)));
//		testDriver.submitEvent(cell_at_400_for_person_271_and_building_52);

		CellularReport cell_at_600_for_person_271_and_building_52 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(600));
		cell_at_600_for_person_271_and_building_52.setPerson(testDriver.createRelationship(Person.class, PERSON_271));
		cell_at_600_for_person_271_and_building_52.setBuilding((testDriver.createRelationship(Building.class, BUILDING_52)));
//		testDriver.submitEvent(cell_at_600_for_person_271_and_building_52);

		CellularReport cell_at_800_for_person_271_and_building_52 = conceptFactory.createCellularReport(absoluteTime.plusMinutes(800));
		cell_at_800_for_person_271_and_building_52.setPerson(testDriver.createRelationship(Person.class, PERSON_271));
		cell_at_800_for_person_271_and_building_52.setBuilding((testDriver.createRelationship(Building.class, BUILDING_52)));
//		testDriver.submitEvent(cell_at_800_for_person_271_and_building_52);


		// cellular reports for person 182

		//cam-T4-ne at 30

		CellularReport cell_at_30_for_person_182_and_building_cam_T4_ne = conceptFactory.createCellularReport(absoluteTime.plusMinutes(30));
		cell_at_30_for_person_182_and_building_cam_T4_ne.setPerson(testDriver.createRelationship(Person.class, PERSON_182));
		cell_at_30_for_person_182_and_building_cam_T4_ne.setBuilding((testDriver.createRelationship(Building.class, "cam-T4-ne")));
//		testDriver.submitEvent(cell_at_30_for_person_182_and_building_cam_T4_ne);

		//cam-T6-nn at 40
		CellularReport cell_at_40_for_person_182_and_building_cam_T6_nn= conceptFactory.createCellularReport(absoluteTime.plusMinutes(40));
		cell_at_40_for_person_182_and_building_cam_T6_nn.setPerson(testDriver.createRelationship(Person.class, PERSON_182));
		cell_at_40_for_person_182_and_building_cam_T6_nn.setBuilding((testDriver.createRelationship(Building.class, "cam-T6-nn")));
//		testDriver.submitEvent(cell_at_40_for_person_182_and_building_cam_T6_nn);

		//cam-T1-cc at 50
		CellularReport cell_at_50_for_person_182_and_building_cam_T1_cc= conceptFactory.createCellularReport(absoluteTime.plusMinutes(50));
		cell_at_50_for_person_182_and_building_cam_T1_cc.setPerson(testDriver.createRelationship(Person.class, PERSON_182));
		cell_at_50_for_person_182_and_building_cam_T1_cc.setBuilding((testDriver.createRelationship(Building.class, "cam-T1-cc")));
//		testDriver.submitEvent(cell_at_50_for_person_182_and_building_cam_T1_cc);

		//cam-T8-ww at 60
		CellularReport cell_at_60_for_person_182_and_building_cam_T8_ww= conceptFactory.createCellularReport(absoluteTime.plusMinutes(60));
		cell_at_60_for_person_182_and_building_cam_T8_ww.setPerson(testDriver.createRelationship(Person.class, PERSON_182));
		cell_at_60_for_person_182_and_building_cam_T8_ww.setBuilding((testDriver.createRelationship(Building.class, "cam-T8-ww")));
//		testDriver.submitEvent(cell_at_60_for_person_182_and_building_cam_T8_ww);

		// entered BUILDING_80
		CellularReport cell_at_80_for_person_182_and_building_80= conceptFactory.createCellularReport(absoluteTime.plusMinutes(80));
		cell_at_80_for_person_182_and_building_80.setPerson(testDriver.createRelationship(Person.class, PERSON_182));
		cell_at_80_for_person_182_and_building_80.setBuilding((testDriver.createRelationship(Building.class, BUILDING_80)));
//		testDriver.submitEvent(cell_at_80_for_person_182_and_building_80);

		CellularReport cell_at_100_for_person_182_and_building_80= conceptFactory.createCellularReport(absoluteTime.plusMinutes(100));
		cell_at_100_for_person_182_and_building_80.setPerson(testDriver.createRelationship(Person.class, PERSON_182));
		cell_at_100_for_person_182_and_building_80.setBuilding((testDriver.createRelationship(Building.class, BUILDING_80)));
//		testDriver.submitEvent(cell_at_100_for_person_182_and_building_80);

		CellularReport cell_at_120_for_person_182_and_building_80= conceptFactory.createCellularReport(absoluteTime.plusMinutes(120));
		cell_at_120_for_person_182_and_building_80.setPerson(testDriver.createRelationship(Person.class, PERSON_182));
		cell_at_120_for_person_182_and_building_80.setBuilding((testDriver.createRelationship(Building.class, BUILDING_80)));
//		testDriver.submitEvent(cell_at_120_for_person_182_and_building_80);

		CellularReport cell_at_140_for_person_182_and_building_80= conceptFactory.createCellularReport(absoluteTime.plusMinutes(140));
		cell_at_140_for_person_182_and_building_80.setPerson(testDriver.createRelationship(Person.class, PERSON_182));
		cell_at_140_for_person_182_and_building_80.setBuilding((testDriver.createRelationship(Building.class, BUILDING_80)));
//		testDriver.submitEvent(cell_at_140_for_person_182_and_building_80);

		//entered BUILDING_52
		CellularReport cell_at_200_for_person_182_and_building_52= conceptFactory.createCellularReport(absoluteTime.plusMinutes(200));
		cell_at_200_for_person_182_and_building_52.setPerson(testDriver.createRelationship(Person.class, PERSON_182));
		cell_at_200_for_person_182_and_building_52.setBuilding((testDriver.createRelationship(Building.class, BUILDING_52)));
//		testDriver.submitEvent(cell_at_200_for_person_182_and_building_52);

		CellularReport cell_at_220_for_person_182_and_building_52= conceptFactory.createCellularReport(absoluteTime.plusMinutes(220));
		cell_at_220_for_person_182_and_building_52.setPerson(testDriver.createRelationship(Person.class, PERSON_182));
		cell_at_220_for_person_182_and_building_52.setBuilding((testDriver.createRelationship(Building.class, BUILDING_52)));
//		testDriver.submitEvent(cell_at_220_for_person_182_and_building_52);

		CellularReport cell_at_240_for_person_182_and_building_52= conceptFactory.createCellularReport(absoluteTime.plusMinutes(240));
		cell_at_240_for_person_182_and_building_52.setPerson(testDriver.createRelationship(Person.class, PERSON_182));
		cell_at_240_for_person_182_and_building_52.setBuilding((testDriver.createRelationship(Building.class, BUILDING_52)));
//		testDriver.submitEvent(cell_at_240_for_person_182_and_building_52);

		CellularReport cell_at_300_for_person_182_and_building_52= conceptFactory.createCellularReport(absoluteTime.plusMinutes(300));
		cell_at_300_for_person_182_and_building_52.setPerson(testDriver.createRelationship(Person.class, PERSON_182));
		cell_at_300_for_person_182_and_building_52.setBuilding((testDriver.createRelationship(Building.class, BUILDING_52)));
//		testDriver.submitEvent(cell_at_300_for_person_182_and_building_52);

		CellularReport cell_at_400_for_person_182_and_building_52= conceptFactory.createCellularReport(absoluteTime.plusMinutes(400));
		cell_at_400_for_person_182_and_building_52.setPerson(testDriver.createRelationship(Person.class, PERSON_182));
		cell_at_400_for_person_182_and_building_52.setBuilding((testDriver.createRelationship(Building.class, BUILDING_52)));
//		testDriver.submitEvent(cell_at_400_for_person_182_and_building_52);

		CellularReport cell_at_600_for_person_182_and_building_52= conceptFactory.createCellularReport(absoluteTime.plusMinutes(600));
		cell_at_600_for_person_182_and_building_52.setPerson(testDriver.createRelationship(Person.class, PERSON_182));
		cell_at_600_for_person_182_and_building_52.setBuilding((testDriver.createRelationship(Building.class, BUILDING_52)));
//		testDriver.submitEvent(cell_at_600_for_person_182_and_building_52);

		CellularReport cell_at_800_for_person_182_and_building_52= conceptFactory.createCellularReport(absoluteTime.plusMinutes(800));
		cell_at_800_for_person_182_and_building_52.setPerson(testDriver.createRelationship(Person.class, PERSON_182));
		cell_at_800_for_person_182_and_building_52.setBuilding((testDriver.createRelationship(Building.class, BUILDING_52)));
//		testDriver.submitEvent(cell_at_800_for_person_182_and_building_52);

		// cellular calls
		CellularCallReport call_at_10_PERSON_82_PERSON_271 = conceptFactory.createCellularCallReport(absoluteTime.plusMinutes(10));
		call_at_10_PERSON_82_PERSON_271.setCaller(testDriver.createRelationship(Person.class, PERSON_82));
		call_at_10_PERSON_82_PERSON_271.setCallee(testDriver.createRelationship(Person.class, PERSON_271));
		call_at_10_PERSON_82_PERSON_271.set_persons(persons);
		call_at_10_PERSON_82_PERSON_271.set_buildings(buildings);
//		testDriver.submitEvent(call_at_10_PERSON_82_PERSON_271);

		CellularCallReport call_at_45_PERSON_82_PERSON_271 = conceptFactory.createCellularCallReport(absoluteTime.plusMinutes(45));
		call_at_45_PERSON_82_PERSON_271.setCaller(testDriver.createRelationship(Person.class, PERSON_82));
		call_at_45_PERSON_82_PERSON_271.setCallee(testDriver.createRelationship(Person.class, PERSON_271));
		call_at_45_PERSON_82_PERSON_271.set_persons(persons);
		call_at_45_PERSON_82_PERSON_271.set_buildings(buildings);
//		testDriver.submitEvent(call_at_45_PERSON_82_PERSON_271);

		CellularCallReport call_at_100_PERSON_82_PERSON_271 = conceptFactory.createCellularCallReport(absoluteTime.plusMinutes(100));
		call_at_100_PERSON_82_PERSON_271.setCaller(testDriver.createRelationship(Person.class, PERSON_82));
		call_at_100_PERSON_82_PERSON_271.setCallee(testDriver.createRelationship(Person.class, PERSON_271));
		call_at_100_PERSON_82_PERSON_271.set_persons(persons);
		call_at_100_PERSON_82_PERSON_271.set_buildings(buildings);
//		testDriver.submitEvent(call_at_100_PERSON_82_PERSON_271);

		CellularCallReport call_at_200_PERSON_82_PERSON_271 = conceptFactory.createCellularCallReport(absoluteTime.plusMinutes(200));
		call_at_200_PERSON_82_PERSON_271.setCaller(testDriver.createRelationship(Person.class, PERSON_82));
		call_at_200_PERSON_82_PERSON_271.setCallee(testDriver.createRelationship(Person.class, PERSON_271));
		call_at_200_PERSON_82_PERSON_271.set_persons(persons);
		call_at_200_PERSON_82_PERSON_271.set_buildings(buildings);
//		testDriver.submitEvent(call_at_200_PERSON_82_PERSON_271);

		CellularCallReport call_at_250_PERSON_152_PERSON_182 = conceptFactory.createCellularCallReport(absoluteTime.plusMinutes(250));
		call_at_250_PERSON_152_PERSON_182.setCaller(testDriver.createRelationship(Person.class, PERSON_152));
		call_at_250_PERSON_152_PERSON_182.setCallee(testDriver.createRelationship(Person.class, PERSON_182));
		call_at_250_PERSON_152_PERSON_182.set_persons(persons);
		call_at_250_PERSON_152_PERSON_182.set_buildings(buildings);
//		testDriver.submitEvent(call_at_250_PERSON_152_PERSON_182);

		CellularCallReport call_at_700_PERSON_152_PERSON_182 = conceptFactory.createCellularCallReport(absoluteTime.plusMinutes(700));
		call_at_700_PERSON_152_PERSON_182.setCaller(testDriver.createRelationship(Person.class, PERSON_152));
		call_at_700_PERSON_152_PERSON_182.setCallee(testDriver.createRelationship(Person.class, PERSON_182));
		call_at_700_PERSON_152_PERSON_182.set_persons(persons);
		call_at_700_PERSON_152_PERSON_182.set_buildings(buildings);
//		testDriver.submitEvent(call_at_700_PERSON_152_PERSON_182);

		
		//submit events
		testDriver.submitEvent(cell_at_10_for_person_82_at_building_cam_T5_ss);		
		testDriver.submitEvent(cell_at_10_for_person_271_and_building_cam_T8_ww);
		testDriver.submitEvent(call_at_10_PERSON_82_PERSON_271); //82 not criminal
		testDriver.submitEvent(cell_at_15_for_person_82_and_building_18);		
		testDriver.submitEvent(cell_at_15_for_person_152_and_building_32);
		testDriver.submitEvent(cell_at_20_for_person_271_and_building_cam_T4_ne);
		testDriver.submitEvent(cell_at_20_for_person_82_at_building_cam_T3_ee);	
		testDriver.submitEvent(cell_at_20_for_person_152_and_building_cam_T8_ww);
		testDriver.submitEvent(cell_at_30_for_person_182_and_building_cam_T4_ne);
		testDriver.submitEvent(cell_at_30_for_person_271_and_building_cam_T4_ne);
		testDriver.submitEvent(cell_at_30_for_person_82_at_building_cam_T3_ee);
		testDriver.submitEvent(cell_at_30_for_person_152_and_building_cam_T4_ne);
		testDriver.submitEvent(cell_at_40_for_person_182_and_building_cam_T6_nn);
		testDriver.submitEvent(cell_at_40_for_person_271_and_building_cam_T4_ne);
		testDriver.submitEvent(cell_at_40_for_person_82_at_building_cam_T4_ne);
		testDriver.submitEvent(cell_at_40_for_person_152_and_building_cam_T4_ne);
		testDriver.submitEvent(cell_at_45_for_person_271_and_building_74);
		testDriver.submitEvent(call_at_45_PERSON_82_PERSON_271); //271 not in bank
		testDriver.submitEvent(cell_at_50_for_person_182_and_building_cam_T1_cc);
		testDriver.submitEvent(cell_at_50_for_person_271_and_building_80);
		testDriver.submitEvent(cell_at_50_for_person_152_and_building_cam_T4_ne);
		testDriver.submitEvent(cell_at_55_for_person_82_and_building_18);
		testDriver.submitEvent(cell_at_55_for_person_152_and_building_32);
		testDriver.submitEvent(cell_at_60_for_person_182_and_building_cam_T8_ww);
		testDriver.submitEvent(cell_at_70_for_person_271_and_building_80);
		testDriver.submitEvent(cell_at_70_for_person_82_and_building_18);
		testDriver.submitEvent(cell_at_70_for_person_152_and_building_32);
		testDriver.submitEvent(cell_at_75_for_person_271_and_building_80);
		testDriver.submitEvent(cell_at_80_for_person_182_and_building_80);
		testDriver.submitEvent(cell_at_80_for_person_271_and_building_80);
		testDriver.submitEvent(cell_at_85_for_person_82_and_building_18);
		testDriver.submitEvent(cell_at_85_for_person_152_and_building_32);
		testDriver.submitEvent(cell_at_100_for_person_182_and_building_80);
		testDriver.submitEvent(cell_at_100_for_person_271_and_building_80);
		testDriver.submitEvent(cell_at_100_for_person_82_and_building_18);
		testDriver.submitEvent(cell_at_100_for_person_152_and_building_32);
		testDriver.submitEvent(call_at_100_PERSON_82_PERSON_271); //82 criminal, 271 in bank
		testDriver.submitEvent(cell_at_115_for_person_82_and_building_18);
		testDriver.submitEvent(cell_at_115_for_person_152_and_building_32);
		testDriver.submitEvent(cell_at_120_for_person_182_and_building_80);
		testDriver.submitEvent(cell_at_120_for_person_271_and_building_80);
		testDriver.submitEvent(cell_at_130_for_person_82_and_building_18);
		testDriver.submitEvent(cell_at_130_for_person_152_and_building_32);
		testDriver.submitEvent(cell_at_140_for_person_182_and_building_80);
		testDriver.submitEvent(cell_at_140_for_person_271_and_building_80);
		testDriver.submitEvent(cell_at_145_for_person_82_and_building_18);
		testDriver.submitEvent(cell_at_145_for_person_152_and_building_32);
		testDriver.submitEvent(cell_at_160_for_person_82_and_building_18);
		testDriver.submitEvent(cell_at_160_for_person_152_and_building_32);
		testDriver.submitEvent(cell_at_175_for_person_82_and_building_18);
		testDriver.submitEvent(cell_at_175_for_person_152_and_building_32);
		testDriver.submitEvent(cell_at_190_for_person_82_and_building_18);
		testDriver.submitEvent(cell_at_190_for_person_152_and_building_32);
		testDriver.submitEvent(cell_at_200_for_person_182_and_building_52);
		testDriver.submitEvent(cell_at_200_for_person_271_and_building_52);
		testDriver.submitEvent(call_at_200_PERSON_82_PERSON_271); // doesn't change any thing - 271 not in bank
		testDriver.submitEvent(cell_at_205_for_person_82_and_building_18);
		testDriver.submitEvent(cell_at_205_for_person_152_and_building_32);
		testDriver.submitEvent(cell_at_220_for_person_182_and_building_52);
		testDriver.submitEvent(cell_at_220_for_person_271_and_building_52);
		testDriver.submitEvent(cell_at_220_for_person_82_and_building_18);
		testDriver.submitEvent(cell_at_220_for_person_152_and_building_32);
		testDriver.submitEvent(cell_at_235_for_person_82_and_building_18);
		testDriver.submitEvent(cell_at_235_for_person_152_and_building_32);
		testDriver.submitEvent(cell_at_240_for_person_182_and_building_52);
		testDriver.submitEvent(cell_at_240_for_person_271_and_building_52);
		testDriver.submitEvent(cell_at_250_for_person_82_and_building_18);
		testDriver.submitEvent(cell_at_250_for_person_152_and_building_32);
		testDriver.submitEvent(call_at_250_PERSON_152_PERSON_182);
		testDriver.submitEvent(cell_at_265_for_person_82_and_building_18);
		testDriver.submitEvent(cell_at_265_for_person_152_and_building_32);
		testDriver.submitEvent(cell_at_280_for_person_82_and_building_18);
		testDriver.submitEvent(cell_at_280_for_person_152_and_building_32);
		testDriver.submitEvent(cell_at_295_for_person_82_and_building_18);
		testDriver.submitEvent(cell_at_295_for_person_152_and_building_32);
		testDriver.submitEvent(cell_at_300_for_person_182_and_building_52);
		testDriver.submitEvent(cell_at_300_for_person_271_and_building_52);
		testDriver.submitEvent(cell_at_310_for_person_82_and_building_18);
		testDriver.submitEvent(cell_at_310_for_person_152_and_building_32);
		testDriver.submitEvent(cell_at_325_for_person_82_and_building_18);
		testDriver.submitEvent(cell_at_325_for_person_152_and_building_32);
		testDriver.submitEvent(cell_at_340_for_person_82_and_building_18);
		testDriver.submitEvent(cell_at_340_for_person_152_and_building_32);
		testDriver.submitEvent(cell_at_400_for_person_182_and_building_52);
		testDriver.submitEvent(cell_at_400_for_person_271_and_building_52);
		testDriver.submitEvent(cell_at_600_for_person_182_and_building_52);
		testDriver.submitEvent(cell_at_600_for_person_271_and_building_52);
		testDriver.submitEvent(call_at_700_PERSON_152_PERSON_182);
		testDriver.submitEvent(cell_at_800_for_person_182_and_building_52);
		testDriver.submitEvent(cell_at_800_for_person_271_and_building_52);

		
		
		
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

		Person person_271 = testDriver.fetchEntity(Person.class, PERSON_271);
		Assert.assertNotNull(person_271);
		Relationship<OrganizationalRole> person_271_relation = person_271.getRole();
		Assert.assertNotNull(person_271_relation);
		OrganizationalRole person_271_organization_role = testDriver.fetchEntity(OrganizationalRole.class, person_271_relation.getKey());
		Assert.assertNotNull(person_271_organization_role);
		Organization person_271_organization = testDriver.fetchEntity(Organization.class, person_271_organization_role.getOrganization().getKey());
		Assert.assertNotNull(person_271_organization);
		Assert.assertTrue(OrganizationType.CRIMINAL == person_271_organization.getType());

		Person person_182 = testDriver.fetchEntity(Person.class, PERSON_182);
		Assert.assertNotNull(person_182);
		Relationship<OrganizationalRole> person_182_relation = person_182.getRole();
		Assert.assertNotNull(person_182_relation);
		OrganizationalRole person_182_organization_role = testDriver.fetchEntity(OrganizationalRole.class, person_182_relation.getKey());
		Assert.assertNotNull(person_182_organization_role);
		Organization person_182_organization = testDriver.fetchEntity(Organization.class, person_182_organization_role.getOrganization().getKey());
		Assert.assertNotNull(person_182_organization);
		Assert.assertTrue(OrganizationType.CRIMINAL == person_182_organization.getType());

		// check		

		DebugInfo[] debugInfos = debugReceiver.getDebugInfo("PersonRuleAgent");

		for (DebugInfo debugInfo : debugInfos) {
			System.out.println( "DebugInfo: " + debugInfo );

			Event event = debugInfo.getEvent();
			String eventXml = testDriver.getModelSerializer().serializeEvent(DataFormat.TYPED_XML, event );
			System.out.println( "Event as XML: " + eventXml );
		}
	}	
}

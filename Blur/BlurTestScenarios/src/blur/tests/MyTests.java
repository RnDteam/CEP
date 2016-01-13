package blur.tests;

import java.time.ZonedDateTime;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import blur.model.BuildingInitialization;
import blur.model.*;
import blur.model.ConceptFactory;
import blur.model.Person;
import blur.model.PersonInitialization;
import blur.model.PersonState;

import com.ibm.geolib.geom.Point;
import com.ibm.geolib.st.SpatioTemporalService;
import com.ibm.ia.common.EventFactory;
import com.ibm.ia.common.GatewayException;
import com.ibm.ia.common.RoutingException;
import com.ibm.ia.common.SolutionException;
import com.ibm.ia.model.Relationship;
import com.ibm.ia.testdriver.TestDriver;

public class MyTests {

	protected static TestDriver testDriver;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testDriver = new TestDriver();
		testDriver.connect();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		testDriver.disconnect();
		testDriver = null;
	}

	@Before
	public void setUp() throws Exception {
		testDriver.deleteAllEntities();
		testDriver.resetSolutionState();
		testDriver.startRecording();
	}

	@After
	public void tearDown() throws Exception {
        testDriver.endTest();
		testDriver.stopRecording();
	}

	@Test
	public void test1() throws SolutionException, GatewayException,
			RoutingException, InterruptedException {
		
		ZonedDateTime now = ZonedDateTime.now();
		ConceptFactory cf = testDriver.getConceptFactory( ConceptFactory.class );
		
		PersonInitialization pi = cf.createPersonInitialization(now);
		pi.setName( "person1");
		Relationship<Person> personRel = testDriver.createRelationship( Person.class, "person1");
		pi.setPerson( personRel );
		pi.setState(PersonState.INACTIVE);
		pi.setProfession( "bad guy" );
		Point location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768, 32.085300 );
		pi.setLocation( location );
		testDriver.submitEvent(pi);

		BuildingInitialization bi = cf.createBuildingInitialization( now );
		bi.setType( BuildingType.APPARTMENT );
		bi.setOwner(personRel);
		bi.setUsageType(BuilidngUsageType.BANK_BRANCH);
		bi.setBuilding( testDriver.createRelationship(Building.class, "Crowne Plaza Beach"));
		bi.setLocation(location);
		testDriver.submitEvent(bi);
		
		for( int n=0; n < 100; n++ ) {
			now = now.plusMinutes(10);
			Thread.sleep(2000);
			TrafficCameraReport tcr = cf.createTrafficCameraReport(now);
			tcr.setCameraId( n );
			int vehId = (int) (Math.random() * 10);
			tcr.setVehicle(testDriver.createRelationship( Vehicle.class, "veh-" + vehId ));
			double fuzzx = Math.random();
			double fuzzy = Math.random();
			location = SpatioTemporalService.getService().getGeometryFactory().getPoint( 34.781768 + fuzzy, 32.085300 + fuzzx );
			tcr.setCameraLocation( location );
			testDriver.submitEvent(tcr);
			System.out.println( "Sending TrafficCameraReport");
		}
	}
}
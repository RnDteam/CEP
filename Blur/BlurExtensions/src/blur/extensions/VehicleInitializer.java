package blur.extensions;

<<<<<<< HEAD
import java.util.Random;

=======
>>>>>>> 2b9e66af4fcce98e137dd9f725fc95260bcd5f0f
import blur.model.ConceptFactory;
import blur.model.Organization;
import blur.model.Person;
import blur.model.TrafficCameraReport;
import blur.model.Vehicle;
import blur.model.VehicleDetails;
import blur.model.VehicleStatus;

import com.ibm.ia.common.ComponentException;
import com.ibm.ia.extension.EntityInitializer;
import com.ibm.ia.extension.annotations.EntityInitializerDescriptor;
import com.ibm.ia.model.Event;
import com.ibm.ia.model.Relationship;

import com.ibm.ia.common.ComponentException;
import com.ibm.ia.common.GatewayException;
import com.ibm.ia.common.SolutionException;
import com.ibm.ia.extension.EntityInitializer;
import com.ibm.ia.extension.annotations.EntityInitializerDescriptor;
import com.ibm.ia.model.Event;
import com.ibm.ia.model.Relationship;

@EntityInitializerDescriptor(entityType = Vehicle.class)
public class VehicleInitializer extends EntityInitializer<Vehicle> {

	private Random random;
	private ConceptFactory conceptFactory;

	@Override
	public Vehicle createEntityFromEvent(Event event) throws ComponentException {
		Vehicle entity = super.createEntityFromEvent(event);

		if( event instanceof TrafficCameraReport ) {
			System.out.println( "***** VehicleInitializer createEntityFromEvent ****** " );
			entity.setLocation(((TrafficCameraReport) event).getCameraLocation());
		}

		return entity;
	}

	@Override
	public void initializeEntity(Vehicle entity) throws ComponentException {
		super.initializeEntity(entity);
		System.out.println( "***** VehicleInitializer initializeEntity ****** " );

		String licensePlateNumber = entity.getLicensePlateNumber();
		entity.setLastSeen(null);
		entity.setOrganization(getOrganizationFromES(licensePlateNumber));
		entity.setDetails(getDetailsFromES(licensePlateNumber));
		entity.setOwner(getOwnerFromES(licensePlateNumber));
		entity.setStatus(VehicleStatus.INACTIVE);
		entity.setSuspicious(false);

	}

	private Relationship<Person> getOwnerFromES(String licensePlateNumber) {
//		Person person = conceptFactory.createPerson("person" +random.nextInt(100));
		Person person = conceptFactory.createPerson("1234");
		return getModelFactory().createRelationship(person);
	}

	private Relationship<VehicleDetails> getDetailsFromES(
			String licensePlateNumber) {
		random = new Random();
		conceptFactory = getConceptFactory(ConceptFactory.class);
		
		VehicleDetails myDetails = conceptFactory.createVehicleDetails("Details-" + licensePlateNumber);
		myDetails.setType(VehicleType.MOTORCYCLE);
		myDetails.setMaker("maker" + random.nextInt());
		myDetails.setMaximumSpeed(130);
		
		Relationship<VehicleDetails> detailsRelationship = getModelFactory().createRelationship(myDetails);
		
		return detailsRelationship;
	}

	private Relationship<Organization> getOrganizationFromES(
			String licensePlateNumber) {
		return null;
	}
}
package blur.extensions;

import java.util.Random;

import blur.model.ConceptFactory;
import blur.model.Organization;
import blur.model.Person;
import blur.model.TrafficCameraReport;
import blur.model.Vehicle;
import blur.model.VehicleDetails;
import blur.model.VehicleStatus;
import blur.model.VehicleType;

import com.ibm.ia.common.ComponentException;
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
		entity.setOwner(getOwnerFromES(licensePlateNumber));
		entity.setStatus(VehicleStatus.INACTIVE);
		entity.setSuspicious(false);
		
		VehicleDetails details = entity.getDetails();
		VehicleDetails newDetails = getDetailsFromES(licensePlateNumber);
		details.setMaker(newDetails.getMaker());
		details.setMaximumSpeed(newDetails.getMaximumSpeed());
		details.setType(newDetails.getType());
		details.setModel(newDetails.getModel());
		details.setYear(newDetails.getYear());
		

	}

	private Relationship<Person> getOwnerFromES(String licensePlateNumber) {
//		Person person = conceptFactory.createPerson("person" +random.nextInt(100));
		Person person = conceptFactory.createPerson("1234");
		return getModelFactory().createRelationship(person);
	}

	private VehicleDetails getDetailsFromES(
			String licensePlateNumber) {
		random = new Random();
		conceptFactory = getConceptFactory(ConceptFactory.class);
		
		VehicleDetails myDetails = conceptFactory.createVehicleDetails();
		myDetails.setType(VehicleType.MOTORCYCLE);
		myDetails.setMaker("maker_" + random.nextInt());
		myDetails.setMaximumSpeed(130);
				
		return myDetails;
	}

	private Relationship<Organization> getOrganizationFromES(
			String licensePlateNumber) {
		return null;
	}
}
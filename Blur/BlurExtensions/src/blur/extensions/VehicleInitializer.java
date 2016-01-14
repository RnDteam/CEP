package blur.extensions;

import com.ibm.ia.common.ComponentException;
import com.ibm.ia.model.Event;
import com.ibm.ia.model.Relationship;
import com.ibm.ia.extension.EntityInitializer;
import com.ibm.ia.extension.annotations.EntityInitializerDescriptor;

import blur.model.ConceptFactory;
import blur.model.Organization;
import blur.model.Person;
import blur.model.TrafficCameraReport;
import blur.model.Vehicle;
import blur.model.VehicleDetails;
import blur.model.VehicleStatus;
import blur.model.VehicleType;

@EntityInitializerDescriptor(entityType = Vehicle.class)
public class VehicleInitializer extends EntityInitializer<Vehicle> {

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
		// TODO Auto-generated method stub
		return null;
	}

	private VehicleDetails getDetailsFromES(
			String licensePlateNumber) {
		ConceptFactory conceptFactory = getConceptFactory(ConceptFactory.class);
		VehicleDetails vd = conceptFactory.createVehicleDetails();
		return vd;
	}

	private Relationship<Organization> getOrganizationFromES(
			String licensePlateNumber) {
		return null;
	}
}
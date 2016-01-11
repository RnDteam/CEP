package blur.extensions;

import com.ibm.ia.common.ComponentException;
import com.ibm.ia.model.Event;
import com.ibm.ia.extension.EntityInitializer;
import com.ibm.ia.extension.annotations.EntityInitializerDescriptor;

import blur.model.ConceptFactory;
import blur.model.TrafficCameraReport;
import blur.model.Vehicle;
import blur.model.VehicleDetails;
import blur.model.VehicleType;

@EntityInitializerDescriptor(entityType = Vehicle.class)
public class VehicleInitializer extends EntityInitializer<Vehicle> {

	@Override
	public Vehicle createEntityFromEvent(Event event) throws ComponentException {
		Vehicle entity = super.createEntityFromEvent(event);

		if( event instanceof TrafficCameraReport ) {
			// TODO -- read the other fields of the Vehicle from the database
			VehicleDetails myDetails = getConceptFactory(ConceptFactory.class)
					.createVehicleDetails();
			myDetails.setType(VehicleType.motorcycle);
			entity.setDetails(myDetails);
		}

		return entity;
	}

	@Override
	public void initializeEntity(Vehicle entity) throws ComponentException {
		super.initializeEntity(entity);
		// this method will be called after createEntityFromEvent or if
		// someone requests a vehicle using the REST API
		
		// TODO -- read the other fields of the Vehicle from the database
		VehicleDetails myDetails = getConceptFactory(ConceptFactory.class)
				.createVehicleDetails();
		myDetails.setType(VehicleType.motorcycle);
		entity.setDetails(myDetails);
	}
}
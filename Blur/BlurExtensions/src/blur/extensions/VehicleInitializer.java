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
			System.out.println( "***** VehicleInitializer createEntityFromEvent ****** " );
			
			// set the fields from the event...
			
			// TODO -- read the other fields of the Vehicle from the database
			VehicleDetails myDetails = getConceptFactory(ConceptFactory.class)
					.createVehicleDetails();
			myDetails.setType(VehicleType.MOTORCYCLE);
			entity.setDetails(myDetails);
		}

		return entity;
	}

	@Override
	public void initializeEntity(Vehicle entity) throws ComponentException {
		super.initializeEntity(entity);
		System.out.println( "***** VehicleInitializer initializeEntity ****** " );

		// this method will be called after createEntityFromEvent or if
		// someone requests a vehicle using the REST API
		
		// set generic fields that are loaded from an external source...
		
		
		// TODO -- read the other fields of the Vehicle from the database
		VehicleDetails myDetails = getConceptFactory(ConceptFactory.class)
				.createVehicleDetails();
		myDetails.setType(VehicleType.MOTORCYCLE);
		entity.setDetails(myDetails);
	}
}
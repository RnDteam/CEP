package blur.extensions;

import java.util.Random;

import blur.model.ConceptFactory;
import blur.model.Organization;
import blur.model.OrganizationType;
import blur.model.Person;
import blur.model.TrafficCameraReport;
import blur.model.Vehicle;
import blur.model.VehicleDetails;
import blur.model.VehicleStatus;
import blur.model.VehicleType;
import blur.model.impl.OrganizationalRole;

import com.ibm.ia.common.ComponentException;
import com.ibm.ia.extension.EntityInitializer;
import com.ibm.ia.extension.annotations.EntityInitializerDescriptor;
import com.ibm.ia.model.Event;
import com.ibm.ia.model.Relationship;

@EntityInitializerDescriptor(entityType = Vehicle.class)
public class VehicleInitializer extends EntityInitializer<Vehicle> {

	private static Random random;
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
		
		random = new Random();
		conceptFactory = getConceptFactory(ConceptFactory.class);

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
		person.setRole( getModelFactory().createRelationship(blur.model.OrganizationalRole.class, "3256"));
		return getModelFactory().createRelationship(person);
	}

	private Relationship<VehicleDetails> getDetailsFromES(
			String licensePlateNumber) {
		
		VehicleDetails myDetails = conceptFactory.createVehicleDetails("Details-" + licensePlateNumber);
		
//		myDetails.setType(generateStatus());
		myDetails.setType(VehicleType.MOTORCYCLE);
		myDetails.setMaker("Maker" + random.nextInt());
		myDetails.setMaximumSpeed(random.nextInt(100) + 150);
		Integer randomYear = (random.nextInt(2015 - 1980)) + 1980;
		myDetails.setYear(randomYear.toString());
		
		Relationship<VehicleDetails> detailsRelationship = getModelFactory().createRelationship(myDetails);
		
		return detailsRelationship;
	}
	
	private static VehicleType generateStatus() {
		int vehicleStatusSize = VehicleType.values().length;
		int randomStatusIndex = random.nextInt(vehicleStatusSize);
		
		return VehicleType.values()[randomStatusIndex];
	}


	private Relationship<Organization> getOrganizationFromES(
			String licensePlateNumber) {
		Organization myOrganization = conceptFactory.createOrganization("Organization-" + licensePlateNumber);
		
		myOrganization.setType(generateOrganizationType());
		Relationship<Organization> organizationRelationship = getModelFactory().createRelationship(myOrganization);
		
		return organizationRelationship;
	}

	private OrganizationType generateOrganizationType() {
		int organizationTypeSize = OrganizationType.values().length;
		int randomStatusIndex = random.nextInt(organizationTypeSize);
		
		return OrganizationType.values()[randomStatusIndex];
	}
}
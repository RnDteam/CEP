package blur.personjavaagent;

import blur.model.Building;
import blur.model.CellularReport;
import blur.model.ConceptFactory;
import blur.model.CriminalPerson;
import blur.model.CriminalPersonDetected;
import blur.model.CriminalPersonInitialization;
import blur.model.Organization;
import blur.model.OrganizationRoleInitialization;
import blur.model.OrganizationRoleType;
import blur.model.OrganizationalRole;
import blur.model.Person;
import blur.model.PersonState;
import blur.model.PersonUpdate;
import blur.model.UpdateRoleEvent;

import com.ibm.geolib.geom.Point;
import com.ibm.geolib.st.MovingGeometry;
import com.ibm.geolib.st.SpatioTemporalService;
import com.ibm.ia.agent.EntityAgent;
import com.ibm.ia.common.AgentException;
import com.ibm.ia.model.Event;
import com.ibm.ia.model.Relationship;

public class PersonAgent extends EntityAgent<Person> {

	@SuppressWarnings("unchecked")
	@Override
	public void process(Event event) throws AgentException {
		Person thisPerson = getBoundEntity();

		if(thisPerson == null) {
			return;
		}

		if(event instanceof UpdateRoleEvent) {
			// check that the role exists
			UpdateRoleEvent ure = (UpdateRoleEvent) event;

			// We ignore the org role passed in - we always create a new role
			// Relationship<OrganizationalRole> roleRel = ure.getOrganizationalRole();

			String newRole = thisPerson.get$Id() + "-" + ure.getOrganization().getKey();
			Relationship<OrganizationalRole> newRoleRel = createRelationship(OrganizationalRole.class, newRole);

			OrganizationRoleInitialization ori = getConceptFactory(ConceptFactory.class).createOrganizationRoleInitialization(event.get$Timestamp());
			ori.setPerson( createRelationship(thisPerson));
			ori.setOrganizationalRole(newRoleRel);
			ori.setName(newRole);
			ori.setOrganization(ure.getOrganization());
			ori.setType(OrganizationRoleType.EMPLOYEE);
			thisPerson.setRole(newRoleRel);
			emit(ori);

			// ROLE HAS CHANGED        	
			updateBoundEntity(thisPerson);
		}

		if (event instanceof PersonUpdate) {
			PersonUpdate personUpdateEvent = (PersonUpdate) event;

			String name = personUpdateEvent.getName();

			if(name != null) {
				thisPerson.setName(name);
			}

			String proffesion = personUpdateEvent.getProfession();

			if(proffesion != null) {
				thisPerson.setProfession(proffesion);
			}

			Point location = personUpdateEvent.getLocation();

			if(location != null) {
				MovingGeometry<Point> personLocation = thisPerson.getLocation();
				if (personLocation == null) {
					personLocation = SpatioTemporalService.getService().getMovingGeometryFactory().getMovingGeometry();
					//					thisPerson.setLocation(personLocation);				
				}

				personLocation.setGeometryAtTime(location, personUpdateEvent.getTimestamp());
			}

			PersonState state = personUpdateEvent.getState();

			if(state != null) {
				thisPerson.setState(state);
			}

			Relationship<OrganizationalRole> role = personUpdateEvent.getRole();

			if(role != null) {
				thisPerson.setRole(role);
			}

			updateBoundEntity(thisPerson);
		}

		if (event instanceof CellularReport){
			CellularReport cellularReportEvent = (CellularReport) event;

			Relationship<Building> building = cellularReportEvent.getBuilding();

			if( building !=null && building.resolve() != null) {
				MovingGeometry<Point> personLocation = thisPerson.getLocation();
				Building theBuilding = building.resolve();
				MovingGeometry<Point> buildingLocation = theBuilding.getLocation();

				if(personLocation != null) {					
					double[] cord = buildingLocation.getLastObservedGeometry().getCoordinates();
					Point newPersonLocation = SpatioTemporalService.getService().getGeometryFactory().getPoint( cord[0]  + (Math.random()+1)/10000.0, cord[1] + (Math.random()+1)/10000.0); 
					personLocation.setGeometryAtTime(newPersonLocation, cellularReportEvent.getTimestamp());
					
					String trace = (thisPerson.getTrace()==null ? "" : thisPerson.getTrace()) + "At time  " + cellularReportEvent.get$Timestamp().getHour()+":"+ cellularReportEvent.get$Timestamp().getMinute() + " is at " + theBuilding.get$Id() + ".<br>";
					thisPerson.setTrace(trace);

				}
			}
			
			updateBoundEntity(thisPerson);
		}

		if (event instanceof CriminalPersonDetected) {

			System.out.println("Recived Criminal Person Detected Event~~~~~~~~");

			UpdateRoleEvent updateRoleEvent = getConceptFactory(ConceptFactory.class).createUpdateRoleEvent(event.get$Timestamp());
			Relationship<OrganizationalRole> roleRelationship = ((CriminalPersonDetected)event).getRole();
			updateRoleEvent.setOrganizationalRole(roleRelationship);
			Relationship<Organization> orgRelationship = ((CriminalPersonDetected)event).getOrganization();
			updateRoleEvent.setOrganization(orgRelationship);
			Relationship<Person> personRelationship = ((CriminalPersonDetected)event).getPerson();
			updateRoleEvent.setPerson(personRelationship);

			emit(updateRoleEvent);

			String id =  thisPerson.get$Id();
			CriminalPersonInitialization criminalPersonInitialization = getConceptFactory(ConceptFactory.class).createCriminalPersonInitialization(event.get$Timestamp());
			criminalPersonInitialization.setCriminalPerson(createRelationship(CriminalPerson.class, id + "-criminal"));
			criminalPersonInitialization.setName(thisPerson.getName());
			criminalPersonInitialization.setProfession(thisPerson.getProfession());
			criminalPersonInitialization.setState(thisPerson.getState());
			criminalPersonInitialization.setLocation(thisPerson.getLocation().getLastObservedGeometry().getReferencePoint());
			Relationship<OrganizationalRole> relationship = createRelationship(OrganizationalRole.class, thisPerson.get$Id() + "-" + ((CriminalPersonDetected)event).getOrganization().resolve().get$Id());
			criminalPersonInitialization.setRole(relationship);

			emit(criminalPersonInitialization);

			System.out.println("~~~~Emited Criminal Person Detected Event " + id + "criminal");

		}

	}

}
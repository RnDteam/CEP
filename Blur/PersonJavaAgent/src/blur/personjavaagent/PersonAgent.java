package blur.personjavaagent;

import blur.model.OrganizationalRole;
import blur.model.Person;
import blur.model.PersonState;
import blur.model.PersonUpdate;

import com.ibm.geolib.geom.Point;
import com.ibm.geolib.st.MovingGeometry;
import com.ibm.geolib.st.SpatioTemporalService;
import com.ibm.ia.common.AgentException;
import com.ibm.ia.agent.EntityAgent;
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
        
    }
    
}
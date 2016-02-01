package blur.personjavaagent;

import blur.model.OrganizationalRole;
import blur.model.Person;
import blur.model.PersonState;
import blur.model.PersonUpdate;

import com.ibm.geolib.geom.Point;
import com.ibm.geolib.st.MovingGeometry;
import com.ibm.ia.common.AgentException;
import com.ibm.ia.agent.EntityAgent;
import com.ibm.ia.model.Event;
import com.ibm.ia.model.Relationship;

public class PersonAgent extends EntityAgent<Person> {
   
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
			
			MovingGeometry<Point> location = thisPerson.getLocation();
			
			if(location != null) {
				thisPerson.setLocation(location);
			}
			
			PersonState state = thisPerson.getState();
			
			if(state != null) {
				thisPerson.setState(state);
			}
			
			Relationship<OrganizationalRole> role = thisPerson.getRole();
			
			if(role != null) {
				thisPerson.setRole(role);
			}
		}
        
    }
    
}
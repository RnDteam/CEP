package blur.buildingjavaagent;

import blur.model.Building;
import blur.model.BuildingType;
import blur.model.BuildingUpdate;
import blur.model.BuildingUsageType;
import blur.model.Person;

import com.ibm.geolib.geom.Point;
import com.ibm.ia.common.AgentException;
import com.ibm.ia.agent.EntityAgent;
import com.ibm.ia.model.Event;
import com.ibm.ia.model.Relationship;

public class BuildingAgent extends EntityAgent<Building> {
   
    @Override
    public void process(Event event) throws AgentException {
    	Building thisBuilding = getBoundEntity();
        
        if(thisBuilding == null) {
        	return;
        }
    	if (event instanceof BuildingUpdate) {
			BuildingUpdate buildingUpdate = (BuildingUpdate) event;
			
			Point location = buildingUpdate.getLocation();
			
			if(location != null) {
				thisBuilding.setLocation(location);
			}
			
			BuildingType type = buildingUpdate.getType();
			
			if(type != null) {
				thisBuilding.setType(type);
			}
			
			Relationship<Person> owner = buildingUpdate.getOwner();
			
			if(owner != null) {
				thisBuilding.setOwner(owner);
			}
			
			BuildingUsageType usageType = buildingUpdate.getUsageType();
			
			if(usageType != null) {
				thisBuilding.setUsageType(usageType);
			}
		}
    	 
    }
    
}
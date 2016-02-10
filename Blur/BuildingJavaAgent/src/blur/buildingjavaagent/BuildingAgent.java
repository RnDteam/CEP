package blur.buildingjavaagent;

import blur.model.Building;
import blur.model.BuildingType;
import blur.model.BuildingUpdate;
import blur.model.BuildingUsageType;
import blur.model.ConceptFactory;
import blur.model.HotBuilding;
import blur.model.HotBuildingDetected;
import blur.model.HotBuildingInitialization;
import blur.model.Person;

import com.ibm.geolib.geom.Point;
import com.ibm.geolib.st.MovingGeometry;
import com.ibm.geolib.st.SpatioTemporalService;
import com.ibm.ia.agent.EntityAgent;
import com.ibm.ia.common.AgentException;
import com.ibm.ia.model.Event;
import com.ibm.ia.model.Relationship;

public class BuildingAgent extends EntityAgent<Building> {
   
    @SuppressWarnings("unchecked")
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
				MovingGeometry<Point> buildingLocation = thisBuilding.getLocation();
				if (buildingLocation == null) {
					buildingLocation = SpatioTemporalService.getService().getMovingGeometryFactory().getMovingGeometry();
					thisBuilding.setLocation(buildingLocation);					
				}
				
				buildingLocation.setGeometryAtTime(location, buildingUpdate.getTimestamp());
			}
			
			BuildingType type = buildingUpdate.getType();
			
			if(type != null) {
				thisBuilding.setType(type);
			}
			
			Relationship<Person> owner = buildingUpdate.getOwner();
			
			if(owner != null) {
				thisBuilding.setOwner(owner);
			}
			
			thisBuilding.setOrganizations(buildingUpdate.getOrganizations());
			
			BuildingUsageType usageType = buildingUpdate.getUsageType();
			
			if(usageType != null) {
				thisBuilding.setUsageType(usageType);
			}
			
			updateBoundEntity(thisBuilding);
		}
    	
    	// Hot building detected
    	if (event instanceof HotBuildingDetected) {
    		
    		String id = thisBuilding.get$Id() + "hot";    		
    		System.out.println("******************* Hot Building *********************");
    		HotBuildingInitialization hotBuildingInitialization = getConceptFactory(ConceptFactory.class).createHotBuildingInitialization(event.get$Timestamp());
    		hotBuildingInitialization.setHotBuilding(createRelationship(HotBuilding.class, id));
    		hotBuildingInitialization.setLocation(thisBuilding.getLocation().getLastObservedGeometry().getReferencePoint());
    		Relationship<Person> relationship = thisBuilding.getOwner();
    		hotBuildingInitialization.setOwner(relationship);
    		hotBuildingInitialization.setType(thisBuilding.getType());
    		hotBuildingInitialization.set_organizations(thisBuilding.getOrganizations());
    		hotBuildingInitialization.setUsageType(thisBuilding.getUsageType());
    		
    		emit(hotBuildingInitialization);
    	}
    	
    	
    }
}
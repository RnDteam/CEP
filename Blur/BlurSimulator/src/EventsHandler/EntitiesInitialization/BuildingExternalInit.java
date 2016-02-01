package EventsHandler.EntitiesInitialization;

import java.sql.ResultSet;
import com.ibm.ia.gateway.SolutionGateway;

import blur.model.Building;
import blur.model.BuildingInitialization;
import blur.model.BuildingType;
import blur.model.Person;
import DBHandler.ConverterUtility;
import EventsHandler.EventCreation;

public class BuildingExternalInit extends EventCreation<BuildingInitialization> {

	@Override
	public BuildingInitialization convertDBRowToObject(ResultSet resultSet,
			SolutionGateway gateway) {
		BuildingInitialization buildingInitEvent = null;
		try {
			buildingInitEvent = gateway.getEventFactory().createEvent(BuildingInitialization.class);
			
			String buildingId = resultSet.getString(1);
			String logntitude = resultSet.getString(2);
			String latitude = resultSet.getString(3);
			String buildingType = resultSet.getString(4);
			String ownerId = resultSet.getString(5);

			buildingInitEvent.setBuilding(gateway.createRelationship(Building.class, buildingId));
			buildingInitEvent.setLocation(ConverterUtility.getPointFromString(logntitude, latitude));
			buildingInitEvent.setType(convertBuildingType(buildingType));
			buildingInitEvent.setOwner(gateway.createRelationship(Person.class, ownerId));
			buildingInitEvent.setTimestamp(ConverterUtility.initDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return buildingInitEvent;
	}

	private BuildingType convertBuildingType(String type) {
		String lowerCase = type.toLowerCase();
		
		for (BuildingType buildType : BuildingType.values()) {
			if(lowerCase.equals(buildType.toString().toLowerCase())) {
				return buildType;
			}
		}
		
		return null;
	}

	@Override
	public String getTableName() {
		return "DB_Structure";
	}

}

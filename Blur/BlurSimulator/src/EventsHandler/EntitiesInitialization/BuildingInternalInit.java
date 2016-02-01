package EventsHandler.EntitiesInitialization;

import java.sql.ResultSet;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import com.ibm.ia.gateway.SolutionGateway;
import com.ibm.ia.model.Relationship;

import blur.model.Building;
import blur.model.BuildingType;
import blur.model.BuildingUpdate;
import blur.model.BuildingUsageType;
import blur.model.Person;
import blur.model.Organization;
import DBHandler.ConverterUtility;
import EventsHandler.EventCreation;

public class BuildingInternalInit extends EventCreation<BuildingUpdate>{

	@Override
	public BuildingUpdate convertDBRowToObject(ResultSet resultSet,
			SolutionGateway gateway) {
		BuildingUpdate buildingInitEvent = null;
		try {
			buildingInitEvent = gateway.getEventFactory().createEvent(BuildingUpdate.class);
			
			String buildingId = resultSet.getString(1);
			String logntitude = resultSet.getString(2);
			String latitude = resultSet.getString(3);
			String buildingType = resultSet.getString(4);
			String ownerId = resultSet.getString(5);
			String usageType = resultSet.getString(6);
			String organizationId = resultSet.getString(7);

			ZonedDateTime reportTimeStamp =  ConverterUtility.initDate;
			buildingInitEvent.setBuilding(gateway.createRelationship(Building.class, buildingId));
			buildingInitEvent.setLocation(ConverterUtility.getMovingGeometryFromString(logntitude, latitude, reportTimeStamp));
			buildingInitEvent.setType(convertBuildingType(buildingType));
			buildingInitEvent.setUsageType(convertUsageType(usageType));
			buildingInitEvent.setOwner(gateway.createRelationship(Person.class, ownerId));
			
			Set<Relationship<Organization>> organizations = new HashSet<>();
			organizations.add(gateway.createRelationship(Organization.class, organizationId));
			
			buildingInitEvent.setOrganizations(organizations);
			buildingInitEvent.setTimestamp(reportTimeStamp);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return buildingInitEvent;
	}

	private BuildingUsageType convertUsageType(String usageType) {
		String lowerCase = usageType.toLowerCase();
		
		for (BuildingUsageType usageTypeType : BuildingUsageType.values()) {
			if(lowerCase.equals(usageTypeType.toString().toLowerCase())) {
				return usageTypeType;
			}
		}
		
		return null;
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
		return "OB_Structure";
	}
}

package EventsHandler.EntitiesInitialization;

import java.sql.ResultSet;

import com.ibm.ia.gateway.SolutionGateway;

import DBHandler.ConverterUtility;
import EventsHandler.EventCreation;
import blur.model.Organization;
import blur.model.OrganizationInitialization;
import blur.model.OrganizationType;

public class OrganizationInit extends EventCreation<OrganizationInitialization> {

	@Override
	public OrganizationInitialization convertDBRowToObject(ResultSet resultSet,
			SolutionGateway gateway) {
		OrganizationInitialization organizationInitEvent = null;
		try {
			organizationInitEvent = gateway.getEventFactory().createEvent(OrganizationInitialization.class);
			
			String organizationId = resultSet.getString(1);
			String organizationType = resultSet.getString(2);
			
			organizationInitEvent.setOrganization(gateway.createRelationship(Organization.class, organizationId));
			organizationInitEvent.setType(getOrganizationType(organizationType));
			organizationInitEvent.setTimestamp(ConverterUtility.initDate.plusDays(days));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return organizationInitEvent;
	}

	private OrganizationType getOrganizationType(String organizationType) {
		String lowerCase = organizationType.toLowerCase();
		
		for (OrganizationType orgType : OrganizationType.values()) {
			if(lowerCase.equals(orgType.toString().toLowerCase())) {
				return orgType;
			}
		}
		return null;
	}

	@Override
	public String getTableName() {
		return "OB_Organization";
	}

}

package EventsHandler.EntitiesInitialization;

import java.sql.ResultSet;

import com.ibm.ia.gateway.SolutionGateway;

import DBHandler.ConverterUtility;
import EventsHandler.EventCreation;
import blur.model.Organization;
import blur.model.OrganizationRoleInitialization;
import blur.model.OrganizationRoleType;
import blur.model.OrganizationalRole;

public class OrganizationRoleInit extends
		EventCreation<OrganizationRoleInitialization> {

	@Override
	public OrganizationRoleInitialization convertDBRowToObject(
			ResultSet resultSet, SolutionGateway gateway) {
		OrganizationRoleInitialization organizationRolesInitEvent = null;
		try {
			organizationRolesInitEvent = gateway.getEventFactory().createEvent(OrganizationRoleInitialization.class);
			
			String organizationRoleId = resultSet.getString(1);
			String roleName = resultSet.getString(2);
			String roleType = resultSet.getString(3);
			String organizationId = resultSet.getString(4);
			
			organizationRolesInitEvent.setOrganizationalRole(
					gateway.createRelationship(OrganizationalRole.class, organizationRoleId));
			organizationRolesInitEvent.setName(roleName);
			organizationRolesInitEvent.setType(getRoleTypeFromString(roleType));
			organizationRolesInitEvent.setOrganization(gateway.createRelationship(Organization.class, organizationId));
			organizationRolesInitEvent.setTimestamp(ConverterUtility.initDate.plusDays(days));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return organizationRolesInitEvent;
	}

	private OrganizationRoleType getRoleTypeFromString(String roleTypeString) {
		String lowerCase = roleTypeString.toLowerCase();
		
		for (OrganizationRoleType roleType : OrganizationRoleType.values()) {
			if(lowerCase.equals(roleType.toString().toLowerCase())) {
				return roleType;
			}
		}
		
		return null;
	}

	@Override
	public String getTableName() {
		return "ob_organization_role";
	}

}

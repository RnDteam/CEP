package EntitiesInitialization;

import java.sql.ResultSet;

import com.ibm.ia.gateway.SolutionGateway;

import DBHandler.ConverterUtility;
import DBHandler.EventCreation;
import blur.model.Organization;
import blur.model.OrganizationRoleInitialization;
import blur.model.OrganizationalRole;
import blur.model.Person;

public class OrganizaionRoleInit extends
		EventCreation<OrganizationRoleInitialization> {

	@Override
	public OrganizationRoleInitialization convertDBRowToObject(
			ResultSet resultSet, SolutionGateway gateway) {
		OrganizationRoleInitialization organizationRolesInitEvent = null;
		try {
			organizationRolesInitEvent = gateway.getEventFactory().createEvent(OrganizationRoleInitialization.class);
			
			String organizationRoleId = resultSet.getString(1);
			String personId = resultSet.getString(2);
			String roleName = resultSet.getString(3);
			String managerId = resultSet.getString(4);
			String organizationId = resultSet.getString(5);
			
			organizationRolesInitEvent.setName(roleName);
			organizationRolesInitEvent.setOrganizationalRole(gateway.createRelationship(OrganizationalRole.class, organizationRoleId));
			organizationRolesInitEvent.setPerson(gateway.createRelationship(Person.class, personId));
			organizationRolesInitEvent.setManager(gateway.createRelationship(OrganizationalRole.class, managerId));
			organizationRolesInitEvent.setOrganization(gateway.createRelationship(Organization.class, organizationId));
			organizationRolesInitEvent.setTimestamp(ConverterUtility.initDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return organizationRolesInitEvent;
	}

	@Override
	public String getTableName() {
		return "organization_roles";
	}

}

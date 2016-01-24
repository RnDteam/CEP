package blur.tests;

public class OrganizationRoleGenerator {

	public static String generateOrganizationRoleQuery(Integer id, Integer personId, Integer organizationId, Integer organizationRoleManagerId) {
		String name = "name" + id.toString();
		String sqlQuery = String.format("insert into organization_roles values ('{0}', '{1}', '{2}', '{3}', '{4}')",
				id, name, organizationId, personId, organizationRoleManagerId);
		return sqlQuery;
	}
}
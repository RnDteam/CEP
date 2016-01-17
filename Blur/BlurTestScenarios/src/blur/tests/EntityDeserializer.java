package blur.tests;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.ZonedDateTime;

import com.ibm.geolib.geom.Point;
import com.ibm.geolib.st.SpatioTemporalService;
import com.ibm.ia.common.GatewayException;
import com.ibm.ia.common.SolutionException;
import com.ibm.ia.model.Relationship;
import com.ibm.ia.testdriver.TestDriver;

import blur.model.ConceptFactory;
import blur.model.Organization;
import blur.model.OrganizationInitialization;
import blur.model.OrganizationRoleInitialization;
import blur.model.OrganizationType;
import blur.model.OrganizationalRole;
import blur.model.Person;
import blur.model.PersonInitialization;
import blur.model.PersonState;
import blur.model.VehicleDetails;
import blur.model.VehicleInitialization;
import blur.model.VehicleStatus;

public class EntityDeserializer {

	public static ZonedDateTime yearAgo = ZonedDateTime.now().minusYears(1);
	
	public static OrganizationInitialization getOrganizationInitialization(ResultSet resultSet, TestDriver testDriver) throws SQLException, SolutionException, GatewayException {
		ConceptFactory conceptFactory = testDriver.getConceptFactory(ConceptFactory.class);
		
		OrganizationInitialization organizationInitialization = conceptFactory.createOrganizationInitialization(yearAgo);
		OrganizationType organizationType = resultSet.getString(2) == "CRIMINAL" ? OrganizationType.CRIMINAL : OrganizationType.COMMERCIAL;
		organizationInitialization.setType(organizationType);
		organizationInitialization.setOrganization(testDriver.createRelationship(Organization.class, resultSet.getString(1)));
		
		return organizationInitialization;
	}
	
	public static PersonInitialization getPersonInitialization(ResultSet resultSet, TestDriver testDriver) throws SolutionException, GatewayException, SQLException {
		ConceptFactory conceptFactory = testDriver.getConceptFactory(ConceptFactory.class);
		
		String personName = resultSet.getString(2);
		String personProfession = resultSet.getString(3);
		String personLocation = resultSet.getString(4);
		String personState = resultSet.getString(5);
		String personOrganizationRoleId = resultSet.getString(6);
		
		PersonInitialization personInitialization = conceptFactory.createPersonInitialization(yearAgo);
		personInitialization.setName(personName);
		personInitialization.setProfession(personProfession);
		personInitialization.setLocation(getPointFromString(personLocation));
		personInitialization.setState(getPersonStatus(personState));
		personInitialization.setRole(testDriver.createRelationship(OrganizationalRole.class, personOrganizationRoleId));
		
		return personInitialization;
	}
	
	public static OrganizationRoleInitialization getOrganizationRoleInitialization(
			ResultSet resultSet, TestDriver testDriver) throws SolutionException, GatewayException, SQLException {
		ConceptFactory conceptFactory = testDriver.getConceptFactory(ConceptFactory.class);
		
		String organizatioRolenName = resultSet.getString(2);
		String organizatioId = resultSet.getString(3);
		String personId = resultSet.getString(4);
		String managerRoleId = resultSet.getString(5);
		
		OrganizationRoleInitialization organizationRoleInitialization = conceptFactory.createOrganizationRoleInitialization(yearAgo);
		organizationRoleInitialization.setName(organizatioRolenName);
		organizationRoleInitialization.setOrganization(testDriver.createRelationship(Organization.class, organizatioId));
		organizationRoleInitialization.setPerson(testDriver.createRelationship(Person.class, personId));
		organizationRoleInitialization.setManager(testDriver.createRelationship(OrganizationalRole.class, managerRoleId));
		
		return organizationRoleInitialization;
	}
	
	public static VehicleInitialization getVehicleInitialization (
			ResultSet resultSet, TestDriver testDriver) throws SolutionException, GatewayException, SQLException {
		ConceptFactory conceptFactory = testDriver.getConceptFactory(ConceptFactory.class);
		
		String location = resultSet.getString(2);
		String status = resultSet.getString(3);
		String detailsId = resultSet.getString(4);
		String lastSeen = resultSet.getString(5);
		String ownerId = resultSet.getString(6);
		String organizationId = resultSet.getString(7);
		String isSuspicious = resultSet.getString(8);
		
		VehicleInitialization vehicleInitialization = conceptFactory.createVehicleInitialization(yearAgo);
		vehicleInitialization.setLocation(getPointFromString(location));
		vehicleInitialization.setStatus(getVehicleStatus(status));
		VehicleDetails vehicleDetails = conceptFactory.creaV
		vehicleInitialization.setDetails(testDriver.createRelationship(VehicleDetails.class, detailsId));
//		vehicleInitialization.setLastSeen(arg0);
		vehicleInitialization.setOwner(testDriver.createRelationship(Person.class, ownerId));
		vehicleInitialization.setOrganization(testDriver.createRelationship(Organization.class, organizationId));
		vehicleInitialization.setSuspicious(getIsSuspicious(isSuspicious));
		
		return vehicleInitialization;
	}

	private static boolean getIsSuspicious(String isSuspicious) {
		return isSuspicious.equals("TRUE") ? true : false;
	}

	private static VehicleStatus getVehicleStatus(String status) {
		return status.equals("ACTIVE") ? VehicleStatus.ACTIVE : VehicleStatus.INACTIVE;
	}

	private static PersonState getPersonStatus(String personState) {
		return personState.equals("ACTIVE") ? PersonState.ACTIVE : PersonState.INACTIVE;
	}
	
	public static Point getPointFromString(String location) {
		double x = Double.parseDouble(location.split(",")[0]);
		double y = Double.parseDouble(location.split(",")[1]);
		
		return SpatioTemporalService.getService().getGeometryFactory().getPoint( x, y);
		
	}
}
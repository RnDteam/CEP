package EntitiesInitialization;

import java.sql.ResultSet;

import com.ibm.ia.gateway.SolutionGateway;

import DBHandler.ConverterUtility;
import DBHandler.DBReader;
import DBHandler.EventCreation;
import blur.model.OrganizationalRole;
import blur.model.Person;
import blur.model.PersonInitialization;
import blur.model.PersonState;
import blur.model.PersonUpdate;

public class PersonInternalInit extends EventCreation<PersonUpdate> {

	@Override
	public PersonUpdate convertDBRowToObject(ResultSet resultSet,
			SolutionGateway gateway) {
		PersonUpdate personInitEvent = null;
		
		try {
			personInitEvent = gateway.getEventFactory().createEvent(PersonUpdate.class);
			
			String personId = resultSet.getString(1);
			String name = resultSet.getString(2);
			String organizationRoleId = resultSet.getString(3);
			String proffesion = resultSet.getString(4);
			String logntitude = resultSet.getString(5);
			String latitude = resultSet.getString(6);
			String state  =resultSet.getString(7);
			
			personInitEvent.setPerson(gateway.createRelationship(Person.class, personId));
			personInitEvent.setName(name);
			personInitEvent.setRole(gateway.createRelationship(OrganizationalRole.class, organizationRoleId));
			personInitEvent.setProfession(proffesion);
			personInitEvent.setLocation(ConverterUtility.getPointFromString(logntitude, latitude));
			personInitEvent.setState(getPersonState(state));
			
			personInitEvent.setTimestamp(ConverterUtility.initDate);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return personInitEvent;
	}

	private PersonState getPersonState(String state) {
		String lowerCase = state.toLowerCase();
		
		for (PersonState personState : PersonState.values()) {
			if(lowerCase.equals(personState.toString().toLowerCase())) {
				return personState;
			}
		}
		
		return null;
	}

	@Override
	public String getTableName() {
		return "persons_internal";
	}

}

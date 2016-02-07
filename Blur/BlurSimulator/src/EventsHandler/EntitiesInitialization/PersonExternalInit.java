package EventsHandler.EntitiesInitialization;

import java.sql.ResultSet;

import com.ibm.ia.gateway.SolutionGateway;

import blur.model.Person;
import blur.model.PersonInitialization;
import DBHandler.ConverterUtility;
import EventsHandler.EventCreation;

public class PersonExternalInit extends EventCreation<PersonInitialization> {

	@Override
	public PersonInitialization convertDBRowToObject(ResultSet resultSet, SolutionGateway gateway) {
		PersonInitialization personInitEvent = null;
		
		try {
			personInitEvent = gateway.getEventFactory().createEvent(PersonInitialization.class);
			
			String personId = resultSet.getString(1);
			String name = resultSet.getString(2);
			
			personInitEvent.setPerson(gateway.createRelationship(Person.class, personId));
			personInitEvent.setName(name);
			personInitEvent.setLocation(ConverterUtility.getPointFromDouble(34.938636, 29.55648));
			
			personInitEvent.setTimestamp(ConverterUtility.initDate.plusDays(days));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return personInitEvent;
	}

	@Override
	public String getTableName() {
		return "db_people";
	}

}

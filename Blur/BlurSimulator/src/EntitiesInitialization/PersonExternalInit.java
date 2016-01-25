package EntitiesInitialization;

import java.sql.ResultSet;

import com.ibm.ia.gateway.SolutionGateway;

import blur.model.Person;
import blur.model.PersonInitialization;
import blur.model.TrafficCameraReport;
import blur.model.Vehicle;
import DBHandler.ConverterUtility;
import DBHandler.DBReader;
import DBHandler.EventCreation;

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
			
			personInitEvent.setTimestamp(ConverterUtility.initDate.minusDays(1));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return personInitEvent;
	}

	@Override
	public String getTableName() {
		return "persons_external";
	}

}

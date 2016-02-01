package EventsHandler.Events;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import com.ibm.ia.gateway.SolutionGateway;
import com.ibm.ia.model.Relationship;

import blur.model.Building;
import blur.model.LocationBasedEvent;
import blur.model.Person;
import DBHandler.DBReader;
import EventsHandler.EventCreation;

public abstract class AbsLocationBasedEvent<T> extends EventCreation<LocationBasedEvent> {

	private final String getAllPersonsOBQuery = "SELECT * FROM ob_person";
	private final String getAllPersonsDBQuery = "SELECT * FROM db_people";
	private final String getAllBuildingOBQuery = "SELECT * FROM ob_structure";
	private final String getAllBuildingDBQuery = "SELECT * FROM db_structure";

	protected Set<Relationship<Person>> getAllPersons(SolutionGateway gateway) {
		Connection dbConnection = DBReader.getDBConnection();
		
		HashSet<Relationship<Person>> personSet = new HashSet<>();
		
		try {
			Statement statement = dbConnection.createStatement();
			ResultSet resultSet = statement.executeQuery(getAllPersonsOBQuery );
			
			while (resultSet.next()) {
				personSet.add(convertDBPersonToRelationship(resultSet, gateway));
			}
			
			resultSet = statement.executeQuery(getAllPersonsDBQuery  );
			
			while (resultSet.next()) {
				personSet.add(convertDBPersonToRelationship(resultSet, gateway));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return personSet;
	}
	
	private Relationship<Person> convertDBPersonToRelationship(ResultSet resultSet,
			SolutionGateway gateway) {
		Relationship<Person> personInited = null;
		try {
			String personId = resultSet.getString(1);
			
			personInited = gateway.createRelationship(Person.class, personId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return personInited;
	}

	protected Set<Relationship<Building>> getAllBuildings(SolutionGateway gateway) {
		Connection dbConnection = DBReader.getDBConnection();
		
		HashSet<Relationship<Building>> buildingSet = new HashSet<>();
		
		try {
			Statement statement = dbConnection.createStatement();
			ResultSet resultSet = statement.executeQuery(getAllBuildingOBQuery  );
			
			while (resultSet.next()) {
				buildingSet.add(convertDBBuildingToRelationship(resultSet, gateway));
			}
			
			resultSet = statement.executeQuery(getAllBuildingDBQuery );
			
			while (resultSet.next()) {
				buildingSet.add(convertDBBuildingToRelationship(resultSet, gateway));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return buildingSet;
	}

	private Relationship<Building> convertDBBuildingToRelationship(
			ResultSet resultSet, SolutionGateway gateway) {
		Relationship<Building> buildingInited = null;
		try {
			String buildingId = resultSet.getString(1);
			
			buildingInited = gateway.createRelationship(Building.class, buildingId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return buildingInited;
	}
}

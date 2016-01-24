package blur.tests;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Random;

import blur.model.BuildingType;
import blur.model.BuildingUsageType;
import blur.model.VehicleStatus;

public class EntityGenerator {

	public static class RandomPoint {

		public static String getRandomPoint() {
			
			Random rand = new Random();		
			int longitude = rand.nextInt(50);
			int latitude = rand.nextInt(50);
			String point = longitude + "," + latitude;
			return point;
		}	
	}
	
	public static Random random = new Random();
	
	public static String generateBuilding(String buildId, String persId, String organizatioId) {
		String buildingId = buildId.toString();
		String location = generateLocation();
		String type = generateType();
		String usage_type = generateUsage();
		String personId = persId.toString();
		
		String insertVehicleQuery = 
				String.format("insert into buildings (id, location, type, usage_type, owner_id, organization_id) VALUES ('%s','%s','%s','%s','%s', '%s')",
						buildingId, location, type, usage_type, personId, organizatioId);
		
		return insertVehicleQuery;
	}

	private static String generateUsage() {
		int usageSize = BuildingUsageType.values().length;
		int randomUsageIndex = random.nextInt(usageSize);
		
		return BuildingUsageType.values()[randomUsageIndex].toString();
	}

	private static String generateLocation() {
		return RandomPoint.getRandomPoint();
	}

	private static String generateType() {
		int typesSize = BuildingType.values().length;
		int randomTypeIndex = random.nextInt(typesSize);
		
		return BuildingType.values()[randomTypeIndex].toString();
	}
	
	// person
	private static final String ACTIVE = "ACTIVE";
	private static final String INACTIVE = "INACTIVE";

	public static String generatePerson(String id, String organizationRole) {
		Random rand = new Random();
		String name = "Name" + id;
		String profession= "Profession" + rand.nextInt(100);
		String location= RandomPoint.getRandomPoint();
		String state;
		int s = rand.nextInt(100) % 2;
		if (s == 0) {
			state = ACTIVE;
		}
		else {
			state = INACTIVE;
		}	
		String query = String.format("INSERT INTO persons VALUES ('%s', '%s', '%s', '%s', '%s', '%s')", id, name, profession, location, state, organizationRole);
		return query;
	}
	
	public static String generatePerson(String id, String organizationRole, String location, String state) {
		Random rand = new Random();
		String name = "Name" + id;
		String profession= "Profession" + rand.nextInt(100);
		
		String query = String.format("INSERT INTO persons VALUES ('%s', '%s', '%s', '%s', '%s', '%s')", id, name, profession, location, state, organizationRole);
		return query;
	}
	
	// vehicle
	public static String generateVehicle(String vehId, String detailsId, String ownerId, String organizationId) {
		String vehicleId = vehId.toString();
		String location = generateLocation();
		String status = generateStatus();
		String details = detailsId.toString();
		String last_seen = generateDate();
		String owner = ownerId.toString();
		String organization = organizationId.toString();
		String suspicious = "FALSE";

		
		String insertVehicleQuery = 
				String.format("INSERT INTO vehicles VALUES ('%s','%s','%s','%s','%s','%s','%s', '%s')",
						vehicleId, location, status, details, last_seen, owner, organization, suspicious);
		
		return insertVehicleQuery;
	}
	
	public static String generateVehicle(String vehicleId, String detailsId, String ownerId, String organizationId,
			String location, String status, String last_seen, String suspicious) {
		
		String insertVehicleQuery = 
				String.format("INSERT INTO vehicles VALUES ('%s','%s','%s','%s','%s','%s','%s', '%s')",
						vehicleId, location, status, detailsId, last_seen, ownerId, organizationId, suspicious);
		
		return insertVehicleQuery;
	}

	private static String generateDate() {
		int year = 2016;
		int month = 01;
		int day = random.nextInt(10) + 1;
		int hour = random.nextInt(24) +1;
		int minutes = random.nextInt(60) + 1;
		int seconds = random.nextInt(60) + 1;
		ZonedDateTime time = ZonedDateTime.of(year, month, day, hour, minutes, seconds, 0, ZoneId.systemDefault());
		
		return time.toString();
	}

	private static String generateStatus() {
		int vehicleStatusSize = VehicleStatus.values().length;
		int randomStatusIndex = random.nextInt(vehicleStatusSize);
		
		return VehicleStatus.values()[randomStatusIndex].toString();
	}
	
	// organization
	public static String generateOrganizationQuery(String id) {
		
		String type = null;
		Random random = new Random();

		if (random.nextBoolean()) {
			type = "CRIMINAL";
		} else {
			type = "COMMERCIAL";
		}
		
		String sqlQuery = String.format("insert into organizations (id, type) values ('%s', '%s')", id, type);
		
		return sqlQuery;
	}
	
	public static String generateOrganizationQuery(String id, String type) {
		
		String sqlQuery = String.format("insert into organizations (id, type) values ('%s', '%s')", id, type);
		
		return sqlQuery;
	}
	
	// organization role
	public static String generateOrganizationRoleQuery(String id, String personId, String organizationId, String organizationRoleManagerId) {
		String name = "name" + id.toString();
		String sqlQuery = String.format("insert into organization_roles values ('%s', '%s', '%s', '%s', '%s')",
				id, name, organizationId, personId, organizationRoleManagerId);
		return sqlQuery;
	}
	
	// vehicle details
	public static String generateVehicleDetails(String id, String maker, String model, String type, String year, String max_speed) {
		
		String sqlQuery = String.format("insert into vehicle_details values ('%s', '%s', '%s', '%s', '%s', '%s')",
				id, maker, model, type, year, max_speed);
		
		return sqlQuery;
	}
}

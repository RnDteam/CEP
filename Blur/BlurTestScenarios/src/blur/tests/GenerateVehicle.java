package blur.tests;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Random;

import blur.model.VehicleStatus;

public class GenerateVehicle {
	public static Random random = new Random();
	
	public static String generateVehicle(Integer vehId, Integer detailsId, Integer ownerId, Integer organizationId) {
		String vehicleId = vehId.toString();
		String location = generateLocation();
		String status = generateStatus();
		String details = detailsId.toString();
		String last_seen = generateDate();
		String owner = ownerId.toString();
		String organization = organizationId.toString();

		
		String insertVehicleQuery = 
				String.format("INSERT INTO vehicles VALUES ('{0}','{1}','{2}','{3}','{4}','{5}','{6}')",
						vehicleId, location, status, details, last_seen, owner, organization);
		
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

	private static String generateLocation() {
		return null;
	}
}

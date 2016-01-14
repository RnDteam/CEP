package blur.tests;

import java.util.Random;

import blur.model.BuildingType;
import blur.model.BuildingUsageType;

public class GenerateBuilding {
	
	public static Random random = new Random();
	
	public static String generateBuilding(Integer buildId, Integer persId) {
		String buildingId = buildId.toString();
		String location = generateLocation();
		String type = generateType();
		String usage_type = generateUsage();
		String personId = persId.toString();
		
		String insertVehicleQuery = 
				String.format("INSERT INTO buildings VALUES ('{0}','{1}','{2}','{3}','{4}')",
						buildingId, location, type, usage_type, personId);
		
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
	
	

}
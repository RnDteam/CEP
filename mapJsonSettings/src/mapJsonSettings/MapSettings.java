package mapJsonSettings;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class MapSettings {

	private static String LICENSE_PLATES_FILE;
	private static final String VALUES_STRING = "values";
	private static final String RANGES_STRING = "ranges";
	private static final String DEFAULT_STRING = "default";
	private static final String FILL_COLOR_STRING = "fillColor";
	private static final String STYLE_STRING = "style";
	private static final String VEHICLE_STRING = "Vehicle";
	private static final String ENTITY_URL_STRING = "url";
	private static final String FEEDS_STRING = "feeds";

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String inputJsonPath = "C:\\devl\\newWorkspace\\CEP\\mapJsonSettings\\Config.json";
		String outputJsonPath = "C:\\devl\\newWorkspace\\CEP\\mapJsonSettings\\newConfig.json";
		LICENSE_PLATES_FILE = "LicensePlates.txt";
		
		JSONObject changedJson = changeJson( inputJsonPath);
		
		WriteJson(changedJson,outputJsonPath);

	}

	public static JSONObject changeJson(String filePath) {

		JSONParser parser = new JSONParser();

		try {

			Object obj = parser.parse(new FileReader(filePath));

			JSONObject jsonObject = (JSONObject) obj;

			JSONArray feeds = (JSONArray) jsonObject.get(FEEDS_STRING);
			Iterator<String> iterator = feeds.iterator();
			int j=0;
			JSONObject vehicleFillJson=null;
			while (iterator.hasNext()) {
				JSONObject stringObj = (JSONObject) feeds.get(j);
				String urlEntity = stringObj.get(ENTITY_URL_STRING).toString();
				if (urlEntity.endsWith(VEHICLE_STRING)){
					vehicleFillJson = (JSONObject)((JSONObject)((JSONObject) stringObj.get(STYLE_STRING)).get(FILL_COLOR_STRING)).get(DEFAULT_STRING);
					break;
				}
				iterator.next();
				j++;
			}
			
			JSONArray ranges = getRangesList();
			System.out.println(vehicleFillJson.get(RANGES_STRING));
			vehicleFillJson.put(RANGES_STRING, ranges);
			System.out.println(vehicleFillJson.get(RANGES_STRING));
			
			JSONArray values = getValuesList(ranges.size());
			System.out.println(vehicleFillJson.get(VALUES_STRING));
			vehicleFillJson.put(VALUES_STRING, values);
			System.out.println(vehicleFillJson.get(VALUES_STRING));
			
			return jsonObject;

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		return null;

	}

	private static JSONArray getValuesList(int length) {
		JSONArray ranges = new JSONArray();
		
		float interval = (float) (360.0 / (length));
		Color c;
		String hex;
	    for (float x = 0; x < 360; x += interval){
	        c = Color.getHSBColor(x / 360, 1, 1);
	        hex = "#"+Integer.toHexString(c.getRGB()).substring(2);
	        ranges.add(hex);
	    }
		
		return ranges;
	}


	private static JSONArray getRangesList() {
		
		JSONArray ranges = new JSONArray();

		try(BufferedReader br = new BufferedReader(new FileReader(LICENSE_PLATES_FILE))) {
		    StringBuilder sb = new StringBuilder();
		    String line = br.readLine();
		    while (line != null) {
		    	ranges.add(line);
		        line = br.readLine();
		    }
		    
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return ranges;
	}

	public static void WriteJson(JSONObject writeJsonObj, String outputFile) {
	
		try {

			FileWriter file = new FileWriter(outputFile);
			file.write(writeJsonObj.toJSONString());
			file.flush();
			file.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.print(writeJsonObj);

	}

}





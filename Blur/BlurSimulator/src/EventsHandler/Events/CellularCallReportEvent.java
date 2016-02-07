package EventsHandler.Events;

import java.sql.ResultSet;

import com.ibm.ia.gateway.SolutionGateway;

import DBHandler.ConverterUtility;
import blur.model.CellularCallReport;
import blur.model.Person;

public class CellularCallReportEvent extends
		AbsLocationBasedEvent<CellularCallReport> {

	@Override
	public CellularCallReport convertDBRowToObject(ResultSet resultSet,
			SolutionGateway gateway) {
		CellularCallReport cellularCallReportEvent = null;
		try {
			cellularCallReportEvent = gateway.getEventFactory().createEvent(CellularCallReport.class);
			
//			String cellularCallReportId = resultSet.getString(1);
			String reportTime = resultSet.getString(2);
			String callerId = resultSet.getString(3);
			String receiverId = resultSet.getString(4);
			
			cellularCallReportEvent.setCaller(gateway.createRelationship(Person.class, callerId));
			cellularCallReportEvent.setCallee(gateway.createRelationship(Person.class, receiverId));
			cellularCallReportEvent.setBuildings(getAllBuildings(gateway));
			cellularCallReportEvent.setPersons(getAllPersons(gateway));
			cellularCallReportEvent.setTimestamp(ConverterUtility.absDate.plusMinutes(Integer.parseInt(reportTime)));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return cellularCallReportEvent;
	}

	@Override
	public String getTableName() {
		return "rp_call";
	}

}

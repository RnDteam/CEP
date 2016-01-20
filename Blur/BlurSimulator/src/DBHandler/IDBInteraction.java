package DBHandler;

import java.sql.ResultSet;
import java.util.List;

import com.ibm.ia.gateway.SolutionGateway;

public interface IDBInteraction<T> {
	public List<T> getAllEntities(SolutionGateway gateway);
	public T converDBRowToObject(ResultSet resultSet, SolutionGateway gateway);
}

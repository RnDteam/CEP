package blurservices;
import blur.model.Person;
public interface IPathFinder {
	public boolean isTherePath(String ownerId, int depth);
	public boolean isGood( Person person, int depth );
}
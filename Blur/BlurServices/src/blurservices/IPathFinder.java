package blurservices;

import blur.model.Organization;
import blur.model.Person;

public interface IPathFinder {
	public Organization pathToCriminal(String ownerId, int depth);
	public boolean isGood( Person person, int depth );
}
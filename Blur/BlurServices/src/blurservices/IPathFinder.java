
package blurservices;

import blur.model.Person;

public interface IPathFinder {
	public boolean pathToCriminalOrganization(String ownerId, int depth);
	public boolean isGood( Person person, int depth );
}

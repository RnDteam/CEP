package blurservices;

import blur.model.Person;

public class PathFinder implements IPathFinder{

	@Override
	public boolean pathToCriminalOrganization (String ownerId, int depth) {
		System.out.println("********************************* PathFinder *****************************");
		if(ownerId.equals("123") || ownerId.equals("1234")) {
			return true;
		}

		return false;
	}

	public boolean isGood( Person person, int depth ) {
		if(depth < 3) {
			return person.getName().contains("Dan");			
		}
		else {
			return false;
		}
	}
}

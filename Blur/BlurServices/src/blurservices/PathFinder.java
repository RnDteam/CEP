package blurservices;

import blur.model.Person;

public class PathFinder implements IPathFinder {
	@Override
	public boolean isTherePath(String ownerId, int depth) {
		System.out.println("********************************* PathFinder *****************************");
		return true;
	}

	@Override
	public boolean isGood( Person person, int depth ) {
		if(depth < 3) {
			return person.getName().contains("Dan");			
		}
		else {
			return false;
		}
	}
}

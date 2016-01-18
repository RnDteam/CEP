package blurservices;

public class PathFinder implements IPathFinder{

	@Override
	public boolean isTherePath(String ownerId, int depth) {
		System.out.println("********************************* PathFinder *****************************");
		if(ownerId.equals("123") || ownerId.equals("1234")) {
			return true;
		}

		return false;
	}
}

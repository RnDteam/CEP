package blurservices;

import blur.model.ConceptFactory;
import blur.model.Organization;
import blur.model.OrganizationType;
import blur.model.Person;
import blur.model.RiskScoreProviderResponse;

import com.ibm.ia.extension.DataProvider;
import com.ibm.ia.extension.annotations.DataProviderDescriptor;

public class PathFinder implements IPathFinder{



	public boolean isGood( Person person, int depth ) {
		if(depth < 3) {
			return person.getName().contains("Dan");			
		}
		else {
			return false;
		}
	}

	@Override
	public Organization pathToCriminal(String ownerId, int depth) {	
		ConceptFactory factory =  getConceptFactory(ConceptFactory.class);
        Organization org = factory.createOrganization("myOrganization");
        org.setType(OrganizationType.CRIMINAL);
		return org;
	}
}

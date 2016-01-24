
import com.ibm.ia.common.ComponentException;
import com.ibm.ia.extension.DataProvider;
import com.ibm.ia.extension.annotations.DataProviderDescriptor;

import blur.model.VehicleDetails;
import blur.model.VehicleTypeProvider;
import blur.model.VehicleTypeProviderRequest;
import blur.model.VehicleTypeProviderResponse;
import blur.model.ConceptFactory;

@DataProviderDescriptor(dataProvider = VehicleTypeProvider.class, responseCacheTimeout = 6000)
public class VehicleTypeDataProvider extends DataProvider<VehicleTypeProviderRequest, VehicleTypeProviderResponse> implements VehicleTypeProvider {
    
	@Override
    public VehicleTypeProviderResponse processRequest(VehicleTypeProviderRequest request) throws ComponentException {
        ConceptFactory factory = getConceptFactory(ConceptFactory.class);
        VehicleTypeProviderResponse response = factory.createVehicleTypeProviderResponse();
        
        VehicleDetails recievedDetails = request.getDetails();

        //TODO: go to DB and recive Vehicle Type
        recievedDetails.setType(recievedDetails.getType());
        System.out.println("~~~~~~In Vehicle Type Data provider, keeping: "+recievedDetails.getType());
        
        response.setFullDetails(recievedDetails);
        
        return response;
	}
    
}
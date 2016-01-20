package blur.extensions;

import com.ibm.ia.common.ComponentException;
import com.ibm.ia.extension.DataProvider;
import com.ibm.ia.extension.annotations.DataProviderDescriptor;

import blur.model.RiskScoreProvider;
import blur.model.RiskScoreProviderRequest;
import blur.model.RiskScoreProviderResponse;
import blur.model.ConceptFactory;

@DataProviderDescriptor(dataProvider = RiskScoreProvider.class, responseCacheTimeout = 1600)
public class RiskDataProvider extends DataProvider<RiskScoreProviderRequest, RiskScoreProviderResponse> implements RiskScoreProvider {
    
    @Override
    public RiskScoreProviderResponse processRequest(RiskScoreProviderRequest request) throws ComponentException {
        ConceptFactory factory = getConceptFactory(ConceptFactory.class);
        RiskScoreProviderResponse response = factory.createRiskScoreProviderResponse();
        
        // TODO Complete the data provider response
        
        return response;
    }
    
}
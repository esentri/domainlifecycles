package fixtures.abstracttypes.impl;

import fixtures.abstracttypes.api.PricingService;

public class PricingServiceImpl implements PricingService {

    @Override
    public int price() {
        return 1;
    }
}

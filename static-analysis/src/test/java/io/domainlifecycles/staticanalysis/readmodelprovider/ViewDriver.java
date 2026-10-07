package io.domainlifecycles.staticanalysis.readmodelprovider;

import io.domainlifecycles.domain.types.ApplicationService;

import java.util.List;
import java.util.Optional;

public class ViewDriver implements ApplicationService {

    public ComputedView compute() {
        return null;
    }

    public Optional<ComputedView> computeOptional() {
        return Optional.empty();
    }

    public List<ComputedView> computeAll() {
        return List.of();
    }

    // returns a read model a query handler provides
    public ProvidedView provided() {
        return null;
    }
}

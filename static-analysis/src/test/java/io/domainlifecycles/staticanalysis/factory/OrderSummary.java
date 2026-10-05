package io.domainlifecycles.staticanalysis.factory;

import io.domainlifecycles.domain.types.ReadModel;

// provided by no query handler
public record OrderSummary(int lines) implements ReadModel {
}

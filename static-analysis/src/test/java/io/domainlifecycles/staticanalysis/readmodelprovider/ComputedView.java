package io.domainlifecycles.staticanalysis.readmodelprovider;

import io.domainlifecycles.domain.types.ReadModel;

// provided by no query handler
public record ComputedView(String value) implements ReadModel {
}

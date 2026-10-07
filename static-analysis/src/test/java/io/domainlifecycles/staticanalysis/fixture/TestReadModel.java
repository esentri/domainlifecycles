package io.domainlifecycles.staticanalysis.fixture;

import io.domainlifecycles.domain.types.ReadModel;

public record TestReadModel(String value) implements ReadModel {
}

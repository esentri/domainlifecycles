package tests.shared.inheritance;

import lombok.Builder;

@Builder
public record ConcreteReadModel(String msg) implements ReadModelType {
}

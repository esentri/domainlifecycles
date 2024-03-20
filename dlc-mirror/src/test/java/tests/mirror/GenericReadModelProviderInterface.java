package tests.mirror;

import nitrox.dlc.domain.types.ReadModel;
import nitrox.dlc.domain.types.ReadModelProvider;

public interface GenericReadModelProviderInterface<T extends ReadModel> extends ReadModelProvider<T> {
}

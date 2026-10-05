package tests.mirror.factory;

import io.domainlifecycles.domain.types.DomainService;
import io.domainlifecycles.domain.types.FactoryMethod;

public class CalendarCreatingService implements DomainService {

    @FactoryMethod
    public Calendar openFor(CalendarId id) {
        return Calendar.open(id);
    }
}

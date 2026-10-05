package tests.mirror.factory;

import io.domainlifecycles.domain.types.Factory;

import java.util.ArrayList;
import java.util.UUID;

public class CalendarFactory implements Factory {

    public Calendar create() {
        return new Calendar(newId(), new ArrayList<>());
    }

    private CalendarId newId() {
        return new CalendarId(UUID.randomUUID());
    }
}

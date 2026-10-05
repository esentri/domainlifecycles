package tests.mirror.factory;

import io.domainlifecycles.domain.types.Factory;

import java.util.ArrayList;

public class CalendarFactoryWithFurtherResponsibilities implements Factory {

    public Calendar create(CalendarId id) {
        return new Calendar(id, new ArrayList<>());
    }

    public void archive(Calendar calendar) {
    }
}

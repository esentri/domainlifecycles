package fixtures.abstracttypes.impl;

import fixtures.abstracttypes.api.Person;

public class PersonRecord implements Person {

    private String name;

    @Override
    public String getName() {
        return name;
    }
}

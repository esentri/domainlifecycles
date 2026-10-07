package fixtures.abstracttypes.impl;

import fixtures.abstracttypes.api.Person;

/**
 * Implements {@link Person} a second time, anonymously.
 */
public class PersonClient {

    public Person find(String name) {
        return new Person() {
            @Override
            public String getName() {
                return name;
            }
        };
    }
}

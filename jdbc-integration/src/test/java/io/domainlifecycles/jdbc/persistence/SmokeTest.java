package io.domainlifecycles.jdbc.persistence;

import org.junit.jupiter.api.Test;

class SmokeTest {

    @Test
    void buildsFullPersistenceMirror() {
        new JdbcTestPersistenceConfiguration();
    }
}

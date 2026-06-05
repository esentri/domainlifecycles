package io.domainlifecycles.boot3.autoconfig.features.all;

import io.domainlifecycles.boot3.autoconfig.annotation.EnableDlc;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.util.Locale;

@SpringBootApplication
@EnableDlc
public class TestApplicationAllFeaturesAutoConfig {

    /**
     * Setting the Locale to explicitly force the language in default validation error messages.
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.ENGLISH);
        new SpringApplicationBuilder(TestApplicationAllFeaturesAutoConfig.class).run(args);
    }
}

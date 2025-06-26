package io.domainlifecycles.autoconfig.features.single.openapi;

import java.util.Locale;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springdoc.core.service.OpenAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test"})
@AutoConfigureMockMvc
public class OpenApiAutoConfigTests {

    private static final String API_DOCS_PATH = "/v3/api-docs";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testOpenApiSimple() throws Exception {
        Locale.setDefault(Locale.ENGLISH);

        mockMvc
            .perform(MockMvcRequestBuilders.get(API_DOCS_PATH).locale(Locale.ENGLISH))
            .andExpect(status().isOk())
            .andReturn();
    }
}



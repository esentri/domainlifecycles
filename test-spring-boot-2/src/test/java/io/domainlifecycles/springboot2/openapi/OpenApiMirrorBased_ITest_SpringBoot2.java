package io.domainlifecycles.springboot2.openapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springdoc.core.OpenAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tests.shared.persistence.domain.bestellung.bv2.Bestellung;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedComplexVo;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedSimpleVo;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedVoEntity;

import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test"})
@Slf4j
@AutoConfigureMockMvc
public class OpenApiMirrorBased_ITest_SpringBoot2 {

    @Autowired
    private OpenAPIService openAPIService;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String API_DOCS_PATH = "/v3/api-docs";

    private static final String API_CALL_PREFIX = "/api/testComplexVo/";

    private static final String SCHEMA_TYPE_STRING = "string";
    private static final String SCHEMA_TYPE_ARRAY = "array";

    private static final String SCHEMA_TYPE_INTEGER = "integer";

    private static final String FORMAT_TYPE_INT64 = "int64";


    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void init() throws Exception {
        Locale.setDefault(Locale.ENGLISH);
        final MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get(API_DOCS_PATH))
            .andExpect(status().isOk())
            .andReturn();
    }

    @Test
    public void testRegularAPICall() throws Exception {
        //test call with "singleValued" VO path param and identity reference path param in flat String representation
        final MvcResult response = mockMvc.perform(
                MockMvcRequestBuilders.get(API_CALL_PREFIX + "/1/abc?bestellPositionId=5&kundennummern=2,3,4"))
            .andExpect(status().isOk())
            .andReturn();
        var res = (AutoMappedComplexVo) objectMapper.readValue(response.getResponse().getContentAsString(),
            AutoMappedComplexVo.class);
        assertThat(res).isNotNull();
        assertThat(res.getValueA()).isEqualTo("1 5 2 3 4");
        assertThat(res.getValueB()).isEqualTo(AutoMappedSimpleVo.builder().setValue("abc").build());
    }

    @Test
    public void testFlatAPIDocRepresentationOfSingleValueVOReferenceAsString() {
        assertPropertyTypeAndGetSchema(AutoMappedComplexVo.class, "valueB", SCHEMA_TYPE_STRING, null, null);
    }

    @Test
    public void testFlatAPIDocRepresentationOfIdentityReferenceAsString() {
        assertPropertyTypeAndGetSchema(Bestellung.class, "kundennummer", SCHEMA_TYPE_STRING, null, null);
    }

    @Test
    public void testFlatAPIDocRepresentationOfListOfSingleValuedVOReferenceAsStringArray() {
        Schema schema = assertPropertyTypeAndGetSchema(Bestellung.class, "aktionsCodes", SCHEMA_TYPE_ARRAY, null, null);
        Schema itemSchema = schema.getItems();
        assertThat(itemSchema).isNotNull();
        assertThat(itemSchema.getType()).isEqualTo(SCHEMA_TYPE_STRING);
        assertThat(itemSchema.getMaxLength()).isEqualTo(10);
    }

    @Test
    public void testBestellungIdPrimaryNotRequired() {
        Schema schema = assertPropertyTypeAndGetSchema(Bestellung.class, "id", SCHEMA_TYPE_INTEGER, FORMAT_TYPE_INT64,
            null);
        var openAPI = openAPIService.getCachedOpenAPI(Locale.getDefault());
        var testSchema = openAPI.getComponents().getSchemas().get(Bestellung.class.getName());
        assertThat(testSchema).isNotNull();
        assertThat(testSchema.getRequired()).doesNotContain("id");
    }

    @Test
    public void testControllerParameters() {
        var openAPI = openAPIService.getCachedOpenAPI(Locale.getDefault());
        var pathItemOptional = openAPI.getPaths()
            .values()
            .stream()
            .filter(item -> {
                return item.getGet() != null && "getComplexVO".equals(item.getGet().getOperationId());
            })
            .findFirst();
        assertThat(pathItemOptional).isPresent();
        assertThat(pathItemOptional.get().getGet().getParameters()).isNotNull();
        for (Parameter p : pathItemOptional.get().getGet().getParameters()) {
            if (p.getName().equals("kundennummern")) {
                assertThat(p.getSchema().getType()).isEqualTo(SCHEMA_TYPE_ARRAY);
                assertThat(p.getSchema().getItems().getType()).isEqualTo(SCHEMA_TYPE_STRING);
            }
            if (p.getName().equals("bestellungId")) {
                assertThat(p.getSchema().getType()).isEqualTo(SCHEMA_TYPE_INTEGER);
            }
            if (p.getName().equals("bestellPositionId")) {
                assertThat(p.getSchema().getType()).isEqualTo(SCHEMA_TYPE_INTEGER);
            }
            if (p.getName().equals("simpleVo")) {
                assertThat(p.getSchema().getType()).isEqualTo(SCHEMA_TYPE_STRING);
            }
        }
    }


    private Schema assertPropertyTypeAndGetSchema(Class containingClass, String propertyName,
                                                  String expectedPropertyType, String expectedFormat,
                                                  String expectedPattern) {
        var openAPI = openAPIService.getCachedOpenAPI(Locale.getDefault());
        var testSchema = openAPI.getComponents().getSchemas().get(containingClass.getName());
        var propertySchema = (Schema) testSchema.getProperties().get(propertyName);
        assertThat(propertySchema.getType()).isEqualTo(expectedPropertyType);
        assertThat(propertySchema.getFormat()).isEqualTo(expectedFormat);
        assertThat(propertySchema.getPattern()).isEqualTo(expectedPattern);
        return propertySchema;
    }

    @Test
    public void testCommandSchema() {
        var openAPI = openAPIService.getCachedOpenAPI(Locale.getDefault());
        var testSchema = openAPI.getComponents().getSchemas().get(TestCommand.class.getName());
        assertThat(testSchema.getProperties()).isNotNull();
        assertThat(testSchema.getProperties().get("complexVo")).isNotNull();
        assertThat(((Schema<?>) testSchema.getProperties().get("complexVo")).get$ref()).contains(
            AutoMappedComplexVo.class.getName());
        assertThat(testSchema.getProperties().get("simpleVo")).isNotNull();
        assertThat(((Schema<?>) testSchema.getProperties().get("simpleVo")).get$ref()).isNull();
        assertThat(testSchema.getProperties().get("voEntity")).isNotNull();
        assertThat(((Schema<?>) testSchema.getProperties().get("voEntity")).get$ref()).contains(
            AutoMappedVoEntity.class.getName());
        assertThat(testSchema.getProperties().get("voEntityId")).isNotNull();
        assertThat(((Schema<?>) testSchema.getProperties().get("voEntityId")).get$ref()).isNull();
        assertThat(testSchema.getProperties().get("voList")).isNotNull();
        assertThat(((Schema<?>) testSchema.getProperties().get("voList")).getItems()).isNotNull();
        assertThat(((Schema<?>) testSchema.getProperties().get("voList")).getItems().get$ref()).isNull();
        assertThat(testSchema.getProperties().get("entityList")).isNotNull();
        assertThat(((Schema<?>) testSchema.getProperties().get("entityList")).getItems()).isNotNull();
        assertThat(((Schema<?>) testSchema.getProperties().get("entityList")).getItems().get$ref()).contains(
            AutoMappedVoEntity.class.getName());
        assertThat(testSchema.getProperties().get("idList")).isNotNull();
        assertThat(((Schema<?>) testSchema.getProperties().get("idList")).getItems()).isNotNull();
        assertThat(((Schema<?>) testSchema.getProperties().get("idList")).getItems().get$ref()).isNull();

    }

}



package io.domainlifecycles.springboot4.openapi;

import io.domainlifecycles.assertion.DomainAssertionException;
import io.domainlifecycles.validation.BeanValidations;
import io.swagger.v3.oas.models.SpecVersion;
import io.swagger.v3.oas.models.media.Schema;
import jakarta.validation.Validation;
import jakarta.validation.metadata.ConstraintDescriptor;
import jakarta.validation.metadata.ContainerElementTypeDescriptor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springdoc.core.service.OpenAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tests.shared.complete.onlinehandel.bestellung.BestellungIdBv3;
import tests.shared.complete.onlinehandel.bestellung.KundennummerBv3;
import tests.shared.openapi.TestId;
import tests.shared.openapi.TestIdInterface;
import tests.shared.openapi.jakarta.TestDTO2;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test"})
@Slf4j
@AutoConfigureMockMvc
public class OpenApiTestDTO2_SpringBoot4_ITest {

    @Autowired
    private OpenAPIService openAPIService;

    private static final String API_DOCS_PATH = "/v3/api-docs";

    private static final String SCHEMA_TYPE_STRING = "string";
    private static final String SCHEMA_TYPE_NUMBER = "number";
    private static final String SCHEMA_TYPE_ARRAY = "array";

    private static final String SCHEMA_TYPE_INTEGER = "integer";

    private static final String FORMAT_TYPE_DATE_TIME = "date-time";
    private static final String FORMAT_TYPE_DATE = "date";

    private static final String FORMAT_TYPE_INT32 = "int32";

    private static final String FORMAT_TYPE_INT64 = "int64";

    private static final String FORMAT_TYPE_BYTE = "byte";

    private static final String FORMAT_TYPE_FLOAT = "float";

    private static final String FORMAT_TYPE_DOUBLE = "double";

    private static final String FORMAT_TYPE_EMAIL = "email";

    @Autowired
    private MockMvc mockMvc;

    private static volatile boolean apiDocCreated = false;

    @BeforeEach
    public void init() throws Exception {
        Locale.setDefault(Locale.ENGLISH);
        synchronized (new Object()) {
            if (!apiDocCreated) {
                final MvcResult response = mockMvc
                    .perform(MockMvcRequestBuilders.get(API_DOCS_PATH).locale(Locale.ENGLISH))
                    .andExpect(status().isOk())
                    .andReturn();
                apiDocCreated = true;
            }
        }

    }

    @Test
    public void testValidationErrors() {
        DomainAssertionException ex = assertThrows(DomainAssertionException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                TestDTO2 t = TestDTO2
                    .builder()
                    .stringArrayNotEmpty(new String[]{})
                    .aByteDecimalMaxExclusive((byte) 5)
                    .aByteDecimalMin((byte) 4)
                    .aByteNegative((byte) 5)
                    .aByteDigits((byte) 10)
                    .aByteMax((byte) 6)
                    .aByteMin((byte) 4)
                    .aDoubleDecimalMaxExclusive(5)
                    .aDoubleDecimalMin(4)
                    .aDoubleNegative(1)
                    .aDoubleDigits(10.666)
                    .aFloatDecimalMaxExclusive(5)
                    .aFloatDecimalMin(4)
                    .aFloatNegative(4)
                    .aFloatDigits(1000)
                    .aLongDecimalMaxExclusive(10)
                    .aLongDecimalMin(4)
                    .aLongNegative(4)
                    .aLongDigits(1000)
                    .aLongMax(10)
                    .aLongMin(4)
                    .anIntDecimalMaxExclusive(5)
                    .anIntDecimalMin(4)
                    .anIntNegative(4)
                    .anIntDigits(1010)
                    .anIntMax(10)
                    .anIntMin(4)
                    .aShortDecimalMaxExclusive((short) 5)
                    .aShortDecimalMin((short) 4)
                    .aShortNegative((short) 4)
                    .aShortDigits((short) 1000)
                    .aShortMax((short) 10)
                    .aShortMin((short) 4)
                    .bigDecimalDecimalMaxExclusive(BigDecimal.TEN)
                    .bigDecimalDecimalMin(BigDecimal.ONE)
                    .bigDecimalNegative(BigDecimal.ONE)
                    .bigDecimalDigits(BigDecimal.valueOf(1000.01))
                    .bigDecimalMax(BigDecimal.TEN)
                    .bigDecimalMin(BigDecimal.ONE)
                    .bigIntegerDecimalMaxExclusive(BigInteger.TEN)
                    .bigIntegerDecimalMin(BigInteger.ONE)
                    .bigIntegerNegative(BigInteger.ONE)
                    .bigIntegerDigits(BigInteger.valueOf(1000))
                    .bigIntegerMax(BigInteger.TEN)
                    .bigIntegerMin(BigInteger.ONE)
                    .instantPast(Instant.now().plus(10, ChronoUnit.DAYS))
                    .localDateFuture(LocalDate.now())
                    .localDateTimePastOrPresent(LocalDateTime.MAX)
                    .localTimeNotNull(null)
                    .monthDayPast(MonthDay.now())
                    .offsetTimePast(OffsetTime.MAX)
                    .offsetDateTimeNotNull(null)
                    .optionalBigDecimalDecimalMaxExclusive(BigDecimal.TEN)
                    .optionalBigDecimalDecimalMin(BigDecimal.ONE)
                    .optionalBigDecimalDigits(BigDecimal.valueOf(10.234))
                    .optionalBigDecimalMax(BigDecimal.TEN)
                    .optionalBigDecimalMin(BigDecimal.ONE)
                    .optionalBigDecimalNegative(BigDecimal.ONE)
                    .optionalBigIntegerDecimalMaxExclusive(BigInteger.TEN)
                    .optionalBigIntegerDecimalMin(BigInteger.ONE)
                    .optionalBigIntegerDigits(BigInteger.valueOf(1001))
                    .optionalBigIntegerMax(BigInteger.TEN)
                    .optionalBigIntegerMin(BigInteger.ONE)
                    .optionalBigIntegerNegative(BigInteger.ONE)
                    .optionalByteDecimalMaxExclusive((byte) 10)
                    .optionalByteDecimalMin((byte) 0)
                    .optionalByteDigits((byte) 10)
                    .optionalByteMax((byte) 10)
                    .optionalByteMin((byte) 0)
                    .optionalByteNegative((byte) 10)
                    .optionalDoubleDecimalMaxExclusive(Double.valueOf(10))
                    .optionalDoubleDecimalMin(Double.valueOf(1))
                    .optionalDoubleDigits(Double.valueOf(1000.0))
                    .optionalDoubleNegative(Double.valueOf(0))
                    .optionalFloatDecimalMaxExclusive(Float.valueOf(10))
                    .optionalFloatDecimalMin(Float.valueOf(2))
                    .optionalFloatDigits(Float.valueOf(10.344f))
                    .optionalFloatNegative(Float.valueOf(10))
                    .optionalInstantPast(Instant.MAX)
                    .optionalIntegerDecimalMaxExclusive(10)
                    .optionalIntegerDecimalMin(0)
                    .optionalIntegerDigits(1000)
                    .optionalIntegerMax(10)
                    .optionalIntegerMin(0)
                    .optionalIntegerNegative(4)
                    .optionalLocalDateFuture(LocalDate.now())
                    .optionalLocalDateTimePastOrPresent(LocalDateTime.MAX)
                    .optionalLocalTimePast(LocalTime.MAX)
                    .optionalLongDecimalMaxExclusive(10l)
                    .optionalLongDecimalMin(4l)
                    .optionalLongDigits(1000l)
                    .optionalLongMax(10l)
                    .optionalLongMin(2l)
                    .optionalLongNegative(3l)
                    .optionalMonthDayPast(MonthDay.now())
                    .optionalOffsetDateTimePast(OffsetDateTime.MAX)
                    .optionalOffsetTimePast(OffsetTime.MAX)
                    .optionalZonedDateTimeFutureOrPresent(ZonedDateTime.now().minus(1, ChronoUnit.DAYS))
                    .optionalShortDecimalMaxExclusive((short) 5)
                    .optionalShortDecimalMin((short) 3)
                    .optionalShortDigits((short) 1001)
                    .optionalShortMax((short) 6)
                    .optionalShortMin((short) 4)
                    .optionalShortNegative((short) 3)
                    .optionalStringSized("1234567890")
                    .optionalYearMonthPast(YearMonth.now())
                    .optionalYearPast(Year.now())
                    .stringArraySized(new String[]{"1", "2", "3"})
                    .stringEmail("bla")
                    .stringListNotEmpty(new ArrayList<>())
                    .stringListSized(List.of("1", "2", "3"))
                    .stringNotBlank(" ")
                    .stringNotEmpty("")
                    .stringNotNull(null)
                    .stringPattern("abx")
                    .stringSized("12334455556")
                    .yearMonthPast(YearMonth.now())
                    .yearPast(Year.now())
                    .zonedDateTimeFutureOrPresent(ZonedDateTime.now().minus(1, ChronoUnit.DAYS))
                    .yearPast(Year.now())
                    .optionalStringEmail("blubb")
                    .optionalStringPattern("blubb")
                    .build();
                BeanValidations.validate(t);
            }

        });
        for (Field f : TestDTO2.class.getDeclaredFields()) {
            if (!f.getName().equals("testId")
                && !f.getName().equals("optionalTestId")
                && !f.getName().equals("optionalTestVo2")
                && !f.getName().equals("optionalTestEnum")
                && !f.getName().equals("testIdInterface")) {
                assertThat(ex.getMessage()).contains(f.getName());
            }
        }
        assertThat(ex.getMessage().chars().filter(ch -> ch == '\n').count()).isEqualTo(
            TestDTO2.class.getDeclaredFields().length - 5);
    }

    @Test
    public void testSchemaStringNotEmpty() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "stringNotEmpty", SCHEMA_TYPE_STRING, null,
            null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "stringNotEmpty", false);
        assertThat(schema.getMinLength()).isEqualTo(1);
    }

    @Test
    public void testSchemaStringEmail() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "stringEmail", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_EMAIL, null);
    }

    @Test
    public void testSchemaOptionalStringEmail() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalStringEmail", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_EMAIL, null);
        assertOptional(schema);
    }

    @Test
    public void testSchemaStringPattern() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "stringPattern", SCHEMA_TYPE_STRING, null,
            "[0-9]");
    }

    @Test
    public void testSchemaOptionalStringPattern() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalStringPattern", SCHEMA_TYPE_STRING,
            null, "[0-9]");
        assertOptional(schema);
    }

    @Test
    public void testSchemaStringSized() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "stringSized", SCHEMA_TYPE_STRING, null, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "stringSized", false);
        assertThat(schema.getMaxLength()).isEqualTo(desc.getAttributes().get("max"));
        assertThat(schema.getMinLength()).isEqualTo(desc.getAttributes().get("min"));
    }

    @Test
    public void testSchemaOptionalStringSized() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalStringSized", SCHEMA_TYPE_STRING, null,
            null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalStringSized", true);
        assertThat(schema.getMaxLength()).isEqualTo(desc.getAttributes().get("max"));
        assertThat(schema.getMinLength()).isEqualTo(desc.getAttributes().get("min"));
        assertOptional(schema);
    }

    @Test
    public void testSchemaStringListSized() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "stringListSized", SCHEMA_TYPE_ARRAY, null,
            null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "stringListSized", false);
        assertThat(schema.getMinItems()).isEqualTo(desc.getAttributes().get("min"));
        assertThat(schema.getMaxItems()).isEqualTo(desc.getAttributes().get("max"));
    }

    @Test
    public void testSchemaStringArraySized() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "stringArraySized", SCHEMA_TYPE_ARRAY, null,
            null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "stringArraySized", false);
        assertThat(schema.getMinItems()).isEqualTo(desc.getAttributes().get("min"));
        assertThat(schema.getMaxItems()).isEqualTo(desc.getAttributes().get("max"));
    }

    @Test
    public void testSchemaStringListNotEmpty() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "stringListNotEmpty", SCHEMA_TYPE_ARRAY, null,
            null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "stringListNotEmpty", false);
        assertThat(schema.getMinItems()).isEqualTo(1);
    }

    @Test
    public void testSchemaStringArrayNotEmpty() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "stringArrayNotEmpty", SCHEMA_TYPE_ARRAY, null,
            null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "stringArrayNotEmpty", false);
        assertThat(schema.getMinItems()).isEqualTo(1);
    }

    @Test
    public void testSchemaAnIntMin() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "anIntMin", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "anIntMin", false);
        assertThat(schema.getMinimum().longValue()).isEqualTo(desc.getAttributes().get("value"));
        assertPropertyRequired(TestDTO2.class, "anIntMin");
    }

    @Test
    public void testSchemaOptionalIntegerMin() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalIntegerMin", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalIntegerMin", true);
        assertThat(schema.getMinimum().longValue()).isEqualTo(desc.getAttributes().get("value"));
        assertOptional(schema);
    }

    @Test
    public void testSchemaAShortMin() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "ashortMin", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "aShortMin", false);
        assertThat(schema.getMinimum().longValue()).isEqualTo(desc.getAttributes().get("value"));
        assertPropertyRequired(TestDTO2.class, "ashortMin");
    }

    @Test
    public void testSchemaOptionalShortMin() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalShortMin", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalShortMin", true);
        assertThat(schema.getMinimum().longValue()).isEqualTo(desc.getAttributes().get("value"));
        assertOptional(schema);
    }

    @Test
    public void testSchemaAByteMin() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "abyteMin", SCHEMA_TYPE_STRING, FORMAT_TYPE_BYTE,
            null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "aByteMin", false);
        assertThat(schema.getMinimum().longValue()).isEqualTo(desc.getAttributes().get("value"));
        assertPropertyRequired(TestDTO2.class, "abyteMin");
    }

    @Test
    public void testSchemaOptionalByteMin() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalByteMin", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_BYTE, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalByteMin", true);
        assertThat(schema.getMinimum().longValue()).isEqualTo(desc.getAttributes().get("value"));
        assertOptional(schema);
    }

    @Test
    public void testSchemaALongMin() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "alongMin", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT64, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "aLongMin", false);
        assertThat(schema.getMinimum().longValue()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaOptionalLongMin() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalLongMin", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT64, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalLongMin", true);
        assertThat(schema.getMinimum().longValue()).isEqualTo(desc.getAttributes().get("value"));
        assertOptional(schema);
    }

    @Test
    public void testSchemaBigDecimalMin() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "bigDecimalMin", SCHEMA_TYPE_NUMBER, null, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "bigDecimalMin", false);
        assertThat(schema.getMinimum().longValue()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaOptionalBigDecimalMin() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalBigDecimalMin", SCHEMA_TYPE_NUMBER,
            null, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalBigDecimalMin", true);
        assertThat(schema.getMinimum().longValue()).isEqualTo(desc.getAttributes().get("value"));
        assertOptional(schema);
    }

    @Test
    public void testSchemaBigIntegerMin() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "bigIntegerMin", SCHEMA_TYPE_INTEGER, null,
            null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "bigIntegerMin", false);
        assertThat(schema.getMinimum().longValue()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaOptionalBigIntegerMin() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalBigIntegerMin", SCHEMA_TYPE_INTEGER,
            null, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalBigIntegerMin", true);
        assertThat(schema.getMinimum().longValue()).isEqualTo(desc.getAttributes().get("value"));
        assertOptional(schema);
    }

    @Test
    public void testSchemaAnIntMax() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "anIntMax", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "anIntMax", false);
        assertThat(schema.getMaximum().longValue()).isEqualTo(desc.getAttributes().get("value"));
        assertPropertyRequired(TestDTO2.class, "anIntMax");
    }

    @Test
    public void testSchemaOptionalIntegerMax() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalIntegerMax", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalIntegerMax", true);
        assertThat(schema.getMaximum().longValue()).isEqualTo(desc.getAttributes().get("value"));
        assertOptional(schema);
    }

    @Test
    public void testSchemaAShortMax() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "ashortMax", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "aShortMax", false);
        assertThat(schema.getMaximum().longValue()).isEqualTo(desc.getAttributes().get("value"));
        assertPropertyRequired(TestDTO2.class, "ashortMax");
    }

    @Test
    public void testSchemaOptionalShortMax() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalShortMax", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalShortMax", true);
        assertThat(schema.getMaximum().longValue()).isEqualTo(desc.getAttributes().get("value"));
        assertOptional(schema);
    }

    @Test
    public void testSchemaAByteMax() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "abyteMax", SCHEMA_TYPE_STRING, FORMAT_TYPE_BYTE,
            null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "aByteMax", false);
        assertThat(schema.getMaximum().longValue()).isEqualTo(desc.getAttributes().get("value"));
        assertPropertyRequired(TestDTO2.class, "abyteMax");
    }

    @Test
    public void testSchemaOptionalByteMax() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalByteMax", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_BYTE, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalByteMax", true);
        assertThat(schema.getMaximum().longValue()).isEqualTo(desc.getAttributes().get("value"));
        assertOptional(schema);
    }

    @Test
    public void testSchemaALongMax() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "alongMax", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT64, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "aLongMax", false);
        assertThat(schema.getMaximum().longValue()).isEqualTo(desc.getAttributes().get("value"));
        assertPropertyRequired(TestDTO2.class, "alongMax");
    }

    @Test
    public void testSchemaOptionalLongMax() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalLongMax", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT64, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalLongMax", true);
        assertThat(schema.getMaximum().longValue()).isEqualTo(desc.getAttributes().get("value"));
        assertOptional(schema);
    }

    @Test
    public void testSchemaBigDecimalMax() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "bigDecimalMax", SCHEMA_TYPE_NUMBER, null, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "bigDecimalMax", false);
        assertThat(schema.getMaximum().longValue()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaOptionalBigDecimalMax() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalBigDecimalMax", SCHEMA_TYPE_NUMBER,
            null, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalBigDecimalMax", true);
        assertThat(schema.getMaximum().longValue()).isEqualTo(desc.getAttributes().get("value"));
        assertOptional(schema);
    }

    @Test
    public void testSchemaBigIntegerMax() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "bigIntegerMax", SCHEMA_TYPE_INTEGER, null,
            null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "bigIntegerMax", false);
        assertThat(schema.getMaximum().longValue()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaOptionalBigIntegerMax() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalBigIntegerMax", SCHEMA_TYPE_INTEGER,
            null, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalBigIntegerMax", true);
        assertThat(schema.getMaximum().longValue()).isEqualTo(desc.getAttributes().get("value"));
        assertOptional(schema);
    }

    @Test
    public void testSchemaAnIntDecimalMin() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "anIntDecimalMin", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "anIntDecimalMin", false);
        assertThat(schema.getMinimum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertPropertyRequired(TestDTO2.class, "anIntDecimalMin");
    }

    @Test
    public void testSchemaOptionalIntegerDecimalMin() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalIntegerDecimalMin", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalIntegerDecimalMin", true);
        assertThat(schema.getMinimum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertOptional(schema);
    }

    @Test
    public void testSchemaAShortDecimalMin() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "ashortDecimalMin", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "aShortDecimalMin", false);
        assertThat(schema.getMinimum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertPropertyRequired(TestDTO2.class, "ashortDecimalMin");
    }

    @Test
    public void testSchemaOptionalShortDecimalMin() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalShortDecimalMin", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalShortDecimalMin", true);
        assertThat(schema.getMinimum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertOptional(schema);
    }

    @Test
    public void testSchemaAByteDecimalMin() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "abyteDecimalMin", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_BYTE, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "aByteDecimalMin", false);
        assertThat(schema.getMinimum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertPropertyRequired(TestDTO2.class, "abyteDecimalMin");
    }

    @Test
    public void testSchemaOptionalByteDecimalMin() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalByteDecimalMin", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_BYTE, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalByteDecimalMin", true);
        assertThat(schema.getMinimum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertOptional(schema);
    }

    @Test
    public void testSchemaALongDecimalMin() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "alongDecimalMin", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT64, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "aLongDecimalMin", false);
        assertThat(schema.getMinimum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertPropertyRequired(TestDTO2.class, "alongDecimalMin");
    }

    @Test
    public void testSchemaOptionalLongDecimalMin() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalLongDecimalMin", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT64, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalLongDecimalMin", true);
        assertThat(schema.getMinimum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertOptional(schema);
    }

    @Test
    public void testSchemaBigDecimalDecimalMin() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "bigDecimalDecimalMin", SCHEMA_TYPE_NUMBER, null,
            null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "bigDecimalDecimalMin", false);
        assertThat(schema.getMinimum().toString()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaOptionalBigDecimalDecimalMin() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalBigDecimalDecimalMin",
            SCHEMA_TYPE_NUMBER, null, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalBigDecimalDecimalMin", true);
        assertThat(schema.getMinimum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertOptional(schema);
    }

    @Test
    public void testSchemaBigIntegerDecimalMin() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "bigIntegerDecimalMin", SCHEMA_TYPE_INTEGER,
            null, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "bigIntegerDecimalMin", false);
        assertThat(schema.getMinimum().toString()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaOptionalBigIntegerDecimalMin() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalBigIntegerDecimalMin",
            SCHEMA_TYPE_INTEGER, null, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalBigIntegerDecimalMin", true);
        assertThat(schema.getMinimum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertOptional(schema);
    }

    @Test
    public void testSchemaAFloatDecimalMin() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "afloatDecimalMin", SCHEMA_TYPE_NUMBER,
            FORMAT_TYPE_FLOAT, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "aFloatDecimalMin", false);
        assertThat(schema.getMinimum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertPropertyRequired(TestDTO2.class, "afloatDecimalMin");
    }

    @Test
    public void testSchemaOptionalFloatDecimalMin() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalFloatDecimalMin", SCHEMA_TYPE_NUMBER,
            FORMAT_TYPE_FLOAT, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalFloatDecimalMin", true);
        assertThat(schema.getMinimum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertOptional(schema);
    }

    @Test
    public void testSchemaADoubleDecimalMin() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "adoubleDecimalMin", SCHEMA_TYPE_NUMBER,
            FORMAT_TYPE_DOUBLE, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "aDoubleDecimalMin", false);
        assertThat(schema.getMinimum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertPropertyRequired(TestDTO2.class, "adoubleDecimalMin");
    }

    @Test
    public void testSchemaOptionalDoubleDecimalMin() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalDoubleDecimalMin", SCHEMA_TYPE_NUMBER,
            FORMAT_TYPE_DOUBLE, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalDoubleDecimalMin", true);
        assertThat(schema.getMinimum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertOptional(schema);
    }

    @Test
    public void testSchemaAnIntDecimalMaxExclusive() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "anIntDecimalMaxExclusive", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "anIntDecimalMaxExclusive", false);
        assertThat(schema.getMaximum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        assertPropertyRequired(TestDTO2.class, "anIntDecimalMaxExclusive");
    }

    @Test
    public void testSchemaOptionalIntegerDecimalMaxExclusive() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalIntegerDecimalMaxExclusive",
            SCHEMA_TYPE_INTEGER, FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalIntegerDecimalMaxExclusive",
            true);
        assertThat(schema.getMaximum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        assertOptional(schema);
    }

    @Test
    public void testSchemaAShortDecimalMaxExclusive() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "ashortDecimalMaxExclusive", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "aShortDecimalMaxExclusive", false);
        if(schema.getSpecVersion().equals(SpecVersion.V30)){
            assertThat(schema.getMaximum().toString()).isEqualTo(desc.getAttributes().get("value"));
            assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        }
        if(schema.getSpecVersion().equals(SpecVersion.V31)){
            assertThat(schema.getExclusiveMaximumValue().toString()).isEqualTo(desc.getAttributes().get("value"));
        }
        assertPropertyRequired(TestDTO2.class, "ashortDecimalMaxExclusive");
    }

    @Test
    public void testSchemaOptionalShortDecimalMaxExclusive() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalShortDecimalMaxExclusive",
            SCHEMA_TYPE_INTEGER, FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalShortDecimalMaxExclusive",
            true);
        assertThat(schema.getMaximum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        assertOptional(schema);
    }

    @Test
    public void testSchemaAByteDecimalMaxExclusive() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "abyteDecimalMaxExclusive", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_BYTE, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "aByteDecimalMaxExclusive", false);
        if(schema.getSpecVersion().equals(SpecVersion.V30)){
            assertThat(schema.getMaximum().toString()).isEqualTo(desc.getAttributes().get("value"));
            assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        }
        if(schema.getSpecVersion().equals(SpecVersion.V31)){
            assertThat(schema.getExclusiveMaximumValue().toString()).isEqualTo(desc.getAttributes().get("value"));
        }
        assertPropertyRequired(TestDTO2.class, "abyteDecimalMaxExclusive");
    }

    @Test
    public void testSchemaOptionalByteDecimalMaxExclusive() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalByteDecimalMaxExclusive",
            SCHEMA_TYPE_STRING, FORMAT_TYPE_BYTE, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalByteDecimalMaxExclusive", true);
        if(schema.getSpecVersion().equals(SpecVersion.V30)) {
            assertThat(schema.getMaximum().toString()).isEqualTo(desc.getAttributes().get("value"));
            assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        }
        if(schema.getSpecVersion().equals(SpecVersion.V31)){
            assertThat(schema.getExclusiveMaximumValue().toString()).isEqualTo(desc.getAttributes().get("value"));
        }
        assertOptional(schema);
    }

    @Test
    public void testSchemaALongDecimalMaxExclusive() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "alongDecimalMaxExclusive", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT64, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "aLongDecimalMaxExclusive", false);
        if(schema.getSpecVersion().equals(SpecVersion.V30)){
            assertThat(schema.getMaximum().toString()).isEqualTo(desc.getAttributes().get("value"));
            assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        }
        if(schema.getSpecVersion().equals(SpecVersion.V31)){
            assertThat(schema.getExclusiveMaximumValue().toString()).isEqualTo(desc.getAttributes().get("value"));
        }
        assertPropertyRequired(TestDTO2.class, "alongDecimalMaxExclusive");
    }

    @Test
    public void testSchemaOptionalLongDecimalMaxExclusive() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalLongDecimalMaxExclusive",
            SCHEMA_TYPE_INTEGER, FORMAT_TYPE_INT64, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalLongDecimalMaxExclusive", true);
        assertThat(schema.getMaximum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        assertOptional(schema);
    }

    @Test
    public void testSchemaBigDecimalDecimalMaxExclusive() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "bigDecimalDecimalMaxExclusive",
            SCHEMA_TYPE_NUMBER, null, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "bigDecimalDecimalMaxExclusive", false);
        assertThat(schema.getMaximum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
    }

    @Test
    public void testSchemaOptionalBigDecimalDecimalMaxExclusive() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalBigDecimalDecimalMaxExclusive",
            SCHEMA_TYPE_NUMBER, null, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalBigDecimalDecimalMaxExclusive",
            true);
        assertThat(schema.getMaximum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        assertOptional(schema);
    }

    @Test
    public void testSchemaBigIntegerDecimalMaxExclusive() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "bigIntegerDecimalMaxExclusive",
            SCHEMA_TYPE_INTEGER, null, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "bigIntegerDecimalMaxExclusive", false);
        assertThat(schema.getMaximum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
    }

    @Test
    public void testSchemaOptionalBigIntegerDecimalMaxExclusive() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalBigIntegerDecimalMaxExclusive",
            SCHEMA_TYPE_INTEGER, null, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalBigIntegerDecimalMaxExclusive",
            true);
        assertThat(schema.getMaximum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        assertOptional(schema);
    }

    @Test
    public void testSchemaAFloatDecimalMaxExclusive() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "afloatDecimalMaxExclusive", SCHEMA_TYPE_NUMBER,
            FORMAT_TYPE_FLOAT, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "aFloatDecimalMaxExclusive", false);
        if(schema.getSpecVersion().equals(SpecVersion.V30)){
            assertThat(schema.getMaximum().toString()).isEqualTo(desc.getAttributes().get("value"));
            assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        }
        if(schema.getSpecVersion().equals(SpecVersion.V31)){
            assertThat(schema.getExclusiveMaximumValue().toString()).isEqualTo(desc.getAttributes().get("value"));
        }
        assertPropertyRequired(TestDTO2.class, "afloatDecimalMaxExclusive");
    }

    @Test
    public void testSchemaOptionalFloatDecimalMaxExclusive() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalFloatDecimalMaxExclusive",
            SCHEMA_TYPE_NUMBER, FORMAT_TYPE_FLOAT, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalFloatDecimalMaxExclusive",
            true);
        assertThat(schema.getMaximum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        assertOptional(schema);
    }

    @Test
    public void testSchemaADoubleDecimalMaxExclusive() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "adoubleDecimalMaxExclusive", SCHEMA_TYPE_NUMBER,
            FORMAT_TYPE_DOUBLE, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "aDoubleDecimalMaxExclusive", false);
        if(schema.getSpecVersion().equals(SpecVersion.V30)){
            assertThat(schema.getMaximum().toString()).isEqualTo(desc.getAttributes().get("value"));
            assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        }
        if(schema.getSpecVersion().equals(SpecVersion.V31)){
            assertThat(schema.getExclusiveMaximumValue().toString()).isEqualTo(desc.getAttributes().get("value"));
        }
        assertPropertyRequired(TestDTO2.class, "adoubleDecimalMaxExclusive");
    }

    @Test
    public void testSchemaOptionalDoubleDecimalMaxExclusive() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalDoubleDecimalMaxExclusive",
            SCHEMA_TYPE_NUMBER, FORMAT_TYPE_DOUBLE, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalDoubleDecimalMaxExclusive",
            true);
        assertThat(schema.getMaximum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        assertOptional(schema);
    }

    @Test
    public void testSchemaAnIntNegative() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "anIntNegative", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "anIntNegative", false);
        if(schema.getSpecVersion().equals(SpecVersion.V30)){
            assertThat(schema.getMaximum()).isEqualTo(BigDecimal.ZERO);
            assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        }
        if(schema.getSpecVersion().equals(SpecVersion.V31)){
            assertThat(schema.getExclusiveMaximumValue()).isEqualTo(BigDecimal.ZERO);
        }
        assertPropertyRequired(TestDTO2.class, "anIntNegative");
    }

    @Test
    public void testSchemaOptionalIntegerNegative() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalIntegerNegative", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalIntegerNegative", true);
        if(schema.getSpecVersion().equals(SpecVersion.V30)){
            assertThat(schema.getMaximum()).isEqualTo(BigDecimal.ZERO);
            assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        }
        if(schema.getSpecVersion().equals(SpecVersion.V31)){
            assertThat(schema.getExclusiveMaximumValue()).isEqualTo(BigDecimal.ZERO);
        }
        assertOptional(schema);
    }

    @Test
    public void testSchemaAShortNegative() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "ashortNegative", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "aShortNegative", false);
        if(schema.getSpecVersion().equals(SpecVersion.V30)){
            assertThat(schema.getMaximum()).isEqualTo(BigDecimal.ZERO);
            assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        }
        if(schema.getSpecVersion().equals(SpecVersion.V31)){
            assertThat(schema.getExclusiveMaximumValue()).isEqualTo(BigDecimal.ZERO);
        }
        assertPropertyRequired(TestDTO2.class, "ashortNegative");
    }

    @Test
    public void testSchemaOptionalShortNegative() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalShortNegative", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalShortNegative", true);
        if(schema.getSpecVersion().equals(SpecVersion.V30)){
            assertThat(schema.getMaximum()).isEqualTo(BigDecimal.ZERO);
            assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        }
        if(schema.getSpecVersion().equals(SpecVersion.V31)){
            assertThat(schema.getExclusiveMaximumValue()).isEqualTo(BigDecimal.ZERO);
        }
        assertOptional(schema);
    }

    @Test
    public void testSchemaAByteNegative() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "abyteNegative", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_BYTE, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "aByteNegative", false);
        if(schema.getSpecVersion().equals(SpecVersion.V30)){
            assertThat(schema.getMaximum()).isEqualTo(BigDecimal.ZERO);
            assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        }
        if(schema.getSpecVersion().equals(SpecVersion.V31)){
            assertThat(schema.getExclusiveMaximumValue()).isEqualTo(BigDecimal.ZERO);
        }
        assertPropertyRequired(TestDTO2.class, "abyteNegative");
    }

    @Test
    public void testSchemaOptionalByteNegative() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalByteNegative", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_BYTE, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalByteNegative", true);
        if(schema.getSpecVersion().equals(SpecVersion.V30)){
            assertThat(schema.getMaximum()).isEqualTo(BigDecimal.ZERO);
            assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        }
        if(schema.getSpecVersion().equals(SpecVersion.V31)){
            assertThat(schema.getExclusiveMaximumValue()).isEqualTo(BigDecimal.ZERO);
        }
        assertOptional(schema);
    }

    @Test
    public void testSchemaALongNegative() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "alongNegative", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT64, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "aLongNegative", false);
        if(schema.getSpecVersion().equals(SpecVersion.V30)){
            assertThat(schema.getMaximum()).isEqualTo(BigDecimal.ZERO);
            assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        }
        if(schema.getSpecVersion().equals(SpecVersion.V31)){
            assertThat(schema.getExclusiveMaximumValue()).isEqualTo(BigDecimal.ZERO);
        }
        assertPropertyRequired(TestDTO2.class, "alongNegative");
    }

    @Test
    public void testSchemaOptionalLongNegative() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalLongNegative", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT64, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalLongNegative", true);
        if(schema.getSpecVersion().equals(SpecVersion.V30)){
            assertThat(schema.getMaximum()).isEqualTo(BigDecimal.ZERO);
            assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        }
        if(schema.getSpecVersion().equals(SpecVersion.V31)){
            assertThat(schema.getExclusiveMaximumValue()).isEqualTo(BigDecimal.ZERO);
        }
        assertOptional(schema);
    }

    @Test
    public void testSchemaBigDecimalNegative() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "bigDecimalNegative", SCHEMA_TYPE_NUMBER, null,
            null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "bigDecimalNegative", false);
        if(schema.getSpecVersion().equals(SpecVersion.V30)){
            assertThat(schema.getMaximum()).isEqualTo(BigDecimal.ZERO);
            assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        }
        if(schema.getSpecVersion().equals(SpecVersion.V31)){
            assertThat(schema.getExclusiveMaximumValue()).isEqualTo(BigDecimal.ZERO);
        }

    }

    @Test
    public void testSchemaOptionalBigDecimalNegative() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalBigDecimalNegative", SCHEMA_TYPE_NUMBER,
            null, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalBigDecimalNegative", true);
        if(schema.getSpecVersion().equals(SpecVersion.V30)){
            assertThat(schema.getMaximum()).isEqualTo(BigDecimal.ZERO);
            assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        }
        if(schema.getSpecVersion().equals(SpecVersion.V31)){
            assertThat(schema.getExclusiveMaximumValue()).isEqualTo(BigDecimal.ZERO);
        }
        assertOptional(schema);
    }

    @Test
    public void testSchemaBigIntegerNegative() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "bigIntegerNegative", SCHEMA_TYPE_INTEGER, null,
            null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "bigIntegerNegative", false);
        if(schema.getSpecVersion().equals(SpecVersion.V30)){
            assertThat(schema.getMaximum()).isEqualTo(BigDecimal.ZERO);
            assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        }
        if(schema.getSpecVersion().equals(SpecVersion.V31)){
            assertThat(schema.getExclusiveMaximumValue()).isEqualTo(BigDecimal.ZERO);
        }
    }

    @Test
    public void testSchemaOptionalBigIntegerNegative() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalBigIntegerNegative",
            SCHEMA_TYPE_INTEGER, null, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalBigIntegerNegative", true);
        if(schema.getSpecVersion().equals(SpecVersion.V30)){
            assertThat(schema.getMaximum()).isEqualTo(BigDecimal.ZERO);
            assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        }
        if(schema.getSpecVersion().equals(SpecVersion.V31)){
            assertThat(schema.getExclusiveMaximumValue()).isEqualTo(BigDecimal.ZERO);
        }
        assertOptional(schema);
    }

    @Test
    public void testSchemaAFloatNegative() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "afloatNegative", SCHEMA_TYPE_NUMBER,
            FORMAT_TYPE_FLOAT, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "aFloatNegative", false);
        if(schema.getSpecVersion().equals(SpecVersion.V30)){
            assertThat(schema.getMaximum()).isEqualTo(BigDecimal.ZERO);
            assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        }
        if(schema.getSpecVersion().equals(SpecVersion.V31)){
            assertThat(schema.getExclusiveMaximumValue()).isEqualTo(BigDecimal.ZERO);
        }
        assertPropertyRequired(TestDTO2.class, "afloatNegative");
    }

    @Test
    public void testSchemaOptionalFloatNegative() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalFloatNegative", SCHEMA_TYPE_NUMBER,
            FORMAT_TYPE_FLOAT, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalFloatNegative", true);
        if(schema.getSpecVersion().equals(SpecVersion.V30)){
            assertThat(schema.getMaximum()).isEqualTo(BigDecimal.ZERO);
            assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        }
        if(schema.getSpecVersion().equals(SpecVersion.V31)){
            assertThat(schema.getExclusiveMaximumValue()).isEqualTo(BigDecimal.ZERO);
        }
        assertOptional(schema);

    }

    @Test
    public void testSchemaADoubleNegative() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "adoubleNegative", SCHEMA_TYPE_NUMBER,
            FORMAT_TYPE_DOUBLE, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "aDoubleNegative", false);
        if(schema.getSpecVersion().equals(SpecVersion.V30)){
            assertThat(schema.getMaximum()).isEqualTo(BigDecimal.ZERO);
            assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        }
        if(schema.getSpecVersion().equals(SpecVersion.V31)){
            assertThat(schema.getExclusiveMaximumValue()).isEqualTo(BigDecimal.ZERO);
        }
        assertPropertyRequired(TestDTO2.class, "adoubleNegative");
    }

    @Test
    public void testSchemaOptionalDoubleNegative() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalDoubleNegative", SCHEMA_TYPE_NUMBER,
            FORMAT_TYPE_DOUBLE, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalDoubleNegative", true);
        if(schema.getSpecVersion().equals(SpecVersion.V30)){
            assertThat(schema.getMaximum()).isEqualTo(BigDecimal.ZERO);
            assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        }
        if(schema.getSpecVersion().equals(SpecVersion.V31)){
            assertThat(schema.getExclusiveMaximumValue()).isEqualTo(BigDecimal.ZERO);
        }
        assertOptional(schema);
    }


    @Test
    public void testSchemaAnIntDigits() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "anIntDigits", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, "^[-]?[1-9]{1,1}[0-9]{0,2}|[0]$");
        assertPropertyRequired(TestDTO2.class, "anIntDigits");
    }

    @Test
    public void testSchemaOptionalIntegerDigits() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalIntegerDigits", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, "^[-]?[1-9]{1,1}[0-9]{0,2}|[0]$");
        assertOptional(schema);
    }

    @Test
    public void testSchemaAShortDigits() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "ashortDigits", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, "^[-]?[1-9]{1,1}[0-9]{0,2}|[0]$");
        assertPropertyRequired(TestDTO2.class, "ashortDigits");
    }

    @Test
    public void testSchemaOptionalShortDigits() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalShortDigits", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, "^[-]?[1-9]{1,1}[0-9]{0,2}|[0]$");
        assertOptional(schema);
    }

    @Test
    public void testSchemaAByteDigits() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "abyteDigits", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_BYTE, "^[-]?[1-9]{1,1}[0-9]{0,0}|[0]$");
        assertPropertyRequired(TestDTO2.class, "abyteDigits");
    }

    @Test
    public void testSchemaOptionalByteDigits() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalByteDigits", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_BYTE, "^[-]?[1-9]{1,1}[0-9]{0,0}|[0]$");
        assertOptional(schema);
    }

    @Test
    public void testSchemaALongDigits() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "alongDigits", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT64, "^[-]?[1-9]{1,1}[0-9]{0,2}|[0]$");
        assertPropertyRequired(TestDTO2.class, "alongDigits");
    }

    @Test
    public void testSchemaOptionalLongDigits() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalLongDigits", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT64, "^[-]?[1-9]{1,1}[0-9]{0,2}|[0]$");
        assertOptional(schema);
    }

    @Test
    public void testSchemaBigDecimalDigits() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "bigDecimalDigits", SCHEMA_TYPE_NUMBER, null,
            "^([-]?[1-9]{1,1}[0-9]{0,2}|[0]|[-][0]){1,1}(\\.[0-9]{1,2})?$");

    }

    @Test
    public void testSchemaOptionalBigDecimalDigits() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalBigDecimalDigits", SCHEMA_TYPE_NUMBER,
            null, "^([-]?[1-9]{1,1}[0-9]{0,2}|[0]|[-][0]){1,1}(\\.[0-9]{1,2})?$");
        assertThat(schema.getTypes().contains("null")).isTrue();
        assertOptional(schema);
    }

    @Test
    public void testSchemaBigIntegerDigits() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "bigIntegerDigits", SCHEMA_TYPE_INTEGER, null,
            "^[-]?[1-9]{1,1}[0-9]{0,2}|[0]$");
    }

    @Test
    public void testSchemaOptionalBigIntegerDigits() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalBigIntegerDigits", SCHEMA_TYPE_INTEGER,
            null, "^[-]?[1-9]{1,1}[0-9]{0,2}|[0]$");
        assertOptional(schema);
    }

    @Test
    public void testSchemaAFloatDigits() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "afloatDigits", SCHEMA_TYPE_NUMBER,
            FORMAT_TYPE_FLOAT, "^([-]?[1-9]{1,1}[0-9]{0,2}|[0]|[-][0]){1,1}(\\.[0-9]{1,2})?$");
        assertPropertyRequired(TestDTO2.class, "afloatDigits");
    }

    @Test
    public void testSchemaOptionalFloatDigits() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalFloatDigits", SCHEMA_TYPE_NUMBER,
            FORMAT_TYPE_FLOAT, "^([-]?[1-9]{1,1}[0-9]{0,2}|[0]|[-][0]){1,1}(\\.[0-9]{1,2})?$");
        assertOptional(schema);
    }

    @Test
    public void testSchemaADoubleDigits() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "adoubleDigits", SCHEMA_TYPE_NUMBER,
            FORMAT_TYPE_DOUBLE, "^([-]?[1-9]{1,1}[0-9]{0,2}|[0]|[-][0]){1,1}(\\.[0-9]{1,2})?$");
        assertPropertyRequired(TestDTO2.class, "adoubleDigits");
    }

    @Test
    public void testSchemaOptionalDoubleDigits() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalDoubleDigits", SCHEMA_TYPE_NUMBER,
            FORMAT_TYPE_DOUBLE, "^([-]?[1-9]{1,1}[0-9]{0,2}|[0]|[-][0]){1,1}(\\.[0-9]{1,2})?$");
        assertOptional(schema);
    }

    @Test
    public void testSchemaInstantPast() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "instantPast", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_DATE_TIME, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "instantPast", false);
        assertThat(schema.getDescription()).isEqualTo("The value must be in the past!");
    }

    @Test
    public void testSchemaOptionalInstantPast() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalInstantPast", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_DATE_TIME, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalInstantPast", true);
        assertThat(schema.getDescription()).isEqualTo("The value must be in the past!");
    }

    @Test
    public void testSchemaLocalDateFuture() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "localDateFuture", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_DATE, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "localDateFuture", false);
        assertThat(schema.getDescription()).isEqualTo("The value must be in the future!");
    }

    @Test
    public void testSchemaOptionalLocalDateFuture() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalLocalDateFuture", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_DATE, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalLocalDateFuture", true);
        assertThat(schema.getDescription()).isEqualTo("The value must be in the future!");
    }

    @Test
    public void testSchemaLocalDateTimePastOrPresent() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "localDateTimePastOrPresent", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_DATE_TIME, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "localDateTimePastOrPresent", false);
        assertThat(schema.getDescription()).isEqualTo(
            "The value must be in the past or correspond to the current time!");
    }

    @Test
    public void testSchemaOptionalLocalDateTimePastOrPresent() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalLocalDateTimePastOrPresent",
            SCHEMA_TYPE_STRING, FORMAT_TYPE_DATE_TIME, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalLocalDateTimePastOrPresent",
            true);
        assertThat(schema.getDescription()).isEqualTo(
            "The value must be in the past or correspond to the current time!");
    }

    @Test
    public void testSchemaLocalTimeNotNull() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "localTimeNotNull", null, null, null);
        assertThat(schema.get$ref()).isEqualTo("#/components/schemas/java.time.LocalTime");
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "localTimeNotNull", false);
        assertPropertyRequired(TestDTO2.class, "localTimeNotNull");
    }

    @Test
    public void testSchemaLocalTime() {
        var openAPI = openAPIService.getCachedOpenAPI(Locale.getDefault());
        var localTimeSchema = openAPI.getComponents().getSchemas().get("java.time.LocalTime");
        assertThat(localTimeSchema).isNotNull();
        if(localTimeSchema.getSpecVersion().equals(SpecVersion.V30)){
            assertThat(localTimeSchema.getType()).isEqualTo(SCHEMA_TYPE_STRING);
        }
        if(localTimeSchema.getSpecVersion().equals(SpecVersion.V31)){
            assertThat(localTimeSchema.getTypes().contains(SCHEMA_TYPE_STRING)).isTrue();
        }

        assertThat(localTimeSchema.getPattern()).isEqualTo(
            "^([0-1][0-9]|[2][0-4])[:]([0-5][0-9])[:]([0-5][0-9])(\\.[0-9]{1,6})?$");
    }

    @Test
    public void testSchemaOptionalLocalTimePast() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalLocalTimePast", null, null, null);
        var oneOfRefSchema = schema.getOneOf().stream().filter(s -> s.get$ref() != null).findFirst().orElse(null);
        assertThat(oneOfRefSchema).isNotNull();
        assertThat(oneOfRefSchema.get$ref()).isEqualTo("#/components/schemas/java.time.LocalTime");
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalLocalTimePast", true);
        assertThat(oneOfRefSchema.getDescription()).isEqualTo("The value must be in the past!");
        assertOptional(schema);
    }

    @Test
    public void testSchemaZonedDateTimeFutureOrPresent() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "zonedDateTimeFutureOrPresent",
            SCHEMA_TYPE_STRING, FORMAT_TYPE_DATE_TIME, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "zonedDateTimeFutureOrPresent", false);
        assertThat(schema.getDescription()).isEqualTo(
            "The value must be in the future or correspond to the current time!");
    }

    @Test
    public void testSchemaOptionalZonedDateTimeFutureOrPresent() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalZonedDateTimeFutureOrPresent",
            SCHEMA_TYPE_STRING, FORMAT_TYPE_DATE_TIME, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalZonedDateTimeFutureOrPresent",
            true);
        assertThat(schema.getDescription()).isEqualTo(
            "The value must be in the future or correspond to the current time!");
    }

    @Test
    public void testSchemaOptionalTestId() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalTestId",
            SCHEMA_TYPE_STRING, "uuid", null);
        assertOptional(schema);
    }

    @Test
    public void testSchemaOptionalVo() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalTestVo2",
            null, null, null);
        assertOptional(schema);
    }

    @Test
    public void testSchemaOptionalEnum() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalTestEnum",
            SCHEMA_TYPE_STRING, null, null);
        assertOptional(schema);
    }

    @Test
    public void testSchemaOptionalTestVo2() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalTestVo2",
            null, null, null);
        assertOptional(schema);
    }

    @Test
    public void testSchemaOffsetDateTimeNotNull() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "offsetDateTimeNotNull", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_DATE_TIME, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "offsetDateTimeNotNull", false);
        assertPropertyRequired(TestDTO2.class, "offsetDateTimeNotNull");
    }

    @Test
    public void testSchemaOptionalOffsetDateTimePast() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalOffsetDateTimePast", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_DATE_TIME, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalOffsetDateTimePast", true);
        assertThat(schema.getDescription()).isEqualTo("The value must be in the past!");
    }

    @Test
    public void testSchemaOffsetTime() {
        var openAPI = openAPIService.getCachedOpenAPI(Locale.getDefault());
        var localTimeSchema = openAPI.getComponents().getSchemas().get("java.time.OffsetTime");

        assertThat(localTimeSchema).isNotNull();
        if(localTimeSchema.getSpecVersion().equals(SpecVersion.V30)){
            assertThat(localTimeSchema.getType()).isEqualTo(SCHEMA_TYPE_STRING);
        }
        if(localTimeSchema.getSpecVersion().equals(SpecVersion.V31)){
            assertThat(localTimeSchema.getTypes().contains(SCHEMA_TYPE_STRING)).isTrue();
        }
        assertThat(localTimeSchema.getPattern()).isEqualTo(
            "^([0-1][0-9]|[2][0-4])[:]([0-5][0-9])[:]([0-5][0-9])(\\.[0-9]{1,6})?([+]|[-])[0-9]{2,2}[:][0-9]{2,2}$");
    }

    @Test
    public void testSchemaOffsetTimePast() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "offsetTimePast", null, null, null);
        assertThat(schema.get$ref()).isEqualTo("#/components/schemas/java.time.OffsetTime");
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "offsetTimePast", false);
        assertThat(schema.getDescription()).isEqualTo("The value must be in the past!");
    }

    @Test
    public void testSchemaOptionalOffsetTimePast() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalOffsetTimePast", null, null, null);
        var oneOfRefSchema = schema.getOneOf().stream().filter(s -> s.get$ref() != null).findFirst().orElse(null);
        assertThat(oneOfRefSchema).isNotNull();
        assertThat(oneOfRefSchema.get$ref()).isEqualTo("#/components/schemas/java.time.OffsetTime");
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalOffsetTimePast", true);
        assertThat(oneOfRefSchema.getDescription()).isEqualTo("The value must be in the past!");
        assertOptional(schema);
    }

    @Test
    public void testSchemaYearMonth() {
        var openAPI = openAPIService.getCachedOpenAPI(Locale.getDefault());
        var schema = openAPI.getComponents().getSchemas().get("java.time.YearMonth");
        assertThat(schema).isNotNull();
        if(schema.getSpecVersion().equals(SpecVersion.V30)){
            assertThat(schema.getType()).isEqualTo(SCHEMA_TYPE_STRING);
        }
        if(schema.getSpecVersion().equals(SpecVersion.V31)){
            assertThat(schema.getTypes().contains(SCHEMA_TYPE_STRING)).isTrue();
        }
        assertThat(schema.getPattern()).isEqualTo("^[0-9]{4,4}[-][0-1][0-9]$");
    }

    @Test
    public void testSchemaYearMonthPast() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "yearMonthPast", null, null, null);
        assertThat(schema.get$ref()).isEqualTo("#/components/schemas/java.time.YearMonth");
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "yearMonthPast", false);
        assertThat(schema.getDescription()).isEqualTo("The value must be in the past!");
    }

    @Test
    public void testSchemaOptionalYearMonthPast() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalYearMonthPast", null, null, null);
        var oneOfRefSchema = schema.getOneOf().stream().filter(s -> s.get$ref() != null).findFirst().orElse(null);
        assertThat(oneOfRefSchema).isNotNull();
        assertThat(oneOfRefSchema.get$ref()).isEqualTo("#/components/schemas/java.time.YearMonth");
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalYearMonthPast", true);
        assertThat(oneOfRefSchema.getDescription()).isEqualTo("The value must be in the past!");
        assertOptional(schema);
    }

    @Test
    public void testSchemaMonthDay() {
        var openAPI = openAPIService.getCachedOpenAPI(Locale.getDefault());
        var schema = openAPI.getComponents().getSchemas().get("java.time.MonthDay");
        assertThat(schema).isNotNull();
        if(schema.getSpecVersion().equals(SpecVersion.V30)){
            assertThat(schema.getType()).isEqualTo(SCHEMA_TYPE_STRING);
        }
        if(schema.getSpecVersion().equals(SpecVersion.V31)){
            assertThat(schema.getTypes().contains(SCHEMA_TYPE_STRING)).isTrue();
        }

        assertThat(schema.getPattern()).isEqualTo("^--[0-1][0-9]-[0-3][0-9]$");
    }

    @Test
    public void testSchemaMonthDayPast() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "monthDayPast", null, null, null);
        assertThat(schema.get$ref()).isEqualTo("#/components/schemas/java.time.MonthDay");
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "monthDayPast", false);
        assertThat(schema.getDescription()).isEqualTo("The value must be in the past!");
    }

    @Test
    public void testSchemaOptionalMonthDayPast() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalMonthDayPast", null, null, null);
        var oneOfRefSchema = schema.getOneOf().stream().filter(s -> s.get$ref() != null).findFirst().orElse(null);
        assertThat(oneOfRefSchema).isNotNull();
        assertThat(oneOfRefSchema.get$ref()).isEqualTo("#/components/schemas/java.time.MonthDay");
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalMonthDayPast", true);
        assertThat(oneOfRefSchema.getDescription()).isEqualTo("The value must be in the past!");
        assertOptional(schema);
    }

    @Test
    public void testSchemaYear() {
        var openAPI = openAPIService.getCachedOpenAPI(Locale.getDefault());
        var schema = openAPI.getComponents().getSchemas().get("java.time.Year");
        assertThat(schema).isNotNull();
        if(schema.getSpecVersion().equals(SpecVersion.V30)){
            assertThat(schema.getType()).isEqualTo(SCHEMA_TYPE_STRING);
        }
        if(schema.getSpecVersion().equals(SpecVersion.V31)){
            assertThat(schema.getTypes().contains(SCHEMA_TYPE_STRING)).isTrue();
        }

        assertThat(schema.getPattern()).isEqualTo("^([+]|[-])?[0-9]{4,4}$");
    }

    @Test
    public void testSchemaYearPast() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "yearPast", null, null, null);
        assertThat(schema.get$ref()).isEqualTo("#/components/schemas/java.time.Year");
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "yearPast", false);
        assertThat(schema.getDescription()).isEqualTo("The value must be in the past!");
    }

    @Test
    public void testSchemaOptionalYearPast() {
        Schema<?> schema = assertPropertyTypeAndGetSchema(TestDTO2.class, "optionalYearPast", null, null, null);
        var oneOfRefSchema = schema.getOneOf().stream().filter(s -> s.get$ref() != null).findFirst().orElse(null);
        assertThat(oneOfRefSchema).isNotNull();
        assertThat(oneOfRefSchema.get$ref()).isEqualTo("#/components/schemas/java.time.Year");
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO2.class, "optionalYearPast", true);
        assertThat(oneOfRefSchema.getDescription()).isEqualTo("The value must be in the past!");
        assertOptional(schema);
    }

    @Test
    public void testIdentitySchemata() {
        testIdentitySchema(TestId.class);
        testIdentitySchema(TestIdInterface.class);
        testIdentitySchema(KundennummerBv3.class);
        testIdentitySchema(BestellungIdBv3.class);
    }

    private void testIdentitySchema(Class identityClass) {
        var openAPI = openAPIService.getCachedOpenAPI(Locale.getDefault());
        var testSchema = openAPI.getComponents().getSchemas().get(identityClass.getName());
        assertThat(testSchema).isNotNull();
        assertThat(testSchema.getRequired().contains("value")).isTrue();
        assertThat(testSchema.getProperties()).isNotNull();
        assertThat(testSchema.getProperties().get("value")).isNotNull();
    }

    private Schema<?> assertPropertyTypeAndGetSchema(Class<?> containingClass, String propertyName,
                                                  String expectedPropertyType, String expectedFormat,
                                                  String expectedPattern) {
        var openAPI = openAPIService.getCachedOpenAPI(Locale.getDefault());
        var testSchema = openAPI.getComponents().getSchemas().get(containingClass.getName());
        var propertySchema = (Schema<?>) testSchema.getProperties().get(propertyName);
        var propertyType = "";
        if(propertySchema.getSpecVersion().equals(SpecVersion.V30)){
            propertyType = propertySchema.getType();
        }
        if(propertySchema.getSpecVersion().equals(SpecVersion.V31)) {
            if (propertySchema.getTypes() == null || propertySchema.getTypes().isEmpty()) {
                propertyType = null;
            } else {
                propertyType = (String) propertySchema.getTypes().stream().filter(t -> !t.equals("null")).findFirst().orElse(null);
            }
        }
        assertThat(propertyType).isEqualTo(expectedPropertyType);
        assertThat(propertySchema.getFormat()).isEqualTo(expectedFormat);
        assertThat(propertySchema.getPattern()).isEqualTo(expectedPattern);
        return propertySchema;
    }

    private ConstraintDescriptor<?> getAnnotationDescriptor(Class<?> containingClass, String propertyName,
                                                            boolean optional) {
        var desc = Validation.buildDefaultValidatorFactory().getValidator().getConstraintsForClass(containingClass);
        var prop = desc.getConstraintsForProperty(propertyName);
        if (optional) {
            var container = (ContainerElementTypeDescriptor) prop.getConstrainedContainerElementTypes().toArray()[0];
            var annotDesc = (ConstraintDescriptor<?>) container.getConstraintDescriptors().toArray()[0];
            assertThat(annotDesc).isNotNull();
            return annotDesc;
        } else {
            var annotDesc = (ConstraintDescriptor<?>) prop.getConstraintDescriptors().toArray()[0];
            assertThat(annotDesc).isNotNull();
            return annotDesc;
        }
    }

    private void assertPropertyRequired(Class<?> containingClass, String propertyName) {
        var openAPI = openAPIService.getCachedOpenAPI(Locale.getDefault());
        var testSchema = openAPI.getComponents().getSchemas().get(containingClass.getName());
        assertThat(testSchema.getRequired()).contains(propertyName);
    }

    private static void assertOptional(Schema<?> schema) {
        if(schema.getSpecVersion().equals(SpecVersion.V30)){
            assertThat(schema.getNullable()).isTrue();
        }
        if(schema.getSpecVersion().equals(SpecVersion.V31)){
            if(schema.getTypes() != null && !schema.getTypes().isEmpty()){
                assertThat(schema.getTypes().contains("null")).isTrue();
            }else{
                assertThat(schema.getOneOf()).isNotEmpty();
                assertThat(
                    schema.getOneOf()
                        .stream()
                        .filter(s -> s.getTypes() != null && s.getTypes().contains("null"))
                        .findFirst()
                ).isPresent();
            }


        }
    }

}



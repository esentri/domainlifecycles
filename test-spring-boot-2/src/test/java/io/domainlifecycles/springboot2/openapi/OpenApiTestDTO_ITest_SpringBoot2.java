package io.domainlifecycles.springboot2.openapi;

import io.swagger.v3.oas.models.media.Schema;
import lombok.extern.slf4j.Slf4j;
import io.domainlifecycles.assertion.DomainAssertionException;
import io.domainlifecycles.validation.BeanValidations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springdoc.core.OpenAPIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import tests.shared.openapi.TestId;
import tests.shared.openapi.TestIdInterface;
import tests.shared.openapi.javax.TestDTO;
import tests.shared.persistence.domain.bestellung.bv2.BestellungId;
import tests.shared.persistence.domain.bestellung.bv2.Kundennummer;

import javax.validation.Validation;
import javax.validation.metadata.ConstraintDescriptor;
import javax.validation.metadata.ContainerElementTypeDescriptor;
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
public class OpenApiTestDTO_ITest_SpringBoot2 {

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
                TestDTO t = TestDTO
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
        for (Field f : TestDTO.class.getDeclaredFields()) {
            if (!f.getName().equals("testId")
                && !f.getName().equals("optionalTestId")
                && !f.getName().equals("testIdInterface")) {
                assertThat(ex.getMessage()).contains(f.getName());
            }
        }
        assertThat(ex.getMessage().chars().filter(ch -> ch == '\n').count()).isEqualTo(
            TestDTO.class.getDeclaredFields().length - 3);
    }

    @Test
    public void testSchemaStringNotEmpty() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "stringNotEmpty", SCHEMA_TYPE_STRING, null, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "stringNotEmpty", false);
        assertThat(schema.getMinLength()).isEqualTo(1);
    }

    @Test
    public void testSchemaStringEmail() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "stringEmail", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_EMAIL, null);
    }

    @Test
    public void testSchemaOptionalStringEmail() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalStringEmail", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_EMAIL, null);
    }

    @Test
    public void testSchemaStringPattern() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "stringPattern", SCHEMA_TYPE_STRING, null,
            "[0-9]");
    }

    @Test
    public void testSchemaOptionalStringPattern() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalStringPattern", SCHEMA_TYPE_STRING, null,
            "[0-9]");
    }

    @Test
    public void testSchemaStringSized() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "stringSized", SCHEMA_TYPE_STRING, null, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "stringSized", false);
        assertThat(schema.getMaxLength()).isEqualTo(desc.getAttributes().get("max"));
        assertThat(schema.getMinLength()).isEqualTo(desc.getAttributes().get("min"));
    }

    @Test
    public void testSchemaOptionalStringSized() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalStringSized", SCHEMA_TYPE_STRING, null,
            null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalStringSized", true);
        assertThat(schema.getMaxLength()).isEqualTo(desc.getAttributes().get("max"));
        assertThat(schema.getMinLength()).isEqualTo(desc.getAttributes().get("min"));
    }

    @Test
    public void testSchemaStringListSized() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "stringListSized", SCHEMA_TYPE_ARRAY, null, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "stringListSized", false);
        assertThat(schema.getMinItems()).isEqualTo(desc.getAttributes().get("min"));
        assertThat(schema.getMaxItems()).isEqualTo(desc.getAttributes().get("max"));
    }

    @Test
    public void testSchemaStringArraySized() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "stringArraySized", SCHEMA_TYPE_ARRAY, null,
            null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "stringArraySized", false);
        assertThat(schema.getMinItems()).isEqualTo(desc.getAttributes().get("min"));
        assertThat(schema.getMaxItems()).isEqualTo(desc.getAttributes().get("max"));
    }

    @Test
    public void testSchemaStringListNotEmpty() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "stringListNotEmpty", SCHEMA_TYPE_ARRAY, null,
            null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "stringListNotEmpty", false);
        assertThat(schema.getMinItems()).isEqualTo(1);
    }

    @Test
    public void testSchemaStringArrayNotEmpty() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "stringArrayNotEmpty", SCHEMA_TYPE_ARRAY, null,
            null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "stringArrayNotEmpty", false);
        assertThat(schema.getMinItems()).isEqualTo(1);
    }

    @Test
    public void testSchemaAnIntMin() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "anIntMin", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "anIntMin", false);
        assertThat(schema.getMinimum().longValue()).isEqualTo(desc.getAttributes().get("value"));
        assertPropertyRequired(TestDTO.class, "anIntMin");
    }

    @Test
    public void testSchemaOptionalIntegerMin() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalIntegerMin", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalIntegerMin", true);
        assertThat(schema.getMinimum().longValue()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaAShortMin() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "ashortMin", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "aShortMin", false);
        assertThat(schema.getMinimum().longValue()).isEqualTo(desc.getAttributes().get("value"));
        assertPropertyRequired(TestDTO.class, "ashortMin");
    }

    @Test
    public void testSchemaOptionalShortMin() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalShortMin", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalShortMin", true);
        assertThat(schema.getMinimum().longValue()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaAByteMin() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "abyteMin", SCHEMA_TYPE_STRING, FORMAT_TYPE_BYTE,
            null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "aByteMin", false);
        assertThat(schema.getMinimum().longValue()).isEqualTo(desc.getAttributes().get("value"));
        assertPropertyRequired(TestDTO.class, "abyteMin");
    }

    @Test
    public void testSchemaOptionalByteMin() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalByteMin", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_BYTE, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalByteMin", true);
        assertThat(schema.getMinimum().longValue()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaALongMin() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "alongMin", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT64, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "aLongMin", false);
        assertThat(schema.getMinimum().longValue()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaOptionalLongMin() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalLongMin", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT64, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalLongMin", true);
        assertThat(schema.getMinimum().longValue()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaBigDecimalMin() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "bigDecimalMin", SCHEMA_TYPE_NUMBER, null, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "bigDecimalMin", false);
        assertThat(schema.getMinimum().longValue()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaOptionalBigDecimalMin() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalBigDecimalMin", SCHEMA_TYPE_NUMBER, null,
            null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalBigDecimalMin", true);
        assertThat(schema.getMinimum().longValue()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaBigIntegerMin() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "bigIntegerMin", SCHEMA_TYPE_INTEGER, null, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "bigIntegerMin", false);
        assertThat(schema.getMinimum().longValue()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaOptionalBigIntegerMin() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalBigIntegerMin", SCHEMA_TYPE_INTEGER,
            null, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalBigIntegerMin", true);
        assertThat(schema.getMinimum().longValue()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaAnIntMax() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "anIntMax", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "anIntMax", false);
        assertThat(schema.getMaximum().longValue()).isEqualTo(desc.getAttributes().get("value"));
        assertPropertyRequired(TestDTO.class, "anIntMax");
    }

    @Test
    public void testSchemaOptionalIntegerMax() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalIntegerMax", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalIntegerMax", true);
        assertThat(schema.getMaximum().longValue()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaAShortMax() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "ashortMax", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "aShortMax", false);
        assertThat(schema.getMaximum().longValue()).isEqualTo(desc.getAttributes().get("value"));
        assertPropertyRequired(TestDTO.class, "ashortMax");
    }

    @Test
    public void testSchemaOptionalShortMax() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalShortMax", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalShortMax", true);
        assertThat(schema.getMaximum().longValue()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaAByteMax() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "abyteMax", SCHEMA_TYPE_STRING, FORMAT_TYPE_BYTE,
            null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "aByteMax", false);
        assertThat(schema.getMaximum().longValue()).isEqualTo(desc.getAttributes().get("value"));
        assertPropertyRequired(TestDTO.class, "abyteMax");
    }

    @Test
    public void testSchemaOptionalByteMax() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalByteMax", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_BYTE, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalByteMax", true);
        assertThat(schema.getMaximum().longValue()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaALongMax() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "alongMax", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT64, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "aLongMax", false);
        assertThat(schema.getMaximum().longValue()).isEqualTo(desc.getAttributes().get("value"));
        assertPropertyRequired(TestDTO.class, "alongMax");
    }

    @Test
    public void testSchemaOptionalLongMax() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalLongMax", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT64, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalLongMax", true);
        assertThat(schema.getMaximum().longValue()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaBigDecimalMax() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "bigDecimalMax", SCHEMA_TYPE_NUMBER, null, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "bigDecimalMax", false);
        assertThat(schema.getMaximum().longValue()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaOptionalBigDecimalMax() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalBigDecimalMax", SCHEMA_TYPE_NUMBER, null,
            null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalBigDecimalMax", true);
        assertThat(schema.getMaximum().longValue()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaBigIntegerMax() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "bigIntegerMax", SCHEMA_TYPE_INTEGER, null, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "bigIntegerMax", false);
        assertThat(schema.getMaximum().longValue()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaOptionalBigIntegerMax() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalBigIntegerMax", SCHEMA_TYPE_INTEGER,
            null, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalBigIntegerMax", true);
        assertThat(schema.getMaximum().longValue()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaAnIntDecimalMin() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "anIntDecimalMin", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "anIntDecimalMin", false);
        assertThat(schema.getMinimum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertPropertyRequired(TestDTO.class, "anIntDecimalMin");
    }

    @Test
    public void testSchemaOptionalIntegerDecimalMin() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalIntegerDecimalMin", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalIntegerDecimalMin", true);
        assertThat(schema.getMinimum().toString()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaAShortDecimalMin() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "ashortDecimalMin", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "aShortDecimalMin", false);
        assertThat(schema.getMinimum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertPropertyRequired(TestDTO.class, "ashortDecimalMin");
    }

    @Test
    public void testSchemaOptionalShortDecimalMin() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalShortDecimalMin", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalShortDecimalMin", true);
        assertThat(schema.getMinimum().toString()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaAByteDecimalMin() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "abyteDecimalMin", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_BYTE, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "aByteDecimalMin", false);
        assertThat(schema.getMinimum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertPropertyRequired(TestDTO.class, "abyteDecimalMin");
    }

    @Test
    public void testSchemaOptionalByteDecimalMin() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalByteDecimalMin", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_BYTE, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalByteDecimalMin", true);
        assertThat(schema.getMinimum().toString()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaALongDecimalMin() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "alongDecimalMin", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT64, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "aLongDecimalMin", false);
        assertThat(schema.getMinimum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertPropertyRequired(TestDTO.class, "alongDecimalMin");
    }

    @Test
    public void testSchemaOptionalLongDecimalMin() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalLongDecimalMin", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT64, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalLongDecimalMin", true);
        assertThat(schema.getMinimum().toString()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaBigDecimalDecimalMin() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "bigDecimalDecimalMin", SCHEMA_TYPE_NUMBER, null,
            null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "bigDecimalDecimalMin", false);
        assertThat(schema.getMinimum().toString()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaOptionalBigDecimalDecimalMin() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalBigDecimalDecimalMin",
            SCHEMA_TYPE_NUMBER, null, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalBigDecimalDecimalMin", true);
        assertThat(schema.getMinimum().toString()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaBigIntegerDecimalMin() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "bigIntegerDecimalMin", SCHEMA_TYPE_INTEGER, null,
            null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "bigIntegerDecimalMin", false);
        assertThat(schema.getMinimum().toString()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaOptionalBigIntegerDecimalMin() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalBigIntegerDecimalMin",
            SCHEMA_TYPE_INTEGER, null, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalBigIntegerDecimalMin", true);
        assertThat(schema.getMinimum().toString()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaAFloatDecimalMin() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "afloatDecimalMin", SCHEMA_TYPE_NUMBER,
            FORMAT_TYPE_FLOAT, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "aFloatDecimalMin", false);
        assertThat(schema.getMinimum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertPropertyRequired(TestDTO.class, "afloatDecimalMin");
    }

    @Test
    public void testSchemaOptionalFloatDecimalMin() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalFloatDecimalMin", SCHEMA_TYPE_NUMBER,
            FORMAT_TYPE_FLOAT, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalFloatDecimalMin", true);
        assertThat(schema.getMinimum().toString()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaADoubleDecimalMin() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "adoubleDecimalMin", SCHEMA_TYPE_NUMBER,
            FORMAT_TYPE_DOUBLE, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "aDoubleDecimalMin", false);
        assertThat(schema.getMinimum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertPropertyRequired(TestDTO.class, "adoubleDecimalMin");
    }

    @Test
    public void testSchemaOptionalDoubleDecimalMin() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalDoubleDecimalMin", SCHEMA_TYPE_NUMBER,
            FORMAT_TYPE_DOUBLE, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalDoubleDecimalMin", true);
        assertThat(schema.getMinimum().toString()).isEqualTo(desc.getAttributes().get("value"));
    }

    @Test
    public void testSchemaAnIntDecimalMaxExclusive() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "anIntDecimalMaxExclusive", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "anIntDecimalMaxExclusive", false);
        assertThat(schema.getMaximum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        assertPropertyRequired(TestDTO.class, "anIntDecimalMaxExclusive");
    }

    @Test
    public void testSchemaOptionalIntegerDecimalMaxExclusive() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalIntegerDecimalMaxExclusive",
            SCHEMA_TYPE_INTEGER, FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalIntegerDecimalMaxExclusive",
            true);
        assertThat(schema.getMaximum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
    }

    @Test
    public void testSchemaAShortDecimalMaxExclusive() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "ashortDecimalMaxExclusive", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "aShortDecimalMaxExclusive", false);
        assertThat(schema.getMaximum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        assertPropertyRequired(TestDTO.class, "ashortDecimalMaxExclusive");
    }

    @Test
    public void testSchemaOptionalShortDecimalMaxExclusive() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalShortDecimalMaxExclusive",
            SCHEMA_TYPE_INTEGER, FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalShortDecimalMaxExclusive", true);
        assertThat(schema.getMaximum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
    }

    @Test
    public void testSchemaAByteDecimalMaxExclusive() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "abyteDecimalMaxExclusive", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_BYTE, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "aByteDecimalMaxExclusive", false);
        assertThat(schema.getMaximum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        assertPropertyRequired(TestDTO.class, "abyteDecimalMaxExclusive");
    }

    @Test
    public void testSchemaOptionalByteDecimalMaxExclusive() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalByteDecimalMaxExclusive",
            SCHEMA_TYPE_STRING, FORMAT_TYPE_BYTE, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalByteDecimalMaxExclusive", true);
        assertThat(schema.getMaximum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
    }

    @Test
    public void testSchemaALongDecimalMaxExclusive() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "alongDecimalMaxExclusive", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT64, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "aLongDecimalMaxExclusive", false);
        assertThat(schema.getMaximum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        assertPropertyRequired(TestDTO.class, "alongDecimalMaxExclusive");
    }

    @Test
    public void testSchemaOptionalLongDecimalMaxExclusive() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalLongDecimalMaxExclusive",
            SCHEMA_TYPE_INTEGER, FORMAT_TYPE_INT64, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalLongDecimalMaxExclusive", true);
        assertThat(schema.getMaximum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
    }

    @Test
    public void testSchemaBigDecimalDecimalMaxExclusive() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "bigDecimalDecimalMaxExclusive",
            SCHEMA_TYPE_NUMBER, null, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "bigDecimalDecimalMaxExclusive", false);
        assertThat(schema.getMaximum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
    }

    @Test
    public void testSchemaOptionalBigDecimalDecimalMaxExclusive() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalBigDecimalDecimalMaxExclusive",
            SCHEMA_TYPE_NUMBER, null, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalBigDecimalDecimalMaxExclusive",
            true);
        assertThat(schema.getMaximum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
    }

    @Test
    public void testSchemaBigIntegerDecimalMaxExclusive() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "bigIntegerDecimalMaxExclusive",
            SCHEMA_TYPE_INTEGER, null, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "bigIntegerDecimalMaxExclusive", false);
        assertThat(schema.getMaximum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
    }

    @Test
    public void testSchemaOptionalBigIntegerDecimalMaxExclusive() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalBigIntegerDecimalMaxExclusive",
            SCHEMA_TYPE_INTEGER, null, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalBigIntegerDecimalMaxExclusive",
            true);
        assertThat(schema.getMaximum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
    }

    @Test
    public void testSchemaAFloatDecimalMaxExclusive() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "afloatDecimalMaxExclusive", SCHEMA_TYPE_NUMBER,
            FORMAT_TYPE_FLOAT, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "aFloatDecimalMaxExclusive", false);
        assertThat(schema.getMaximum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        assertPropertyRequired(TestDTO.class, "afloatDecimalMaxExclusive");
    }

    @Test
    public void testSchemaOptionalFloatDecimalMaxExclusive() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalFloatDecimalMaxExclusive",
            SCHEMA_TYPE_NUMBER, FORMAT_TYPE_FLOAT, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalFloatDecimalMaxExclusive", true);
        assertThat(schema.getMaximum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
    }

    @Test
    public void testSchemaADoubleDecimalMaxExclusive() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "adoubleDecimalMaxExclusive", SCHEMA_TYPE_NUMBER,
            FORMAT_TYPE_DOUBLE, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "aDoubleDecimalMaxExclusive", false);
        assertThat(schema.getMaximum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        assertPropertyRequired(TestDTO.class, "adoubleDecimalMaxExclusive");
    }

    @Test
    public void testSchemaOptionalDoubleDecimalMaxExclusive() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalDoubleDecimalMaxExclusive",
            SCHEMA_TYPE_NUMBER, FORMAT_TYPE_DOUBLE, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalDoubleDecimalMaxExclusive",
            true);
        assertThat(schema.getMaximum().toString()).isEqualTo(desc.getAttributes().get("value"));
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
    }

    @Test
    public void testSchemaAnIntNegative() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "anIntNegative", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "anIntNegative", false);
        assertThat(schema.getMaximum()).isEqualTo(BigDecimal.ZERO);
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        assertPropertyRequired(TestDTO.class, "anIntNegative");
    }

    @Test
    public void testSchemaOptionalIntegerNegative() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalIntegerNegative", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalIntegerNegative", true);
        assertThat(schema.getMaximum()).isEqualTo(BigDecimal.ZERO);
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
    }

    @Test
    public void testSchemaAShortNegative() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "ashortNegative", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "aShortNegative", false);
        assertThat(schema.getMaximum()).isEqualTo(BigDecimal.ZERO);
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        assertPropertyRequired(TestDTO.class, "ashortNegative");
    }

    @Test
    public void testSchemaOptionalShortNegative() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalShortNegative", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalShortNegative", true);
        assertThat(schema.getMaximum()).isEqualTo(BigDecimal.ZERO);
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
    }

    @Test
    public void testSchemaAByteNegative() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "abyteNegative", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_BYTE, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "aByteNegative", false);
        assertThat(schema.getMaximum()).isEqualTo(BigDecimal.ZERO);
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        assertPropertyRequired(TestDTO.class, "abyteNegative");
    }

    @Test
    public void testSchemaOptionalByteNegative() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalByteNegative", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_BYTE, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalByteNegative", true);
        assertThat(schema.getMaximum()).isEqualTo(BigDecimal.ZERO);
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
    }

    @Test
    public void testSchemaALongNegative() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "alongNegative", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT64, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "aLongNegative", false);
        assertThat(schema.getMaximum()).isEqualTo(BigDecimal.ZERO);
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        assertPropertyRequired(TestDTO.class, "alongNegative");
    }

    @Test
    public void testSchemaOptionalLongNegative() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalLongNegative", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT64, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalLongNegative", true);
        assertThat(schema.getMaximum()).isEqualTo(BigDecimal.ZERO);
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
    }

    @Test
    public void testSchemaBigDecimalNegative() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "bigDecimalNegative", SCHEMA_TYPE_NUMBER, null,
            null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "bigDecimalNegative", false);
        assertThat(schema.getMaximum()).isEqualTo(BigDecimal.ZERO);
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
    }

    @Test
    public void testSchemaOptionalBigDecimalNegative() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalBigDecimalNegative", SCHEMA_TYPE_NUMBER,
            null, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalBigDecimalNegative", true);
        assertThat(schema.getMaximum()).isEqualTo(BigDecimal.ZERO);
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
    }

    @Test
    public void testSchemaBigIntegerNegative() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "bigIntegerNegative", SCHEMA_TYPE_INTEGER, null,
            null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "bigIntegerNegative", false);
        assertThat(schema.getMaximum()).isEqualTo(BigDecimal.ZERO);
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
    }

    @Test
    public void testSchemaOptionalBigIntegerNegative() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalBigIntegerNegative", SCHEMA_TYPE_INTEGER,
            null, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalBigIntegerNegative", true);
        assertThat(schema.getMaximum()).isEqualTo(BigDecimal.ZERO);
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
    }

    @Test
    public void testSchemaAFloatNegative() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "afloatNegative", SCHEMA_TYPE_NUMBER,
            FORMAT_TYPE_FLOAT, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "aFloatNegative", false);
        assertThat(schema.getMaximum()).isEqualTo(BigDecimal.ZERO);
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        assertPropertyRequired(TestDTO.class, "afloatNegative");
    }

    @Test
    public void testSchemaOptionalFloatNegative() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalFloatNegative", SCHEMA_TYPE_NUMBER,
            FORMAT_TYPE_FLOAT, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalFloatNegative", true);
        assertThat(schema.getMaximum()).isEqualTo(BigDecimal.ZERO);
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
    }

    @Test
    public void testSchemaADoubleNegative() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "adoubleNegative", SCHEMA_TYPE_NUMBER,
            FORMAT_TYPE_DOUBLE, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "aDoubleNegative", false);
        assertThat(schema.getMaximum()).isEqualTo(BigDecimal.ZERO);
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
        assertPropertyRequired(TestDTO.class, "adoubleNegative");
    }

    @Test
    public void testSchemaOptionalDoubleNegative() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalDoubleNegative", SCHEMA_TYPE_NUMBER,
            FORMAT_TYPE_DOUBLE, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalDoubleNegative", true);
        assertThat(schema.getMaximum()).isEqualTo(BigDecimal.ZERO);
        assertThat(schema.getExclusiveMaximum()).isEqualTo(true);
    }


    @Test
    public void testSchemaAnIntDigits() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "anIntDigits", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, "^[-]?[1-9]{1,1}[0-9]{0,2}|[0]$");
        assertPropertyRequired(TestDTO.class, "anIntDigits");
    }

    @Test
    public void testSchemaOptionalIntegerDigits() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalIntegerDigits", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, "^[-]?[1-9]{1,1}[0-9]{0,2}|[0]$");
    }

    @Test
    public void testSchemaAShortDigits() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "ashortDigits", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, "^[-]?[1-9]{1,1}[0-9]{0,2}|[0]$");
        assertPropertyRequired(TestDTO.class, "ashortDigits");
    }

    @Test
    public void testSchemaOptionalShortDigits() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalShortDigits", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT32, "^[-]?[1-9]{1,1}[0-9]{0,2}|[0]$");
    }

    @Test
    public void testSchemaAByteDigits() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "abyteDigits", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_BYTE, "^[-]?[1-9]{1,1}[0-9]{0,0}|[0]$");
        assertPropertyRequired(TestDTO.class, "abyteDigits");
    }

    @Test
    public void testSchemaOptionalByteDigits() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalByteDigits", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_BYTE, "^[-]?[1-9]{1,1}[0-9]{0,0}|[0]$");
    }

    @Test
    public void testSchemaALongDigits() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "alongDigits", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT64, "^[-]?[1-9]{1,1}[0-9]{0,2}|[0]$");
        assertPropertyRequired(TestDTO.class, "alongDigits");
    }

    @Test
    public void testSchemaOptionalLongDigits() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalLongDigits", SCHEMA_TYPE_INTEGER,
            FORMAT_TYPE_INT64, "^[-]?[1-9]{1,1}[0-9]{0,2}|[0]$");
    }

    @Test
    public void testSchemaBigDecimalDigits() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "bigDecimalDigits", SCHEMA_TYPE_NUMBER, null,
            "^([-]?[1-9]{1,1}[0-9]{0,2}|[0]|[-][0]){1,1}(\\.[0-9]{1,2})?$");

    }

    @Test
    public void testSchemaOptionalBigDecimalDigits() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalBigDecimalDigits", SCHEMA_TYPE_NUMBER,
            null, "^([-]?[1-9]{1,1}[0-9]{0,2}|[0]|[-][0]){1,1}(\\.[0-9]{1,2})?$");
    }

    @Test
    public void testSchemaBigIntegerDigits() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "bigIntegerDigits", SCHEMA_TYPE_INTEGER, null,
            "^[-]?[1-9]{1,1}[0-9]{0,2}|[0]$");
    }

    @Test
    public void testSchemaOptionalBigIntegerDigits() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalBigIntegerDigits", SCHEMA_TYPE_INTEGER,
            null, "^[-]?[1-9]{1,1}[0-9]{0,2}|[0]$");
    }

    @Test
    public void testSchemaAFloatDigits() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "afloatDigits", SCHEMA_TYPE_NUMBER,
            FORMAT_TYPE_FLOAT, "^([-]?[1-9]{1,1}[0-9]{0,2}|[0]|[-][0]){1,1}(\\.[0-9]{1,2})?$");
        assertPropertyRequired(TestDTO.class, "afloatDigits");
    }

    @Test
    public void testSchemaOptionalFloatDigits() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalFloatDigits", SCHEMA_TYPE_NUMBER,
            FORMAT_TYPE_FLOAT, "^([-]?[1-9]{1,1}[0-9]{0,2}|[0]|[-][0]){1,1}(\\.[0-9]{1,2})?$");
    }

    @Test
    public void testSchemaADoubleDigits() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "adoubleDigits", SCHEMA_TYPE_NUMBER,
            FORMAT_TYPE_DOUBLE, "^([-]?[1-9]{1,1}[0-9]{0,2}|[0]|[-][0]){1,1}(\\.[0-9]{1,2})?$");
        assertPropertyRequired(TestDTO.class, "adoubleDigits");
    }

    @Test
    public void testSchemaOptionalDoubleDigits() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalDoubleDigits", SCHEMA_TYPE_NUMBER,
            FORMAT_TYPE_DOUBLE, "^([-]?[1-9]{1,1}[0-9]{0,2}|[0]|[-][0]){1,1}(\\.[0-9]{1,2})?$");
    }

    @Test
    public void testSchemaInstantPast() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "instantPast", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_DATE_TIME, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "instantPast", false);
        assertThat(schema.getDescription()).isEqualTo("The value must be in the past!");
    }

    @Test
    public void testSchemaOptionalInstantPast() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalInstantPast", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_DATE_TIME, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalInstantPast", true);
        assertThat(schema.getDescription()).isEqualTo("The value must be in the past!");
    }

    @Test
    public void testSchemaLocalDateFuture() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "localDateFuture", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_DATE, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "localDateFuture", false);
        assertThat(schema.getDescription()).isEqualTo("The value must be in the future!");
    }

    @Test
    public void testSchemaOptionalLocalDateFuture() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalLocalDateFuture", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_DATE, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalLocalDateFuture", true);
        assertThat(schema.getDescription()).isEqualTo("The value must be in the future!");
    }

    @Test
    public void testSchemaLocalDateTimePastOrPresent() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "localDateTimePastOrPresent", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_DATE_TIME, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "localDateTimePastOrPresent", false);
        assertThat(schema.getDescription()).isEqualTo(
            "The value must be in the past or correspond to the current time!");
    }

    @Test
    public void testSchemaOptionalLocalDateTimePastOrPresent() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalLocalDateTimePastOrPresent",
            SCHEMA_TYPE_STRING, FORMAT_TYPE_DATE_TIME, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalLocalDateTimePastOrPresent",
            true);
        assertThat(schema.getDescription()).isEqualTo(
            "The value must be in the past or correspond to the current time!");
    }

    @Test
    public void testSchemaLocalTimeNotNull() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "localTimeNotNull", null, null, null);
        assertThat(schema.get$ref()).isEqualTo("#/components/schemas/java.time.LocalTime");
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "localTimeNotNull", false);
        assertPropertyRequired(TestDTO.class, "localTimeNotNull");
    }

    @Test
    public void testSchemaLocalTime() {
        var openAPI = openAPIService.getCachedOpenAPI(Locale.getDefault());
        var localTimeSchema = openAPI.getComponents().getSchemas().get("java.time.LocalTime");
        assertThat(localTimeSchema).isNotNull();
        assertThat(localTimeSchema.getType()).isEqualTo(SCHEMA_TYPE_STRING);
        assertThat(localTimeSchema.getPattern()).isEqualTo(
            "^([0-1][0-9]|[2][0-4])[:]([0-5][0-9])[:]([0-5][0-9])(\\.[0-9]{1,6})?$");
    }

    @Test
    public void testSchemaOptionalLocalTimePast() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalLocalTimePast", null, null, null);
        assertThat(schema.get$ref()).isEqualTo("#/components/schemas/java.time.LocalTime");
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalLocalTimePast", true);
        assertThat(schema.getDescription()).isEqualTo("The value must be in the past!");
    }

    @Test
    public void testSchemaZonedDateTimeFutureOrPresent() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "zonedDateTimeFutureOrPresent",
            SCHEMA_TYPE_STRING, FORMAT_TYPE_DATE_TIME, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "zonedDateTimeFutureOrPresent", false);
        assertThat(schema.getDescription()).isEqualTo(
            "The value must be in the future or correspond to the current time!");
    }

    @Test
    public void testSchemaOptionalZonedDateTimeFutureOrPresent() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalZonedDateTimeFutureOrPresent",
            SCHEMA_TYPE_STRING, FORMAT_TYPE_DATE_TIME, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalZonedDateTimeFutureOrPresent",
            true);
        assertThat(schema.getDescription()).isEqualTo(
            "The value must be in the future or correspond to the current time!");
    }

    @Test
    public void testSchemaOffsetDateTimeNotNull() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "offsetDateTimeNotNull", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_DATE_TIME, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "offsetDateTimeNotNull", false);
        assertPropertyRequired(TestDTO.class, "offsetDateTimeNotNull");
    }

    @Test
    public void testSchemaOptionalOffsetDateTimePast() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalOffsetDateTimePast", SCHEMA_TYPE_STRING,
            FORMAT_TYPE_DATE_TIME, null);
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalOffsetDateTimePast", true);
        assertThat(schema.getDescription()).isEqualTo("The value must be in the past!");
    }

    @Test
    public void testSchemaOffsetTime() {
        var openAPI = openAPIService.getCachedOpenAPI(Locale.getDefault());
        var localTimeSchema = openAPI.getComponents().getSchemas().get("java.time.OffsetTime");
        assertThat(localTimeSchema).isNotNull();
        assertThat(localTimeSchema.getType()).isEqualTo(SCHEMA_TYPE_STRING);
        assertThat(localTimeSchema.getPattern()).isEqualTo(
            "^([0-1][0-9]|[2][0-4])[:]([0-5][0-9])[:]([0-5][0-9])(\\.[0-9]{1,6})?([+]|[-])[0-9]{2,2}[:][0-9]{2,2}$");
    }

    @Test
    public void testSchemaOffsetTimePast() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "offsetTimePast", null, null, null);
        assertThat(schema.get$ref()).isEqualTo("#/components/schemas/java.time.OffsetTime");
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "offsetTimePast", false);
        assertThat(schema.getDescription()).isEqualTo("The value must be in the past!");
    }

    @Test
    public void testSchemaOptionalOffsetTimePast() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalOffsetTimePast", null, null, null);
        assertThat(schema.get$ref()).isEqualTo("#/components/schemas/java.time.OffsetTime");
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalOffsetTimePast", true);
        assertThat(schema.getDescription()).isEqualTo("The value must be in the past!");
    }

    @Test
    public void testSchemaYearMonth() {
        var openAPI = openAPIService.getCachedOpenAPI(Locale.getDefault());
        var schema = openAPI.getComponents().getSchemas().get("java.time.YearMonth");
        assertThat(schema).isNotNull();
        assertThat(schema.getType()).isEqualTo(SCHEMA_TYPE_STRING);
        assertThat(schema.getPattern()).isEqualTo("^[0-9]{4,4}[-][0-1][0-9]$");
    }

    @Test
    public void testSchemaYearMonthPast() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "yearMonthPast", null, null, null);
        assertThat(schema.get$ref()).isEqualTo("#/components/schemas/java.time.YearMonth");
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "yearMonthPast", false);
        assertThat(schema.getDescription()).isEqualTo("The value must be in the past!");
    }

    @Test
    public void testSchemaOptionalYearMonthPast() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalYearMonthPast", null, null, null);
        assertThat(schema.get$ref()).isEqualTo("#/components/schemas/java.time.YearMonth");
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalYearMonthPast", true);
        assertThat(schema.getDescription()).isEqualTo("The value must be in the past!");
    }

    @Test
    public void testSchemaMonthDay() {
        var openAPI = openAPIService.getCachedOpenAPI(Locale.getDefault());
        var schema = openAPI.getComponents().getSchemas().get("java.time.MonthDay");
        assertThat(schema).isNotNull();
        assertThat(schema.getType()).isEqualTo(SCHEMA_TYPE_STRING);
        assertThat(schema.getPattern()).isEqualTo("^--[0-1][0-9]-[0-3][0-9]$");
    }

    @Test
    public void testSchemaMonthDayPast() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "monthDayPast", null, null, null);
        assertThat(schema.get$ref()).isEqualTo("#/components/schemas/java.time.MonthDay");
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "monthDayPast", false);
        assertThat(schema.getDescription()).isEqualTo("The value must be in the past!");
    }

    @Test
    public void testSchemaOptionalMonthDayPast() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalMonthDayPast", null, null, null);
        assertThat(schema.get$ref()).isEqualTo("#/components/schemas/java.time.MonthDay");
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalMonthDayPast", true);
        assertThat(schema.getDescription()).isEqualTo("The value must be in the past!");
    }

    @Test
    public void testSchemaYear() {
        var openAPI = openAPIService.getCachedOpenAPI(Locale.getDefault());
        var schema = openAPI.getComponents().getSchemas().get("java.time.Year");
        assertThat(schema).isNotNull();
        assertThat(schema.getType()).isEqualTo(SCHEMA_TYPE_STRING);
        assertThat(schema.getPattern()).isEqualTo("^([+]|[-])?[0-9]{4,4}$");
    }

    @Test
    public void testSchemaYearPast() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "yearPast", null, null, null);
        assertThat(schema.get$ref()).isEqualTo("#/components/schemas/java.time.Year");
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "yearPast", false);
        assertThat(schema.getDescription()).isEqualTo("The value must be in the past!");
    }

    @Test
    public void testSchemaOptionalYearPast() {
        Schema schema = assertPropertyTypeAndGetSchema(TestDTO.class, "optionalYearPast", null, null, null);
        assertThat(schema.get$ref()).isEqualTo("#/components/schemas/java.time.Year");
        ConstraintDescriptor<?> desc = getAnnotationDescriptor(TestDTO.class, "optionalYearPast", true);
        assertThat(schema.getDescription()).isEqualTo("The value must be in the past!");
    }

    @Test
    public void testIdentitySchemata() {
        testIdentitySchema(TestId.class);
        testIdentitySchema(TestIdInterface.class);
        testIdentitySchema(Kundennummer.class);
        testIdentitySchema(BestellungId.class);
    }

    private void testIdentitySchema(Class identityClass) {
        var openAPI = openAPIService.getCachedOpenAPI(Locale.getDefault());
        var testSchema = openAPI.getComponents().getSchemas().get(identityClass.getName());
        assertThat(testSchema).isNotNull();
        assertThat(testSchema.getRequired()).contains("value");
        assertThat(testSchema.getProperties()).isNotNull();
        assertThat(testSchema.getProperties().get("value")).isNotNull();
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

    private ConstraintDescriptor<?> getAnnotationDescriptor(Class containingClass, String propertyName,
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

    private void assertPropertyRequired(Class containingClass, String propertyName) {
        var openAPI = openAPIService.getCachedOpenAPI(Locale.getDefault());
        var testSchema = openAPI.getComponents().getSchemas().get(containingClass.getName());
        assertThat(testSchema.getRequired()).contains(propertyName);
    }

}



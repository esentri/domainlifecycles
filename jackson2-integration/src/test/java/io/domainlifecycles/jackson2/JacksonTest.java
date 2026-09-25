package io.domainlifecycles.jackson2;

import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import io.domainlifecycles.builder.innerclass.InnerClassDomainObjectBuilderProvider;
import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.jackson2.module.DlcJacksonModule;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.persistence.provider.EntityIdentityProvider;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import tests.shared.TestDataGenerator;
import tests.shared.complete.ecommerce.order.ArticleIdBv3;
import tests.shared.complete.ecommerce.order.OrderCommentBv3;
import tests.shared.complete.ecommerce.order.OrderCommentIdBv3;
import tests.shared.complete.ecommerce.order.OrderItemBv3;
import tests.shared.complete.ecommerce.order.OrderItemIdBv3;
import tests.shared.complete.ecommerce.order.OrderStatusBv3;
import tests.shared.complete.ecommerce.order.OrderStatusIdBv3;
import tests.shared.complete.ecommerce.order.OrderBv3;
import tests.shared.complete.ecommerce.order.OrderIdBv3;
import tests.shared.complete.ecommerce.order.DeliveryAddressBv3;
import tests.shared.complete.ecommerce.order.DeliveryAddressIdBv3;
import tests.shared.complete.ecommerce.order.PriceBv3;
import tests.shared.complete.ecommerce.order.CurrencyEnumBv3;
import tests.shared.jackson.TypeTestValueObject;
import tests.shared.persistence.domain.complex.TestRoot;
import tests.shared.persistence.domain.complex.TestRootId;
import tests.shared.persistence.domain.optional.OptionalAggregate;
import tests.shared.persistence.domain.simple.TestRootSimple;
import tests.shared.persistence.domain.simple.TestRootSimpleId;
import tests.shared.persistence.domain.simpleUuid.TestRootSimpleUuid;
import tests.shared.persistence.domain.simpleUuid.TestRootSimpleUuidId;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedComplexVo;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedSimpleVo;
import tests.shared.persistence.domain.valueobjects.ComplexVo;
import tests.shared.persistence.domain.valueobjects.SimpleVo;
import tests.shared.persistence.domain.valueobjects.SimpleVoOneToMany2;
import tests.shared.persistence.domain.valueobjects.SimpleVoOneToMany3;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.IOException;
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
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class JacksonTest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(JacksonTest.class);
    private final ObjectMapper objectMapper;

    public JacksonTest() {
        Locale.setDefault(Locale.GERMAN);
        Domain.initialize(new ReflectiveDomainMirrorFactory("tests"));

        var entityIdentityProvider = new EntityIdentityProvider() {

            @Override
            public Identity<?> provideFor(String entityTypeName) {
                if (entityTypeName.equals(TestRootSimpleUuid.class.getName())) {
                    return new TestRootSimpleUuidId(UUID.randomUUID());
                } else if (entityTypeName.equals(TestRootSimple.class.getName())) {
                    return new TestRootSimpleId(1L);
                } else if (entityTypeName.equals(OrderBv3.class.getName())) {
                    return new OrderIdBv3(1L);
                } else if (entityTypeName.equals(OrderItemBv3.class.getName())) {
                    return new OrderItemIdBv3(1L);
                } else if (entityTypeName.equals(DeliveryAddressBv3.class.getName())) {
                    return new DeliveryAddressIdBv3(1L);
                } else if (entityTypeName.equals(OrderCommentBv3.class.getName())) {
                    return new OrderCommentIdBv3(1L);
                } else if (entityTypeName.equals(OrderStatusBv3.class.getName())) {
                    return new OrderStatusIdBv3(1L);
                }
                return null;
            }
        };

        this.objectMapper = JsonMapper.builder()
            .addModule(new DlcJacksonModule(
                new InnerClassDomainObjectBuilderProvider(),
                entityIdentityProvider))
            .addModule(new Jdk8Module())
            .addModule(new ParameterNamesModule())
            .addModule(new JavaTimeModule())
            .build();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testJacksonMappingTypeTest() throws Exception{
        TypeTestValueObject vo = new TypeTestValueObject();
        vo.setABoolean(true);
        vo.setAByte((byte) 5);
        vo.setAChar('a');
        vo.setADouble(5);
        vo.setAFloat(5);
        vo.setALong(5L);
        vo.setAnInt(5);
        vo.setAShort((short) 5);
        vo.setBigDecimal(BigDecimal.TEN);
        vo.setBigInteger(BigInteger.TEN);
        vo.setLocalDateTime(LocalDateTime.now());
        vo.setLocalDate(LocalDate.now());
        vo.setCalendar(Calendar.getInstance());
        vo.setDate(new Date());
        vo.setInstant(Instant.now());
        vo.setOffsetDateTime(OffsetDateTime.now());
        vo.setLocalTime(LocalTime.now());
        vo.setString("str");
        vo.setUuid(UUID.randomUUID());
        vo.setMonthDay(MonthDay.now());
        vo.setYear(Year.now());
        vo.setYearMonth(YearMonth.now());
        vo.setOffsetTime(OffsetTime.now());
        vo.setZonedDateTime(ZonedDateTime.now());
        vo.setOptionalBigDecimal(Optional.of(BigDecimal.ONE));
        vo.setOptionalBigInteger(Optional.of(BigInteger.ONE));
        vo.setOptionalByte(Optional.of((byte) 4));
        vo.setOptionalDouble(Optional.of(5.0));
        vo.setOptionalFloat(Optional.of(5F));
        vo.setOptionalInstant(Optional.of(Instant.now()));
        vo.setOptionalInteger(Optional.of(4));
        vo.setOptionalShort(Optional.of((short) 4));
        vo.setOptionalLocalDate(Optional.of(LocalDate.now()));
        vo.setZonedDateTime(ZonedDateTime.now());
        vo.setOptionalLocalDateTime(Optional.of(LocalDateTime.now()));
        vo.setOptionalString(Optional.of("test"));
        vo.setOptionalLong(Optional.of(8L));
        vo.setOptionalLocalTime(Optional.of(LocalTime.now()));
        vo.setOptionalMonthDay(Optional.of(MonthDay.now()));
        vo.setOptionalYear(Optional.of(Year.now()));
        vo.setOptionalYearMonth(Optional.of(YearMonth.now()));
        vo.setOptionalZonedDateTime(Optional.of(ZonedDateTime.now()));
        vo.setOptionalOffsetDateTime(Optional.of(OffsetDateTime.now()));
        vo.setOptionalOffsetTime(Optional.of(OffsetTime.now()));

        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(vo);
        log.info("JSON = " + json);

        TypeTestValueObject vo2 = objectMapper.readValue(json, TypeTestValueObject.class);
        log.info("Read = " + vo2);
        SoftAssertions softly = new SoftAssertions();
        for (Field f : TypeTestValueObject.class.getDeclaredFields()) {
            if (f.getType().isAssignableFrom(ZonedDateTime.class)) {
                softly.assertThat(((ZonedDateTime) f.get(vo2)).toInstant().truncatedTo(ChronoUnit.MILLIS)).isEqualTo(
                    ((ZonedDateTime) f.get(vo)).toInstant().truncatedTo(ChronoUnit.MILLIS));
            } else if (f.getType().isAssignableFrom(Calendar.class)) {
                softly.assertThat(((Calendar) f.get(vo2)).toInstant().truncatedTo(ChronoUnit.MILLIS)).isEqualTo(
                    ((Calendar) f.get(vo)).toInstant().truncatedTo(ChronoUnit.MILLIS));
            } else if (f.getType().isAssignableFrom(OffsetDateTime.class)) {
                softly.assertThat(((OffsetDateTime) f.get(vo2)).toInstant().truncatedTo(ChronoUnit.MILLIS)).isEqualTo(
                    ((OffsetDateTime) f.get(vo)).toInstant().truncatedTo(ChronoUnit.MILLIS));
            } else if (f.getType().isAssignableFrom(OffsetTime.class)) {
                softly.assertThat(((OffsetTime) f.get(vo2)).atDate(LocalDate.now()).toInstant().truncatedTo(
                    ChronoUnit.MILLIS)).isEqualTo(
                    ((OffsetTime) f.get(vo)).atDate(LocalDate.now()).toInstant().truncatedTo(ChronoUnit.MILLIS));
            } else if (f.getType().isAssignableFrom(LocalTime.class)) {
                softly.assertThat(
                    ((LocalTime) f.get(vo2)).atDate(LocalDate.now()).toInstant(ZoneOffset.UTC).truncatedTo(
                        ChronoUnit.MILLIS)).isEqualTo(
                    ((LocalTime) f.get(vo)).atDate(LocalDate.now()).toInstant(ZoneOffset.UTC).truncatedTo(
                        ChronoUnit.MILLIS));
            } else if (f.getType().isAssignableFrom(LocalDateTime.class)) {
                softly.assertThat(((LocalDateTime) f.get(vo2)).atOffset(ZoneOffset.UTC).toInstant().truncatedTo(
                    ChronoUnit.MILLIS)).isEqualTo(
                    ((LocalDateTime) f.get(vo)).atOffset(ZoneOffset.UTC).toInstant().truncatedTo(ChronoUnit.MILLIS));
            } else if (f.getType().isAssignableFrom(Instant.class)) {
                softly.assertThat(((Instant) f.get(vo2)).truncatedTo(ChronoUnit.MILLIS)).isEqualTo(
                    ((Instant) f.get(vo)).truncatedTo(ChronoUnit.MILLIS));
            } else if (f.getType().isAssignableFrom(Optional.class) && ((Optional) f.get(
                vo)).get().getClass().isAssignableFrom(ZonedDateTime.class)) {
                var optVo = (Optional<ZonedDateTime>) f.get(vo);
                var optVo2 = (Optional<ZonedDateTime>) f.get(vo2);
                softly.assertThat(optVo2.get().toInstant().truncatedTo(ChronoUnit.MILLIS)).isEqualTo(
                    optVo.get().toInstant().truncatedTo(ChronoUnit.MILLIS));
            } else if (f.getType().isAssignableFrom(Optional.class) && ((Optional) f.get(
                vo)).get().getClass().isAssignableFrom(OffsetDateTime.class)) {
                var optVo = (Optional<OffsetDateTime>) f.get(vo);
                var optVo2 = (Optional<OffsetDateTime>) f.get(vo2);
                softly.assertThat(optVo2.get().toInstant().truncatedTo(ChronoUnit.MILLIS)).isEqualTo(
                    optVo.get().toInstant().truncatedTo(ChronoUnit.MILLIS));
            } else if (f.getType().isAssignableFrom(Optional.class) && ((Optional) f.get(
                vo)).get().getClass().isAssignableFrom(LocalDateTime.class)) {
                var optVo = (Optional<LocalDateTime>) f.get(vo);
                var optVo2 = (Optional<LocalDateTime>) f.get(vo2);
                softly.assertThat(
                    optVo2.get().atOffset(ZoneOffset.UTC).toInstant().truncatedTo(ChronoUnit.MILLIS)).isEqualTo(
                    optVo.get().atOffset(ZoneOffset.UTC).toInstant().truncatedTo(ChronoUnit.MILLIS));
            } else if (f.getType().isAssignableFrom(Optional.class) && ((Optional) f.get(
                vo)).get().getClass().isAssignableFrom(LocalTime.class)) {
                var optVo = (Optional<LocalTime>) f.get(vo);
                var optVo2 = (Optional<LocalTime>) f.get(vo2);
                softly.assertThat(optVo2.get().atOffset(ZoneOffset.UTC).atDate(LocalDate.now()).toInstant().truncatedTo(
                    ChronoUnit.MILLIS)).isEqualTo(
                    optVo.get().atOffset(ZoneOffset.UTC).atDate(LocalDate.now()).toInstant().truncatedTo(
                        ChronoUnit.MILLIS));
            } else if (f.getType().isAssignableFrom(Optional.class) && ((Optional) f.get(
                vo)).get().getClass().isAssignableFrom(OffsetTime.class)) {
                var optVo = (Optional<OffsetTime>) f.get(vo);
                var optVo2 = (Optional<OffsetTime>) f.get(vo2);
                softly.assertThat(
                    optVo2.get().atDate(LocalDate.now()).toInstant().truncatedTo(ChronoUnit.MILLIS)).isEqualTo(
                    optVo.get().atDate(LocalDate.now()).toInstant().truncatedTo(ChronoUnit.MILLIS));
            } else if (f.getType().isAssignableFrom(Optional.class) && ((Optional) f.get(
                vo)).get().getClass().isAssignableFrom(Instant.class)) {
                var optVo = (Optional<Instant>) f.get(vo);
                var optVo2 = (Optional<Instant>) f.get(vo2);
                softly.assertThat(optVo2.get().truncatedTo(ChronoUnit.MILLIS)).isEqualTo(
                    optVo.get().truncatedTo(ChronoUnit.MILLIS));
            } else {
                log.debug("Debug analysis: " + f.getName() + " " + f.getType().getName());
                softly.assertThat(f.get(vo2)).isEqualTo(f.get(vo));
            }
        }
        softly.assertAll();

    }


    @Test
    public void testJacksonMappingSimpleVOOnly() throws IOException {
        SimpleVo simpleVo = SimpleVo.builder().setValue("TEST").build();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(simpleVo);
        log.info("JSON = " + json);

        SimpleVo simpleVo2 = objectMapper.readValue(json, SimpleVo.class);
        log.info("Read = " + simpleVo2);
        Assertions.assertThat(simpleVo2).isEqualTo(simpleVo);
    }

    @Test
    public void testJacksonMappingSimpleVOOnlyCallback() throws IOException{
        SimpleVo simpleVo = SimpleVo.builder().setValue("TEST").build();

        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(simpleVo);
        log.info("JSON = " + json);

        SimpleVo simpleVo2 = objectMapper.readValue(json, SimpleVo.class);
        log.info("Read = " + simpleVo2);
        Assertions.assertThat(simpleVo2).isEqualTo(simpleVo);
    }

    @Test
    public void testJacksonMappingInnerSimpleVO() throws IOException{

        TestClass tc = new TestClass();
        tc.setSimpleVo(SimpleVo.builder().setValue("INNER").build());
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(tc);
        log.info("JSON = " + json);

        TestClass tc2 = objectMapper.readValue(json, TestClass.class);
        log.info("Read = " + tc2);
        Assertions.assertThat(tc2).isEqualTo(tc);
    }

    @Test
    public void testJacksonMappingReadInnerSimpleVONull() throws IOException{
        TestClass tc = new TestClass();
        String json = """
            {
              "simpleVo" : null
            }""";
        log.info("JSON = " + json);

        TestClass tc2 = objectMapper.readValue(json, TestClass.class);
        log.info("Read = " + tc2);
        Assertions.assertThat(tc2).isEqualTo(tc);
    }

    @Test
    public void testJacksonMappingComplexVO() throws IOException{

        ComplexVo cvo = ComplexVo.builder()
            .setValueA("ValueA")
            .setValueB(SimpleVo.builder().setValue("ValueB").build())
            .build();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cvo);
        log.info("JSON = " + json);

        ComplexVo cvo2 = objectMapper.readValue(json, ComplexVo.class);
        log.info("Read = " + cvo2);
        Assertions.assertThat(cvo2).isEqualTo(cvo);
    }

    @Test
    public void testJacksonMappingReadComplexVONullA() throws IOException{
        ComplexVo cvo = ComplexVo.builder()
            .setValueB(SimpleVo.builder().setValue("ValueB").build())
            .build();
        String json = """
            {
              "valueA" : null,
              "valueB" : "ValueB"
            }""";
        log.info("JSON = " + json);

        ComplexVo cvo2 = objectMapper.readValue(json, ComplexVo.class);
        log.info("Read = " + cvo2);
        Assertions.assertThat(cvo2).isEqualTo(cvo);
    }

    @Test
    public void testJacksonMappingReadComplexVONullB() throws IOException{
        ComplexVo cvo = ComplexVo.builder()
            .setValueA("ValueA")
            .build();
        String json = """
            {
              "valueA" : "ValueA",
              "valueB" : null
            }""";
        log.info("JSON = " + json);

        ComplexVo cvo2 = objectMapper.readValue(json, ComplexVo.class);
        log.info("Read = " + cvo2);
        Assertions.assertThat(cvo2).isEqualTo(cvo);
    }

    @Test
    public void testJacksonMappingComplexVOOnlyA() throws IOException{

        ComplexVo cvo = ComplexVo.builder()
            .setValueA("ValueA")
            .build();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cvo);
        log.info("JSON = " + json);

        ComplexVo cvo2 = objectMapper.readValue(json, ComplexVo.class);
        log.info("Read = " + cvo2);
        Assertions.assertThat(cvo2).isEqualTo(cvo);
    }

    @Test
    public void testJacksonMappingComplexVOOnlyB() throws IOException{

        ComplexVo cvo = ComplexVo.builder()
            .setValueB(SimpleVo.builder().setValue("ValueB").build())
            .build();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cvo);
        log.info("JSON = " + json);

        ComplexVo cvo2 = objectMapper.readValue(json, ComplexVo.class);
        log.info("Read = " + cvo2);
        Assertions.assertThat(cvo2).isEqualTo(cvo);
    }

    @Test
    public void testJacksonMappingSimpleVOOneTOMany2OnlyFull() throws IOException{
        Set<SimpleVoOneToMany3> set = new HashSet<>();
        set.add(SimpleVoOneToMany3.builder().setValue("A").build());
        set.add(SimpleVoOneToMany3.builder().setValue("B").build());
        set.add(SimpleVoOneToMany3.builder().setValue("C").build());

        SimpleVoOneToMany2 vo = SimpleVoOneToMany2.builder().setValue("TEST").setOneToMany3Set(set).build();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(vo);
        log.info("JSON = " + json);

        SimpleVoOneToMany2 vo2 = objectMapper.readValue(json, SimpleVoOneToMany2.class);
        log.info("Read = " + vo2);
        Assertions.assertThat(vo2).isEqualTo(vo);
    }

    @Test
    public void testJacksonMappingSimpleVOOneTOMany2OnlyEmptySet() throws IOException{
        Set<SimpleVoOneToMany3> set = new HashSet<>();

        SimpleVoOneToMany2 vo = SimpleVoOneToMany2.builder().setValue("TEST").setOneToMany3Set(set).build();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(vo);
        log.info("JSON = " + json);

        SimpleVoOneToMany2 vo2 = objectMapper.readValue(json, SimpleVoOneToMany2.class);
        log.info("Read = " + vo2);
        Assertions.assertThat(vo2).isEqualTo(vo);
    }

    @Test
    public void testJacksonMappingSimpleVOOneTOMany2OnlyNullSet() throws IOException{
        SimpleVoOneToMany2 vo = SimpleVoOneToMany2.builder().setValue("TEST").setOneToMany3Set(null).build();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(vo);
        log.info("JSON = " + json);

        SimpleVoOneToMany2 vo2 = objectMapper.readValue(json, SimpleVoOneToMany2.class);
        log.info("Read = " + vo2);
        Assertions.assertThat(vo2).isEqualTo(vo);
    }

    @Test
    public void testJacksonMappingSimpleVOOneTOMany2ReadOnlyNullSet() throws IOException{
        SimpleVoOneToMany2 vo = SimpleVoOneToMany2.builder().setValue("TEST").build();
        String json = """
            {
              "value" : "TEST",
              "oneToMany3Set" : null}""";
        log.info("JSON = " + json);

        SimpleVoOneToMany2 vo2 = objectMapper.readValue(json, SimpleVoOneToMany2.class);
        log.info("Read = " + vo2);
        Assertions.assertThat(vo2).isEqualTo(vo);
    }

    @Test
    public void testIdentity() throws IOException{
        TestRootId id = new TestRootId(1L);
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(id);
        log.info("JSON = " + json);
        TestRootId id2 = objectMapper.readValue(json, TestRootId.class);
        log.info("Read = " + id2);
        Assertions.assertThat(id2).isEqualTo(id);
    }

    @Test
    public void testInnerIdentity() throws IOException{
        TestClassId id = new TestClassId();
        id.setTestRootId(new TestRootId(5L));
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(id);
        log.info("JSON = " + json);

        TestClassId id2 = objectMapper.readValue(json, TestClassId.class);
        log.info("Read = " + id2);
        Assertions.assertThat(id2).isEqualTo(id);
    }

    @Test
    public void testInnerIdentityFullObject() throws IOException{
        TestClassId id = new TestClassId();
        id.setTestRootId(new TestRootId(5L));
        String json = "{\n" +
            "  \"testRootId\" : { \"value\": 5 }\n" +
            "}";
        log.info("JSON = " + json);

        TestClassId id2 = objectMapper.readValue(json, TestClassId.class);
        log.info("Read = " + id2);
        Assertions.assertThat(id2).isEqualTo(id);
    }

    @Test
    public void testInnerIdentityFullObjectDifferentValueName() throws IOException{
        TestClassId id = new TestClassId();
        id.setTestRootId(new TestRootId(5L));
        String json = "{\n" +
            "  \"testRootId\" : { \"id\": 5 }\n" +
            "}";
        log.info("JSON = " + json);

        TestClassId id2 = objectMapper.readValue(json, TestClassId.class);
        log.info("Read = " + id2);
        Assertions.assertThat(id2).isEqualTo(id);
    }

    @Test
    public void testInnerIdentityNull() throws IOException{
        TestClassId id = new TestClassId();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(id);
        log.info("JSON = " + json);

        TestClassId id2 = objectMapper.readValue(json, TestClassId.class);
        log.info("Read = " + id2);
        Assertions.assertThat(id2).isEqualTo(id);
    }

    @Test
    public void testUUIDIdentity() throws IOException{
        TestRootSimpleUuidId id = new TestRootSimpleUuidId(UUID.randomUUID());
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(id);
        log.info("JSON = " + json);

        TestRootSimpleUuidId id2 = objectMapper.readValue(json, TestRootSimpleUuidId.class);
        log.info("Read = " + id2);
        Assertions.assertThat(id2).isEqualTo(id);
    }

    @Test
    public void testSingleValueVo() throws IOException{
        SimpleVo vo = new SimpleVo("Test");
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(vo);
        log.info("JSON = " + json);

        SimpleVo vo2 = objectMapper.readValue(json, SimpleVo.class);
        log.info("Read = " + vo2);
        Assertions.assertThat(vo2).isEqualTo(vo);
    }

    @Test
    public void testOrder() throws IOException{
        OrderBv3 b = TestDataGenerator.buildOrderBv3();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(b);
        log.info("JSON = " + json);

        OrderBv3 b2 = objectMapper.readValue(json, OrderBv3.class);
        log.info("Read = " + b2);
        Assertions.assertThat(b2)
            .usingRecursiveComparison()
            .ignoringAllOverriddenEquals()
            .ignoringCollectionOrder()
            .withStrictTypeChecking()
            .withComparatorForType(Comparator.comparingDouble(BigDecimal::doubleValue), BigDecimal.class)
            .ignoringFieldsOfTypes(UUID.class)
            .isEqualTo(b);
        Assertions.assertThat(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(b2)).isEqualTo(json);
    }

    @Test
    public void testOrderIdProvisioningAndCallbacks() throws IOException{

        String json = """
            {
              "priority" : 1,
              "customerNumber" : "777777",
              "totalPrice" : {
                "amount" : 200,
                "currency" : "EUR"
              },
              "deliveryAddress" : {
                "name" : "Thor",
                "street" : "Hammerallee 7",
                "postalCode" : "77777",
                "city" : "Donnerberg"
              },
              "orderItems" : [ {
                "articleId" : 1,
                "quantity" : 100,
                "unitPrice" : {
                  "amount" : 1,
                  "currency" : "EUR"
                }
              }, {
                "articleId" : 2,
                "quantity" : 10,
                "unitPrice" : {
                  "amount" : 10,
                  "currency" : "EUR"
                }
              } ],
              "orderStatus" : {
                "statusCode" : "INITIAL",
                "statusChangedAt" : "2021-01-01T12:01:00"
              },
              "orderComments" : [ {
                "commentText" : "Mach schnell sonst kommt der Hammer!",
                "commentedAt" : "2021-01-01T12:00:00"
              }, {
                "commentText" : "Der Donnergott grüßt!",
                "commentedAt" : "2021-01-02T12:00:00"
              } ]
            }""";
        log.info("JSON = " + json);

        OrderBv3 b2 = objectMapper.readValue(json, OrderBv3.class);
        log.info("Read = " + b2);
    }

    @Test
    public void testManyOrders() throws IOException{
        var many = TestDataGenerator.buildManyOrdersBv3();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(many);
        log.info("JSON = " + json);
        var b2 = objectMapper.readValue(json, new TypeReference<List<OrderBv3>>() {
        });

        log.info("Read = " + b2);
        Assertions.assertThat(b2)
            .usingRecursiveComparison()
            .ignoringAllOverriddenEquals()
            .ignoringCollectionOrder()
            .withStrictTypeChecking()
            .withComparatorForType(Comparator.comparingDouble(BigDecimal::doubleValue), BigDecimal.class)
            .ignoringFieldsOfTypes(UUID.class)
            .isEqualTo(many);
        Assertions.assertThat(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(b2)).isEqualTo(json);
    }

    @Test
    public void testComplex() throws IOException{
        TestRoot b = TestDataGenerator.buildTestRootComplex();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(b);
        log.info("JSON = " + json);

        TestRoot b2 = objectMapper.readValue(json, TestRoot.class);
        log.info("Read = " + b2);
        Assertions.assertThat(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(b2)).isEqualTo(json);
    }


    @Test
    public void testOptionalMax() throws IOException{
        OptionalAggregate b = TestDataGenerator.buildOptionalAggregateMax();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(b);
        log.info("JSON = " + json);

        OptionalAggregate b2 = objectMapper.readValue(json, OptionalAggregate.class);
        log.info("Read = " + b2);
        Assertions.assertThat(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(b2)).isEqualTo(json);
    }

    @Test
    public void testOptionalMin() throws IOException{
        OptionalAggregate b = TestDataGenerator.buildOptionalAggregateMin();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(b);
        log.info("JSON = " + json);

        OptionalAggregate b2 = objectMapper.readValue(json, OptionalAggregate.class);
        log.info("Read = " + b2);
        Assertions.assertThat(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(b2)).isEqualTo(json);
    }

    @Test
    public void testTestRootSimpleWithIdFromIdProvider() throws IOException{
        String json = """
            {
              "name" : "Test",
              "concurrencyVersion" : 0
            }""";
        log.info("JSON = " + json);

        TestRootSimple t2 = objectMapper.readValue(json, TestRootSimple.class);
        log.info("Read = " + t2);
    }


    @Test
    public void testTestRootSimpleUUIDWithIdFromIdProviderByUUID() throws IOException{
        String json = """
            {
              "name" : "Test",
              "concurrencyVersion" : 0
            }""";
        log.info("JSON = " + json);

        TestRootSimpleUuid t2 = objectMapper.readValue(json, TestRootSimpleUuid.class);
        log.info("Read = " + t2);
    }

    @Test
    public void testIdProvisioningDtoRegularWithId() throws IOException{
        IdProvisioningDto dto = new IdProvisioningDto();
        dto.orderItem = OrderItemBv3.builder()
            .setId(new OrderItemIdBv3(1L))
            .setArticleId(new ArticleIdBv3(1L))
            .setQuantity(100)
            .setUnitPrice(PriceBv3.builder()
                .setAmount(BigDecimal.ONE)
                .setCurrency(CurrencyEnumBv3.EUR)
                .build())
            .build();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);
        log.info("JSON = " + json);

        IdProvisioningDto dtoRead = objectMapper.readValue(json, IdProvisioningDto.class);
        log.info("Read = " + dtoRead);
        Assertions.assertThat(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dtoRead)).isEqualTo(
            json);
    }

    @Test
    public void testJacksonMappingReadAutoMappedComplexVoNullA() throws IOException{
        AutoMappedComplexVo cvo = AutoMappedComplexVo.builder()
            .setValueB(AutoMappedSimpleVo.builder().setValue("ValueB").build())
            .build();
        String json = "{\n" +
            "  \"valueA\" : null,\n" +
            "  \"valueB\" : \"ValueB\"\n" +
            "}";
        log.info("JSON = " + json);

        AutoMappedComplexVo cvo2 = objectMapper.readValue(json, AutoMappedComplexVo.class);
        log.info("Read = " + cvo2);
        assertThat(cvo2).isEqualTo(cvo);
    }

    @Test
    public void testReadIdProvisioningDtoWithoutId() throws IOException{
        String json = """
            {
              "orderItem" : {
                "articleId" : 1,
                "quantity" : 100,
                "concurrencyVersion" : 0,
                "unitPrice" : {
                  "amount" : 1,
                  "currency" : "EUR"
                }
              }
            }""";

        IdProvisioningDto dtoToAssert = new IdProvisioningDto();
        dtoToAssert.orderItem = OrderItemBv3.builder()
            .setId(new OrderItemIdBv3(1L))
            .setArticleId(new ArticleIdBv3(1L))
            .setQuantity(100)
            .setUnitPrice(PriceBv3.builder()
                .setAmount(BigDecimal.ONE)
                .setCurrency(CurrencyEnumBv3.EUR)
                .build())
            .build();


        IdProvisioningDto dtoRead = objectMapper.readValue(json, IdProvisioningDto.class);
        log.info("Read = " + dtoRead);
        Assertions.assertThat(dtoRead.getOrderItem().getArticleId()).isEqualTo(
            dtoToAssert.getOrderItem().getArticleId());
        Assertions.assertThat(dtoRead.getOrderItem().getId()).isNotNull();
        Assertions.assertThat(dtoRead.getOrderItem().getQuantity()).isEqualTo(
            dtoToAssert.getOrderItem().getQuantity());
        Assertions.assertThat(dtoRead.getOrderItem().getUnitPrice()).isEqualTo(
            dtoToAssert.getOrderItem().getUnitPrice());
    }

    public static class IdProvisioningDto {

        public OrderItemBv3 getOrderItem() {
            return orderItem;
        }

        private OrderItemBv3 orderItem;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IdProvisioningDto that = (IdProvisioningDto) o;
            return orderItem.equals(that.orderItem);
        }

        @Override
        public int hashCode() {
            return Objects.hash(orderItem);
        }
    }

    public static class TestClass {
        private SimpleVo simpleVo;

        public SimpleVo getSimpleVo() {
            return simpleVo;
        }

        public void setSimpleVo(SimpleVo simpleVo) {
            this.simpleVo = simpleVo;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TestClass testClass)) return false;
            return Objects.equals(simpleVo, testClass.simpleVo);
        }

        @Override
        public int hashCode() {
            return Objects.hash(simpleVo);
        }

        @Override
        public String toString() {
            return "TestClass{" +
                "simpleVo=" + simpleVo +
                '}';
        }
    }

    public static class TestClassId {
        private TestRootId testRootId;

        public TestRootId getTestRootId() {
            return testRootId;
        }

        public void setTestRootId(TestRootId testRootId) {
            this.testRootId = testRootId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TestClassId that)) return false;
            return Objects.equals(testRootId, that.testRootId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(testRootId);
        }

        @Override
        public String toString() {
            return "TestClassId{" +
                "testRootId=" + testRootId +
                '}';
        }
    }

}



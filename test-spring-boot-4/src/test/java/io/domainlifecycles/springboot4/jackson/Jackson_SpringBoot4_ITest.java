package io.domainlifecycles.springboot4.jackson;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.domainlifecycles.jackson.api.JacksonMappingCustomizer;
import io.domainlifecycles.jackson.api.MappingAction;
import io.domainlifecycles.jackson.databind.context.DomainObjectMappingContext;
import io.domainlifecycles.springboot4.persistence.bestellung.BestellungBv3Repository;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import tests.shared.TestDataGenerator;
import tests.shared.complete.onlinehandel.bestellung.ArtikelIdBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellKommentarBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellPositionBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellPositionIdBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellStatusBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellungBv3;
import tests.shared.complete.onlinehandel.bestellung.BestellungIdBv3;
import tests.shared.complete.onlinehandel.bestellung.LieferadresseBv3;
import tests.shared.complete.onlinehandel.bestellung.PreisBv3;
import tests.shared.complete.onlinehandel.bestellung.WaehrungEnumBv3;
import tests.shared.jackson.TypeTestValueObject;
import tests.shared.persistence.domain.optional.OptionalAggregate;
import tests.shared.persistence.domain.simple.TestRootSimple;
import tests.shared.persistence.domain.simpleUuid.TestRootSimpleUuid;
import tests.shared.persistence.domain.simpleUuid.TestRootSimpleUuidId;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedComplexVo;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedSimpleVo;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedSimpleVoOneToMany2;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedSimpleVoOneToMany3;

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
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"test"})
public class Jackson_SpringBoot4_ITest {

    private static final Logger log = org.slf4j.LoggerFactory.getLogger(Jackson_SpringBoot4_ITest.class);
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testJacksonMappingTypeTest() throws Throwable {
        TypeTestValueObject vo = new TypeTestValueObject();
        vo.setABoolean(true);
        vo.setAByte((byte) 5);
        vo.setAChar('a');
        vo.setADouble(5);
        vo.setAFloat(5);
        vo.setALong(5l);
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
        vo.setOptionalByte(Optional.of(Byte.valueOf((byte) 4)));
        vo.setOptionalDouble(Optional.of(Double.valueOf(5)));
        vo.setOptionalFloat(Optional.of(Float.valueOf(5)));
        vo.setOptionalInstant(Optional.of(Instant.now()));
        vo.setOptionalInteger(Optional.of(Integer.valueOf(4)));
        vo.setOptionalShort(Optional.of((short) 4));
        vo.setOptionalLocalDate(Optional.of(LocalDate.now()));
        vo.setZonedDateTime(ZonedDateTime.now());
        vo.setOptionalLocalDateTime(Optional.of(LocalDateTime.now()));
        vo.setOptionalString(Optional.of("test"));
        vo.setOptionalLong(Optional.of(8l));
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
                softly.assertThat(((ZonedDateTime) f.get(vo2)).toInstant()).isEqualTo(
                    ((ZonedDateTime) f.get(vo)).toInstant());
            } else if (f.getType().isAssignableFrom(Calendar.class)) {
                softly.assertThat(((Calendar) f.get(vo2)).toInstant()).isEqualTo(((Calendar) f.get(vo)).toInstant());
            } else if (f.getType().isAssignableFrom(OffsetDateTime.class)) {
                softly.assertThat(((OffsetDateTime) f.get(vo2)).toInstant()).isEqualTo(
                    ((OffsetDateTime) f.get(vo)).toInstant());
            } else if (f.getType().isAssignableFrom(Optional.class) && ((Optional) f.get(
                vo)).get().getClass().isAssignableFrom(ZonedDateTime.class)) {
                var optVo = (Optional<ZonedDateTime>) f.get(vo);
                var optVo2 = (Optional<ZonedDateTime>) f.get(vo2);
                softly.assertThat(optVo2.get().toInstant()).isEqualTo(optVo.get().toInstant());
            } else if (f.getType().isAssignableFrom(Optional.class) && ((Optional) f.get(
                vo)).get().getClass().isAssignableFrom(OffsetDateTime.class)) {
                var optVo = (Optional<OffsetDateTime>) f.get(vo);
                var optVo2 = (Optional<OffsetDateTime>) f.get(vo2);
                softly.assertThat(optVo2.get().toInstant()).isEqualTo(optVo.get().toInstant());
            } else {
                softly.assertThat(f.get(vo2)).isEqualTo(f.get(vo));
            }
        }
        softly.assertAll();

    }


    @Test
    public void testJacksonMappingSimpleVOOnly() throws Throwable {
        AutoMappedSimpleVo simpleVo = AutoMappedSimpleVo.builder().setValue("TEST").build();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(simpleVo);
        log.info("JSON = " + json);

        AutoMappedSimpleVo simpleVo2 = objectMapper.readValue(json, AutoMappedSimpleVo.class);
        log.info("Read = " + simpleVo2);
        assertThat(simpleVo2).isEqualTo(simpleVo);
    }

    @Test
    public void testJacksonMappingSimpleVOOnlyCallback() throws Throwable {
        AutoMappedSimpleVo simpleVo = AutoMappedSimpleVo.builder().setValue("TEST").build();

        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(simpleVo);
        log.info("JSON = " + json);

        AutoMappedSimpleVo simpleVo2 = objectMapper.readValue(json, AutoMappedSimpleVo.class);
        log.info("Read = " + simpleVo2);
        assertThat(simpleVo2).isEqualTo(simpleVo);
    }

    @Test
    public void testJacksonMappingInnerSimpleVO() throws Throwable {

        TestClass tc = new TestClass();
        tc.setSimpleVo(AutoMappedSimpleVo.builder().setValue("INNER").build());
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(tc);
        log.info("JSON = " + json);

        TestClass tc2 = objectMapper.readValue(json, TestClass.class);
        log.info("Read = " + tc2);
        assertThat(tc2).isEqualTo(tc);
    }

    @Test
    public void testJacksonMappingReadInnerSimpleVONull() throws Throwable {
        TestClass tc = new TestClass();
        String json = "{\n" +
            "  \"simpleVo\" : null\n" +
            "}";
        log.info("JSON = " + json);

        TestClass tc2 = objectMapper.readValue(json, TestClass.class);
        log.info("Read = " + tc2);
        assertThat(tc2).isEqualTo(tc);
    }

    @Test
    public void testJacksonMappingAutoMappedComplexVo() throws Throwable {

        AutoMappedComplexVo cvo = AutoMappedComplexVo.builder()
            .setValueA("ValueA")
            .setValueB(AutoMappedSimpleVo.builder().setValue("ValueB").build())
            .build();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cvo);
        log.info("JSON = " + json);

        AutoMappedComplexVo cvo2 = objectMapper.readValue(json, AutoMappedComplexVo.class);
        log.info("Read = " + cvo2);
        assertThat(cvo2).isEqualTo(cvo);
    }

    @Test
    public void testJacksonMappingReadAutoMappedComplexVoNullA() throws Throwable {
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
    public void testJacksonMappingReadAutoMappedComplexVoNullB() throws Throwable {
        AutoMappedComplexVo cvo = AutoMappedComplexVo.builder()
            .setValueA("ValueA")
            .build();
        String json = "{\n" +
            "  \"valueA\" : \"ValueA\",\n" +
            "  \"valueB\" : null\n" +
            "}";
        log.info("JSON = " + json);

        AutoMappedComplexVo cvo2 = objectMapper.readValue(json, AutoMappedComplexVo.class);
        log.info("Read = " + cvo2);
        assertThat(cvo2).isEqualTo(cvo);
    }

    @Test
    public void testJacksonMappingAutoMappedComplexVoOnlyA() throws Throwable {

        AutoMappedComplexVo cvo = AutoMappedComplexVo.builder()
            .setValueA("ValueA")
            .build();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cvo);
        log.info("JSON = " + json);

        AutoMappedComplexVo cvo2 = objectMapper.readValue(json, AutoMappedComplexVo.class);
        log.info("Read = " + cvo2);
        assertThat(cvo2).isEqualTo(cvo);
    }

    @Test
    public void testJacksonMappingAutoMappedComplexVoOnlyB() throws Throwable {

        AutoMappedComplexVo cvo = AutoMappedComplexVo.builder()
            .setValueB(AutoMappedSimpleVo.builder().setValue("ValueB").build())
            .build();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(cvo);
        log.info("JSON = " + json);

        AutoMappedComplexVo cvo2 = objectMapper.readValue(json, AutoMappedComplexVo.class);
        log.info("Read = " + cvo2);
        assertThat(cvo2).isEqualTo(cvo);
    }

    @Test
    public void testJacksonMappingSimpleVOOneTOMany2OnlyFull() throws Throwable {
        Set<AutoMappedSimpleVoOneToMany3> set = new HashSet<>();
        set.add(AutoMappedSimpleVoOneToMany3.builder().setValue("A").build());
        set.add(AutoMappedSimpleVoOneToMany3.builder().setValue("B").build());
        set.add(AutoMappedSimpleVoOneToMany3.builder().setValue("C").build());

        AutoMappedSimpleVoOneToMany2 vo = AutoMappedSimpleVoOneToMany2.builder().setValue("TEST").setOneToMany3Set(
            set).build();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(vo);
        log.info("JSON = " + json);

        AutoMappedSimpleVoOneToMany2 vo2 = objectMapper.readValue(json, AutoMappedSimpleVoOneToMany2.class);
        log.info("Read = " + vo2);
        assertThat(vo2).isEqualTo(vo);
    }

    @Test
    public void testJacksonMappingSimpleVOOneTOMany2OnlyEmptySet() throws Throwable {
        Set<AutoMappedSimpleVoOneToMany3> set = new HashSet<>();

        AutoMappedSimpleVoOneToMany2 vo = AutoMappedSimpleVoOneToMany2.builder().setValue("TEST").setOneToMany3Set(
            set).build();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(vo);
        log.info("JSON = " + json);

        AutoMappedSimpleVoOneToMany2 vo2 = objectMapper.readValue(json, AutoMappedSimpleVoOneToMany2.class);
        log.info("Read = " + vo2);
        assertThat(vo2).isEqualTo(vo);
    }

    @Test
    public void testJacksonMappingSimpleVOOneTOMany2OnlyNullSet() throws Throwable {
        AutoMappedSimpleVoOneToMany2 vo = AutoMappedSimpleVoOneToMany2.builder().setValue("TEST").setOneToMany3Set(
            null).build();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(vo);
        log.info("JSON = " + json);

        AutoMappedSimpleVoOneToMany2 vo2 = objectMapper.readValue(json, AutoMappedSimpleVoOneToMany2.class);
        log.info("Read = " + vo2);
        assertThat(vo2).isEqualTo(vo);
    }

    @Test
    public void testJacksonMappingSimpleVOOneTOMany2ReadOnlyNullSet() throws Throwable {
        AutoMappedSimpleVoOneToMany2 vo = AutoMappedSimpleVoOneToMany2.builder().setValue("TEST").build();
        String json = "{\n" +
            "  \"value\" : \"TEST\",\n" +
            "  \"oneToMany3Set\" : null" +
            "}";
        log.info("JSON = " + json);

        AutoMappedSimpleVoOneToMany2 vo2 = objectMapper.readValue(json, AutoMappedSimpleVoOneToMany2.class);
        log.info("Read = " + vo2);
        assertThat(vo2).isEqualTo(vo);
    }

    @Test
    public void testIdentity() throws Throwable {
        BestellungIdBv3 id = new BestellungIdBv3(1l);
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(id);
        log.info("JSON = " + json);
        BestellungIdBv3 id2 = objectMapper.readValue(json, BestellungIdBv3.class);
        log.info("Read = " + id2);
        assertThat(id2).isEqualTo(id);
    }

    @Test
    public void testInnerIdentity() throws Throwable {
        TestClassId id = new TestClassId();
        id.setTestRootId(new BestellungIdBv3(5l));
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(id);
        log.info("JSON = " + json);

        TestClassId id2 = objectMapper.readValue(json, TestClassId.class);
        log.info("Read = " + id2);
        assertThat(id2).isEqualTo(id);
    }

    @Test
    public void testInnerIdentityNull() throws Throwable {
        TestClassId id = new TestClassId();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(id);
        log.info("JSON = " + json);

        TestClassId id2 = objectMapper.readValue(json, TestClassId.class);
        log.info("Read = " + id2);
        assertThat(id2).isEqualTo(id);
    }

    @Test
    public void testUidIdentity() throws Throwable {
        TestRootSimpleUuidId id = new TestRootSimpleUuidId(UUID.randomUUID());
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(id);
        log.info("JSON = " + json);

        TestRootSimpleUuidId id2 = objectMapper.readValue(json, TestRootSimpleUuidId.class);
        log.info("Read = " + id2);
        assertThat(id2).isEqualTo(id);
    }

    @Test
    public void testBestellung() throws Throwable {
        BestellungBv3 b = TestDataGenerator.buildBestellungBv3();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(b);
        log.info("JSON = " + json);

        BestellungBv3 b2 = objectMapper.readValue(json, BestellungBv3.class);
        log.info("Read = " + b2);
        assertThat(b2)
            .usingRecursiveComparison()
            .ignoringAllOverriddenEquals()
            .ignoringCollectionOrder()
            .withStrictTypeChecking()
            .withComparatorForType(new Comparator<BigDecimal>() {
                @Override
                public int compare(BigDecimal o1, BigDecimal o2) {
                    return Double.compare(o1.doubleValue(), o2.doubleValue());
                }
            }, BigDecimal.class)
            .ignoringFieldsOfTypes(UUID.class)
            .isEqualTo(b);
        assertThat(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(b2)).isEqualTo(json);
    }

    @Test
    public void testBestellungIdProvisioningAndCallbacks() throws Throwable {
        int callsBefore = TestConfiguration.calls;

        String json = "{\n" +
            "  \"prioritaet\" : 1,\n" +
            "  \"kundennummer\" : \"777777\",\n" +
            "  \"gesamtPreis\" : {\n" +
            "    \"betrag\" : 200,\n" +
            "    \"waehrung\" : \"EUR\"\n" +
            "  },\n" +
            "  \"lieferadresse\" : {\n" +
            "    \"name\" : \"Thor\",\n" +
            "    \"strasse\" : \"Hammerallee 7\",\n" +
            "    \"postleitzahl\" : \"77777\",\n" +
            "    \"ort\" : \"Donnerberg\"\n" +
            "  },\n" +
            "  \"bestellPositionen\" : [ {\n" +
            "    \"artikelId\" : 1,\n" +
            "    \"stueckzahl\" : 100,\n" +
            "    \"stueckPreis\" : {\n" +
            "      \"betrag\" : 1,\n" +
            "      \"waehrung\" : \"EUR\"\n" +
            "    }\n" +
            "  }, {\n" +
            "    \"artikelId\" : 2,\n" +
            "    \"stueckzahl\" : 10,\n" +
            "    \"stueckPreis\" : {\n" +
            "      \"betrag\" : 10,\n" +
            "      \"waehrung\" : \"EUR\"\n" +
            "    }\n" +
            "  } ],\n" +
            "  \"bestellStatus\" : {\n" +
            "    \"statusCode\" : \"INITIAL\",\n" +
            "    \"statusAenderungAm\" : \"2021-01-01T12:01:00\"\n" +
            "  },\n" +
            "  \"bestellKommentare\" : [ {\n" +
            "    \"kommentarText\" : \"Mach schnell sonst kommt der Hammer!\",\n" +
            "    \"kommentarAm\" : \"2021-01-01T12:00:00\"\n" +
            "  }, {\n" +
            "    \"kommentarText\" : \"Der Donnergott grüßt!\",\n" +
            "    \"kommentarAm\" : \"2021-01-02T12:00:00\"\n" +
            "  } ]\n" +
            "}";
        log.info("JSON = " + json);

        BestellungBv3 b2 = objectMapper.readValue(json, BestellungBv3.class);
        log.info("Read = " + b2);

        assertThat(TestConfiguration.calls).isEqualTo(callsBefore + 7);
    }

    @Test
    public void testManyBestellungen() throws Throwable {
        var many = TestDataGenerator.buildManyBestellungenBv3();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(many);
        log.info("JSON = " + json);
        var b2 = objectMapper.readValue(json, new TypeReference<List<BestellungBv3>>() {
        });

        log.info("Read = " + b2);
        assertThat(b2)
            .usingRecursiveComparison()
            .ignoringAllOverriddenEquals()
            .ignoringCollectionOrder()
            .withStrictTypeChecking()
            .withComparatorForType(new Comparator<BigDecimal>() {
                @Override
                public int compare(BigDecimal o1, BigDecimal o2) {
                    return Double.compare(o1.doubleValue(), o2.doubleValue());
                }
            }, BigDecimal.class)
            .ignoringFieldsOfTypes(UUID.class)
            .isEqualTo(many);
        assertThat(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(b2)).isEqualTo(json);
    }

    @Test
    public void testOptionalMax() throws Throwable {
        OptionalAggregate b = TestDataGenerator.buildOptionalAggregateMax();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(b);
        log.info("JSON = " + json);

        OptionalAggregate b2 = objectMapper.readValue(json, OptionalAggregate.class);
        log.info("Read = " + b2);
        assertThat(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(b2)).isEqualTo(json);
    }

    @Test
    public void testOptionalMin() throws Throwable {
        OptionalAggregate b = TestDataGenerator.buildOptionalAggregateMin();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(b);
        log.info("JSON = " + json);

        OptionalAggregate b2 = objectMapper.readValue(json, OptionalAggregate.class);
        log.info("Read = " + b2);
        assertThat(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(b2)).isEqualTo(json);
    }

    @Test
    public void testTestRootSimpleWithIdFromIdProviderBySequence() throws Throwable {
        String json = "{\n" +
            "  \"name\" : \"Test\",\n" +
            "  \"concurrencyVersion\" : 0\n" +
            "}";
        log.info("JSON = " + json);

        TestRootSimple t2 = objectMapper.readValue(json, TestRootSimple.class);
        log.info("Read = " + t2);
    }


    @Test
    public void testTestRootSimpleUUIDWithIdFromIdProviderByUUID() throws Throwable {
        String json = "{\n" +
            "  \"name\" : \"Test\",\n" +
            "  \"concurrencyVersion\" : 0\n" +
            "}";
        log.info("JSON = " + json);

        TestRootSimpleUuid t2 = objectMapper.readValue(json, TestRootSimpleUuid.class);
        log.info("Read = " + t2);
    }

    @Test
    public void testIdProvisioningDtoRegularWithId() throws Throwable {
        IdProvisioningDto dto = new IdProvisioningDto();
        dto.bestellPosition = BestellPositionBv3.builder()
            .setId(new BestellPositionIdBv3(1l))
            .setArtikelId(new ArtikelIdBv3(1l))
            .setStueckzahl(100)
            .setStueckPreis(PreisBv3.builder()
                .setBetrag(BigDecimal.ONE)
                .setWaehrung(WaehrungEnumBv3.EUR)
                .build())
            .build();
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dto);
        log.info("JSON = " + json);

        IdProvisioningDto dtoRead = objectMapper.readValue(json, IdProvisioningDto.class);
        log.info("Read = " + dtoRead);
        assertThat(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(dtoRead)).isEqualTo(json);
    }

    @Test
    public void testReadIdProvisioningDtoWithoutId() throws Throwable {
        String json = "{\n" +
            "  \"bestellPosition\" : {\n" +
            "    \"artikelId\" : 1,\n" +
            "    \"stueckzahl\" : 100,\n" +
            "    \"concurrencyVersion\" : 0,\n" +
            "    \"stueckPreis\" : {\n" +
            "      \"betrag\" : 1,\n" +
            "      \"waehrung\" : \"EUR\"\n" +
            "    }\n" +
            "  }\n" +
            "}";

        IdProvisioningDto dtoToAssert = new IdProvisioningDto();
        dtoToAssert.bestellPosition = BestellPositionBv3.builder()
            .setId(new BestellPositionIdBv3(1l))
            .setArtikelId(new ArtikelIdBv3(1l))
            .setStueckzahl(100)
            .setStueckPreis(PreisBv3.builder()
                .setBetrag(BigDecimal.ONE)
                .setWaehrung(WaehrungEnumBv3.EUR)
                .build())
            .build();


        IdProvisioningDto dtoRead = objectMapper.readValue(json, IdProvisioningDto.class);
        log.info("Read = " + dtoRead);
        assertThat(dtoRead.getBestellPosition().getArtikelId()).isEqualTo(
            dtoToAssert.getBestellPosition().getArtikelId());
        assertThat(dtoRead.getBestellPosition().getId()).isNotNull();
        assertThat(dtoRead.getBestellPosition().getStueckzahl()).isEqualTo(
            dtoToAssert.getBestellPosition().getStueckzahl());
        assertThat(dtoRead.getBestellPosition().getStueckPreis()).isEqualTo(
            dtoToAssert.getBestellPosition().getStueckPreis());
    }

    public static class IdProvisioningDto {

        public BestellPositionBv3 getBestellPosition() {
            return bestellPosition;
        }

        private BestellPositionBv3 bestellPosition;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            IdProvisioningDto that = (IdProvisioningDto) o;
            return bestellPosition.equals(that.bestellPosition);
        }

        @Override
        public int hashCode() {
            return Objects.hash(bestellPosition);
        }
    }

    public static class TestClass {
        private AutoMappedSimpleVo simpleVo;

        public AutoMappedSimpleVo getSimpleVo() {
            return simpleVo;
        }

        public void setSimpleVo(AutoMappedSimpleVo simpleVo) {
            this.simpleVo = simpleVo;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TestClass)) return false;
            TestClass testClass = (TestClass) o;
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
        private BestellungIdBv3 testRootId;

        public BestellungIdBv3 getTestRootId() {
            return testRootId;
        }

        public void setTestRootId(BestellungIdBv3 testRootId) {
            this.testRootId = testRootId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof TestClassId)) return false;
            TestClassId that = (TestClassId) o;
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

    @org.springframework.boot.test.context.TestConfiguration
    public static class TestConfiguration {

        public static int calls = 0;

        @Bean
        JacksonMappingCustomizer<AutoMappedSimpleVo> simpleVoMapping() {
            return new JacksonMappingCustomizer<>(AutoMappedSimpleVo.class) {

                @Override
                public MappingAction beforeObjectRead(DomainObjectMappingContext mappingContext, ObjectCodec codec) {
                    calls++;
                    return super.beforeObjectRead(mappingContext, codec);
                }

            };
        }

        @Bean
        JacksonMappingCustomizer<BestellungBv3> bestellungMapping(BestellungBv3Repository bestellungRepository) {
            return new JacksonMappingCustomizer(BestellungBv3.class) {

                @Override
                public MappingAction beforeObjectRead(DomainObjectMappingContext mappingContext, ObjectCodec codec) {
                    calls++;
                    return super.beforeObjectRead(mappingContext, codec);
                }
            };
        }

        @Bean
        JacksonMappingCustomizer<BestellPositionBv3> bestellPositionMapping(BestellungBv3Repository bestellungRepository) {
            return new JacksonMappingCustomizer<>(BestellPositionBv3.class) {

                @Override
                public MappingAction beforeObjectRead(DomainObjectMappingContext mappingContext, ObjectCodec codec) {
                    calls++;
                    return super.beforeObjectRead(mappingContext, codec);
                }

            };
        }

        @Bean
        JacksonMappingCustomizer<BestellKommentarBv3> bestellKommentarMapping(BestellungBv3Repository bestellungRepository) {
            return new JacksonMappingCustomizer(BestellKommentarBv3.class) {

                @Override
                public MappingAction beforeObjectRead(DomainObjectMappingContext mappingContext, ObjectCodec codec) {
                    calls++;
                    return super.beforeObjectRead(mappingContext, codec);
                }

            };
        }

        @Bean
        JacksonMappingCustomizer<LieferadresseBv3> lieferadresseMapping(BestellungBv3Repository bestellungRepository) {
            return new JacksonMappingCustomizer<>(LieferadresseBv3.class) {

                @Override
                public MappingAction beforeObjectRead(DomainObjectMappingContext mappingContext, ObjectCodec codec) {
                    calls++;
                    return super.beforeObjectRead(mappingContext, codec);
                }

            };
        }

        @Bean
        JacksonMappingCustomizer<BestellStatusBv3> bestellStatusMapping(BestellungBv3Repository bestellungRepository) {
            return new JacksonMappingCustomizer<>(BestellStatusBv3.class) {
                @Override
                public MappingAction beforeObjectRead(DomainObjectMappingContext mappingContext, ObjectCodec codec) {
                    calls++;
                    return super.beforeObjectRead(mappingContext, codec);
                }

            };
        }

    }

}



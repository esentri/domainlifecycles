package io.domainlifecycles.spring.http;


import io.domainlifecycles.domain.types.ValueObject;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class SingleValuedValueObjectConverterTest {

    private static FormattingConversionService cs;
    private static StringToDomainValueObjectConverterFactory factory;

    public record AnotherVo(String myValue) implements ValueObject {}
    public record BigDecimalVo(BigDecimal theValue) implements ValueObject {}
    public record LocalDateVo(LocalDate when) implements ValueObject {}

    @BeforeAll
    static void init(){
        cs = new DefaultFormattingConversionService();
        Domain.initialize(new ReflectiveDomainMirrorFactory("io.domainlifecycles.spring.http"));
        factory = new StringToDomainValueObjectConverterFactory(TestUtil.providerOf(cs), TestUtil.emptyProvider());
    }

    @Test
    void convertsStringToValueObjectUsingSpringConverterBigDecimal() {
        // given
        var orderConv = factory.getConverter(BigDecimalVo.class);
        //when then
        assertThat(orderConv.convert("12.1234")).isEqualTo(new BigDecimalVo(BigDecimal.valueOf(12.1234)));
    }

    @Test
    void convertsStringToValueObjectUsingSpringConverterLocalDate() {
        // given
        var userConv  = factory.getConverter(LocalDateVo.class);
        //when then
        assertThat(userConv.convert("2023-01-01"))
            .isEqualTo(new LocalDateVo(LocalDate.of(2023, 1, 1)));
    }

    @Test
    void convertsStringToIdentityUsingNoConverter() {
        // given
        var anotherVoConverter  = factory.getConverter(AnotherVo.class);
        //when then
        assertThat(anotherVoConverter.convert("jo"))
            .isEqualTo(new AnotherVo("jo"));
    }

    @Test
    void convertsNull() {
        // given
        var anotherIdConverter  = factory.getConverter(AnotherVo.class);
        //when then
        assertThat(anotherIdConverter.convert(null))
            .isEqualTo(null);
    }

}

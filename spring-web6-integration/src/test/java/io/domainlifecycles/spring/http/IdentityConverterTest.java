package io.domainlifecycles.spring.http;

import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.mirror.api.Domain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import org.junit.jupiter.api.BeforeAll;

import org.junit.jupiter.api.Test;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;

import static org.assertj.core.api.Assertions.assertThat;

class IdentityConverterTest {

    private static FormattingConversionService cs;
    private static StringToDomainIdentityConverterFactory factory;

    public record AnotherId(String value) implements Identity<String> {}
    public record OrderId(Long value) implements Identity<Long> {}
    public record UserId(java.util.UUID value) implements Identity<java.util.UUID> {}

    @BeforeAll
    static void init(){
        cs = new DefaultFormattingConversionService();
        Domain.initialize(new ReflectiveDomainMirrorFactory("io.domainlifecycles.spring.http"));
        factory = new StringToDomainIdentityConverterFactory(TestUtil.providerOf(cs), TestUtil.emptyProvider());
    }

    @Test
    void convertsStringToIdentityUsingSpringConverterLong() {
        // given
        var orderConv = factory.getConverter(OrderId.class);
        //when then
        assertThat(orderConv.convert("123")).isEqualTo(new OrderId(123L));
    }

    @Test
    void convertsStringToIdentityUsingSpringConverterUUID() {
        // given
        var userConv  = factory.getConverter(UserId.class);
        //when then
        assertThat(userConv.convert("550e8400-e29b-41d4-a716-446655440000"))
            .isEqualTo(new UserId(java.util.UUID.fromString("550e8400-e29b-41d4-a716-446655440000"))); // example UUID
    }

    @Test
    void convertsStringToIdentityUsingNoConverter() {
        // given
        var anotherIdConverter  = factory.getConverter(AnotherId.class);
        //when then
        assertThat(anotherIdConverter.convert("jo"))
            .isEqualTo(new AnotherId("jo"));
    }

    @Test
    void convertsNull() {
        // given
        var anotherIdConverter  = factory.getConverter(AnotherId.class);
        //when then
        assertThat(anotherIdConverter.convert(null))
            .isEqualTo(null);
    }

}

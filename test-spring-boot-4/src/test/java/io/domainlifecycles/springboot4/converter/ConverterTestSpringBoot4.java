package io.domainlifecycles.springboot4.converter;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.convert.ConversionService;
import org.springframework.test.context.ActiveProfiles;
import tests.shared.converter.ConverterBigDecimalVo;
import tests.shared.converter.ConverterSimpleVo;
import tests.shared.converter.ConverterStringVo;
import tests.shared.converter.LongId;
import tests.shared.converter.StringId;
import tests.shared.converter.UuidId;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles({"test"})
public class ConverterTestSpringBoot4 {

    @Autowired
    private ConversionService conversionService;


    @Test
    public void pathParamIdString() {
        StringId sid = conversionService.convert("test", StringId.class);
        assertThat(sid).isEqualTo(new StringId("test"));
    }

    @Test
    public void pathParamIdLong() {
        LongId sid = conversionService.convert("12345", LongId.class);
        assertThat(sid).isEqualTo(new LongId(12345l));
    }

    @Test
    public void pathParamIdUuid() {
        UUID u = UUID.randomUUID();
        UuidId sid = conversionService.convert(u.toString(), UuidId.class);
        assertThat(sid).isEqualTo(new UuidId(u));
    }

    @Test
    public void pathParamValueObjectString() {
        ConverterStringVo sid = conversionService.convert("whoop", ConverterStringVo.class);
        assertThat(sid).isEqualTo(new ConverterStringVo("whoop"));
    }

    @Test
    public void pathParamSimpleValueObject() {
        ConverterSimpleVo sid = conversionService.convert("whoop", ConverterSimpleVo.class);
        assertThat(sid).isEqualTo(new ConverterSimpleVo("whoop"));
    }

    @Test
    public void pathParamBigDecimalValueObject() {
        ConverterBigDecimalVo sid = conversionService.convert("100", ConverterBigDecimalVo.class);
        assertThat(sid).isEqualTo(new ConverterBigDecimalVo(BigDecimal.valueOf(100l)));
    }

}

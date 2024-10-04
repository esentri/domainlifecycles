/*
 *
 *     ___
 *     │   ╲                 _
 *     │    ╲ ___ _ __  __ _(_)_ _
 *     |     ╲ _ ╲ '  ╲╱ _` │ │ ' ╲
 *     |_____╱___╱_│_│_╲__,_│_│_||_|
 *     │ │  (_)╱ _│___ __ _  _ __│ |___ ___
 *     │ │__│ │  _╱ -_) _│ ││ ╱ _│ ╱ -_|_-<
 *     │____│_│_│ ╲___╲__│╲_, ╲__│_╲___╱__╱
 *                      |__╱
 *
 *  Copyright 2019-2024 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       https://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package io.domainlifecycles.springboot3.converter;

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
public class ConverterTestSpringBoot3 {

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

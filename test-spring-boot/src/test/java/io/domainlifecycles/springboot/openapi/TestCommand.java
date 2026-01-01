package io.domainlifecycles.springboot.openapi;

import lombok.Value;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedComplexVo;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedSimpleVo;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedVoEntity;
import tests.shared.persistence.domain.valueobjectAutoMapping.AutoMappedVoEntityId;

import java.util.List;

@Value
public class TestCommand {

    private AutoMappedComplexVo complexVo;
    private AutoMappedSimpleVo simpleVo;
    private AutoMappedVoEntity voEntity;
    private AutoMappedVoEntityId voEntityId;
    private List<AutoMappedSimpleVo> voList;
    private List<AutoMappedVoEntity> entityList;
    private List<AutoMappedVoEntityId> idList;


}

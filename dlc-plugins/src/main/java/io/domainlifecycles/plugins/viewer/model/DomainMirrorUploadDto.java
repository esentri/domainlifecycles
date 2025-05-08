package io.domainlifecycles.plugins.viewer.model;

import io.domainlifecycles.mirror.api.DomainMirror;
import java.util.List;

public class DomainMirrorUploadDto {

    private final String domainMirrorJson;
    private final List<String> domainModelPackages;

    public DomainMirrorUploadDto(String domainMirrorJson, List<String> domainModelPackages) {
        this.domainMirrorJson = domainMirrorJson;
        this.domainModelPackages = domainModelPackages;
    }

    public String getDomainMirrorJson() {
        return domainMirrorJson;
    }

    public List<String> getDomainModelPackages() {
        return domainModelPackages;
    }
}

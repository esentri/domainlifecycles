package tests.mirror;


import io.domainlifecycles.domain.types.Identity;
import io.domainlifecycles.domain.types.base.EntityBase;

import java.util.List;
import java.util.Optional;

public class BaseEntityWithHidden<TEST> extends EntityBase<BaseEntityWithHidden.HiddenId> {

    public record HiddenId(Long value) implements Identity<Long> {
    }

    private String hiddenField = "hidden";

    private TEST inheritedGeneric;

    private List<TEST> inheritedGenericList;

    public String hidden() {
        return hiddenField;
    }

    public TEST showTestNotOverridden(TEST test) {
        return null;
    }

    public TEST showTestOverridden(TEST test) {
        return null;
    }

    public Optional<? extends TEST> showTestNotOverriddenOptionalWildcardUpperBound(Optional<? extends TEST> test) {
        return null;
    }

    public Optional<TEST[]> deliverArray() {
        return null;
    }

    ;

    @Override
    public void validate() {
        super.validate();
    }
}

package test.objectdispatch;

import io.domainlifecycles.domain.types.DomainService;

import java.util.List;

public class DescribingService implements DomainService {

    /** A call on {@code Object}: nothing tells which domain type it reaches. */
    public String describe(Object value) {
        return value.toString();
    }

    // a method reference on Object: nothing tells which domain type it reaches either
    public List<String> describeAll(List<Object> values) {
        return values.stream().map(Object::toString).toList();
    }

    // a method reference on a domain type
    public List<String> describeTags(List<Tag> tags) {
        return tags.stream().map(Tag::toString).toList();
    }

    /** A call on a domain type. */
    public String describeName(Name name) {
        return name.toString();
    }
}

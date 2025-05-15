package helper;

import io.domainlifecycles.domain.types.ValueObject;
import lombok.Builder;

@Builder(setterPrefix = "set")
public class TestValueObjectSameSetters implements ValueObject {

    public String first;
    public Long second;

    public void setFirst(String first) {
        this.first = first;
    }

    public void setSecond(Long second) {
        this.second = second;
    }
}

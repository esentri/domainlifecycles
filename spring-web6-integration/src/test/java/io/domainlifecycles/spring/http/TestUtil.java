package io.domainlifecycles.spring.http;

import org.springframework.beans.factory.ObjectProvider;

import java.util.stream.Stream;

public class TestUtil {

    static <T> ObjectProvider<T> providerOf(T value) {
        return new ObjectProvider<>() {
            @Override
            public Stream<T> stream() {
                return Stream.of(value);
            }
        };
    }

    static <T> ObjectProvider<T> emptyProvider() {
        return new ObjectProvider<>() {
            @Override
            public Stream<T> stream() {
                return Stream.empty();
            }
        };
    }
}

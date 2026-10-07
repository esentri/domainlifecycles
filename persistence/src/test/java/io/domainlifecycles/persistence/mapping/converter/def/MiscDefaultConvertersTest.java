package io.domainlifecycles.persistence.mapping.converter.def;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for the {@code Default*Converter} classes converting between {@link String} and {@link URI}/{@link
 * UUID}.
 */
public class MiscDefaultConvertersTest {

    @Test
    public void testStringToURI() {
        var converter = new DefaultStringToURIConverter();

        assertThat(converter.convert("https://domainlifecycles.io/docs"))
            .isEqualTo(URI.create("https://domainlifecycles.io/docs"));
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testURIToString() {
        var converter = new DefaultURIToStringConverter();
        var uri = URI.create("https://domainlifecycles.io/docs");

        assertThat(converter.convert(uri)).isEqualTo("https://domainlifecycles.io/docs");
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testStringToUuid() {
        var converter = new DefaultStringToUuidConverter();
        var uuid = UUID.randomUUID();

        assertThat(converter.convert(uuid.toString())).isEqualTo(uuid);
        assertThat(converter.convert(null)).isNull();
    }

    @Test
    public void testUuidToString() {
        var converter = new DefaultUuidToStringConverter();
        var uuid = UUID.randomUUID();

        assertThat(converter.convert(uuid)).isEqualTo(uuid.toString());
        assertThat(converter.convert(null)).isNull();
    }
}

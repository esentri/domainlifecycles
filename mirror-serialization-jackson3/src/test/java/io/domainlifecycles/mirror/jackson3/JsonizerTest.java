package io.domainlifecycles.mirror.jackson3;

import io.domainlifecycles.mirror.api.DomainMirrorFactory;
import io.domainlifecycles.mirror.serialize.jackson3.JacksonDomainSerializer;
import io.domainlifecycles.mirror.model.EntityModel;
import io.domainlifecycles.mirror.model.FieldModel;
import io.domainlifecycles.mirror.model.MethodModel;
import io.domainlifecycles.mirror.model.ProvidedDomain;
import io.domainlifecycles.mirror.reflect.ReflectiveDomainMirrorFactory;
import io.domainlifecycles.mirror.resolver.TypeMetaResolver;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FilterInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;


@Slf4j
public class JsonizerTest {

    @Test
    public void testJsonizeWithoutTypeMetaJackson3() {
        ReflectiveDomainMirrorFactory factory = new ReflectiveDomainMirrorFactory("tests");
        testJsonize(factory);
    }

    @Test
    public void testJsonizeWithTypeMetaJackson3() {
        ReflectiveDomainMirrorFactory factory = new ReflectiveDomainMirrorFactory( "tests");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        testJsonize(factory);
    }

    private void testJsonize(DomainMirrorFactory factory) {
        var serializer = new JacksonDomainSerializer(true);
        var dm = factory.initializeDomainMirror();
        var result = serializer.serialize(dm);
        log.info("Result:"+result);
        var init = serializer.deserialize(result);
        Assertions.assertThat(init.getAllBoundedContextMirrors()).isEqualTo(dm.getAllBoundedContextMirrors());
        Assertions.assertThat(init.getAllDomainTypeMirrors().size()).isEqualTo(dm.getAllDomainTypeMirrors().size());
        Assertions.assertThat(init.getAllDomainTypeMirrors()).isEqualTo(dm.getAllDomainTypeMirrors());

        Assertions.assertThat(init).isEqualTo(dm);
        Assertions.assertThat(init.getAllDomainTypeMirrors()
                .stream()
                .map(t -> (ProvidedDomain) t)
                .allMatch(ProvidedDomain::domainMirrorSet)).isTrue();
        Assertions.assertThat(init.getAllDomainTypeMirrors()
                .stream()
                .flatMap(dt-> dt.getAllFields().stream())
                .allMatch(t-> ((FieldModel)t).domainMirrorSet())).isTrue();
        Assertions.assertThat(init.getAllDomainTypeMirrors()
            .stream()
            .flatMap(dt-> dt.getMethods().stream())
            .allMatch(t-> ((MethodModel)t).domainMirrorSet())).isTrue();
        Assertions.assertThat(init.getAllDomainTypeMirrors()
            .stream()
            .filter(dt -> dt instanceof EntityModel)
            .map(dt ->(EntityModel) dt)
            .map(dt-> (FieldModel)dt.getIdentityField().orElse(null))
            .filter(Objects::nonNull)
            .allMatch(FieldModel::domainMirrorSet)).isTrue();
        var result2 = serializer.serialize(init);
        Assertions.assertThat(result).isEqualTo(result2);
    }

    @Test
    public void testJsonizeViaStreamsJackson3() {
        ReflectiveDomainMirrorFactory factory = new ReflectiveDomainMirrorFactory("tests");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        var serializer = new JacksonDomainSerializer(false);
        var dm = factory.initializeDomainMirror();

        var out = new ByteArrayOutputStream();
        serializer.serialize(dm, out);

        var deserialized = serializer.deserialize(new ByteArrayInputStream(out.toByteArray()));

        Assertions.assertThat(deserialized).isEqualTo(dm);
        // the stream based serialization produces exactly the same JSON as the String based one
        Assertions.assertThat(out.toString(StandardCharsets.UTF_8)).isEqualTo(serializer.serialize(dm));
    }

    /**
     * The stream based serialize/deserialize methods document that they leave closing the given
     * stream to the caller. Jackson closes a stream it was given by default once done with it, so
     * this has to be explicitly turned off - this test guards against that regressing silently
     * (a {@link ByteArrayOutputStream}/{@link ByteArrayInputStream}, as used in the test above, would
     * not catch this: closing them has no effect either way).
     */
    @Test
    public void streamsAreNotClosedByTheSerializer() throws IOException {
        ReflectiveDomainMirrorFactory factory = new ReflectiveDomainMirrorFactory("tests");
        factory.setGenericTypeResolver(new TypeMetaResolver());
        var serializer = new JacksonDomainSerializer(false);
        var dm = factory.initializeDomainMirror();

        var byteStream = new ByteArrayOutputStream();
        var trackingOut = new CloseTrackingOutputStream(byteStream);
        serializer.serialize(dm, trackingOut);
        Assertions.assertThat(trackingOut.closed).isFalse();

        var trackingIn = new CloseTrackingInputStream(new ByteArrayInputStream(byteStream.toByteArray()));
        serializer.deserialize(trackingIn);
        Assertions.assertThat(trackingIn.closed).isFalse();
    }

    private static final class CloseTrackingOutputStream extends FilterOutputStream {
        boolean closed = false;

        CloseTrackingOutputStream(java.io.OutputStream out) {
            super(out);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            out.write(b, off, len);
        }

        @Override
        public void close() throws IOException {
            closed = true;
        }
    }

    private static final class CloseTrackingInputStream extends FilterInputStream {
        boolean closed = false;

        CloseTrackingInputStream(java.io.InputStream in) {
            super(in);
        }

        @Override
        public void close() {
            closed = true;
        }
    }

}

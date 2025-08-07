package io.domainlifecycles.mirror.model;

import io.domainlifecycles.mirror.api.DomainMirror;
import io.domainlifecycles.mirror.api.DomainMirrorFactory;
import io.domainlifecycles.mirror.exception.MirrorException;
import io.domainlifecycles.mirror.serialize.api.DomainSerializer;
import io.domainlifecycles.mirror.serialize.api.JacksonDomainSerializer;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FileDomainMirrorFactory implements DomainMirrorFactory {

    public static final String META_INF_DLC_MIRROR_FILE_PATH = "META-INF/dlc/mirror.json";

    /**
     * Initializes the domain with the mirrors defined in a file under the {@code META-INF/dlc} directory.
     *
     * @return DomainMirror - a container for all mirrors that were read in the file.
     */
    @Override
    public DomainMirror initializeDomainMirror() {
        DomainSerializer domainSerializer = new JacksonDomainSerializer(false);
        String mirrorJson = readMirrorJsonFile();

        return domainSerializer.deserialize(mirrorJson);
    }

    public String readMirrorJsonFile() {
        try (InputStream inputStream = getClass().getClassLoader()
            .getResourceAsStream(META_INF_DLC_MIRROR_FILE_PATH)) {

            if (inputStream == null) {
                throw MirrorException.fail(
                    String.format("Could not find mirror file for serializing. Make sure file %s is present.",
                        META_INF_DLC_MIRROR_FILE_PATH));
            }

            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

        } catch (IOException e) {
            throw MirrorException.fail("Could not read mirror file.", e);
        }
    }
}

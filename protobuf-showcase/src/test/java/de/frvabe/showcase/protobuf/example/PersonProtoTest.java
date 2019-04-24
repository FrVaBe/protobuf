package de.frvabe.showcase.protobuf.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import de.frvabe.showcase.protobuf.example.company.Person;

/**
 * {@link Person} related unit tests.
 */
public class PersonProtoTest {

    @Test
    public void showDatamodel() {
        String descriptorString = Person.getDescriptor().toProto().toString();
        assertTrue(descriptorString.contains("name: \"Person\""));
        System.out.println(descriptorString);
    }

    @Test
    public void toProtoBuf() throws IOException {

        // given
        Person p = Person.newBuilder().setId(UUID.randomUUID().toString()).setName("Foo Bar").build();
        File personFile =
                new File(System.getProperty("user.dir") + "/target/test/person-" + System.currentTimeMillis() + ".bin");
        Files.createDirectories(Paths.get(personFile.getParent()));
        try (FileOutputStream os = new FileOutputStream(personFile)) {
            p.writeTo(os);
        }

        // when
        byte[] personBin;
        try (FileInputStream is = new FileInputStream(personFile)) {
            personBin = is.readAllBytes();
        }
        Person in = Person.parseFrom(personBin);

        // then
        assertEquals(in.getName(), p.getName());

    }

}

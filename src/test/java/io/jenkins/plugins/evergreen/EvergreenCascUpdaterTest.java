package io.jenkins.plugins.evergreen;

import org.apache.commons.codec.digest.DigestUtils;
import org.hamcrest.core.IsEqual;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class EvergreenCascUpdaterTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Test
    public void fixFileIfNeeded() throws IOException {
        // given
        final File file = folder.newFile();
        final Path filePath = file.toPath();
        Files.copy(EvergreenCascUpdaterTest.class.getResourceAsStream("/io/jenkins/plugins/evergreen/create-admin-user-to-replace.yaml"),
                   filePath,
                   StandardCopyOption.REPLACE_EXISTING);

        // When
        EvergreenCascUpdater.fixFileIfNeeded(filePath);

        // Then
        final byte[] data = Files.readAllBytes(filePath);
        assertThat(DigestUtils.sha1Hex(data), equalTo("7cae6193a5d4e837ffdc54fb96361a332b99df3c"));
    }
}

package io.jenkins.plugins.essentials.logging;

import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.assertj.core.api.Assertions.assertThat;

public class FileHandlerTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    private String fileEncodingBefore;

    /**
     * Implementation note: to test this, one *must* set -Dfile.encoding=ISO-8859-1 or anything that is not UTF-8.
     * Setting the file.encoding property at runtime has no effect.
     * <p>
     * So, the caveat here is that if this tests is always ever run on platforms with UTF-8 as default encoding, then it won't catch issues ever :-(.
     */
    @Test
    public void isUTF8Encoded() throws IOException, InterruptedException {

        // Force another platform encoding than UTF-8 to check the serialization is enforced in UTF-8 and things get deserialized correctly
        final File logsFolder = this.folder.newFolder();

        final String msg = "Héhé, comîn chô và, lØ.";
        final LogRecord record = new LogRecord(Level.SEVERE, msg);
        final File file = new File(logsFolder, "blah.log");
        final FileHandler fileHandler = EssentialsLoggingConfigurer.createFileHandler(file.getAbsolutePath());
        assertThat(fileHandler.isLoggable(record)).isTrue();
        fileHandler.publish(record);

        String content = FileUtils.readFileToString(new File(logsFolder, "blah.log.0"), "UTF-8");
        assertThat(content).contains(msg);
    }
}

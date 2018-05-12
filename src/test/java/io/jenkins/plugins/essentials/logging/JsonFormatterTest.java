package io.jenkins.plugins.essentials.logging;

import org.junit.Test;

import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.assertj.core.api.Assertions.assertThat;
public class JsonFormatterTest {

    @Test
    public void format() {

        final String msg = "the message\nand another line {0}";
        final LogRecord record = new LogRecord(Level.INFO, msg);
        record.setLoggerName("The name");
        record.setParameters(new String[]{"yay"});

        record.setThrown(new IllegalStateException());

        final String json = new JsonFormatter().format(record);

        assertThat(json).isNotBlank();
        assertThat(json).contains(msg.replaceAll("\n", "\\\\n")
                                          .replaceAll("\\{.*}", ""));
        assertThat(json).contains("yay");
        assertThat(json).contains("\"name\":\"The name\"");
    }
}
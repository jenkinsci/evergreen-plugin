package io.jenkins.plugins.essentials.logging;

import io.jenkins.plugins.essentials.logging.JsonFormatter;
import org.apache.commons.lang3.StringUtils;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.junit.Assert.*;

public class JsonFormatterTest {

    @Test
    public void format() {

        final String msg = "the message\nand another line";
        final LogRecord record = new LogRecord(Level.INFO, msg);
        record.setThrown(new IllegalStateException());

        final String json = new JsonFormatter().format(record);

        assertTrue(StringUtils.isNotBlank(json));
        assertThat(json, CoreMatchers.containsString(msg.replaceAll("\n", "\\\\n")));
    }
}
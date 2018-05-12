package io.jenkins.plugins.essentials.logging;

import net.sf.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.logging.Level;
import java.util.logging.LogRecord;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;

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

        final JSONObject jsonObject = JSONObject.fromObject(json);

        // explicit checks based on https://github.com/jenkinsci/jep/tree/master/jep/304#logging-format
        assertThat(jsonObject.get("version")).isNotNull();
        assertThat(jsonObject.get("timestamp")).isNotNull();
        assertThat(jsonObject.get("name")).isNotNull();
        assertThat(jsonObject.get("level")).isNotNull();
        assertThat(jsonObject.get("message")).isNotNull();

        final Object exception = jsonObject.get("exception");
        assertThat(exception).isNotNull();
        assertThat(json).contains(msg.replaceAll("\n", "\\\\n")
                                          .replaceAll("\\{.*}", ""));
        assertThat(json).contains("yay");
        assertThat(json).contains("\"name\":\"The name\"");
    }
}
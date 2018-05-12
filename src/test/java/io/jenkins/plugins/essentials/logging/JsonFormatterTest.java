package io.jenkins.plugins.essentials.logging;

import net.sf.json.JSONObject;
import org.junit.Test;

import java.util.Map;
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

        assertThat(json).endsWith("\n");
        // important to be valid JSON
        assertThat(json.substring(0, json.length() - 1)).doesNotContain("\n");

        final JSONObject jsonObject = JSONObject.fromObject(json);

        // explicit checks based on https://github.com/jenkinsci/jep/tree/master/jep/304#logging-format
        assertThat(jsonObject.get("version")).isNotNull();
        assertThat(jsonObject.get("timestamp")).isNotNull();
        assertThat(jsonObject.get("name")).isNotNull();
        assertThat(jsonObject.get("level")).isNotNull();

        final String message = (String) jsonObject.get("message");
        assertThat(message).isNotNull();
        assertThat(message).isEqualTo("the message\\nand another line yay");

        final JSONObject exception = (JSONObject) jsonObject.get("exception");
        assertThat((Map) exception).isNotNull();
        assertThat((String) exception.get("raw")).contains("IllegalStateException");

        assertThat(json).contains("\"name\":\"The name\"");
    }

    @Test
    public void formatWithoutException() {
        final LogRecord record = new LogRecord(Level.INFO, "whateva");

        final String json = new JsonFormatter().format(record);
        System.out.println(json);
        assertThat((Map<String, Object>) JSONObject.fromObject(json)).doesNotContainKey("exception");

    }
}
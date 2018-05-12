package io.jenkins.plugins.essentials.logging;

import hudson.Functions;
import net.sf.json.JSONObject;

import javax.annotation.Nonnull;
import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

public class JsonFormatter extends Formatter {

    SimpleFormatter messageFormatter = new SimpleFormatter();
    @Override
    public String format(LogRecord record) {
        JSONObject jsonLog = new JSONObject();

        jsonLog.put("version", 1);
        jsonLog.put("timestamp", record.getMillis());
        jsonLog.put("name", record.getLoggerName());
        jsonLog.put("level", record.getLevel().getName());
        jsonLog.put("message", messageFormatter.formatMessage(record));
        final Throwable thrown = record.getThrown();
        if (thrown != null) {
            jsonLog.put("exception", Functions.printThrowable(thrown));
        }
        return jsonLog.toString() + System.lineSeparator();
    }
}

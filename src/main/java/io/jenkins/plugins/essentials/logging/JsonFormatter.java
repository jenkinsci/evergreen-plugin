package io.jenkins.plugins.essentials.logging;

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

        jsonLog.put("timestamp", record.getMillis());
        jsonLog.put("level", record.getLevel().getName());
        jsonLog.put("message", messageFormatter.formatMessage(record));

        final Throwable thrown = record.getThrown();
        if (thrown != null) {
            jsonLog.put("exception", formatException(thrown));
        }
        return jsonLog.toString() + System.lineSeparator();
    }

    private String formatException(@Nonnull Throwable thrown) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println();
        thrown.printStackTrace(pw);
        pw.close();
        return sw.toString();
    }
}

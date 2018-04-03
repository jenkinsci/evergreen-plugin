package io.jenkins.plugins.essentials.logging;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hudson.init.InitMilestone;
import hudson.init.Initializer;
import hudson.triggers.SafeTimerTask;
import jenkins.model.Jenkins;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.FileHandler;
import java.util.logging.Filter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/**
 * Parses {@link Jenkins#logRecords} to push them to the
 */
public class EssentialsLogggingConfigurer {

    private static final Logger LOGGER = Logger.getLogger(EssentialsLogggingConfigurer.class.getName());

    @Initializer(after = InitMilestone.EXTENSIONS_AUGMENTED)
    public static void configure() throws Exception {

        // Lower Level, debugging hack
        LOGGER.log(Level.SEVERE, "I started!");

        if (Jenkins.logRecords.size() >= getMaxNumberOfLogs()) {
            LOGGER.severe("The Jenkins logs buffer is full. " +
                                  "Some logs might have been missed, and so many logs shows something probably very wrong!");
        }

        // FIXME: should I use the no-arg ctor to let usual sysprops be usable?
        FileHandler fileHandler = new FileHandler(getFilePattern(), 10 * 1000 * 1000, 5, false);
        fileHandler.setFormatter(new JsonFormatter());
        fileHandler.setFilter(record -> record.getLevel().intValue() >= Level.WARNING.intValue());

        Jenkins.logRecords.stream()
                .sorted(Collections.reverseOrder(Comparator.comparingInt(record1 -> record1.getLevel().intValue())))
                .forEach(fileHandler::publish);

        Logger.getLogger("").addHandler(fileHandler);
    }


    @SuppressFBWarnings("RV_RETURN_VALUE_IGNORED_BAD_PRACTICE")
    private static String getFilePattern() {
        final File logsRoot = SafeTimerTask.getLogsRoot();
        logsRoot.mkdir();
        return new File(logsRoot, "essentials.log.%g").getAbsolutePath();
    }

    /**
     * Returns the size of the logs buffer used behind .
     * <p>
     * FIXME: un-hardcode 256 to use the actual value used in {@link hudson.util.RingBufferLogHandler#records.size()}
     *
     * @return the size of the {@link Jenkins#logRecords} logs buffer.
     */
    private static int getMaxNumberOfLogs() {
        return 256;
    }

}

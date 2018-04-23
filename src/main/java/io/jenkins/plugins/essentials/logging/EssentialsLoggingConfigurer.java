package io.jenkins.plugins.essentials.logging;

import com.google.common.annotations.VisibleForTesting;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import hudson.WebAppMain;
import hudson.init.InitMilestone;
import hudson.init.Initializer;
import hudson.triggers.SafeTimerTask;
import jenkins.model.Jenkins;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Parses {@link Jenkins#logRecords} and  to push them to the
 */
@SuppressWarnings("unused")
public class EssentialsLoggingConfigurer {

    private static final Logger LOGGER = Logger.getLogger(EssentialsLoggingConfigurer.class.getName());

    @Initializer(after = InitMilestone.EXTENSIONS_AUGMENTED)
    public static void configure() throws Exception {

        // Lower Level, debugging hack
        LOGGER.log(Level.SEVERE, "I started!");

        checkNotTooManyLogsAlready();

        FileHandler fileHandler = createFileHandler();

        Jenkins.logRecords.stream()
                .sorted(Collections.reverseOrder(Comparator.comparingInt(record1 -> record1.getLevel().intValue())))
                .forEach(fileHandler::publish);

        Logger.getLogger("").addHandler(fileHandler);
    }

    private static FileHandler createFileHandler() throws IOException {
        return createFileHandler(getFilePattern());
    }

    @Restricted(NoExternalUse.class)
    @VisibleForTesting
    static FileHandler createFileHandler(String filePattern) throws IOException {

        // FIXME: should we use the no-arg ctor to let usual sysprops be usable?
        FileHandler fileHandler = new FileHandler(filePattern, 10 * 1000 * 1000, 5, true);
        fileHandler.setFormatter(new JsonFormatter());
        fileHandler.setFilter(record -> record.getLevel().intValue() >= Level.WARNING.intValue());
        fileHandler.setEncoding("UTF-8");
        return fileHandler;
    }

    /**
     * {@link Jenkins#logRecords} uses an underlying {@link hudson.util.RingBufferLogHandler} with a default size of 256 (at least until 2.114 included).
     * In our case, we use it only once to access the logs that were generated <strong>before</strong> the current plugin is started.
     * <p>
     * If that ring buffer is full, that already means per-se something is most likely unhealthy.
     * In normal conditions, there should be only like 20 log entries in that buffer when the plugin starts.
     */
    private static void checkNotTooManyLogsAlready() {
        final int logRecordsSize = Jenkins.logRecords.size();
        LOGGER.log(Level.INFO, "There are {0} log entries that were generated already.", logRecordsSize);

        final int maxNumberOfLogs = getMaxNumberOfLogs();
        if (logRecordsSize >= maxNumberOfLogs) {
            LOGGER.log(Level.SEVERE,
                       "The Jenkins logs buffer is already full. " +
                               "Some logs might have been missed, and so many logs shows something probably very wrong! (max={0})",
                       maxNumberOfLogs);
        }
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
     * FIXME: use the new non-reflective access method when JENKINS-50669 is done.
     *
     * @return the size of the {@link Jenkins#logRecords} logs buffer.
     */
    private static int getMaxNumberOfLogs() {
        try {
            final Field default_ring_buffer_size = WebAppMain.class.getDeclaredField("DEFAULT_RING_BUFFER_SIZE");
            default_ring_buffer_size.setAccessible(true);
            final int anInt = default_ring_buffer_size.getInt(null);
            return anInt;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            LOGGER.log(Level.SEVERE, "Unable to access WebAppMain.DEFAULT_RING_BUFFER_SIZE. Returning default value.");
            return 256;
        }
    }

}

package io.jenkins.plugins.evergreen;

import hudson.Extension;
import hudson.ExtensionList;
import hudson.PluginManager;
import hudson.model.AdministrativeMonitor;
import hudson.model.AsyncPeriodicWork;
import hudson.model.TaskListener;
import hudson.model.UpdateCenter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.stream.Stream;

@Extension
public class AdminMonitorsDeactivator extends AsyncPeriodicWork {

    private static final Logger LOGGER = Logger.getLogger(AdminMonitorsDeactivator.class.getName());

    public AdminMonitorsDeactivator() {
        super("Evergreen Administrative Monitor Deactivator");
    }

    protected AdminMonitorsDeactivator(String name) {
        super("Evergreen Administrative Monitor Deactivator");
    }

    @Override
    protected void execute(TaskListener listener) throws IOException, InterruptedException {
        // Evergreen handles updates on its own. Leaving messages show up on instances is misleading
        Stream.of(UpdateCenter.CoreUpdateMonitor.class, PluginManager.PluginUpdateMonitor.class).forEach(
                adminMonitorClazz -> safeDisable(adminMonitorClazz));

    }

    private void safeDisable(Class<? extends AdministrativeMonitor> adminMonitorClazz) {
        try {
            ExtensionList.lookupSingleton(adminMonitorClazz).disable(true);
        } catch (Exception e) {
            LogRecord record = new LogRecord(Level.WARNING, "Unable to disable {0}");
            record.setThrown(e);
            record.setParameters(new Object[]{adminMonitorClazz});
            LOGGER.log(record);
        }
    }

    /** Nothing should reenable this in theory, but doing this regularly shouldn't hurt */
    @Override
    public long getRecurrencePeriod() {
        return TimeUnit.MINUTES.toMillis(5);
    }
}

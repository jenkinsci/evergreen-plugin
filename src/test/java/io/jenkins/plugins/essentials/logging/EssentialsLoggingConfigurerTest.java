package io.jenkins.plugins.essentials.logging;

import hudson.Functions;
import hudson.triggers.SafeTimerTask;
import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;
import java.util.stream.Stream;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

public class EssentialsLoggingConfigurerTest {

    @Rule
    public JenkinsRule j = new JenkinsRule() {
        @Override
        public void after() throws Exception {
            if (Functions.isWindows()) {
                System.out.println("Closing loggers to avoid locking issue on Windows");
                Stream.of(Logger.getLogger("").getHandlers()).forEach(Handler::close);
            }
            super.after();
        }
    };

    @Test
    public void leaveBritneyAlone() throws Exception {
        System.out.println("Reading log files");
        Files.list(SafeTimerTask.getLogsRoot().toPath())
                .filter(path -> !path.endsWith(".lck"))
                .forEach(path -> {
                    try {
                        FileUtils.readLines(path.toFile()).stream()
                                .forEach(line -> {
                                    assertThat(line, containsString("message"));
                                    assertThat(line, containsString("level"));
                                });

                    } catch (IOException e) {
                        throw new IllegalStateException(e);
                    }
                });

    }
}
package io.jenkins.plugins.essentials.logging;

import hudson.triggers.SafeTimerTask;
import org.apache.commons.io.FileUtils;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import java.io.IOException;
import java.nio.file.Files;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;

public class EssentialsLoggingConfigurerTest {

    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Test
    public void leaveBritneyAlone() throws Exception {
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
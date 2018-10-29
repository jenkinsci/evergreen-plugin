package io.jenkins.plugins.evergreen;

import org.junit.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class EvergreenUpdateLevelPageDecoratorTest {
    @Test
    public void getUpdateLevel() {

        final File manifestFile = new File(getClass().getResource("manifest.json").getPath());
        assertThat(manifestFile).exists();
        assertThat(new EvergreenUpdateLevelPageDecorator(manifestFile).getUpdateLevel()).isEqualTo("2");
    }
}
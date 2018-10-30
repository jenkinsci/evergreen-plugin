package io.jenkins.plugins.evergreen;

import org.junit.Test;

import java.io.File;
import java.net.URL;

import static org.assertj.core.api.Assertions.assertThat;

public class EvergreenUpdateLevelPageDecoratorTest {
    @Test
    public void getUpdateLevel() {

        final URL resource = getClass().getResource("manifest.json");
        assertThat(resource).isNotNull();
        final File manifestFile = new File(resource.getPath());
        assertThat(manifestFile).exists();
        assertThat(new EvergreenUpdateLevelPageDecorator(manifestFile).getUpdateLevel()).isEqualTo("2");
    }
}

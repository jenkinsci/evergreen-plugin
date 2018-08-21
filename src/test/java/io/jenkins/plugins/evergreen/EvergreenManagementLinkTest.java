package io.jenkins.plugins.evergreen;

import hudson.ExtensionList;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class EvergreenManagementLinkTest {
    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Test
    public void jobTypes() {

        final ExtensionList<EvergreenManagementLink> extensionList = j.jenkins.getExtensionList(EvergreenManagementLink.class);

        assertThat(extensionList).hasSize(1);
        EvergreenManagementLink link = extensionList.get(0);

        final String[] topLevelDescriptors = link.getTopLevelDescriptors();
        assertThat(topLevelDescriptors).isNotEmpty();

        assertThat(Arrays.stream(topLevelDescriptors).noneMatch(descriptor -> !descriptor.contains("MockFolder"))).isTrue();

    }
}
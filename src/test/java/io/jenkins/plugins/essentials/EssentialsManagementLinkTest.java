package io.jenkins.plugins.essentials;

import hudson.ExtensionList;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import java.text.DecimalFormat;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class EssentialsManagementLinkTest {
    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Test
    public void jobTypes() {

        final ExtensionList<EssentialsManagementLink> extensionList = j.jenkins.getExtensionList(EssentialsManagementLink.class);

        assertThat(extensionList).hasSize(1);
        EssentialsManagementLink link = extensionList.get(0);

        final String[] topLevelDescriptors = link.getTopLevelDescriptors();
        assertThat(topLevelDescriptors).isNotEmpty();

        assertThat(Arrays.stream(topLevelDescriptors).noneMatch(descriptor -> !descriptor.contains("MockFolder"))).isTrue();

    }
}
package io.jenkins.plugins.essentials.filters;

import hudson.ExtensionList;
import hudson.model.FreeStyleProject;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import static org.assertj.core.api.Assertions.*;

public class FreestyleJobFilterTest {
    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Test
    public void noFreeStyle() throws ClassNotFoundException {
        final ExtensionList<?> extensionList = j.jenkins.getExtensionList(FreeStyleProject.DescriptorImpl.class);
        assertThat(extensionList).isEmpty();
    }

    @Test
    public void noEvilMaven() {
        final ExtensionList<?> extensionList = j.jenkins.getExtensionList(hudson.tasks.Maven.MavenInstallation.DescriptorImpl.class);
        assertThat(extensionList).isEmpty();
    }
}
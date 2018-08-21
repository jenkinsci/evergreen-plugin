package io.jenkins.plugins.evergreen.filters;

import hudson.model.TopLevelItemDescriptor;
import org.assertj.core.api.Assertions;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.JenkinsRule;

import java.util.List;
import java.util.stream.Collectors;

public class InessentialItemTypeFilterTest {
    @Rule
    public JenkinsRule j = new JenkinsRule();

    @Test
    public void noFreeStyleNorMaven() throws ClassNotFoundException {
        final List<String> foundTopLevelItems = j.jenkins.get().getExtensionList(TopLevelItemDescriptor.class).stream()
                .map(descriptor -> descriptor.getClass().getName())
                .collect(Collectors.toList());

        System.out.println(foundTopLevelItems);
        Assertions.assertThat(foundTopLevelItems).
                doesNotContain(
                        "hudson.model.FreeStyleProject$DescriptorImpl",
                        "hudson.maven.MavenModuleSet$DescriptorImpl"
                );
    }

}
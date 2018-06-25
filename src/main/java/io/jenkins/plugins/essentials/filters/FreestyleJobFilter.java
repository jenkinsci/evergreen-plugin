package io.jenkins.plugins.essentials.filters;

import hudson.Extension;
import hudson.ExtensionComponent;
import jenkins.ExtensionFilter;


@Extension
public class FreestyleJobFilter extends ExtensionFilter {

    @Override
    public <T> boolean allows(Class<T> type, ExtensionComponent<T> component) {
        if (type.getName().contains("hudson.model.FreeStyleProject$DescriptorImpl")) {
            return false;
        }
        if (type.getName().contains("hudson.tasks.Maven$MavenInstallation$DescriptorImpl")) {
            return false;
        }
        return true;
    }
}

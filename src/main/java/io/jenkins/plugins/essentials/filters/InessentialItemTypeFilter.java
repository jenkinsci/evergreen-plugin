package io.jenkins.plugins.essentials.filters;

import hudson.Extension;
import hudson.ExtensionComponent;
import jenkins.ExtensionFilter;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


@Extension
public class InessentialItemTypeFilter extends ExtensionFilter {

    private static final Logger LOGGER = Logger.getLogger(InessentialItemTypeFilter.class.getName());

    private static Set<String> COMPONENTS_TO_REMOVE = new LinkedHashSet<>();

    static {
        // FreeStyle jobs
        COMPONENTS_TO_REMOVE.add("hudson.model.FreeStyleProject$DescriptorImpl");

        // "Native" Maven jobs
        COMPONENTS_TO_REMOVE.add("hudson.maven.MavenModuleSet$DescriptorImpl");
    }

    @Override
    public <T> boolean allows(Class<T> type, ExtensionComponent<T> component) {
        Class componentType = component.getInstance().getClass();
        String componentTypeClassName = componentType.getName();

        for (String toRemove : COMPONENTS_TO_REMOVE) {
            if (componentTypeClassName.contains(toRemove)) {
                LOGGER.log(Level.WARNING, "Filtering out {0} from {1} type discovery",
                           new Object[]{componentTypeClassName, type});
                return false;
            }
        }
        return true;

    }
}

package io.jenkins.plugins.evergreen;

import hudson.Extension;
import hudson.model.Api;
import hudson.model.ManagementLink;
import hudson.model.TopLevelItemDescriptor;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

import javax.annotation.CheckForNull;

@ExportedBean
@Symbol("evergreen")
@Extension
public class EvergreenManagementLink extends ManagementLink {
    @CheckForNull
    @Override
    public String getIconFileName() {
        return "/plugin/evergreen/images/evergreen.svg";
    }

    @CheckForNull
    @Override
    public String getDisplayName() {
        return "Evergreen";
    }

    @CheckForNull
    @Override
    public String getUrlName() {
        return "evergreen";
    }

    public Api getApi() {
        return new Api(this);
    }

    @Exported(inline = true)
    public String[] getTopLevelDescriptors() {
        return TopLevelItemDescriptor.all().stream()
                .map(itemDescriptor -> itemDescriptor.getClass().getName())
                .toArray(String[]::new);
    }

}

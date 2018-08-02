package io.jenkins.plugins.essentials;

import hudson.Extension;
import hudson.model.Api;
import hudson.model.ManagementLink;
import hudson.model.TopLevelItemDescriptor;
import org.jenkinsci.Symbol;
import org.kohsuke.stapler.export.Exported;
import org.kohsuke.stapler.export.ExportedBean;

import javax.annotation.CheckForNull;

@ExportedBean
@Symbol("essentials")
@Extension
public class EssentialsManagementLink extends ManagementLink {
    @CheckForNull
    @Override
    public String getIconFileName() {
        return "/plugin/essentials/images/essentials.svg";
    }

    @CheckForNull
    @Override
    public String getDisplayName() {
        return "Essentials";
    }

    @CheckForNull
    @Override
    public String getUrlName() {
        return "essentials";
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

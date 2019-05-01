package io.jenkins.plugins.evergreen;


import com.google.common.annotations.VisibleForTesting;
import hudson.Extension;
import hudson.init.InitMilestone;
import hudson.init.Initializer;
import hudson.model.PersistentDescriptor;
import jenkins.model.GlobalConfiguration;
import org.apache.commons.codec.digest.DigestUtils;
import org.jenkinsci.Symbol;
import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is mostly a workaround to update config-as-code (JCasC) files on the disk.
 * <p>
 * E.g. since https://github.com/jenkinsci/jenkins/pull/3838, i.e. Jenkins 2.165, the remoting CLI has been removed from Jenkins Core.
 * This means that in JCasC, an attempt to configure it will simply fail, crashing the instance during startup.
 * <p>
 * In Evergreen, we don't a way to update the evergreen-client, or static files, yet. But we can update plugins.
 * So using the Evergreen Jenkins plugin, we detect these outdated files, and update them in place, so next update will not just crash
 * instances.
 */
@Extension @Symbol("remotingCLI")
@Restricted(NoExternalUse.class)
public class RemotingCLIEvergreenStub extends GlobalConfiguration implements PersistentDescriptor {

    private static final Logger LOGGER = Logger.getLogger(RemotingCLIEvergreenStub.class.getName());
    private static final String SHA1_BEFORE_CHANGE = "8d4ef50d006e24d26c4eaf0a9122a2dfc4f40dd9";

    @Initializer(after = InitMilestone.EXTENSIONS_AUGMENTED, before = InitMilestone.JOB_LOADED)
    public static void init() throws Exception {
        final Path createAdminUserFilePath = Paths.get(System.getenv("CASC_JENKINS_CONFIG"), "create-admin-user.yaml");
        fixFileIfNeeded(createAdminUserFilePath);
    }

    @VisibleForTesting
    static void fixFileIfNeeded(Path createAdminUserFilePath) throws IOException {
        if (createAdminUserFilePath.toFile().exists()) {
            byte[] fileContent = Files.readAllBytes(createAdminUserFilePath);
            String fileSha1 = DigestUtils.sha1Hex(fileContent);

            if (SHA1_BEFORE_CHANGE.equals(fileSha1)) {
                LOGGER.log(Level.WARNING, "Outdated config file detected, updating it with new version");
                Files.copy(RemotingCLIEvergreenStub.class.getResourceAsStream(
                        "/io/jenkins/plugins/evergreen/create-admin-user-without-remotingCLI.yaml"),
                           createAdminUserFilePath,
                           StandardCopyOption.REPLACE_EXISTING);
            }
        } else {
            LOGGER.log(Level.WARNING, "JCasC file not found, something is very very wrong");
        }
    }

}

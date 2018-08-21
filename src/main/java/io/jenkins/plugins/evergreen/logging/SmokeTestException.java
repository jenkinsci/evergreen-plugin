package io.jenkins.plugins.evergreen.logging;

import org.kohsuke.accmod.Restricted;
import org.kohsuke.accmod.restrictions.NoExternalUse;

@Restricted(NoExternalUse.class)
class SmokeTestException extends Exception {

    SmokeTestException() {
        super("Please ignore this exception");
    }
}

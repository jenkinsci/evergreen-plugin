package io.jenkins.plugins.evergreen;

import com.google.common.annotations.VisibleForTesting;
import hudson.Extension;
import hudson.model.PageDecorator;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import net.sf.json.groovy.JsonSlurper;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;


@Extension
public class EvergreenUpdateLevelPageDecorator extends PageDecorator {

    private static final Logger LOGGER = Logger.getLogger(EvergreenUpdateLevelPageDecorator.class.getName());
    private File manifest;

    @VisibleForTesting
    EvergreenUpdateLevelPageDecorator(File manifestFilePath) {
        this.manifest = manifestFilePath;
    }

    public EvergreenUpdateLevelPageDecorator() {
        final String evergreenData = System.getenv("EVERGREEN_DATA");
        manifest = new File(evergreenData, "updates.json");
    }

    public String getUpdateLevel() {
        String updateLevel = "N/A";

        if (manifest.exists()) {
            try {
                final JSONObject parsedJson = (JSONObject) new JsonSlurper().parse(manifest);
                updateLevel = "" + ((JSONObject) parsedJson.get("meta")).get("level");
            } catch (IOException e) {
                updateLevel = "Unable to parse: " + e.getMessage();
            } catch (JSONException e) {
                updateLevel = "Corrupted manifest: " + e.getMessage();
            }
        } else {
            LOGGER.warning("No Evergreen manifest found!");
        }
        return updateLevel;
    }
}

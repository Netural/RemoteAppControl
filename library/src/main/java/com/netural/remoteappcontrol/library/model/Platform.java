package com.netural.remoteappcontrol.library.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by stephan.schober on 27.05.15.
 */
public class Platform {

    private String minVersion;
    private int minVersionCode;
    private String currentVersion;
    private int currentVersionCode;

    private HashMap<String, VersionDialogLanguage> languages = new HashMap<>();

    public static Platform from(JSONObject jsonObject) throws JSONException {

        Platform platform = new Platform();

        if (jsonObject.has("minVersion")) {
            platform.setMinVersion(jsonObject.getString("minVersion"));
        }

        if (jsonObject.has("minVersionCode")) {
            platform.setMinVersionCode(jsonObject.getInt("minVersionCode"));
        }

        if (jsonObject.has("currentVersion")) {
            platform.setCurrentVersion(jsonObject.getString("currentVersion"));
        }

        if (jsonObject.has("currentVersionCode")) {
            platform.setCurrentVersionCode(jsonObject.getInt("currentVersionCode"));
        }

        if (jsonObject.has("languages")) {
            JSONArray jsonArrayLanguages = jsonObject.getJSONArray("languages");
            for (int i = 0; i < jsonArrayLanguages.length(); i++) {
                JSONObject jsonObjectLanguage = jsonArrayLanguages.getJSONObject(i);

                VersionDialogLanguage versionDialogLanguage = VersionDialogLanguage
                        .from(jsonObjectLanguage);
                platform.getLanguages()
                        .put(versionDialogLanguage.getLanguageCode(), versionDialogLanguage);
            }
        }

        return platform;
    }

    public String getMinVersion() {
        return minVersion;
    }

    public void setMinVersion(String minVersion) {
        this.minVersion = minVersion;
    }

    public int getMinVersionCode() {
        return minVersionCode;
    }

    public void setMinVersionCode(int minVersionCode) {
        this.minVersionCode = minVersionCode;
    }

    public String getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(String currentVersion) {
        this.currentVersion = currentVersion;
    }

    public int getCurrentVersionCode() {
        return currentVersionCode;
    }

    public void setCurrentVersionCode(int currentVersionCode) {
        this.currentVersionCode = currentVersionCode;
    }

    public HashMap<String, VersionDialogLanguage> getLanguages() {
        return languages;
    }

    public void setLanguages(
            HashMap<String, VersionDialogLanguage> languages) {
        this.languages = languages;
    }
}

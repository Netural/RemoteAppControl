package com.netural.remoteappcontrol.library.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by stephan.schober on 27.05.15.
 */
public class VersionDialogLanguage {

    private String languageCode;
    private String url;
    private String title;
    private String message;

    public static VersionDialogLanguage from(JSONObject jsonObject) throws JSONException {
        VersionDialogLanguage versionDialogLanguage = new VersionDialogLanguage();

        if (jsonObject.has("language")) {
            versionDialogLanguage.setLanguageCode(jsonObject.getString("language"));
        }

        if (jsonObject.has("title")) {
            versionDialogLanguage.setTitle(jsonObject.getString("title"));
        }

        if (jsonObject.has("message")) {
            versionDialogLanguage.setMessage(jsonObject.getString("message"));
        }

        if (jsonObject.has("url")) {
            versionDialogLanguage.setUrl(jsonObject.getString("url"));
        }

        return versionDialogLanguage;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

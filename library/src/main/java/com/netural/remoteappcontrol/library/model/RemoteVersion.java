package com.netural.remoteappcontrol.library.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by stephan.schober on 27.05.15.
 */
public class RemoteVersion {

    private Platform androidPlatform;

    public static RemoteVersion from(String json) throws JSONException {
        JSONObject jObject = new JSONObject(json);
        RemoteVersion remoteVersion = new RemoteVersion();

        if (jObject.has("android")) {
            remoteVersion.setAndroidPlatform(Platform.from(jObject.getJSONObject("android")));
        }

        return remoteVersion;
    }

    public Platform getAndroidPlatform() {
        return androidPlatform;
    }

    public void setAndroidPlatform(
            Platform androidPlatform) {
        this.androidPlatform = androidPlatform;
    }
}

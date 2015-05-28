package com.netural.remoteappcontrol.library;

import com.netural.remoteappcontrol.library.model.Platform;
import com.netural.remoteappcontrol.library.model.RemoteVersion;
import com.netural.remoteappcontrol.library.model.VersionDialogLanguage;
import com.netural.remoteappcontrol.library.network.NetworkUtils;

import org.json.JSONException;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;

/**
 * Created by stephan.schober on 26.05.15.
 */
public class RemoteAppControl {

    // the name of the shared preferences, can be accessed to be stored with BackupHelper
    public static final String REMOTEAPPCONTROL_PREFS_NAME = "remote_app_control_prefs";
    private static final String TAG = RemoteAppControl.class.getSimpleName();
    private static final String PREF_LAST_CHANGE = "pref_last_change";
    private static final String PREF_LAST_JSON = "pref_last_json";

    private String remoteUrl;
    private Context ctx;
    private SharedPreferences sharedPreferences;
    private int currentVersionCode;

    // the default style of the dialog
    private int style = R.style.Theme_AppCompat_Dialog_Alert;
    private CloseAppListener listener;

    /**
     * Initialize RemoteAppControl with these params
     *
     * @param activity           the activity where the dialog should be shown
     * @param currentVersionCode the current version code of your app, received with
     *                           BuildConfig.VERSION_CODE
     * @param url                the url where the json file is stored
     * @param listener           a callback listner for closing the app
     */
    public RemoteAppControl(Activity activity, int currentVersionCode, String url,
                            CloseAppListener listener) {
        this.ctx = activity;
        this.currentVersionCode = currentVersionCode;
        this.sharedPreferences = ctx.getSharedPreferences(REMOTEAPPCONTROL_PREFS_NAME, 0);
        this.remoteUrl = url;
        this.listener = listener;
    }

    /**
     * Set your own style for the dialog, for more info see AppCompat AlertDialog
     */
    public RemoteAppControl withStyle(int style) {
        this.style = style;
        return this;
    }

    /**
     * Launch the remote version check
     */
    public void check() {
        new RemoteVersionCheckAsyncTask().execute();
    }

    /**
     * Show the update dialog in the declared activity
     *
     * @param title       dialog title
     * @param message     dialog message
     * @param updateUrl   the url the user is redirected to
     * @param dismissable can the user cancel the dialog
     */
    private void showDialog(String title, String message, final String updateUrl,
                            boolean dismissable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx,
                style);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ctx.startActivity(new Intent(Intent.ACTION_VIEW, Uri
                        .parse(updateUrl)));
            }
        });
        if (dismissable) {
            builder.setNegativeButton(android.R.string.cancel, null);
        } else {
            builder.setCancelable(false);
            builder.setNegativeButton(android.R.string.cancel,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            if (listener != null) {
                                listener.onCloseApp();
                            }
                            ctx = null;
                        }
                    });
        }
        builder.show();
    }

    /**
     * Listener which has to be implemented to close the app if update is forced
     */
    public interface CloseAppListener {

        void onCloseApp();
    }

    public class RemoteVersionCheckAsyncTask extends AsyncTask<Void, Integer, RemoteVersion> {

        @Override
        protected RemoteVersion doInBackground(Void... params) {

            // check when the remote json file has been updated
            long lastServerChange = 0;
            try {
                long starttime = System.currentTimeMillis();
                Date lastUpDate = new Date();
                lastServerChange = NetworkUtils.checkLastUpdate(remoteUrl);
                lastUpDate.setTime(lastServerChange);
                Log.i(TAG, "checked last change for " + remoteUrl + ": " + lastUpDate.toString()
                        + " in " + (System.currentTimeMillis() - starttime) + " ms");
            } catch (IOException e) {
                Log.w(TAG, "could not check last change date", e);
                return null;
            }

            long lastChange = sharedPreferences.getLong(PREF_LAST_CHANGE, 0);

            String json = null;
            // is the remote version newer?
            if (lastServerChange <= lastChange) {
                Log.i(TAG, "trying to read remote version from shared preferences");
                // read json from shared preferences
                json = sharedPreferences.getString(PREF_LAST_JSON, null);
            } else {
                // load json from url
                try {
                    long starttime = System.currentTimeMillis();
                    json = NetworkUtils.fetchUrl(remoteUrl);

                    Log.i(TAG, "fetched and parsed version info in " + (System.currentTimeMillis()
                            - starttime) + " ms");
                } catch (IOException e) {
                    Log.w(TAG, "could not fetch remote version", e);
                    return null;
                }

                // update shared preferences
                sharedPreferences.edit().putLong(PREF_LAST_CHANGE, lastServerChange).commit();
                sharedPreferences.edit().putString(PREF_LAST_JSON, json).commit();
            }

            if (json == null) {
                return null;
            }

            RemoteVersion remoteVersion = null;
            try {
                // parse json file
                remoteVersion = RemoteVersion.from(json);
            } catch (JSONException e) {
                Log.w(TAG, "could not parse remote version", e);
                return null;
            }

            return remoteVersion;
        }

        @Override
        protected void onPostExecute(RemoteVersion remoteVersion) {
            if (remoteVersion == null) {
                return;
            }

            Platform androidPlatform = remoteVersion.getAndroidPlatform();
            VersionDialogLanguage versionDialogLanguage = null;

            // check if current user language is available, otherwise use "en"
            if (androidPlatform.getLanguages().containsKey(Locale.getDefault().getLanguage())) {
                versionDialogLanguage = androidPlatform.getLanguages()
                                                       .get(Locale.getDefault().getLanguage());
            } else if (androidPlatform.getLanguages().containsKey(Locale.US.getLanguage())) {
                versionDialogLanguage = androidPlatform.getLanguages()
                                                       .get(Locale.US.getLanguage());
            }

            boolean showUpdate = currentVersionCode < androidPlatform.getCurrentVersionCode();
            boolean forceUpdate = currentVersionCode < androidPlatform.getMinVersionCode();

            if (versionDialogLanguage != null && showUpdate) {
                showDialog(versionDialogLanguage.getTitle(), versionDialogLanguage.getMessage(),
                        versionDialogLanguage.getUrl(), !forceUpdate);
            }
        }
    }
}

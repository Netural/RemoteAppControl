package com.netural.remoteappcontrol.library;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;

/**
 * Created by stephan.schober on 26.05.15.
 */
public class RemoteAppControl {

    private String remoteUrl;
    private Activity hostActivity;
    private int style;

    public RemoteAppControl(Activity activity, String url) {
        this.hostActivity = activity;
        this.remoteUrl = url;
    }

    public RemoteAppControl withStyle(int style) {
        this.style = style;
        return this;
    }

    public void check() {
        showDialog("test", "pls update now!",
                "https://play.google.com/store/apps/details?id=com.whatsapp", false);
    }

    private void showDialog(String title, String message, final String updateUrl,
                            boolean dismissable) {
        AlertDialog.Builder builder = new AlertDialog.Builder(hostActivity,
                style);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                hostActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri
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
                            hostActivity.finish();
                        }
                    });
        }
        builder.show();
    }
}

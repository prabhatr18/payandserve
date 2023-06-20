package com.payment.aeps.permission;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;

public abstract class PermissionHandler {

    public abstract void onGranted();

    public void onDenied(Context context, ArrayList<String> deniedPermissions) {
        if (AppPermissions.loggingEnabled) {
            StringBuilder builder = new StringBuilder();
            builder.append("Denied:");
            for (String permission : deniedPermissions) {
                builder.append(" ");
                builder.append(permission);
            }
            AppPermissions.log(builder.toString());
        }
        Toast.makeText(context, "Permission Denied.", Toast.LENGTH_SHORT).show();
    }

    @SuppressWarnings("UnusedParameters")
    public boolean onBlocked(Context context, ArrayList<String> blockedList) {
        if (AppPermissions.loggingEnabled) {
            StringBuilder builder = new StringBuilder();
            builder.append("Set not to ask again:");
            for (String permission : blockedList) {
                builder.append(" ");
                builder.append(permission);
            }
            AppPermissions.log(builder.toString());
        }
        return false;
    }


    public void onJustBlocked(Context context, ArrayList<String> justBlockedList,
                              ArrayList<String> deniedPermissions) {
        if (AppPermissions.loggingEnabled) {
            StringBuilder builder = new StringBuilder();
            builder.append("Just set not to ask again:");
            for (String permission : justBlockedList) {
                builder.append(" ");
                builder.append(permission);
            }
            AppPermissions.log(builder.toString());
        }
        onDenied(context, deniedPermissions);
    }

}
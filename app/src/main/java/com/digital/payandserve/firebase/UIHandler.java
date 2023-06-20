package com.digital.payandserve.firebase;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

public class UIHandler extends Handler {
    public static final int DISPLAY_UI_TOAST = 0;
    public static final int DISPLAY_UI_DIALOG = 1;
    private Context context;
    private String title;
    private String body;

    public UIHandler(Looper looper, Context context, String title, String body) {
        super(looper);
        this.context = context;
        this.title = title;
        this.body = body;
    }

    @Override
    public void handleMessage(Message msg) {
//        AlertDialog.Builder alertDialog = new MaterialAlertDialogBuilder(context, R.style.AppTheme);
//        alertDialog.setTitle(title);
//        alertDialog.setCancelable(false);
//        alertDialog.setMessage(body);
//        alertDialog.setIcon(R.drawable.app_logo);
//        alertDialog.setPositiveButton("Close", (dialog, which) -> {
//            dialog.dismiss();
//        });
//        alertDialog.show();

        Toast.makeText(context, "", Toast.LENGTH_SHORT).show();
    }
}
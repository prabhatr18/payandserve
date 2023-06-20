package com.digital.payandserve.firebase;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class NotificationDialog extends AppCompatActivity {

    public static String title;
    public static String message;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle(title);
        alertDialog.setCancelable(true);
        alertDialog.setMessage(message);
        alertDialog.setPositiveButton("OK", (dialog, which) -> {
            dialog.dismiss();
            NotificationDialog.this.finish();
        });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}

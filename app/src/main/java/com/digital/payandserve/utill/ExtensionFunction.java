package com.digital.payandserve.utill;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Calendar;

public class ExtensionFunction {

    public static void showToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }


    public static void checkValidation(EditText editText,String msg){
        editText.requestFocus();
        editText.setError(msg);
    }


    public static void showLog(String TAG, String message) {
        Log.d(TAG, message);
    }

    public static <T> void startActivity(Context context, Class<T> destanationActivity) {
        context.startActivity(new Intent(context, destanationActivity));
    }

    public static void changeBtnBg(Button btn,Drawable drawable,int textColor){
        btn.setBackground(drawable);
        btn.setTextColor(textColor);
    }

    public static void showHideView(View showView,View hideView){
        showView.setVisibility(View.VISIBLE);
        hideView.setVisibility(View.GONE);
    }

    public static void changeTint(Button btn, int bgColor, int textColor) {
        Drawable buttonDrawable = btn.getBackground();
        buttonDrawable = DrawableCompat.wrap(buttonDrawable);
        //the color is a direct color int and not a color resource
        DrawableCompat.setTint(buttonDrawable, bgColor);
        btn.setBackground(buttonDrawable);
        btn.setTextColor(textColor);
    }

    public static void setLayoutManager(RecyclerView recyclerView, Context context){
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    public static void setGridLayoutManager(RecyclerView recyclerView, Context context,int count){
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(context, count);
        recyclerView.setLayoutManager(layoutManager);
    }


    public static void datePicker(Context context, EditText editText){

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(context,new DatePickerDialog.OnDateSetListener(){

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                editText.setText(dayOfMonth+" - "+ (month+1)+" - "+year);
            }
        },year,month,day);

        datePickerDialog.show();


    }



    public static void openMessageDialog(Context context,String msg){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setMessage(msg);
//        builder.setTitle("this is title");

        //Setting message manually and performing action on button click
        //This will not allow to close dialogbox until user selects an option
        builder.setCancelable(false);

        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //  Action for 'NO' Button
                dialog.cancel();
            }
        });

        //Creating dialog box
        AlertDialog alert = builder.create();
        //Setting the title manually
        //alert.setTitle("AlertDialogExample");
        alert.show();
    }

    public static void openBrowser(String url,Context context) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }

    public static void openFacebook(String url,Context context) {
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        String facebookUrl = getFacebookPageURL(context);
        facebookIntent.setData(Uri.parse(facebookUrl));
        context.startActivity(facebookIntent);
    }

    public static String getFacebookPageURL(Context context) {
         String FACEBOOK_URL = "https://www.facebook.com/PayandServe";
         String FACEBOOK_PAGE_ID = "PayandServe";

        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;

            boolean activated = packageManager.getApplicationInfo("com.facebook.katana", 0).enabled;
            if (activated) {
                if ((versionCode >= 3002850)) {
                    return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
                } else {
                    return "fb://page/" + FACEBOOK_PAGE_ID;
                }
            } else {
                return FACEBOOK_URL;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL;
        }

    }


    public static void sendWhatsappMessage(String number,Context context) {
        try {
            number = number.replace(" ", "");
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(number) + "@s.whatsapp.net");
            context.startActivity(sendIntent);
        } catch (Exception e) {
            Log.e("TAG", "ERROR_OPEN_MESSANGER" + e.toString());
        }
    }

    public static void sendEmail(String mail,Context context) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + mail));
          /*  intent.putExtra(Intent.EXTRA_SUBJECT, "Refferal Code");
            intent.putExtra(Intent.EXTRA_TEXT, ReferCode);*/
            context.startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(context, "Sorry...You don't have any mail app", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    public static void callNumber(String number,Context context) {

        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + number));
        context.startActivity(callIntent);
    }

    public static void transparentStatusBar(Activity activity) {
        try {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

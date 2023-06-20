package com.digital.payandserve.app;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.ViewPropertyTransition;
import com.digital.payandserve.R;
import com.digital.payandserve.network.VolleyNetworkCall;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.utill.PrefLoginManager;
import com.digital.payandserve.utill.Print;
import com.digital.payandserve.utill.SharedPrefs;
import com.digital.payandserve.views.auth.LoginActivity;

import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AppManager {

    private static int formArraySize = 0;
    private static int imageArraySize = 0;

    private static Activity activity;
    private static Context context;
    private static ProgressDialog loading;

    private static AppManager appManager;

    public String lossType;
    public int tabPosition = 0;

    public ArrayList<String> filteredForm;
    //For Summary
    int listviewPosition = -1;

    private AppManager() {
    }

    public static AppManager getInstance() {
        if (appManager == null)
            appManager = new AppManager();
        return appManager;
    }

    public static boolean isOnline(Context context) {
        boolean isConnected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            isConnected = cm.getActiveNetworkInfo().isConnected();
        } catch (Exception ex) {
            isConnected = false;
        }
        return isConnected;
    }


    public static void loadImage(final Context context, ImageView imageView, String url) {
        if (context == null || ((Activity) context).isDestroyed()) return;
        ViewPropertyTransition.Animator animationObject = new ViewPropertyTransition.Animator() {
            @Override
            public void animate(View view) {
                view.setAlpha(0f);
                ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
                fadeAnim.setDuration(1000);
                fadeAnim.start();
            }
        };

        RequestOptions options = new RequestOptions()
                //.placeholder(context.getResources().getDrawable(R.drawable.loading_bg))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .timeout(60000)
                .error(context.getResources().getDrawable(R.drawable.image1));


        Glide.with(context) //passing context
                .load(url) //passing your url to load image.
                .transition(GenericTransitionOptions.with(animationObject))
                .apply(options)
                .into(imageView);
    }

    public Uri createURI(String path, Context context) {
        File file = new File(path);
        Uri outputFileUri = null;
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)) {
            outputFileUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".provider", file);
        } else {
            outputFileUri = Uri.fromFile(file);
        }
        return outputFileUri;
    }

    public void logoutFromServer(Activity context, boolean isLoad) {
        new PrefLoginManager(context).setFarmerLoginRes("");
        String t = SharedPrefs.getValue(context, SharedPrefs.APP_TOKEN);
        String u = SharedPrefs.getValue(context, SharedPrefs.USER_ID);
        SharedPrefs.clearAllPreferences(context);
        Intent i = new Intent(context, LoginActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        context.startActivity(i);
        networkCallUsingVolleyApi(context, isLoad, t, u);
        Toast.makeText(context, "Session Cleared", Toast.LENGTH_SHORT).show();
    }

    private void networkCallUsingVolleyApi(Activity context, boolean isLoad, String t, String u) {
        if (AppManager.isOnline(context)) {
            new VolleyNetworkCall(context, Constants.URL.LOGOUT_USER, 1, param(context, t, u), isLoad).netWorkCall();
        } else {
            Print.P("Network connection error");
        }
    }

    private Map<String, String> param(Activity context, String t, String u) {
        Map<String, String> map = new HashMap<>();
        if (MyUtil.isNN(t) && MyUtil.isNN(u)) {
            map.put("apptoken", t);
            map.put("user_id", u);
        }
        Print.P("PARAMS: " + new JSONObject(map).toString());
        return map;
    }

    @SuppressLint("QueryPermissionsNeeded")
    private void shareLog(Context context) {
        try {
            File logFile2 = new File(getFiles());
            if (logFile2.exists()) {
                Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                emailIntent.setType("vnd.android.cursor.dir/email");
                String[] to = {"sumitkumarx95@gmail.com"};
                emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
                ArrayList<Uri> outputFileUri = new ArrayList<Uri>();
                if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)) {

                    if (logFile2.exists()) {
                        outputFileUri.add(FileProvider.getUriForFile(context, context.getPackageName() + ".files", logFile2));
                    }//  outputFileUri.add
                } else {

                    if (logFile2.exists()) {
                        outputFileUri.add(Uri.fromFile(logFile2));
                    }
                }
                emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, outputFileUri);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "App Error log");
                if (emailIntent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(Intent.createChooser(emailIntent, "Send email..."));
                } else {
                    Toast.makeText(context, "No email application is available to share error log file", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(context, "Log files are not existed", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    public void shareReportPdf(Context mContext, File currentReportPdf) {
        try {
            if (currentReportPdf != null && currentReportPdf.getParent() != null) {
                Toast.makeText(mContext, R.string.share_pdf, Toast.LENGTH_SHORT).show();
                if (currentReportPdf.exists()) {
                    Intent emailIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                    emailIntent.setType("vnd.android.cursor.dir/email");
                    String[] to = {""};
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
                    ArrayList<Uri> outputFileUri = new ArrayList<>();
                    if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)) {
                        if (currentReportPdf.exists()) {
                            outputFileUri.add(FileProvider.getUriForFile(mContext,
                                    mContext.getPackageName() + ".files", currentReportPdf));
                        }
                    } else {
                        if (currentReportPdf.exists()) {
                            outputFileUri.add(Uri.fromFile(currentReportPdf));
                        }
                    }
                    emailIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, outputFileUri);
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "App Error log");
                    if (emailIntent.resolveActivity(mContext.getPackageManager()) != null) {
                        mContext.startActivity(Intent.createChooser(emailIntent, mContext.getString(R.string.share_report)));
                    } else {
                        Toast.makeText(mContext, R.string.no_application, Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                Toast.makeText(mContext, R.string.no_report, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getFiles() {
        File sd = new File(Environment.getExternalStorageDirectory() + "/SEC2PAY/LOG/");
        File[] sdFileList = sd.listFiles();
        if (sdFileList != null && sdFileList.length > 1) {
            Arrays.sort(sdFileList, (object1, object2) -> (int) (Math.min(object2.lastModified(), object1.lastModified())));
            return sdFileList[sdFileList.length - 1].getAbsolutePath();
        } else if (sdFileList != null && sdFileList.length == 1) {
            return sdFileList[sdFileList.length - 1].getAbsolutePath();
        } else {
            return "";
        }
    }


    public void logoutApp(Activity context) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Authentication!");

            builder.setMessage("Your session has been expired.");
            builder.setNegativeButton("Login", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    dialog.dismiss();
                    //Toast.makeText(context, "Thanks", Toast.LENGTH_SHORT).show();
                    logoutFromServer(context);
                }
            });

            AlertDialog alert = builder.create();
            alert.setCancelable(false);
            alert.show();
        } catch (Exception e) {
            Toast.makeText(context, "Getting exception in logout process", Toast.LENGTH_SHORT).show();
            AppManager.appendLog("Logout Error : " + Constants.SIMPLE_DATE_FORMAT.format(new Date()));
            AppManager.appendLog(e.toString());
            e.printStackTrace();
        }
    }


    public static void appendLog(String text) {
        // Create an log file name with date
        String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        String FileName = "AppLog_" + timeStamp + ".txt";
        File myDirectory = new File(Environment.getExternalStorageDirectory(), "/SECURE_PAYMENT/LOG/");

        if (!myDirectory.exists()) {
            myDirectory.mkdirs();
        }
        File logFile = new File(Environment.getExternalStorageDirectory(), "/SECURE_PAYMENT/LOG/" + FileName);
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void logoutFromServer(Activity context) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setMessage("Please wait...");
        dialog.show();
        dialog.setCancelable(false);
        String token = SharedPrefs.getValue(context, SharedPrefs.APP_TOKEN);
        String id = SharedPrefs.getValue(context, SharedPrefs.USER_ID);
        String apiUrl = Constants.URL.BASE_URL + "api/android/auth/logout?apptoken=" + token + "&user_id=" + id;

        new PrefLoginManager(context).setFarmerLoginRes("");
        SharedPrefs.clearAllPreferences(context);

        //Log.e("Log", "url" + apiUrl);
        StringRequest sendRequest = new StringRequest(
                Request.Method.GET,
                apiUrl,
                result -> {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    Print.P("Volley Form Response : " + result);
                    try {
                        Toast.makeText(context, "session cleared successfully", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(context, LoginActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                                Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        context.startActivity(i);
                    } catch (Exception e) {
                        Toast.makeText(context, "Exception in session clearing process", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                },
                volleyError -> {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                    //Toast.makeText(context, "Getting error in logout api ", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(context, LoginActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |
                            Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    context.startActivity(i);
                    volleyError.printStackTrace();
                    Print.P("Not able to connect with server");
                });
        AppController.getInstance().addToRequestQueue(sendRequest);
    }


    public int dpToPx(int dp, Context context) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

}


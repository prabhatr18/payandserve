package com.payment.aeps.util;

import android.util.Log;

import com.payment.aeps.BuildConfig;

public class Print {

    static int count = 0;

    Print() {
    }

    public static void P(String sb) {
        if (BuildConfig.DEBUG)
            Log.e("LOG", "" + sb);
    }

    public static void printFormUploadParameter(String key, String value) {
        //  System.out.println(key+" = "+value+" "+count++);
    }

    public static void appendLog(String text) {
        // Create an log file name with date
//        String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
//        String FileName = "AppLog_" + timeStamp + ".txt";
//        File myDirectory = new File(Environment.getExternalStorageDirectory(), "/SEC2PAY/LOG/");
//
//        if (!myDirectory.exists()) {
//            myDirectory.mkdirs();
//        }
//        File logFile = new File(Environment.getExternalStorageDirectory(), "/SEC2PAY/LOG/" + FileName);
//        if (!logFile.exists()) {
//            try {
//                logFile.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        try {
//            //BufferedWriter for performance, true to set append to file flag
//            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
//            buf.append(text);
//            buf.newLine();
//            buf.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}

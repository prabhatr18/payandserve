package com.payment.aeps.moduleprinter;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Parcelable;
import android.view.View;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.core.widget.NestedScrollView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class wd {
    public static Bitmap NUL(final Bitmap bitmap, final Bitmap bitmap2) {
        final Bitmap bitmap3 = Bitmap.createBitmap(bitmap.getWidth(), bitmap2.getHeight() + bitmap.getHeight(), bitmap.getConfig());
        final Canvas canvas = new Canvas();
        new Canvas(bitmap3);
        final Canvas canvas2 = new Canvas();
        canvas2.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        canvas.drawBitmap(bitmap2, 0.0f, (float) bitmap.getHeight(), (Paint) null);
        return bitmap3;
    }

    private static void NUL(final Bitmap finalBitmap, final Context context) {
        try {
            NUL(saveFileToLocal(finalBitmap, context), context);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static String tempDirectoryName = "reports";

    public static String getTempFolder(Context context) {
        File tempDirectory = new File(context.getExternalFilesDir(null) + File.separator + tempDirectoryName);
        if (!tempDirectory.exists()) {
            System.out.println("creating directory: temp");
            tempDirectory.mkdir();
        }
        return tempDirectory.getAbsolutePath();
    }

    public static File saveFileToLocal(final Bitmap finalBitmap, final Context context) throws IOException {
        File tempDirectory = new File(context.getExternalFilesDir(null) + File.separator + tempDirectoryName);
        if (tempDirectory.exists()) {
            try {
                for (File f : tempDirectory.listFiles()) f.delete();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        File tempFile = new File(getTempFolder(context), "tran_report.jpg");
        String mFilePath = tempFile.getAbsolutePath();

        FileOutputStream fos = null;
        // Create file.
        File imageFile = null;
        if (mFilePath == null || mFilePath.isEmpty()) {
            imageFile = File.createTempFile(Long.toString(new Date().getTime()), "invoice");
        } else {
            imageFile = new File(mFilePath);
        }

        //Create parent directories
        File parentFile = imageFile.getParentFile();
        if (parentFile == null) {
            return null;
        }
        if (!parentFile.exists() && !parentFile.mkdirs()) {
            throw new IllegalStateException("Couldn't create directory: " + parentFile);
        }
        boolean fileExists = imageFile.exists();
        // If File already Exists. delete it.
        if (fileExists) {
            fileExists = !imageFile.delete();
        }
        try {
            if (!fileExists) {
                // Create New File.
                fileExists = imageFile.createNewFile();
            }

            if (fileExists) {
                // Write PDFDocument to the file.
                fos = new FileOutputStream(imageFile);
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                fos.flush();
                fos.close();
            }
            return imageFile;
        } catch (IOException exception) {
            exception.printStackTrace();
            if (fos != null) {
                fos.close();
            }
            throw exception;
        }
    }

    public static void NUL(final File file, final Context context) {
        //final Uri uriForFile = FileProvider.getUriForFile(context, "org.egram.aepslib", file);
        Uri uriForFile = null;
        if ((Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)) {
            if (file.exists())
                uriForFile = FileProvider.getUriForFile(context, context.getPackageName() + ".files", file);
        } else {
            if (file.exists()) {
                uriForFile = Uri.fromFile(file);
            }
        }

        final Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        intent.setType("image/*");
        intent.putExtra("android.intent.extra.SUBJECT", "");
        intent.putExtra("android.intent.extra.TEXT", "");
        intent.putExtra("android.intent.extra.STREAM", (Parcelable) uriForFile);
        final String s = "Share Screenshot";
        try {
            context.startActivity(Intent.createChooser(intent, (CharSequence) s));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(context, (CharSequence) "No App Available", Toast.LENGTH_SHORT).show();
        }
    }

    public void NUL(final ScrollView scrollView, final Context context) {
        scrollView.setDrawingCacheEnabled(true);
        final Activity activity;
        int n;
        if ((activity = (Activity) context).getWindow().getDecorView().getHeight() > scrollView.getChildAt(0).getHeight()) {
            n = activity.getWindow().getDecorView().getHeight();
        } else {
            n = scrollView.getChildAt(0).getHeight();
        }
//        final Bitmap nul = NUL(this.NUL(view, view.getHeight(), view.getWidth()), this.NUL((View) scrollView, n, scrollView.getWidth()));
        scrollView.setDrawingCacheEnabled(false);
        //NUL(nul, context);

        int totalHeight = scrollView.getChildAt(0).getHeight();
        int totalWidth = scrollView.getChildAt(0).getWidth();

        Bitmap b = getBitmapFromView(scrollView, totalHeight, totalWidth);
        NUL(b, context);
    }

    public void NUL(final NestedScrollView scrollView, final Context context) {
        scrollView.setDrawingCacheEnabled(true);
        final Activity activity;
        scrollView.setDrawingCacheEnabled(false);
        int totalHeight = scrollView.getChildAt(0).getHeight();
        int totalWidth = scrollView.getChildAt(0).getWidth();
        Bitmap b = getBitmapFromView(scrollView, totalHeight, totalWidth);
        NUL(b, context);
    }

    public void NUL(final View scrollView, final Context context) {
        scrollView.setDrawingCacheEnabled(true);
        final Activity activity;
        scrollView.setDrawingCacheEnabled(false);
        int totalHeight = scrollView.getHeight();
        int totalWidth = scrollView.getWidth();
        Bitmap b = getBitmapFromView(scrollView, totalHeight, totalWidth);
        NUL(b, context);
    }

    public Bitmap getBitmapFromView(View view, int totalHeight, int totalWidth) {

        Bitmap returnedBitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return returnedBitmap;
    }


    public final Bitmap NUL(final View view, final int n, final int n2) {
        final Bitmap bitmap = Bitmap.createBitmap(n2, n, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        final Drawable background;
        if ((background = view.getBackground()) != null) {
            background.draw(canvas);
        } else {
            canvas.drawColor(-1);
        }
        final Bitmap bitmap2 = bitmap;
        view.draw(canvas);
        return bitmap2;
    }
}

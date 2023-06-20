package com.payment.aeps.network;

import static com.payment.aeps.app.ModuleConstants.AEPS_TYPE;
import static com.payment.aeps.app.ModuleConstants.PAYSPRINT_AEPS;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

import com.payment.aeps.app.ModuleConstants;
import com.payment.aeps.util.Print;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ImageUploadHelper {
    ProgressDialog loaderDialog;
    long totalSize = 0;
    private File mFilePart1;
    private File mFilePart2;
    private File mFilePart3;
    private File mFilePart4;
    private HashMap<String, String> params;
    private Activity context;
    private MRequestResponseLis listener;

    public ImageUploadHelper(File mFilePart1,
                             File mFilePart2,
                             File mFilePart3,
                             File mFilePart4,
                             HashMap<String, String> params, Activity context,
                             String iaeps, MRequestResponseLis listener) {
        this.mFilePart1 = mFilePart1;
        this.mFilePart2 = mFilePart2;
        this.mFilePart3 = mFilePart3;
        this.mFilePart4 = mFilePart4;
        this.params = params;
        this.context = context;
        this.listener = listener;
        new ImageUploadHttp().execute();
    }

    class ImageUploadHttp extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loaderDialog = new ProgressDialog(context);
            loaderDialog.setMessage("Please wait...");
            loaderDialog.setCancelable(false);
            loaderDialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            String url = (ModuleConstants.AEPS_API);
            if (AEPS_TYPE.equalsIgnoreCase(PAYSPRINT_AEPS)) {
               // url = ModuleConstants.baseUrl + "raeps/transaction";
                url = ModuleConstants.baseUrl + "iaeps/transaction";
            }
            HttpPost httppost = new HttpPost(url);
            Print.P("URL : " + url);
            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        num -> publishProgress((int) ((num / (float) totalSize) * 100)));
                if (mFilePart1 != null && mFilePart1.exists()) {
                    entity.addPart("aadharPics", new FileBody(mFilePart1));
                }

                if (mFilePart2 != null && mFilePart2.exists()) {
                    entity.addPart("pancardPics", new FileBody(mFilePart2));
                }

                if (mFilePart2 != null && mFilePart2.exists()) {
                    entity.addPart("passports", new FileBody(mFilePart3));
                }

                if (mFilePart2 != null && mFilePart2.exists()) {
                    entity.addPart("shoppics", new FileBody(mFilePart4));
                }

                for (Map.Entry<String, String> entry : params.entrySet()) {
                    entity.addPart(entry.getKey(), new StringBody(entry.getValue()));
                }

                totalSize = entity.getContentLength();
                httppost.setEntity(entity);

                // Making server call
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity r_entity = response.getEntity();
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode == 200) {
                    responseString = EntityUtils.toString(r_entity);
                } else {
                    responseString = "Error occurred! Http Status Code: " + statusCode;
                }
            } catch (IOException e) {
                responseString = e.toString();
            }
            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                Print.P("response: " + result);
                if (loaderDialog != null && loaderDialog.isShowing()) {
                    loaderDialog.dismiss();
                }
                listener.onSuccessRequest(result);
            } catch (Exception ignored) {

            }
        }
    }
}

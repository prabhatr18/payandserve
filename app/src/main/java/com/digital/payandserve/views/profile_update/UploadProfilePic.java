package com.digital.payandserve.views.profile_update;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.digital.payandserve.R;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.utill.Print;
import com.digital.payandserve.utill.SharedPrefs;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;

public class UploadProfilePic extends AppCompatActivity {
    private CircularImageView imgProfile;

    private File mProfileFile = null;
    private static String apiUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_profile_pic);

        Button btnPick = findViewById(R.id.btnPick);
        Button btnUpload = findViewById(R.id.btnUpload);
        imgProfile = findViewById(R.id.imgProfile);


            MyUtil.setGlideImage(SharedPrefs.getValue(this,SharedPrefs.USER_PROFILE_PIC),imgProfile,this);


        btnPick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickGalleryImage();
            }
        });

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (mProfileFile != null && mProfileFile.exists()) {
                        apiUrl = Constants.URL.BASE_URL + "api/android/auth/profile/image/upload";
                        new UploadFileToServer().execute();
                    } else {
                        Toast.makeText(UploadProfilePic.this, "Image file is required", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void pickGalleryImage() {
        ImagePicker.Companion.with(this)
                .crop()                    //Crop image(Optional), Check Customization for more option
                .compress(100)            //Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == Activity.RESULT_OK) {
               // mProfileFile = ImagePicker.Companion.getFile(data);
                Uri uri = data.getData();
                mProfileFile = new File(uri.getPath());
                if (mProfileFile != null) {
                    showImage(mProfileFile, imgProfile);
                    int file_size = Integer.parseInt(String.valueOf(mProfileFile.length() / 1024));
                    //Log.e("Size", "Size" + file_size);
                }
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                Toast.makeText(this, "" + ImagePicker.Companion.getError(data), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showImage(File file, CircularImageView view) {
        if (file.exists()) {
            Bitmap myBitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
            view.setImageBitmap(myBitmap);
        }
    }

    long totalSize = 0;
    ProgressDialog dialog;

    private class UploadFileToServer extends AsyncTask<Void, Integer, String> {
        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            super.onPreExecute();
            dialog = new ProgressDialog(UploadProfilePic.this);
            dialog.setMessage(getString(R.string.loading_text));
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(Void... params) {
            return uploadFile();
        }

        @SuppressWarnings("deprecation")
        private String uploadFile() {
            String responseString = null;

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(apiUrl);

            try {
                AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                        num -> publishProgress((int) ((num / (float) totalSize) * 100)));

                if (mProfileFile != null && mProfileFile.exists()) {
                    entity.addPart("profilepic", new FileBody(mProfileFile));
                }

                String token = SharedPrefs.getValue(UploadProfilePic.this, SharedPrefs.APP_TOKEN);
                String userId = SharedPrefs.getValue(UploadProfilePic.this, SharedPrefs.USER_ID);
                entity.addPart("user_id", new StringBody(userId == null ? "" : userId));
                entity.addPart("apptoken", new StringBody(token == null ? "" : token));

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

            } catch (Exception e) {
                e.printStackTrace();
                responseString = e.toString();
            }

            return responseString;

        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (!UploadProfilePic.this.isDestroyed()) {
                closeLoader();
            }

            try {
                JSONObject jsonObject = new JSONObject(result);

                Print.P(result);

                String message = "Getting some error in uploading process please try after some time";
                String status = jsonObject.getString("status");
                if (jsonObject.has("message"))
                    message = jsonObject.getString("message");

                if (status.equalsIgnoreCase("TXN")) {
                    String profile = jsonObject.getString("profile");
                    SharedPrefs.setValue(UploadProfilePic.this, SharedPrefs.USER_PROFILE_PIC, profile);
                    Toast.makeText(UploadProfilePic.this, message, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(UploadProfilePic.this, message, Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                Toast.makeText(UploadProfilePic.this, "Expected Unhandled response in the uploading process", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private void closeLoader() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    @Override
    public boolean isDestroyed() {
        closeLoader();
        return super.isDestroyed();
    }
}

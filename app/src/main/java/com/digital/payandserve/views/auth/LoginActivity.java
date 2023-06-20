package com.digital.payandserve.views.auth;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.digital.payandserve.BuildConfig;
import com.digital.payandserve.R;
import com.digital.payandserve.app.AppManager;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.network.RequestResponseLis;
import com.digital.payandserve.network.VolleyNetworkCall;
import com.digital.payandserve.online_service.WebViewActivity;
import com.digital.payandserve.permission.AppPermissions;
import com.digital.payandserve.permission.PermissionHandler;
import com.digital.payandserve.utill.AppHandler;
import com.digital.payandserve.utill.ExtensionFunction;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.utill.Print;
import com.digital.payandserve.utill.SharedPrefs;
import com.digital.payandserve.views.about_us.AboutUsActivity;
import com.digital.payandserve.views.otpview.OTPValidateAuth;
import com.digital.payandserve.views.otpview.OTPValidateForgetPassword;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements RequestResponseLis {
    private EditText etUser, etPassword;
    private TextView tvForgot, tvSignup, acceptCondition;
    private Context context;
    private ImageView imgPass;
    private Button btnLogin;
    private int REQUEST_TYPE = 0;
    private boolean isPassword = true;
    CheckBox acceptTerms;
    ImageView googleImg, facebookImg, instaIc, youtubeIc;

    private void init() {
        context = LoginActivity.this;
        etUser = findViewById(R.id.etUser);
        imgPass = findViewById(R.id.imgPass);
        tvForgot = findViewById(R.id.tvForgot);
        btnLogin = findViewById(R.id.btnLogin);
        etPassword = findViewById(R.id.etPassword);
        tvSignup = findViewById(R.id.tvSignup);
        acceptTerms = findViewById(R.id.acceptTerms);
        acceptCondition = findViewById(R.id.acceptCondition);
        googleImg = findViewById(R.id.googleImg);
        facebookImg = findViewById(R.id.facebookImg);
        instaIc = findViewById(R.id.instaIc);
        youtubeIc = findViewById(R.id.youtubeIc);

        TextView tvPrivacy = findViewById(R.id.tvPrivacy);
        tvPrivacy.setOnClickListener(v -> {
            Intent intent = new Intent((Context) this, WebViewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("url", "https://login.payandserve.com/privacy.html");
            intent.putExtras(bundle);
            startActivity(intent);
        });

        TextView tvDisclaimer = findViewById(R.id.tvDisclaimer);
        tvDisclaimer.setOnClickListener(v -> {
            Intent intent = new Intent((Context) this, WebViewActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("url", "https://login.payandserve.com/website-disclaimer.html");
            intent.putExtras(bundle);
            startActivity(intent);
        });

        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.READ_PHONE_STATE};
        AppPermissions.check(this, permissions, null, null, new PermissionHandler() {
            @Override
            public void onGranted() {

            }
        });

        if (BuildConfig.DEBUG) {
           // etUser.setText("9504065205");
           // etPassword.setText("8102228300");
            etUser.setText("8804850420");
            etPassword.setText("Baibhav@1998");
        }
    }

    private void passwordVisibility() {
        if (isPassword) {
            isPassword = false;
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT);
            imgPass.setImageDrawable(getDrawable(R.drawable.password_view));
        } else {
            isPassword = true;
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            imgPass.setImageDrawable(getDrawable(R.drawable.password_off));
        }
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_design_three);
        init();

        FirebaseApp.initializeApp((Context) this);
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        return;
                    }
                    String token = task.getResult();
                    SharedPrefs.setValue(this, SharedPrefs.FIREBASE_TOKEN, token);
                    Print.P("Token : " + token);
                });

        btnLogin.setOnClickListener(v -> {
            if (isValid()) {
                REQUEST_TYPE = 0;
                networkCallUsingVolleyApi(Constants.URL.LOGIN, true);
            }
        });

        tvForgot.setOnClickListener(v -> {
            if (isValidForgot()) {
                REQUEST_TYPE = 1;
                networkCallUsingVolleyApi(Constants.URL.PASSWORD_RESET_REQ, true);
            }
        });

        tvSignup.setOnClickListener(v -> {
            ExtensionFunction.startActivity((Context) this, Signup.class);
        });

        acceptCondition.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("title", "Terms and Conditions");
            bundle.putString("url", "file:///android_asset/html/terms.html");
            startActivity(new Intent((Context) LoginActivity.this, AboutUsActivity.class).putExtras(bundle));
        });

        googleImg.setOnClickListener(v -> {
            ExtensionFunction.openBrowser("https://g.page/r/CeDfysLuYIiBEBA", this);
        });

        facebookImg.setOnClickListener(v -> {
            ExtensionFunction.openFacebook("https://www.facebook.com/PayandServe", this);
        });

        instaIc.setOnClickListener(v -> {
            ExtensionFunction.openBrowser("https://www.instagram.com/payandserve_fintech/", this);
        });

        youtubeIc.setOnClickListener(v -> {
            ExtensionFunction.openBrowser("https://www.youtube.com/channel/UC8lbn_ojl9sblTqysplSHyw", this);
        });
    }

    private void networkCallUsingVolleyApi(String url, boolean isLoad) {
        if (AppManager.isOnline(this)) {
            new VolleyNetworkCall(this, this, url, 1, param(), isLoad).netWorkCall();
        } else {
            Toast.makeText((Context) this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private Map<String, String> param() {
        Map<String, String> map = new HashMap<>();
        map.put("mobile", etUser.getText().toString());
        if (REQUEST_TYPE == 0)
            map.put("password", etPassword.getText().toString());
        return map;
    }

    private boolean isValid() {
        if (!MyUtil.isNN(etUser.getText().toString())) {
            Toast.makeText((Context) this, "Please input user id", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!MyUtil.isNN(etPassword.getText().toString())) {
            Toast.makeText((Context) this, "Please input password", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!acceptTerms.isChecked()) {
            ExtensionFunction.showToast(this, "Please accept the terms and conditions");
            return false;
        }
        return true;
    }

    private boolean isValidForgot() {
        if (!MyUtil.isNN(etUser.getText().toString())) {
            Toast.makeText((Context) this, "Please input user id", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    public void onSuccessRequest(String res) {
        try {
            String status = AppHandler.getStatus(res);
            String msg = AppHandler.getMessage(res);
            if (REQUEST_TYPE == 1) {
                Intent i = new Intent((Context) this, OTPValidateForgetPassword.class);
                i.putExtra("USER_ID", etUser.getText().toString());
                startActivity(i);
            } else {
                if (status.equals("TXN")) {
                    AppHandler.parseAuthRes(etUser.getText().toString(), res, this);
                } else if (status.equalsIgnoreCase("OTP")) {
                    Intent i = new Intent((Context) this, OTPValidateAuth.class);
                    i.putExtra("USER_ID", etUser.getText().toString());
                    i.putExtra("PASSWORD", etPassword.getText().toString());
                    i.putExtra("MSG", msg);
                    startActivity(i);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailRequest(String msg) {
        Print.P("" + msg);
    }
}

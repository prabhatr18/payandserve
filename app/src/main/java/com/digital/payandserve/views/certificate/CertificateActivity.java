package com.digital.payandserve.views.certificate;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.digital.payandserve.R;
import com.digital.payandserve.utill.AppHandler;
import com.digital.payandserve.utill.SharedPrefs;

public class CertificateActivity extends AppCompatActivity {

    ImageView backBtn, btnShare;
    TextView name,type,number,email;
    View certificateView ;
    private void init() {
        backBtn = findViewById(R.id.backBtn);
        btnShare = findViewById(R.id.btnShare);
        name = findViewById(R.id.name);
        type = findViewById(R.id.type);
        number = findViewById(R.id.number);
        email = findViewById(R.id.email);
        certificateView = findViewById(R.id.secShare);

        name.setText(SharedPrefs.getValue(this,SharedPrefs.USER_NAME));
        type.setText(SharedPrefs.getValue(this,SharedPrefs.ROLE_SLUG));
        number.setText(SharedPrefs.getValue(this,SharedPrefs.USER_CONTACT));
        email.setText(SharedPrefs.getValue(this,SharedPrefs.USER_EMAIL));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificate);

        init();

        backBtn.setOnClickListener(v -> {
            finish();
        });

        btnShare.setOnClickListener(v -> {
            AppHandler.printInvoiceFromAdapter(certificateView, this);

        });
    }


}
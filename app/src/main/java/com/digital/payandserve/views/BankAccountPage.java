package com.digital.payandserve.views;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.digital.payandserve.R;
import com.digital.payandserve.utill.AppHandler;
import com.digital.payandserve.utill.ExtensionFunction;
import com.digital.payandserve.views.profile_update.UploadProfilePic;
import com.google.android.material.card.MaterialCardView;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.utill.SharedPrefs;

public class BankAccountPage extends AppCompatActivity {
    private TextView tvUserName, tvMobile, tvKYC, tvEmail, tvPanCard, tvAadhaarCard, tvShopName;
    private LinearLayout secMsg, secCall;
    private ImageView profileEditImg;

    private void init() {
        tvUserName = findViewById(R.id.tvUserName);
        tvMobile = findViewById(R.id.tvMobile);
        tvKYC = findViewById(R.id.tvKYC);
        tvEmail = findViewById(R.id.tvEmail);
        tvPanCard = findViewById(R.id.tvPanCard);
        tvAadhaarCard = findViewById(R.id.tvAadhaarCard);
        tvShopName = findViewById(R.id.tvShopName);
        secMsg = findViewById(R.id.secMsg);
        secCall = findViewById(R.id.secCall);
        profileEditImg = findViewById(R.id.profileEditImg);

        String userName = SharedPrefs.getValue(this, SharedPrefs.USER_NAME);
        String email = SharedPrefs.getValue(this, SharedPrefs.BANK);
        String mobile = SharedPrefs.getValue(this, SharedPrefs.USER_CONTACT);
        String kyc = SharedPrefs.getValue(this, SharedPrefs.KYC);
        String pancard = SharedPrefs.getValue(this, SharedPrefs.ACCOUNT);
        String aadhaarCard = SharedPrefs.getValue(this, SharedPrefs.IFSC);
        String shopName = SharedPrefs.getValue(this, SharedPrefs.SHOP_NAME);
        secMsg.setOnClickListener(v -> AppHandler.openWhatsApp("9504065205", this));
        secCall.setOnClickListener(v -> AppHandler.dialContactPhone("9504065205", this));
        tvUserName.setText(userName);


        tvMobile.setText(mobile);
        tvKYC.setText(kyc);

        tvEmail.setText("Not Available");
        if (MyUtil.isNN(email))
            tvEmail.setText(email);
        tvPanCard.setText("Not Available");
        if (MyUtil.isNN(email))
            tvPanCard.setText(pancard);
        tvAadhaarCard.setText(aadhaarCard);
        tvShopName.setText(shopName);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyUtil.transparentStatusBar(this);
        setContentView(R.layout.activity_bank_page_new);
        init();
        animateRightGrid();

        profileEditImg.setOnClickListener( v -> {
            ExtensionFunction.startActivity(this, UploadProfilePic.class);
        });
    }

    // animation side panel
    private void animateRightGrid() {
        ConstraintLayout iconsGroup = findViewById(R.id.cardView);
        Animation slideAnimation = AnimationUtils.loadAnimation(this, R.anim.left_to_right_rotate);
        iconsGroup.startAnimation(slideAnimation);
        iconsGroup.animate().rotation(-90).setDuration(700).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateProfileImage();
    }
    private void updateProfileImage() {
        MyUtil.setGlideImage(SharedPrefs.getValue(this,SharedPrefs.USER_PROFILE_PIC),profileEditImg,this);
    }
}

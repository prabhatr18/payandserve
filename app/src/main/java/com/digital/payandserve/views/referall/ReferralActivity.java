package com.digital.payandserve.views.referall;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.digital.payandserve.R;
import com.digital.payandserve.utill.SharedPrefs;


public class ReferralActivity extends Activity {

    private TextView tvReferralValue, tvRefCopy;
    private RelativeLayout secShare;

    private void init() {
        tvReferralValue = findViewById(R.id.tvReferralValue);
        tvRefCopy = findViewById(R.id.tvRefCopy);
        secShare = findViewById(R.id.secShare);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referral_bonus);
        init();
        String subUrl = "https://play.google.com/store/apps/details?id=com.digital.payandserve&referrer=utm_source%3Dgoogle%26utm_medium%3Dcpc%26utm_campaign%3D";
        String refCode = SharedPrefs.getValue(this, SharedPrefs.REF_CODE);
        String shareUrl = subUrl + refCode;
        tvReferralValue.setText(""+refCode);
        secShare.setOnClickListener(v->{ shareText(shareUrl); });
        tvRefCopy.setOnClickListener(v->{ copyToClipBoard(refCode); });
    }

    private void shareText(String url) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        String shareBody = "" + url;
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Referral Link");
        intent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(intent, "Share Via"));
    }

    private void copyToClipBoard(String text){
        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("lbl", text);
        clipboard.setPrimaryClip(clip);
    }
}
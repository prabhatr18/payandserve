package com.digital.payandserve.contact_us;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.PhoneNumberUtils;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.digital.payandserve.R;

import java.util.List;
import java.util.Locale;

public class ContactUsActivity extends AppCompatActivity {

    public static String FACEBOOK_URL = "https://www.facebook.com/PayandServe";
    public static String FACEBOOK_PAGE_ID = "PayandServe";
    TextView helplineNumOne, helplineNumTwo, helplineNumThree, helplineNumFour;
    TextView emailOne, emailTwo, emailThree;
    TextView whatNumOne, whatTwo;
    CardView whatsappOneCard, whatsappTwoCard, addressCardView;
    ImageView googleIc, facebookIc, instaIc, youtubeIc, mapIc;
    String helplineNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        helplineNumOne = findViewById(R.id.helplineNumOne);
        helplineNumTwo = findViewById(R.id.helplineNumTwo);
        helplineNumThree = findViewById(R.id.helplineNumThree);
        helplineNumFour = findViewById(R.id.helplineNumFour);
        emailOne = findViewById(R.id.emailOne);
        emailTwo = findViewById(R.id.emailTwo);
        emailThree = findViewById(R.id.emailThree);
        whatsappOneCard = findViewById(R.id.whatsappOneCard);
        whatsappTwoCard = findViewById(R.id.whatsappTwoCard);
        googleIc = findViewById(R.id.googleIc);
        facebookIc = findViewById(R.id.facebookIc);
        instaIc = findViewById(R.id.instaIc);
        youtubeIc = findViewById(R.id.youtubeIc);
        addressCardView = findViewById(R.id.addressCardView);
        whatNumOne = findViewById(R.id.whatNumOne);
        whatTwo = findViewById(R.id.whatTwo);
        mapIc = findViewById(R.id.mapIc);


        helplineNumOne.setOnClickListener(v -> {

            callNumber(helplineNumOne.getText().toString().replace("-", ""));
        });

        helplineNumTwo.setOnClickListener(v -> {
            callNumber(helplineNumTwo.getText().toString().replace("-", ""));
        });

        helplineNumThree.setOnClickListener(v -> {
            callNumber(helplineNumThree.getText().toString().replace("-", ""));
        });

        helplineNumFour.setOnClickListener(v -> {
            callNumber(helplineNumFour.getText().toString().replace("-", ""));
        });

        emailOne.setOnClickListener(v -> {
            sendEmail(emailOne.getText().toString().trim());
        });

        emailTwo.setOnClickListener(v -> {
            sendEmail(emailOne.getText().toString().trim());
        });

        emailThree.setOnClickListener(v -> {
            sendEmail(emailOne.getText().toString().trim());
        });

        whatsappOneCard.setOnClickListener(v -> {
            sendWhatsappMessage(whatNumOne.getText().toString().replace("+", ""));
        });

        whatsappTwoCard.setOnClickListener(v -> {
            sendWhatsappMessage(whatTwo.getText().toString().replace("+", ""));
        });

        googleIc.setOnClickListener(v -> {
            openBrowser("https://g.page/r/CeDfysLuYIiBEBA");
        });

        facebookIc.setOnClickListener(v -> {
            Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
            String facebookUrl = getFacebookPageURL(this);
            facebookIntent.setData(Uri.parse(facebookUrl));
            startActivity(facebookIntent);
        });

        instaIc.setOnClickListener(v -> {

            openBrowser("https://www.instagram.com/payandserve_fintech/");

        });

        youtubeIc.setOnClickListener(v -> {
            openBrowser("https://www.youtube.com/channel/UC8lbn_ojl9sblTqysplSHyw");
        });

        addressCardView.setOnClickListener(v -> {
            String uri = String.format(Locale.ENGLISH, "geo:%f,%f", 27.1751, 78.0421);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        });

    }


    private void sendWhatsappMessage(String number) {
        try {
            number = number.replace(" ", "");
            Intent sendIntent = new Intent("android.intent.action.MAIN");
            sendIntent.setComponent(new ComponentName("com.whatsapp", "com.whatsapp.Conversation"));
            sendIntent.putExtra("jid", PhoneNumberUtils.stripSeparators(number) + "@s.whatsapp.net");
            startActivity(sendIntent);
        } catch (Exception e) {
            Log.e("TAG", "ERROR_OPEN_MESSANGER" + e.toString());
        }
    }

    private void sendEmail(String mail) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + mail));
          /*  intent.putExtra(Intent.EXTRA_SUBJECT, "Refferal Code");
            intent.putExtra(Intent.EXTRA_TEXT, ReferCode);*/
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Sorry...You don't have any mail app", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void callNumber(String number) {

        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + number));
        startActivity(callIntent);
    }

    //To open The Facebook when user click facebook icon
    public String getFacebookPageURL(Context context) {
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

    private boolean isIntentAvailable(Context ctx, Intent intent) {
        final PackageManager packageManager = ctx.getPackageManager();
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


    private void openBrowser(String url) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);
    }

}
package com.digital.payandserve.utill;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.transition.ViewPropertyTransition;
import com.digital.payandserve.R;
import com.digital.payandserve.app.AppController;
import com.digital.payandserve.views.billpayment.model.ParameItem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyUtil {

    public static void transparentStatusBar(Activity activity) {
        try {
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void explodeAnimation(Activity activity) {
        activity.getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        Explode explode = new Explode();
        activity.getWindow().setExitTransition(explode);
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    public static void whiteStatusBar(Activity activity) {
        try {
            if (Build.VERSION.SDK_INT >= 21) {
                Window window = activity.getWindow();
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(activity.getResources().getColor(R.color.white_card));
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeStatusBar(Activity activity) {
        try {
            activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void styleChange(Activity activity) {
        boolean isNightMode = false;
        if (activity.getResources().getConfiguration().uiMode == Configuration.UI_MODE_NIGHT_MASK) {
            isNightMode = true;
        }

        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    public static String format(Object value) {
        double m = Double.parseDouble(String.valueOf(value));
        return new DecimalFormat("0.00").format(m);
    }

    public static String formatWithRupee(Context context, Object value) {
        if (value != null) {
            String rupee = context.getResources().getString(R.string.rupee);
            double m = Double.parseDouble(String.valueOf(value));
            return rupee + " " + new DecimalFormat("0.00").format(m);
        } else {
            return "Not Available";
        }
    }

    public static boolean isNN(String str) {
        boolean flag = false;
        if (str != null && str.length() > 0 && !str.equalsIgnoreCase("null")) {
            flag = true;
        }
        return flag;
    }

    public static String capitalise(String status) {
        StringBuilder sb = new StringBuilder(status);
        sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
        return sb.toString();
    }

    public static boolean isNN_ET(EditText strView) {
        String str = strView.getText().toString();
        boolean flag = false;
        if (str != null) {
            flag = true;
        }
        return flag;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public static void setProviderImage(String url, ImageView imageView, String provider, Context context, String type) {
        if (url != null && url.length() > 0 && !url.equalsIgnoreCase("null")) {
            ImageLoader imageLoader = AppController.getInstance().getImageLoader();
            imageLoader.get(url, new ImageLoader.ImageListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    MyUtil.setStaticProviderImg(provider, imageView, context, type);
                }

                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean arg1) {
                    if (response.getBitmap() != null) {
                        imageView.setImageBitmap(response.getBitmap());
                    }
                }
            });
        } else
            MyUtil.setStaticProviderImg(provider, imageView, context, type);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public static void setGlideImage(String url, ImageView imageView, Context context) {
        if (context == null || ((Activity) context).isDestroyed()) return;
        ViewPropertyTransition.Animator animationObject = view -> {
            view.setAlpha(0f);
            ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
            fadeAnim.setDuration(1000);
            fadeAnim.start();
        };

        RequestOptions options = new RequestOptions()
                .error(context.getResources().getDrawable(R.drawable.not_found_error))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .timeout(60000)
                .centerCrop();
        Glide.with(context) //passing context
                .load(url) //passing your url to load image.
                .transition(GenericTransitionOptions.with(animationObject))
                .apply(options)
                .into(imageView);
    }

    public static void setGlideImageTwo(String url, ImageView imageView, Context context) {
        if (context == null || ((Activity) context).isDestroyed()) return;
        ViewPropertyTransition.Animator animationObject = view -> {
            view.setAlpha(0f);
            ObjectAnimator fadeAnim = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
            fadeAnim.setDuration(1000);
            fadeAnim.start();
        };

        RequestOptions options = new RequestOptions()
                .error(context.getResources().getDrawable(R.drawable.not_found_error))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority(Priority.HIGH)
                .timeout(60000);
        Glide.with(context) //passing context
                .load(url) //passing your url to load image.
                .transition(GenericTransitionOptions.with(animationObject))
                .apply(options)
                .into(imageView);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public static void setStaticProviderImg(String name, ImageView img, Context context, String type) {
        name = name.toLowerCase().replaceAll("odisha", "");
        name = name.replaceAll(" ", "").toLowerCase();
        try {
            if (name.contains("dish")) {
                img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_dish_tv));
            } else if (name.contains("sundirect")) {
                img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_sun_direct_vector_logo));
            } else if (name.contains("tatasky")) {
                img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_tata_sky));
            } else if (name.contains("videocon")) {
                img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_videocon));
            } else if (name.contains("vodafone")) {
                img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_vodafone));
            } else if (name.contains("bsnl")) {
                img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_bsnl));
            } else if (name.contains("jio")) {
                img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_jio));
            } else if (name.contains("idea")) {
                img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_idea));
            } else if (name.contains("airtel")) {
                img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_airtel));
            } else if (name.contains("tatadocomo")) {
                img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_docomo));
            } else if (type.contains("electricity")) {
                img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_power_source));
            } else if (type.contains("lpggas")) {
                img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_gas_provider));
            } else if (type.contains("landline")) {
                img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_postpaid));
            } else if (type.contains("broadband")) {
                img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_broadband_provider));
            } else if (type.contains("water")) {
                img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_water_provider));
            } else if (type.contains("loanrepay")) {
                img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_loan_provider));
            } else if (type.contains("lifeinsurance")) {
                img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_insurance_provider));
            } else if (type.contains("fasttag")) {
                img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_fast_provider));
            } else if (type.contains("cable")) {
                img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_cable_provider));
            } else if (type.contains("insurance")) {
                img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_insurance_provider));
            } else if (type.contains("schoolfees")) {
                img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_school_fees));
            } else if (type.contains("muncipal")) {
                img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_tax_provider));
            } else if (type.contains("postpaid")) {
                img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_postpaid));
            } else if (type.contains("housing")) {
                img.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_housing_provider));
            } else {
                img.setImageDrawable(context.getResources().getDrawable(R.drawable.na_icon));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<ParameItem> getBillPayParam(String type) {
        List<ParameItem> dataList = new ArrayList<>();
        if (type.contains("electricity")) {
            dataList.add(new ParameItem("Bill Number"));
        } else if (type.contains("lpggas")) {
            dataList.add(new ParameItem("Customer ID"));
        } else if (type.contains("landline")) {
            dataList.add(new ParameItem("Telephone Number"));
        } else if (type.contains("broadband")) {
            dataList.add(new ParameItem("Account Number"));
        } else if (type.contains("water")) {
            dataList.add(new ParameItem("Customer ID"));
        } else if (type.contains("loanrepay")) {
            dataList.add(new ParameItem("Loan Account Number"));
        } else if (type.contains("lifeinsurance")) {
            dataList.add(new ParameItem("Policy Number"));
        } else if (type.contains("fasttag")) {
            dataList.add(new ParameItem("Vehicle Number"));
        } else if (type.contains("cable")) {
            dataList.add(new ParameItem("Account Number"));
        } else if (type.contains("insurance")) {
            dataList.add(new ParameItem("Policy Number"));
        } else if (type.contains("schoolfees")) {
            dataList.add(new ParameItem("Date of Birth(dd/mm/yyyy)"));
        } else if (type.contains("muncipal")) {
            dataList.add(new ParameItem("Property ID"));
        } else if (type.contains("postpaid")) {
            dataList.add(new ParameItem("Customer Id"));
        } else if (type.contains("housing")) {
            dataList.add(new ParameItem("Apartment Number"));
        } else {
            dataList.add(new ParameItem("Customer id"));
        }
        dataList.add(new ParameItem("Contact Number"));
        return dataList;
    }

    public static void shareOnAllApps(Context context, String body) {
        Intent intent = new Intent(android.content.Intent.ACTION_SEND);
        String sub = "Issue in " + context.getString(R.string.app_name);
        intent.setType("text/plain");
        intent.putExtra(android.content.Intent.EXTRA_SUBJECT, sub);
        intent.putExtra(android.content.Intent.EXTRA_TEXT, body);
        context.startActivity(Intent.createChooser(intent, "Share using"));
    }

    public static boolean isValidEmail(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static String getImei(Context context) {
        String deviceId = null;
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && Build.VERSION.SDK_INT < 29) {
                deviceId = telephonyManager.getImei(0);
            } else if (Build.VERSION.SDK_INT >= 29) {
                deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            } else {
                deviceId = telephonyManager.getDeviceId();
            }
        } catch (Exception e) {
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        return deviceId;
    }
}


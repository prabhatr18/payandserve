package com.digital.payandserve.views;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.amirarcane.lockscreen.activity.EnterPinActivity;
import com.digital.payandserve.BuildConfig;
import com.digital.payandserve.R;
import com.digital.payandserve.adapter.BankingServiceAdapter;
import com.digital.payandserve.adapter.HomeAdapter;
import com.digital.payandserve.adapter.MyCustomPagerAdapter;
import com.digital.payandserve.app.AppManager;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.contact_us.ContactUsActivity;
import com.digital.payandserve.customer_care.AppCompain;
import com.digital.payandserve.dataModel.MainLocalData;
import com.digital.payandserve.model.ActivityListModel;
import com.digital.payandserve.network.RequestResponseLis;
import com.digital.payandserve.network.VolleyNetworkCall;
import com.digital.payandserve.newservice.GetPlanActivity;
import com.digital.payandserve.newservice.PayLaterRegistrationActivity;
import com.digital.payandserve.online_service.OnlineServiceAdapter;
import com.digital.payandserve.online_service.WebViewActivity;
import com.digital.payandserve.utill.ExtensionFunction;
import com.digital.payandserve.utill.MarqueeTextView;
import com.digital.payandserve.utill.MyUtil;
import com.digital.payandserve.utill.Print;
import com.digital.payandserve.utill.RecyclerTouchListener;
import com.digital.payandserve.utill.SharedPrefs;
import com.digital.payandserve.views.about_us.AboutUsActivity;
import com.digital.payandserve.views.allservices_search.AllServicesSearch;
import com.digital.payandserve.views.billpayment.RechargeAndBillPayment;
import com.digital.payandserve.views.certificate.CertificateActivity;
import com.digital.payandserve.views.commission.CommissionList;
import com.digital.payandserve.views.dash_board.ServiceDialogRecyclerAdapter;
import com.digital.payandserve.views.dash_board.model.ServiceDialogModel;
import com.digital.payandserve.views.dmt.PGSearchAccount;
import com.digital.payandserve.views.member.MemberListAll;
import com.digital.payandserve.views.mhagram_aeps_matm.HandlerActivity;
import com.digital.payandserve.views.moneytransfer.DMTSearchAccount;
import com.digital.payandserve.views.notification.NotificationsListing;
import com.digital.payandserve.views.ourproducts.BuyProductsActivity;
import com.digital.payandserve.views.paylater.PayLaterActivity;
import com.digital.payandserve.views.paylater.PaylaterPlansActivity;
import com.digital.payandserve.views.referall.ReferralActivity;
import com.digital.payandserve.views.reports.AEPSTransaction;
import com.digital.payandserve.views.reports.AllReports;
import com.digital.payandserve.views.reports.BillRechargeTransaction;
import com.digital.payandserve.views.reports.DMTTransactionReport;
import com.digital.payandserve.views.reports.adapter.HorizontalReportListAdapter;
import com.digital.payandserve.views.settings.Settings;
import com.digital.payandserve.views.tutorial.TutorialActivity;
import com.digital.payandserve.views.walletsection.AepsMatmWalletReqest;
import com.digital.payandserve.views.walletsection.WalletFundRequest;
import com.digital.payandserve.views.walletsection.WalletOptions;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;
import com.payment.aeps.AepsDashboard;
import com.paysprint.onboardinglib.activities.HostActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class DashBoardActivity extends AppCompatActivity implements RequestResponseLis,
        OnlineServiceAdapter.OnlineServiceClickListner, ServiceDialogRecyclerAdapter.OnServiceDialogItemClicked {
    private static final int REQUEST_CODE = 101;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private View mainWallet, aepsWallet, matmWallet, kuberWallet;
    private TextView mainWalletType, aepsWalletType, matmWalletType, kuberWalletType;
    private TextView mainWalletAmount, aepsWalletAmount, matmWalletAmount, kuberWalletAmount;
    private Button mainWalletBtn, aepsWalletBtn, matmWalletBtn, kuberWalletBtn;
    private RecyclerView rvHome;
    private TextView etSearch;
    private ImageView imgProfile;
    private ArrayList<ServiceDialogModel> serviceList;
    private String selectedOption = "";
    private RecyclerView bankingServiceRecycler, lendingServiceRecycler,
            governanceRecycler, insuranceRecycler, ecommerceRecycler, tourTravelRecycler, rvReport;
    private RecyclerView serviceRecyclerView;
    private OnlineServiceAdapter lendingAdapter, governanceAdapter, insuranceAdapter,
            ecommerceAdapter, tourAdapter;
    private ServiceDialogRecyclerAdapter dialogRecyclerAdapter;
    private AlertDialog alert;
    private ViewPager viewPager;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private ImageView imgCardWallet, imgCardAeps, imgCardMatm, imgCardKuber;
    private String loanId = "";

    private void init() {
        etSearch = findViewById(R.id.etSearch);
        rvHome = findViewById(R.id.rvHome);
        navigationView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        bankingServiceRecycler = findViewById(R.id.bankingServiceRecycler);
        lendingServiceRecycler = findViewById(R.id.lendingServiceRecycler);
        governanceRecycler = findViewById(R.id.governanceRecycler);
        insuranceRecycler = findViewById(R.id.insuranceRecycler);
        ecommerceRecycler = findViewById(R.id.ecommerceRecycler);
        tourTravelRecycler = findViewById(R.id.tourTravelRecycler);
        rvReport = findViewById(R.id.rvReport);

        mainWallet = findViewById(R.id.mainWallet);
        mainWalletType = mainWallet.findViewById(R.id.tvLbl);
        mainWalletAmount = mainWallet.findViewById(R.id.tvAmount);
        mainWalletBtn = mainWallet.findViewById(R.id.btnAddMoney);
        imgCardWallet = mainWallet.findViewById(R.id.imgWalletCard);
        imgCardWallet.setImageDrawable(getDrawable(R.drawable.wallet_icon));
        mainWalletType.setText("Wallet Balance");
        mainWalletBtn.setText("WALLET TOP-UP");

        aepsWallet = findViewById(R.id.aepsWallet);
        aepsWalletType = aepsWallet.findViewById(R.id.tvLbl);
        aepsWalletAmount = aepsWallet.findViewById(R.id.tvAmount);
        aepsWalletBtn = aepsWallet.findViewById(R.id.btnAddMoney);
        imgCardAeps = aepsWallet.findViewById(R.id.imgWalletCard);
        imgCardAeps.setImageDrawable(getDrawable(R.drawable.finger_print));
        aepsWalletType.setText("Kiosk Balance");
        aepsWalletBtn.setText("BALANCE\nSettlement");

        matmWallet = findViewById(R.id.matmWallet);
        matmWalletType = matmWallet.findViewById(R.id.tvLbl);
        matmWalletAmount = matmWallet.findViewById(R.id.tvAmount);
        matmWalletBtn = matmWallet.findViewById(R.id.btnAddMoney);
        imgCardMatm = matmWallet.findViewById(R.id.imgWalletCard);
        imgCardMatm.setImageDrawable(getDrawable(R.drawable.wallet_matm));
        matmWalletType.setText("Card Balance");
        matmWalletBtn.setText("BALANCE\nSettlement");

        kuberWallet = findViewById(R.id.kuberWallet);
        kuberWalletType = kuberWallet.findViewById(R.id.tvLbl);
        kuberWalletAmount = kuberWallet.findViewById(R.id.tvAmount);
        kuberWalletBtn = kuberWallet.findViewById(R.id.btnAddMoney);
        imgCardKuber = kuberWallet.findViewById(R.id.imgWalletCard);
        imgCardKuber.setImageDrawable(getDrawable(R.drawable.wallet_kuber));
        kuberWalletType.setText("Kuber Balance");
        kuberWalletBtn.setText("Add-Money");

        ExtensionFunction.setGridLayoutManager(bankingServiceRecycler, this, 4);
        ExtensionFunction.setGridLayoutManager(lendingServiceRecycler, this, 4);
        ExtensionFunction.setGridLayoutManager(governanceRecycler, this, 5);
        ExtensionFunction.setGridLayoutManager(insuranceRecycler, this, 4);
        ExtensionFunction.setGridLayoutManager(ecommerceRecycler, this, 4);
        ExtensionFunction.setGridLayoutManager(tourTravelRecycler, this, 4);

        appLockHandler();

        mainWalletBtn.setOnClickListener(v -> {
            Intent i = new Intent((Context) DashBoardActivity.this, WalletFundRequest.class);
            startActivity(i);
        });

        aepsWalletBtn.setOnClickListener(v -> {
            Intent i = new Intent((Context) DashBoardActivity.this, AepsMatmWalletReqest.class);
            i.putExtra("activity_type", "aeps");
            startActivity(i);
        });

        matmWalletBtn.setOnClickListener(v -> {
            Intent i = new Intent((Context) DashBoardActivity.this, AepsMatmWalletReqest.class);
            i.putExtra("activity_type", "matm");
            startActivity(i);
        });

        kuberWalletBtn.setOnClickListener(v -> {
            Intent i = new Intent((Context) DashBoardActivity.this, AepsMatmWalletReqest.class);
            i.putExtra("activity_type", "kuber");
            startActivity(i);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_home);

        init();
        imageSliderInit();
        gridInit();
        initBalance();
        initMarquee();
        setUpNavigationView();
        intiBankingRecycler();
        intiLendingRecycler();
        intiGovernanceRecycler();
        intiInsuranceRecycler();
        intiEcommerceRecycler();
        intiTourTravelRecycler();
        gridInitReport();

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

        etSearch.setOnClickListener(v -> startActivity(new Intent(
                (Context) DashBoardActivity.this, AllServicesSearch.class)));
    }

    private void initBalance() {
        mainWalletAmount.setText(MyUtil.formatWithRupee(this, SharedPrefs.getValue(this, SharedPrefs.MAIN_WALLET)));
        aepsWalletAmount.setText(MyUtil.formatWithRupee(this, SharedPrefs.getValue(this, SharedPrefs.APES_BALANCE)));
        kuberWalletAmount.setText(MyUtil.formatWithRupee(this, SharedPrefs.getValue(this, SharedPrefs.KUBER_BALANCE)));
        matmWalletAmount.setText("Fund Request");
        String mBal = SharedPrefs.getValue(this, SharedPrefs.MICRO_ATM_BALANCE);
        if (mBal != null && !mBal.equalsIgnoreCase("NO") && mBal.length() > 0)
            matmWalletAmount.setText(MyUtil.formatWithRupee(this, mBal));

    }

    private void imageSliderInit() {
        String[] images = Constants.images;
        List<String> stringList = Arrays.asList(images);
        String is_avail_array = SharedPrefs.getValue(this, SharedPrefs.BANNERARRAY);
        if (MyUtil.isNN(is_avail_array))
            try {
                stringList = new ArrayList<>();
                JSONArray bannersJson = new JSONArray(is_avail_array);
                for (int i = 0; i < bannersJson.length(); i++) {
                    JSONObject obj = bannersJson.getJSONObject(i);
                    String url = Constants.URL.BASE_URL + "/public/" + obj.getString("value");
                    stringList.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        viewPager = findViewById(R.id.viewPager);
        MyCustomPagerAdapter myCustomPagerAdapter = new MyCustomPagerAdapter(DashBoardActivity.this, stringList);
        viewPager.setAdapter(myCustomPagerAdapter);
        CircleIndicator circleIndicator = findViewById(R.id.circle);
        circleIndicator.setViewPager(viewPager);
        NUM_PAGES = images.length;
        final Handler handler = new Handler();
        final Runnable Update = () -> {
            if (currentPage == NUM_PAGES) {
                currentPage = 0;
            }
            viewPager.setCurrentItem(currentPage++, true);
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);
    }

    private void gridInit() {
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager((Context) DashBoardActivity.this, 4);
        rvHome.setLayoutManager(layoutManager);
        rvHome.setItemAnimator(new DefaultItemAnimator());
        HomeAdapter homeAdapter = new HomeAdapter(DashBoardActivity.this, MainLocalData.getHomeGridRecord());
        rvHome.setAdapter(homeAdapter);
        rvHome.addOnItemTouchListener(new RecyclerTouchListener(this,
                rvHome, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent i;
                if (position == 3) {
                    i = new Intent((Context) DashBoardActivity.this, AllServices.class);
                } else {
                    i = new Intent((Context) DashBoardActivity.this, RechargeAndBillPayment.class);
                    i.putExtra("position", String.valueOf(position));
                }
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    private void gridInitReport() {
        LinearLayoutManager nearLayoutManager = new LinearLayoutManager((Context) this, LinearLayoutManager.HORIZONTAL, false);
        rvReport.setLayoutManager(nearLayoutManager);
        rvReport.setItemAnimator(new DefaultItemAnimator());
        HorizontalReportListAdapter reportAdapter = new HorizontalReportListAdapter(DashBoardActivity.this,
                MainLocalData.getReportGridRecord());
        rvReport.setAdapter(reportAdapter);
        rvReport.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(),
                rvReport, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                switch (position) {
                    case 0:
                        Intent i = new Intent((Context) DashBoardActivity.this, AEPSTransaction.class);
                        i.putExtra("title", "AEPS Transactions");
                        i.putExtra("type", "aepsstatement");
                        startActivity(i);
                        break;
                    case 1:
                        i = new Intent((Context) DashBoardActivity.this, BillRechargeTransaction.class);
                        i.putExtra("title", "Recharge Statement");
                        i.putExtra("type", "rechargestatement");
                        startActivity(i);
                        break;
                    case 2:
                        i = new Intent((Context) DashBoardActivity.this, BillRechargeTransaction.class);
                        i.putExtra("title", "Bill Payment Statement");
                        i.putExtra("type", "billpaystatement");
                        startActivity(i);
                        break;
                    case 3:
                        i = new Intent((Context) DashBoardActivity.this, DMTTransactionReport.class);
                        i.putExtra("title", "DMT Statement");
                        i.putExtra("type", "dmtstatement");
                        startActivity(i);
                        break;
                    case 4:
                        i = new Intent((Context) DashBoardActivity.this, AllReports.class);
                        i.putExtra("title", "All Reports");
                        startActivity(i);
                }
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    private void initMarquee() {
        String t = "";
        findViewById(R.id.secNews).setVisibility(View.VISIBLE);
        MarqueeTextView tvMarquee = findViewById(R.id.tvMarquee);
        t = SharedPrefs.getValue(this, SharedPrefs.NEWS);
        if (!MyUtil.isNN(t) || t.equalsIgnoreCase("no"))
            t = "Welcome to " + getString(R.string.app_name);
        tvMarquee.setText(t);
    }


  /*  @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case REQUEST_CODE:
                    if (resultCode == EnterPinActivity.RESULT_BACK_PRESSED) {
                        finishAffinity();
                        startActivity(new Intent((Context) this, SplashScreen.class));
                    }
                    break;
                case 5000:
                    String msg = data.getStringExtra("msg");
                    if (MyUtil.isNN(msg)) {
                        Print.P(msg);
                        Toast.makeText((Context) this, "" + msg, Toast.LENGTH_SHORT).show();
                    }

                case 999:
                    boolean status = data.getBooleanExtra("status", false);
                    int response = data.getIntExtra("response", 0);
                    String message = data.getStringExtra("message");
                    String detailedResponse = "Status: " + status + ",  Response: " + response + ", Message: " + message;
                    Toast.makeText((Context) this, detailedResponse, Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case REQUEST_CODE:
                    if (resultCode == EnterPinActivity.RESULT_BACK_PRESSED) {
                        finishAffinity();
                        startActivity(new Intent((Context) this, SplashScreen.class));
                    }
                    break;
                case 5000:
                    String msg = data.getStringExtra("msg");
                    if (MyUtil.isNN(msg)) {
                        Print.P(msg);
                        Toast.makeText(this, "" + msg, Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 999:
                    boolean status = data.getBooleanExtra("status", false);
                    int response = data.getIntExtra("response", 0);
                    String message = data.getStringExtra("message");
                    String detailedResponse = "Status: " + status + ",  Response: " + response + ", Message: " + message;
                    Toast.makeText((Context) this, detailedResponse, Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpNavigationView() {
        navigationView.setNavigationItemSelectedListener(menuItem -> {
            drawer.closeDrawers();
            if (menuItem.getItemId() == R.id.report) {
                startActivity(new Intent((Context) DashBoardActivity.this, AllReports.class));
            } else if (menuItem.getItemId() == R.id.member) {
                slugPopup();
            } else if (menuItem.getItemId() == R.id.settle) {
                startActivity(new Intent((Context) DashBoardActivity.this, WalletOptions.class));
            } else if (menuItem.getItemId() == R.id.profile) {
                startActivity(new Intent((Context) DashBoardActivity.this, ProfilePage.class));
            } else if (menuItem.getItemId() == R.id.bank) {
                startActivity(new Intent((Context) DashBoardActivity.this, BankAccountPage.class));
            } else if (menuItem.getItemId() == R.id.settings) {
                startActivity(new Intent((Context) DashBoardActivity.this, Settings.class));
            } else if (menuItem.getItemId() == R.id.payLater) {
                if (!loanId.isEmpty()) {
                    startActivity(new Intent((Context) DashBoardActivity.this, PayLaterActivity.class));
                } else {
                    startActivity(new Intent((Context) DashBoardActivity.this, PaylaterPlansActivity.class));
                }

            } else if (menuItem.getItemId() == R.id.commission) {
                startActivity(new Intent((Context) DashBoardActivity.this, CommissionList.class));
            } else if (menuItem.getItemId() == R.id.tutorial) {
                startActivity(new Intent((Context) DashBoardActivity.this, TutorialActivity.class));
            } else if (menuItem.getItemId() == R.id.privacy) {
                Bundle bundle = new Bundle();
                bundle.putString("title", "Privacy Policy");
                bundle.putString("url", "file:///android_asset/html/privacy.html");
                startActivity(new Intent((Context) DashBoardActivity.this, AboutUsActivity.class).putExtras(bundle));
            } else if (menuItem.getItemId() == R.id.rate_us) {
                String appPackageName = "com.digital.payandserve";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
//                showRateApp();
            } else if (menuItem.getItemId() == R.id.about_us) {
                Bundle bundle = new Bundle();
                bundle.putString("title", "About Us");
                bundle.putString("url", "file:///android_asset/html/about.html");
                startActivity(new Intent((Context) DashBoardActivity.this, AboutUsActivity.class).putExtras(bundle));
            } else if (menuItem.getItemId() == R.id.contact_us) {
                startActivity(new Intent((Context) DashBoardActivity.this, ContactUsActivity.class));
            } else if (menuItem.getItemId() == R.id.support_ticket) {
                startActivity(new Intent((Context) DashBoardActivity.this, AppCompain.class));
            } else if (menuItem.getItemId() == R.id.notification) {
                startActivity(new Intent((Context) DashBoardActivity.this, NotificationsListing.class));
            } else if (menuItem.getItemId() == R.id.product) {
                startActivity(new Intent((Context) DashBoardActivity.this, BuyProductsActivity.class));
            } else if (menuItem.getItemId() == R.id.vcart) {
                startActivity(new Intent((Context) DashBoardActivity.this, CertificateActivity.class));
            } else if (menuItem.getItemId() == R.id.refer) {
                String refCode = SharedPrefs.getValue(DashBoardActivity.this, SharedPrefs.REF_CODE);
                if (MyUtil.isNN(refCode)) {
                    startActivity(new Intent((Context) DashBoardActivity.this, ReferralActivity.class));
                } else {
                    Toast.makeText((Context) DashBoardActivity.this, "Referral code is not available", Toast.LENGTH_SHORT).show();
                }
            } else if (menuItem.getItemId() == R.id.onBoarding) {

                if (loanId.isEmpty()) {
                    startActivity(new Intent((Context) DashBoardActivity.this, PayLaterRegistrationActivity.class));

                    // startActivity(new Intent((Context) DashBoardActivity.this, PayLaterActivity.class));
                } else {
                    startActivity(new Intent((Context) DashBoardActivity.this, GetPlanActivity.class));
                    ;
                }

            }
            return true;
        });

        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle((Activity) this, drawer,
                toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
        actionBarDrawerToggle.syncState();
        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        imgProfile = headerView.findViewById(R.id.imgProfile);
        TextView tvNavName = headerView.findViewById(R.id.tvNavName);
        TextView tvNavEmail = headerView.findViewById(R.id.tvNavEmail);
        String name = SharedPrefs.getValue(this, SharedPrefs.USER_NAME);
        tvNavName.setText(name);
        String email = SharedPrefs.getValue(this, SharedPrefs.USER_EMAIL);
        String useID = SharedPrefs.getValue(this, SharedPrefs.USER_ID);
        email += "\nuser id : " + useID;
        tvNavEmail.setText(email);
        Button btnLogout = headerView.findViewById(R.id.btnLogout);
        imgProfile.setOnClickListener(v -> startActivity(new Intent((Context) DashBoardActivity.this, ProfilePage.class)));
        btnLogout.setOnClickListener(v -> AppManager.getInstance().logoutFromServer(this, false));
    }

    private void slugPopup() {
        String slug = SharedPrefs.getValue(this, SharedPrefs.ROLE_SLUG);
        if (MyUtil.isNN(slug) && (slug.equalsIgnoreCase("md") || slug.equalsIgnoreCase("distributor"))) {
            if (slug.equalsIgnoreCase("distributor")) {
                Intent i = new Intent((Context) this, MemberListAll.class);
                i.putExtra("type", "retailer");
                startActivity(i);
            } else {
                final CharSequence[] choice = {"distributor", "retailer"};
                AlertDialog.Builder alert = new MaterialAlertDialogBuilder((Context) this, R.style.ThemeOverlay_App_MaterialAlertDialog);
                alert.setTitle("Please select role");
                alert.setSingleChoiceItems(choice, -1, (dialog, which) -> {
                    String txt = choice[which].toString();
                    dialog.dismiss();
                    Intent i = new Intent((Context) this, MemberListAll.class);
                    i.putExtra("type", txt);
                    startActivity(i);
                });
                //alert.setCancelable(false);
                alert.show();
            }
        } else {
            Toast.makeText((Context) this, "You don't have permission for this option", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateProfileImage() {
        MyUtil.setGlideImage(SharedPrefs.getValue(this, SharedPrefs.USER_PROFILE_PIC), imgProfile, this);
    }


    private void appLockHandler() {
        String isLockEnable = SharedPrefs.getValue(this, SharedPrefs.IS_ALLOWED_APP_LOCK);
        if (MyUtil.isNN(isLockEnable)) {
            Intent intent = new Intent((Context) this, EnterPinActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//            shimmer.setVisibility(View.VISIBLE);
        networkCallUsingVolleyApi(Constants.URL.BALANCE_UPDATE, false);
        updateProfileImage();
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
        return map;
    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
//        shimmer.setVisibility(View.GONE);
        try {
            JSONObject jsonObject = new JSONObject(JSonResponse);
            JSONObject userObject = new JSONObject(jsonObject.getString("data"));
            loanId = userObject.getString("loanId").trim();

            Log.d("loanId :>>>", "---" + loanId);

            SharedPreferences sharedPref = getSharedPreferences("loan", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("loanId", loanId);
            editor.apply();


            if (userObject.has("mainwallet"))
                SharedPrefs.setValue(this, SharedPrefs.MAIN_WALLET, userObject.getString("mainwallet"));
            else
                SharedPrefs.setValue(this, SharedPrefs.MAIN_WALLET, userObject.getString("balance"));
            if (userObject.has("microatmbalance")) {
                SharedPrefs.setValue(this, SharedPrefs.MICRO_ATM_BALANCE, userObject.getString("microatmbalance"));
            } else {
                SharedPrefs.setValue(this, SharedPrefs.MICRO_ATM_BALANCE, "NO");
            }

            if (userObject.has("kuberwallet")) {
                SharedPrefs.setValue(this, SharedPrefs.KUBER_BALANCE, userObject.getString("kuberwallet"));
            } else {
                SharedPrefs.setValue(this, SharedPrefs.KUBER_BALANCE, "NO");
            }

            SharedPrefs.setValue(this, SharedPrefs.APES_BALANCE, userObject.getString("aepsbalance"));

            if (userObject.has("forceUpdate"))
                SharedPrefs.setValue(this, SharedPrefs.IS_FORCE_UPDATE_ENABLE, userObject.getString("forceUpdate"));
            else
                SharedPrefs.setValue(this, SharedPrefs.IS_FORCE_UPDATE_ENABLE, "");

            if (userObject.has("versioncode"))
                SharedPrefs.setValue(this, SharedPrefs.APP_VERSION_CODE, userObject.getString("versioncode"));
            else
                SharedPrefs.setValue(this, SharedPrefs.APP_VERSION_CODE, "");

            initBalance();
            isUpdateAvailable();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFailRequest(String msg) {
//        shimmer.setVisibility(View.GONE);
    }

    private void confirmPopup() {
        AlertDialog.Builder builder = new AlertDialog.Builder((Context) this)
                .setTitle("Confirm")
                .setMessage("Do you really want to exit?")
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> finish())
                .setNegativeButton(android.R.string.no, (dialog, whichButton) -> alert.dismiss());
        alert = builder.create();
        alert.show();
    }

    private void isUpdateAvailable() {
        if (alert != null && alert.isShowing()) alert.dismiss();
        int appVCode = BuildConfig.VERSION_CODE;
        String serverVersionCode = SharedPrefs.getValue(this, SharedPrefs.APP_VERSION_CODE);
        String isForceUpdate = SharedPrefs.getValue(this, SharedPrefs.IS_FORCE_UPDATE_ENABLE);
        if (MyUtil.isNN(serverVersionCode)) {
            try {
                int code = Integer.parseInt(serverVersionCode);
                if (code > appVCode) {
                    AlertDialog.Builder builder = new MaterialAlertDialogBuilder(this, R.style.ThemeOverlay_App_MaterialAlertDialog)
                            .setTitle("Update")
                            .setMessage("A new version is available please update you application")
                            .setPositiveButton("OK", (dialog, whichButton) -> {
                                dialog.dismiss();
                                openPlayStore();
                            });
                    if (!isForceUpdate.equalsIgnoreCase("yes")) {
                        builder.setNegativeButton("Cancel", (dialog, whichButton) -> alert.dismiss());
                    }
                    alert = builder.create();
                    alert.setCancelable(false);
                    alert.show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void intiBankingRecycler() {
        BankingServiceAdapter bankingServiceAdapter = new BankingServiceAdapter(DashBoardActivity.this,
                MainLocalData.getBankingService());
        bankingServiceRecycler.setAdapter(bankingServiceAdapter);

        bankingServiceRecycler.addOnItemTouchListener(new RecyclerTouchListener(this,
                bankingServiceRecycler, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent i;
                ActivityListModel model = MainLocalData.getBankingService().get(position);
                switch (model.getTxt1()) {
                    case "ICICI Kiosk Services":
//                        ExtensionFunction.startActivity(this, OnBoardingActivity.class);
                        i = new Intent((Context) DashBoardActivity.this, HandlerActivity.class);
                        i.putExtra("appUserId", SharedPrefs.getValue(DashBoardActivity.this, SharedPrefs.USER_ID));
                        i.putExtra("type", "aeps");
                        startActivityForResult(i, 5000);
                        break;

                    case "Kiosk Services 2.O":

                        aepsOnboardingCheck();

                        break;
                    case "DMT 2.O":

                        startActivity(new Intent(DashBoardActivity.this, PGSearchAccount.class).putExtra("dmtType", "PDmt"));

                        break;
                    case "Mini-ATM":
                        i = new Intent((Context) DashBoardActivity.this, HandlerActivity.class);
                        i.putExtra("appUserId", SharedPrefs.getValue(DashBoardActivity.this, SharedPrefs.USER_ID));
                        i.putExtra("type", "matm");
                        startActivityForResult(i, 5000);
                        break;
                    case "Money Transfer":
                        startActivity(new Intent((Context) DashBoardActivity.this, DMTSearchAccount.class));
                        break;
                    case "Insta Account Opening":
                        serviceList = new ArrayList<>();
                        serviceList.add(new ServiceDialogModel("Icici Bank", R.drawable.icic, true));
                        serviceList.add(new ServiceDialogModel("Kotak Mahindra Bank", R.drawable.kotak_icon, false));
                        serviceList.add(new ServiceDialogModel("IndusInd Bank", R.drawable.indus_icon, false));
                        openDialogBox("Icici Bank", "Select Bank", serviceList);
                        break;
                }
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));
    }

    private void aepsOnboardingCheck() {

        new VolleyNetworkCall(new RequestResponseLis() {
            @Override
            public void onSuccessRequest(String JSonResponse) {

                Log.d("Response aeps :", "" + JSonResponse);

                try {
                    JSONObject json = new JSONObject(JSonResponse);

                    JSONObject jsonObject = json.getJSONObject("data");
                    Log.d("Response aeps dara:", "" + json.getJSONObject("data"));

                    String pId = jsonObject.getString("pId");
                    String pApiKey = jsonObject.getString("pApiKey");
                    String mCode = jsonObject.getString("mCode");
                    String mobile = jsonObject.getString("mobile");
                    String firm = jsonObject.getString("firm");
                    String email = jsonObject.getString("email");
                    String lat = jsonObject.getString("lat");
                    String lng = jsonObject.getString("lng");

                    SharedPrefs.setValue(DashBoardActivity.this, SharedPrefs.PKEY, pId);
                    SharedPrefs.setValue(DashBoardActivity.this, SharedPrefs.PAPIKEY, pApiKey);
                    SharedPrefs.setValue(DashBoardActivity.this, SharedPrefs.MCODE, mCode);
                    SharedPrefs.setValue(DashBoardActivity.this, SharedPrefs.LAT, lat);
                    SharedPrefs.setValue(DashBoardActivity.this, SharedPrefs.LONG, lng);


                    Log.d("Response pId:", "" + pId);

                    if (json.has("pasprintonboard")) {
                        if (json.get("pasprintonboard").equals("pending")) {
                            Toast.makeText(DashBoardActivity.this, "On boarding pending for approval", Toast.LENGTH_SHORT).show();
                        } else if (json.get("pasprintonboard").equals("false")) {

                            openPSOnboarding();

                            return;
                        } else if (json.get("pasprintonboard").equals("true")) {

                            Intent i2 = new Intent(DashBoardActivity.this, AepsDashboard.class);
                            i2.putExtra("userId", SharedPrefs.getValue(DashBoardActivity.this, SharedPrefs.USER_ID));
                            i2.putExtra("appToken", SharedPrefs.getValue(DashBoardActivity.this, SharedPrefs.APP_TOKEN));
                            i2.putExtra("aepsType", "PAYSPRINT");
                            i2.putExtra("baseUrl", Constants.URL.BASE_URL);
                            startActivity(i2);

                        }
                    }

                    Log.d("Response aeps dara:", "" + json.getJSONObject("data"));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailRequest(String msg) {
            }
        }, this, Constants.URL.AEPS_ONBOARD, 1, param(), true).netWorkCall();

    }

    private void openPSOnboarding() {
       // Toast.makeText(this, "Host", Toast.LENGTH_SHORT).show();
        Log.d("Response pId", SharedPrefs.getValue(DashBoardActivity.this, SharedPrefs.PKEY));
        Log.d("Response pApiKey", SharedPrefs.getValue(DashBoardActivity.this, SharedPrefs.PAPIKEY));
        Log.d("Response mCode", SharedPrefs.getValue(DashBoardActivity.this, SharedPrefs.MCODE));
        Log.d("Response mobile", SharedPrefs.getValue(DashBoardActivity.this, SharedPrefs.USER_CONTACT));
        Log.d("Response lat", SharedPrefs.getValue(DashBoardActivity.this, SharedPrefs.LAT));
        Log.d("Response lng", SharedPrefs.getValue(DashBoardActivity.this, SharedPrefs.LONG));
        Log.d("Response shop", SharedPrefs.getValue(DashBoardActivity.this, SharedPrefs.SHOP_NAME));
        Log.d("Response email", SharedPrefs.getValue(DashBoardActivity.this, SharedPrefs.USER_EMAIL));
        Intent intent = new Intent(DashBoardActivity.this, HostActivity.class);
        intent.putExtra("pId", SharedPrefs.getValue(DashBoardActivity.this, SharedPrefs.PKEY));
        intent.putExtra("pApiKey", SharedPrefs.getValue(DashBoardActivity.this, SharedPrefs.PAPIKEY));
        intent.putExtra("mCode", SharedPrefs.getValue(DashBoardActivity.this, SharedPrefs.MCODE));
        intent.putExtra("mobile", SharedPrefs.getValue(DashBoardActivity.this, SharedPrefs.USER_CONTACT));
        intent.putExtra("lat", SharedPrefs.getValue(DashBoardActivity.this, SharedPrefs.LAT));
        intent.putExtra("lng", SharedPrefs.getValue(DashBoardActivity.this, SharedPrefs.LONG));
        intent.putExtra("firm", SharedPrefs.getValue(DashBoardActivity.this, SharedPrefs.SHOP_NAME));
        intent.putExtra("email", SharedPrefs.getValue(DashBoardActivity.this, SharedPrefs.USER_EMAIL));
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivityForResult(intent, 999);
    }

    private void openDialogBox(String defaultOption, String title, ArrayList<ServiceDialogModel> serviceList) {
        selectedOption = defaultOption;

        LayoutInflater factory = LayoutInflater.from((Context) this);
        final View view = factory.inflate(R.layout.custom_service_dialog, null);

        serviceRecyclerView = view.findViewById(R.id.serviceRecyclerView);

        ExtensionFunction.setLayoutManager(serviceRecyclerView, this);
        dialogRecyclerAdapter = new ServiceDialogRecyclerAdapter(serviceList, this);
        serviceRecyclerView.setAdapter(dialogRecyclerAdapter);


        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder((Context) this);
        alertDialog.setView(view);

        alertDialog.setTitle(title);
        /*alertDialog.setSingleChoiceItems(dialogItemList,0 , (dialog, which) -> {
            selectedOption = dialogItemList[which];
        });*/

        alertDialog.setPositiveButton("Proceed", (dialog, which) -> {

            if (selectedOption != null && !selectedOption.equals("")) {
                if (selectedOption.equalsIgnoreCase("Icici Bank")) {
                    ExtensionFunction.openBrowser("https://buy.icicibank.com/", this);
//                    openWebView("");
                } else if (selectedOption.equalsIgnoreCase("Kotak Mahindra Bank")) {
                    ExtensionFunction.openBrowser("https://bit.ly/3klBVEwACopenkotakPsispl", this);
                } else if (selectedOption.equalsIgnoreCase("IndusInd Bank")) {
                    ExtensionFunction.openBrowser("https://myaccount.indusind.com/", this);
                } else if (selectedOption.equalsIgnoreCase("IndusInd Fast Tag")) {
                    ExtensionFunction.openBrowser("https://fastag.indusind.com/agent/", this);
                } else if (selectedOption.equalsIgnoreCase("Kotak Fast Tag")) {
                    ExtensionFunction.openBrowser("https://fastag.kotak.com/agent/", this);
                } else if (selectedOption.equalsIgnoreCase("SBI Fast Tag")) {
                    ExtensionFunction.openBrowser("https://fastag.onlinesbi.com/AGENTADMIN/login", this);
                }

            } else {
                ExtensionFunction.showToast(this, "Select the Field");

            }
        });


        alertDialog.setNegativeButton("Cancle", (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }

    private void intiEcommerceRecycler() {
        ecommerceAdapter = new OnlineServiceAdapter(MainLocalData.getEcommerceService(this), this);
        ecommerceRecycler.setAdapter(ecommerceAdapter);
    }

    private void intiLendingRecycler() {
        lendingAdapter = new OnlineServiceAdapter(MainLocalData.getLendingService(this), this);
        lendingServiceRecycler.setAdapter(lendingAdapter);
    }

    private void intiTourTravelRecycler() {
        tourAdapter = new OnlineServiceAdapter(MainLocalData.getToursTravelService(this), this);
        tourTravelRecycler.setAdapter(tourAdapter);
    }

    private void intiGovernanceRecycler() {
        governanceAdapter = new OnlineServiceAdapter(MainLocalData.getGoverenceService(this), this);
        governanceRecycler.setAdapter(governanceAdapter);
    }

    private void intiInsuranceRecycler() {
        insuranceAdapter = new OnlineServiceAdapter(MainLocalData.getInsuranceService(this), this);
        insuranceRecycler.setAdapter(insuranceAdapter);
    }

    private void openPlayStore() {
        final String appPackageName = getPackageName();
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }


    @Override
    public void onlineServiceItemClicked(int postion, String msg, String type, Boolean openWithBrowser) {

        if (type.equalsIgnoreCase("Fast Tag")) {
            serviceList = new ArrayList<>();
            serviceList.add(new ServiceDialogModel("IndusInd Fast Tag", R.drawable.indus_icon, true));
            serviceList.add(new ServiceDialogModel("Kotak Fast Tag", R.drawable.kotak_icon, false));
            serviceList.add(new ServiceDialogModel("SBI Fast Tag", R.drawable.sbi, false));
            openDialogBox("IndusInd Fast Tag", "Fast Tag", serviceList);

        } else if (type.equalsIgnoreCase("url")) {
            if (openWithBrowser) {
                ExtensionFunction.openBrowser(msg, this);
            } else {
                openWebView(msg);
            }

        } else {
            ExtensionFunction.openMessageDialog(this, msg);
        }


    }

    private void openWebView(String msg) {
        Intent intent = new Intent((Context) this, WebViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("url", msg);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onItemDialogItemClicked(int position, Boolean flag, String value) {
        selectedOption = value;
        for (ServiceDialogModel data : serviceList) {
            data.setSelected(false);
        }
        serviceList.get(position).setSelected(true);
        dialogRecyclerAdapter.notifyDataSetChanged();
    }


}
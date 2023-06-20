package com.digital.payandserve.online_service;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.payandserve.R;
import com.digital.payandserve.online_service.model.OnlienServiceModel;
import com.digital.payandserve.utill.ExtensionFunction;

import java.util.ArrayList;

public class OnlineServicesActivity extends AppCompatActivity implements OnlineServiceAdapter.OnlineServiceClickListner {

    RecyclerView bankingServiceRecycler,lendingServiceRecycler,
            governanceRecycler,fatTagRecycler,ecommerceRecycler,tourTravelRecycler;

    OnlineServiceAdapter bankingAdapter, lendingAdapter,governanceAdapter,fastTagAdapter,
    ecommerceAdapter,tourAdapter;

    ArrayList<OnlienServiceModel> bankingList,lendingList,governanceList,fastTagList,ecommerceList,
    tourList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_services);

        bankingServiceRecycler  = findViewById(R.id.bankingServiceRecycler);
        lendingServiceRecycler  = findViewById(R.id.lendingServiceRecycler);
        governanceRecycler  = findViewById(R.id.governanceRecycler);
        fatTagRecycler  = findViewById(R.id.fatTagRecycler);
        ecommerceRecycler  = findViewById(R.id.ecommerceRecycler);
        tourTravelRecycler  = findViewById(R.id.tourTravelRecycler);

        ExtensionFunction.setGridLayoutManager(bankingServiceRecycler,this,3);
        ExtensionFunction.setGridLayoutManager(lendingServiceRecycler,this,3);
        ExtensionFunction.setGridLayoutManager(governanceRecycler,this,3);
        ExtensionFunction.setGridLayoutManager(fatTagRecycler,this,3);
        ExtensionFunction.setGridLayoutManager(ecommerceRecycler,this,3);
        ExtensionFunction.setGridLayoutManager(tourTravelRecycler,this,3);
/*
        intiBankingRecycler();
        intiLendingRecycler();
        intiGovernanceRecycler();
        intifastTagRecycler();
        intiEcommerceRecycler();
        intiTourTravelRecycler();*/

    }

    @Override
    public void onlineServiceItemClicked(int postion, String msg, String type, Boolean openWithBrowser) {

    }
/*

    private void intiBankingRecycler() {
        bankingList = new ArrayList<>();
        bankingList.add(new OnlienServiceModel(getResources().getDrawable(R.drawable.icic),"Insta Account Opening","https://buy.icicibank.com/","url"));
        bankingList.add(new OnlienServiceModel(getResources().getDrawable(R.drawable.kotak_icon),"Insta Account Opening","https://bit.ly/3klBVEwACopenkotakPsispl","url"));
        bankingList.add(new OnlienServiceModel(getResources().getDrawable(R.drawable.indus_icon),"Insta Account Opening","https://bit.ly/3fzM4KF","url"));
        bankingAdapter = new OnlineServiceAdapter(bankingList,this);
        bankingServiceRecycler.setAdapter(bankingAdapter);
    }

    private void intiEcommerceRecycler() {
        ecommerceList = new ArrayList<>();
        ecommerceList.add(new OnlienServiceModel(getResources().getDrawable(R.drawable.amazon),"Amazon Store","https://amzn.to/3rssDYS/","url"));
        ecommerceList.add(new OnlienServiceModel(getResources().getDrawable(R.drawable.shopping),"Online Shopping","https://bit.ly/PayandServeOnlineOrderPortal","url"));
        ecommerceAdapter = new OnlineServiceAdapter(ecommerceList,this);
        ecommerceRecycler.setAdapter(ecommerceAdapter);
    }

    private void intiLendingRecycler() {
        lendingList = new ArrayList<>();
        lendingList.add(new OnlienServiceModel(getResources().getDrawable(R.drawable.personal_loan),"Personal Loan","https://bit.ly/Personalloanpsispl","url"));
        lendingList.add(new OnlienServiceModel(getResources().getDrawable(R.drawable.bussiness_loan),"Business Loan","https://bit.ly/Personalloanpsispl","url"));
        lendingList.add(new OnlienServiceModel(getResources().getDrawable(R.drawable.home_loan),"Home Loan","https://bit.ly/Personalloanpsispl","url"));
        lendingList.add(new OnlienServiceModel(getResources().getDrawable(R.drawable.credit),"Apply For Credit Card","https://bit.ly/3yYbWHtcreditcardICICI","url"));
        lendingAdapter = new OnlineServiceAdapter(lendingList,this);
        lendingServiceRecycler.setAdapter(lendingAdapter);
    }

    private void intiTourTravelRecycler() {
        tourList = new ArrayList<>();
        tourList.add(new OnlienServiceModel(getResources().getDrawable(R.drawable.flight),"Flight Booking","For this service activation contact our Sales Team +91 9504-06-205 ","msg"));
        tourList.add(new OnlienServiceModel(getResources().getDrawable(R.drawable.bus_booking),"Bus Booking","For this service activation contact our Sales Team +91 9504-06-205 /","msg"));
        tourList.add(new OnlienServiceModel(getResources().getDrawable(R.drawable.hotel_booking),"Hotel booking","For this service activation contact our Sales Team +91 9504-06-205 ","msg"));
        tourList.add(new OnlienServiceModel(getResources().getDrawable(R.drawable.irctc),"IRCTC","For this service activation contact our Sales Team ","msg"));
        tourAdapter = new OnlineServiceAdapter(tourList,this);
        tourTravelRecycler.setAdapter(tourAdapter);
    }

    private void intiGovernanceRecycler() {
        governanceList = new ArrayList<>();
        governanceList.add(new OnlienServiceModel(getResources().getDrawable(R.drawable.ayushman),"PMJAY- Ayushman card","https://www.pmjay.utiitsl.com/pmjayecard/","url"));
        governanceList.add(new OnlienServiceModel(getResources().getDrawable(R.drawable.pan),"Pan Card","https://www.psaonline.utiitsl.com/psaonline/showLogin","url"));
        governanceList.add(new OnlienServiceModel(getResources().getDrawable(R.drawable.income_tax),"Income Tax Services","For this service activation contact our Sales Team +91 9504-06-205 ","msg"));
        governanceList.add(new OnlienServiceModel(getResources().getDrawable(R.drawable.gst),"GST Services","For this service activation contact our Sales Team +91 9504-06-205","msg"));
        governanceAdapter = new OnlineServiceAdapter(governanceList,this);
        governanceRecycler.setAdapter(governanceAdapter);
    }

    private void intifastTagRecycler() {

        fastTagList = new ArrayList<>();
        fastTagList.add(new OnlienServiceModel(getResources().getDrawable(R.drawable.indus_icon),"IndusInd Fast Tag","https://fastag.indusind.com/agent/","url",true));
        fastTagList.add(new OnlienServiceModel(getResources().getDrawable(R.drawable.kotak_icon),"Kotak Fast Tag","https://fastag.kotak.com/agent/","url",true));
        fastTagList.add(new OnlienServiceModel(getResources().getDrawable(R.drawable.sbi),"SBI Fast Tag","https://fastag.onlinesbi.com/AGENTADMIN/login","url",true));
        fastTagAdapter = new OnlineServiceAdapter(fastTagList,this);
        fatTagRecycler.setAdapter(fastTagAdapter);
    }


*/
}
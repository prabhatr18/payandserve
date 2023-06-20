package com.digital.payandserve.views.tutorial;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.digital.payandserve.R;
import com.digital.payandserve.app.AppManager;
import com.digital.payandserve.app.Constants;
import com.digital.payandserve.network.RequestResponseLis;
import com.digital.payandserve.network.VolleyNetworkCall;
import com.digital.payandserve.online_service.WebViewActivity;
import com.digital.payandserve.utill.AppHandler;
import com.digital.payandserve.utill.ExtensionFunction;
import com.digital.payandserve.utill.Print;
import com.digital.payandserve.views.operator.model.OperatorModel;
import com.digital.payandserve.views.tutorial.model.TutorialModel;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;


public class TutorialActivity extends AppCompatActivity implements TutorialAdapter.OnTutorialItemClickListner, RequestResponseLis {

    TutorialAdapter adapter;
    ArrayList<TutorialModel> tutorialList;
    RecyclerView tutorialRecycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        tutorialRecycler = findViewById(R.id.tutorialRecycler);

        tutorialRecycler.setLayoutManager(new LinearLayoutManager(this));

        tutorialList = new ArrayList<>();

        networkCallUsingVolleyApi(Constants.URL.VIDEO_LINK, true);
        initTutorial();
    }



    private void networkCallUsingVolleyApi(String url, boolean isLoad) {
        if (AppManager.isOnline(this)) {
            new VolleyNetworkCall(this, this, url, isLoad).netWorkCall();
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show();
        }
    }

    private void initTutorial() {
        adapter = new TutorialAdapter(tutorialList,this::onTutorialItemClicked);
        tutorialRecycler.setAdapter(adapter);
    }

    @Override
    public void onTutorialItemClicked(int position, String url) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, WebViewActivity.class);
        bundle.putString("url",url);
        intent.putExtras(bundle);
        startActivity(intent);

    }

    @Override
    public void onSuccessRequest(String JSonResponse) {
        Print.P(JSonResponse);
        try {
            JSONObject object = new JSONObject(JSonResponse);
            if (object.has("data")) {
                JSONArray bannerArray = object.getJSONArray("data");

                tutorialList.addAll(AppHandler.parseTutorialVideo(this, bannerArray));
                initTutorial();
            }
        } catch (Exception e) {
            ExtensionFunction.showToast(this,AppHandler.parseExceptionMsg(e));
        }
    }

    @Override
    public void onFailRequest(String msg) {
        Print.P(msg);
    }
}
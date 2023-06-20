package com.payment.aeps.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.payment.aeps.app.ModuleConstants;
import com.payment.aeps.util.ModuleSharedPrefs;
import com.payment.aeps.util.ModuleUtil;
import com.payment.aeps.util.Print;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class VolleyGetNetworkCall {

    private RequestResponseLis listener;
    private Context context;
    private String apiUrl;
    private int method = 0;
    private Map<String, String> map;

    public VolleyGetNetworkCall(RequestResponseLis listener, Context context) {
        this.listener = listener;
        this.context = context;
        apiUrl = ModuleConstants.baseUrl;
    }

    public VolleyGetNetworkCall(RequestResponseLis listener, Context context, String apiUrl) {
        this.listener = listener;
        this.context = context;
        this.apiUrl = apiUrl;
    }

    public VolleyGetNetworkCall(RequestResponseLis listener, Context context, String apiUrl, int method, Map<String, String> map) {
        this.listener = listener;
        this.context = context;
        this.apiUrl = apiUrl;
        this.method = method;
        this.map = map;
    }

    ProgressDialog loaderDialog;

    public void netWorkCall() {
        Print.P("URL : " + apiUrl);
        loaderDialog = new ProgressDialog(context);
        loaderDialog.setMessage("Please wait...");
        loaderDialog.setCancelable(false);
        loaderDialog.show();

        StringRequest sendRequest = new StringRequest(
                method,
                apiUrl,
                Response -> {
                    Print.P("Response  : "+ Response);
                    closeLoader();
                    listener.onSuccessRequest(Response);
                }
                , volleyError -> {
            try {
                String json = null;
                NetworkResponse response = volleyError.networkResponse;
                if (response != null && response.data != null) {
                    apiUrl = apiUrl.replaceAll(ModuleConstants.baseUrl, "");
                    switch (response.statusCode) {
                        case 400:
                            String error = "Status Code : Unexpected response code 400 \nPath : " + apiUrl;
                            json = new String(response.data);
                            json = trimMessage(json, "message");
                            if (json != null)
                                error = error + "\n\n" + json;
                            showNetworkError(error);
                            break;
                        case 404:
                            error = "Response Code 404 for \nPath : " + apiUrl;
                            showNetworkError(error);
                            break;
                        default:
                            error = "Response code " + response.statusCode + " for\nPath : " + apiUrl;
                            showNetworkError(error);
                            break;
                    }
                }
            } catch (Exception error) {
                error.printStackTrace();
                showNetworkError("Some kind of network error encountered");
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                if (map == null) {
                    map = new HashMap<>();
                }
                String t = ModuleSharedPrefs.getValue(context, ModuleSharedPrefs.APP_TOKEN);
                String u = ModuleSharedPrefs.getValue(context, ModuleSharedPrefs.USER_ID);
                if (ModuleUtil.isNotNull(t) && ModuleUtil.isNotNull(u)) {
//                    map.put("apptoken", ModuleSharedPrefs.getValue(context, ModuleSharedPrefs.APP_TOKEN));
//                    map.put("user_id", ModuleSharedPrefs.getValue(context, ModuleSharedPrefs.USER_ID));
                }
                Print.P("Params : " + new JSONObject(map));
                return map;
            }
        };

        sendRequest.setRetryPolicy(new
                DefaultRetryPolicy(
                60000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(sendRequest);
    }

    public interface RequestResponseLis {
        void onSuccessRequest(String JSonResponse);

        void onFailRequest(String msg);
    }

    private void closeLoader() {
        if (loaderDialog != null && loaderDialog.isShowing()) {
            loaderDialog.dismiss();
        }
    }

    private void showNetworkError(String error) {
        closeLoader();
        listener.onFailRequest(error);
        Toast.makeText(context, "" + error, Toast.LENGTH_SHORT).show();
    }

    public String trimMessage(String json, String key) {
        String trimmedString = null;
        try {
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
        return trimmedString;
    }
}

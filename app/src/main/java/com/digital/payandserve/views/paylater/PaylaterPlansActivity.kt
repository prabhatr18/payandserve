package com.digital.payandserve.views.paylater

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.JsResult
import android.widget.Toast
import com.digital.payandserve.R
import com.digital.payandserve.app.AppManager
import com.digital.payandserve.app.Constants
import com.digital.payandserve.databinding.ActivityPayLaterBinding
import com.digital.payandserve.databinding.ActivityPaylaterPlansBinding
import com.digital.payandserve.network.RequestResponseLis
import com.digital.payandserve.network.VolleyNetworkCall
import com.digital.payandserve.utill.Print
import com.digital.payandserve.utill.SharedPrefs
import org.json.JSONObject

class PaylaterPlansActivity : AppCompatActivity(), RequestResponseLis {

    lateinit var binding: ActivityPaylaterPlansBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaylaterPlansBinding.inflate(layoutInflater)
        setContentView(binding.root)

        networkCallUsingVolleyApi(Constants.URL.PAYLATER_PAYMENT, true)

    }


    private fun networkCallUsingVolleyApi(
        url: String,
        isLoad: Boolean,
    ) {
        if (AppManager.isOnline(this)) {
            // new VolleyPostNetworkCall(this, this, url, isLoad).netWorkCall(param());
            VolleyNetworkCall(this, this, url, 1, param(), isLoad).netWorkCall();
        } else {
            Toast.makeText(this, "Network connection error", Toast.LENGTH_LONG).show()
        }
    }

    private fun param(): Map<String, String> {
        val map: MutableMap<String, String> = HashMap()
        map["apptoken"] = SharedPrefs.getValue(this, SharedPrefs.APP_TOKEN)
        map["user_id"] = SharedPrefs.getValue(this, SharedPrefs.USER_ID)
        map["mobile"] = SharedPrefs.getValue(this, SharedPrefs.USER_CONTACT)
        map["type"] = "getplan"
        return map
    }

    override fun onSuccessRequest(JSonResponse: String?) {
        try {
            Print.P("Response => $JSonResponse")
            val jsonObject = JSONObject(JSonResponse)

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onFailRequest(msg: String?) {
        Print.P(msg)
    }
}
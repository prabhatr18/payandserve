package com.digital.payandserve.views.paylater

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.digital.payandserve.app.AppManager
import com.digital.payandserve.app.Constants.URL.PAYLATER_ONBOARD
import com.digital.payandserve.databinding.ActivityPayLaterBinding
import com.digital.payandserve.network.RequestResponseLis
import com.digital.payandserve.network.VolleyNetworkCall
import com.digital.payandserve.utill.AppHandler
import com.digital.payandserve.utill.Print
import com.digital.payandserve.utill.SharedPrefs
import org.json.JSONObject

class PayLaterActivity : AppCompatActivity(), RequestResponseLis {
    lateinit var binding: ActivityPayLaterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPayLaterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSubmit.setOnClickListener {
            networkCallUsingVolleyApi(PAYLATER_ONBOARD, true)
        }

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
        map["mobile"] = binding.etMobile.text.toString()
        map["firstName"] = binding.etFName.text.toString()
        map["middleName"] = binding.etMiddleName.text.toString()
        map["lastName"] = binding.etLastName.text.toString()
        map["Email"] = binding.etEmail.text.toString()
        map["gender"] = binding.etGender.text.toString()
        map["line1"] = binding.etLine.text.toString()
        map["city"] = binding.etCity.text.toString()
        map["state"] = binding.etState.text.toString()
        map["birthday"] = binding.etDob.text.toString()
        return map
    }

    override fun onSuccessRequest(JSonResponse: String?) {
        try {
            val jsonObject = JSONObject(JSonResponse)
            val status = jsonObject.getString("statuscode")
            if (status.equals("TXN", ignoreCase = true)) {
                confirmPopup(AppHandler.getMessage(JSonResponse))
            } else {
                Toast.makeText(this, "${AppHandler.getMessage(JSonResponse)}", Toast.LENGTH_SHORT)
                    .show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onFailRequest(msg: String?) {
        Print.P(msg)
    }

    private fun confirmPopup(msg: String) {
        val builder1 = AlertDialog.Builder(this)
        builder1.setMessage(msg)
        builder1.setCancelable(false)
        builder1.setPositiveButton(
            "OK"
        ) { dialog, id ->
            dialog.cancel()
            finish()
        }
        val alert11 = builder1.create()
        alert11.show()
    }
}


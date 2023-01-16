package com.example.loginapirest

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.example.loginapirest.databinding.ActivityMainBinding
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        mBinding.swType.setOnCheckedChangeListener { button, checked ->
            button.text = if (checked) getString(R.string.login) else getString(R.string.register)
            mBinding.btnLogin.text = button.text
        }
        mBinding.btnLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val typeMethod =
            if (mBinding.swType.isChecked) Constants.LOGIN_PATH else Constants.REGISTER_PATH

        val url = Constants.BASE_URL + Constants.API_PATH + typeMethod
        val email = mBinding.etEmail.text?.trim().toString()
        val password = mBinding.etPassword.text?.trim().toString()
        val jsonParams = JSONObject()

        if (email.isNotEmpty()) {
            jsonParams.put(Constants.EMAIL_PARAM, email)
        }

        if (password.isNotEmpty()) {
            jsonParams.put(Constants.PASSWORD_PARAM, password)
        }
        val jsonObjectRequest =
            object : JsonObjectRequest(Request.Method.POST, url, jsonParams, { response ->
                Log.i("response", response.toString())
                val id = response.optString(Constants.ID_PROPERTY, Constants.ERROR_VALUE)
                val token = response.optString(Constants.TOKEN_PROPERTY, Constants.ERROR_VALUE)

                val result =
                    if (id.equals(Constants.ERROR_VALUE)) "${Constants.TOKEN_PROPERTY}: $token"
                    else "${Constants.ID_PROPERTY}: $id"
                updateUI(result)
            }, {
                it.printStackTrace()
                if (it.networkResponse.statusCode == 400) {
                    updateUI(getString(R.string.name_error_server))
                }
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val params = HashMap<String, String>()
                    params["Content-Type"] = "application/json"

                    return params
                }

            }
        LoginApplication.reqresAPI.addToRequestQueue(jsonObjectRequest)

    }

    private fun updateUI(result: String) {
        mBinding.tvResult.visibility = View.VISIBLE
        mBinding.tvResult.text = result
    }
}
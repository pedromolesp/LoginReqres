package com.example.loginapirest

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.loginapirest.databinding.ActivityMainBinding

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
        updateUI("(^_^)")
    }

    private fun updateUI(result: String) {
        mBinding.tvResult.visibility = View.VISIBLE
        mBinding.tvResult.text = result
    }
}
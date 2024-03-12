package com.app.userecommerce.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.core.content.ContextCompat
import com.app.userecommerce.R
import com.app.userecommerce.Utils
import com.app.userecommerce.databinding.ActivityLoginBinding
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)



        binding.gotoSignUpBtn.setOnClickListener {
            openSignup()
        }

        Utils.getCorrectUserPhoneNumberSize(this@LoginActivity,binding.etUserNumber,binding.logInBtn)

        binding.logInBtn.setOnClickListener {
            validateUser()

        }


    }

    private fun validateUser() {
        val number = binding.etUserNumber.text.toString()
        if (binding.etUserNumber.text!!.isEmpty() || number.length != 10) {
            Utils.showToast(this, "Please fill all fields")

        } else {
            sendOtp(number)
        }
    }

    private fun sendOtp(number: String) {
        Utils.showDialog(this, "Sending OTP...")

        val options = PhoneAuthOptions.newBuilder(FirebaseAuth.getInstance())
            .setPhoneNumber("+91$number") // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(this) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)

    }

    val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        override fun onVerificationCompleted(credential: PhoneAuthCredential) {

        }

        override fun onVerificationFailed(e: FirebaseException) {

        }

        override fun onCodeSent(
            verificationId: String,
            token: PhoneAuthProvider.ForceResendingToken,
        ) {
            Utils.hideDialog()
            Utils.showToast(this@LoginActivity, "OTP sent")
            val intent = Intent(this@LoginActivity, OTPActivity::class.java)
            intent.putExtra("verificationId", verificationId)
            intent.putExtra("number", binding.etUserNumber.text.toString())
            startActivity(intent)
            finish()
        }
    }



    private fun openSignup() {
        startActivity(Intent(this, RegisterActivity::class.java))
        finish()
    }


}
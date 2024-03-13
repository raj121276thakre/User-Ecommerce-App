package com.app.userecommerce.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.app.userecommerce.MainActivity
import com.app.userecommerce.R
import com.app.userecommerce.Utils
import com.app.userecommerce.databinding.ActivityLoginBinding
import com.app.userecommerce.databinding.ActivityOtpactivityBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

class OTPActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOtpactivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpactivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Utils.getCorrectOTPSize(this@OTPActivity, binding.userOTP, binding.verifyOtpBtn)

        binding.verifyOtpBtn.setOnClickListener {
            if (binding.userOTP.text!!.isEmpty()) {
                Utils.showToast(this, "Please enter otp")
            } else {
                verifyUser(binding.userOTP.text.toString())
            }

        }

    }

    private fun verifyUser(otp: String) {
        Utils.showDialog(this@OTPActivity, "Verifying please wait")
        val credential =
            PhoneAuthProvider.getCredential(intent.getStringExtra("verificationId")!!, otp)
        signInWithPhoneAuthCredential(credential)
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Utils.hideDialog()

                    var userNumber = intent.getStringExtra("number")!!

                    val preferences = this.getSharedPreferences("user", MODE_PRIVATE)
                    val editor = preferences.edit()
                    editor.putString("number", userNumber)
                    editor.apply()

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()


                } else {
                    Utils.hideDialog()

                    Utils.showToast(this, "Something went wrong")
                }
            }
    }


}















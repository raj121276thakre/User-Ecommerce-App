package com.app.userecommerce.auth

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.app.userecommerce.R
import com.app.userecommerce.Utils
import com.app.userecommerce.databinding.ActivityRegisterBinding
import com.app.userecommerce.model.UserModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.gotoLoginBtn.setOnClickListener {
            openLogin()
        }

        Utils.getCorrectUserPhoneNumberSize(this@RegisterActivity,binding.userNumber,binding.signUpBtn)
        binding.signUpBtn.setOnClickListener {
            validateUser()

        }


    }


    private fun validateUser() {
        val number = binding.userNumber.text.toString()
        if (binding.userName.text!!.isEmpty() || binding.userNumber.text!!.isEmpty() || number.length != 10) {
            Utils.showToast(this, "Please fill all fields")

        } else {
            storeData()
        }
    }

    private fun storeData() {
        Utils.showDialog(this, "Loading please wait...")

        var userName = binding.userName.text.toString()
        var userNumber = binding.userNumber.text.toString()

        val preferences = this.getSharedPreferences("user", MODE_PRIVATE)//user
        val editor = preferences.edit()
        editor.putString("name", userName)
        editor.putString("number", userNumber)
        editor.apply()

        val data = UserModel(userName = userName, userPhoneNumber = userNumber)

        Firebase.firestore.collection("user").document(binding.userNumber.text.toString())
            .set(data)
            .addOnSuccessListener {
                Utils.hideDialog()
                Utils.showToast(this, "User registered")
                openLogin()
            }
            .addOnFailureListener {
                Utils.hideDialog()
                Utils.showToast(this, "Something went wrong")
            }
    }

    private fun openLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }




}
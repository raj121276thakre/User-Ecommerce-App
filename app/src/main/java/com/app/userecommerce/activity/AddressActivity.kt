package com.app.userecommerce.activity

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.app.userecommerce.R
import com.app.userecommerce.Utils
import com.app.userecommerce.databinding.ActivityAddressBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class AddressActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddressBinding

    private lateinit var  preferences : SharedPreferences //
    private lateinit var  totalCost : String //
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatusBarColor()

        preferences = this.getSharedPreferences("user", MODE_PRIVATE) //

        totalCost = intent.getStringExtra("totalCost")!!

        loadUserInfo()

        binding.proceedBtn.setOnClickListener {
            validateData(
                binding.userNumber.text.toString(),
                binding.userName.text.toString(),
                binding.userPincode.text.toString(),
                binding.userCity.text.toString(),
                binding.userState.text.toString(),
                binding.userVillage.text.toString(),
            )
        }


    }

    private fun validateData(
        number: String,
        name: String,
        pinCode: String,
        city: String,
        state: String,
        village: String
    ) {

        if (number.isEmpty() || name.isEmpty() || pinCode.isEmpty() || city.isEmpty() || state.isEmpty() || village.isEmpty()) {

            Utils.showToast(this@AddressActivity, "Please fill all fields")
        } else {

            Utils.showDialog(this@AddressActivity,"Loading please wait")
            storeData(
                pinCode,
                city,
                state,
                village
            )
        }

    }

    private fun storeData(pinCode: String, city: String, state: String, village: String) {

        val map = hashMapOf<String, Any>()
        map["pinCode"] = pinCode
        map["city"] = city
        map["state"] = state
        map["village"] = village

        Firebase.firestore.collection("users")
            .document(preferences.getString("number", "")!!)
            .update(map)
            .addOnSuccessListener {
                Utils.hideDialog()
               // Utils.showToast(this@AddressActivity, "Address saved")

                //goto checkoutActivity
                val bundle = Bundle()
                bundle.putStringArrayList("productIds",intent.getStringArrayListExtra("productIds"))
                bundle.putString("totalCost",totalCost)

                val intent = Intent(this, CheckoutActivity::class.java)
                intent.putExtras(bundle)
               startActivity(intent)

            }.addOnFailureListener {
                Utils.hideDialog()
                Utils.showToast(this@AddressActivity, "Something went wrong")
            }
    }


    private fun loadUserInfo() {


        Firebase.firestore.collection("users")
            .document(preferences.getString("number", "9786567545")!!)
            .get().addOnSuccessListener {

                binding.userName.setText(it.getString("userName"))
                binding.userNumber.setText(it.getString("userPhoneNumber"))
                binding.userVillage.setText(it.getString("village"))
                binding.userCity.setText(it.getString("city"))
                binding.userPincode.setText(it.getString("pinCode"))
                binding.userState.setText(it.getString("state"))

            }.addOnFailureListener {

                Utils.showToast(this@AddressActivity, "Something went wrong")
            }
    }


    //set status bar color of activity
    private fun setStatusBarColor() {
        window?.apply {
            val statusBarColors = ContextCompat.getColor(this@AddressActivity, R.color.yellow)
            statusBarColor = statusBarColors
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }

}










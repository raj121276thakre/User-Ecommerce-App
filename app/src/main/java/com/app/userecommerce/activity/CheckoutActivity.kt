package com.app.userecommerce.activity

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.app.userecommerce.MainActivity
import com.app.userecommerce.R
import com.app.userecommerce.Utils
import com.app.userecommerce.databinding.ActivityCheckoutBinding
import com.app.userecommerce.roomdb.AppDatabase
import com.app.userecommerce.roomdb.ProductModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class CheckoutActivity : AppCompatActivity(), PaymentResultListener {
    private lateinit var binding: ActivityCheckoutBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatusBarColor()

        val checkout = Checkout()
        checkout.setKeyID(getString(R.string.razorpay_Api_key));

        //checkout.setKeyID("<YOUR_KEY_ID>");

        val price = intent.getStringExtra("totalCost")

        try {
            val options = JSONObject()
            options.put("name", getString(R.string.app_name))
            options.put("description", getString(R.string.razorpay_Description))
            //You can omit the image option to fetch the image from the dashboard
            options.put("image", getString(R.string.razorpay_App_image))
            options.put("theme.color", getString(R.string.razorpay_color));
            options.put("currency", "INR");
            options.put("amount", (price!!.toInt() * 100))//pass amount in currency subunits

            val prefill = JSONObject()
            prefill.put("email", getString(R.string.razorpay_Email_id))
            prefill.put("contact", getString(R.string.razorpay_Contact_number))

//            val retryObj =  JSONObject();
//            retryObj.put("enabled", true);
//            retryObj.put("max_count", 4);
//            options.put("retry", retryObj);

            options.put("prefill", prefill)

            checkout.open(this, options)
        } catch (e: Exception) {
            Utils.showToast(this@CheckoutActivity, "Something went wrong")
            e.printStackTrace()
        }


    }

    override fun onPaymentSuccess(p0: String?) {
        Utils.showToast(this@CheckoutActivity, "Payment successful")
        uploadData()
    }

    private fun uploadData() {
        Utils.showDialog(this, "Placing your orders...")
        val id = intent.getStringArrayListExtra("productIds")
        for (currentId in id!!) {
            fetchData(currentId)
        }
    }

    private fun fetchData(productId: String?) {

        val dao = AppDatabase.getInstance(this).productDao()

        Firebase.firestore.collection("products")
            .document(productId!!).get()
            .addOnSuccessListener {

                lifecycleScope.launch(Dispatchers.IO) {
                    dao.deleteProduct(ProductModel(productId, "", "", ""))
                }


                saveData(it.getString("productName"), it.getString("productSp"), productId)

            }.addOnFailureListener {
                Utils.hideDialog()
                Utils.showToast(this@CheckoutActivity, "Something went wrong")
            }

    }

    private fun saveData(name: String?, price: String?, productId: String) {


        val preferences = this.getSharedPreferences("user", MODE_PRIVATE) //
        val data = hashMapOf<String, Any>()
        data["name"] = name!!
        data["price"] = price!!
        data["productId"] = productId!!
        data["userId"] = preferences.getString("number", "")!!
        data["status"] = "Ordered"

        val firestore = Firebase.firestore.collection("allOrders")
        val key = firestore.document().id
        data["orderId"] = key

        firestore.document(key).set(data).addOnSuccessListener {
            Utils.hideDialog()
            Utils.showToast(this@CheckoutActivity, "Ordered placed")
            //goto mainActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()



        }
            .addOnFailureListener {
                Utils.hideDialog()
                Utils.showToast(this@CheckoutActivity, "Something went wrong")
            }

    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Utils.showToast(this@CheckoutActivity, "Payment error")
    }



    //set status bar color of activity
    private fun setStatusBarColor() {
        window?.apply {
            val statusBarColors = ContextCompat.getColor(this@CheckoutActivity, R.color.yellow)
            statusBarColor = statusBarColors
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }


}














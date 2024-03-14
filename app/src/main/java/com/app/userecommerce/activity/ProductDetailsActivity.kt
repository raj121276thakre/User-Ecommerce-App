package com.app.userecommerce.activity

import android.content.Intent
import android.text.Html
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.app.userecommerce.MainActivity
import com.app.userecommerce.R
import com.app.userecommerce.Utils
import com.app.userecommerce.databinding.ActivityProductDetailsBinding
import com.app.userecommerce.roomdb.AppDatabase
import com.app.userecommerce.roomdb.ProductDao
import com.app.userecommerce.roomdb.ProductModel
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.models.SlideModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setStatusBarColor() // set status bar color

        getProductDetails(intent.getStringExtra("id"))

    }

    private fun getProductDetails(proId: String?) {

        Firebase.firestore.collection("products").document(proId!!)
            .get().addOnSuccessListener {

                val list = it.get("productImages") as ArrayList<String>
                val name =  it.getString("productName")
                val productSp =  "₹" + it.getString("productSp")
                val prodDescription = it.getString("productDescription")

                val productQuantity = it.getString("productQuantity")
                val productDiscount = it.getString("productDiscount")
                val productMrp = it.getString("productMrp")

                // Format the product MRP with a strikethrough using HTML
                val formattedProductMrp = Utils.applyStrikethrough("₹$productMrp")

                binding.tvProductName.text = name
                binding.tvProductSp.text = productSp
                binding.tvProductDescription.text = prodDescription

                binding.tvQuantity.text = productQuantity
                binding.tvProductDiscount.text = productDiscount
                binding.tvProductMrp.text = formattedProductMrp


                val slideList = ArrayList<SlideModel>()
                for (data in list) {
                    slideList.add(
                        SlideModel(
                            data,
                            ScaleTypes.CENTER_INSIDE  // image scale type add here
                        )
                    )
                }

                cartAction(proId, name , productSp  , it.getString("productCoverImg"))

                binding.imageSlider.setImageList(slideList)

            }.addOnFailureListener {
                Utils.showToast(this, "Something went wrong")
            }

    }


    private fun cartAction(prodId: String, name: String?, productSp: String?, coverImg: String?) {
        val productDao = AppDatabase.getInstance(this).productDao()

        if (productDao.isExit(prodId) != null) {
            binding.tvBtnAddToCart.text = "Go to cart"

        } else {
            binding.tvBtnAddToCart.text = "Add to cart"

        }

        binding.tvBtnAddToCart.setOnClickListener {
            if (productDao.isExit(prodId) != null) {
                openCart()

            } else {
                addToCart(productDao, prodId, name, productSp, coverImg)

            }
        }


    }

    private fun addToCart(
        productDao: ProductDao,
        prodId: String,
        name: String?,
        productSp: String?,
        coverImg: String?
    ) {

        val data = ProductModel(prodId, name, coverImg,productSp)
        lifecycleScope.launch (Dispatchers.IO){
            productDao.insertProduct(data)
            binding.tvBtnAddToCart.text = "Go to cart"

        }
    }

    private fun openCart() {
        val preference = this.getSharedPreferences("info", MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isCart", true)
        editor.apply()

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()

    }

//set status bar color of activity
    private fun setStatusBarColor() {
        window?.apply {
            val statusBarColors = ContextCompat.getColor(this@ProductDetailsActivity, R.color.yellow)
            statusBarColor = statusBarColors
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
        }
    }



}




















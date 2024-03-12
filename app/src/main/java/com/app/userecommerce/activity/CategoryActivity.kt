package com.app.userecommerce.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.app.userecommerce.R
import com.app.userecommerce.Utils
import com.app.userecommerce.adapter.CategoryProductAdapter
import com.app.userecommerce.model.AddProductModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class CategoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)


        getCategoryProducts(intent.getStringExtra("cat"))
    }

    private fun getCategoryProducts(category: String?) {
        val list = ArrayList<AddProductModel>()
        Firebase.firestore.collection("products").whereEqualTo("productCategory", category)
            .get().addOnSuccessListener {

                list.clear()
                for (doc in it.documents) {
                    val data = doc.toObject(AddProductModel::class.java)
                    list.add(data!!)
                }
                val recyclerView = findViewById<RecyclerView>(R.id.categoryProductRV)
                recyclerView.adapter = CategoryProductAdapter(this, list)

            }.addOnFailureListener {
                Utils.showToast(this, "Something went wrong")
            }
    }
}
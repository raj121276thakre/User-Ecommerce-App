package com.app.userecommerce.fragment

import android.content.Intent
import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.app.userecommerce.MainActivity
import com.app.userecommerce.R
import com.app.userecommerce.activity.AddressActivity
import com.app.userecommerce.activity.CategoryActivity
import com.app.userecommerce.adapter.CartAdapter
import com.app.userecommerce.databinding.FragmentCartBinding
import com.app.userecommerce.roomdb.AppDatabase
import com.app.userecommerce.roomdb.ProductModel

class CartFragment : Fragment() {

    private lateinit var binding: FragmentCartBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment
        val preference =
            requireContext().getSharedPreferences("info", AppCompatActivity.MODE_PRIVATE)
        val editor = preference.edit()
        editor.putBoolean("isCart", false)
        editor.apply()

        val dao = AppDatabase.getInstance(requireContext()).productDao()

        dao.getAllProducts().observe(requireActivity()) {
            binding.cartRecycler.adapter = CartAdapter(requireContext(), it)

            toatalCost(it)
        }



        return binding.root
    }

    private fun toatalCost(data: List<ProductModel>?) {
        var total = 0
        for (item in data!!) {
            // Remove currency symbol before parsing to integer
            val priceWithoutCurrency = item.productSp?.replace("₹", "")
            total += priceWithoutCurrency?.toIntOrNull() ?: 0
            //total += item.productSp!!.toInt()
        }

        binding.totalCartItemTV.text = "Total item in cart is ${data.size}"
        binding.totalCartCostTV.text = "Total cost : ₹${total}"

        binding.btnCheckOut.setOnClickListener {
            val intent = Intent(context, AddressActivity::class.java)
            intent.putExtra("totalCost",total)
            startActivity(intent)
        }


    }


}
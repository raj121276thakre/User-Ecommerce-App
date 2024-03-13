package com.app.userecommerce.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.app.userecommerce.R
import com.app.userecommerce.Utils
import com.app.userecommerce.adapter.AllOrderAdapter
import com.app.userecommerce.databinding.FragmentHomeBinding
import com.app.userecommerce.databinding.FragmentMoreBinding
import com.app.userecommerce.model.AllOrderModel
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore


class MoreFragment : Fragment() {

    private lateinit var binding: FragmentMoreBinding
    private lateinit var list: ArrayList<AllOrderModel>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMoreBinding.inflate(layoutInflater)
        // Inflate the layout for this fragment

        list= ArrayList()

        val  preferences = requireContext().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE) //

        Firebase.firestore.collection("allOrders").whereEqualTo("userId", preferences.getString("number","")!!)
            .get()
            .addOnSuccessListener {
                list.clear()

                for (doc in it){
                    val data = doc.toObject(AllOrderModel::class.java)
                    list.add(data)

                }
                binding.allordersRecycler.adapter = AllOrderAdapter(list,requireContext())
            }
            .addOnFailureListener {
                Utils.showToast(requireContext(), "Something went wrong")
            }


        return binding.root
    }


}
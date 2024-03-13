package com.app.userecommerce.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.userecommerce.databinding.AllOrderItemLayoutBinding
import com.app.userecommerce.model.AllOrderModel

import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import org.checkerframework.checker.index.qual.GTENegativeOne


class AllOrderAdapter(val list: ArrayList<AllOrderModel>, val context: Context):
    RecyclerView.Adapter<AllOrderAdapter.AllOrderViewHolder>(){

    inner class AllOrderViewHolder(val binding : AllOrderItemLayoutBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllOrderViewHolder {
        val binding = AllOrderItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return AllOrderViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: AllOrderViewHolder, position: Int) {
        holder.binding.productTitle.text = list[position].name
        holder.binding.productPrice.text = "₹" + list[position].price



        when( list[position].status){

            "Ordered" ->{

                holder.binding.productStatus.text = "Ordered"


            } //

            "Dispatched" ->{
                holder.binding.productStatus.text = "Dispatched"


            }//

            "Delivered" ->{
                holder.binding.productStatus.text = "Delivered"

            }//

            "Canceled" ->{
                holder.binding.productStatus.text = "Canceled"
            }//
        }

    }






}













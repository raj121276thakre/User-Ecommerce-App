package com.app.userecommerce.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.userecommerce.databinding.LayoutCartItemBinding
import com.app.userecommerce.databinding.LayoutProductItemBinding
import com.app.userecommerce.roomdb.ProductModel
import com.bumptech.glide.Glide


class CartAdapter(val context: Context, val list: List<ProductModel>) :
    RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    inner class CartViewHolder(val binding: LayoutCartItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = LayoutCartItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return CartViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {

        Glide.with(context).load(list[position].productImages).into(holder.binding.cartProductIV)

        holder.binding.cartProductName.text = list[position].productName
        holder.binding.cartProductSp.text =  list[position].productSp
    }


}










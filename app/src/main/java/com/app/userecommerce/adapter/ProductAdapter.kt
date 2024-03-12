package com.app.userecommerce.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.userecommerce.activity.ProductDetailsActivity
import com.app.userecommerce.databinding.LayoutProductItemBinding
import com.app.userecommerce.model.AddProductModel
import com.bumptech.glide.Glide

class ProductAdapter(var context: Context, val list: ArrayList<AddProductModel> ):
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>()  {

    inner class ProductViewHolder(val binding : LayoutProductItemBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = LayoutProductItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return ProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val data  = list[position]

        Glide.with(context).load(data.productCoverImg).into(holder.binding.productCoverImage)
        holder.binding.productName.text = data.productName
        holder.binding.productCategory.text = data.productCategory
        holder.binding.productMrp.text = "₹" + data.productMrp

        holder.binding.btnProductSp.text = "₹" + data.productSp



        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("id",list[position].productId)
            context.startActivity(intent)
        }


    }

}



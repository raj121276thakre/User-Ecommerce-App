package com.app.userecommerce.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.userecommerce.activity.CategoryActivity
import com.app.userecommerce.activity.ProductDetailsActivity
import com.app.userecommerce.databinding.ItemCategoryProductLayoutBinding
import com.app.userecommerce.databinding.LayoutProductItemBinding
import com.app.userecommerce.model.AddProductModel
import com.app.userecommerce.model.CategoryModel
import com.bumptech.glide.Glide


class CategoryProductAdapter(var context: Context, val list: ArrayList<AddProductModel> ):
    RecyclerView.Adapter<CategoryProductAdapter.CategoryProductViewHolder>()   {

    inner class CategoryProductViewHolder(val binding : ItemCategoryProductLayoutBinding)
        : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryProductViewHolder {
        val binding = ItemCategoryProductLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return CategoryProductViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CategoryProductViewHolder, position: Int) {
        val data  = list[position]

        Glide.with(context).load(data.productCoverImg).into(holder.binding.productCoverIV)
        holder.binding.productNametxt.text = data.productName
        holder.binding.productSptxt.text = "₹" + data.productSp

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("id",list[position].productId)
            context.startActivity(intent)
        }
    }
}




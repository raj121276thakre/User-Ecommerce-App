package com.app.userecommerce.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.app.userecommerce.R
import com.app.userecommerce.activity.CategoryActivity
import com.app.userecommerce.databinding.LayoutCategoryItemBinding
import com.app.userecommerce.model.CategoryModel
import com.bumptech.glide.Glide

class CategoryAdapter(var context: Context, val categoryList: ArrayList<CategoryModel>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(view: View) : ViewHolder(view) {
        val binding = LayoutCategoryItemBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            LayoutInflater.from(context).inflate(R.layout.layout_category_item, parent, false)
        )
    }


    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        holder.binding.catName.text = categoryList[position].cat
        Glide.with(context).load(categoryList[position].img).into(holder.binding.categoryImg)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, CategoryActivity::class.java)
            intent.putExtra("cat",categoryList[position].cat)
            context.startActivity(intent)
        }
    }


    override fun getItemCount(): Int {
        return categoryList.size
    }

}
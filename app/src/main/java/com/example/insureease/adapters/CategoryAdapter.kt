package com.example.insureease.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.insureease.R
import com.example.insureease.databinding.ItemCategoryBinding
import com.example.insureease.models.Category
import com.squareup.picasso.Picasso

class CategoryAdapter : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    inner class CategoryViewHolder(val itemCategoryBinding: ItemCategoryBinding) : RecyclerView.ViewHolder(itemCategoryBinding.root)

    private val categoryList = arrayListOf<Category>()

    lateinit var onClick : (String,String)->Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding = ItemCategoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CategoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categoryList[position]
        val binding = holder.itemCategoryBinding
        //Bind views
        with(binding){
            //Animation
            main.startAnimation(AnimationUtils.loadAnimation(main.context, R.anim.anim_category))
            textViewCategory.text = category.name
            Picasso.get().load(category.icon).into(imageViewCategory)
            if (holder.adapterPosition % 2 == 0){
                imageViewBg.setImageResource(R.drawable.bg_red)
            }else{
                imageViewBg.setImageResource(R.drawable.bg_blue)
            }

            main.setOnClickListener {
                onClick.invoke(category.name,category.description)
            }
        }
    }

    fun updateAdapter(dataSet : List<Category>){
        if (dataSet != categoryList){
            categoryList.clear()
            categoryList.addAll(dataSet)
            notifyDataSetChanged()
        }
    }

    fun isEmpty() : Boolean{
        Log.e("cowboyMf",categoryList.toString())
        return categoryList.isEmpty()
    }
}
package com.example.insureease.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.insureease.databinding.ItemCompanyBinding
import com.example.insureease.models.Category
import com.squareup.picasso.Picasso

class CompanyAdapter : RecyclerView.Adapter<CompanyAdapter.CompanyViewHolder>() {
    inner class CompanyViewHolder (val itemCompanyBinding: ItemCompanyBinding) : RecyclerView.ViewHolder(itemCompanyBinding.root)

    private val companyList = arrayListOf<Category>()
    lateinit var onClick : (Category)->Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompanyViewHolder {
        val binding = ItemCompanyBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CompanyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return companyList.size
    }

    override fun onBindViewHolder(holder: CompanyViewHolder, position: Int) {
        val company = companyList[position]
        val binding = holder.itemCompanyBinding

        //bind views
        with(binding){
            textViewC.text = company.name
            Picasso.get().load(company.icon).into(imageViewCompany)
            main.setOnClickListener {
                onClick.invoke(company)
            }
        }
    }
    fun updateAdapter (dataSet : ArrayList<Category>){
        if (dataSet != companyList){
            companyList.clear()
            companyList.addAll(dataSet)
            notifyDataSetChanged()
        }
    }

    fun isEmpty() : Boolean{
        return companyList.isEmpty()
    }
}
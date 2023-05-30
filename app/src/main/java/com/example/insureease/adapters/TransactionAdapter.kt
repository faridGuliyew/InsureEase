package com.example.insureease.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.insureease.databinding.ItemTransactionBinding
import com.example.insureease.models.Transaction
import com.squareup.picasso.Picasso

class TransactionAdapter : RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder>() {
    inner class TransactionViewHolder(val itemTransactionBinding: ItemTransactionBinding) : RecyclerView.ViewHolder(itemTransactionBinding.root)

    private val transactionList = arrayListOf<Transaction>()
    lateinit var onClick : (Transaction)->Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = ItemTransactionBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return TransactionViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return transactionList.size
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val transaction = transactionList[position]
        val binding = holder.itemTransactionBinding
        //bind views
        with(binding){
            main.setOnClickListener {
                onClick.invoke(transaction)
            }
            textViewCategoryName.text = transaction.name
            textViewCompany.text = transaction.company
            textViewPrice.text = "-${transaction.price} AZN"
            textViewDate.text = transaction.date
            Picasso.get().load(transaction.icon).into(imageViewCategoryIcon)
        }
    }
    fun updateAdapter(dataSet : List<Transaction>){
        transactionList.clear()
        transactionList.addAll(dataSet)
        notifyDataSetChanged()
    }
}
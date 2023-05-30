package com.example.insureease.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.insureease.R
import com.example.insureease.databinding.ItemActiveBinding
import com.example.insureease.models.Active
import com.squareup.picasso.Picasso

class ActiveAdapter : RecyclerView.Adapter<ActiveAdapter.ActiveViewHolder>() {
    inner class ActiveViewHolder (val itemActiveBinding: ItemActiveBinding) : RecyclerView.ViewHolder(itemActiveBinding.root)

    private val activeList = arrayListOf<Active>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActiveViewHolder {
        val binding = ItemActiveBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ActiveViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return activeList.size
    }

    override fun onBindViewHolder(holder: ActiveViewHolder, position: Int) {
        val active = activeList[position]
        val binding = holder.itemActiveBinding

        //Bind views
        with(binding){
            textViewName.text = active.name
            textViewComp.text = active.company
            textViewDuration.text = active.duration
            if (holder.adapterPosition % 2 == 0){
                imageViewBack.setImageResource(R.drawable.bg_blue)
            }else{
                imageViewBack.setImageResource(R.drawable.bg_green)
            }
            Picasso.get().load(active.icon).into(imageViewIco)
        }
    }

    fun updateAdapter (dataSet : ArrayList<Active>){
        activeList.clear()
        activeList.addAll(dataSet)
        notifyDataSetChanged()
    }
}
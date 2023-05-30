package com.example.insureease.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.insureease.databinding.ItemOfferBinding
import com.example.insureease.models.Offer

class OfferAdapter : RecyclerView.Adapter<OfferAdapter.OfferViewHolder>() {
    inner class OfferViewHolder (val itemOfferBinding: ItemOfferBinding) : RecyclerView.ViewHolder(itemOfferBinding.root)

    private val offerList = arrayListOf<Offer>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OfferViewHolder {
        val binding = ItemOfferBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OfferViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return offerList.size
    }

    override fun onBindViewHolder(holder: OfferViewHolder, position: Int) {
        val offer = offerList[position]
        val binding = holder.itemOfferBinding

        //bind views
        with(binding){
            imageViewIcon.setImageResource(offer.companyIcon)
            imageViewO1.setImageResource(offer.offer1)
            imageViewO2.setImageResource(offer.offer2)
            textViewNameO.text = offer.companyName
            textViewO1.text = offer.offer1Name
            textViewO2.text = offer.offer2Name
            textViewOld.text = "${offer.oldPrice} AZN"
            textViewOld.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            textViewNew.text = "${offer.newPrice} AZN"
        }
    }
    fun updateAdapter(dataSet : ArrayList<Offer>){
        offerList.clear()
        offerList.addAll(dataSet)
        notifyDataSetChanged()
    }
}
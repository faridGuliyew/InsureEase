package com.example.insureease.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.insureease.R
import com.example.insureease.models.Active
import com.example.insureease.models.Category
import com.example.insureease.models.Offer
import com.example.insureease.models.Transaction
import com.example.insureease.room.TransactionDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DatabaseRepository {

    companion object{

        private var activeData = ArrayList<Active>()
        private var offerData = ArrayList<Offer>()
/*
        suspend fun getActiveData() : ArrayList<Active>{
            return withContext(Dispatchers.IO){
                setActiveData()
                activeData
            }
        }*/

        suspend fun getOfferData() : ArrayList<Offer>{
            return withContext(Dispatchers.IO){
                setOfferData()
                offerData
            }
        }
/*
        private suspend fun setActiveData() {
            val dataSet = ArrayList<Active>()
            withContext(Dispatchers.IO){
                dataSet.add(Active("İcbari Avto", "Pasha Sığorta","2 ay",R.drawable.ic_car,R.drawable.bg_blue))
                dataSet.add(Active("Kasko", "Ata Sığorta","1 həftə",R.drawable.ic_kasko,R.drawable.bg_green))
                dataSet.add(Active("Tibbi", "Ana Sığorta","1 il", R.drawable.ic_medical,R.drawable.bg_green))
                activeData = dataSet
            }
        }*/

        private suspend fun setOfferData(){
            val dataSet = ArrayList<Offer>()
            withContext(Dispatchers.IO){
                dataSet.add(Offer( R.drawable.logo_pasha,"Pasha Sığorta",R.drawable.ic_car,"Icbari Avto",R.drawable.ic_residence,"Icbari Mülk",195.7,180.0,R.drawable.bg_green))
                dataSet.add(Offer( R.drawable.logo_ata,"Ata Sığorta",
                    R.drawable.ic_travel,"Səyahət",R.drawable.ic_residence,"Icbari Mülk",225.7,200.7,
                    R.drawable.bg_blue
                ))
                offerData = dataSet
            }
        }
    }
}
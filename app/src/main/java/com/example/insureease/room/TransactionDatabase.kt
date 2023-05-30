package com.example.insureease.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.insureease.room.dao.TransactionDao
import com.example.insureease.room.entity.Transaction


@Database([Transaction::class],[],1,false)
abstract class TransactionDatabase : RoomDatabase(){

    abstract fun getTransactionDao() : TransactionDao

    companion object{

        @Volatile
        private var INSTANCE : TransactionDatabase? = null

        fun getTransactionDatabase(context : Context) : TransactionDatabase {
            val tempInstance = INSTANCE

            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(context,TransactionDatabase::class.java
                ,"TransactionDatabase").build()
                INSTANCE = instance
                return instance
            }
        }

    }
}
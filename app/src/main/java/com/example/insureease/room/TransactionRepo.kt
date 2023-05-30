package com.example.insureease.room

import android.util.Log
import com.example.insureease.models.Transaction
import com.example.insureease.room.dao.TransactionDao

class TransactionRepo (private val transactionDao : TransactionDao) {

    val allTransactions = transactionDao.getAllTransactions()

    suspend fun addTransaction(transaction : Transaction){
        transactionDao.addTransaction(transaction)
    }

    suspend fun deleteAllTransactions(){
        transactionDao.deleteAllTransactions()
    }
}
package com.example.insureease.room.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.DeleteTable
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.insureease.models.Transaction


@Dao
interface TransactionDao {

    @Insert(com.example.insureease.room.entity.Transaction::class,onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTransaction(transaction: Transaction)

    @Query("DELETE FROM `transaction`")
    suspend fun deleteAllTransactions()

    @Query("SELECT * FROM `transaction` ORDER BY id ASC")
    fun getAllTransactions() : LiveData<List<Transaction>>
}
package com.example.insureease.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity("transaction")
data class Transaction (
    @PrimaryKey(true)
    val id : Int,
    val company : String,
    val icon : Int,
    val name : String,
    val price : Double,
    val date : String
)
package com.example.insureease.models

data class Offer (val companyIcon : Int,
                  val companyName : String,
                  val offer1 : Int,
                  val offer1Name : String,
                  val offer2 : Int,
                  val offer2Name : String,
                  val oldPrice : Double,
                  val newPrice : Double,
                  val background : Int)
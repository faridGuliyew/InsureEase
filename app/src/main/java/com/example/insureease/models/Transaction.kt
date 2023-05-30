package com.example.insureease.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Transaction(val name : String = "", val company : String = "", val price : Double = 0.0, val date : String = "", val icon : String = "", val id : Int = 0):Parcelable

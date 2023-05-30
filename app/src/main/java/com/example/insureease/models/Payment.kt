package com.example.insureease.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


@Parcelize
data class Payment(val companyName : String, val companyIcon : Uri, val categoryName : String, val code : String):Parcelable

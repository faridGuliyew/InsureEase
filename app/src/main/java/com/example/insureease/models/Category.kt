package com.example.insureease.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Category(val name : String, val icon : Uri, val bg : Int? = null, val description : String = ""):Parcelable

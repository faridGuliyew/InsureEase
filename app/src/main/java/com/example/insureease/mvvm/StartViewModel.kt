package com.example.insureease.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class StartViewModel : ViewModel() {

    private val _loading = MutableLiveData<Boolean>()
    val loading : LiveData<Boolean>
        get() = _loading
    val status = NetworkRepository.status
    val error = NetworkRepository.error
    val uid = MutableLiveData<String>()

    fun firebaseRegister (email : String, password : String){
        viewModelScope.launch {
            _loading.value = true
            uid.value = NetworkRepository.firebaseRegister(email, password)
            _loading.value = false
        }
    }

    fun firebaseLogin (email : String, password : String){
        viewModelScope.launch {
            _loading.value = true
            uid.value = NetworkRepository.firebaseLogin(email, password)
            _loading.value = false
        }
    }
}
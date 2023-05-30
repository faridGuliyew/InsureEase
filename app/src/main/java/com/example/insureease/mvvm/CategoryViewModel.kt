package com.example.insureease.mvvm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.insureease.models.Active
import com.example.insureease.models.Category
import com.example.insureease.models.Offer
import com.example.insureease.models.Transaction
import com.example.insureease.room.TransactionDatabase
import com.example.insureease.room.TransactionRepo
import kotlinx.coroutines.launch

class CategoryViewModel (application: Application) : AndroidViewModel(application) {

    lateinit var transactionData : LiveData<List<Transaction>>

    private var _activeData = MutableLiveData<ArrayList<Active>>()
    val activeData : LiveData<ArrayList<Active>>
        get() = _activeData
    private var _offerData = MutableLiveData<ArrayList<Offer>>()
    val offerData : LiveData<ArrayList<Offer>>
        get() = _offerData

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean>
        get() = _isLoading

    //ROOM
    private val transactionRepo : TransactionRepo
    val transactionDataRoom :  LiveData<List<Transaction>>

    //Firebase
    private var _categoryResponseFirebase =  MutableLiveData<ArrayList<Category>>()
    val categoryResponseFirebase : LiveData<ArrayList<Category>>
        get() = _categoryResponseFirebase

    private var _companyResponseFirebase =  MutableLiveData<ArrayList<Category>>()
    val companyResponseFirebase : LiveData<ArrayList<Category>>
        get() = _companyResponseFirebase

    //User data
    private var _balance = MutableLiveData<Double>()
    val balance : LiveData<Double>
        get() = _balance

    private var _transactions = MutableLiveData<List<Transaction>>()
    val transactions : LiveData<List<Transaction>>
        get() = _transactions

    //Status
    private var _status = MutableLiveData<Boolean>()
    val status : LiveData<Boolean>
        get() = _status

    val username = NetworkRepository.username

    init {
        val transactionDao = TransactionDatabase.getTransactionDatabase(application.applicationContext).getTransactionDao()
        transactionRepo = TransactionRepo(transactionDao)
        transactionDataRoom = transactionRepo.allTransactions
    }

    fun addTransaction (transaction: Transaction){
        viewModelScope.launch {
            transactionRepo.addTransaction(transaction)
        }
    }
    //End of the ROOM


    fun getTransactionData(){
        viewModelScope.launch {
            transactionData = transactionRepo.allTransactions
        }
    }
/*
    fun getActiveData(){
        viewModelScope.launch {
            _activeData.value = DatabaseRepository.getActiveData()
        }
    }*/
    fun getOfferData(){
        viewModelScope.launch {
            _offerData.value = DatabaseRepository.getOfferData()
        }
    }
    fun getCategoryResponseFirebase(){
        viewModelScope.launch {
            _isLoading.value = true
            _categoryResponseFirebase.value = NetworkRepository.getCategories()
            _isLoading.value = false
        }
    }

    fun getCompanyResponseFirebase(category : String){
        viewModelScope.launch {
            _isLoading.value = true
            _companyResponseFirebase.value = NetworkRepository.getCompanies(category)
            _isLoading.value = false
        }
    }

    fun setUserData(uid:String){
        viewModelScope.launch {
            _balance.value = NetworkRepository.getUserBalance(uid)
        }
    }

    fun setTransactionDataFromFirestore(uid:String){
        viewModelScope.launch {
            _isLoading.value = true
            _transactions.value = NetworkRepository.getTransactionData(uid)
            _isLoading.value = false
        }
    }

    fun addTransactionToFirestore(transaction: Transaction,uid:String){
        viewModelScope.launch {
            _status.value = NetworkRepository.addTransactionToFirestore(transaction,uid)
        }
    }

    fun deleteAllTransactions(){
        viewModelScope.launch {
            transactionRepo.deleteAllTransactions()
        }
    }
    fun isRegistered(code:String){
        viewModelScope.launch {
            _isLoading.value = true
            _status.value = NetworkRepository.isRegistered(code)
            _isLoading.value = false
        }
    }
}
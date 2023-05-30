package com.example.insureease.mvvm

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.insureease.models.Category
import com.example.insureease.models.Transaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class NetworkRepository {

    companion object{
        private val auth : FirebaseAuth = FirebaseAuth.getInstance()
        //private val uid = auth.currentUser?.uid
        private val _status = MutableLiveData<Boolean>()
        val status : LiveData<Boolean>
            get() = _status
        private val _error = MutableLiveData<String>()
        val error : LiveData<String>
            get() = _error

        init {
            _status.value = false
        }

        private val fireStore = Firebase.firestore
        private val fireStorageRef = FirebaseStorage.getInstance().reference

        //Just to get username based on registration code
        private var _username = MutableLiveData<String>()
        val username : LiveData<String>
            get() = _username


        suspend fun getCategories() : ArrayList<Category>{
            return try {
                val querySnapshot = withContext(Dispatchers.IO) {
                    fireStore.collection("categories").get().await()
                }

                val categories = querySnapshot.documents.map { snapshot ->
                    val categoryQuery = snapshot.id
                    val uri = fireStorageRef.child("categories/$categoryQuery.png").downloadUrl.await()
                    Category(snapshot["name"].toString(), uri,0,categoryQuery)
                }
                Log.e("feridRepo", categories.toString())
                categories as ArrayList<Category>
            } catch (exception: Exception) {
                // Handle the exception
                ArrayList()
            }
        }

        suspend fun getCompanies(category : String) : ArrayList<Category>{
            return try {
                val querySnapshot = withContext(Dispatchers.IO) {
                    fireStore.collection("categories").document(category).collection("companies").get().await()
                }

                val companies = querySnapshot.documents.map {snapshot->
                    val companyQuery = snapshot.id
                    val uri = fireStorageRef.child("companies/$companyQuery.png").downloadUrl.await()
                    Category(snapshot["name"].toString(), uri, 0, snapshot["price"].toString())
                }

                Log.e("feridRepo2", companies.toString())
                companies as ArrayList<Category>
            } catch (exception: Exception) {
                // Handle the exception
                ArrayList()
            }
        }

        suspend fun firebaseRegister(email : String,password : String) : String{
            var uid : String
            return withContext(Dispatchers.IO){
                try {
                    val request = auth.createUserWithEmailAndPassword(email,password).await()
                    uid = request.user!!.uid
                    Log.e("kurtulusFromShit",uid)
                    fireStore.collection("users").document(uid).set(hashMapOf("balance" to 500.0)).await()
                    withContext(Dispatchers.Main){
                        _status.value = true
                    }
                }catch (e : Exception){
                    uid = ""
                    withContext(Dispatchers.Main){
                        _error.value = e.localizedMessage?:"Something went wrong"
                        _status.value = false
                    }
                }
                uid
            }
        }

        suspend fun firebaseLogin (email : String,password : String) : String{
            var uid : String
            return withContext(Dispatchers.IO){
                try {
                    val request = auth.signInWithEmailAndPassword(email,password).await()
                    withContext(Dispatchers.Main){
                        _status.value = true
                    }
                    uid = request.user!!.uid
                    Log.e("kurtulusFromShit",uid)
                }catch (e : Exception){
                    uid = ""
                    withContext(Dispatchers.Main){
                        _error.value = e.localizedMessage ?: "Something went wrong"
                        _status.value = false
                    }
                }
                uid
            }
        }

        suspend fun getUserBalance(uid:String) : Double{
            //val uid = auth.currentUser!!.uid
            var balance: Double
            return withContext(Dispatchers.IO){
                try {
                    //Get balance
                    val userQuery = fireStore.collection("users").document(uid).get().await()
                    balance = userQuery["balance"] as Double
                    return@withContext balance
                }catch(e : java.lang.Exception){
                    Log.e("cowboyShit",e.localizedMessage?:"wtf")
                    return@withContext 0.0
                }
            }
        }
        suspend fun getTransactionData(uid: String) : ArrayList<Transaction>{
            //val uid = auth.currentUser!!.uid
            var transactions = ArrayList<Transaction>()
            withContext(Dispatchers.IO){
                try{
                    val transactionResponse = fireStore.collection("users").document(uid)
                        .collection("transactions").get().await()
                    transactions = transactionResponse.documents.map {
                        it.let {
                            Log.e("Niye de ala?",it.data.toString())
                            it.toObject(Transaction::class.java)!!
                        }
                    } as ArrayList<Transaction>
                }catch (e : Exception){
                    Log.e("bambuk",e.localizedMessage!!)
                }
            }
            return transactions
        }

        suspend fun addTransactionToFirestore(transaction: Transaction,uid:String) : Boolean{
            var status: Boolean
            return withContext(Dispatchers.IO){
                //val uid = auth.currentUser!!.uid
                status = try{
                   fireStore.collection("users").document(uid)
                        .collection("transactions")
                        .document().set(transaction)
                        .await()
                    true
                }catch (e : java.lang.Exception){
                    false
                }
                status
            }
        }

        suspend fun isRegistered(code : String) : Boolean{
            var isValid = false
            return withContext(Dispatchers.IO){
                try {
                    val request = fireStore.collection("registered").get().await()
                    for(document in request.documents) {
                        if (code == document.id){
                            isValid = true
                            withContext(Dispatchers.Main){
                                _username.value = document["name"].toString()
                            }
                            break
                        }
                    }
                }catch (e : Exception){
                    isValid = false
                }
                isValid
            }
        }
    }
}
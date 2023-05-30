package com.example.insureease.mainFragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.insureease.R
import com.example.insureease.databinding.FragmentResultBinding
import com.example.insureease.models.Transaction
import com.example.insureease.mvvm.CategoryViewModel
import com.example.insureease.room.TransactionRepo
import com.google.firebase.auth.FirebaseAuth
import com.squareup.picasso.Picasso
import java.util.Date
import java.util.Locale
import kotlin.math.ceil
import kotlin.math.round

class ResultFragment : Fragment() {
    private var _binding : FragmentResultBinding? = null
    private val binding
        get() = _binding!!
    private lateinit var categoryViewModel: CategoryViewModel
    private var uid = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentResultBinding.inflate(inflater,container,false)
        uid = getUidSp()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoryViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
        observeViewModel()
        bindViews()
        shareButton(binding.buttonShare)
        backButton(binding.buttonBack)
    }

    private fun shareButton(button: Button){
        button.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT,"This is a mock data, normally your file will appear here!")
            intent.type = "text/plain"
            if (intent.resolveActivity(requireActivity().packageManager) != null){
                startActivity(intent)
            }
        }
    }

    private fun observeViewModel(){
        with(categoryViewModel){
            addTransactionToFirestore(getSp(), uid)
            status.observe(viewLifecycleOwner){
                if (!it){
                    Toast.makeText(context,"Could not save to the database",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun currentDate() : String{
        return SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
    }

    private fun getSp() : Transaction{
        val sp = requireActivity().getSharedPreferences("transaction", Context.MODE_PRIVATE)
        val category = sp.getString("category","")!!
        val company = sp.getString("company","")!!
        val logo  = sp.getString("logo","")
        val price =getPriceSp()
        return Transaction(category,company, round(price),currentDate(),logo!!)
    }

    private fun getPriceSp() : Double{
        val sp = requireActivity().getSharedPreferences("query", Context.MODE_PRIVATE)
        return sp.getFloat("price",0f).toDouble()
    }

    private fun bindViews(){
        with(binding){
            val transaction = getSp()
            Picasso.get().load(transaction.icon).into(imageView50)
            textView50.text = transaction.company
            textViewType.text = transaction.name
            textViewTime.text = currentDate()
            textViewCost.text = "${round(transaction.price!!)} AZN"
        }
    }

    private fun backButton(button: Button){
        button.setOnClickListener {
            findNavController().navigate(ResultFragmentDirections.actionResultFragmentToHomeFragment())
        }
    }

    private fun getUidSp() : String{
        val sp = requireActivity().getSharedPreferences("uid",Context.MODE_PRIVATE)
        return sp.getString("uid","")!!
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.insureease.mainFragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.insureease.R
import com.example.insureease.databinding.FragmentIncreaseBinding
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.round

class IncreaseFragment : Fragment() {
    private var _binding : FragmentIncreaseBinding? = null
    private val binding
        get() = _binding!!
    //Normally should use MVVM, but tired af
    private val firestore = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIncreaseBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setCard(binding.creditCard)
        increaseBalance(binding.buttonIncrease)
        addCard(binding.imageViewAdd)
    }

    private fun setCard(cardView : MaterialCardView){
        cardView.setOnClickListener {
            cardView.isChecked = !cardView.isChecked
            if (cardView.isChecked){
                binding.textViewBalanceI.text = getBalance() + " AZN"
            }else{
                binding.textViewBalanceI.text = "Balansını görmək istədiyiniz kartı seçin"
            }
        }
    }

    private fun increaseBalance(button: Button){
        button.setOnClickListener {
            if (binding.creditCard.isChecked){
                //Increase balance here (MVVM)
                val amount = binding.editTextNumber.text.toString().toFloat()
                binding.editTextNumber.text.clear()
                firestore.collection("users").document(getUid()).set(hashMapOf("balance" to getBalance().toDouble() + amount))
                    .addOnSuccessListener {
                        Toast.makeText(context,"Uğurlu əməliyyat",Toast.LENGTH_SHORT).show()
                        setBalance(getBalance().toFloat() + amount)
                        findNavController().popBackStack()
                    }.addOnFailureListener {
                        Toast.makeText(context,"Uğursuz əməliyyat",Toast.LENGTH_SHORT).show()
                    }
            }else{
                Snackbar.make(it,"İlk növbədə kart seçin!",Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun addCard(addIcon : ImageView){
        addIcon.setOnClickListener {
            Toast.makeText(context,"Not yet implemented",Toast.LENGTH_SHORT).show()
        }
    }

    private fun getBalance() : String{
        val sp = requireActivity().getSharedPreferences("Balance", Context.MODE_PRIVATE)
        return round(sp.getFloat("balance",0f)).toString()
    }

    private fun setBalance(amount : Float) {
        val sp = requireActivity().getSharedPreferences("Balance", Context.MODE_PRIVATE)
        sp.edit().putFloat("balance",amount).apply()
    }

    private fun getUid() : String{
        val sp = requireActivity().getSharedPreferences("uid", Context.MODE_PRIVATE)
        return sp.getString("uid","")!!
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
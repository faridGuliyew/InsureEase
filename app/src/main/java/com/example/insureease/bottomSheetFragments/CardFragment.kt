package com.example.insureease.bottomSheetFragments

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.insureease.R
import com.example.insureease.databinding.FragmentCardBinding
import com.example.insureease.mainFragments.PaymentFragment
import com.example.insureease.mainFragments.PaymentFragmentDirections
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.card.MaterialCardView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.math.round

class CardFragment : BottomSheetDialogFragment() {
    private var _binding : FragmentCardBinding? = null
    private val binding
        get() = _binding!!
    private var balance  = 0.0
    private var price  = 0.0

    private val firestore = Firebase.firestore
    private var uid = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCardBinding.inflate(inflater,container,false)
        uid = getUidSp()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initVariables()
        setButtonAndCard(binding.buttonPayment,binding.card)
        bindViews()
    }

    private fun bindViews(){
        with(binding){
            textViewBalance.text = round(balance).toString()
        }
    }

    private fun initVariables(){
        balance = getBalanceSp()
        price = getPriceSp()
        Log.e("babus",price.toString())
    }

    private fun setButtonAndCard(button: Button, cardView : MaterialCardView){
        cardView.setOnClickListener {
            cardView.isChecked = !cardView.isChecked
        }
        cardView.setOnCheckedChangeListener { _, isChecked ->
            button.isEnabled = isChecked
        }

        button.setOnClickListener {
            if (balance >= price){
                val dialog = Dialog(requireContext())
                val window = dialog.window!!
                dialog.setContentView(R.layout.dialog_confirm)
                window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                window.setWindowAnimations(R.style.MyDialogAnimation)
                val confirmButton = dialog.findViewById<Button>(R.id.buttonPos)
                val negateButton = dialog.findViewById<Button>(R.id.buttonNeg)
                confirmButton.setOnClickListener {
                    dialog.dismiss()
                    //Show a progress bar!
                    //Decrease the balance
                    firestore.collection("users").document(uid).set(hashMapOf("balance" to balance-price))
                        .addOnSuccessListener {
                            findNavController().navigate(PaymentFragmentDirections.actionPaymentFragmentToResultFragment())
                            decreaseBalanceSp(price)
                            dismiss()
                        }
                }
                negateButton.setOnClickListener {
                    dialog.dismiss()
                }
                dialog.show()
            }else{
                Snackbar.make(binding.root,"Balansınızda kifayət qədər məbləğ yoxdur", Snackbar.LENGTH_SHORT)
                    .setAction("Artır"){
                        //GO TO THE INCREASE BALANCE FRAGMENT!
                        findNavController().navigate(PaymentFragmentDirections.actionPaymentFragmentToIncreaseFragment())
                        dismiss()
                    }
                    .show()
            }
        }
    }

    private fun getBalanceSp() : Double{
        val sp = requireActivity().getSharedPreferences("Balance", Context.MODE_PRIVATE)
        return sp.getFloat("balance",0f).toDouble()
    }

    private fun decreaseBalanceSp(byPrice : Double){
        val sp = requireActivity().getSharedPreferences("Balance", Context.MODE_PRIVATE)
        sp.edit().putFloat("balance", round(balance-byPrice).toFloat()).apply()
    }

    private fun getPriceSp() : Double{
        val sp = requireActivity().getSharedPreferences("query", Context.MODE_PRIVATE)
        return sp.getFloat("price",0f).toDouble()
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
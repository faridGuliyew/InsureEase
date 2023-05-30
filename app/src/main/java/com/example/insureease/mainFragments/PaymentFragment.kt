package com.example.insureease.mainFragments

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.fragment.navArgs
import com.example.insureease.R
import com.example.insureease.bottomSheetFragments.CardFragment
import com.example.insureease.databinding.FragmentPaymentBinding
import com.example.insureease.models.Payment
import com.squareup.picasso.Picasso
import java.util.Date
import java.util.Locale

class PaymentFragment : Fragment() {
    private var _binding : FragmentPaymentBinding? = null
    private val binding
        get() = _binding!!

    private val args : PaymentFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPaymentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setSp()
        animate()
        bindViews()
        goToCard(binding.buttonPay)
    }

    private fun animate(){
        binding.body.startAnimation(AnimationUtils.loadAnimation(context,R.anim.anim_payment))
    }

    private fun goToCard(button : Button){
        button.setOnClickListener {

            CardFragment().show(parentFragmentManager,null)
        }
    }

    private fun bindViews(){
        val payment = args.payment
        with(binding){
            textViewCode.text = payment.code
            textViewCompanyP.text = payment.companyName
            textViewDesc.text = payment.categoryName
            Picasso.get().load(payment.companyIcon).into(imageViewC)
            textViewPriceP.text = "${getPriceSp()} AZN"
            textViewUser.text = getUsernameSp()
        }
    }

    private fun getPriceSp() : Float{
        val sp = requireActivity().getSharedPreferences("query", Context.MODE_PRIVATE)
        return sp.getFloat("price",0f)
    }

    private fun setSp(){
        val sp = requireActivity().getSharedPreferences("transaction", Context.MODE_PRIVATE)
        val editor = sp.edit()
        val payment = args.payment
        editor.putString("company",payment.companyName)
            .putString("category",payment.categoryName)
            .putString("logo",payment.companyIcon.toString())
            .putFloat("price",95.3f).apply()
    }
    private fun getUsernameSp() : String {
        val sp = requireActivity().getSharedPreferences("uid", Context.MODE_PRIVATE)
        return sp.getString("user","Username could not be loaded")!!
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
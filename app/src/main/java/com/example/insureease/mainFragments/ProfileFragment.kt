package com.example.insureease.mainFragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.insureease.MainActivity
import com.example.insureease.R
import com.example.insureease.StartActivity
import com.example.insureease.databinding.FragmentProfileBinding
import com.example.insureease.mvvm.CategoryViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlin.math.log
import kotlin.math.round

class ProfileFragment : Fragment() {
    private var _binding : FragmentProfileBinding? = null
    private val binding
        get() = _binding!!
    private val auth = FirebaseAuth.getInstance()
    private lateinit var categoryViewModel: CategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews()
        initVars()
        controlBalance(binding.buttonControl)
        disableFab()
        logOut(binding.buttonLogout)
    }

    private fun initVars(){
        categoryViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
    }

    private fun bindViews(){
        binding.textViewB.text = getBalance() + " AZN"
    }

    private fun controlBalance(button : Button){
        button.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToIncreaseFragment())
        }
    }

    private fun disableFab(){
        val activity = requireActivity() as MainActivity
        activity.disableFab()
    }

    private fun logOut(button: Button){
        button.setOnClickListener {
            auth.signOut()
            //categoryViewModel.deleteAllTransactions()
            startActivity(Intent(requireContext(),StartActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun getBalance() : String{
        val sp = requireActivity().getSharedPreferences("Balance", Context.MODE_PRIVATE)
        return round(sp.getFloat("balance",0f)).toString()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
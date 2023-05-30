package com.example.insureease.startFragments

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.telephony.SmsManager
import android.text.InputFilter
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.insureease.MainActivity
import com.example.insureease.R
import com.example.insureease.databinding.FragmentRegisterBinding
import com.example.insureease.mvvm.StartViewModel
import com.google.android.material.snackbar.Snackbar


class RegisterFragment : Fragment() {
    private var _binding : FragmentRegisterBinding? = null
    private val binding
        get() = _binding!!
    private lateinit var startViewModel : StartViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViewModel()
        observeViewModel()
        finEditText()
        register(binding.buttonRegister)
        goToLogin(binding.textViewLogin)
    }

    private fun initViewModel(){
        startViewModel = ViewModelProvider(this)[StartViewModel::class.java]
    }

    private fun observeViewModel(){
        val progressBar = binding.progressBarReg
        with(startViewModel){
            loading.observe(viewLifecycleOwner){
                if (it){
                    progressBar.visibility = View.VISIBLE
                }else{
                    progressBar.visibility = View.GONE
                }
            }
            uid.observe(viewLifecycleOwner){
                if(it != ""){
                    setUidSp(it)
                    findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToPhoneFragment())
                }else{
                    //Snackbar.make(binding.root,"Something went wrong",Snackbar.LENGTH_SHORT).show()
                }
            }
            /*
            status.observe(viewLifecycleOwner){
                if (status.value == false){
                    Snackbar.make(binding.root,error.value?:"Something went wrong, reason : unknown",Snackbar.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }
                else{
                    findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToPhoneFragment())
                }
            }*/
        }
    }

    private fun finEditText(){
        binding.editTextFin.filters += InputFilter.AllCaps()
    }

    private fun register(button : Button){
        button.setOnClickListener {
            if (isValid() && permissionIsGiven()){
                //Send message!
                //sendOtp()
                //MVVM
                startViewModel.firebaseRegister("${binding.editTextFin.text}@insure.az","${binding.editTextPwd.text}")
            }
        }
    }

    private fun goToLogin(textView : TextView){
        textView.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }

    private fun isValid() : Boolean{

        if (binding.editTextFin.text.length != 7){
            binding.editTextFin.error = "Fin kod ölçüsü 7 simvol olmalıdır!"
            return false
        }

        else if (binding.editTextPwd.text.length < 8){
            binding.editTextPwd.error = "Şifrə uzunluğu minimum 8 simvol olmalıdır!"
            return false
        }
        else if (binding.editTextPwd.text.toString() != binding.editTextPwdAgain.text.toString()){
            Snackbar.make(binding.root,"Şifrələr fərqlidir!",Snackbar.LENGTH_SHORT).show()
            return false
        }
        else if (binding.editTextPhone.text.length != 9){
            binding.editTextPhone.error = "Telefon nömrəsi düzgün daxil edilməyib!"
            return false
        }
        else{
            return true
        }
    }

    private fun permissionIsGiven() : Boolean {
        return if ( ContextCompat.checkSelfPermission(requireContext(),android.Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED){
            true
        }else{
            //Ask for permission
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(android.Manifest.permission.SEND_SMS),100)
            false
        }
    }

    private fun sendOtp(){
        val otp = "369172"
        val phoneNumber = "0" + binding.editTextPhone.text.toString()
        val smsManager : SmsManager = SmsManager.getDefault()
        val parts = smsManager.divideMessage(otp + " " + "is your verification code")
        smsManager.sendMultipartTextMessage(phoneNumber,null,parts,null,null)
    }

    private fun setUidSp(uid : String){
        val sp = requireActivity().getSharedPreferences("uid", Context.MODE_PRIVATE)
        sp.edit().putString("uid",uid).apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
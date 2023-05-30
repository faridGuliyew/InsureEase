package com.example.insureease.startFragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.insureease.MainActivity
import com.example.insureease.R
import com.example.insureease.databinding.FragmentLoginBinding
import com.example.insureease.mvvm.StartViewModel
import com.google.android.material.snackbar.Snackbar


class LoginFragment : Fragment() {
    private var _binding : FragmentLoginBinding? = null
    private val binding
        get() = _binding!!
    private lateinit var startViewModel : StartViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        finEditText()
        initViewModel()
        observeViewModel()
        goToRegister(binding.textViewLogin)
        logIn(binding.buttonRegister)
    }

    private fun goToRegister(textView : TextView){
        textView.setOnClickListener {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
        }
    }

    private fun logIn(button: Button){
        button.setOnClickListener {
            if (isValid()){
                //MVVM
                startViewModel.firebaseLogin("${binding.editTextFin2.text}@insure.az",binding.editTextPwd.text.toString())
            }
        }
    }
    private fun initViewModel(){
        startViewModel = ViewModelProvider(this)[StartViewModel::class.java]
    }
    private fun observeViewModel(){
        val progressBar = binding.progressBar2
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
                    Log.e("kurtulus",it.toString())
                    val parent = requireActivity()
                    startActivity(Intent(parent, MainActivity::class.java))
                    parent.finish()
                }else{
                    //Snackbar.make(binding.root,"Something went wrong",Snackbar.LENGTH_SHORT).show()
                }
            }
            /*
            status.observe(viewLifecycleOwner){
                if (status.value == false){
                    //Snackbar.make(binding.root,error.value!!,Snackbar.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                }
                else{
                    val parent = requireActivity()
                    startActivity(Intent(parent, MainActivity::class.java))
                    parent.finish()
                }
            }*/
        }
    }

    private fun isValid() : Boolean{
        return if (binding.editTextFin2.text.length != 7){
            binding.editTextFin2.error = "Fin kod ölçüsü 7 simvol olmalıdır!"
            false
        } else if (binding.editTextPwd.text.length < 8) {
            binding.editTextPwd.error = "Şifrə uzunluğu minimum 8 simvol olmalıdır!"
            false
        }else{
            true
        }
    }

    private fun finEditText(){
        binding.editTextFin2.filters += InputFilter.AllCaps()
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
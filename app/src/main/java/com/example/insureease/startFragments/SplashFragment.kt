package com.example.insureease.startFragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.insureease.MainActivity
import com.example.insureease.databinding.FragmentSplashBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await

class SplashFragment : Fragment() {
    private var _binding : FragmentSplashBinding? = null
    private val binding
        get() = _binding!!
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        skip()
    }

    private fun skip(){
        Handler(Looper.myLooper()!!).postDelayed({
            if (auth.currentUser == null){
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToRegisterFragment())
            }else{
                val parent = requireActivity()
                startActivity(Intent(parent,MainActivity::class.java))
                parent.finish()
            }
        },1000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}
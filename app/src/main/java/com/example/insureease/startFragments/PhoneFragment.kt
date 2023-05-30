package com.example.insureease.startFragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import com.example.insureease.MainActivity
import com.example.insureease.R
import com.example.insureease.databinding.FragmentPhoneBinding

class PhoneFragment : Fragment() {
    private var _binding : FragmentPhoneBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhoneBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        phoneOtpMask()
    }

    private fun phoneOtpMask(){
        binding.editTextNumber1.requestFocus()
        binding.editTextNumber1.addTextChangedListener {
            binding.editTextNumber2.requestFocus()
        }
        binding.editTextNumber2.addTextChangedListener {
            binding.editTextNumber3.requestFocus()
        }
        binding.editTextNumber3.addTextChangedListener {
            binding.editTextNumber4.requestFocus()
        }
        binding.editTextNumber4.addTextChangedListener {
            binding.editTextNumber5.requestFocus()
        }
        binding.editTextNumber5.addTextChangedListener {
            binding.editTextNumber6.requestFocus()
            startActivity(Intent(requireContext(),MainActivity::class.java))
            requireActivity().finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}
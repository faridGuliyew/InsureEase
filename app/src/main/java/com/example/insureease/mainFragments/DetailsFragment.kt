package com.example.insureease.mainFragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.insureease.R
import com.example.insureease.databinding.FragmentDetailsBinding
import com.example.insureease.models.Payment
import com.example.insureease.mvvm.CategoryViewModel
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Picasso

class DetailsFragment : Fragment() {
    private var _binding : FragmentDetailsBinding? = null
    private val binding
        get() = _binding!!
    private val args : DetailsFragmentArgs by navArgs()
    private lateinit var categoryViewModel : CategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initVars()
        observeViewModel()
        bindViews()
        goToPayment(binding.buttonConfirm)
    }

    private fun initVars(){
        categoryViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
    }

    private fun observeViewModel(){
        with(categoryViewModel){
            isLoading.observe(viewLifecycleOwner){
                val progressBar = binding.progressBar3
                if(it){
                    progressBar.visibility = View.VISIBLE
                }else{
                    progressBar.visibility = View.GONE
                }
            }
            username.observe(viewLifecycleOwner){
                setUsernameSp(it)
                Log.e("cowboyShit",it.toString())
            }
            status.observe(viewLifecycleOwner){
                if (it){
                    val company = args.category
                    val payment = Payment(company.name,company.icon,company.description,binding.editTextCode.text.toString())
                    findNavController().navigate(DetailsFragmentDirections.actionDetailsFragmentToPaymentFragment(payment))
                }else{
                    Snackbar.make(binding.root,"No such user exists in the database!",Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun bindViews(){
        with(binding){
            Picasso.get().load(args.category.icon).into(binding.imageViewCategoryIconD)
            textViewCategoryTitle.text = args.category.name
        }
    }

    private fun goToPayment(button : Button){
        button.setOnClickListener {
            if (isValid()) {
                //MVVM
                categoryViewModel.isRegistered(binding.editTextCode.text.toString())
            }
        }
    }

    private fun isValid() : Boolean{
        val codeEditText = binding.editTextCode
        return if (codeEditText.text.length < 6){
            codeEditText.error = "Uzunluq ən azı 6 simvol olmalıdır!"
            false
        }else{
            true
        }
    }

    private fun setUsernameSp(user : String){
        val sp = requireActivity().getSharedPreferences("uid", Context.MODE_PRIVATE)
        sp.edit().putString("user",user).apply()
    }

    override fun onResume() {
        super.onResume()
        val categoryArray = resources.getStringArray(R.array.Categories)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.item_drop_down,categoryArray)
        binding.autoCompleteTextView2.setAdapter(arrayAdapter)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
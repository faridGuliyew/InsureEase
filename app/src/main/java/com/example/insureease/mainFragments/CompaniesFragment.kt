package com.example.insureease.mainFragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.insureease.R
import com.example.insureease.adapters.CompanyAdapter
import com.example.insureease.databinding.FragmentCompaniesBinding
import com.example.insureease.models.Category
import com.example.insureease.mvvm.CategoryViewModel

class CompaniesFragment : Fragment() {
    private var _binding : FragmentCompaniesBinding? = null
    private val binding
        get() = _binding!!
    private val companyAdapter = CompanyAdapter()
    private lateinit var categoryViewModel : CategoryViewModel

    private val args : CompaniesFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCompaniesBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViewModel()
        observeViewModel()
        setRv()
    }

    private fun setRv(){
        val rv = binding.rv
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = companyAdapter

        companyAdapter.onClick={
            val category = Category(it.name,it.icon,it.bg,args.description)
            setPriceSp(it.description.toDouble())
            findNavController().navigate(CompaniesFragmentDirections.actionCompaniesFragmentToDetailsFragment(category))
        }
    }

    private fun initViewModel(){
        categoryViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
    }
    private fun observeViewModel(){
        with(categoryViewModel){
            if (companyAdapter.isEmpty()){
                getCompanyResponseFirebase(getCategoryNameSp())
            }
            companyResponseFirebase.observe(viewLifecycleOwner){
                companyAdapter.updateAdapter(it)
            }
            isLoading.observe(viewLifecycleOwner){
                if(it){
                    binding.progressBarCompany.visibility = View.VISIBLE
                }else{
                    binding.progressBarCompany.visibility = View.GONE
                }
            }
        }
    }

    private fun getCategoryNameSp() : String{
        val sp = requireActivity().getSharedPreferences("query", Context.MODE_PRIVATE)
        return sp.getString("category","")!!
    }

    private fun setPriceSp(price : Double){
        val sp = requireActivity().getSharedPreferences("query", Context.MODE_PRIVATE)
        sp.edit().putFloat("price",price.toFloat()).apply()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.example.insureease.mainFragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentContainerView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.insureease.MainActivity
import com.example.insureease.R
import com.example.insureease.adapters.CategoryAdapter
import com.example.insureease.databinding.FragmentAddBinding
import com.example.insureease.mvvm.CategoryViewModel

class AddFragment : Fragment() {
   private var _binding : FragmentAddBinding? = null
    private val binding
        get() = _binding!!
    private val adapter = CategoryAdapter()
    private lateinit var categoryViewModel : CategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        disableFab()
        goBack()
        goToOffers(binding.buttonOffers)
        initViewModel()
        observeViewModel()
        setRv()
    }

    private fun initViewModel(){
        categoryViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
    }

    private fun observeViewModel(){
        with(categoryViewModel){
            if (adapter.isEmpty()){
                getCategoryResponseFirebase()
            }
            categoryResponseFirebase.observe(viewLifecycleOwner){
                adapter.updateAdapter(it)
            }
            isLoading.observe(viewLifecycleOwner){
                if (it){
                    binding.progressBarCategory.visibility = View.VISIBLE
                }
                else{
                    binding.progressBarCategory.visibility = View.GONE
                }
            }
        }
    }

    private fun setQuerySp(category : String){
        val sp = requireActivity().getSharedPreferences("query", Context.MODE_PRIVATE)
        sp.edit().putString("category",category).apply()
    }

    private fun disableFab(){
        val activity = requireActivity() as MainActivity
        activity.disableFab()
    }

    private fun goBack(){
        binding.imageViewMainMenu.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.textViewMainMenu.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun setRv(){
        val rv = binding.categoryRv
        rv.layoutManager = GridLayoutManager(context,2)
        rv.adapter = adapter
        adapter.onClick={it,category->
            setQuerySp(category)
            findNavController().navigate(AddFragmentDirections.actionAddFragmentToCompaniesFragment(it))
        }
    }

    private fun goToOffers(button: Button){
        button.setOnClickListener {
            findNavController().navigate(R.id.action_addFragment_to_offersFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
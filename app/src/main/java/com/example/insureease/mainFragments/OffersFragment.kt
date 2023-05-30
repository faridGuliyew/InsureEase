package com.example.insureease.mainFragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.insureease.R
import com.example.insureease.adapters.OfferAdapter
import com.example.insureease.databinding.FragmentOffersBinding
import com.example.insureease.mvvm.CategoryViewModel

class OffersFragment : Fragment() {
   private var _binding : FragmentOffersBinding? = null
    private val binding
        get() = _binding!!
    private val adapter = OfferAdapter()
    private lateinit var categoryViewModel : CategoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOffersBinding.inflate(inflater,container,false)
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
        rv.adapter = adapter
    }

    private fun initViewModel(){
        categoryViewModel= ViewModelProvider(this)[CategoryViewModel::class.java]
    }

    private fun observeViewModel(){
        with(categoryViewModel){
            getOfferData()
            offerData.observe(viewLifecycleOwner){
                adapter.updateAdapter(it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.insureease.MainActivity
import com.example.insureease.R
import com.example.insureease.adapters.ActiveAdapter
import com.example.insureease.adapters.TransactionAdapter
import com.example.insureease.databinding.FragmentHomeBinding
import com.example.insureease.models.Active
import com.example.insureease.models.Transaction
import com.example.insureease.mvvm.CategoryViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import java.lang.Math.abs

class HomeFragment : Fragment() {
    private var _binding : FragmentHomeBinding? = null
    private val binding
        get() = _binding!!
    private val transactionAdapter = TransactionAdapter()
    private val activeAdapter = ActiveAdapter()
    private lateinit var categoryViewModel: CategoryViewModel


    private var uid = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater,container,false)
        uid = getUidSp()
        Log.e("kurtulus",uid)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enableFab()
        initViewModel()
        observeViewModel()
        setTransactionRv()
        setViewPager()
        setPageTransformer(binding.viewPager2)
    }

    private fun enableFab(){
        val activity = requireActivity() as MainActivity
        activity.enableFab()
    }

    private fun setTransactionRv(){
        val rv = binding.transactionsRv
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = transactionAdapter
        transactionAdapter.onClick={
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToInspectFragment2(it))
        }
    }

    private fun setViewPager(){
        val viewPager = binding.viewPager2
        viewPager.adapter = activeAdapter
    }

    private fun initViewModel(){
        categoryViewModel = ViewModelProvider(this)[CategoryViewModel::class.java]
    }
    private fun observeViewModel(){
        with(categoryViewModel){
            setTransactionDataFromFirestore(uid)
            transactions.observe(viewLifecycleOwner){list->
                if (list.isNullOrEmpty()){
                    binding.textViewEmpty.visibility = View.VISIBLE
                    binding.lottieEmpty.visibility = View.VISIBLE
                }else{
                    transactionAdapter.updateAdapter(list)
                    val activeList = ArrayList<Active>()
                    list.forEach {
                        activeList.add(Active(it.name,it.company,it.date,it.icon))
                    }
                    activeAdapter.updateAdapter(activeList)
                    binding.textViewEmpty.visibility = View.GONE
                    binding.lottieEmpty.visibility = View.GONE
                }
            }
            isLoading.observe(viewLifecycleOwner){
                val progressBar = binding.progressBar4
                if(it){
                    progressBar.visibility = View.VISIBLE
                }else{
                    progressBar.visibility = View.GONE
                }
            }

            setUserData(uid)
            balance.observe(viewLifecycleOwner){
                setBalanceSp(it)
            }
        }
    }

    private fun setPageTransformer(viewPager : ViewPager2){
        val transformer = CompositePageTransformer()
        transformer.addTransformer { page, position ->
            val x = (1 - kotlin.math.abs(position) * 0.15).toFloat()
            page.scaleY = x
            page.scaleX = x
        }
        viewPager.setPageTransformer(transformer)
    }

    private fun setBalanceSp(balance : Double){
        val sp = requireActivity().getSharedPreferences("Balance", Context.MODE_PRIVATE)
        sp.edit().putFloat("balance",balance.toFloat()).apply()
    }

    private fun getUidSp() : String{
        val sp = requireActivity().getSharedPreferences("uid",Context.MODE_PRIVATE)
        return sp.getString("uid","")!!
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
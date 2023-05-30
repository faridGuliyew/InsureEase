package com.example.insureease.bottomSheetFragments

import android.content.Intent
import android.icu.text.SimpleDateFormat
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.navArgs
import com.example.insureease.R
import com.example.insureease.databinding.FragmentInspectBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.squareup.picasso.Picasso
import java.util.Date
import java.util.Locale
import kotlin.math.round

class InspectFragment : BottomSheetDialogFragment() {
    private var _binding : FragmentInspectBinding? = null
    private val binding
        get() = _binding!!
    private val args : InspectFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInspectBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindViews()
        shareButton(binding.buttonShare)
    }

    private fun bindViews(){
        with(binding){
            val transaction = args.transaction
            Picasso.get().load(transaction.icon).into(imageView50)
            textView50.text = transaction.company
            textViewType.text = transaction.name
            textViewTime.text = currentDate()
            textViewCost.text = "${round(transaction.price!!)} AZN"
        }
    }

    private fun shareButton(button: Button){
        button.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT,"This is a mock data, normally your file will appear here!")
            intent.type = "text/plain"
            if (intent.resolveActivity(requireActivity().packageManager) != null){
                startActivity(intent)
            }
        }
    }

    private fun currentDate() : String{
        return SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
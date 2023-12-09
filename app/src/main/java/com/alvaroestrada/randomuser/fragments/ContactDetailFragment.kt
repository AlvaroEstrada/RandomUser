package com.alvaroestrada.randomuser.fragments

import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alvaroestrada.randomuser.R
import com.alvaroestrada.randomuser.databinding.FragmentContactDetailBinding
import com.alvaroestrada.randomuser.extensions.loadBorderCircularImage
import com.alvaroestrada.randomuser.extensions.loadImage

class ContactDetailFragment : Fragment(R.layout.fragment_contact_detail) {

    private val args: ContactDetailFragmentArgs by navArgs()

    private var _binding: FragmentContactDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentContactDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbar()
        binding.textView.text = args.contact.fullName
        binding.contactImage.loadBorderCircularImage(args.contact.largePicUrl)
        binding.portraitImage.loadImage("file:///android_asset/portrait.png")
    }

    private fun setToolbar() {
        binding.mainToolbar.title = args.contact.fullName
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.mainToolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.mainToolbar.navigationIcon?.colorFilter = PorterDuffColorFilter(
            ContextCompat.getColor(requireContext(), R.color.white),
            PorterDuff.Mode.SRC_ATOP
        )
        binding.mainToolbar.setNavigationOnClickListener { findNavController().navigateUp() }
    }

}
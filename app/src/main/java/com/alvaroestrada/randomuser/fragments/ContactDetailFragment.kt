package com.alvaroestrada.randomuser.fragments

import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.alvaroestrada.randomuser.BuildConfig
import com.alvaroestrada.randomuser.R
import com.alvaroestrada.randomuser.databinding.FragmentContactDetailBinding
import com.alvaroestrada.randomuser.extensions.loadBorderCircularImage
import com.alvaroestrada.randomuser.extensions.loadImage
import com.alvaroestrada.randomuser.extensions.toFormattedDate
import com.alvaroestrada.randomuser.extensions.toast
import com.alvaroestrada.randomuser.models.ContactView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class ContactDetailFragment : Fragment(R.layout.fragment_contact_detail) {

    private val args: ContactDetailFragmentArgs by navArgs()

    private lateinit var mapFragment: SupportMapFragment

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
        setListeners()
        loadUserData()
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

    private fun setListeners(){
        binding.cameraDetailButton.setOnClickListener { requireActivity().toast(R.string.detail_camera_warning) }
        binding.editDetailButton.setOnClickListener { requireActivity().toast(R.string.detail_edit_warning) }
    }

    private fun loadUserData(){
        binding.contactImage.loadBorderCircularImage(args.contact.largePicUrl)
        binding.portraitImage.loadImage("file:///android_asset/portrait.png")
        binding.nameEdt.setText(args.contact.fullName)
        binding.emailEdt.setText(args.contact.email)
        binding.genderIcon.setImageDrawable(getGenderIcon(args.contact))
        binding.genderEdt.setText(getGenderText(args.contact))
        binding.dateEdt.setText(args.contact.registrationDate.toFormattedDate())
        binding.phoneEdt.setText(args.contact.phoneNumber)
        configureMap()
    }

    private fun configureMap(){
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            googleMap.uiSettings.apply {
                isZoomControlsEnabled = false
                isZoomGesturesEnabled = false
                isScrollGesturesEnabled = false
                isTiltGesturesEnabled = false
                isRotateGesturesEnabled = false
            }
            val customMarkerIcon = BitmapDescriptorFactory.fromResource(R.drawable.ic_map_marker)
            val location = LatLng(args.contact.latitude.toDouble(), args.contact.longitude.toDouble())
            val markerOptions = MarkerOptions()
                .position(location)
                .title(args.contact.fullName)
                .icon(customMarkerIcon)
            googleMap.addMarker(markerOptions)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14f))
        }
    }

    private fun getGenderIcon(contact: ContactView): Drawable? {
        return if (contact.gender.lowercase().contains("female")) {
            ContextCompat.getDrawable(requireActivity(), R.drawable.ic_female_detail)
        } else {
            ContextCompat.getDrawable(requireActivity(), R.drawable.ic_male_detail)
        }
    }

    private fun getGenderText(contact: ContactView): String {
        return if (contact.gender.lowercase().contains("female")) {
            "Mujer"
        } else {
            "Hombre"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapFragment.getMapAsync { googleMap ->
            googleMap.clear()
            val defaultLocation = LatLng(0.0, 0.0)
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(defaultLocation))
        }
        _binding = null
    }


}
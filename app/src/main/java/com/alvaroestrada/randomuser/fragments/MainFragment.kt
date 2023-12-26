package com.alvaroestrada.randomuser.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alvaroestrada.randomuser.R
import com.alvaroestrada.randomuser.adapters.ContactsAdapter
import com.alvaroestrada.randomuser.databinding.FilterBottomSheetBinding
import com.alvaroestrada.randomuser.databinding.FragmentMainBinding
import com.alvaroestrada.randomuser.extensions.hideKeyboard
import com.alvaroestrada.randomuser.extensions.toast
import com.alvaroestrada.randomuser.models.ContactView
import com.alvaroestrada.randomuser.viewmodels.MainFragmentViewModel
import com.alvaroestrada.randomuser.viewmodels.UiState
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("NotifyDataSetChanged")
@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainFragmentViewModel by viewModels()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var bindingBottomSheet: FilterBottomSheetBinding

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var contactsAdapter: ContactsAdapter
    private var isSearching: Boolean = false

    private var lastVisibleItemPosition: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindingBottomSheet = FilterBottomSheetBinding.bind(binding.root)
        setToolbar()
        initView()

        savedInstanceState?.let {
            val showShimmer = it.getBoolean("SHIMMER_STATE", true)
            contactsAdapter.showShimmer = showShimmer
            if (!showShimmer) {
                contactsAdapter.notifyDataSetChanged()
            }
        }

        observe()
    }

    private fun setToolbar() {
        binding.mainToolbar.title = getString(R.string.contacts_toolbar_title)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.mainToolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.mainToolbar.setNavigationOnClickListener {  }
    }

    private fun initView() {
        configureRecycler()
        configureBottomSheet()
    }

    private fun observe() {
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    is UiState.Loading -> showLoading()
                    is UiState.Success -> showData(uiState.contacts)
                    is UiState.RefreshSucces -> refreshData(uiState.contacts)
                    is UiState.Error -> showError(uiState.message)
                    is UiState.FilteredList -> showData(uiState.filteredContacts)
                }
            }
        }
    }

    private fun configureRecycler() {
        contactsAdapter = ContactsAdapter { showContactDetail(it) }
        with(binding.contactRv) {
            adapter = contactsAdapter
            layoutManager = LinearLayoutManager(context)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val totalItemCount = layoutManager.itemCount
                    val lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition()

                    if (viewModel.isLoading.not() && isSearching.not() && totalItemCount <= (lastVisibleItemPosition + 10)) {
                        viewModel.loadMoreContacts()
                    }
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    hideKeyboard()
                    lastVisibleItemPosition = 0
                }
            })
        }
    }

    private fun configureBottomSheet() {
        val bottomSheet = requireView().findViewById<View>(R.id.filterBottomSheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        hideKeyboard()
                        isSearching = false
                        viewModel.restoreContacts()
                    }

                    BottomSheetBehavior.STATE_EXPANDED -> {
                        isSearching = true
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {}
        })

        bindingBottomSheet.closeBs.setOnClickListener {
            resetBottomSheet()
        }

        bindingBottomSheet.filterEdt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString().trim().lowercase()
                viewModel.filterContacts(searchText)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

    }

    private fun showContactDetail(contact: ContactView) {
        hideKeyboard()
        bindingBottomSheet.filterEdt.text?.clear()
        resetBottomSheet()
        val layoutManager = binding.contactRv.layoutManager as LinearLayoutManager
        lastVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
        val action = MainFragmentDirections.actionMainDestinationToDetailDestination(contact)
        findNavController().navigate(action)
    }

    private fun showLoading() {
        contactsAdapter.showShimmer = true
        contactsAdapter.notifyDataSetChanged()
    }

    private fun showData(contacts: List<ContactView>?) {
        lifecycleScope.launch {
            contactsAdapter.submitList(contacts) {
                if (lastVisibleItemPosition != RecyclerView.NO_POSITION && lastVisibleItemPosition != 0 && isSearching.not()) {
                    binding.contactRv.layoutManager?.scrollToPosition(lastVisibleItemPosition)
                }
            }
            contactsAdapter.notifyDataSetChanged()
            contactsAdapter.showShimmer = false
        }
    }

    private fun refreshData(contacts: List<ContactView>?) {
        contactsAdapter.showShimmer = true
        contactsAdapter.submitList(emptyList())

        lifecycleScope.launch {
            contactsAdapter.submitList(contacts)
            contactsAdapter.notifyDataSetChanged()
            contactsAdapter.showShimmer = false
        }
    }

    private fun showError(message: Int) {
        requireActivity().toast(getString(message))
        contactsAdapter.showShimmer = false
    }

    @Deprecated("Deprecated in Java")
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.contacts_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Deprecated("Deprecated in Java")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_options -> {
                true
            }

            R.id.filter_option -> {
                if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                } else {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                }
                true
            }

            R.id.refresh_option -> {
                lifecycleScope.launch(Dispatchers.IO) { viewModel.refreshContacts() }
                true
            }

            android.R.id.home -> {
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun resetBottomSheet() {
        closeBottomSheet()
    }

    private fun closeBottomSheet() {
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("SHIMMER_STATE", contactsAdapter.showShimmer)
    }

}
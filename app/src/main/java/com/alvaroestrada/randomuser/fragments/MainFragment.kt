package com.alvaroestrada.randomuser.fragments

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alvaroestrada.randomuser.R
import com.alvaroestrada.randomuser.adapters.ContactsAdapter
import com.alvaroestrada.randomuser.databinding.FilterBottomSheetBinding
import com.alvaroestrada.randomuser.databinding.FragmentMainBinding
import com.alvaroestrada.randomuser.extensions.hide
import com.alvaroestrada.randomuser.extensions.hideKeyboard
import com.alvaroestrada.randomuser.models.ContactView
import com.alvaroestrada.randomuser.viewmodels.MainFragmentViewModel
import com.alvaroestrada.randomuser.viewmodels.UiState
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainFragment : Fragment() {

    private val viewModel: MainFragmentViewModel by viewModels()
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>
    private lateinit var bindingBottomSheet: FilterBottomSheetBinding

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var contactsAdapter: ContactsAdapter
    private var isSearching: Boolean = false

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
        observe()
    }

    private fun setToolbar() {
        binding.mainToolbar.title = getString(R.string.contacts_toolbar_title)
        (requireActivity() as AppCompatActivity).setSupportActionBar(binding.mainToolbar)
        (requireActivity() as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.mainToolbar.setNavigationOnClickListener {  }
    }

    private fun initView(){
        val action1 = MainFragmentDirections.actionMainDestinationToDetailDestination("Usuario")
        configureRecycler()
        configureBottomSheet()
    }

    private fun configureRecycler(){
        contactsAdapter = ContactsAdapter()
        with(binding.contactRv){
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
                }
            })
        }
    }

    private fun configureBottomSheet(){
        val bottomSheet = requireView().findViewById<View>(R.id.filterBottomSheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_COLLAPSED -> {
                        hideKeyboard()
                        isSearching = false
                        viewModel.restoreContacts()
                    }
                    BottomSheetBehavior.STATE_EXPANDED -> { isSearching = true }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {

            }
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

    private fun observe() {
        lifecycleScope.launch {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    is UiState.Loading -> showLoading()
                    is UiState.Success -> showData(uiState.contacts)
                    is UiState.Error -> showError(uiState.message)
                    is UiState.FilteredList -> showData(uiState.filteredContacts)
                }
            }
        }
    }

    private fun showLoading() {
        // Código para mostrar la carga, como un ProgressBar
    }

    private fun showData(contacts: List<ContactView>?) {
        contactsAdapter.submitList(contacts)
    }

    private fun showError(message: String?) {
        // Código para mostrar un mensaje de error, como un Toast o un Snackbar
        // Asegúrate de manejar el caso de mensaje nulo
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.contacts_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

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

}
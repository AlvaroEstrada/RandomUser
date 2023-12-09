package com.alvaroestrada.randomuser.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alvaroestrada.data.repositories.ContactRepository
import com.alvaroestrada.randomuser.mappers.toContactView
import com.alvaroestrada.randomuser.models.ContactView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainFragmentViewModel @Inject  constructor(
    private val repository: ContactRepository
): ViewModel() {
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private var contacts = emptyList<ContactView>()

    var isLoading = false

    init {
        loadContacts()
    }

    private fun loadContacts(){
        viewModelScope.launch {
            try {
                val newUsers = repository.getNextPageOfUsers()?.map { it.toContactView()}
                _uiState.value = UiState.Success(newUsers)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message)
            }
        }
    }

    fun loadMoreContacts() {
        if (!isLoading) {
            viewModelScope.launch {
                isLoading = true
                try {
                    val currentUsers = (_uiState.value as? UiState.Success)?.contacts.orEmpty()
                    val newUsers = repository.getNextPageOfUsers()?.map { it.toContactView() }
                    contacts = currentUsers + newUsers.orEmpty()
                    _uiState.value = UiState.Success(contacts)
                } catch (e: Exception) {
                    _uiState.value = UiState.Error(e.message)
                } finally {
                    isLoading = false
                }
            }
        }
    }

    fun filterContacts(char: String){
        val filteredContacts = contacts.map { it.copy() }.filter { contact ->
            contact.fullName.lowercase().contains(char.lowercase()) || contact.email.lowercase().contains(char.lowercase())
        }
        _uiState.value = UiState.FilteredList(filteredContacts)
    }

    fun restoreContacts(){
        _uiState.value = UiState.Success(contacts)
    }

    fun refreshContacts(){
        loadContacts()
    }
}

sealed class UiState {
    data object Loading : UiState()
    data class Success(val contacts: List<ContactView>?) : UiState()
    data class FilteredList(val filteredContacts: List<ContactView>?) : UiState()
    data class Error(val message: String?) : UiState()
}
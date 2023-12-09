package com.alvaroestrada.data.network.usecases

import com.alvaroestrada.data.repositories.ContactRepository
import com.alvaroestrada.domain.interfaces.view.IContactView
import javax.inject.Inject

class GetContactsUseCase @Inject constructor(
    private val repository: ContactRepository
) {
    suspend fun getContacts(resetPage: Boolean): List<IContactView> {
        return repository.getNextPageOfUsers(resetPage)
    }
}
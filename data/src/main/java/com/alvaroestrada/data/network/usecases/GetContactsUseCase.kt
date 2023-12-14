package com.alvaroestrada.data.network.usecases

import com.alvaroestrada.data.errors.CustomError
import com.alvaroestrada.data.errors.Either
import com.alvaroestrada.data.extensions.ifLeft
import com.alvaroestrada.data.extensions.ifRight
import com.alvaroestrada.data.repositories.ContactRepository
import com.alvaroestrada.domain.interfaces.view.IContactView
import javax.inject.Inject

class GetContactsUseCase @Inject constructor(
    private val repository: ContactRepository
) {
    suspend fun getContacts(resetPage: Boolean): Either<CustomError, List<IContactView>> {
        return repository.getNextPageOfUsers(resetPage)
            .ifLeft { error ->
                Either.Left(error)
            }.ifRight { contactList ->
                Either.Right(contactList)
            }
    }
}
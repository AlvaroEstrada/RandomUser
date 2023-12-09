package com.alvaroestrada.data.repositories

import com.alvaroestrada.data.network.api.ContactApiService
import com.alvaroestrada.domain.interfaces.view.IContactView
import com.alvaroestrada.domain.mappers.ContactMapper
import javax.inject.Inject

class ContactRepository @Inject constructor(
    private val apiService: ContactApiService,
    private val mapper: ContactMapper
) {
    private var currentPage = 1
    private val pageSize = 20

    suspend fun getNextPageOfUsers(): List<IContactView>? {
        val response = apiService.getContacts(currentPage, pageSize)
        if (response.isSuccessful) {
            currentPage = response.body()?.info?.page?.toInt()?.plus(1) ?: 1
            return response.body()?.contacts?.map { mapper.fromRemoteToView(it) }
        } else {
            throw Exception("Error al obtener usuarios")
        }
    }
}
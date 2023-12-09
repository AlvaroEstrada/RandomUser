package com.alvaroestrada.data.network.usecases

import com.alvaroestrada.data.repositories.ContactRepository
import com.alvaroestrada.domain.interfaces.view.IContactView
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class GetContactsUseCaseTest {

    @RelaxedMockK
    private lateinit var repository: ContactRepository

    private lateinit var getContactsUseCase: GetContactsUseCase

    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        getContactsUseCase = GetContactsUseCase(repository)
    }

    @Test
    fun `get contacts returns list of contacts when successful`() = runBlocking {
        // Given
        val expectedContacts = listOf(mockk<IContactView>())
        coEvery { repository.getNextPageOfUsers() } returns expectedContacts

        // When
        val result = getContactsUseCase.getContacts()

        // Then
        coVerify(exactly = 1) { repository.getNextPageOfUsers() }
        assertEquals(expectedContacts, result)
    }

    @Test
    fun `get contacts returns empty list when repository returns null`() = runBlocking {
        // Given
        coEvery { repository.getNextPageOfUsers() } returns null

        // When
        val result = getContactsUseCase.getContacts()

        // Then
        coVerify(exactly = 1) { repository.getNextPageOfUsers() }
        assertEquals(emptyList<IContactView>(), result)
    }
}
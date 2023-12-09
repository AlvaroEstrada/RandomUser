package com.alvaroestrada.data.network.usecases

import com.alvaroestrada.data.repositories.ContactRepository
import com.alvaroestrada.domain.interfaces.view.IContactView
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
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
    fun `getContacts should return list from repository when resetPage is false`() = runBlocking {
        // Given
        val mockContactsList = listOf<IContactView>()
        coEvery { repository.getNextPageOfUsers(false) } returns mockContactsList

        // When
        val result = getContactsUseCase.getContacts(false)

        // Then
        assertEquals(mockContactsList, result)
        coVerify { runBlocking { repository.getNextPageOfUsers(false) } }
    }

    @Test
    fun `getContacts should return a list of 20 contacts when resetPage is true`() = runBlocking {
        // Given
        val dummyContactsList = generateDummyContactsList(20)
        coEvery { repository.getNextPageOfUsers(true) } returns dummyContactsList

        // When
        val result = getContactsUseCase.getContacts(true)

        // Then
        assertEquals(20, result.size)
    }

    private fun generateDummyContactsList(size: Int): List<IContactView> {
        return List(size) { mockk<IContactView>(relaxed = true) }
    }


    @Test(expected = Exception::class)
    fun `getContacts should throw an exception when repository throws an exception`() = runBlocking {
        // Given
        coEvery { repository.getNextPageOfUsers(any()) } throws Exception("Test exception")

        // When
        getContactsUseCase.getContacts(true)

        // Then
        coVerify { runBlocking { repository.getNextPageOfUsers(true) } }
    }

    @Test
    fun `getContacts should return an empty list when repository returns an empty list`() = runBlocking {
        // Given
        coEvery { repository.getNextPageOfUsers(any()) } returns emptyList()

        // When
        val result = getContactsUseCase.getContacts(true)

        // Then
        assertTrue(result.isEmpty())
        coVerify { runBlocking { repository.getNextPageOfUsers(true) } }
    }
}
package com.alvaroestrada.data.network.usecases

import com.alvaroestrada.data.errors.CustomError
import com.alvaroestrada.data.errors.Either
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
    fun `getContacts should return Right with list from repository when resetPage is false`() = runBlocking {
        // Given
        val mockContactsList = listOf<IContactView>()
        coEvery { repository.getNextPageOfUsers(false) } returns Either.Right(mockContactsList)

        // When
        val result = getContactsUseCase.getContacts(false)

        // Then
        assert(result is Either.Right)
        assertEquals(mockContactsList, (result as Either.Right).value)
        coVerify { runBlocking { repository.getNextPageOfUsers(false) } }
    }

    @Test
    fun `getContacts should return Right with a list of 20 contacts when resetPage is true`() = runBlocking {
        // Given
        val dummyContactsList = generateDummyContactsList(20)
        coEvery { repository.getNextPageOfUsers(true) } returns Either.Right(dummyContactsList)

        // When
        val result = getContactsUseCase.getContacts(true)

        // Then
        assert(result is Either.Right)
        assertEquals(20, (result as Either.Right).value.size)
    }

    @Test
    fun `getContacts should return Left with CustomError when repository fails`() = runBlocking {
        // Given
        val mockError = CustomError.NetworkError
        coEvery { repository.getNextPageOfUsers(any()) } returns Either.Left(mockError)

        // When
        val result = getContactsUseCase.getContacts(true)

        // Then
        assert(result is Either.Left)
        assertEquals(mockError, (result as Either.Left).value)
        coVerify { runBlocking { repository.getNextPageOfUsers(true) } }
    }

    @Test
    fun `getContacts should return Right with an empty list when repository returns an empty list`() = runBlocking {
        // Given
        coEvery { repository.getNextPageOfUsers(any()) } returns Either.Right(emptyList())

        // When
        val result = getContactsUseCase.getContacts(true)

        // Then
        assert(result is Either.Right)
        assertTrue((result as Either.Right).value.isEmpty())
        coVerify { runBlocking { repository.getNextPageOfUsers(true) } }
    }

    private fun generateDummyContactsList(size: Int): List<IContactView> {
        return List(size) { mockk<IContactView>(relaxed = true) }
    }
}
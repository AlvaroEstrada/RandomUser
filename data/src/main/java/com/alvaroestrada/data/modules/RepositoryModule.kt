package com.alvaroestrada.data.modules

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.alvaroestrada.data.network.api.ContactApiService
import com.alvaroestrada.data.repositories.ContactRepository
import com.alvaroestrada.domain.mappers.ContactMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideContactRepository(
        apiService: ContactApiService,
        mapper: ContactMapper,
        dataStore: DataStore<Preferences>
    ): ContactRepository {
        return ContactRepository(apiService, mapper, dataStore)
    }
}
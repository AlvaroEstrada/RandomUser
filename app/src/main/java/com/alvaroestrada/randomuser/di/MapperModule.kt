package com.alvaroestrada.randomuser.di

import com.alvaroestrada.domain.mappers.ContactMapper
import com.alvaroestrada.service.implementations.ContactMapperImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object MappersModule {
    @Provides
    fun provideContactMapper(): ContactMapper {
        return ContactMapperImpl()
    }
}
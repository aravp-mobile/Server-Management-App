package dev.arvind.androidtask.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.arvind.androidtask.data.local.dao.ServerDao
import dev.arvind.androidtask.data.repository.ServerRepositoryImpl
import dev.arvind.androidtask.domain.repository.ServerRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindServerRepository(
        serverRepositoryImpl: ServerRepositoryImpl
    ): ServerRepository
}
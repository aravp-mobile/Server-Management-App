package dev.arvind.androidtask.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    // Use cases are automatically provided by Hilt with @Inject constructor
}
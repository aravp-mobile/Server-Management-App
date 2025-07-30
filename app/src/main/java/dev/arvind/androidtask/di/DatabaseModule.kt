package dev.arvind.androidtask.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.arvind.androidtask.data.local.ServerDatabase
import dev.arvind.androidtask.data.local.dao.ServerDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideServerDatabase(@ApplicationContext context: Context): ServerDatabase {
        return Room.databaseBuilder(
            context,
            ServerDatabase::class.java,
            ServerDatabase.DATABASE_NAME
        ).build()
    }

    @Provides
    fun provideServerDao(database: ServerDatabase): ServerDao {
        return database.serverDao()
    }
}
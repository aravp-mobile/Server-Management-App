package dev.arvind.androidtask.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import dev.arvind.androidtask.data.local.dao.ServerDao
import dev.arvind.androidtask.data.local.entities.ServerEntity

@Database(
    entities = [ServerEntity::class],
    version = 1,
    exportSchema = false
)
abstract class ServerDatabase : RoomDatabase() {
    abstract fun serverDao(): ServerDao

    companion object {
        const val DATABASE_NAME = "server_database"
    }
}
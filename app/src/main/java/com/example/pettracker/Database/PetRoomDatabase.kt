package com.example.pettracker.Database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase



@Database(
    entities = [Pet::class],
    version = 1,
    exportSchema = false
)
abstract class PetRoomDatabase : RoomDatabase() {
    abstract fun petRoomDao() : PetDao

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: PetRoomDatabase? = null

        fun getInstance(context: Context): PetRoomDatabase {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context).also { instance = it }
            }
        }

        private fun buildDatabase(context: Context): PetRoomDatabase {
            return Room.databaseBuilder(context, PetRoomDatabase::class.java, "pets").build()
        }
    }
}
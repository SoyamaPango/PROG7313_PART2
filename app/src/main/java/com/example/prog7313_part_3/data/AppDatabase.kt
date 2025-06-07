package com.example.prog7313_part_3.data

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import com.example.room.dao.TransactionDao
import com.example.room.dao.UserDAO
import com.example.room.entity.Transaction
import com.example.room.entity.UserEntity

@Database(entities = [UserEntity::class, Transaction::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDAO(): UserDAO

    abstract fun transactionDAO(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): Any {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(

                    context.applicationContext, AppDatabase::class.java,
                    "user_database"
                ).fallbackToDestructiveMigration(false).build()
                INSTANCE = instance
                instance
            }
        }
    }
}


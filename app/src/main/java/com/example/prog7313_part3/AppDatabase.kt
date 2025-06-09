package com.example.prog7313_part3

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.prog7313_part3.daos.ExpenseDao
import com.example.prog7313_part3.daos.UserDao
import com.example.prog7313_part3.daos.BudgetDao
import com.example.prog7313_part3.entities.Expense
import com.example.prog7313_part3.entities.User
import com.example.prog7313_part3.entities.Budget

@Database(
    entities = [User::class, Expense::class, Budget::class],
    version = 5, // increase when this class has changed
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun budgetDao(): BudgetDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration()  // This will recreate the database if the schema changes
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
package com.example.prog7313_part3.repositories

import androidx.lifecycle.LiveData
import com.example.prog7313_part3.daos.ExpenseDao
import com.example.prog7313_part3.entities.Expense
import kotlinx.coroutines.flow.Flow
import kotlin.text.insert

class ExpenseRepository(private val expenseDao: ExpenseDao) {

    fun getExpensesByUserId(userId: Long): LiveData<List<Expense>> {
        return expenseDao.getExpensesByUserId(userId)
    }

    fun getExpenseById(expenseId: Long): LiveData<Expense> {
        return expenseDao.getExpenseById(expenseId)
    }

    suspend fun insert(expense: Expense): Long {
        return expenseDao.insert(expense)
    }

    fun getAllExpensesForUser(userId: Long): Flow<List<Expense>> {
        return expenseDao.getAllExpensesForUser(userId)
    }

    suspend fun deleteExpense(expense: Expense) {
        expenseDao.delete(expense)
    }

    suspend fun deleteExpenseById(expenseId: Long) {
        expenseDao.deleteById(expenseId)
    }
}
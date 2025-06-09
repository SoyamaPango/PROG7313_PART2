package com.example.prog7313_part3.repositories

import androidx.lifecycle.LiveData
import com.example.prog7313_part3.daos.BudgetDao
import com.example.prog7313_part3.entities.Budget

class BudgetRepository(private val budgetDao: BudgetDao) {

    suspend fun insert(budget: Budget): Long {
        return budgetDao.insert(budget)
    }

    suspend fun update(budget: Budget) {
        budgetDao.update(budget)
    }

    fun getAllBudgets(userId: Long): LiveData<List<Budget>> {
        return budgetDao.getAllBudgets(userId)
    }

    fun getBudgetById(userId: Long, budgetId: Long): LiveData<Budget> {
        return budgetDao.getBudgetById(userId, budgetId)
    }

    fun getBudgetByMonth(
        userId: Long,
        year: Int,
        month: Int,
    ): LiveData<Budget?> {
        return budgetDao.getBudgetByMonth(userId, year, month)
    }

    suspend fun deleteBudget(budgetId: Long) {
        budgetDao.deleteBudgetById(budgetId)
    }

    fun getAllBudgetsByUser(userId: Long): LiveData<List<Budget>> {
        return budgetDao.getAllBudgetsByUser(userId)
    }

    fun getMonthlySpending(userId: Long, month: Int, year: Int): LiveData<Double?> {
        // Format month to 2 digits (e.g. "01" for January)
        val monthStr = String.format("%02d", month)
        val yearStr = year.toString()
        return budgetDao.getMonthlySpending(userId, monthStr, yearStr)
    }
}
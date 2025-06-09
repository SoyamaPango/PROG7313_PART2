package com.example.prog7313_part3.ui.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import com.example.prog7313_part3.AppDatabase
import com.example.prog7313_part3.entities.Budget
import com.example.prog7313_part3.entities.Expense
import com.example.prog7313_part3.repositories.BudgetRepository
import com.example.prog7313_part3.repositories.ExpenseRepository

class DashboardViewModel(application: Application) : AndroidViewModel(application) {

    private val expenseRepository: ExpenseRepository
    private val budgetRepository: BudgetRepository

    init {
        val database = AppDatabase.getDatabase(application)
        expenseRepository = ExpenseRepository(database.expenseDao())
        budgetRepository = BudgetRepository(database.budgetDao())
    }

    // Get current month's budget
    fun getCurrentMonthBudget(userId: Long, year: Int, month: Int): LiveData<Budget?> {
        return budgetRepository.getBudgetByMonth(userId, year, month)
    }

    // Get spending for specific month
    fun getMonthlySpending(userId: Long, month: Int, year: Int): LiveData<Double?> {
        return budgetRepository.getMonthlySpending(userId, month, year)
    }

    // Get most recent budget
    fun getMostRecentBudget(userId: Long): LiveData<Budget?> {
        return budgetRepository.getAllBudgetsByUser(userId).map { budgets ->
            if (budgets.isNotEmpty()) {
                // Sort budgets by year and month (descending) and take the first
                budgets.sortedWith(compareByDescending<Budget> { it.year }
                    .thenByDescending { it.month })
                    .firstOrNull()
            } else {
                null
            }
        }
    }

    // Get recent expenses (limit to 5)
    fun getRecentExpenses(userId: Long): LiveData<List<Expense>> {
        return expenseRepository.getExpensesByUserId(userId).map { expenses ->
            // Sort by date (newest first) and take only the most recent 5
            expenses.sortedByDescending { it.date }.take(5)
        }
    }
}
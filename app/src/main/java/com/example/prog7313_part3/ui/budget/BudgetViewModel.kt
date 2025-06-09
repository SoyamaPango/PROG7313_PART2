package com.example.prog7313_part3.ui.budget

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.prog7313_part3.AppDatabase
import com.example.prog7313_part3.entities.Budget
import com.example.prog7313_part3.repositories.BudgetRepository
import kotlinx.coroutines.launch
import androidx.lifecycle.MediatorLiveData

data class BudgetWithSpending(
    val budget: Budget,
    val currentSpending: Double
)

class BudgetViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BudgetRepository

    init {
        val budgetDao = AppDatabase.getDatabase(application).budgetDao()
        repository = BudgetRepository(budgetDao)
    }

    fun insert(budget: Budget) = viewModelScope.launch {
        repository.insert(budget)
    }

    fun update(budget: Budget) = viewModelScope.launch {
        repository.update(budget)
    }

    fun getAllBudgets(userId: Long): LiveData<List<Budget>> {
        return repository.getAllBudgets(userId)
    }

    fun getBudgetById(userId: Long, budgetId: Long): LiveData<Budget> {
        return repository.getBudgetById(userId, budgetId)
    }

    fun getBudgetByMonth(
        userId: Long,
        year: Int,
        month: Int
    ): LiveData<Budget?> {
        return repository.getBudgetByMonth(userId, year, month)
    }

    fun deleteBudget(budgetId: Long) = viewModelScope.launch {
        repository.deleteBudget(budgetId)
    }

    fun getAllBudgetsByUser(userId: Long): LiveData<List<Budget>> {
        return repository.getAllBudgetsByUser(userId)
    }

    fun getBudgetsWithSpending(userId: Long): LiveData<List<BudgetWithSpending>> {
        val result = MediatorLiveData<List<BudgetWithSpending>>()
        val budgetsLiveData = repository.getAllBudgetsByUser(userId)

        result.addSource(budgetsLiveData) { budgets ->
            if (budgets.isEmpty()) {
                result.value = emptyList()
                return@addSource
            }

            val budgetsWithSpending = mutableListOf<BudgetWithSpending>()
            var pendingQueries = budgets.size

            budgets.forEach { budget ->
                val spendingLiveData = repository.getMonthlySpending(userId, budget.month, budget.year)
                result.addSource(spendingLiveData) { spending ->
                    budgetsWithSpending.add(BudgetWithSpending(budget, spending ?: 0.0))
                    pendingQueries--

                    if (pendingQueries == 0) {
                        result.value = budgetsWithSpending
                    }

                    // Remove source after getting the value
                    result.removeSource(spendingLiveData)
                }
            }
        }

        return result
    }
}
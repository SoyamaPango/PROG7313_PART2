package com.example.prog7313_part3.ui.expenses

import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.AndroidViewModel
import com.example.prog7313_part3.AppDatabase
import com.example.prog7313_part3.entities.Expense
import com.example.prog7313_part3.repositories.ExpenseRepository
import android.app.Application
import kotlinx.coroutines.launch


class ExpensesViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ExpenseRepository

    init {
        val expenseDao = AppDatabase.getDatabase(application).expenseDao()
        repository = ExpenseRepository(expenseDao)
    }

    fun insert(expense: Expense) = viewModelScope.launch {
        repository.insert(expense)
    }

    fun getUserExpenses(userId: Long): LiveData<List<Expense>> {
        return repository.getExpensesByUserId(userId)
    }

    fun getExpenseById(expenseId: Long) = repository.getExpenseById(expenseId)

    fun deleteExpense(expenseId: Long) {
        viewModelScope.launch {
            repository.deleteExpenseById(expenseId)
        }
    }
}
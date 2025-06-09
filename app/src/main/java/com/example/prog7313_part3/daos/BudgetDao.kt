package com.example.prog7313_part3.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.prog7313_part3.entities.Budget

@Dao
interface BudgetDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: Budget): Long

    @Update
    suspend fun update(budget: Budget)

    @Query("SELECT * FROM budgets WHERE userId = :userId ORDER BY year DESC, month DESC")
    fun getAllBudgets(userId: Long): LiveData<List<Budget>>

    @Query("SELECT * FROM budgets WHERE userId = :userId AND id = :budgetId")
    fun getBudgetById(userId: Long, budgetId: Long): LiveData<Budget>

    @Query("SELECT * FROM budgets WHERE userId = :userId AND year = :year AND month = :month")
    fun getBudgetByMonth(
        userId: Long,
        year: Int,
        month: Int
    ): LiveData<Budget?>

    @Query("DELETE FROM budgets WHERE id = :budgetId")
    suspend fun deleteBudgetById(budgetId: Long)

    @Query("SELECT * FROM budgets WHERE userId = :userId ORDER BY year ASC, month ASC")
    fun getAllBudgetsByUser(userId: Long): LiveData<List<Budget>>

    @Query("SELECT SUM(amount) FROM expenses WHERE userId = :userId AND strftime('%m', date/1000, 'unixepoch') = :month AND strftime('%Y', date/1000, 'unixepoch') = :year")
    fun getMonthlySpending(userId: Long, month: String, year: String): LiveData<Double?>

    @Query("SELECT * FROM budgets WHERE userId = :userId ORDER BY year DESC, month DESC LIMIT 1")
    fun getMostRecentBudget(userId: Long): LiveData<Budget?>
}
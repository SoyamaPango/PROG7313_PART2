package  vcmsa.projects.tcss.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ExpenseDao {
    @Insert
    suspend fun insertExpense(expense: Expense)

    @Query("SELECT * FROM expenses ORDER BY date DESC")
    suspend fun getAllExpenses(): List<Expense>
}

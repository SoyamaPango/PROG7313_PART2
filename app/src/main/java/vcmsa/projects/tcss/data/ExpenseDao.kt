package vcmsa.projects.tcss.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ExpenseDao {

    // Insert method with the correct return type
   @Insert
    suspend fun insertExpense(expense: Expense): Long

    // Query method to fetch all expenses, with a return type of List<Expense>
    @Query("SELECT * FROM expense")
    suspend fun getAllExpenses(): List<Expense>
}



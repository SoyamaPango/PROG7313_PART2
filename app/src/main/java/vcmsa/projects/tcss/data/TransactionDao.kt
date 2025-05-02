package vcmsa.projects.tcss.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TransactionDao {

    // Insert method with the correct return type
    @Insert
    suspend fun insertTransaction(transaction: Transaction): Long

    // Query method to fetch all expenses, with a return type of List<Transaction>
    @Query("SELECT * FROM `transactions`")
    suspend fun getAllTransactions(): List<Transaction>

    // Query method to fetch expenses for a specific user, with a return type of List<Transaction>
    @Query("SELECT * FROM `transactions` WHERE Id = :userId")
    suspend fun getUserTransactions(userId: Long): List<Transaction>
}



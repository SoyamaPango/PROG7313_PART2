
package vcmsa.projects.tcss.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val date: Long,
    val description: String,
    val amount: Double,
    val category: String,
    val photoUri: String? = null
)

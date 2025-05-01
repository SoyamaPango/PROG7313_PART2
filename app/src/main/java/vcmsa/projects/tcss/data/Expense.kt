package vcmsa.projects.tcss.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: Double,
    val date: String,
    val description: String,
    val category: String,
    val imagePath: String? = null
)

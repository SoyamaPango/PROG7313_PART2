package vcmsa.projects.tcss.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    // @ColumnInfo(name = "amount")
    val amount: Double,

    //  @ColumnInfo(name = "date")
    val date: String,

    // @ColumnInfo(name = "description")
    val description: String,

    // @ColumnInfo(name = "category")
    val category: String
)

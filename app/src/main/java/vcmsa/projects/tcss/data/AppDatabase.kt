
package vcmsa.projects.tcss.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import vcmsa.projects.tcss.models.Expense

@Database(entities = [Expense::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "expense_database"
                ).build().also { INSTANCE = it }
            }
        }
    }
}

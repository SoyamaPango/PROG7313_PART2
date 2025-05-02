package vcmsa.projects.tcss

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.Room
import vcmsa.projects.tcss.data.Transaction
import vcmsa.projects.tcss.data.TransactionDao
import vcmsa.projects.tcss.data.UserDAO
import vcmsa.projects.tcss.data.UserEntity

@Database(entities = [UserEntity::class, Transaction::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun userDAO(): UserDAO

    abstract fun transactionDAO(): TransactionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): Any {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(

                    context.applicationContext, AppDatabase::class.java,
                    "user_database"
                ).fallbackToDestructiveMigration(false).build()
                INSTANCE = instance
                instance
            }
        }
    }
}



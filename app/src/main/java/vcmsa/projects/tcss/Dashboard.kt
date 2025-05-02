package vcmsa.projects.tcss

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.ComponentActivity
import java.text.NumberFormat
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.launch
import vcmsa.projects.tcss.data.TransactionDao

class DashboardActivity : ComponentActivity() {

    private lateinit var db: AppDatabase
    private lateinit var transactionDao: TransactionDao
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_page)

        val imageButtons = arrayOf(
            R.id.imgTransaction1,
            R.id.imgTransaction2,
            R.id.imgTransaction3,
            R.id.imgTransaction4
        )


        //Database
        userPreferences =
            UserPreferences(this) // Initialize user preferences before using it in the coroutine
        Log.d(
            "DashboardActivity", "User Preferences initialized"
        )

        userPreferences = UserPreferences(this)
        db =
            Room.databaseBuilder(applicationContext, AppDatabase::class.java, "transactions.db")
                .build()
        transactionDao = db.transactionDAO()

        lifecycleScope.launch {
            userPreferences.userId.collect { userId ->
                Log.d("DashboardActivity", "User ID: $userId")
            }
            userPreferences.userId.collect { userId ->
                if (userId != -1L) {
                    val transactions = transactionDao.getUserTransactions(userId)
                    for (i in 0 until imageButtons.size) {
                        Log.d("Dashboard", "Amount $i: ${transactions.get(i).amount}")
                        Log.d("Dashboard", "Date $i: ${transactions.get(i).date}")
                        Log.d("Dashboard", "Category $i: ${transactions.get(i).category}")
                        Log.d("Dashboard", "Description $i: ${transactions.get(i).description}")

                    }
                }
            }
        }


        val format = NumberFormat.getCurrencyInstance()
        val Balance: Float = 900f  //Change balance value using database
        val formattedBalance = format.format(Balance)
        findViewById<TextView>(R.id.lblBalance).setText(formattedBalance)

        //Functionality code
        val FinancailStat = findViewById<ImageButton>(R.id.imgFinacialStat)
        FinancailStat.setOnClickListener {
            //Show finacial state of user
        }
        val Transaction1 = findViewById<ImageButton>(R.id.imgTransaction1)
        Transaction1.setOnClickListener {
            //Show transaction history

        }
        val Transaction2 = findViewById<ImageButton>(R.id.imgTransaction2)
        Transaction2.setOnClickListener {
            //Show transaction history
        }
        val Transaction3 = findViewById<ImageButton>(R.id.imgTransaction3)
        Transaction3.setOnClickListener {
            //Show transaction history
        }
        val Transaction4 = findViewById<ImageButton>(R.id.imgTransaction4)
        Transaction4.setOnClickListener {
            //Show transaction history
        }

        //Nav bar
        val Home = findViewById<ImageButton>(R.id.btnHome)
        Home.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }

        val Transaction = findViewById<ImageButton>(R.id.btnTransactions)
        Transaction.setOnClickListener {
            val intent = Intent(this, TransactionPage::class.java)
            startActivity(intent)
        }

        val Profile = findViewById<ImageButton>(R.id.btnAccount)
        Profile.setOnClickListener {
            val intent = Intent(this, ProfileActivity::class.java)
            startActivity(intent)
        }

        val Setting = findViewById<ImageButton>(R.id.btnSettings)
        Setting.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

    }
}
//
//class ExpenseListActivity : ComponentActivity() {
//
//    private lateinit var expenseContainer: LinearLayout
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_expense_list)
//
//
//        //Dadabase
////        expenseContainer = findViewById(R.id.expenseContainer)
////
////        lifecycleScope.launch {
////            val expenses = AppDatabase.getDatabase(applicationContext).expenseDao().getAllExpenses()
////
////            expenses.forEach { expense ->
////                val text = TextView(this@ExpenseListActivity)
////                text.text =
////                    "${expense.date} - ${expense.category}: R${expense.amount}\n${expense.description}"
////                expenseContainer.addView(text)
////            }
////        }
//    }
//}

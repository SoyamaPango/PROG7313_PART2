package vcmsa.projects.tcss

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import java.text.NumberFormat

class DashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_page)

        //Functionality code
        val FinancailStat = findViewById<ImageButton>(R.id.imgFinacialStat)
        FinancailStat.setOnClickListener {
            //Show finacial state of user


            val format = NumberFormat.getCurrencyInstance()
            val Balance: Float = 900f  //Change balance value using database
            val formattedBalance = format.format(Balance)
            findViewById<TextView>(R.id.lblBalance).setText(formattedBalance)

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

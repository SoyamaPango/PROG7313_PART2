
package vcmsa.projects.tcss

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_page)


    }
}

class ExpenseListActivity : AppCompatActivity() {

    private lateinit var expenseContainer: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_list)

        expenseContainer = findViewById(R.id.expenseContainer)

        lifecycleScope.launch {
            val expenses = AppDatabase.getDatabase(applicationContext).expenseDao().getAllExpenses()

            expenses.forEach { expense ->
                val text = TextView(this@ExpenseListActivity)
                text.text = "${expense.date} - ${expense.category}: R${expense.amount}\n${expense.description}"
                expenseContainer.addView(text)
            }
        }
    }
}

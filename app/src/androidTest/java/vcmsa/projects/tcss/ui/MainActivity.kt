package vcmsa.projects.tcss.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import vcmsa.projects.tcss.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnAdd = findViewById<Button>(R.id.btnAddExpense)
        val btnView = findViewById<Button>(R.id.btnViewExpenses)

        btnAdd.setOnClickListener {
            startActivity(Intent(this, vcmsa.projects.tcss.ui.AddExpenseActivity::class.java))
        }

        btnView.setOnClickListener {
            startActivity(Intent(this, vcmsa.projects.tcss.ui.ExpenseListActivity::class.java))
        }

        // TODO: Uncomment and implement these later
        // val btnLogin = findViewById<Button>(R.id.btnLogin)
        // val btnSetGoals = findViewById<Button>(R.id.btnSetGoals)
    }
}
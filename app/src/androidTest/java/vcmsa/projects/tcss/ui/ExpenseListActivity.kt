package vcmsa.projects.tcss.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.launch
import vcmsa.projects.tcss.R
import vcmsa.projects.tcss.adapter.ExpenseAdapter
import vcmsa.projects.tcss.data.AppDatabase

class ExpenseListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExpenseAdapter
    private val expenses = mutableListOf<Expense>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expense_list)

        recyclerView = findViewById(R.id.recyclerViewExpenses)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = ExpenseAdapter(expenses)
        recyclerView.adapter = adapter

        // Load data from RoomDB
        lifecycleScope.launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val allExpenses = db.expenseDao().getAllExpenses()
            expenses.addAll(allExpenses)
            adapter.notifyDataSetChanged()
        }
    }
}

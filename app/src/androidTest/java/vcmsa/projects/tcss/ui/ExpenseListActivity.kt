package vcmsa.projects.tcss

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import vcmsa.projects.tcss.data.AppDatabase
import vcmsa.projects.tcss.data.Expense
import kotlinx.coroutines.launch
import vcmsa.projects.tcss.R

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

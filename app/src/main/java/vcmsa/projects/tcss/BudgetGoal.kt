package vcmsa.projects.tcss


import android.os.Bundle
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity

class BudgetActivity : ComponentActivity() {

    private lateinit var totalBudgetInput: EditText
    private lateinit var spinnerCategory: Spinner
    private lateinit var categoryLimit: EditText
    private lateinit var saveBudgetButton: Button

    private val categories = listOf("Food", "Transport", "Entertainment", "Utilities", "Savings")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budgetgoal)

        totalBudgetInput = findViewById(R.id.totalBudgetInput)
        spinnerCategory = findViewById(R.id.spinnerCategory)
        categoryLimit = findViewById(R.id.categoryLimit)
        saveBudgetButton = findViewById(R.id.saveBudgetButton)

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerCategory.adapter = adapter

        saveBudgetButton.setOnClickListener {
            saveBudget()
        }
    }

    private fun saveBudget() {
        val totalBudget = totalBudgetInput.text.toString().toDoubleOrNull()
        val selectedCategory = spinnerCategory.selectedItem.toString()
        val categoryLimit = categoryLimit.text.toString().toDoubleOrNull()

        if (totalBudget == null || categoryLimit == null) {
            Toast.makeText(this, "Please enter valid amounts", Toast.LENGTH_SHORT).show()
            return
        }

        val message =
            "Total Budget: R$totalBudget\nCategory: $selectedCategory\nLimit: R$categoryLimit"
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}


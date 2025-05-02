package vcmsa.projects.tcss.ui


import vcmsa.projects.tcss.R
import vcmsa.projects.tcss.data.AppDatabase
import vcmsa.projects.tcss.data.Expense
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity


class AddExpenseActivity : AppCompatActivity() {
    private lateinit var amountInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var dateButton: Button
    private lateinit var categoryInput: EditText
    private lateinit var saveButton: Button
    private var selectedDate: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        amountInput = findViewById(R.id.editAmount)
        descriptionInput = findViewById(R.id.editDescription)
        dateButton = findViewById(R.id.btnSelectDate)
        categoryInput = findViewById(R.id.editCategory)
        saveButton = findViewById(R.id.btnSave)

        dateButton.setOnClickListener {
            val calendar = Calendar.getInstance()
            DatePickerDialog(this, { _, year, month, dayOfMonth ->
                selectedDate = "$year-${month + 1}-$dayOfMonth"
                dateButton.text = selectedDate
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        saveButton.setOnClickListener {
            val amount = amountInput.text.toString().toDoubleOrNull()
            val description = descriptionInput.text.toString()
            val category = categoryInput.text.toString()

            if (amount == null || selectedDate.isEmpty() || category.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val expense = Expense(
                amount = amount,
                date = selectedDate,
                description = description,
                category = category
            )

            lifecycleScope.launch {
                AppDatabase.getDatabase(applicationContext).expenseDao().insertExpense(expense)
                finish()
            }
        }
    }
}
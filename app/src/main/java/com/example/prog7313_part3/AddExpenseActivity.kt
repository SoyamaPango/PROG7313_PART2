package com.example.prog7313_part3

import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.prog7313_part3.entities.Expense
import java.util.Date
import com.example.prog7313_part3.databinding.ActivityAddExpenseBinding
import com.example.prog7313_part3.ui.expenses.ExpensesViewModel
import kotlin.text.insert
import kotlin.text.toLong


class AddExpenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddExpenseBinding
    private lateinit var viewModel: ExpensesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Add New Expense"

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[ExpensesViewModel::class.java]

        // Setup category dropdown
        setupCategoryDropdown()
        setupCurrencyFormatting()

        // Setup save button
        binding.btnSaveExpense.setOnClickListener {
            saveExpense()
        }
    }

    private fun setupCategoryDropdown() {
        val categories = arrayOf(
            "Food",
            "Transportation",
            "Housing",
            "Entertainment",
            "Utilities",
            "Healthcare",
            "Other",
            "Create own Category"  // New option
        )
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, categories)
        binding.dropdownCategory?.apply {
            setAdapter(adapter)
            setText(categories[0], false)

            // Set listener to detect when "Create own Category" is selected
            setOnItemClickListener { _, _, position, _ ->
                if (position == categories.size - 1) { // Last item
                    // Show custom category input field
                    binding.layoutCustomCategory.visibility = android.view.View.VISIBLE
                } else {
                    // Hide custom category input field
                    binding.layoutCustomCategory.visibility = android.view.View.GONE
                }
            }
        }
    }

    private fun setupCurrencyFormatting() {
        binding.editTextAmount.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: android.text.Editable?) {
                if (s.toString().isNotEmpty() && !s.toString().equals("R")) {
                    binding.editTextAmount.removeTextChangedListener(this)

                    // Remove any existing formatting
                    var cleanString = s.toString().replace(Regex("[R,.]"), "")

                    // Parse to a number
                    val parsed = cleanString.toDoubleOrNull() ?: 0.0
                    val formatted = String.format("%,.2f", parsed / 100)

                    binding.editTextAmount.setText(formatted)
                    binding.editTextAmount.setSelection(formatted.length)
                    binding.editTextAmount.addTextChangedListener(this)
                }
            }
        })
    }

    private fun saveExpense() {
        try {
            val description = binding.editTextDescription.text.toString().trim()
            val amountStr = binding.editTextAmount.text.toString().trim()
            val selectedCategory = (binding.layoutCategory.editText as? AutoCompleteTextView)?.text.toString()

            // Determine which category to use
            val category = if (selectedCategory == "Create own Category") {
                binding.editTextCustomCategory.text.toString().trim()
            } else {
                selectedCategory
            }

            // Validate inputs
            if (description.isEmpty() || amountStr.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return
            }

            // Additional validation for custom category
            if (selectedCategory == "Create own Category" && category.isEmpty()) {
                Toast.makeText(this, "Please enter a custom category", Toast.LENGTH_SHORT).show()
                return
            }

            // Parse the amount correctly by removing formatting characters
            val cleanAmountStr = amountStr.replace(",", "").replace("R", "")
            val amount = cleanAmountStr.toDoubleOrNull()

            if (amount == null || amount <= 0) {
                Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
                return
            }

            // Get current user ID from session
            val sessionManager = SessionManager(applicationContext)
            val userId = sessionManager.getUserId().toLong()

            // Create expense object
            val expense = Expense(
                userId = userId,
                amount = amount,
                category = category,
                date = Date().time,  // Current timestamp
                description = description
            )

            // Save expense to database
            viewModel.insert(expense)

            Toast.makeText(this, "Expense added successfully", Toast.LENGTH_SHORT).show()
            finish()
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
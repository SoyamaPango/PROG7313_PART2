package com.example.prog7313_part3

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.prog7313_part3.databinding.ActivityAddBudgetBinding
import com.example.prog7313_part3.entities.Budget
import com.example.prog7313_part3.ui.budget.BudgetViewModel
import java.util.Calendar

class AddBudgetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBudgetBinding
    private lateinit var viewModel: BudgetViewModel
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBudgetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(applicationContext)
        viewModel = ViewModelProvider(this).get(BudgetViewModel::class.java)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        setupYearDropdown()
        setupMonthDropdown()
        setupCurrencyFormatting(binding.editTextMinAmount)
        setupCurrencyFormatting(binding.editTextMaxAmount)

        binding.buttonSave.setOnClickListener {
            saveBudget()
        }
    }

    private fun setupYearDropdown() {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val years = (currentYear - 1..currentYear + 5).map { it.toString() }.toTypedArray()

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, years)
        (binding.layoutYear.editText as? AutoCompleteTextView)?.apply {
            setAdapter(adapter)
            setText(currentYear.toString(), false)
        }
    }

    private fun setupMonthDropdown() {
        val months = arrayOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )

        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, months)
        (binding.layoutMonth.editText as? AutoCompleteTextView)?.apply {
            setAdapter(adapter)

            // Set current month as default
            val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
            setText(months[currentMonth], false)
        }
    }

    private fun setupCurrencyFormatting(editText: com.google.android.material.textfield.TextInputEditText) {
        editText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                if (s.toString().isNotEmpty() && !s.toString().equals("R")) {
                    editText.removeTextChangedListener(this)

                    // Remove any existing formatting
                    var cleanString = s.toString().replace(Regex("[R,.]"), "")

                    // Parse to a number
                    val parsed = cleanString.toDoubleOrNull() ?: 0.0
                    val formatted = String.format("%,.2f", parsed / 100)

                    editText.setText(formatted)
                    editText.setSelection(formatted.length)
                    editText.addTextChangedListener(this)
                }
            }
        })
    }

    private fun saveBudget() {
        try {
            val yearText = (binding.layoutYear.editText as? AutoCompleteTextView)?.text.toString()
            val monthText = (binding.layoutMonth.editText as? AutoCompleteTextView)?.text.toString()
            val minAmountStr = binding.editTextMinAmount.text.toString().trim()
            val maxAmountStr = binding.editTextMaxAmount.text.toString().trim()

            // Validate inputs
            if (yearText.isEmpty() || monthText.isEmpty() || minAmountStr.isEmpty() || maxAmountStr.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return
            }

            val year = yearText.toInt()
            val month = getMonthNumber(monthText)

            val minAmount = parseAmount(minAmountStr)
            val maxAmount = parseAmount(maxAmountStr)

            if (minAmount >= maxAmount) {
                Toast.makeText(this, "Maximum amount must be greater than minimum amount", Toast.LENGTH_SHORT).show()
                return
            }

            // Get current user ID from session
            val userId = sessionManager.getUserId().toLong()

            // Create budget object
            val budget = Budget(
                userId = userId,
                month = month,
                year = year,
                minAmount = minAmount,
                maxAmount = maxAmount
            )

            // Use observe once pattern to check if budget exists
            viewModel.getBudgetByMonth(userId, year, month).observe(this, object : androidx.lifecycle.Observer<Budget?> {
                override fun onChanged(existingBudget: Budget?) {
                    // Remove the observer to prevent multiple callbacks
                    viewModel.getBudgetByMonth(userId, year, month).removeObserver(this)

                    if (existingBudget != null) {
                        Toast.makeText(
                            this@AddBudgetActivity,
                            "Budget for $monthText $year already exists. Please update it instead.",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        // Save budget to database
                        viewModel.insert(budget)
                        Toast.makeText(this@AddBudgetActivity, "Budget added successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            })
        } catch (e: Exception) {
            Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getMonthNumber(monthName: String): Int {
        val months = arrayOf(
            "January", "February", "March", "April", "May", "June",
            "July", "August", "September", "October", "November", "December"
        )
        return months.indexOf(monthName) + 1
    }

    private fun parseAmount(amountStr: String): Double {
        // Remove formatting characters and convert to double
        return amountStr.replace(Regex("[R,]"), "").toDoubleOrNull() ?: 0.0
    }
}
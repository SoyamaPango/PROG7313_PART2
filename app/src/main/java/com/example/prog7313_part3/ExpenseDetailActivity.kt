package com.example.prog7313_part3

import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.prog7313_part3.databinding.ActivityExpenseDetailBinding
import com.example.prog7313_part3.entities.Expense
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import com.example.prog7313_part3.ui.expenses.ExpensesViewModel
import java.io.File

class ExpenseDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExpenseDetailBinding
    private lateinit var viewModel: ExpensesViewModel
    private var expenseId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExpenseDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up toolbar
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Expense Details"

        // Initialize ViewModel
        viewModel = ViewModelProvider(this)[ExpensesViewModel::class.java]

        // Get expense ID from intent
        expenseId = intent.getLongExtra(EXTRA_EXPENSE_ID, 0)
        if (expenseId == 0L) {
            Toast.makeText(this, "Error loading expense", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Load expense details
        loadExpenseDetails()

        // Set up button click listeners
        setupButtonListeners()
    }

    private fun loadExpenseDetails() {
        viewModel.getExpenseById(expenseId).observe(this) { expense ->
            if (expense != null) {
                displayExpenseDetails(expense)
            } else {
                Toast.makeText(this, "Expense not found", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun displayExpenseDetails(expense: Expense) {
        // Format currency for South African Rand
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "ZA"))

        // Format date
        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val date = Date(expense.date)

        binding.textCategory.text = expense.category
        binding.textAmount.text = currencyFormat.format(expense.amount)
        binding.textDescription.text = expense.description
        binding.textDate.text = dateFormat.format(date)

        if (!expense.imagePath.isNullOrEmpty()) {
            val imageFile = File(expense.imagePath)
            if (imageFile.exists()) {
                binding.receiptImage.visibility = View.VISIBLE
                binding.noImageText.visibility = View.GONE
                binding.receiptImage.setImageURI(Uri.fromFile(imageFile))
            } else {
                binding.receiptImage.visibility = View.GONE
                binding.noImageText.visibility = View.VISIBLE
            }
        } else {
            binding.receiptImage.visibility = View.GONE
            binding.noImageText.visibility = View.VISIBLE
        }
    }

    private fun setupButtonListeners() {
        binding.btnEditExpense.setOnClickListener {
            // TODO: Implement edit functionality
            Toast.makeText(this, "Edit functionality to be implemented", Toast.LENGTH_SHORT).show()
        }

        binding.btnDeleteExpense.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Delete Expense")
            .setMessage("Are you sure you want to delete this expense?")
            .setPositiveButton("Delete") { _, _ ->
                deleteExpense()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteExpense() {
        viewModel.deleteExpense(expenseId)
        Toast.makeText(this, "Expense deleted", Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_EXPENSE_ID = "extra_expense_id"
    }
}
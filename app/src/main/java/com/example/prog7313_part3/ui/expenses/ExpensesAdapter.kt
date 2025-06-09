package com.example.prog7313_part3.ui.expenses

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.prog7313_part3.entities.Expense
import com.example.prog7313_part3.databinding.ItemExpenseBinding
import java.io.File
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExpensesAdapter(private val onItemClick: (Expense) -> Unit) :
    ListAdapter<Expense, ExpensesAdapter.ExpenseViewHolder>(ExpenseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ItemExpenseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExpenseViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ExpenseViewHolder(
        private val binding: ItemExpenseBinding,
        private val onItemClick: (Expense) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(expense: Expense) {
            // Format currency for South African Rand
            val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "ZA"))

            // Format date
            val dateFormat = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val date = Date(expense.date)

            binding.textCategory.text = expense.category
            binding.textAmount.text = currencyFormat.format(expense.amount)
            binding.textDescription.text = expense.description
            binding.textDate.text = dateFormat.format(date)

            if (!expense.imagePath.isNullOrEmpty()) {
                val imageFile = File(expense.imagePath)
                if (imageFile.exists()) {
                    binding.expenseImage.visibility = View.VISIBLE
                    binding.expenseImage.setImageURI(Uri.fromFile(imageFile))
                } else {
                    binding.expenseImage.visibility = View.GONE
                }
            } else {
                binding.expenseImage.visibility = View.GONE
            }

            // Set click listener on the item
            binding.root.setOnClickListener {
                onItemClick(expense)
            }

            class ExpenseDiffCallback : DiffUtil.ItemCallback<Expense>() {
                override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
                    return oldItem.id == newItem.id
                }

                override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
                    return oldItem == newItem
                }
        }
    }
    }

    class ExpenseDiffCallback : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem == newItem
        }
    }
}
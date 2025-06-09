package com.example.prog7313_part3.ui.budget

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.prog7313_part3.databinding.ItemBudgetBinding
import java.text.NumberFormat
import java.util.Locale
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import java.text.SimpleDateFormat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.DiffUtil


class BudgetAdapter(private val onItemClick: (BudgetWithSpending) -> Unit) :
    ListAdapter<BudgetWithSpending, BudgetAdapter.BudgetViewHolder>(BudgetDiffCallback()) {

    class BudgetViewHolder(val binding: ItemBudgetBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BudgetViewHolder {
        val binding = ItemBudgetBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return BudgetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BudgetViewHolder, position: Int) {
        val context = holder.itemView.context
        val budgetWithSpending = getItem(position)
        val budget = budgetWithSpending.budget
        val currentSpending = budgetWithSpending.currentSpending

        // Format month and year
        val monthDate = SimpleDateFormat("MM", Locale.getDefault()).parse("${budget.month}")!!
        val monthShort = SimpleDateFormat("MMM", Locale.getDefault()).format(monthDate)

        // Set month and year separately
        holder.binding.textMonthShort.text = monthShort
        holder.binding.textYear.text = budget.year.toString()

        // Format currency values
        val formatter = NumberFormat.getCurrencyInstance(Locale("en", "ZA"))
        val minAmountFormatted = formatter.format(budget.minAmount)
        val maxAmountFormatted = formatter.format(budget.maxAmount)
        val currentSpendingFormatted = formatter.format(currentSpending)

        // Set current spending with color depending on budget status
        holder.binding.textCurrentSpending.text = currentSpendingFormatted

        // Rest of your existing code remains the same
        holder.binding.textMinAmount.text = "Min: $minAmountFormatted"
        holder.binding.textMaxAmount.text = "Max: $maxAmountFormatted"

        // Configure progress bar
        val progress = if (budget.maxAmount > 0) {
            ((currentSpending / budget.maxAmount) * 100).toInt().coerceIn(0, 100)
        } else 0

        holder.binding.progressSpending.progress = progress

        // Color code based on spending level
        when {
            currentSpending < budget.minAmount -> {
                holder.binding.textCurrentSpending.setTextColor(
                    ContextCompat.getColor(context, android.R.color.holo_blue_dark)
                )
                holder.binding.progressSpending.progressTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.holo_blue_light))
            }
            currentSpending <= budget.maxAmount -> {
                holder.binding.textCurrentSpending.setTextColor(
                    ContextCompat.getColor(context, android.R.color.holo_green_dark)
                )
                holder.binding.progressSpending.progressTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.holo_green_light))
            }
            else -> {
                holder.binding.textCurrentSpending.setTextColor(
                    ContextCompat.getColor(context, android.R.color.holo_red_dark)
                )
                holder.binding.progressSpending.progressTintList =
                    ColorStateList.valueOf(ContextCompat.getColor(context, android.R.color.holo_red_light))
            }
        }

        // Set max for progress
        holder.binding.progressSpending.max = 100

        // Set click listener
        holder.itemView.setOnClickListener {
            onItemClick(budgetWithSpending)
        }
    }

    class BudgetDiffCallback : DiffUtil.ItemCallback<BudgetWithSpending>() {
        override fun areItemsTheSame(oldItem: BudgetWithSpending, newItem: BudgetWithSpending): Boolean {
            return oldItem.budget.id == newItem.budget.id
        }

        override fun areContentsTheSame(oldItem: BudgetWithSpending, newItem: BudgetWithSpending): Boolean {
            return oldItem.budget == newItem.budget &&
                    oldItem.currentSpending == newItem.currentSpending
        }
    }
}
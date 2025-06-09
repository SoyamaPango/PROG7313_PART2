package com.example.prog7313_part3.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prog7313_part3.SessionManager
import com.example.prog7313_part3.databinding.FragmentDashboardBinding
import com.example.prog7313_part3.ui.expenses.ExpensesAdapter
import java.text.NumberFormat
import java.util.*
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.components.XAxis

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!
    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var expensesAdapter: ExpensesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        dashboardViewModel = ViewModelProvider(this).get(DashboardViewModel::class.java)

        // Get current user ID from session
        val sessionManager = SessionManager(requireContext())
        val userId = sessionManager.getUserId().toLong()

        // Setup RecyclerView for recent transactions
        setupRecentTransactions(userId)

        // Setup current month's budget display
        setupBudgetDisplay(userId)

        // Setup expense graph
        setupExpenseGraph(userId)
    }

    private fun setupRecentTransactions(userId: Long) {
        // Initialize adapter with click handling
        expensesAdapter = ExpensesAdapter { expense ->
            // You can handle clicks if needed, or pass empty lambda
        }

        binding.recentTransactionsRecycler.apply {
            adapter = expensesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        // Get recent expenses (limited to 5)
        dashboardViewModel.getRecentExpenses(userId).observe(viewLifecycleOwner) { expenses ->
            expensesAdapter.submitList(expenses)
        }
    }

    private fun setupBudgetDisplay(userId: Long) {
        // Get current month and year
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH) + 1 // Calendar months are 0-based
        val currentYear = calendar.get(Calendar.YEAR)

        // Format currency
        val currencyFormat = NumberFormat.getCurrencyInstance(Locale("en", "ZA"))

        // Get current month's budget
        dashboardViewModel.getCurrentMonthBudget(userId, currentYear, currentMonth)
            .observe(viewLifecycleOwner) { budget ->
                if (budget != null) {
                    // Budget exists for current month
                    binding.budgetTitle.text = "Budget for ${getMonthName(currentMonth)} $currentYear"
                    binding.budgetAmount.text = "${currencyFormat.format(budget.minAmount)} - ${currencyFormat.format(budget.maxAmount)}"

                    // Get spending for this month to calculate progress
                    dashboardViewModel.getMonthlySpending(userId, currentMonth, currentYear)
                        .observe(viewLifecycleOwner) { spending ->
                            val amount = spending ?: 0.0
                            val maxBudget = budget.maxAmount

                            // Calculate percentage of budget used
                            val percentUsed = if (maxBudget > 0) (amount / maxBudget * 100).toInt() else 0

                            binding.budgetProgress.progress = percentUsed
                            binding.budgetStatus.text = "$percentUsed% of budget used (${currencyFormat.format(amount)})"
                        }
                } else {
                    // Try to get the most recent budget if current month doesn't exist
                    dashboardViewModel.getMostRecentBudget(userId).observe(viewLifecycleOwner) { recentBudget ->
                        if (recentBudget != null) {
                            binding.budgetTitle.text = "Budget for ${getMonthName(recentBudget.month)} ${recentBudget.year}"
                            binding.budgetAmount.text = "${currencyFormat.format(recentBudget.minAmount)} - ${currencyFormat.format(recentBudget.maxAmount)}"

                            // Get spending for this month
                            dashboardViewModel.getMonthlySpending(userId, recentBudget.month, recentBudget.year)
                                .observe(viewLifecycleOwner) { spending ->
                                    val amount = spending ?: 0.0
                                    val maxBudget = recentBudget.maxAmount

                                    val percentUsed = if (maxBudget > 0) (amount / maxBudget * 100).toInt() else 0

                                    binding.budgetProgress.progress = percentUsed
                                    binding.budgetStatus.text = "$percentUsed% of budget used (${currencyFormat.format(amount)})"
                                }
                        } else {
                            // No budget exists
                            binding.budgetTitle.text = "No Budget Set"
                            binding.budgetAmount.text = "Set a budget to track your expenses"
                            binding.budgetProgress.progress = 0
                            binding.budgetStatus.text = "No spending data available"
                        }
                    }
                }
            }
    }

    private fun getMonthName(month: Int): String {
        return when (month) {
            1 -> "January"
            2 -> "February"
            3 -> "March"
            4 -> "April"
            5 -> "May"
            6 -> "June"
            7 -> "July"
            8 -> "August"
            9 -> "September"
            10 -> "October"
            11 -> "November"
            12 -> "December"
            else -> "Unknown"
        }
    }

    private fun setupExpenseGraph(userId: Long) {
        // Get the chart view from layout
        val barChart = binding.expenseGraph as BarChart

        dashboardViewModel.getRecentExpenses(userId).observe(viewLifecycleOwner) { expenses ->
            // Group expenses by category
            val categoryMap = mutableMapOf<String, Double>()

            // Sum up expenses by category
            expenses.forEach { expense ->
                val category = expense.category ?: "Uncategorized"
                categoryMap[category] = (categoryMap[category] ?: 0.0) + expense.amount
            }

            // Prepare data entries for the chart
            val entries = mutableListOf<BarEntry>()
            val xAxisLabels = mutableListOf<String>()

            // Convert map to bar entries (limit to top 7 categories if needed)
            categoryMap.entries.sortedByDescending { it.value }.take(7).forEachIndexed { index, entry ->
                entries.add(BarEntry(index.toFloat(), entry.value.toFloat()))
                xAxisLabels.add(entry.key)
            }

            // Create the dataset
            val dataSet = BarDataSet(entries, "Expenses by Category")
            dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()

            // Create and set the data
            val barData = BarData(dataSet)
            barChart.data = barData

            // Set x-axis labels
            val xAxis = barChart.xAxis
            xAxis.valueFormatter = IndexAxisValueFormatter(xAxisLabels)
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.granularity = 1f
            xAxis.isGranularityEnabled = true

            // Allow labels to be displayed at an angle if they're long
            xAxis.labelRotationAngle = 45f

            // Customize the chart
            barChart.description.isEnabled = false
            barChart.legend.isEnabled = true
            barChart.setFitBars(true)
            barChart.animateY(1000)

            // Ensure there's enough bottom margin for the rotated labels
            barChart.extraBottomOffset = 10f

            // Update the chart
            barChart.invalidate()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
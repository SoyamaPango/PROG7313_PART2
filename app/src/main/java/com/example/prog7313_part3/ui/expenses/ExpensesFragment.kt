package com.example.prog7313_part3.ui.expenses

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prog7313_part3.ExpenseDetailActivity
import com.example.prog7313_part3.SessionManager
import com.example.prog7313_part3.databinding.FragmentExpensesBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ExpensesFragment : Fragment() {

    private var _binding: FragmentExpensesBinding? = null
    private val binding get() = _binding!!
    private lateinit var expensesViewModel: ExpensesViewModel
    private lateinit var adapter: ExpensesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExpensesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize ViewModel
        expensesViewModel = ViewModelProvider(this).get(ExpensesViewModel::class.java)

        // Setup RecyclerView with click handling
        adapter = ExpensesAdapter { expense ->
            // Handle item click - navigate to detail screen
            navigateToExpenseDetail(expense.id)
        }
        binding.recyclerViewExpenses.adapter = adapter
        binding.recyclerViewExpenses.layoutManager = LinearLayoutManager(requireContext())

        // Get current user ID
        val sessionManager = SessionManager(requireContext())
        val userId = sessionManager.getUserId().toLong()

        // Load expenses for current user
        expensesViewModel.getUserExpenses(userId).observe(viewLifecycleOwner) { expenses ->
            if (expenses.isEmpty()) {
                binding.emptyView.visibility = View.VISIBLE
                binding.recyclerViewExpenses.visibility = View.GONE
            } else {
                binding.emptyView.visibility = View.GONE
                binding.recyclerViewExpenses.visibility = View.VISIBLE
                adapter.submitList(expenses)
            }
        }
    }

    private fun navigateToExpenseDetail(expenseId: Long) {
        val intent = Intent(requireContext(), ExpenseDetailActivity::class.java).apply {
            putExtra(ExpenseDetailActivity.EXTRA_EXPENSE_ID, expenseId)
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
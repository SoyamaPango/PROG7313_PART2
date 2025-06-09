package com.example.prog7313_part3.ui.budget

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.prog7313_part3.AddBudgetActivity
import com.example.prog7313_part3.SessionManager
import com.example.prog7313_part3.databinding.FragmentBudgetBinding


class BudgetFragment : Fragment() {

    private var _binding: FragmentBudgetBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: BudgetViewModel
    private lateinit var adapter: BudgetAdapter
    private lateinit var sessionManager: SessionManager

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBudgetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionManager = SessionManager(requireContext())
        viewModel = ViewModelProvider(this).get(BudgetViewModel::class.java)

        setupRecyclerView()
        setupFab()
        loadBudgets()
    }

    private fun setupRecyclerView() {
        adapter = BudgetAdapter { }
        binding.recyclerViewBudgets.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = this@BudgetFragment.adapter
        }
    }

    private fun setupFab() {
        binding.fabAddBudget.setOnClickListener {
            val intent = Intent(requireContext(), AddBudgetActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadBudgets() {
        val userId = sessionManager.getUserId().toLong()
        viewModel.getBudgetsWithSpending(userId).observe(viewLifecycleOwner) { budgetsWithSpending ->
            if (budgetsWithSpending.isEmpty()) {
                binding.textNoBudgets.visibility = View.VISIBLE
                binding.recyclerViewBudgets.visibility = View.GONE
            } else {
                binding.textNoBudgets.visibility = View.GONE
                binding.recyclerViewBudgets.visibility = View.VISIBLE
                adapter.submitList(budgetsWithSpending)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
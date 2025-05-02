package vcmsa.projects.tcss.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vcmsa.projects.tcss.data.Expense
import vcmsa.projects.tcss.R


class ExpenseAdapter(private val expenseList: List<Expense>) :
    RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    class ExpenseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val amount: TextView = view.findViewById(R.id.txtAmount)
        val date: TextView = view.findViewById(R.id.txtDate)
        val category: TextView = view.findViewById(R.id.txtCategory)
        val description: TextView = view.findViewById(R.id.txtDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenseList[position]
        holder.amount.text = "R ${expense.amount}"
        holder.date.text = expense.date
        holder.category.text = expense.category
        holder.description.text = expense.description
    }

    override fun getItemCount() = expenseList.size
}
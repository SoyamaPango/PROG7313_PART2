
package vcmsa.projects.tcss.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import vcmsa.projects.tcss.R
import vcmsa.projects.tcss.models.Expense
import java.text.SimpleDateFormat
import java.util.*

class ExpenseAdapter(private val expenses: List<Expense>) :
    RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    class ExpenseViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtAmount: TextView = view.findViewById(R.id.txtAmount)
        val txtCategory: TextView = view.findViewById(R.id.txtCategory)
        val txtDate: TextView = view.findViewById(R.id.txtDate)
        val txtDescription: TextView = view.findViewById(R.id.txtDescription)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        holder.txtAmount.text = "R${expense.amount}"
        holder.txtCategory.text = expense.category
        holder.txtDescription.text = expense.description
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        holder.txtDate.text = sdf.format(Date(expense.date))
    }

    override fun getItemCount() = expenses.size
}

package vcmsa.projects.tcss

import android.os.Bundle
import android.widget.EditText
import android.content.Intent
import android.icu.text.DecimalFormat
import android.widget.Button
import androidx.activity.ComponentActivity

class TransactionPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_page)

        val Savebtn = findViewById<Button>(R.id.btnSave)
        Savebtn.setOnClickListener {
            onSaveButtonClick()
        }
        btnCancel.setOnClickListener {
            val CancelIntent = Intent(this, DashboardActivity::class.java)
            startActivity(CancelIntent)
        }
    }

    private fun onSaveButtonClick() {
        val amountEditText = findViewById<EditText>(R.id.edtAmount)
        val amountString = amountEditText.text.toString()

        val amount: Float? = amountString.toFloatOrNull()

        if (amount != null) {
            // The string was successfully parsed to a Float
            // You can now use the 'amount' variable (which is a Float)
        } else {
            // The string could not be parsed to a Float
            // Handle the error
        }
    }
}

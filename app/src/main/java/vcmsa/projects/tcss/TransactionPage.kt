
package vcmsa.projects.tcss

import android.os.Bundle
import android.widget.EditText
import android.content.Intent
import android.icu.text.DecimalFormat
import android.widget.Button
import androidx.activity.ComponentActivity
import java.util.Date

class TransactionPage : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_page)

        val Savebtn = findViewById<Button>(R.id.btnSave)
        Savebtn.setOnClickListener {
            onSaveButtonClick()
        }

        val btnCancel = findViewById<Button>(R.id.btnCancel)
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
            //store to database
        } else {
            // Handle the error
        }
        val Category: String = findViewById<EditText>(R.id.edtCategory).text.toString()
        val Date: Date = findViewById<EditText>(R.id.edtDate).text.toString()
        val PaymentType: String = findViewById<EditText>(R.id.edtPaymentMethod).text.toString()
        val Description: String = findViewById<EditText>(R.id.edtDescription).text.toString()

    }
}


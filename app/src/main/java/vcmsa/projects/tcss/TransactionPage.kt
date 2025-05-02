package vcmsa.projects.tcss

import android.os.Bundle
import android.widget.EditText
import android.content.Intent
import android.icu.text.DecimalFormat
import android.widget.Button
import android.net.Uri
import android.util.Log
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.ComponentActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import vcmsa.projects.tcss.data.Transaction
import vcmsa.projects.tcss.data.TransactionDao
import vcmsa.projects.tcss.data.UserDAO
import vcmsa.projects.tcss.data.UserEntity
import java.util.Date

class TransactionPage : ComponentActivity() {
    private var selectedImageUri: Uri? = null
    private lateinit var db: AppDatabase
    private lateinit var transactionDao: TransactionDao

    //  Register the activity result contract for picking media
    private val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            // Callback is invoked after the user selects a media item or closes the
            // photo picker.
            if (uri != null) {
                selectedImageUri = uri
                // You now have the URI of the selected image.
                // You can display a preview or proceed with uploading.
                // Example: displayPreview(uri)
            } else {
                // User cancelled the picker
                // Handle this case (optional)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction_page)

        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "transactions.db")
            .build()
        transactionDao = db.transactionDAO()

        val btnUpload = findViewById<Button>(R.id.btnUpload)
        btnUpload.setOnClickListener {
            selectImage()
        }

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
        val amountEditText = findViewById<EditText>(R.id.edtAmount).text.toString()
        val amount: Double = amountEditText.toDouble()
        val Category: String = findViewById<EditText>(R.id.edtCategory).text.toString()
        val Date: String = findViewById<EditText>(R.id.edtDate).text.toString()
        val PaymentType: String = findViewById<EditText>(R.id.edtPaymentMethod).text.toString()
        val Description: String = findViewById<EditText>(R.id.edtDescription).text.toString()

        if (amount <= 0) {
            lifecycleScope.launch(Dispatchers.IO) {
                val newTransaction = Transaction(0, amount, Date, Description, Category)
                transactionDao.insertTransaction(newTransaction)

                launch(Dispatchers.Main) {
                    showToast("Transaction added successfully!")
                    GoToDashboard()
                }

            }
        } else {
            // Handle the error
            showToast("Enter valid Amount")
        }

    }

    private fun GoToDashboard() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
    }

    private fun selectImage() {
        // Launch the photo picker and let the user choose an image.
        pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }
}


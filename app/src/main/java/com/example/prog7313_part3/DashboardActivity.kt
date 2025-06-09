package com.example.prog7313_part3

import android.os.Bundle
import android.view.Menu
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.prog7313_part3.databinding.ActivityDashboardBinding
import android.widget.Toast
import android.widget.TextView
import android.content.Intent
import android.widget.ImageView
import androidx.lifecycle.lifecycleScope
import android.widget.PopupMenu
import com.example.prog7313_part3.repositories.UserRepository
import android.graphics.BitmapFactory
import kotlinx.coroutines.launch


class DashboardActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityDashboardBinding
    private lateinit var userRepository: UserRepository
    private lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
        sessionManager = SessionManager(applicationContext)
        val appDatabase = AppDatabase.getDatabase(applicationContext)
        userRepository = UserRepository(appDatabase.userDao())

        binding.appBarMain.fab.setOnClickListener { view ->
            // Create popup menu
            val popupMenu = PopupMenu(this, view)
            popupMenu.menuInflater.inflate(R.menu.menu_add_options, popupMenu.menu)

            // Set click listener for menu items
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu_add_budget -> {
                        // Launch add budget activity
                        val intent = Intent(this, AddBudgetActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.menu_add_expense -> {
                        // Launch add expense activity
                        val intent = Intent(this, AddExpenseActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    else -> false
                }
            }

            // Show the popup menu
            popupMenu.show()
        }
        
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_dashboard, R.id.nav_monthly_budget, R.id.nav_expenses
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        navView.menu.findItem(R.id.nav_logout).setOnMenuItemClickListener {
            // Handle logout click
            logoutUser()
            true
        }

        // Get user information
        val userId = sessionManager.getUserId()
        val userEmail = sessionManager.getUserEmail()
        val userName = sessionManager.getUserName()

        // Update navigation header with user email
        val headerView = navView.getHeaderView(0)
        val txtEmail = headerView.findViewById<TextView>(R.id.txtEmail)
        val txtName = headerView.findViewById<TextView>(R.id.txtUsername)
        val imgUser = headerView.findViewById<ImageView>(R.id.imgUser)

        // Set the email
        txtName.text = userName ?: getString(R.string.nav_header_title)
        txtEmail.text = userEmail ?: getString(R.string.nav_header_subtitle)
        setUserIcon(imgUser)


        if (userId != -1) {
            Toast.makeText(
                this,
                "Welcome $userName!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun logoutUser() {
        sessionManager.clearSession()

        // Show logout message
        Toast.makeText(
            this,
            "Logged out",
            Toast.LENGTH_SHORT
        ).show()

        // Redirect to intro screen
        val intent = Intent(this, IntroActivity::class.java)
        // Clear back stack so user can't go back to Dashboard after logout
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun setUserIcon(img: ImageView) {
        lifecycleScope.launch {
            try {
                val userId = sessionManager.getUserId()
                if (userId != -1) {
                    val user = userRepository.getUserById(userId)

                    user?.profilePicturePath?.let { path ->
                        // Load image from file path
                        if (path.isNotEmpty()) {
                            val bitmap = BitmapFactory.decodeFile(path)
                            img.setImageBitmap(bitmap)
                        }
                    }
                }
            } catch (e: Exception) {

            }
        }
    }
}
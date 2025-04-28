package com.example.tamaleshr.ui

import android.os.Bundle
import android.view.Menu
import android.widget.TextView
import androidx.activity.viewModels
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.tamaleshr.R
import com.example.tamaleshr.databinding.ActivityMainBinding
import com.example.tamaleshr.service.employee.Employee
import com.example.tamaleshr.usecase.DefaultError

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> { MainViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_profile, R.id.nav_salary
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onStart() {
        super.onStart()
        viewModel.uiResultLiveData.observe(this){ state ->
            when(state){
                is BaseUiState.Error<Employee, DefaultError> -> Unit // Opt out
                is BaseUiState.Loading<Employee, DefaultError> -> Unit // Opt out
                is BaseUiState.Success<Employee, DefaultError> -> {
                    binding.drawerLayout.apply {
                        findViewById<TextView>(R.id.tvUserInitials).text = state.data?.initials()
                        findViewById<TextView>(R.id.tvEmployeeNo).text = context.getString(R.string.home_emp_no, state.data?.emp_no?.toString())
                        findViewById<TextView>(R.id.tvUserFullName).text = state.data?.fullName()
                    }
                }
            }
        }

        // Fetch employee data
        viewModel.fetchEmployee()
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
}
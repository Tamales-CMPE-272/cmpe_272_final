package com.example.tamaleshr.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import com.example.tamaleshr.R
import com.example.tamaleshr.databinding.ActivityMainBinding
import com.example.tamaleshr.di.koin
import com.example.tamaleshr.service.auth.JwtTokenPayload
import com.example.tamaleshr.service.auth.Role
import com.example.tamaleshr.service.employee.Employee
import com.example.tamaleshr.ui.auth.AuthActivity
import com.example.tamaleshr.usecase.DefaultError
import com.example.tamaleshr.util.AuthTokenManager

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, MainActivity::class.java)
        }
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel> { MainViewModel.Factory }
    private val navController: NavController
        get() = findNavController(R.id.nav_host_fragment_content_main)
    private val authTokenManager: AuthTokenManager
        get() = koin.get<AuthTokenManager>()
    private val jwtTokenPayload: JwtTokenPayload?
        get() = authTokenManager.decodeJwtPayloadToModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home,
                R.id.nav_profile,
                R.id.nav_salary,
                R.id.nav_department,
                R.id.nav_logout
            ), binding.drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        binding.navView.setNavigationItemSelectedListener(this)
        binding.navView.menu.findItem(R.id.nav_department)?.isVisible = jwtTokenPayload?.role == Role.MANAGER

        // Create an OnBackPressedCallback
        val onBackPressedCallback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                // Your custom back press handling logic here
                if (!navController.popBackStack()) {
                    // If NavController can't handle it, fall back to default behavior
                    finish()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    override fun onStart() {
        super.onStart()
        viewModel.uiResultLiveData.observe(this){ state ->
            when(state){
                is BaseUiState.Error<Employee, DefaultError> -> Unit // Opt out
                is BaseUiState.Loading<Employee, DefaultError> -> Unit // Opt out
                is BaseUiState.Success<Employee, DefaultError> -> {
                    binding.drawerLayout.apply {
                        findViewById<TextView>(R.id.tvUserInitials)?.text = state.data?.initials()
                        findViewById<TextView>(R.id.tvEmployeeNo)?.text = context.getString(R.string.home_emp_no, state.data?.emp_no?.toString())
                        findViewById<TextView>(R.id.tvUserFullName)?.text = state.data?.fullName()
                    }
                }
            }
        }
        viewModel.fetchEmployee()
    }

    override fun onResume() {
        super.onResume()
        if(!authTokenManager.isTokenValid()){
            logout()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                navController.navigate(R.id.nav_home)
            }
            R.id.nav_profile -> {
                navController.navigate(R.id.nav_profile)
            }
            R.id.nav_salary -> {
                navController.navigate(R.id.nav_salary)
            }
            R.id.nav_department -> {
                navController.navigate(R.id.nav_department)
            }
            R.id.nav_logout -> {
                logout()
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun logout() {
        authTokenManager.clear()
        routeToAuth()
    }

    private fun routeToAuth() {
        val intent = AuthActivity.newIntent(this).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }
}
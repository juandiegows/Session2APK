package com.example.session2apk

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.navigation.Navigation
import com.example.session2apk.Helper.Singleton
import com.example.session2apk.databinding.ActivityMainBinding
import com.example.session2apk.databinding.NavHeaderMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    override fun onBackPressed() {
        Toast.makeText(this, "not permitted to go back", Toast.LENGTH_LONG).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemLoginOut -> {
                startActivity(Intent(this, LoginActivity::class.java))
            }
            R.id.additemMenu->{
                Singleton.statusRegister = Singleton.STAT.ADD
               Navigation.findNavController(this,R.id.nav_host_fragment_content_main).navigate(R.id.registerFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.listUserFragment, R.id.aboutFragment, R.id.seleccionFragment, R.id.registerFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        var binding2: NavHeaderMainBinding = NavHeaderMainBinding.bind(navView.getHeaderView(0))
        binding2.txtName.setText("${Singleton.userLogin.Nome} ${Singleton.userLogin.Apelido}")
        binding2.txtEmailNav.setText(Singleton.userLogin.Email)
       
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        println(Singleton.userLogin.Perfil)
        if (Singleton.userLogin.Perfil.lowercase(Locale.ROOT) != "Administrador".lowercase(Locale.ROOT))
            menu.findItem(R.id.additemMenu).isVisible = false
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
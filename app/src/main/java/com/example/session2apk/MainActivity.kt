package com.example.session2apk

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.session2apk.Helper.Singleton
import com.example.session2apk.databinding.ActivityMainBinding
import com.example.session2apk.databinding.NavHeaderMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onBackPressed() {
        this.supportFragmentManager.fragments.forEach {
            if (Singleton.statusRegister != Singleton.STAT.REGISTER) {
                super.onBackPressed()
                return
            }
        }

        Toast.makeText(this, "not permited to go back", Toast.LENGTH_LONG).show()
    }

    fun setTitulo(title: String) {
        binding.appBarMain.toolbar.setTitle(title)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemLoginOut -> {
                AlertDialog.Builder(this)
                    .setTitle("Question")
                    .setMessage("Are you sure you want  to exit? (logout)")
                    .setPositiveButton("Yes, I sure") { _, _ ->

                        startActivity(Intent(this, LoginActivity::class.java))

                    }
                    .setNegativeButton("NO") { _, _ -> }
                    .create().show()

            }
            R.id.additemMenu -> {
                Singleton.statusRegister = Singleton.STAT.ADD
                toGoUpdateAddUser()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)


        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        if (Singleton.userLogin.Perfil.lowercase(Locale.ROOT) != "Administrador".lowercase(Locale.ROOT)){
           navView.menu.findItem(R.id.listUserFragment).isVisible = false

        }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.listUserFragment,
                R.id.aboutFragment,
                R.id.seleccionFragment,
                R.id.homeFragment
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        if (Singleton.userLogin.Perfil.lowercase() == "Administrador".lowercase())
            Navigation.findNavController(this, R.id.nav_host_fragment_content_main)
                .navigate(R.id.listUserFragment)
        var binding2: NavHeaderMainBinding = NavHeaderMainBinding.bind(navView.getHeaderView(0))
        binding2.txtName.setText("${Singleton.userLogin.Nome} ${Singleton.userLogin.Apelido}")
        binding2.txtEmailNav.setText(Singleton.userLogin.Email)
        binding2.txtName.setOnClickListener {
            toGoUpdateUser()
            drawerLayout.closeDrawers()
        }
        binding2.txtEmailNav.setOnClickListener {
            toGoUpdateUser()
            drawerLayout.closeDrawers()
        }
        binding2.imageView.setOnClickListener {
            toGoUpdateUser()
            drawerLayout.closeDrawers()
        }
    }

    private fun toGoUpdateUser() {
        with(Singleton) {
            userLoginEdit = userLogin
            statusRegister = Singleton.STAT.UPDATE
        }

        toGoUpdateAddUser()
    }

    private fun toGoUpdateAddUser() {
        Navigation.findNavController(this, R.id.nav_host_fragment_content_main)
            .navigate(R.id.registerFragment)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        println(Singleton.userLogin.Perfil)
        if (Singleton.userLogin.Perfil.lowercase(Locale.ROOT) != "Administrador".lowercase(Locale.ROOT)){
            menu.findItem(R.id.additemMenu).isVisible = false

        }

        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}
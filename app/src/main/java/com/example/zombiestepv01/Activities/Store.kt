package com.example.zombiestepv01.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.zombiestepv01.Model.BEUser
import com.example.zombiestepv01.R
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main_window.*
import kotlinx.android.synthetic.main.activity_store.*
import kotlinx.android.synthetic.main.activity_store.drawerLayout
import kotlinx.android.synthetic.main.activity_store.imageButton
import kotlinx.android.synthetic.main.content_main.*

class Store : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    private lateinit var user : BEUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store)
        var extras: Bundle = intent.extras!!
        user = extras.getSerializable("loggedUser") as BEUser
        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.open,R.string.close)
        toggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        imageButton.setOnClickListener{v-> drawerLayout.open()}
        var navigationView : NavigationView = findViewById(R.id.nav_menu)
        val headerView = navigationView.getHeaderView(0)
        val txt_stepcoins = headerView.findViewById(R.id.stepcoins) as TextView
        txt_stepcoins.text = "Stepcoins: ${user.stepCoins}"
        navigationView.setNavigationItemSelectedListener(this)

    }

    override fun onBackPressed() {

    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean = when (item.itemId){
        R.id.main_window-> {
            val intent = Intent(this, MainWindow::class.java)
            intent.putExtra("loggedUser",user)
            startActivity(intent)
            //supportFragmentManager.beginTransaction().replace(R.id.drawerLayout, storeFrg).commit()
            drawerLayout.close()
            finish()
            // User chose the "Settings" item, show the app settings UI...
            true
        }
        else -> {
            true
        }
    }

}
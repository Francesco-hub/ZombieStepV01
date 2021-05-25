package com.example.zombiestepv01.Activities

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.example.zombiestepv01.Model.BEUser
import com.example.zombiestepv01.R
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main_window.*
import kotlinx.android.synthetic.main.content_main.*


class MainWindow : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var user : BEUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_window)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.open,R.string.close)
        toggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        imageButton.setOnClickListener{v-> drawerLayout.open()}
        btn_startWalking.setOnClickListener{v-> openWalkActivity()}
        btn_viewBase.setOnClickListener{v-> openViewBaseActivity()}
        var extras: Bundle = intent.extras!!
        user = extras.getSerializable("loggedUser") as BEUser
        txt_welcome.text = "Welcome back ${user.name}"
        txt_lvlBase.text = "${user.fortressLvl}"
        txt_lvlWall.text = "${user.wallLvl}"
        txt_lvlWeapons.text = "${user.weaponsLvl}"

        var navigationView : NavigationView = findViewById(R.id.nav_menu)
        val headerView = navigationView.getHeaderView(0)
        val txt_stepcoins = headerView.findViewById(R.id.stepcoins) as TextView
        txt_stepcoins.text = "Stepcoins: ${user.stepCoins}"
        navigationView.setNavigationItemSelectedListener(this)

    }

    private fun openViewBaseActivity() {

    }

    private fun openWalkActivity() {
        val intent = Intent(this, StepCounterActivity::class.java)
        intent.putExtra("loggedUser", user)
        startActivity(intent)
        drawerLayout.close()
        finish()
    }

    override fun onBackPressed() {
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean = when (item.itemId){
        R.id.activity_store -> {
            val intent = Intent(this, Store::class.java)
            intent.putExtra("loggedUser", user)
            startActivity(intent)
            drawerLayout.close()
            finish()
            true
        }
        else -> {
            true
        }
    }
}
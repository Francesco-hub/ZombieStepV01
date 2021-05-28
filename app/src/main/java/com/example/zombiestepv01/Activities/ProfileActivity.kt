package com.example.zombiestepv01.Activities

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import com.example.zombiestepv01.Data.IUserDao
import com.example.zombiestepv01.Data.UserDao_Impl
import com.example.zombiestepv01.Model.BEUser
import com.example.zombiestepv01.R
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_store.drawerLayout
import kotlinx.android.synthetic.main.activity_store.imageButton
import kotlinx.android.synthetic.main.content_main.*
import java.io.File

class ProfileActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private lateinit var user : BEUser
    private val REQUEST_CODE = 1
    private lateinit var userRepo: IUserDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)


        val toggle = ActionBarDrawerToggle(this, drawerLayout,toolbar,R.string.open,R.string.close)
        toggle.isDrawerIndicatorEnabled = true
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        imageButton.setOnClickListener{v-> drawerLayout.open()}
        setSupportActionBar(toolbar)
        var extras: Bundle = intent.extras!!
        user = extras.getSerializable("loggedUser") as BEUser
        var navigationView : NavigationView = findViewById(R.id.nav_menu)
        val headerView = navigationView.getHeaderView(0)
        val txt_stepcoins = headerView.findViewById(R.id.stepcoins) as TextView
        txt_stepcoins.text = "Stepcoins: ${user.stepCoins}"
        navigationView.setNavigationItemSelectedListener(this)
        txt_pName.text = user.name
        txt_pMultiplier.text = "Multiplier:    " + "x" + user.multiplier.toString()
        txt_pTotalSteps.text = "Total Steps:    " + user.totalSteps
        var km = 0.0
        km = (user.totalSteps/1400).toDouble()
        btn_shareSteps.setOnClickListener{v -> onClickShare()}
        txt_pKm.text = "Total Distance:    $km  Km"
        btn_pPicture.setImageDrawable(Drawable.createFromPath(user.Picture?.absolutePath))
        btn_pPicture.setOnClickListener { v -> openCamera() }
        userRepo = UserDao_Impl(this)
    }

    private fun onClickShare() {
        val sms_uri = Uri.parse("smsto:")
        val sms_intent = Intent(Intent.ACTION_SENDTO, sms_uri)
        //val intent = Intent(Intent.ACTION_VIEW)
        sms_intent.putExtra("sms_body", "Hey, I have already made ${user.totalSteps} steps in ZombieStep, and my fortress is already at level ${user.fortressLvl}. What about you? Are you ready for the apocalypse?")
        startActivity(sms_intent)
    }

    private fun openCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE)
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
        R.id.main_window-> {
            val intent = Intent(this, MainWindow::class.java)
            intent.putExtra("loggedUser",user)
            startActivity(intent)
            drawerLayout.close()
            finish()
            true
        }
        R.id.activity_logIn -> {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            drawerLayout.close()
            finish()
            true
        }
        else -> {
            true
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { //Method that will check that the CameraActivity will return a picture and assign it to our friend as well as display it in our DetailsActivity
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                var newPicture = data?.extras?.getSerializable("newPicture") as File
                if (newPicture != null) {
                    btn_pPicture.setImageDrawable(Drawable.createFromPath(newPicture?.absolutePath))
                    user.Picture = newPicture
                    userRepo.updateUser(user)
                }
            }
        }
    }
}
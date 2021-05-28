package com.example.zombiestepv01.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isInvisible
import androidx.core.view.isVisible
import com.example.zombiestepv01.Model.BEUser
import com.example.zombiestepv01.R
import kotlinx.android.synthetic.main.activity_main_window.*
import kotlinx.android.synthetic.main.activity_view_base.*

class viewBaseActivity : AppCompatActivity() {
    private lateinit var user : BEUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_base)
        var extras: Bundle = intent.extras!!
        user = extras.getSerializable("loggedUser") as BEUser
        hideAllPictures()
        showRelevantPictures()
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainWindow::class.java)
        intent.putExtra("loggedUser", user)
        startActivity(intent)
        finish()
    }

    private fun hideAllPictures() {
        img_sv1.isVisible = false
        img_sv2.isVisible = false
        img_sv3.isVisible = false
        img_sv4.isVisible = false
        img_sv5.isVisible = false
        img_sv6.isVisible = false
        img_wl1.isVisible = false
        img_wl2.isVisible = false
        img_wl3.isVisible = false
        img_wl4.isVisible = false
        img_wl5.isVisible = false
        img_wl6.isVisible = false
        img_wp1.isVisible = false
        img_wp2.isVisible = false
        img_wp3.isVisible = false
        img_wp4.isVisible = false
        img_wp5.isVisible = false
        img_wp6.isVisible = false
    }

    private fun showRelevantPictures() {
        if(user.fortressLvl==0) img_base.setImageResource(R.drawable.bs0)
        else if(user.fortressLvl==1) img_base.setImageResource(R.drawable.bs1)
        else if(user.fortressLvl==2) img_base.setImageResource(R.drawable.bs2)
        else if(user.fortressLvl==3) img_base.setImageResource(R.drawable.bs3)
        else if(user.fortressLvl==4) img_base.setImageResource(R.drawable.bs4)
        else if(user.fortressLvl==5) img_base.setImageResource(R.drawable.bs5)
        else{ img_base.setImageResource(R.drawable.bs6)}
        setSurvivorPictures()
        setWeaponsPictures()
        setWallPictures()
    }

    private fun setWallPictures() {
        if(user.wallLvl>=1) img_wl1.isVisible = true
        if(user.wallLvl>=2) img_wl2.isVisible = true
        if(user.wallLvl>=3) img_wl3.isVisible = true
        if(user.wallLvl>=4) img_wl4.isVisible = true
        if(user.wallLvl>=5) img_wl5.isVisible = true
        if(user.wallLvl>=6) img_wl6.isVisible = true
    }

    private fun setWeaponsPictures() {
        if(user.weaponsLvl>=1) img_wp1.isVisible = true
        if(user.weaponsLvl>=2) img_wp2.isVisible = true
        if(user.weaponsLvl>=3) img_wp3.isVisible = true
        if(user.weaponsLvl>=4) img_wp4.isVisible = true
        if(user.weaponsLvl>=5) img_wp5.isVisible = true
        if(user.weaponsLvl>=6) img_wp6.isVisible = true
    }

    private fun setSurvivorPictures() {
        if(user.multiplier>=1.01) img_sv1.isVisible = true
        if(user.multiplier>1.49) img_sv2.isVisible = true
        if(user.multiplier>1.74) img_sv3.isVisible = true
        if(user.multiplier>1.99) img_sv4.isVisible = true
        if(user.multiplier>2.24) img_sv5.isVisible = true
        if(user.multiplier>2.49) img_sv6.isVisible = true
    }

}
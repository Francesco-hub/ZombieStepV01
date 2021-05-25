package com.example.zombiestepv01.Activities

import android.Manifest
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.zombiestepv01.Model.BEUser
import com.example.zombiestepv01.R
import com.example.zombiestepv01.stepCounter.MainFragment
import com.example.zombiestepv01.stepCounter.SharedPreferencesManager
import kotlinx.android.synthetic.main.activity_step_counter.*

class StepCounterActivity : AppCompatActivity() {

    private lateinit var user : BEUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_counter)
        var extras: Bundle = intent.extras!!
        user = extras.getSerializable("loggedUser") as BEUser
        if (savedInstanceState == null) {
            ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow()

        }
    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACTIVITY_RECOGNITION)
    }
    }


package com.example.zombiestepv01.Activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProviders
import com.example.zombiestepv01.Data.IUserDao
import com.example.zombiestepv01.Data.UserDao_Impl
import com.example.zombiestepv01.Model.BEUser
import com.example.zombiestepv01.R
import com.example.zombiestepv01.stepCounter.AppUtils
import com.example.zombiestepv01.stepCounter.MainViewModel
import com.example.zombiestepv01.stepCounter.SharedPreferencesManager
import kotlinx.android.synthetic.main.activity_step_counter.*
import java.util.*



class StepCounterActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var user : BEUser
    private lateinit var userRepo: IUserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_step_counter)
        var extras: Bundle = intent.extras!!
        user = extras.getSerializable("loggedUser") as BEUser
        userRepo = UserDao_Impl(this)
        if (savedInstanceState == null) {
            ActivityCompat.requestPermissions(
                    this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
        sensorManager = this.getSystemService(Context.SENSOR_SERVICE) as SensorManager

        // load previous status if any
        startDate = SharedPreferencesManager.instance.loadStartDate()
        counterSteps = SharedPreferencesManager.instance.loadInitialStepCount()


        if (startDate != null) {
            isStarted = true
        }

        initUI()
        bindUI()

        }
    companion object {
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.ACTIVITY_RECOGNITION)
    }
    private var isStarted = false
    private var sensorManager: SensorManager? = null
    private var stepCounter = 0
    private var counterSteps = 0
    private var stepDetector = 0
    private var startDate: Date? = null



    private lateinit var viewModel: MainViewModel


    override fun onResume() {
        super.onResume()
        if (this.isStarted) {
            startService()
        }
    }

    override fun onPause() {
        super.onPause()
        stopService()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    override fun onSensorChanged(event: SensorEvent) {
        when (event.sensor.type) {
            Sensor.TYPE_STEP_DETECTOR -> {
                stepDetector++
            }
            Sensor.TYPE_STEP_COUNTER -> {
                if (counterSteps < 1) {
                    counterSteps = event.values[0].toInt()
                    SharedPreferencesManager.instance.saveInitialStepCount(counterSteps)
                }
                stepCounter = event.values[0].toInt() - counterSteps
            }
        }
        txt_stepCount.text = "$stepCounter"
        var generatedStepcoins = (stepCounter * user.multiplier).toInt()
        txt_stepcoinCount.text = "$generatedStepcoins"
    }

    private fun initUI() {
        startDate?.let {
            txt_starTime.text = getString(R.string.start_time, AppUtils.getFormattedDate(it))
        } ?: run {
            txt_starTime.text = getString(R.string.start_time, "-")
        }
        txt_stepCount.text = "$stepCounter"
        var generatedStepcoins = (stepCounter * user.multiplier).toInt()
        txt_stepcoinCount.text = "$generatedStepcoins"

        if (this.isStarted) {
            btn_start.text = getString(R.string.stop)
        } else {
            btn_start.text = getString(R.string.start)
        }
    }

    private fun bindUI() {
        btn_start.setOnClickListener {
            if (this.isStarted) {
                stopCounter()
                btn_start.text = getString(R.string.start)
            } else {
                startCounter()
                btn_start.text = getString(R.string.stop)
            }
        }
    }

    private fun startService() {
        val stepsSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if (stepsSensor == null) {
        } else {
            sensorManager?.registerListener(this, stepsSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    private fun stopService() {
        sensorManager?.unregisterListener(this)

    }

    override fun onBackPressed() {
        val intent = Intent(this, MainWindow::class.java)
        intent.putExtra("loggedUser", user)
        startActivity(intent)
        finish()
    }

    private fun startCounter() {
        isStarted = true
        this.stepCounter = 0
        this.stepDetector = 0
        this.counterSteps = 0

        // reset UI
        initUI()

        startDate = Date()
        startDate?.let {
            txt_starTime.text =
                    getString(R.string.start_time, AppUtils.getFormattedDate(it))
            SharedPreferencesManager.instance.saveStartDate(it)
        }

        startService()
    }

    private fun stopCounter() {
        isStarted = false
        stopService()
        SharedPreferencesManager.instance.clear()
        RegisterWorkout()
        val intent = Intent(this, MainWindow::class.java)
        intent.putExtra("loggedUser", user)
        startActivity(intent)
        finish()
    }

    private fun RegisterWorkout() {
        user.stepCoins += (stepCounter * user.multiplier).toInt()
        user.totalSteps += stepCounter
        userRepo.updateUser(user)
        user = userRepo.getUserById(user.id)
    }
}


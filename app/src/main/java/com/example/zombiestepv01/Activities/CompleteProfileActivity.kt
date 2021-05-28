package com.example.zombiestepv01.Activities

import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.example.zombiestepv01.Data.IStoreDao
import com.example.zombiestepv01.Data.IUserDao
import com.example.zombiestepv01.Data.StoreDao_Impl
import com.example.zombiestepv01.Data.UserDao_Impl
import com.example.zombiestepv01.Model.BEUser
import com.example.zombiestepv01.R
import kotlinx.android.synthetic.main.activity_complete_profile.*
import java.io.File

class CompleteProfileActivity : AppCompatActivity() {

    private lateinit var userRepo: IUserDao
    private lateinit var storeRepo: IStoreDao
    private lateinit var user : BEUser
    private var userPicture: File? = null
    private val REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete_profile)
        userRepo = UserDao_Impl(this)
        storeRepo = StoreDao_Impl(this)
        txt_errorPicture.isVisible = false
        var extras: Bundle = intent.extras!!
        user = extras.getSerializable("loggedUser") as BEUser
        btn_complete.setOnClickListener{v -> completeProfile()}
        btn_openCamera.setOnClickListener{v -> openCamera()}
    }

    private fun openCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        startActivityForResult(intent, REQUEST_CODE)
    }

    private fun completeProfile() {
        if(validateData()){
            user.name = fld_name.text.toString()
            user.Picture = userPicture
            user.stepCoins = 0
            userRepo.updateUser(user)
            storeRepo.initializeStore(user.id)
            val intent = Intent(this, MainWindow::class.java)
            intent.putExtra("loggedUser", user)
            startActivity(intent)
        }
    }

    private fun validateData(): Boolean {
        if(fld_name.text.isNullOrBlank() || userPicture == null) {
            showMissingInfo()
            return false
        }
        return true
    }

    private fun showMissingInfo() {
        if(fld_name.text.isNullOrBlank()) fld_name.error = "Missing name"
        txt_errorPicture.isVisible = userPicture == null
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) { //Method that will check that the CameraActivity will return a picture and assign it to our friend as well as display it in our DetailsActivity
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                var newPicture = data?.extras?.getSerializable("newPicture") as File
                if (newPicture != null) {
                    btn_openCamera.setImageDrawable(Drawable.createFromPath(newPicture?.absolutePath))
                    userPicture = newPicture
                }
            }
        }
    }
}
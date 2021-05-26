package com.example.zombiestepv01.Activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import com.example.zombiestepv01.Data.IStoreDao
import com.example.zombiestepv01.Data.IUserDao
import com.example.zombiestepv01.Data.StoreDao_Impl
import com.example.zombiestepv01.Data.UserDao_Impl
import com.example.zombiestepv01.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var userRepo: IUserDao
    private lateinit var storeRepo: IStoreDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        userRepo = UserDao_Impl(this)
        storeRepo = StoreDao_Impl(this)
        userRepo.restartDb()
        storeRepo.restartDb()
        txt_noUserFound.isVisible = false
        btn_attemptLogIn.setOnClickListener{ v -> attemptLogin()}
        btn_ActivitySignUp.setOnClickListener{ v -> openSignUpActivity()}
        fld_email.setText("W@.")
        fld_password.setText("1")
        //fld_email.setOnClickListener{v -> txt_noUserFound.isVisible = false}
        //fld_password.setOnClickListener{v -> txt_noUserFound.isVisible = false}
    }

    private fun openSignUpActivity() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
    }

    private fun attemptLogin() {
        if(validateData()){
            var loggedUser = userRepo.login(fld_email.text.toString(),fld_password.text.toString())
            if(loggedUser.id == 0){
                txt_noUserFound.isVisible =  true
            }
            else if(loggedUser.name == ""){
                val intent = Intent(this, CompleteProfileActivity::class.java)
                intent.putExtra("loggedUser", loggedUser)
                startActivity(intent)
            }
            else{
                val intent = Intent(this, MainWindow::class.java)
                intent.putExtra("loggedUser", loggedUser)
                startActivity(intent)
            }
        }
    }

    private fun validateData(): Boolean {
        var dataValidated = true
        var validatedEmail = true
        var validatedPassword = true
        if(fld_email.text.isNullOrBlank()) validatedEmail = false
        if(fld_password.text.isNullOrBlank()) validatedPassword = false
        if(fld_email.text.contains("'")) validatedEmail = false
        if(fld_password.text.contains("'")) validatedPassword = false
        if(!fld_email.text.contains("@")) validatedEmail = false
        if(!fld_email.text.contains(".")) validatedEmail = false
        if(fld_email.text.contains(";")) validatedEmail = false
        if(!validatedPassword) fld_password.setError("Invalid Password")
        if(!validatedEmail) fld_email.setError("Invalid Email")
        if(!validatedEmail || !validatedPassword) dataValidated = false
        return dataValidated
    }
}
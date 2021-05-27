package com.example.zombiestepv01.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import com.example.zombiestepv01.Data.IUserDao
import com.example.zombiestepv01.Data.UserDao_Impl
import com.example.zombiestepv01.Model.BEUser
import com.example.zombiestepv01.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.fld_email
import kotlinx.android.synthetic.main.activity_sign_up.fld_password

class SignUpActivity : AppCompatActivity() {

    private lateinit var userRepo: IUserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        userRepo = UserDao_Impl(this)
        txt_errorUserExists.isVisible = false

        btn_startWalking.setOnClickListener { v -> attemptSignUp() }
    }

    private fun attemptSignUp() {
        if(validateData()){
            var newUser = BEUser(0,"",fld_email.text.toString(),fld_password.text.toString(), 0,0,1.0,1,0,0,null)
            if(!userAlreadyExists()) {
                userRepo.createNewUser(newUser)
                var loggedUser = userRepo.login(fld_email.text.toString(),fld_password.text.toString())
                val intent = Intent(this, CompleteProfileActivity::class.java)
                intent.putExtra("loggedUser", loggedUser)
                startActivity(intent)
                finish()
            }
            else{
                txt_errorUserExists.isVisible = true
            }
        }
    }

    private fun userAlreadyExists(): Boolean {
    return userRepo.checkIfUserExists(fld_email.text.toString())
    }

    private fun validateData(): Boolean {
        var dataValidated = true
        var validatedEmail = true
        var validatedPassword = true
        var validateConfirmPassword = true
        if(fld_email.text.isNullOrBlank()) validatedEmail = false
        if(fld_password.text.isNullOrBlank()) validatedPassword = false
        if(fld_email.text.contains("'")) validatedEmail = false
        if(fld_password.text.contains("'")) validatedPassword = false
        if(!fld_email.text.contains("@")) validatedEmail = false
        if(!fld_email.text.contains(".")) validatedEmail = false
        if(fld_email.text.contains(";")) validatedEmail = false
        if(fld_password.text.toString() != fld_passwordConfirm.text.toString()) validateConfirmPassword = false
        if(!validatedPassword) fld_password.error = "Invalid Password"
        if(!validatedEmail) fld_email.error = "Invalid Email"
        if(!validateConfirmPassword) fld_passwordConfirm.error = "Passwords do not match"
        if(!validatedEmail || !validatedPassword || !validateConfirmPassword) dataValidated = false
        return dataValidated
    }
}
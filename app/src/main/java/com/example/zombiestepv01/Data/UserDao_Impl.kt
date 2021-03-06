package com.example.zombiestepv01.Data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.zombiestepv01.Model.BEItem
import com.example.zombiestepv01.Model.BEUser
import com.example.zombiestepv01.R
import java.io.File

class UserDao_Impl (context: Context) : SQLiteOpenHelper(context, DATABASE_USER, null, DATABASE_VERSION), IUserDao {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_USER = "User"
        private const val DATABASE_STORE = "Store"
    }


    override fun onCreate(db: SQLiteDatabase?) { //Creates the Friend table on runtime with the relevant table columns
        db?.execSQL("CREATE TABLE $DATABASE_USER (id INTEGER PRIMARY KEY, name TEXT, email NVARCHAR(255) UNIQUE, password TEXT, stepCoins INTEGER, totalSteps INTEGER, multiplier DOUBLE, fortressLvl INTEGER, wallLvl INTEGER, weaponsLvl INTEGER, picture String )")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) { //drops the Friend table in case the Db version is updated
        db!!.execSQL("DROP TABLE IF EXISTS $DATABASE_USER")
        onCreate(db)
    }
    override fun login(InputEmail : String, InputPassword : String): BEUser {
        var retrievedUser = BEUser(0,"1","aa","aa", 3,2,1.0,2,3,1,null)
        val db = this.readableDatabase
        var cursor = db.query("$DATABASE_USER", arrayOf("id","name","email","password","stepCoins","totalSteps","multiplier","fortressLvl","wallLvl","weaponsLvl","picture"),"email LIKE '%$InputEmail%' AND password LIKE '%$InputPassword%'",null,null,null,"id")
        var result = getByCursor(cursor)
        if (result.isNotEmpty()) {
            return result[0]
        }
        else {
            return retrievedUser
        }
    }

    override fun checkIfUserExists(email: String): Boolean {
        val db = this.readableDatabase
        var cursor = db.query("$DATABASE_USER", arrayOf("id","name","email","password","stepCoins","totalSteps","multiplier","fortressLvl","wallLvl","weaponsLvl","picture"),"email LIKE '%$email%'",null,null,null,"id")
        var result = getByCursor(cursor)
        return result.isNotEmpty()
    }

    private fun getByCursor(cursor: Cursor): List<BEUser> {
        val result = ArrayList<BEUser>()
        if(cursor.moveToFirst()){
            do{
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val name = cursor.getString(cursor.getColumnIndex("name"))
                val email = cursor.getString(cursor.getColumnIndex("email"))
                val password = cursor.getString(cursor.getColumnIndex("password"))
                val stepCoins = cursor.getInt(cursor.getColumnIndex("stepCoins"))
                val totalSteps = cursor.getInt(cursor.getColumnIndex("totalSteps"))
                val multiplier = cursor.getDouble(cursor.getColumnIndex("multiplier"))
                val fortressLvl = cursor.getInt(cursor.getColumnIndex("fortressLvl"))
                val wallLvl = cursor.getInt(cursor.getColumnIndex("wallLvl"))
                val weaponsLvl = cursor.getInt(cursor.getColumnIndex("weaponsLvl"))
                val picture = cursor.getString(cursor.getColumnIndex("picture"))
                result.add(BEUser(id,name,email,password,stepCoins,totalSteps,multiplier,fortressLvl,wallLvl,weaponsLvl,File(picture)))
            }while (cursor.moveToNext())
        }
        return result
    }

    override fun createNewUser(user: BEUser) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put("name", user.name)
        cv.put("email",user.email)
        cv.put("password",user.password)
        cv.put("stepCoins",user.stepCoins)
        cv.put("totalSteps",user.totalSteps)
        cv.put("multiplier",user.multiplier)
        cv.put("fortressLvl",user.fortressLvl)
        cv.put("wallLvl",user.wallLvl)
        cv.put("weaponsLvl",user.weaponsLvl)
        cv.put("picture",user.Picture.toString())
        val result = db.insert("$DATABASE_USER", null, cv)
        if (result > 0.toLong()) {
            user.id = result.toInt()
        }
    }

    override fun updateUser(user: BEUser) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put("name", user.name)
        cv.put("email",user.email)
        cv.put("password",user.password)
        cv.put("stepCoins",user.stepCoins)
        cv.put("totalSteps",user.totalSteps)
        cv.put("multiplier",user.multiplier)
        cv.put("fortressLvl",user.fortressLvl)
        cv.put("wallLvl",user.wallLvl)
        cv.put("weaponsLvl",user.weaponsLvl)
        cv.put("picture",user.Picture.toString())
        val whereClause = "id=?"
        val whereArgs = arrayOf((user.id).toString())
        db.update("$DATABASE_USER", cv, whereClause, whereArgs)

    }

    override fun getUserById(id: Int) : BEUser {
        var retrievedUser = BEUser(0,"1","aa","aa", 3,2,1.0,2,3,1,null)
        val db = this.readableDatabase
        var cursor = db.query("$DATABASE_USER", arrayOf("id","name","email","password","stepCoins","totalSteps","multiplier","fortressLvl","wallLvl","weaponsLvl","picture"),"id LIKE '%$id%'",null,null,null,"id")
        var result = getByCursor(cursor)
        if (result.isNotEmpty()) {
            return result[0]
        }
        else {
            return retrievedUser
        }
    }

    override fun restartDb() {
        val db = this.writableDatabase
        db!!.execSQL("DELETE FROM $DATABASE_USER WHERE 1=1")
        insertMocks(db)
    }

    private fun insertMocks(db: SQLiteDatabase) {
        val cv = ContentValues()
        cv.put("name", "Bruce")
        cv.put("email","wayne@batman.com")
        cv.put("password",1234)
        cv.put("stepCoins",10000000)
        cv.put("totalSteps",1939)
        cv.put("multiplier",1.0)
        cv.put("fortressLvl",0)
        cv.put("wallLvl",0)
        cv.put("weaponsLvl",0)
        cv.put("picture", R.drawable.hazard.toString())
        val result = db.insert("$DATABASE_USER", null, cv)
    }

}
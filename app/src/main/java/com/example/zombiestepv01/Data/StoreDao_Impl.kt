package com.example.zombiestepv01.Data

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import com.example.zombiestepv01.Model.BEItem

class StoreDao_Impl (context: Context) : SQLiteOpenHelper(context, DATABASE_STORE, null, DATABASE_VERSION), IStoreDao {

    companion object {
        private const val DATABASE_VERSION = 2
        private const val DATABASE_STORE = "Store"
    }

    override fun onCreate(db: SQLiteDatabase?) { //Creates the Friend table on runtime with the relevant table columns
        db?.execSQL("CREATE TABLE ${StoreDao_Impl.DATABASE_STORE} (id INTEGER PRIMARY KEY, userId INTEGER, itemName TEXT, itemPrice INTEGER, itemDescription TEXT, itemLevel INTEGER, itemType INTEGER)")
    }
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) { //drops the Friend table in case the Db version is updated
        db!!.execSQL("DROP TABLE IF EXISTS ${StoreDao_Impl.DATABASE_STORE}")
        onCreate(db)
    }

    override fun initializeStore(userId: Int) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put("userId", "$userId")
        cv.put("itemName","Gun")
        cv.put("itemPrice",1000)
        cv.put("itemDescription","Increases your weapons level")
        cv.put("itemLevel",1)
        cv.put("itemType", 1)
        db.insert("${StoreDao_Impl.DATABASE_STORE}", null, cv)

        val cv2 = ContentValues()
        cv2.put("userId", "$userId")
        cv2.put("itemName","Wall Reinforcement")
        cv2.put("itemPrice",1000)
        cv2.put("itemDescription","Increases your wall level")
        cv2.put("itemLevel",1)
        cv2.put("itemType",2)
        db.insert("${StoreDao_Impl.DATABASE_STORE}", null, cv2)

        val cv3 = ContentValues()
        cv3.put("userId", "$userId")
        cv3.put("itemName","Survivor")
        cv3.put("itemPrice",1000)
        cv3.put("itemDescription","Increases your multiplier by 0.25")
        cv3.put("itemLevel",1)
        cv3.put("itemType",3)
        db.insert("${StoreDao_Impl.DATABASE_STORE}", null, cv3)
    }

    override fun getStore(userId: Int): List<BEItem> {

        val itemLst: ArrayList<BEItem> = ArrayList()
        val selectQuery = "SELECT  * FROM ${StoreDao_Impl.DATABASE_STORE} WHERE userId LIKE $userId ORDER BY id"
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, null)
        } catch (e: SQLiteException) {
            db.execSQL(selectQuery)
            return ArrayList()
        }
        var itemId: Int
        var retrievedUserId: Int
        var itemName: String
        var itemPrice: Int
        var itemDescription: String
        var itemLevel: Int
        var itemType: Int
        if (cursor.moveToFirst()) {
            do {
                itemId = cursor.getInt(cursor.getColumnIndex("id"))
                retrievedUserId = cursor.getInt(cursor.getColumnIndex("userId"))
                itemName = cursor.getString(cursor.getColumnIndex("itemName"))
                itemPrice = cursor.getInt(cursor.getColumnIndex("itemPrice"))
                itemDescription = cursor.getString(cursor.getColumnIndex("itemDescription"))
                itemLevel = cursor.getInt(cursor.getColumnIndex("itemLevel"))
                itemType  = cursor.getInt(cursor.getColumnIndex("itemType"))
                val storeItem = BEItem(
                        itemId = itemId,
                        userId = retrievedUserId,
                        itemName = itemName,
                        itemPrice = itemPrice,
                        itemDescription = itemDescription,
                        itemLevel = itemLevel,
                        itemType = itemType
                )
                itemLst.add(storeItem)
            } while (cursor.moveToNext())
        }
        return itemLst
    }
     override fun restartDb() {
            val db = this.writableDatabase
            db!!.execSQL("DELETE FROM ${StoreDao_Impl.DATABASE_STORE} WHERE 1=1")
            initializeStore(1)
            initializeStore(2)
        }

    override fun updateStore(item: BEItem) {
        val db = this.writableDatabase
        val cv = ContentValues()
        cv.put("userId", item.userId)
        cv.put("itemName",item.itemName)
        cv.put("itemPrice",item.itemPrice)
        cv.put("itemDescription",item.itemDescription)
        cv.put("itemLevel",item.itemLevel)
        cv.put("itemType",item.itemType)
        val whereClause = "id=?"
        val whereArgs = arrayOf((item.itemId).toString())
        db.update("${StoreDao_Impl.DATABASE_STORE}", cv, whereClause, whereArgs)
    }

}
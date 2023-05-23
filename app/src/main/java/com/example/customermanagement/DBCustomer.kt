package com.example.customermanagement

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.core.content.contentValuesOf
import javax.security.auth.Subject

class DBCustomer (context: Context, factory: SQLiteDatabase.CursorFactory?):
SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION){

    var context: Context
    init { // save context parameter object for later use
        this.context = context
    }

    companion object {
        private val DB_NAME = "smtbiz"
        private val DB_VERSION = 1
        val TABLE_NAME = "customer"
        val ID = "id"
        val NAME = "name"
        val EMAIL = "email"
        val MOBILE = "mobile"
        var latestId = 123000

         private fun nextID(): Int {
             latestId++
             return latestId
         }

          fun resetID() {
            latestId = 123000
          }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        //create table and query
        val query = (
                "CREATE TABLE $TABLE_NAME (" +
                        "$ID INTEGER PRIMARY KEY," +
                        "$NAME TEXT," +
                        "$MOBILE TEXT," +
                        "$EMAIL TEXT"+ ")"
                )
        db?.execSQL(query)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL("DROP TABLE IF EXISTS $TABLE_NAME") //NON-Null assertion. Error if null at compile time
        onCreate(db)
    }

    fun deleteDB(): Boolean {
        resetID()
        return context.deleteDatabase(DB_NAME)
    }

    fun addCustomers(name: String, mobile: String, email: String) {
        val values = ContentValues()
        val id = nextID()

        values.put(ID, id)
        values.put(NAME, name)
        values.put(MOBILE, mobile)
        values.put(EMAIL, email)

        val db = this.writableDatabase

        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun printCustomers(): ArrayList<Customer> {
        val db = this.readableDatabase

        val cursor = db.rawQuery("SELECT * FROM $TABLE_NAME",null)
        val customerList = ArrayList<Customer>()

        if (cursor.moveToFirst()) {
            do {
                customerList.add(
                    Customer(
                        cursor.getString(cursor.getColumnIndexOrThrow(ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(MOBILE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(EMAIL))
                    )
                )
            } while (cursor.moveToNext())
        }
        cursor.close()
        return customerList
    }

    fun deleteCustomers(id: Int): Int {
        val db = this.writableDatabase

        db.beginTransaction()
        try{
            val rows = db.delete(TABLE_NAME, "id=?", arrayOf(id.toString()))
            db.setTransactionSuccessful()
            return rows
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    fun updateCustomers(id: Int, name: String, mobile: String, email: String): Int {
        val db = this.writableDatabase
        val values = ContentValues()

        values.put(NAME, name)
        values.put(MOBILE, mobile)
        values.put(EMAIL, email)

        db.beginTransaction()
        try{
            val rows = db.update(TABLE_NAME, values, "id=?", arrayOf(id.toString()))
            db.setTransactionSuccessful()
            return rows
        }
        finally {
            db.endTransaction()
            db.close()
        }
    }

    fun searchCustomers(name: String): ArrayList<Customer> {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $NAME LIKE '%$name%'"
        Log.d("Search Query", query)
        val cursor = db.rawQuery(query, null)

        val searchList = ArrayList<Customer>()

        while (cursor.moveToNext()) {
            val customer = Customer(
                cursor.getString(cursor.getColumnIndexOrThrow(ID)),
                cursor.getString(cursor.getColumnIndexOrThrow(NAME)),
                cursor.getString(cursor.getColumnIndexOrThrow(MOBILE)),
                cursor.getString(cursor.getColumnIndexOrThrow(EMAIL))
            )
            searchList.add(customer)
        }

        cursor.close()
        return searchList
    }



}
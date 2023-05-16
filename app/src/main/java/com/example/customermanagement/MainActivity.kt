package com.example.customermanagement

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etCustomerID = findViewById<EditText>(R.id.etCustomerID)
        val etName = findViewById<EditText>(R.id.etName)
        val etMobile = findViewById<EditText>(R.id.etMobile)
        val etEmail = findViewById<EditText>(R.id.etEmail)
        val tvCustomer = findViewById<TextView>(R.id.tvCustomer)

        val initialDB = DBCustomer(this, null)
        initialDB.addCustomers("Nick Scali", "0433388899", "nickscali@gmail.com")
        initialDB.addCustomers("Jame Hard", "0432569874", "jameshard@gmail.com")
        initialDB.addCustomers("Alice Wonderland", "0412345678", "alice@gmail.com")
        initialDB.addCustomers("Ken Bek", "0432698556", "ken@gmail.com")
        initialDB.addCustomers("Stuart G", "0495632140", "stuart@gmail.com")


        val btnAddCustomer = findViewById<Button>(R.id.btnAddCustomer)
        btnAddCustomer.setOnClickListener {

            val db = DBCustomer(this, null)
            val name = etName.text.toString()
            val mobile = etMobile.text.toString()
            val email = etEmail.text.toString()

            db.addCustomers(name, mobile, email)

            Toast.makeText(this, "$name is added to database", Toast.LENGTH_SHORT).show()

            etName.text.clear()
            etEmail.text.clear()
            etMobile.text.clear()
        }

        val btnPrintUsers =findViewById<Button>(R.id.btnPrintCustomer)
        btnPrintUsers.setOnClickListener {
            val db = DBCustomer(this, null)
            val customerList = db.printCustomers()

            tvCustomer.text = ""

            customerList.forEach {
                tvCustomer.append("$it\n")
            }
        }

        val btnDeleteCustomer = findViewById<Button>(R.id.btnDeleteCustomer)
        btnDeleteCustomer.setOnClickListener {
            val db = DBCustomer(this, null)
            val customerID = etCustomerID.text.toString().toInt()
            val rows = db.deleteCustomers(customerID)

            Toast.makeText(
                this,
                when (rows) {
                    0 -> "Nothing deleted"
                    1 -> "1 user deleted"
                    else -> ""
                },
                Toast.LENGTH_LONG
            ).show()

            etCustomerID.text.clear()
        }

        val btnUpdateCustomer = findViewById<Button>(R.id.btnUpdateCustomer)
        btnUpdateCustomer.setOnClickListener {
            val db = DBCustomer(this, null)
            val customerID = etCustomerID.text.toString().toInt()
            val name = etName.text.toString()
            val mobile = etMobile.text.toString()
            val email = etEmail.text.toString()

            val rows = db.updateCustomers(customerID,name,mobile,email)

            Toast.makeText(this, "$rows users updated", Toast.LENGTH_SHORT).show()

            etCustomerID.text.clear()
        }

        val btnSearchCustomer = findViewById<Button>(R.id.btnSearch)
        btnSearchCustomer.setOnClickListener {
            val db = DBCustomer(this, null)
            val customerID = etCustomerID.toString().toInt()
            val rows = db.searchCustomers(customerID)

            tvCustomer.text = "$rows"
        }

        val btnDeleteDatabase = findViewById<Button>(R.id.btnDeleteDatabase)
        btnDeleteDatabase.setOnClickListener {
            val db = DBCustomer(this, null)
            val isSuccessful = db.deleteDB()
            Customer.resetID()

            Toast.makeText(this,
                when (isSuccessful) {
                    true -> "Database successfully deleted"
                    false -> "Failed to delete database"
                },
                Toast.LENGTH_LONG
            ).show()
        }

    }
}
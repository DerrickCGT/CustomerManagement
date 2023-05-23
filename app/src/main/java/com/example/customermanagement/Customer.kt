package com.example.customermanagement

class Customer(val id: String, val name: String, val mobile: String, val email: String){
//    val id: Int = nextID()
//
//    companion object {
//        var latestId = 1230000
//
//        private fun nextID(): Int {
//            latestId++
//            return latestId
//        }
//
//        fun resetID() {
//            latestId = 123000
//        }
//    }



    override fun toString(): String {
        return "ID: $id | Name: $name \n M: $mobile | E: $email"
    }
}
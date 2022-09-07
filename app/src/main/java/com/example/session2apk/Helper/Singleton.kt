package com.example.session2apk.Helper

import com.example.session2apk.Model.User

object Singleton {
    lateinit var userLogin:User
    enum class STAT {
        REGISTER, ADD, UPDATE
    }
    var statusRegister:STAT = STAT.REGISTER
}
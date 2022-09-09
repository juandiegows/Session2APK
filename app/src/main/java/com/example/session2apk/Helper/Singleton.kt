package com.example.session2apk.Helper

import com.example.session2apk.Model.User

object Singleton {
    var userLogin: User = User()
    var userLoginEdit: User = User()

    enum class STAT {
        REGISTER, ADD, UPDATE
    }

    var statusRegister: STAT = STAT.REGISTER
}
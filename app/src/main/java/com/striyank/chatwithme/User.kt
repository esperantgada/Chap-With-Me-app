package com.striyank.chatwithme

class User() {
    var name : String? = null
    var email : String? = null
    var uid : String? = null


    constructor(name : String?, email : String? , uid : String?) : this() {
        this.name = name
        this.email = email
        this.uid = uid
    }

}
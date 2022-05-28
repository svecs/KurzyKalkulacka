package com.example.kurzykalkulacka.data

import android.util.Log
import java.lang.RuntimeException

class NejdeAPIException(message: String?) : RuntimeException(message) {
    init {
        this.printStackTrace()
        Log.wtf("NejdeAPIException", message)
    }
}
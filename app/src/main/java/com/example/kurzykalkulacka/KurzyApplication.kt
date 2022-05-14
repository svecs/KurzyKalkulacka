package com.example.kurzykalkulacka

import android.app.Application
import com.example.kurzykalkulacka.data.DefaultRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class KurzyApplication : Application() {
    var applicationScope = CoroutineScope(SupervisorJob())
}
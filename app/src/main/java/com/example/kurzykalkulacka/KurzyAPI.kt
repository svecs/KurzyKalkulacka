package com.example.kurzykalkulacka

import androidx.annotation.WorkerThread
import com.example.kurzykalkulacka.data.entities.Kurz
import retrofit2.Response
import retrofit2.http.GET

interface KurzyAPI {

    @WorkerThread
    @GET("stats/eurofxref/eurofxref-hist-90d.xml")
    suspend fun ziskajKurzy90(): Response<KurzyResponse>

}
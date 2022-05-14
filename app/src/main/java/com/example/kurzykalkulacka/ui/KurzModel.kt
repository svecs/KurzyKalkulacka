package com.example.kurzykalkulacka.ui

import com.example.kurzykalkulacka.data.entities.Kurz
import com.example.kurzykalkulacka.data.entities.Mena
import kotlin.math.abs

class KurzModel(
    val mena: Mena,
    val dnesnyKurz: Kurz,
    val vcerajsiKurz: Kurz,
) {
    val percento: Double
        get() = abs(1.0 - dnesnyKurz.hodnota/vcerajsiKurz.hodnota)

    val pohyb: Int
        get() = if(percento > 0) Pohyb.RAST
                else if(percento < 0) Pohyb.POKLES
                else Pohyb.STAG

    object Pohyb {
        const val RAST = 1
        const val POKLES = -1
        const val STAG = 0
    }
}
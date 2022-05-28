package com.example.kurzykalkulacka.ui

import android.graphics.drawable.Drawable
import com.example.kurzykalkulacka.data.entities.Kurz
import com.example.kurzykalkulacka.data.entities.Mena
import com.murgupluoglu.flagkit.FlagKit
import kotlin.math.abs

class KurzModel(
    val mena: Mena,
    val dnesnyKurz: Kurz,
    val vcerajsiKurz: Kurz,
    var vlajka: Drawable? = null
) {
    val percento: Double
        get() = dnesnyKurz.hodnota/vcerajsiKurz.hodnota - 1.0

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
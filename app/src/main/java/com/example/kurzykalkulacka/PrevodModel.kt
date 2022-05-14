package com.example.kurzykalkulacka

import com.example.kurzykalkulacka.data.entities.Kurz
import java.text.SimpleDateFormat
import java.util.*

object PrevodModel {

    fun preved(z: Kurz, na: Kurz, hodnota: Double): Double {
        return hodnota/z.hodnota*na.hodnota
    }

    private fun stringNaDatum(datum: String): Date =
        SimpleDateFormat("yyyy-MM-dd").parse(datum)

    fun staryStringZDatumu(datum: String): String {
        var staryDatum = stringNaDatum(datum)
        val c: Calendar = Calendar.getInstance()
        c.time = staryDatum
        c.add(Calendar.MONTH, -3)
        staryDatum = c.time
        return SimpleDateFormat("yyyy-MM-dd").format(staryDatum)
    }

    fun dnesnyDatumString(): String =
        SimpleDateFormat("yyyy-MM-dd").format(Date())

    fun slovenskyDatum(anglickyDatum: String) =
        SimpleDateFormat("d.M.yyyy").format(SimpleDateFormat("yyyy-MM-dd").parse(anglickyDatum))
}
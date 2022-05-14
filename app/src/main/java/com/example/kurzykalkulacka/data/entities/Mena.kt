package com.example.kurzykalkulacka.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meny")
data class Mena(
    @PrimaryKey val skratka: String,
    val anglickyNazov: String,
    val slovenskyNazov: String,
    val oblubena: Boolean = false
)
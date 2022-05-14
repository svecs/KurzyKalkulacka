package com.example.kurzykalkulacka.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import java.sql.Date

@Entity(tableName = "kurzy",
        foreignKeys = [ForeignKey(
            entity = Mena::class,
            parentColumns = ["skratka"],
            childColumns =  ["idMeny"],
            onDelete = ForeignKey.CASCADE
        )],
        primaryKeys = ["idMeny", "datum"]
)
data class Kurz(val idMeny: String, val datum: String, val hodnota: Double) {
}

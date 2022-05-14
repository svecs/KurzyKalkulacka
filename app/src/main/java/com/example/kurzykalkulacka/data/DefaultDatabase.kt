package com.example.kurzykalkulacka.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kurzykalkulacka.data.entities.Kurz
import com.example.kurzykalkulacka.data.dao.KurzDao
import com.example.kurzykalkulacka.data.dao.MenaDao
import com.example.kurzykalkulacka.data.entities.Mena

@Database(entities = [Kurz::class, Mena::class], version = 4, exportSchema = false)
abstract class DefaultDatabase : RoomDatabase() {
    abstract val kurzDao : KurzDao
    abstract val menaDao : MenaDao
}
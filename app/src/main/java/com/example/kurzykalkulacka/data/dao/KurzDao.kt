package com.example.kurzykalkulacka.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kurzykalkulacka.data.entities.Kurz
import dagger.Provides
import kotlinx.coroutines.flow.Flow

@Dao
interface KurzDao {

    //@Query("SELECT * FROM kurzy k JOIN meny m ON k.idMeny = m.id WHERE datum=:dnes")
    @Query("SELECT * FROM kurzy WHERE datum=:dnes")
    fun readDnesneKurzy(dnes: String): Flow<List<Kurz>>

    @Query("SELECT * FROM kurzy WHERE datum = (SELECT max(datum) FROM kurzy) ORDER BY idMeny")
    suspend fun getNajnovsieKurzy(): List<Kurz>

    @Query("SELECT * FROM kurzy WHERE datum = (SELECT DISTINCT datum FROM kurzy ORDER BY datum DESC LIMIT 1 OFFSET 1) ORDER BY idMeny")
    suspend fun getStarsieKurzy(): List<Kurz>

    @Query("SELECT * FROM kurzy WHERE idMeny=:menaId ORDER BY datum DESC LIMIT 1")
    fun readNajnovsiKurzPreMenu(menaId: String): Kurz

    @Query("SELECT * FROM kurzy WHERE idMeny=:menaId ORDER BY datum DESC LIMIT 1 OFFSET 1")
    fun readVcerajsiKurzPreMenu(menaId: String): Kurz?

    @Query("SELECT datum FROM kurzy ORDER BY datum DESC LIMIT 1")
    suspend fun readNajnovsiDatum(): String

    @Query("DELETE FROM kurzy WHERE datum <= :datum")
    suspend fun zmazStare(datum: String)

    @Query("SELECT * FROM kurzy WHERE idMeny=:menaId ORDER BY datum DESC")
    suspend fun readVsetkyKurzyPreMenu(menaId: String): List<Kurz>

    @Insert
    /*suspend*/ fun insert(k: Kurz)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(kurzy: List<Kurz>)

    @Query("DELETE FROM kurzy")
    /*suspend*/ fun deleteAll()
}
package com.example.kurzykalkulacka.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kurzykalkulacka.data.entities.Mena
import kotlinx.coroutines.flow.Flow

@Dao
interface MenaDao {
    @Insert
    /*suspend*/ fun insert(m: Mena)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(meny: List<Mena>)

    @Query("DELETE FROM meny")
    suspend fun deleteAll()

    @Query("SELECT * FROM meny ORDER BY oblubena DESC, skratka ASC")
    suspend fun readAll(): List<Mena>

    @Query("SELECT COUNT(*) FROM meny")
    suspend fun menaCount(): Int

    @Query("SELECT * FROM meny WHERE skratka=:menaId")
    suspend fun readMena(menaId: String): Mena
}
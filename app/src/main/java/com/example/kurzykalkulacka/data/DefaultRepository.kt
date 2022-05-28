package com.example.kurzykalkulacka.data

import android.text.format.DateFormat
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import com.example.kurzykalkulacka.KurzyAPI
import com.example.kurzykalkulacka.KurzyResponse
import com.example.kurzykalkulacka.PrevodModel
import com.example.kurzykalkulacka.data.dao.KurzDao
import com.example.kurzykalkulacka.data.dao.MenaDao
import com.example.kurzykalkulacka.data.entities.Kurz
import com.example.kurzykalkulacka.data.entities.Mena
import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception
import java.time.LocalDate
import java.util.*
import javax.inject.Inject
import retrofit2.Callback
import java.lang.RuntimeException

@WorkerThread
class DefaultRepository @Inject constructor(
    private val remoteSource: DefaultRemoteSource,
    private val kurzDao: KurzDao,
    private val menaDao: MenaDao,
    val kurzyAPI: KurzyAPI
) {
    fun getDnesnyKurz(menaId: String?): Kurz? {
        return if(menaId == null) null
        else kurzDao.readNajnovsiKurzPreMenu(menaId)
    }

    suspend fun getNovsieKurzy(): List<Kurz> {
        return kurzDao.getNajnovsieKurzy()
    }

    suspend fun getStarsieKurzy(): List<Kurz> {
        return kurzDao.getStarsieKurzy()
    }

    suspend fun getMena(menaId: String): Mena {
        return menaDao.readMena(menaId)
    }

    suspend fun getVsetkyKurzyPreMenu(menaId: String): List<Kurz> {
        return kurzDao.readVsetkyKurzyPreMenu(menaId)
    }

    suspend fun jePrazdnaKurzTab(): Boolean = kurzDao.readPocetKurzov() == 0

    suspend fun getKurzy(): Boolean {
        menaDao.insertAll(Meny.vsetkyMeny)
        return try {
            val resp: Response<KurzyResponse> = kurzyAPI.ziskajKurzy90()
            Log.wtf("respons", resp.toString())
            if(resp.isSuccessful) {
                val k: KurzyResponse? = resp.body()
                Log.wtf("kr",k.toString())
                /*val meny = mutableListOf<Mena>()
                if (k != null) {
                    Log.wtf("kjub", k.cube!!.toString())
                    for (m in k.cube!!.cubes!![0].cubes!!) {
                        val mena: Mena = Mena(m.currency!!, false)
                        meny.add(mena)
                    }
                    meny.add(Mena("RUB", false))
                }*/

                //kurzDao.deleteAll()
                val kurzy = mutableListOf<Kurz>()
                for(i in k!!.cube!!.cubes!!) {
                    for(j in i.cubes!!) {
                        val kurz: Kurz = Kurz(j.currency!!, i.time!!, j.rate!!)
                        kurzy.add(kurz)
                        //Log.wtf("insert kurz", kurz.idMeny)
                        //kurzDao.insert(kurz)
                    }
                    kurzy.add(Kurz("EUR", i.time!!, 1.0))
                }
                kurzDao.insertAll(kurzy)
                //TODO kurzdao deleteold
                val sdatum = PrevodModel.staryStringZDatumu(kurzDao.readNajnovsiDatum())
                Log.wtf("stary datum", sdatum)
                kurzDao.zmazStare(sdatum)
            }
            true
        } catch (e: Exception) {
             false
        }
    }

    suspend fun getVsetkyMeny(): List<Mena> {
        return menaDao.readAll()
    }

    suspend fun setOblubena(mena: Mena?) {
        mena?.let {
            menaDao.setOblubena(it.skratka, it.oblubena)
        }
    }

    fun jePripojene() = Runtime.getRuntime().exec("ping -c 1 google.com").waitFor() == 0
    //https://stackoverflow.com/questions/9570237/android-check-internet-connection
}
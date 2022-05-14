package com.example.kurzykalkulacka.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kurzykalkulacka.data.DefaultRepository
import com.example.kurzykalkulacka.data.entities.Kurz
import com.example.kurzykalkulacka.data.entities.Mena
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository: DefaultRepository
): ViewModel() {

    val menaLD: MutableLiveData<Mena> = MutableLiveData<Mena>()
    val kurzyLD: MutableLiveData<List<Kurz>> = MutableLiveData<List<Kurz>>()

    fun nacitajData(menaId: String) {
        viewModelScope.launch {
            lateinit var mena: Mena
            lateinit var kurzy: List<Kurz>
            withContext(Dispatchers.IO) {
                try {
                    mena = repository.getMena(menaId)
                    kurzy = repository.getVsetkyKurzyPreMenu(menaId)
                }
                catch (e: Exception) {
                    e.printStackTrace()
                    throw e
                }
            }

            menaLD.value = mena
            kurzyLD.value = kurzy
        }
    }

}
package com.example.kurzykalkulacka.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kurzykalkulacka.data.DefaultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    val repository: DefaultRepository
): ViewModel() {

    val chybaPripojenia: MutableLiveData<Boolean> = MutableLiveData(false)

    val dataPrisli: MutableLiveData<Boolean> = MutableLiveData(false)

    val tabulkaPrazdna: MutableLiveData<Boolean> = MutableLiveData(false)

    var razZavolane = false

    fun jePrazdnaTabulka() {
        viewModelScope.launch(Dispatchers.IO) {
            val prazdnaTab = repository.jePrazdnaKurzTab()
            withContext(Dispatchers.Main) {
                tabulkaPrazdna.value = prazdnaTab
            }
        }
    }

    fun stiahniData() {

        try {

            viewModelScope.launch {

                withContext(Dispatchers.IO) {
                    val b = repository.getKurzy()
                    Log.wtf("STIAHNI DATA", b.toString())
                    if(!b) {
                        withContext(Dispatchers.Main) {
                            chybaPripojenia.value = true
                        }
                    }
                    else {
                        withContext(Dispatchers.Main) {
                            Log.wtf("HBNCSF", "menim dataPrisli")
                            dataPrisli.value = true
                            Log.wtf("HFdpFMAVM", dataPrisli.value.toString())

                        }
                    }
                }

            }
        }
        catch (e: Exception) {
            Log.wtf("NAE", "problem")
        }
    }

    override fun onCleared() {
        Log.wtf("MAVM", "oncleared()")
        super.onCleared()
    }
}
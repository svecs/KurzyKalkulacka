package com.example.kurzykalkulacka.ui.home

import android.text.format.DateFormat
import android.util.Log
import androidx.lifecycle.*
import com.example.kurzykalkulacka.data.entities.Kurz
import com.example.kurzykalkulacka.PrevodModel
import com.example.kurzykalkulacka.R
import com.example.kurzykalkulacka.data.DefaultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.reflect.Field
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PrevodyViewModel @Inject constructor(
    private val repository: DefaultRepository
): ViewModel() {

    fun loadData() {
        dataNacitane.value = false
        //val asc = viewModelScope.launch {
            //repository.loadData()
            dataNacitane.postValue(true)
        //}
    }

    fun stiahniData() {
        viewModelScope.launch {

            withContext(Dispatchers.IO) {
                repository.getKurzy()
            }

        }
    }

    val dataNacitane: MutableLiveData<Boolean> = MutableLiveData(false)

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    private val _firstVal = MutableLiveData<Double>().apply {
        value = 0.0;
    }
    private val _secondVal = MutableLiveData<Double>().apply {
        value = 0.0;
    }
    val text: LiveData<String> = _text
    val firstVal: MutableLiveData<Double> = _firstVal;
    val secondVal: MutableLiveData<Double> = _secondVal

    val kurzA: MutableLiveData<Kurz> = MutableLiveData<Kurz>(null)
    val kurzB: MutableLiveData<Kurz> = MutableLiveData<Kurz>(null)

    val nasobok: MutableLiveData<Double> = MutableLiveData(1.0)

    fun upravKurzA(idMeny: String) {
        //viewModelScope.launch(Dispatchers.IO) {
            kurzA.value = repository.getDnesnyKurz(idMeny)
            if(kurzA.value != null && kurzB.value != null)
                nasobok.value = (kurzB.value!!.hodnota)/(kurzA.value!!.hodnota)
        //}
    }

    fun upravKurzB(idMeny: String) {
        viewModelScope.launch {
            kurzB.value = repository.getDnesnyKurz(idMeny)
            if(kurzA.value != null && kurzB.value != null)
                nasobok.value = (kurzB.value!!.hodnota)/(kurzA.value!!.hodnota)
        }
    }

    fun aktualizujPrevodyZhoraDole() {
        /*Log.e("KURZ_A", "uvod")
        var k: Kurz = repository.getDnesnyKurz(idMenyA.value!!)
        Log.e("KURZ_A", kurzA.value.toString())*/
        if(kurzA.value == null || kurzB.value == null) return
        Log.i("FW", firstVal.value!!.toString())
        val d: Double = PrevodModel.preved(kurzA.value!!, kurzB.value!!, firstVal.value!!)
        secondVal.value = d
        Log.i("AK", d.toString())
    }

    fun aktualizujPrevodyZdolaHore() {
        /*Log.e("KURZ_B", "uvod")
        var k: Kurz = repository.getDnesnyKurz(idMenyB.value!!)
        Log.e("KURZ_B", kurzB.value.toString())*/
        if(kurzA.value == null || kurzB.value == null) return
        Log.i("FW", secondVal.value!!.toString())
        val d: Double = PrevodModel.preved(kurzB.value!!,kurzA.value!!, secondVal.value!!)
        firstVal.value = d
        Log.i("AK", d.toString())
    }

}
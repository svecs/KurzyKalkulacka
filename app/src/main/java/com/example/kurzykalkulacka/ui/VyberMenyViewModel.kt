package com.example.kurzykalkulacka.ui

import android.graphics.drawable.Drawable
import android.util.Log
import androidx.lifecycle.*
import com.example.kurzykalkulacka.data.DefaultRepository
import com.example.kurzykalkulacka.data.entities.Mena
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class VyberMenyViewModel @Inject constructor(
    val repository: DefaultRepository
): ViewModel() {

    var zoznamMien: LiveData<List<Mena>> = liveData {

        withContext(Dispatchers.IO) {
            val data = repository.getVsetkyMeny()
            Log.wtf("data", data.toString())
            emit(data)
        }
    }

    var zoznamVlajok: List<Drawable?> = listOf()
}
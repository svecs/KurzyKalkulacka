package com.example.kurzykalkulacka.ui.rates

import androidx.lifecycle.*
import com.example.kurzykalkulacka.data.DefaultRepository
import com.example.kurzykalkulacka.data.entities.Kurz
import com.example.kurzykalkulacka.ui.KurzModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: DefaultRepository
) : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is dashboard Fragment"
    }
    val text: LiveData<String> = _text

    var oblubeneMeny = 0

    var prveNacitanie = true

    val zoznamKurzModel: LiveData<List<KurzModel>> = liveData {
        withContext(Dispatchers.IO) {
            var meny = repository.getVsetkyMeny()
            val dnesneKurzy = repository.getNovsieKurzy()
            val vcerajsieKurzy = repository.getStarsieKurzy()

            meny = meny.sortedBy { it.skratka }

            var km: List<KurzModel> = mutableListOf()
            meny.forEach { mena ->
                var dnesnyKurz: Kurz? = null
                var vceraKurz: Kurz? = null
                for(dk in dnesneKurzy) {
                    if(mena.skratka == dk.idMeny) {
                        dnesnyKurz = dk
                        break
                    }
                }
                for(dk in vcerajsieKurzy) {
                    if(mena.skratka == dk.idMeny) {
                        vceraKurz = dk
                        break
                    }
                }
                if(dnesnyKurz != null && vceraKurz != null) {
                    (km as MutableList<KurzModel>).add(KurzModel(mena, dnesnyKurz, vceraKurz))
                    if(mena.oblubena == true) oblubeneMeny++
                }
            }

            km = km.sortedByDescending {
                it.mena.oblubena
            }
            emit(km)
        }
    }
}
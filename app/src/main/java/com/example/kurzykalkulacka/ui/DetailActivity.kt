package com.example.kurzykalkulacka.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.kurzykalkulacka.PrevodModel
import com.example.kurzykalkulacka.R
import com.example.kurzykalkulacka.databinding.ActivityDetailBinding
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.detailToolbar)

        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        detailViewModel.nacitajData(intent.getStringExtra("idMeny")!!)

        detailViewModel.menaLD.observe(this) {
            binding.englishTw.text = it.anglickyNazov
            binding.slovakTw.text = it.slovenskyNazov
            binding.isoTw.text = it.skratka
            binding.favCb.isChecked = it.oblubena
        }

        detailViewModel.kurzyLD.observe(this) {
            if(it.isNotEmpty()) {
                binding.valueTw.text = it[it.size-1].hodnota.toString()
                binding.lastUpTw.text = PrevodModel.slovenskyDatum(it[it.size - 1].datum)

                val entries: MutableList<Entry> = mutableListOf()

                var d = 1F

                for(e in it) {
                    entries.add(Entry(d, e.hodnota.toFloat()))
                    d++
                }

                val dataSet: LineDataSet = LineDataSet(entries, "label");
                dataSet.setDrawValues(false)
                dataSet.color = getColor(R.color.purple_500)
                dataSet.setDrawCircles(false)
                binding.detailChart.setDrawBorders(true)
                binding.detailChart.xAxis.setDrawGridLines(false)
                binding.detailChart.axisLeft.setDrawGridLines(false)
                binding.detailChart.data = LineData(dataSet)
                binding.detailChart.legend.isEnabled = false
                binding.detailChart.xAxis.setDrawLabels(false)
                binding.detailChart.description.text = ""
                binding.detailChart.invalidate()
            }
        }

        binding.favCb.setOnClickListener {
            if(binding.favCb.isChecked) {
                Log.wtf("favcb", "check")
                detailViewModel.nastavOblubena(true)

            } else {
                Log.wtf("favcb", "not check")
                detailViewModel.nastavOblubena(false)
            }
        }



    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        binding.favCb.isChecked = detailViewModel.menaLD.value?.oblubena ?: false
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onSupportNavigateUp()
        ukonci()
        return true
    }

    override fun onBackPressed() {
        ukonci(true)
        super.onBackPressed()
    }

    fun ukonci(bp: Boolean = false) {
        val i = Intent()
        //"idMeny")
        //val zmeniloSa = it.data?.getBooleanExtra("zmeniloSa", false) ?: false
        //val novaOblubenost = it.data?.getBooleanExtra("oblubenost"
        i.putExtra("idMeny", detailViewModel.menaLD.value?.skratka)
        i.putExtra("zmeniloSa", detailViewModel.zmeniloSa)
        i.putExtra("oblubenost", detailViewModel.menaLD.value?.oblubena)
        setResult(Activity.RESULT_OK, i)
        if(!bp) finish()
        Log.wtf("koniec detail", "Riadne ukoncujem aktivitu DetailActivity")
    }
}
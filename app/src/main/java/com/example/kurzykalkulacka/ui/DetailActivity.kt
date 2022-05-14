package com.example.kurzykalkulacka.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import com.example.kurzykalkulacka.PrevodModel
import com.example.kurzykalkulacka.R
import com.example.kurzykalkulacka.databinding.ActivityDetailBinding
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
                binding.valueTw.text = it[0].hodnota.toString()
                binding.lastUpTw.text = PrevodModel.slovenskyDatum(it[0].datum)
            }
        }

        binding.favCb.setOnClickListener {
            if(binding.favCb.isChecked) {
                Log.wtf("favcb", "check")

            } else {
                Log.wtf("favcb", "not check")
            }


        }
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onSupportNavigateUp()
        finish()
        return true
    }
}
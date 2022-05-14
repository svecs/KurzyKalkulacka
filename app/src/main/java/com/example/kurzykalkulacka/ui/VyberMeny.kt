package com.example.kurzykalkulacka.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.SearchView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kurzykalkulacka.R
import com.example.kurzykalkulacka.data.entities.Mena
import com.example.kurzykalkulacka.databinding.ActivityVyberMenyBinding
import com.example.kurzykalkulacka.databinding.FragmentHomeBinding
import com.murgupluoglu.flagkit.FlagKit
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class VyberMeny : AppCompatActivity(), VyberMenyAdapter.MenaClickListener {
    private lateinit var binding: ActivityVyberMenyBinding
    private val vyberMenyViewModel: VyberMenyViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVyberMenyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.vyberMenyToolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        val searchEditText = (binding.vybermenySearchview.findViewById(androidx.appcompat.R.id.search_src_text)) as EditText
        searchEditText.setTextColor(Color.WHITE)


        binding.vyberMenyRw.layoutManager = LinearLayoutManager(this)

        vyberMenyViewModel.zoznamMien.observe(this) { zoznam ->
            Log.wtf("DATA", "zmenili sa data")
            val vlajky: List<Drawable?> = zoznam.map {
                FlagKit.getDrawable(this, it.skratka.lowercase().substring(0,2))
            }
            vyberMenyViewModel.zoznamVlajok = vlajky
            val farbaHviezdy: Int = ContextCompat.getColor(this, R.color.purple_700)
            binding.vyberMenyRw.adapter = VyberMenyAdapter(zoznam, vlajky, this, farbaHviezdy)
        }

        val searchListener: androidx.appcompat.widget.SearchView.OnQueryTextListener = object: androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                Log.wtf("Hladanie", p0)
                val meny: MutableList<Mena> = mutableListOf()
                val vlajky: MutableList<Drawable?> = mutableListOf()
                if(p0.isNullOrEmpty()) {
                    //zobrazime vsetko
                    (binding.vyberMenyRw.adapter as VyberMenyAdapter).data = vyberMenyViewModel.zoznamMien.value!!
                    (binding.vyberMenyRw.adapter as VyberMenyAdapter).vlajky = vyberMenyViewModel.zoznamVlajok
                }
                else {
                    vyberMenyViewModel.zoznamMien.value?.forEachIndexed { index, mena ->
                        if(mena.skratka.contains(p0, true) or mena.slovenskyNazov.contains(p0, true)) {
                            meny.add(mena)
                            vlajky.add(vyberMenyViewModel.zoznamVlajok[index])
                        }
                    }
                    (binding.vyberMenyRw.adapter as VyberMenyAdapter).data = meny
                    (binding.vyberMenyRw.adapter as VyberMenyAdapter).vlajky = vlajky
                }
                (binding.vyberMenyRw.adapter as VyberMenyAdapter).notifyDataSetChanged()
                return true
            }

        }

        binding.vybermenySearchview.setOnQueryTextListener(searchListener)


    }

    override fun onSupportNavigateUp(): Boolean {
        super.onSupportNavigateUp()
        onBackPressed()
        return true
    }

    override fun onClick(m: Mena) {
        Log.e("Button press", "stlaceny listitem")
        val poradie = intent.getIntExtra("poradie", -1)

        val intent: Intent = Intent()
        intent.putExtra("menaId", m.skratka);
        intent.putExtra("poradie", poradie)
        setResult(RESULT_OK, intent)
        finish()
    }
}
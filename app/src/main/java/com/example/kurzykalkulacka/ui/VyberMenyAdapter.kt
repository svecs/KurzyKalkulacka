package com.example.kurzykalkulacka.ui

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kurzykalkulacka.R
import com.example.kurzykalkulacka.data.entities.Mena
import com.google.android.material.card.MaterialCardView
import com.murgupluoglu.flagkit.FlagKit

class VyberMenyAdapter(var data: List<Mena>, var vlajky: List<Drawable?>, val listener: MenaClickListener, val farbaHviezdy: Int):
    RecyclerView.Adapter<VyberMenyAdapter.ViewHolder>() {

    interface MenaClickListener {
        fun onClick(m: Mena)
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val nazovMenyTextview: TextView
        val skratkaMenyTextview: TextView
        val vlajkaImageView: ImageView
        val karta: MaterialCardView
        val oblubenyImageView: ImageView
        init {
            nazovMenyTextview = view.findViewById(R.id.nazovMenyTextview)
            skratkaMenyTextview = view.findViewById(R.id.skratkaMenyTextview)
            vlajkaImageView = view.findViewById(R.id.vlajkaImageview)
            karta = view.findViewById(R.id.itemKarta)
            oblubenyImageView = view.findViewById(R.id.oblubeneVyberMeny)
        }
        fun bind(listener: MenaClickListener, mena: Mena) {
            karta.setOnClickListener {
                listener.onClick(mena)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vyber_meny_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.skratkaMenyTextview.text = data[position].skratka
        holder.nazovMenyTextview.text = data[position].slovenskyNazov
        holder.vlajkaImageView.setImageDrawable(vlajky[position])
        if(data[position].oblubena) {
            Log.wtf("VMA", data[position].toString())
            holder.oblubenyImageView.setImageResource(R.drawable.ic_baseline_star_24)
            holder.oblubenyImageView.setColorFilter(farbaHviezdy)
        }
        else {
            holder.oblubenyImageView.setImageResource(0)
        }
        holder.bind(listener, data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
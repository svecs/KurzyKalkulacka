package com.example.kurzykalkulacka.ui

import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kurzykalkulacka.R
import com.example.kurzykalkulacka.data.entities.Mena
import com.google.android.material.card.MaterialCardView
import org.w3c.dom.Text
import java.text.DecimalFormat

class KurzyAdapter(var meny: List<KurzModel>, var oblubene: Int, var vlajky: List<Drawable?>, var farby: Map<Int, Int>, val listener: KurzModelClickListener):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface KurzModelClickListener {
        fun onItemClick(k: KurzModel)
    }

    class CatViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val catTextView: TextView
        init {
            catTextView = view.findViewById(R.id.catTextView)
        }
    }

    class KurzViewHolder(view: View): RecyclerView.ViewHolder(view) {
        val nazovMenyTextview: TextView
        val skratkaMenyTextview: TextView
        val vlajkaImageView: ImageView
        val karta: MaterialCardView
        val rastTextView: TextView
        init {
            nazovMenyTextview = view.findViewById(R.id.nazovMenyTextviewK)
            skratkaMenyTextview = view.findViewById(R.id.skratkaMenyTextviewK)
            vlajkaImageView = view.findViewById(R.id.vlajkaImageviewK)
            karta = view.findViewById(R.id.itemKartaK)
            rastTextView = view.findViewById(R.id.rastTextView)
        }
        fun bind(listener: KurzModelClickListener, mena: KurzModel) {
            karta.setOnClickListener {
                listener.onItemClick(mena)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view0 = LayoutInflater.from(parent.context).inflate(R.layout.kurzy_item_cat, parent, false)
        val view1 = LayoutInflater.from(parent.context).inflate(R.layout.kurzy_item, parent, false)

        if(viewType == 0) {
            return CatViewHolder(view0)
        }
        else {
            return KurzViewHolder(view1)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder.itemViewType == 0) {
            if(position == 0) {
                (holder as CatViewHolder).catTextView.text = "Obľúbené" //TODO: Zmenit
            } else {
                (holder as CatViewHolder).catTextView.text = "Ostatné"
            }
        } else {
            var pos = if(position > oblubene + 1) position - 2 else position - 1
            (holder as KurzViewHolder).skratkaMenyTextview.text = meny[pos].mena.skratka
            (holder as KurzViewHolder).nazovMenyTextview.text = meny[pos].mena.slovenskyNazov
            (holder as KurzViewHolder).rastTextView.text = DecimalFormat("##.##%").format(meny[pos].percento)
            (holder as KurzViewHolder).vlajkaImageView.setImageDrawable(vlajky[pos])

            (holder as KurzViewHolder).rastTextView.setTextColor(farby[meny[pos].pohyb]!!)
            (holder as KurzViewHolder).rastTextView.compoundDrawableTintList = ColorStateList.valueOf(farby[meny[pos].pohyb]!!)

                    //https://stackoverflow.com/questions/30938620/android-button-drawable-tint
            //(holder as KurzViewHolder).vlajkaImageView.setImageDrawable(vlajky[position])
            //holder.bind(listener, data[position])
            (holder as KurzViewHolder).bind(listener, meny[pos])
        }

    }

    override fun getItemCount(): Int {
        return meny.size + 2
    }

    //private var pocetVratenychCat = 0

    override fun getItemViewType(position: Int): Int {
        if((position == 0) or (position == oblubene + 1)) {
            //pocetVratenychCat++
            return 0
        }
        else return 1
    }
}
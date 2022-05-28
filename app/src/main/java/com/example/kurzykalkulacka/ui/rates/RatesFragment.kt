package com.example.kurzykalkulacka.ui.rates

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kurzykalkulacka.R
import com.example.kurzykalkulacka.databinding.FragmentDashboardBinding
import com.example.kurzykalkulacka.ui.DetailActivity
import com.example.kurzykalkulacka.ui.KurzModel
import com.example.kurzykalkulacka.ui.KurzyAdapter
import com.murgupluoglu.flagkit.FlagKit
import dagger.hilt.android.AndroidEntryPoint
import org.intellij.lang.annotations.Language
import java.util.*

@AndroidEntryPoint
class RatesFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val vm: DashboardViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val startForResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                val idMeny = it.data?.getStringExtra("idMeny")
                val zmeniloSa = it.data?.getBooleanExtra("zmeniloSa", false) ?: false
                val novaOblubenost = it.data?.getBooleanExtra("oblubenost", false) ?: false
                Log.wtf("prijatie zmeny", listOf(idMeny, zmeniloSa, novaOblubenost).toString())
                if (zmeniloSa) {
                    //(vm.zoznamKurzModel as MutableLiveData<KurzModel>).value =
                    var m: List<KurzModel>? = vm.zoznamKurzModel.value?.map { km ->
                        if (km.mena.skratka == idMeny) {
                            km.mena.oblubena = novaOblubenost
                        }
                        km
                    }
                    m = m?.sortedByDescending {
                        it.mena.oblubena
                    }
                    (vm.zoznamKurzModel as MutableLiveData<List<KurzModel>>).value = m
                    if(novaOblubenost) {
                        vm.oblubeneMeny++
                    }
                    else {
                        vm.oblubeneMeny--
                    }
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(DashboardViewModel::class.java)

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.kurzyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        var prveNacitanie = true
        vm.zoznamKurzModel.observe(viewLifecycleOwner) {
            if(prveNacitanie) {
                val farby = mapOf(
                    KurzModel.Pohyb.POKLES to ContextCompat.getColor(
                        requireContext(),
                        R.color.trending_down
                    ),
                    KurzModel.Pohyb.RAST to ContextCompat.getColor(
                        requireContext(),
                        R.color.trending_up
                    ),
                    KurzModel.Pohyb.STAG to ContextCompat.getColor(
                        requireContext(),
                        R.color.purple_700
                    )
                )
                val vlajky = it.map { km ->
                    FlagKit.getDrawable(requireContext(), km.mena.skratka.substring(0, 2))
                }

                for(i in it.indices) {
                    vm.zoznamKurzModel.value?.get(i)?.vlajka = vlajky[i]
                }

                binding.kurzyRecyclerView.adapter = KurzyAdapter(
                    vm.zoznamKurzModel.value!!,
                    vm.oblubeneMeny,
                    farby,
                    object : KurzyAdapter.KurzModelClickListener {
                        override fun onItemClick(k: KurzModel) {
                            val i = Intent(requireContext(), DetailActivity::class.java)
                            i.putExtra("idMeny", k.mena.skratka)
                            startForResult.launch(i)
                        }

                    },
                    Pair(getString(R.string.favorites), getString(R.string.other))
                    )
                prveNacitanie = false
            }
            else {
                (binding.kurzyRecyclerView.adapter as KurzyAdapter).oblubene = vm.oblubeneMeny
                (binding.kurzyRecyclerView.adapter as KurzyAdapter).meny = vm.zoznamKurzModel.value!!
                (binding.kurzyRecyclerView.adapter as KurzyAdapter).notifyDataSetChanged()
            }
        }

    }
}
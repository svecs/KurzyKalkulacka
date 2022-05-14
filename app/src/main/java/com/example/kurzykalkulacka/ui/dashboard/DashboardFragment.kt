package com.example.kurzykalkulacka.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kurzykalkulacka.R
import com.example.kurzykalkulacka.databinding.FragmentDashboardBinding
import com.example.kurzykalkulacka.ui.DetailActivity
import com.example.kurzykalkulacka.ui.KurzModel
import com.example.kurzykalkulacka.ui.KurzyAdapter
import com.murgupluoglu.flagkit.FlagKit
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val vm: DashboardViewModel by viewModels()

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

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
        vm.zoznamKurzModel.observe(viewLifecycleOwner) {
            val farby = mapOf(
                KurzModel.Pohyb.POKLES to ContextCompat.getColor(requireContext(), R.color.trending_down),
                KurzModel.Pohyb.RAST to ContextCompat.getColor(requireContext(), R.color.trending_up),
                KurzModel.Pohyb.STAG to ContextCompat.getColor(requireContext(), R.color.purple_700)
            )
            val vlajky = it.map { km ->
                FlagKit.getDrawable(requireContext(), km.mena.skratka.substring(0,2))
            }
            binding.kurzyRecyclerView.adapter = KurzyAdapter(it, vm.oblubeneMeny, vlajky, farby, object : KurzyAdapter.KurzModelClickListener{
                override fun onItemClick(k: KurzModel) {
                    val i = Intent(requireContext(), DetailActivity::class.java)
                    i.putExtra("idMeny", k.mena.skratka)
                    startActivity(i)
                }

            })
        }
    }
}
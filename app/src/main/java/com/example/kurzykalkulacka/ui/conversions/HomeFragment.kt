package com.example.kurzykalkulacka.ui.conversions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.viewModelScope
import com.example.kurzykalkulacka.MainActivity
import com.example.kurzykalkulacka.PrevodModel
import com.example.kurzykalkulacka.R
import com.example.kurzykalkulacka.databinding.FragmentHomeBinding
import com.example.kurzykalkulacka.ui.MainActivityViewModel
import com.example.kurzykalkulacka.ui.VyberMeny
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.murgupluoglu.flagkit.FlagKit
import dagger.hilt.android.AndroidEntryPoint
import java.text.NumberFormat
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val prevodyViewModel: PrevodyViewModel by viewModels()
    private val mainActivityViewModel: MainActivityViewModel by viewModels ({
        requireActivity()
    })

    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == Activity.RESULT_OK) {
            val idMeny = it.data?.getStringExtra("menaId")
            val poradie = it.data?.getIntExtra("poradie", -1)
            Log.e("ID zvolenej meny", idMeny.toString())
            Log.e("Poradie chipu", poradie.toString())
            //Toast.makeText(context, "IT data: $idMeny", Toast.LENGTH_SHORT).show()
            val sp: SharedPreferences = requireContext().getSharedPreferences("data", Context.MODE_PRIVATE)
            val pref: SharedPreferences.Editor = sp.edit()
            if(poradie == 0) {
                pref.putString("mena0", idMeny.toString())
            }
            else pref.putString("mena1", idMeny.toString())
            pref.commit()
            if(poradie == 0) {
                //prevodyViewModel.idMenyA.value = idMeny
                prevodyViewModel.upravKurzA(idMeny!!)
                //revodyViewModel.aktualizujPrevodyZhoraDole()
            }
            else {
                //prevodyViewModel.idMenyB.value = idMeny
                //binding.secondChip.chipIcon = FlagKit.getDrawable(requireContext(), idMeny!!.substring(0,2))
                //binding.secondChip.text = idMeny!!
                prevodyViewModel.upravKurzB(idMeny!!)
                //prevodyViewModel.aktualizujPrevodyZdolaHore()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try {
            //_binding = Fra.inflate(inflater, container, false)
            _binding = FragmentHomeBinding.inflate(inflater, container, false)

            //prevodyViewModel.loadData()

            val root: View = binding.root

            prevodyViewModel.kurzA.observe(viewLifecycleOwner) {
                if(it == null) return@observe
                binding.firstChip.chipIcon = FlagKit.getDrawable(requireContext(),
                    it.idMeny.substring(0,2)
                )
                binding.firstChip.text = it.idMeny
                prevodyViewModel.aktualizujPrevodyZhoraDole()
            }

            prevodyViewModel.kurzB.observe(viewLifecycleOwner) {
                if(it == null) return@observe
                binding.secondChip.chipIcon = FlagKit.getDrawable(requireContext(),
                    it.idMeny.substring(0,2)
                )
                binding.secondChip.text = it.idMeny
                prevodyViewModel.aktualizujPrevodyZdolaHore()
            }

            binding.firstChip.setOnClickListener {
                val i = Intent(this.requireContext(), VyberMeny::class.java)
                i.putExtra("poradie", 0)
                startForResult.launch(i)
            }

            binding.secondChip.setOnClickListener {
                val i = Intent(this.requireContext(), VyberMeny::class.java)
                i.putExtra("poradie", 1)
                startForResult.launch(i)
            }

            return root
        } catch (e: Exception) {
            Log.e("FRAGMENT_CHYBA", e.toString())
            throw e
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private var firstEventLock: Boolean = false
    private var secondEventLock: Boolean = false

    private var firstObserveLock: Boolean = false
    private var secondObserveLock: Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sp: SharedPreferences = requireContext().getSharedPreferences("data", Context.MODE_PRIVATE)
        prevodyViewModel.firstVal.observe(viewLifecycleOwner, Observer {
            if(!firstObserveLock) {
                if(it == 0.0) binding.firstValEditText.setText("")
                else {
                    val str: String = "%.2f".format(it)
                    binding.firstValEditText.setText(str)
                }
            } else {
                firstObserveLock = false
            }
        })
        prevodyViewModel.secondVal.observe(viewLifecycleOwner, Observer {
            if(!secondObserveLock) {
                if(it == 0.0) binding.secondValEditText.setText("")
                else {
                    val str: String = "%.2f".format(it)
                    binding.secondValEditText.setText(str)
                }
            } else {
                secondObserveLock = false
            }
        })



        binding.firstValEditText.showSoftInputOnFocus = false
        binding.secondValEditText.showSoftInputOnFocus = false
       // https://stackoverflow.com/questions/10636635/disable-keyboard-on-edittext


        binding.b0.setOnClickListener { stlacKlaves('0') }
        binding.b1.setOnClickListener { stlacKlaves('1') }
        binding.b2.setOnClickListener { stlacKlaves('2') }
        binding.b3.setOnClickListener { stlacKlaves('3') }
        binding.b4.setOnClickListener { stlacKlaves('4') }
        binding.b5.setOnClickListener { stlacKlaves('5') }
        binding.b6.setOnClickListener { stlacKlaves('6') }
        binding.b7.setOnClickListener { stlacKlaves('7') }
        binding.b8.setOnClickListener { stlacKlaves('8') }
        binding.b9.setOnClickListener { stlacKlaves('9') }
        binding.bdot.setOnClickListener { stlacKlaves(',') }
        binding.bback.setOnClickListener { stlacKlaves('-') }
        binding.bback.setOnLongClickListener {
            if(binding.firstValEditText.hasFocus()) {
                binding.firstValEditText.setText("")
            }
            else if(binding.secondValEditText.hasFocus()) {
                binding.secondValEditText.setText("")
            }
            true
        }


        binding.firstValEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if(firstEventLock) {
                    firstEventLock = false;
                    return;
                }
                firstObserveLock = true //zamkneme prvy observer
                secondObserveLock = false
                secondEventLock = true
                Log.e("TXTWATCHER-FIRST",p0.toString())
                val nf: NumberFormat = NumberFormat.getInstance(Locale.getDefault())
                val n: Number = nf.parse(if(p0 == null || p0.isEmpty()) "0" else p0.toString())
                val hodnota: Double = n.toDouble()
                prevodyViewModel.firstVal.value = hodnota
                prevodyViewModel.aktualizujPrevodyZhoraDole()
            }
        })
        binding.secondValEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if(secondEventLock) {
                    secondEventLock = false
                    return;
                }
                secondObserveLock = true
                firstObserveLock = false
                firstEventLock = true
                Log.e("TXTWATCHER-SECOND",p0.toString())
                val nf: NumberFormat = NumberFormat.getInstance(Locale.getDefault())
                val n: Number = nf.parse(if(p0 == null || p0.isEmpty()) "0" else p0.toString())
                val hodnota: Double = n.toDouble()
                prevodyViewModel.secondVal.value = hodnota
                prevodyViewModel.aktualizujPrevodyZdolaHore()
            }

        })


        /*val poslednaAktualizacia: String? = sp.getString("poslednaAktualizacia", null)
        val dnesnyDatum = PrevodModel.dnesnyDatumString()
        if(!poslednaAktualizacia.isNullOrEmpty() and (dnesnyDatum != poslednaAktualizacia)) {*/
        //prevodyViewModel.stiahniData()
            /*val pref: SharedPreferences.Editor = sp.edit()
            pref.putString("poslednaAktualizacia", dnesnyDatum)
            pref.commit()
        }*/

        //prevodyViewModel.upravKurzA(sp.getString("mena0", null) ?: "USD")
        //prevodyViewModel.upravKurzB(sp.getString("mena1", null) ?: "CZK")

        //Log.wtf("HFdpF", mainActivityViewModel.dataPrisli.value!!.toString())

        mainActivityViewModel.dataPrisli.observe(activity as MainActivity) {
            Log.wtf("TTPK", it.toString())
            if(it) {
                prevodyViewModel.upravKurzA(sp.getString("mena0", null))
                prevodyViewModel.upravKurzB(sp.getString("mena1", null))

                /*binding.firstChip.chipIcon = FlagKit.getDrawable(this.requireContext(), prevodyViewModel.kurzA.value!!.idMeny.substring(0,2))
                binding.secondChip.chipIcon = FlagKit.getDrawable(this.requireContext(), prevodyViewModel.kurzB.value!!.idMeny.substring(0,2))

                binding.firstChip.text = prevodyViewModel.kurzA.value!!.idMeny
                binding.secondChip.text = prevodyViewModel.kurzB.value!!.idMeny*/

                binding.loadingCL.visibility = View.GONE
                binding.kalkulackaLL.visibility = View.VISIBLE
            }
        }

        mainActivityViewModel.chybaPripojenia.observe(activity as MainActivity) {
            Log.wtf("CHPR", it.toString())
            if(!mainActivityViewModel.razZavolane) {
                mainActivityViewModel.razZavolane = true
                return@observe
            }
            if(it) {
                mainActivityViewModel.jePrazdnaTabulka()
                /*prevodyViewModel.upravKurzA(sp.getString("mena0", null) ?: "USD")
                prevodyViewModel.upravKurzB(sp.getString("mena1", null) ?: "CZK")

                binding.firstChip.chipIcon = FlagKit.getDrawable(this.requireContext(), prevodyViewModel.kurzA.value!!.idMeny.substring(0,2))
                binding.secondChip.chipIcon = FlagKit.getDrawable(this.requireContext(), prevodyViewModel.kurzB.value!!.idMeny.substring(0,2))

                binding.firstChip.text = prevodyViewModel.kurzA.value!!.idMeny
                binding.secondChip.text = prevodyViewModel.kurzB.value!!.idMeny*/

                //binding.loadingCL.visibility = View.GONE
                //binding.kalkulackaLL.visibility = View.VISIBLE
            }
            else {
                //prevodyViewModel.upravKurzA(sp.getString("mena0", null))
                //prevodyViewModel.upravKurzB(sp.getString("mena1", null))

                /*binding.firstChip.chipIcon = FlagKit.getDrawable(this.requireContext(), prevodyViewModel.kurzA.value!!.idMeny.substring(0,2))
                binding.secondChip.chipIcon = FlagKit.getDrawable(this.requireContext(), prevodyViewModel.kurzB.value!!.idMeny.substring(0,2))

                binding.firstChip.text = prevodyViewModel.kurzA.value!!.idMeny
                binding.secondChip.text = prevodyViewModel.kurzB.value!!.idMeny*/
            }
        }

        mainActivityViewModel.tabulkaPrazdna.observe(activity as MainActivity) {
            if(it) {
                Log.wtf("TabPr", "Tabulka je prazdna")
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle(getString(R.string.empty_tab_header))
                    .setMessage(getString(R.string.empty_tab_message))
                    .setPositiveButton(getString(R.string.empty_tab_accept)) { dialog, which ->
                    }
                    .setOnDismissListener {
                        (activity as MainActivity).finishAndRemoveTask()
                    }
                    .show()
            } else {
                prevodyViewModel.upravKurzA(sp.getString("mena0", null))
                prevodyViewModel.upravKurzB(sp.getString("mena1", null))

                binding.loadingCL.visibility = View.GONE
                binding.kalkulackaLL.visibility = View.VISIBLE
            }
        }

        prevodyViewModel.nasobok.observe(viewLifecycleOwner) {
            if(mainActivityViewModel.dataPrisli.value!! xor mainActivityViewModel.chybaPripojenia.value!!) {
                Log.wtf("nasobok", it.toString())
                if(it <= 0) return@observe
                val fm: String = prevodyViewModel.kurzA.value!!.idMeny
                var nsb: String = "%.2f".format(prevodyViewModel.nasobok.value)
                val sm: String = prevodyViewModel.kurzB.value!!.idMeny
                val datum: String = PrevodModel.slovenskyDatum(prevodyViewModel.kurzA.value!!.datum)
                binding.kurzText.text = "1 $fm = $nsb $sm\n(k $datum)"
            }
        }

        binding.swapButton.setOnClickListener {
            binding.firstChip.chipIcon = FlagKit.getDrawable(requireContext(), prevodyViewModel.kurzB.value!!.idMeny.substring(0,2))
            binding.firstChip.text = prevodyViewModel.kurzB.value!!.idMeny
            binding.secondChip.chipIcon = FlagKit.getDrawable(requireContext(), prevodyViewModel.kurzA.value!!.idMeny.substring(0,2))
            binding.secondChip.text = prevodyViewModel.kurzA.value!!.idMeny
            prevodyViewModel.swapniKurzy()
        }
    }

    fun stlacKlaves(znak: Char) {
        Log.wtf("HFdpF", mainActivityViewModel.dataPrisli.value!!.toString())
        if(binding.firstValEditText.hasFocus()) {
            if(znak == '-') {
                if((binding.firstValEditText.text?.length ?: 0) == 0) return
                binding.firstValEditText.setText(binding.firstValEditText.text?.dropLast(1))
                binding.firstValEditText.setSelection(binding.firstValEditText.text?.length ?: 0)
            }
            else if(znak == ',') {
                if((binding.firstValEditText.text?.length ?: 0) == 0) return
                if(binding.firstValEditText.text?.toString()?.contains(',') == true) return
                binding.firstValEditText.text?.append(',')
            }
            else {
                binding.firstValEditText.text?.append(znak)
            }
        }
        else if(binding.secondValEditText.hasFocus()) {
            if(znak == '-') {
                if((binding.secondValEditText.text?.length ?: 0) == 0) return
                binding.secondValEditText.setText(binding.secondValEditText.text?.dropLast(1))
                binding.secondValEditText.setSelection(binding.secondValEditText.text?.length ?: 0)
            }
            else if(znak == ',') {
                if((binding.secondValEditText.text?.length ?: 0) == 0) return
                if(binding.secondValEditText.text?.toString()?.contains(',') == true) return
                binding.secondValEditText.text?.append(',')
            }
            else {
                binding.secondValEditText.text?.append(znak)
            }
        }
    }
}
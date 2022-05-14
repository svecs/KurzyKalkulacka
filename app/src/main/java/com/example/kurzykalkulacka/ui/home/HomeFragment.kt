package com.example.kurzykalkulacka.ui.home

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
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
import com.example.kurzykalkulacka.PrevodModel
import com.example.kurzykalkulacka.databinding.FragmentHomeBinding
import com.example.kurzykalkulacka.ui.VyberMeny
import com.murgupluoglu.flagkit.FlagKit
import dagger.hilt.android.AndroidEntryPoint
import java.lang.NumberFormatException
import java.text.NumberFormat
import java.util.*
import java.util.prefs.Preferences

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val prevodyViewModel: PrevodyViewModel by viewModels()

    private var dataNacitane: Boolean = false;

    val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if(it.resultCode == Activity.RESULT_OK) {
            val idMeny = it.data?.getStringExtra("menaId")
            val poradie = it.data?.getIntExtra("poradie", -1)
            Log.e("ID zvolenej meny", idMeny.toString())
            Log.e("Poradie chipu", poradie.toString())
            Toast.makeText(context, "IT data: $idMeny", Toast.LENGTH_SHORT).show()
            val sp: SharedPreferences = requireContext().getSharedPreferences("data", Context.MODE_PRIVATE)
            val pref: SharedPreferences.Editor = sp.edit()
            if(poradie == 0) {
                pref.putString("mena0", idMeny.toString())
            }
            else pref.putString("mena1", idMeny.toString())
            pref.commit()
            if(poradie == 0) {
                //prevodyViewModel.idMenyA.value = idMeny
                binding.firstChip.chipIcon = FlagKit.getDrawable(requireContext(), idMeny!!.substring(0,2))
                binding.firstChip.text = idMeny!!
                prevodyViewModel.upravKurzA(idMeny!!)
                prevodyViewModel.aktualizujPrevodyZhoraDole()
            }
            else {
                //prevodyViewModel.idMenyB.value = idMeny
                binding.secondChip.chipIcon = FlagKit.getDrawable(requireContext(), idMeny!!.substring(0,2))
                binding.secondChip.text = idMeny!!
                prevodyViewModel.upravKurzB(idMeny!!)
                prevodyViewModel.aktualizujPrevodyZdolaHore()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try {
            _binding = FragmentHomeBinding.inflate(inflater, container, false)

            prevodyViewModel.loadData()

            val root: View = binding.root



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

        binding.firstValEditText.setOnTouchListener { view, motionEvent ->
            view.onTouchEvent(motionEvent)
            val im: InputMethodManager? = (view.context.getSystemService(Context.INPUT_METHOD_SERVICE)) as InputMethodManager?
            im?.hideSoftInputFromWindow(view.windowToken, 0)
            true
        }

        binding.secondValEditText.setOnTouchListener { view, motionEvent ->
            view.onTouchEvent(motionEvent)
            val im: InputMethodManager? = (view.context.getSystemService(Context.INPUT_METHOD_SERVICE)) as InputMethodManager?
            im?.hideSoftInputFromWindow(view.windowToken, 0)
            true
        }

        //zdroj: https://stackoverflow.com/questions/43017184/prevent-keyboard-from-popping-up-on-edittext-click

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
            prevodyViewModel.stiahniData()
            /*val pref: SharedPreferences.Editor = sp.edit()
            pref.putString("poslednaAktualizacia", dnesnyDatum)
            pref.commit()
        }*/

        prevodyViewModel.upravKurzA(sp.getString("mena0", null) ?: "USD")
        prevodyViewModel.upravKurzB(sp.getString("mena1", null) ?: "CZK")

        binding.firstChip.chipIcon = FlagKit.getDrawable(this.requireContext(), prevodyViewModel.kurzA.value!!.idMeny.substring(0,2))
        binding.secondChip.chipIcon = FlagKit.getDrawable(this.requireContext(), prevodyViewModel.kurzB.value!!.idMeny.substring(0,2))

        binding.firstChip.text = prevodyViewModel.kurzA.value!!.idMeny
        binding.secondChip.text = prevodyViewModel.kurzB.value!!.idMeny

        prevodyViewModel.nasobok.observe(viewLifecycleOwner) {
            val fm: String = prevodyViewModel.kurzA.value!!.idMeny
            var nsb: String = "%.2f".format(prevodyViewModel.nasobok.value)
            val sm: String = prevodyViewModel.kurzB.value!!.idMeny
            val datum: String = PrevodModel.slovenskyDatum(prevodyViewModel.kurzA.value!!.datum)
            binding.kurzText.text = "1 $fm = $nsb $sm\n(k $datum)"
        }
    }

    fun stlacKlaves(znak: Char) {
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
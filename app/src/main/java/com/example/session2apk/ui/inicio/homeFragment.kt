package com.example.session2apk.ui.inicio

import android.app.DatePickerDialog
import android.os.Build
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.example.session2apk.Helper.*
import com.example.session2apk.Network.CallServicesJD
import com.example.session2apk.Network.ServicesJD
import com.example.session2apk.R
import com.example.session2apk.adapter.AdapterJogo
import com.example.session2apk.databinding.FragmentHomeBinding
import org.json.JSONArray
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class homeFragment : Fragment() {

    companion object {
        fun newInstance() = homeFragment()
    }

    private lateinit var viewModel: HomeViewModel
    lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)

        FillJogo()
        binding.txtDate.text = "${Calendar.getInstance().get(Calendar.YEAR)}/" +
                "${Calendar.getInstance().get(Calendar.MONTH)}/${
                    Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
                }"
        binding.txtDate.setOnClickListener {
            var dateJD = binding.txtDate.text.toString().toDate("yyyy/MM/dd")
            DatePickerDialog(
                requireActivity(),
                { view, year, month, dayOfMonth ->
                    binding.txtDate.text = "$year/${month+1}/$dayOfMonth"
                    FillJogo()
                    Log.e("TAG", "onCreateView: $year-${month + 1}/$dayOfMonth ")

                }, dateJD.get(Calendar.YEAR),
                dateJD.get(Calendar.MONTH),
                dateJD.get(Calendar.DAY_OF_MONTH)

            ).apply {
                setButton(DatePickerDialog.BUTTON_NEUTRAL, "Clear") { _, _ ->
                    FillJogo()
                    binding.txtDate.text = "All"
                }

            }.show()
        }

        return binding.root
    }

    private fun FillJogo() {
        CallServicesJD.startQuery("api/jogoes", CallServicesJD.Companion.method.GET,
            "",
            object : ServicesJD {
                override fun Finish(response: String, status: Int) {
                    if (status == HTTP.OK) {
                        this@homeFragment.requireActivity().runOnUiThread {
                            var adapter = AdapterJogo(
                                this@homeFragment,
                                JSONArray(response).ToList(Jogo::class.java.name).Cast()
                            )
                            binding.listJogo.adapter = adapter
                            if (binding.txtDate.text.toString().length != 3) {
                                var date = binding.txtDate.text.toString().toDate("yyyy/MM/dd")
                                if (binding.listJogo.adapter is AdapterJogo) {
                                    var adapterJogo = binding.listJogo.adapter as AdapterJogo
                                    adapterJogo.list.removeIf {
                                        it.Data.split("T")[0].toDate() != ("${date.get(Calendar.YEAR)}-${
                                            date.get(
                                                Calendar.MONTH
                                            )
                                        }-" +
                                                "${date.get(Calendar.DAY_OF_MONTH)}").toDate()
                                    }
                                    adapterJogo.notifyDataSetChanged()
                                }

                            }

                        }

                    }
                }

                override fun Error(response: String, status: Int) {

                }
            })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
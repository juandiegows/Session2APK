package com.example.session2apk.ui.seleccion

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.session2apk.Helper.Cast
import com.example.session2apk.Helper.HTTP
import com.example.session2apk.Helper.ToList
import com.example.session2apk.Model.Selecion
import com.example.session2apk.Network.CallServicesJD
import com.example.session2apk.Network.ServicesJD
import com.example.session2apk.R
import com.example.session2apk.adapter.AdapterSelecion
import com.example.session2apk.databinding.FragmentSeleccionBinding
import org.json.JSONArray

class SeleccionFragment : Fragment() {

    companion object {
        fun newInstance() = SeleccionFragment()
    }

    var orderNombre = true
    private lateinit var viewModel: SeleccionViewModel
    private lateinit var binding: FragmentSeleccionBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSeleccionBinding.inflate(layoutInflater)
        CallServicesJD.startQuery("api/Selecaos", CallServicesJD.Companion.method.GET,
            "",
            object : ServicesJD {
                override fun Finish(response: String, status: Int) {
                    if (status == HTTP.OK) {
                        this@SeleccionFragment.requireActivity().runOnUiThread {
                            binding.listSeleccion.adapter = AdapterSelecion(
                                this@SeleccionFragment,
                                JSONArray(response).ToList(Selecion::class.java.name).Cast()
                            )
                        }

                    }
                }

                override fun Error(response: String, status: Int) {

                }
            }
        )
        binding.txtOrder.setOnClickListener {
            var adapter = binding.listSeleccion.adapter
            if (adapter is AdapterSelecion) {

                if (orderNombre){

                    orderNombre = false
                    binding.txtOrder.text = "Order by point"
                    adapter.list.sortByDescending {
                        it.Point
                    }
                    adapter.notifyDataSetChanged()
                }else {
                    orderNombre = true
                    binding.txtOrder.text = "Order by Name"
                    adapter.list.sortByDescending {
                        it.Nome
                    }
                    adapter.notifyDataSetChanged()
                }

            }
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SeleccionViewModel::class.java)
        // TODO: Use the ViewModel

    }

}
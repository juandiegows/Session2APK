package com.example.session2apk.ui.seleccion

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.session2apk.R

class SeleccionFragment : Fragment() {

    companion object {
        fun newInstance() = SeleccionFragment()
    }

    private lateinit var viewModel: SeleccionViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_seleccion, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SeleccionViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
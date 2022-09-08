package com.example.session2apk.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.fragment.app.Fragment
import com.example.session2apk.Helper.toImage
import com.example.session2apk.Model.Selecion
import com.example.session2apk.R
import com.example.session2apk.databinding.ItemSeleccionBinding

class AdapterSelecion(val fragment: Fragment, val list: ArrayList<Selecion>) : BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list.get(position)
    }

    override fun getItemId(position: Int): Long {
        return list.get(position).Id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
       var binding: ItemSeleccionBinding = ItemSeleccionBinding.bind(LayoutInflater.from(fragment.requireContext()).inflate(
           R.layout.item_seleccion, parent, false))
        var selecion = list.get(position)
        with(binding){
            with(selecion){
                txtPoint.text = Point.toString()
                txtSeleccion.text = Nome
                img.setImageBitmap(Bandeira.toImage())
            }
        }
        return  binding.root
    }
}
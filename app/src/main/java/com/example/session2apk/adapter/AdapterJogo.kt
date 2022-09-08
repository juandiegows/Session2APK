package com.example.session2apk.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.fragment.app.Fragment
import com.example.session2apk.Helper.Jogo
import com.example.session2apk.Helper.toImage
import com.example.session2apk.R
import com.example.session2apk.databinding.ItemPartidoBinding

class AdapterJogo(val fragment: Fragment, var list:ArrayList<Jogo>) :BaseAdapter() {

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list.get(position)
    }

    override fun getItemId(position: Int): Long {
        return  list.get(position).Id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var binding:ItemPartidoBinding = ItemPartidoBinding.bind(LayoutInflater.from(fragment.requireContext()).inflate(
            R.layout.item_partido, parent,false
        ))

        var jogo = list.get(position)
        with(binding){
            with(jogo){
                txtGoal1.text = GoalH
                txtGoal2.text = GoalV
                txtTeam1.text = Home
                txtTeam2.text = Visit
                imgL.setImageBitmap(HomeB.toImage())
                imgV.setImageBitmap(VisitB.toImage())
                txtDateJogo.text = Data.replace("T", " ")
                txtEstadio.text = Estadio
            }
        }

        return  binding.root
    }
}
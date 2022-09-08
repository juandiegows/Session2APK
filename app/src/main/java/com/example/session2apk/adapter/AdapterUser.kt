package com.example.session2apk.adapter

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.provider.CalendarContract
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.session2apk.Helper.AlertOK
import com.example.session2apk.Helper.HTTP
import com.example.session2apk.Helper.Singleton
import com.example.session2apk.Helper.toJson
import com.example.session2apk.Model.User
import com.example.session2apk.Network.CallServicesJD
import com.example.session2apk.Network.ServicesJD
import com.example.session2apk.R
import com.example.session2apk.databinding.ItemUserBinding

class AdapterUser(val fragment: Fragment, val listUser: ArrayList<User>) : BaseAdapter() {
    override fun getCount(): Int {
        return listUser.size
    }

    override fun getItem(position: Int): Any {
        return listUser.get(position)
    }

    override fun getItemId(position: Int): Long {
        return listUser.get(position).Id.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var binding: ItemUserBinding = ItemUserBinding.bind(
            LayoutInflater.from(fragment.requireContext())
                .inflate(R.layout.item_user, parent, false)
        )
        var user = listUser.get(position)
        with(binding) {
            with(user) {
                txtApelido.text = Apelido
                txtEmailListUser.text = Email
                if (Bloqueado) {
                    txtEstado.text = "Disabled"
                    txtEstado.setTextColor(Color.RED)
                    txtApelido.setTextColor(Color.RED)
                    txtEmailListUser.setTextColor(Color.RED)
                } else {
                    txtEstado.setTextColor(Color.BLUE)
                    txtApelido.setTextColor(Color.BLUE)
                    txtEmailListUser.setTextColor(Color.BLUE)
                    txtEstado.text = "Enabled"
                }
                btnLockUnlock.setImageResource(
                    if (Bloqueado) {

                        R.drawable.desbloqueia
                    }
                    else{

                        R.drawable.block
                    }

                )
                btnEditlistUser.setOnClickListener {
                    Singleton.userLoginEdit = user
                    Singleton.statusRegister = Singleton.STAT.UPDATE
                    Navigation.findNavController(
                        fragment.requireActivity(),
                        R.id.nav_host_fragment_content_main
                    )
                        .navigate(R.id.registerFragment)
                }
                btnDelete.setOnClickListener {
                    AlertDialog.Builder(fragment.requireContext())
                        .setTitle("Question")
                        .setMessage("Are you sure you want to apply the changes? (Delete)")
                        .setPositiveButton("yes, sure") { _, _ ->
                            user.Bloqueado = !user.Bloqueado
                            CallServicesJD.startQuery("api/usuarios/${user.Id}",
                                CallServicesJD.Companion.method.DELETE,
                                "",
                                object : ServicesJD {
                                    override fun Finish(response: String, status: Int) {
                                        fragment.requireActivity().runOnUiThread {
                                            if (status == HTTP.OK) {
                                                fragment.requireContext().AlertOK(
                                                    "success",
                                                    "the changes were applied successful (Delete)"
                                                )
                                              listUser.remove(user)
                                                this@AdapterUser.notifyDataSetChanged()
                                            } else {
                                                fragment.requireContext().AlertOK(
                                                    "error",
                                                    "the changes were not applied\n $status $response"
                                                )
                                            }
                                            Log.e("Finish", "Finish: $status $response")
                                        }

                                    }

                                    override fun Error(response: String, status: Int) {
                                        fragment.requireActivity().runOnUiThread {
                                            fragment.requireContext().AlertOK(
                                                "Error",
                                                "the changes were not applied\n $status $response"
                                            )
                                            Log.e("Finish", "Finish: $status $response")
                                        }

                                    }
                                }
                            )

                        }
                        .setNegativeButton("no") { _, _ -> }
                        .create().show()
                }
                btnLockUnlock.setOnClickListener {

                    val action = if (user.Bloqueado)
                        "it will enable"
                    else
                        "it will disabled"

                    AlertDialog.Builder(fragment.requireContext())
                        .setTitle("Question")
                        .setMessage("Are you sure you want to apply the changes? ($action)")
                        .setPositiveButton("yes, sure") { _, _ ->
                            user.Bloqueado = !user.Bloqueado
                            CallServicesJD.startQuery("api/usuarios/${user.Id}",
                                CallServicesJD.Companion.method.PUT,
                                user.toJson(),
                                object : ServicesJD {
                                    override fun Finish(response: String, status: Int) {
                                        fragment.requireActivity().runOnUiThread {
                                            if (status == HTTP.UPDATE) {
                                                fragment.requireContext().AlertOK(
                                                    "success",
                                                    "the changes were applied successful"
                                                )
                                                this@AdapterUser.notifyDataSetChanged()
                                                btnLockUnlock.setImageResource(
                                                    if (Bloqueado)
                                                        R.drawable.desbloqueia
                                                    else
                                                        R.drawable.block,
                                                )
                                            } else {
                                                fragment.requireContext().AlertOK(
                                                    "error",
                                                    "the changes were not applied\n $status $response"
                                                )
                                            }
                                            Log.e("Finish", "Finish: $status $response")
                                        }

                                    }

                                    override fun Error(response: String, status: Int) {
                                        fragment.requireActivity().runOnUiThread {
                                            fragment.requireContext().AlertOK(
                                                "Error",
                                                "the changes were not applied\n $status $response"
                                            )
                                            Log.e("Finish", "Finish: $status $response")
                                        }

                                    }
                                }
                            )

                        }
                        .setNegativeButton("no") { _, _ -> }
                        .create().show()
                }
            }
        }

        return binding.root
    }
}
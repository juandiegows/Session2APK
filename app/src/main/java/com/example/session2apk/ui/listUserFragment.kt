package com.example.session2apk.ui

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.session2apk.Helper.Cast
import com.example.session2apk.Helper.HTTP
import com.example.session2apk.Helper.Singleton
import com.example.session2apk.Helper.ToList
import com.example.session2apk.Model.User
import com.example.session2apk.Network.CallServicesJD
import com.example.session2apk.Network.ServicesJD
import com.example.session2apk.R
import com.example.session2apk.adapter.AdapterUser
import com.example.session2apk.databinding.FragmentListUserBinding
import org.json.JSONArray

class listUserFragment : Fragment() {

    companion object {
        fun newInstance() = listUserFragment()
    }

    lateinit var binding: FragmentListUserBinding

    private lateinit var viewModel: ListUserViewModel

    init {

        Singleton.statusRegister = Singleton.STAT.REGISTER
    }

    override fun onResume() {
        Singleton.statusRegister = Singleton.STAT.REGISTER
        super.onResume()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListUserBinding.inflate(layoutInflater)
        init()

        return binding.root
    }

    private fun init() {
        CallServicesJD.startQuery("api/Usuarios", CallServicesJD.Companion.method.GET, "",
            object : ServicesJD {
                override fun Finish(response: String, status: Int) {
                    if (status == HTTP.OK) {
                        this@listUserFragment.requireActivity().runOnUiThread{
                            binding.listUser.adapter = AdapterUser(
                                this@listUserFragment,
                                JSONArray(response).ToList(User::class.java.name).Cast()
                            )
                        }

                    }
                }

                override fun Error(response: String, status: Int) {

                }
            })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListUserViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
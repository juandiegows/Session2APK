package com.example.session2apk

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.example.session2apk.Helper.*
import com.example.session2apk.Model.User
import com.example.session2apk.Network.CallServicesJD
import com.example.session2apk.Network.ServicesJD
import com.example.session2apk.databinding.FragmentRegisterBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    lateinit var listView: List<View>


    fun saveUsuario(user: User) {
        if(Singleton.statusRegister == Singleton.STAT.REGISTER || Singleton.statusRegister == Singleton.STAT.ADD){
            CallServicesJD.startQuery("api/usuarios",
                CallServicesJD.Companion.method.POST,
                user.toJson(),
                object : ServicesJD {
                    override fun Finish(response: String, status: Int) {
                        requireActivity().runOnUiThread {
                            if (status == HTTP.CREATED) {
                                requireContext().AlertOK(
                                    "cambios aplicados correctamente",
                                    "Se ha registrado un nuevo usuario"
                                )
                            } else {
                                requireContext().AlertOK(
                                    "no se ha guardado los cambios",
                                    "$status $response"
                                )
                            }
                            Log.e("Finish", "Finish: $status $response")
                        }

                    }

                    override fun Error(response: String, status: Int) {
                        requireActivity().runOnUiThread {
                            requireContext().AlertOK(
                                "no se ha guardado los cambios",
                                "$status $response"
                            )
                            Log.e("Finish", "Finish: $status $response")
                        }

                    }
                }
            )
        }else {
            CallServicesJD.startQuery("api/usuarios/${Singleton.userLogin.Id}",
                CallServicesJD.Companion.method.PUT,
                user.toJson(),
                object : ServicesJD {
                    override fun Finish(response: String, status: Int) {
                        requireActivity().runOnUiThread {
                            if (status == HTTP.CREATED) {
                                requireContext().AlertOK(
                                    "cambios aplicados correctamente",
                                    "Se ha actualizado"
                                )
                            } else {
                                requireContext().AlertOK(
                                    "no Se ha actualizado los cambios",
                                    "$status $response"
                                )
                            }
                            Log.e("Finish", "Finish: $status $response")
                        }

                    }

                    override fun Error(response: String, status: Int) {
                        requireActivity().runOnUiThread {
                            requireContext().AlertOK(
                                "no se ha actualizado los cambios",
                                "$status $response"
                            )
                            Log.e("Finish", "Finish: $status $response")
                        }

                    }
                }
            )
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        if (Singleton.statusRegister != Singleton.STAT.REGISTER)
            binding.buttonSecond.isVisible = false
        if (Singleton.statusRegister != Singleton.STAT.ADD)
            binding.btnRegister.setText("Add")
        else if (Singleton.statusRegister != Singleton.STAT.UPDATE){
            with (binding){
                btnRegister.setText("Update")
                with(Singleton.userLogin){
                    txtEmail.TextJD = Email
                    txtpass.TextJD = Senha
                    txtCPass.TextJD = Senha
                    txtSurName.TextJD = Apelido
                    txtUser.TextJD = Nome
                    ckAdmin.isChecked = Perfil.lowercase() == "Administrador".lowercase()
                }

            }
        }

        else
            binding.btnRegister.setText("Register")

        with(binding) {
            txtUser.Requerido(txtSUser, 3)
            txtSurName.Requerido(txtSSurName, 3)
            txtpass.Requerido(txtSPass, 3)
            txtCPass.EqualsTo(txtSCPass, txtpass)
            txtEmail.IsEmail(txtSEmail)
            listView = listOf(txtSUser, txtSPass, txtSEmail, txtSCPass, txtSSurName)
        }
        binding.btnRegister.setOnClickListener {
            if (listView.ISCorrecto()) {
                AlertDialog.Builder(requireContext())
                    .setTitle("Alert")
                    .setMessage("Do you want apply in the database? ")
                    .setPositiveButton("yes, Save") { _, _ ->
                        with(binding) {
                            saveUsuario(User().apply {
                                Id = 0
                                Apelido = txtSurName.TextJD
                                Bloqueado = false
                                Email = txtEmail.TextJD
                                Nome = txtUser.TextJD

                                if (ckAdmin.isChecked) {
                                    Perfil = "Administrador"
                                } else {
                                    Perfil = "Usuario"
                                }
                                Senha = txtpass.TextJD


                            })
                        }

                    }
                    .setNegativeButton("No") { _, _ ->

                    }.create().show()
            } else {
                requireContext().AlertOK("all field are required", "please introduce all field")
            }
        }
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
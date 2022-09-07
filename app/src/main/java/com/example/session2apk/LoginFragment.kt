package com.example.session2apk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.session2apk.Helper.*
import com.example.session2apk.Model.Login
import com.example.session2apk.Model.User
import com.example.session2apk.Network.CallServicesJD
import com.example.session2apk.Network.ServicesJD
import com.example.session2apk.databinding.FragmentLoginBinding
import org.json.JSONObject

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    init {
        var texto = "asdadA34.hghjh8jjhk@"
        var patternsJD = "^[a-zA-Z]{1}+[a-zA-Z0-9]{3,15}+[._-]{1}+[a-zA-Z0-9]{0,15}+@"
        var regex= Regex(patternsJD)

        val matched1 = regex.matches(input = "Yabcdy-@")
        val matched2 = regex.matches(input = "bcdyabScd_@")
        val matched3 = regex.matches(input = "abcd..@")
        val matched4 = regex.matches(texto)
        println("1) $matched1")
        println("2) $matched2")
        println("3) $matched3")
        println("4) $matched4")
        Log.e(
            "Result", "Text : $texto\n" +
                    "Pattern : $patternsJD \n" +
                    "Valido = ${regex.matches(texto)}\n" +
                    "Patterns esperando : ${texto.toRegex()}"
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.txtSUser.animate().translationY(-1500f).setDuration(2000).withStartAction {
            binding.txtSPassword.animate().translationY(-1500f).setDuration(2000).withStartAction {
                binding.txtSPassword.animate().translationY(-1500f).setDuration(2000)
                    .withStartAction {
                        binding.btnLogin.animate().translationY(-1500f).setDuration(2000)
                            .withStartAction {
                                binding.imgLogoLogin.animate().rotation(360f).setDuration(2000)
                                    .start()
                                binding.txtRegister.animate().alpha(0f).translationY(-1500f)
                                    .setDuration(2000).start()
                            }
                    }
            }
        }
        eventos()
        return binding.root

    }

    fun eventos() {

        binding.txtUser.Requerido(binding.txtSUser)
        binding.txtPassword.Requerido(binding.txtSPassword)

        binding.txtRegister.setOnClickListener {
            Toast.makeText(requireContext(), "Click", Toast.LENGTH_LONG).show()
            findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
        }
        binding.btnLogin.setOnClickListener {
            if (binding.txtUser.IsCorrect(binding.txtSUser) && binding.txtPassword.IsCorrect(binding.txtSPassword)) {


                CallServicesJD.startQuery("api/auth", CallServicesJD.Companion.method.POST,
                    (Login().apply {
                        User = binding.txtUser.TextJD
                        Password = binding.txtPassword.TextJD
                    }).toJson(),
                    object : ServicesJD {
                        override fun Finish(response: String, status: Int) {
                            Log.e("TAG", "Finish ($status): $response")
                            this@LoginFragment.requireActivity().runOnUiThread {
                                if (status == HTTP.OK) {
                                    if (response == "null") {
                                        requireContext().AlertOK(
                                            "User or password incorrect",
                                            "to introduce the creditial correct"
                                        )
                                        binding.txtRegister.animate().alpha(1f).setDuration(2000)
                                            .start()
                                    } else if (status == HTTP.OK) {

                                        startActivity(
                                            Intent(
                                                requireContext(),
                                                MainActivity::class.java
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        override fun Error(response: String, status: Int) {
                            Log.e("TAG", "Error ($status): $response")
                            this@LoginFragment.requireActivity().runOnUiThread {
                                if (status == HTTP.UnAuth) {
                                    var User: User =
                                        JSONObject(response).toClass(User::class.java.name).Cast()
                                    requireContext().AlertOK(
                                        "User lock",
                                        "This User ${User.Nome} is lock"
                                    )
                                } else if (status == HTTP.BadRequest) {
                                    requireContext().AlertOK(
                                        "User not exists",
                                        "User not exists, please to register"
                                    )
                                    binding.txtRegister.animate().alpha(1f).setDuration(2000)
                                        .start()
                                }
                            }
                        }
                    }
                )
            } else {
                requireContext().AlertOK("all field required", "you will to fill all fieldJua")
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
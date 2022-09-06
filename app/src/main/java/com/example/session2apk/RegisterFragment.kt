package com.example.session2apk

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.session2apk.Helper.EqualsTo
import com.example.session2apk.Helper.IsEmail
import com.example.session2apk.Helper.Requerido
import com.example.session2apk.databinding.FragmentRegisterBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        binding.txtUser.Requerido(binding.txtSUser,3)
        binding.txtSurName.Requerido(binding.txtSSurName,3)
        binding.txtpass.Requerido(binding.txtSPass,3)
        binding.txtCPass.EqualsTo(binding.txtSCPass, binding.txtCPass)
        binding.txtEmail.IsEmail(binding.txtSEmail)
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
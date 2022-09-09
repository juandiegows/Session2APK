package com.example.session2apk.ui.about

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.view.animation.TranslateAnimation
import com.example.session2apk.R
import com.example.session2apk.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {

    companion object {
        fun newInstance() = AboutFragment()
    }

    var animado = true
    private lateinit var viewModel: AboutViewModel
    lateinit var binding: FragmentAboutBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAboutBinding.inflate(layoutInflater)
        binding.imgLogoT.setOnClickListener {
            stopAnimation()

        }
        binding.imgFifaAbout.setOnClickListener {
            stopAnimation()
        }
        Animar()
        return binding.root
    }

    private fun stopAnimation() {
        if (!animado) {
            binding.imgFifaAbout.animate().cancel()
            binding.imgLogoT.animate().cancel()
        }
        animado = !animado
        Animar()
    }

    private fun Animar() {
        if (animado)
            binding.imgFifaAbout.animate().translationX(20f).setDuration(1000).withStartAction {
                binding.imgLogoT.animate().translationX(-20f).setDuration(1000).withEndAction() {
                    binding.imgLogoT.animate().translationX(20f).setDuration(1000).withEndAction() {

                    }
                }
            }.withEndAction {
                binding.imgFifaAbout.animate().translationX(-20f).setDuration(1000).withEndAction {

                    Animar()
                }
            }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(AboutViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
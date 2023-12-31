package com.lans.recipein_mobile.presentation.profile.profile_signout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.lans.recipein_mobile.R
import com.lans.recipein_mobile.databinding.FragmentProfileSignoutBinding
import com.lans.recipein_mobile.presentation.home.HomeFragmentDirections
import com.lans.recipein_mobile.utils.safeNavigate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileSignOutFragment : Fragment(), OnClickListener {
    private lateinit var binding: FragmentProfileSignoutBinding
    private val viewModel: ProfileSignOutViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentProfileSignoutBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeComponent()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.btnSignIn -> {
                findNavController().popBackStack()
            }
        }
    }

    private fun initializeComponent() {
        binding.btnSignIn.setOnClickListener(this)
    }
}
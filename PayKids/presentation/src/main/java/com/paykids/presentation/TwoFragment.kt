package com.paykids.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.paykids.navigation.FragmentNavigator
import com.paykids.navigation.Screen
import com.paykids.presentation.databinding.FragmentTwoBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TwoFragment : Fragment() {
    private var _binding: FragmentTwoBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTwoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnMove.setOnClickListener {
            (activity as? FragmentNavigator)?.navigateTo(Screen.ONE)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
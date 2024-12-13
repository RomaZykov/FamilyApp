package com.example.familyapp.ui.fragment.parent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.example.familyapp.databinding.FragmentSuccessTasksAddedBinding

class SuccessTasksAddedFragment : Fragment() {

    private var _binding: FragmentSuccessTasksAddedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSuccessTasksAddedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.taskAddedButton.setOnClickListener {
            parentFragmentManager.setFragmentResult(
                "tasksAddedRequestKey",
                bundleOf("buttonPressedKey" to true)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
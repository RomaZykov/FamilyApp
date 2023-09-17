package com.n1.moguchi.ui.fragments.parent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentDeleteParentBinding

class DeleteParentFragment : Fragment() {
    private lateinit var binding: FragmentDeleteParentBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDeleteParentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val currentFragmentInContainer =
            requireParentFragment().childFragmentManager.findFragmentByTag("DeleteParentFragment")
        if (currentFragmentInContainer != null && currentFragmentInContainer.isVisible) {
            val topTitle = requireParentFragment().view?.findViewById<TextView>(R.id.edit_top_title_tv)
            topTitle?.text = "Удаление аккаунта"
          val bottomLinearLayout = requireParentFragment().view?.findViewById<LinearLayout>(R.id.bottom_edit_ll)
            bottomLinearLayout?.visibility = View.GONE
        }

        binding.cancelButton.setOnClickListener {
            TODO()
        }
    }
}
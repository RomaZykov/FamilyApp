package com.n1.moguchi.ui.fragments.child

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentChildHomeBinding
import com.n1.moguchi.ui.activity.MainActivity

class HomeChildFragment : Fragment() {

    private lateinit var binding: FragmentChildHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChildHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        MainActivity.isParentProfile = false

        updateBottomNavigationView()

        (activity as MainActivity).findViewById<BottomNavigationView>(R.id.bottom_navigation_view).menu.findItem(R.id.switchToParent).setOnMenuItemClickListener {

            true
        }
    }

//    private fun showToParentChangeBottomSheet() {
//        val fragmentManager = supportFragmentManager
//        val modalBottomSheet = PrimaryBottomSheetFragment()
//        val mainActivityTag = MainActivity.TAG
//        fragmentManager.setFragmentResult("requestKey", bundleOf("bundleKey" to mainActivityTag))
//        modalBottomSheet.show(fragmentManager, MainActivity.TAG)
//    }

    private fun updateBottomNavigationView() {
        (activity as MainActivity).setupBottomNavigationView()
    }
}
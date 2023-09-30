package com.n1.moguchi.ui.fragments.parent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.n1.moguchi.R
import com.n1.moguchi.databinding.FragmentParentProfileBinding
import com.n1.moguchi.ui.ViewModelFactory
import com.n1.moguchi.ui.adapters.NotificationsRecyclerAdapter
import com.n1.moguchi.ui.viewmodels.HomeViewModel
import javax.inject.Inject

class ParentProfileFragment : Fragment() {

    private var _binding: FragmentParentProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var notificationsRecyclerAdapter: NotificationsRecyclerAdapter

    private val notificationList = mutableListOf(
        "Максимка достиг цели «Велосипед»",
        "Алёна выполнила задачу «Сходить в магазин в пятницу»",
        "Истёк срок повторения задачи «Написать контрольную минимум на 4» для Алёны",
        "Алёна достигла цели «Поход в кино с друзьями»",
        "Максимка достиг цели «Велосипед»"
    )

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentParentProfileBinding.inflate(layoutInflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navHostFragment =
            requireParentFragment().parentFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController

        val topAppBar = requireActivity().findViewById<Toolbar>(R.id.parent_profile_app_bar)
        topAppBar.setNavigationOnClickListener {
            navController.navigate(R.id.action_parentProfileFragment_to_parentHomeFragment)
        }

        binding.signOutButton.setOnClickListener {
            showBottomSheet(ParentLogOutBottomSheetFragment())
        }

        binding.profileCard.editProfileButton.setOnClickListener {
            showBottomSheet(ParentEditProfileBottomSheetFragment())
        }

        binding.myChildrenButton.setOnClickListener {
            navController.navigate(R.id.action_parentProfileFragment_to_addChildFragment)
        }

        binding.deleteNotificationsButton.setOnClickListener {
            notificationList.clear()
            notificationsRecyclerAdapter.notifyItemRangeChanged(0, notificationList.size - 1)
        }

        binding.notificationsSwitch.isChecked = false
        binding.notificationsSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            setupRecyclerView(view, notificationList)
            if (isChecked) {
                if (notificationsRecyclerAdapter.itemCount != 0) {
                    binding.deleteNotificationsButton.visibility = View.VISIBLE
                    binding.emptyNotificationsHint.visibility = View.GONE
                }
                binding.rvNotificationsList.visibility = View.VISIBLE
            } else {
                binding.emptyNotificationsHint.visibility = View.VISIBLE
                binding.rvNotificationsList.visibility = View.GONE
                binding.deleteNotificationsButton.visibility = View.GONE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView(view: View, notificationList: MutableList<String>) {
        val recyclerViewNotifications: RecyclerView = view.findViewById(R.id.rv_notifications_list)
        recyclerViewNotifications.layoutManager = LinearLayoutManager(requireContext())
        notificationsRecyclerAdapter = NotificationsRecyclerAdapter(notificationList)
        recyclerViewNotifications.adapter = notificationsRecyclerAdapter
    }

    private fun showBottomSheet(fragment: Fragment) {
        val fragmentManager = requireParentFragment().childFragmentManager
        val modalBottomSheet = fragment as BottomSheetDialogFragment
        modalBottomSheet.show(fragmentManager, modalBottomSheet.tag)
    }
}
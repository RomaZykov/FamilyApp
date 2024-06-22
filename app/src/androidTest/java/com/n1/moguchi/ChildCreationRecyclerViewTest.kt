package com.n1.moguchi

import android.os.Bundle
import android.os.SystemClock
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.fragment.app.testing.withFragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isFocusable
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.n1.moguchi.core.RecyclerViewMatcher
import com.n1.moguchi.data.remote.model.Child
import com.n1.moguchi.ui.activity.MainActivity
import com.n1.moguchi.ui.fragment.parent.child_creation.ChildCreationFragment
import com.n1.moguchi.ui.fragment.parent.child_creation.ChildrenCreationRecyclerAdapter
import com.n1.moguchi.ui.fragment.parent.goal_creation.GoalCreationFragment
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ChildCreationRecyclerViewTest {

    private lateinit var fragmentScenario: FragmentScenario<ChildCreationFragment>

    @get:Rule
    val activityTestRule = ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun setUp() {
        val requireArgs = Bundle().apply {
            putBoolean("deleteChildOptionEnable", true)
            putBoolean("isFromParentProfile", true)
            putBoolean("isFromOnBoarding", true)
            putBoolean("isFromParentHome", false)
            putBoolean("deleteChildProfile", false)
        }
        fragmentScenario = launchFragmentInContainer(
            fragmentArgs = requireArgs,
            themeResId = R.style.Base_Theme_Moguchi,
            initialState = Lifecycle.State.RESUMED,
            factory = null
        )
        fragmentScenario.withFragment {
            val recyclerView = requireActivity().findViewById<RecyclerView>(R.id.rv_children_creation_list)
            val adapter = ChildrenCreationRecyclerAdapter(
                requireArgs.getBoolean("deleteChildOptionEnable"),
                requireArgs.getBoolean("isFromParentProfile"),
                requireArgs.getBoolean("isFromOnBoarding"),
                requireArgs.getBoolean("isFromParentHome")
            )
            recyclerView.adapter = adapter
            adapter.children.add(
                0,
                Child()
            )
            adapter.notifyItemInserted(0)
        }
    }

    @Test
    fun test_child_creation_first_empty_item_is_displayed_after_parent_on_boarding_steps() {
        onView(
            RecyclerViewMatcher(R.id.rv_children_creation_list).atPosition(
                0,
                R.id.material_child_creation_card
            )
        ).check(matches(isDisplayed()))
    }
}
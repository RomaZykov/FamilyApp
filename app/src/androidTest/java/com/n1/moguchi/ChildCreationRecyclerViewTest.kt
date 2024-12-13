package com.example.familyapp

import android.os.Bundle
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.fragment.app.testing.withFragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isChecked
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.isEnabled
import androidx.test.espresso.matcher.ViewMatchers.isNotEnabled
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.example.familyapp.core.RecyclerViewMatcher
import com.example.familyapp.data.remote.model.Child
import com.example.familyapp.ui.activity.MainActivity
import com.example.familyapp.ui.fragment.parent.child_creation.ChildCreationFragment
import com.example.familyapp.ui.fragment.parent.child_creation.ChildrenCreationRecyclerAdapter
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
            val recyclerView =
                requireActivity().findViewById<RecyclerView>(R.id.rv_children_creation_list)
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

        onView(
            RecyclerViewMatcher(R.id.rv_children_creation_list).atPosition(
                0,
                R.id.avatar_male_1
            )
        ).check(matches(isChecked()))

        onView(
            RecyclerViewMatcher(R.id.rv_children_creation_list).atPosition(
                1,
                R.id.add_child_button
            )
        ).check(matches(isNotEnabled()))
    }

    @Test
    fun test_child_creation_change_add_button_enable_status_after_performing_child_name() {
        val inputText = "Roman"
        onView(
            RecyclerViewMatcher(R.id.rv_children_creation_list).atPosition(
                0,
                R.id.child_name_edit_text
            )
        ).perform(typeText(inputText))

        onView(
            RecyclerViewMatcher(R.id.rv_children_creation_list).atPosition(
                0,
                R.id.avatar_female_2
            )
        ).perform(click())

        onView(
            RecyclerViewMatcher(R.id.rv_children_creation_list).atPosition(
                1,
                R.id.add_child_button
            )
        ).check(matches(isEnabled()))
    }
}
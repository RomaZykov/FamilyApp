package com.n1.moguchi

import androidx.annotation.UiThread
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.n1.moguchi.core.RecyclerViewMatcher
import com.n1.moguchi.ui.activity.MainActivity
import com.n1.moguchi.ui.fragment.parent.child_creation.ChildCreationFragment
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class AddChildRecyclerViewTest {

    @get:Rule
    val activityTestRule = ActivityScenarioRule(MainActivity::class.java)
    private lateinit var fragmentScenario: FragmentScenario<ChildCreationFragment>

    @Before
    fun setUp() {
        fragmentScenario = launchFragmentInContainer(
            fragmentArgs = null,
            themeResId = R.style.Base_Theme_Moguchi,
            initialState = Lifecycle.State.RESUMED,
            factory = null
        )
    }

    @Test
    @UiThread
    fun test_recycler_view_first_item_is_displayed() {
        onView(
            RecyclerViewMatcher(R.id.rv_children_creation_list).atPosition(
                0,
                R.id.child_material_card
            )
        )
            .check(matches(isDisplayed()))
    }
}
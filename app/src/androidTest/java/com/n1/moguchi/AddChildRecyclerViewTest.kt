package com.n1.moguchi

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import com.n1.moguchi.core.RecyclerViewMatcher
import com.n1.moguchi.ui.fragment.parent.children_creation.AddChildFragment
import org.junit.Before
import org.junit.Test

class AddChildRecyclerViewTest {

    private lateinit var scenario: FragmentScenario<AddChildFragment>

    @Before
    fun setUp() {
        scenario = launchFragmentInContainer()
    }

    @Test
    fun test_recycler_view_add_child() {
        scenario.onFragment {
            onView(
                RecyclerViewMatcher(R.id.rv_children_creation_list).atPosition(
                    0,
                    R.id.child_material_card
                )
            )
                .check(matches(isDisplayed()))
        }
    }
}
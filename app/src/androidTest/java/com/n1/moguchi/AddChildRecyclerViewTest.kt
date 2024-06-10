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
import com.n1.moguchi.presentation.activity.MainActivity
import com.n1.moguchi.presentation.fragment.tasks.TasksFragment
import org.junit.Rule
import org.junit.Test

class TasksRecyclerViewTest {

    @get:Rule
    val activityTestRule = ActivityScenarioRule(MainActivity::class.java)
    private lateinit var fragmentScenario: FragmentScenario<TasksFragment>

    @Test
    @UiThread
    fun test_recycler_view_first_item_is_displayed() {
        fragmentScenario = launchFragmentInContainer(
            fragmentArgs = null,
            themeResId = R.style.Base_Theme_Moguchi,
            initialState = Lifecycle.State.RESUMED,
            factory = null
        )

        onView(
            RecyclerViewMatcher(R.id.rv_tasks_list).atPosition(
                0,
                R.layout.editable_task_item
            )
        ).check(matches(isDisplayed()))
    }
}
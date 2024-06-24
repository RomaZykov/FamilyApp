package com.n1.moguchi.presentation.fragment.parent.task_creation

import com.n1.moguchi.InstantTaskExecutorRuleForJUnit5
import com.n1.moguchi.data.FakeGoalRepository
import com.n1.moguchi.data.FakeTaskRepository
import com.n1.moguchi.data.remote.model.Goal
import com.n1.moguchi.data.remote.model.Task
import com.n1.moguchi.getOrAwaitValue
import com.n1.moguchi.ui.fragment.parent.task_creation.TaskCreationViewModel
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorRuleForJUnit5::class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class TaskCreationViewModelTest {

    private lateinit var viewModel: TaskCreationViewModel
    private lateinit var taskRepository: FakeTaskRepository
    private lateinit var goalRepository: FakeGoalRepository

    @BeforeEach
    fun setUp() {
        taskRepository = FakeTaskRepository()
        goalRepository = FakeGoalRepository()
        val goalsList = listOf(
            Goal(
                goalId = "1",
                title = "Поездка",
                currentPoints = 5,
                totalPoints = 10,
                goalCompleted = false
            )
        )
        goalRepository.addGoals(goalsList)

        viewModel = TaskCreationViewModel(goalRepository, taskRepository)
    }

    @Test
    fun test_initial_tasks_list_empty() {
        val initialTasks = viewModel.tasks.value

        assertEquals(emptyList<Task>(), initialTasks)
    }
}
package com.n1.moguchi

import com.n1.moguchi.data.FakeAppRepository
import com.n1.moguchi.data.FakeGoalRepository
import com.n1.moguchi.data.FakeTaskRepository
import com.n1.moguchi.data.models.remote.Task
import com.n1.moguchi.ui.fragment.tasks.TasksViewModel
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class TasksViewModelTest {

    private lateinit var viewModel: TasksViewModel
    private lateinit var taskRepository: FakeTaskRepository
    private lateinit var goalRepository: FakeGoalRepository
    private lateinit var appRepository: FakeAppRepository

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        taskRepository = FakeTaskRepository()
        val tasksList = mutableListOf(
            Task(taskId = "1", title = "A", height = 1),
            Task(taskId = "2", title = "B", height = 2),
            Task(taskId = "3", title = "C", height = 3),
            Task(taskId = "4", title = "D", height = 1, taskCompleted = true),
            Task(taskId = "5", title = "E", height = 3, taskCompleted = true),
        )
        taskRepository.addTasks(tasksList)

        goalRepository = FakeGoalRepository()
        appRepository = FakeAppRepository()
        viewModel = TasksViewModel(taskRepository, goalRepository, appRepository)
    }

    @Test
    fun test_active_task_change_status_to_completed_task() = runTest {
        val task = taskRepository.getActiveTask()
        taskRepository.updateTask(task)
        val expected = taskRepository.fetchCompletedTasks("")

        viewModel.updateTaskStatus(task, task.taskCompleted)

        advanceUntilIdle()

        assertEquals(expected, viewModel.completedTasks)
    }

    @Test
    fun test_change_task_to_check_status() {

    }
}

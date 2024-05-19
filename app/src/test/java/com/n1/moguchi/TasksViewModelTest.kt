package com.n1.moguchi

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.n1.moguchi.data.FakeAppRepository
import com.n1.moguchi.data.FakeGoalRepository
import com.n1.moguchi.data.FakeTaskRepository
import com.n1.moguchi.data.models.remote.Task
import com.n1.moguchi.ui.fragment.tasks.TasksViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
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

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setup() {
        taskRepository = FakeTaskRepository()
        val tasksList = mutableListOf(
            Task(taskId = "1", title = "A", height = 1, taskCompleted = false),
            Task(taskId = "2", title = "B", height = 2, taskCompleted = false),
            Task(taskId = "3", title = "C", height = 3, taskCompleted = false),
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
        val completedTasks = mutableListOf<Task>()
        val activeTasks = mutableListOf<Task>()
        viewModel.fetchCompletedTasks("")
        viewModel.fetchActiveTasks("")
        with(taskRepository) {
            updateTask(task)
            fetchCompletedTasks("").collect {
                completedTasks.addAll(it)
            }
            fetchActiveTasks("").collect {
                activeTasks.addAll(it)
            }
        }

        viewModel.updateTaskStatus(task, task.taskCompleted)

        val completedTasksVm = viewModel.completedTasks.getOrAwaitValue()
        val activeTasksVm = viewModel.activeTasks.getOrAwaitValue()
        assertEquals(completedTasks.size, completedTasksVm.size)
        assertEquals(activeTasksVm.size, activeTasksVm.size)
    }
}
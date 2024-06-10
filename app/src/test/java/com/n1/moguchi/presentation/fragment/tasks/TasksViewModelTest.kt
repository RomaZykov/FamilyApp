package com.n1.moguchi.presentation.fragment.tasks

import com.n1.moguchi.InstantTaskExecutorRuleForJUnit5
import com.n1.moguchi.MainCoroutineRule
import com.n1.moguchi.data.FakeAppRepository
import com.n1.moguchi.data.FakeGoalRepository
import com.n1.moguchi.data.FakeTaskRepository
import com.n1.moguchi.data.remote.model.Task
import com.n1.moguchi.getOrAwaitValue
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorRuleForJUnit5::class)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class TasksViewModelTest {

    private lateinit var viewModel: TasksViewModel
    private lateinit var taskRepository: FakeTaskRepository
    private lateinit var goalRepository: FakeGoalRepository
    private lateinit var appRepository: FakeAppRepository

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @BeforeEach
    fun setUp() {
        taskRepository = FakeTaskRepository()
        val tasksList = mutableListOf(
            Task(taskId = "1", title = "A", height = 1, taskCompleted = false),
            Task(taskId = "2", title = "B", height = 2, taskCompleted = false),
            Task(taskId = "3", title = "C", height = 3, taskCompleted = false),
            Task(taskId = "4", title = "D", height = 1, taskCompleted = true),
            Task(taskId = "5", title = "E", height = 3, taskCompleted = true)
        )
        taskRepository.addTasks(tasksList)

        goalRepository = FakeGoalRepository()
        appRepository = FakeAppRepository()
        viewModel = TasksViewModel(
            mainCoroutineRule.testDispatcher,
            taskRepository,
            goalRepository,
            appRepository
        )
    }

    @Test
    fun test_active_task_change_status_to_completed_task() = runTest {
        val task = taskRepository.getActiveTask()
        viewModel.fetchActiveTasks("")
        val activeTasksExpected = listOf(
            Task(taskId = "2", title = "B", height = 2, taskCompleted = false),
            Task(taskId = "3", title = "C", height = 3, taskCompleted = false),
        )
        viewModel.fetchCompletedTasks("")
        val completedTasksExpected = listOf(
            Task(taskId = "4", title = "D", height = 1, taskCompleted = true),
            Task(taskId = "5", title = "E", height = 3, taskCompleted = true),
            Task(taskId = "1", title = "A", height = 1, taskCompleted = true)
        )

        viewModel.updateTaskStatus(task, isActiveTask = true)

        val activeTasksVm = viewModel.activeTasks.getOrAwaitValue()
        assertEquals(activeTasksExpected, activeTasksVm)
        val completedTasksVm = viewModel.completedTasks.getOrAwaitValue()
        assertEquals(completedTasksExpected, completedTasksVm)
    }

    @Test
    fun test_active_task_change_status_to_empty_completed_tasks() {
        taskRepository.clearTasks()
        taskRepository.addTasks(
            listOf(
                Task(
                    taskId = "1",
                    title = "A",
                    height = 1,
                    taskCompleted = false
                )
            )
        )
        viewModel.fetchActiveTasks("")
        val task = taskRepository.getActiveTask()
        viewModel.fetchCompletedTasks("")
        val completedExpected = listOf(
            Task(
                taskId = "1",
                title = "A",
                height = 1,
                taskCompleted = true
            ),
        )

        viewModel.updateTaskStatus(task, isActiveTask = true)

        val activeTasks = viewModel.activeTasks.getOrAwaitValue()
        assertEquals(emptyList<Task>(), activeTasks)
        val completedTasksVm = viewModel.completedTasks.getOrAwaitValue()
        assertEquals(completedExpected, completedTasksVm)
    }

    @Test
    fun test_delete_task() = runTest {
        val task = taskRepository.getActiveTask()
        viewModel.fetchActiveTasks("")

        viewModel.deleteTask("", task, isActiveTask = true)

        val result = viewModel.activeTasks.getOrAwaitValue()
        assertEquals(2, result.size)
    }
}
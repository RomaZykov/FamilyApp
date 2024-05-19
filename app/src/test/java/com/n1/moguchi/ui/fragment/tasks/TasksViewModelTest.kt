package com.n1.moguchi.ui.fragment.tasks

import com.n1.moguchi.InstantTaskExecutorRuleForJUnit5
import com.n1.moguchi.MainCoroutineRule
import com.n1.moguchi.data.FakeAppRepository
import com.n1.moguchi.data.FakeGoalRepository
import com.n1.moguchi.data.FakeTaskRepository
import com.n1.moguchi.data.models.remote.Task
import com.n1.moguchi.getOrAwaitValue
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(InstantTaskExecutorRuleForJUnit5::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TasksViewModelTest {

    private lateinit var viewModel: TasksViewModel
    private lateinit var taskRepository: FakeTaskRepository
    private lateinit var goalRepository: FakeGoalRepository
    private lateinit var appRepository: FakeAppRepository

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    @BeforeAll
    fun setUp() {
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
        viewModel = TasksViewModel(mainCoroutineRule.testDispatcher, taskRepository, goalRepository, appRepository)
    }

    @Test
    fun test_fetch_all_tasks_to_collect_active_and_completed_task() = runTest {
        val allTasks = mutableListOf<Task>()
        with(taskRepository) {
            fetchAllTasks("").collect {
                allTasks.addAll(it)
            }
        }

        viewModel.fetchAllTasks("")

        val result = viewModel.completedTasks.getOrAwaitValue()
        assertEquals(allTasks, result)
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

        viewModel.updateTaskStatus(task, !task.taskCompleted) // Active task flag

        val completedTasksVm = viewModel.completedTasks.getOrAwaitValue()
        val activeTasksVm = viewModel.activeTasks.getOrAwaitValue()
        assertEquals(completedTasks, completedTasksVm)
        assertEquals(activeTasks, activeTasksVm)
    }

    @Test
    fun deleteTask() {
    }
}
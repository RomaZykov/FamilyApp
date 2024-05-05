package com.n1.moguchi

import com.n1.moguchi.data.models.local.UserPreferences
import com.n1.moguchi.data.models.remote.Goal
import com.n1.moguchi.data.models.remote.ProfileMode
import com.n1.moguchi.data.models.remote.Task
import com.n1.moguchi.data.repositories.AppRepository
import com.n1.moguchi.data.repositories.GoalRepository
import com.n1.moguchi.data.repositories.TaskRepository
import com.n1.moguchi.ui.fragment.tasks.TasksViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class TasksViewModelTest {

    private lateinit var viewModel: TasksViewModel
    private lateinit var taskRepository: TaskRepository
    private lateinit var goalRepository: GoalRepository
    private lateinit var appRepository: AppRepository

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
//        taskRepository = FakeTaskRepository()
        goalRepository = FakeGoalRepository()
        appRepository = FakeAppRepository()
        viewModel = TasksViewModel(taskRepository, goalRepository, appRepository)
    }

    @Test
    fun test_active_task_change_status_to_completed_task() = runTest {
        val activeTasksList = mutableListOf(
            Task(taskId = "1", title = "A", height = 1),
            Task(taskId = "2", title = "B", height = 2),
            Task(taskId = "3", title = "C", height = 3),
        )
        val completedTasksList = mutableListOf(
            Task(taskId = "4", title = "D", height = 1, taskCompleted = true),
            Task(taskId = "5", title = "E", height = 3, taskCompleted = true),
        )

        viewModel.updateTaskStatus(activeTasksList[0], activeTasksList[0].taskCompleted)
        advanceUntilIdle()
        activeTasksList.remove(activeTasksList[0])
        completedTasksList.add(activeTasksList[0])

        assertEquals(activeTasksList, viewModel.activeTasks)
        assertEquals(completedTasksList, viewModel.completedTasks)
    }

    @Test
    fun test_completed_task_change_status_to_active_task() {

    }
}

class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher(),
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

private class FakeTaskRepository(items: List<Task>) : TaskRepository {

    private val tasks: MutableList<Task> = mutableListOf()

    init {
        tasks.addAll(items)
    }

    override fun fetchAllTasks(goalId: String): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun fetchActiveTasks(goalId: String): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun fetchCompletedTasks(goalId: String): Flow<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun createTask(goalId: String): Task {
        TODO("Not yet implemented")
    }

    override suspend fun updateTask(task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(goalId: String, task: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun saveTasksToDb(goalId: String, tasks: List<Task>) {
        TODO("Not yet implemented")
    }
}

private class FakeGoalRepository : GoalRepository {
    override fun fetchActiveGoals(childId: String): Flow<List<Goal>> {
        TODO("Not yet implemented")
    }

    override fun fetchCompletedGoals(childId: String): Flow<List<Goal>?> {
        TODO("Not yet implemented")
    }

    override suspend fun getGoal(goalId: String): Goal {
        TODO("Not yet implemented")
    }

    override fun returnCreatedGoal(title: String, totalPoints: Int, childId: String): Goal {
        TODO("Not yet implemented")
    }

    override suspend fun saveGoalWithTasksToDb(goal: Goal, tasks: List<Task>) {
        TODO("Not yet implemented")
    }

    override suspend fun updateGoalPoints(goalId: String, taskHeight: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun updateSecondaryGoalPoints(goalId: String, taskHeight: Int) {
        TODO("Not yet implemented")
    }

    override suspend fun updateGoalStatus(goalId: String) {
        TODO("Not yet implemented")
    }
}

private class FakeAppRepository : AppRepository {
    override fun getUserPrefs(): Flow<UserPreferences> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUserPrefs(newMode: ProfileMode, childId: String?) {
        TODO("Not yet implemented")
    }

    override suspend fun sendChildPasswordNotificationEmail(childId: String) {
        TODO("Not yet implemented")
    }
}
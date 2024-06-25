package com.n1.moguchi.ui.fragment.tasks

import com.n1.moguchi.InstantTaskExecutorRuleForJUnit5
import com.n1.moguchi.MainCoroutineRule
import com.n1.moguchi.data.FakeAppRepository
import com.n1.moguchi.data.FakeGoalRepository
import com.n1.moguchi.data.FakeTaskRepository
import com.n1.moguchi.data.remote.model.Goal
import com.n1.moguchi.data.remote.model.Task
import com.n1.moguchi.getOrAwaitValue
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertAll
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
        val tasksList = listOf(
            Task(
                taskId = "1",
                title = "A",
                height = 1,
                goalOwnerId = "",
                onCheck = false,
                taskCompleted = false
            ),
            Task(
                taskId = "2",
                title = "B",
                height = 2,
                goalOwnerId = "",
                onCheck = false,
                taskCompleted = false
            ),
            Task(
                taskId = "3",
                title = "C",
                height = 3,
                goalOwnerId = "",
                onCheck = false,
                taskCompleted = false
            ),
            Task(
                taskId = "4",
                title = "D",
                height = 1,
                goalOwnerId = "",
                onCheck = false,
                taskCompleted = true
            ),
            Task(
                taskId = "5",
                title = "E",
                height = 3,
                goalOwnerId = "",
                onCheck = false,
                taskCompleted = true
            )
        )
        taskRepository.addTasks(tasksList)

        goalRepository = FakeGoalRepository()
        val goalsList = listOf(
            Goal(
                goalId = "1",
                title = "Поездка",
                currentPoints = 5,
                totalPoints = 10,
                goalCompleted = false
            ),
            Goal(
                goalId = "2",
                title = "Покупка компьютера",
                currentPoints = 9,
                totalPoints = 10,
                goalCompleted = false
            ),
            Goal(
                goalId = "3",
                title = "Велосипед",
                currentPoints = 10,
                totalPoints = 10,
                goalCompleted = true
            ),
            Goal(
                goalId = "4",
                title = "Пиво",
                currentPoints = 1,
                totalPoints = 1,
                goalCompleted = true
            )
        )
        goalRepository.addGoals(goalsList)

        appRepository = FakeAppRepository()
        viewModel = TasksViewModel(
            mainCoroutineRule.testDispatcher,
            taskRepository,
            goalRepository,
            appRepository
        )
    }

    @AfterEach
    fun tearDownEach() {
        taskRepository.clearTasks()
    }

    @Test
    fun test_goal_details_fetching() = runTest {
        val goal = goalRepository.getGoal("1")

        viewModel.setupRelatedGoalDetails(goal.goalId!!)

        val resultTotalGoalPoints = viewModel.totalGoalPoints.getOrAwaitValue()
        val resultCurrentGoalPoints = viewModel.currentGoalPoints.getOrAwaitValue()
        val resultGoalTitle = viewModel.goalTitle.getOrAwaitValue()
        assertAll(
            "Should return all goal details",
            { assertEquals(goal.totalPoints, resultTotalGoalPoints) },
            { assertEquals(goal.currentPoints, resultCurrentGoalPoints) },
            { assertEquals(goal.title, resultGoalTitle) }
        )
    }

    @Test
    fun test_all_task_fetched() = runTest {
        viewModel.fetchAllTasks("")

        val result = viewModel.completedTasks.getOrAwaitValue()
        assertEquals(
            listOf(
                Task(
                    taskId = "1", title = "A", height = 1, onCheck = false,
                    goalOwnerId = "", taskCompleted = false
                ),
                Task(
                    taskId = "2", title = "B", height = 2, onCheck = false,
                    goalOwnerId = "", taskCompleted = false
                ),
                Task(
                    taskId = "3", title = "C", height = 3, onCheck = false,
                    goalOwnerId = "", taskCompleted = false
                ),
                Task(
                    taskId = "4", title = "D", height = 1, onCheck = false,
                    goalOwnerId = "", taskCompleted = true
                ),
                Task(
                    taskId = "5", title = "E", height = 3, onCheck = false,
                    goalOwnerId = "", taskCompleted = true
                )
            ), result
        )
    }

    @Test
    fun test_active_task_change_status_to_completed_task() = runTest {
        val task = taskRepository.getActiveTask()
        viewModel.fetchActiveTasks("")
        val activeTasksExpected = listOf(
            Task(
                taskId = "2",
                title = "B",
                height = 2,
                goalOwnerId = "",
                onCheck = false,
                taskCompleted = false
            ),
            Task(
                taskId = "3",
                title = "C",
                height = 3,
                goalOwnerId = "",
                onCheck = false,
                taskCompleted = false
            ),
        )
        viewModel.fetchCompletedTasks("")
        val completedTasksExpected = listOf(
            Task(
                taskId = "4",
                title = "D",
                height = 1,
                goalOwnerId = "",
                onCheck = false,
                taskCompleted = true
            ),
            Task(
                taskId = "5",
                title = "E",
                height = 3,
                goalOwnerId = "",
                onCheck = false,
                taskCompleted = true
            ),
            Task(
                taskId = "1",
                title = "A",
                height = 1,
                goalOwnerId = "",
                onCheck = false,
                taskCompleted = true
            )
        )

        viewModel.updateTaskStatus(task)

        val activeTasks = viewModel.activeTasks.getOrAwaitValue()
        assertEquals(activeTasksExpected, activeTasks)
        val completedTasks = viewModel.completedTasks.getOrAwaitValue()
        assertEquals(completedTasksExpected, completedTasks)
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
                    onCheck = false,
                    goalOwnerId = "",
                    taskCompleted = false
                )
            )
        )
        viewModel.fetchActiveTasks("")
        val task = taskRepository.getActiveTask()
        viewModel.fetchCompletedTasks("")
        val completedTasksExpected = listOf(
            Task(
                taskId = "1",
                title = "A",
                height = 1,
                onCheck = false,
                goalOwnerId = "",
                taskCompleted = true
            ),
        )

        viewModel.updateTaskStatus(task)

        val activeTasks = viewModel.activeTasks.getOrAwaitValue()
        assertEquals(emptyList<Task>(), activeTasks)
        val completedTasks = viewModel.completedTasks.getOrAwaitValue()
        assertEquals(completedTasksExpected, completedTasks)
    }

    @Test
    fun test_delete_task() = runTest {
        val task = taskRepository.getActiveTask()

        viewModel.fetchActiveTasks("")

        viewModel.deleteTask("", task)

        val result = viewModel.activeTasks.getOrAwaitValue()
        assertEquals(2, result.size)
    }

    @Test
    fun test_on_check_task_status_for_active_task_updated() = runTest {
        val task = taskRepository.getActiveTask()
        viewModel.fetchActiveTasks("")

        viewModel.updateTaskCheckStatus(task)

        val result = viewModel.activeTasks.getOrAwaitValue().find { it.taskId == task.taskId }
        assertEquals(
            Task(
                taskId = "1",
                title = "A",
                height = 1,
                taskCompleted = false,
                goalOwnerId = "",
                onCheck = true
            ), result
        )
    }
}
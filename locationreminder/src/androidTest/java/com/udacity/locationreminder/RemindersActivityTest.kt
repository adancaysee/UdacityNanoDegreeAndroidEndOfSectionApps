package com.udacity.locationreminder

import android.app.Application
import androidx.room.Room
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.udacity.locationreminder.data.domain.Reminder
import com.udacity.locationreminder.data.source.FakeReminderRepository
import com.udacity.locationreminder.data.source.local.RemindersDatabase
import com.udacity.locationreminder.data.source.repository.ReminderRepository
import com.udacity.locationreminder.domain.MainActivity
import com.udacity.locationreminder.domain.reminderlist.ReminderListViewModel
import com.udacity.locationreminder.domain.savereminder.SaveReminderViewModel
import com.udacity.locationreminder.util.DataBindingIdlingResource
import com.udacity.locationreminder.util.EspressoIdlingResource
import com.udacity.locationreminder.util.getOrAwaitValue
import com.udacity.locationreminder.util.monitorActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@LargeTest
class RemindersActivityTest : AutoCloseKoinTest() {

    private lateinit var reminderRepository: ReminderRepository
    private lateinit var appContext: Application
    private lateinit var saveReminderViewModel:SaveReminderViewModel

    private val dataBindingIdlingResource = DataBindingIdlingResource()

    @Before
    fun init() {
        stopKoin()
        appContext = getApplicationContext()
        val myModule = module {
            viewModel {
                ReminderListViewModel(
                    appContext,
                    get()
                )
            }
            single {
                SaveReminderViewModel(
                    appContext,
                    get()
                )
            }
            single<ReminderRepository> { FakeReminderRepository() }
            single {
                Room.databaseBuilder(
                    androidContext(),
                    RemindersDatabase::class.java,
                    "reminders-database"
                ).build()
            }
            single {
                get<RemindersDatabase>().remindersDao
            }
        }

        startKoin {
            modules(listOf(myModule))
        }
        //Get our real repository
        reminderRepository = get()

        saveReminderViewModel = get()

        runBlocking {
            reminderRepository.deleteAllReminders()
        }
    }

    @Before
    fun registerIdlingResource() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().register(dataBindingIdlingResource)
    }

    @After
    fun unregisterIdlingResource() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
        IdlingRegistry.getInstance().unregister(dataBindingIdlingResource)
    }

    @Test
    fun saveReminder_emptyLocation_emitError() = runTest {
        //GIVEN -- Location null
        val reminder = Reminder("TITLE1", "DESCRIPTION")
        reminderRepository.saveReminder(reminder)
        val activityScenario = ActivityScenario.launch(MainActivity::class.java)
        dataBindingIdlingResource.monitorActivity(activityScenario)

        Espresso.onView(withId(R.id.add_reminder_fab)).perform(ViewActions.click())
        Espresso.onView(withId(R.id.reminderTitle))
            .perform(ViewActions.typeText("TITLE1"))
        Espresso.onView(withId(R.id.reminderDescription)).perform(
            ViewActions.typeText("DESCRIPTION"),
            ViewActions.closeSoftKeyboard()
        )

        //WHEN - perform save reminder
        Espresso.onView(withId(R.id.saveReminder)).perform(ViewActions.click())

        //Show an error message with snackbar event
        withContext(Dispatchers.Main) {
            val value = saveReminderViewModel.showSnackBarIntEvent.getOrAwaitValue() ==  R.string.err_select_location
            assertTrue(value)
        }

    }
}

package com.udacity.locationreminder.domain.savereminder

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.udacity.locationreminder.R
import com.udacity.locationreminder.data.source.FakeReminderRepository
import com.udacity.locationreminder.data.source.repository.ReminderRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class SaveReminderFragmentTest : AutoCloseKoinTest() {
    private lateinit var fakeReminderRepository: ReminderRepository

    private lateinit var appContext: Application

    @Before
    fun setUp() {
        stopKoin()
        appContext = ApplicationProvider.getApplicationContext()

        val myModule = module {
            single<ReminderRepository> { FakeReminderRepository() }
            viewModel {
                SaveReminderViewModel(
                    appContext,
                    get()
                )
            }
        }
        startKoin {
            modules(listOf(myModule))
        }
        fakeReminderRepository = get()

        runBlocking {
            fakeReminderRepository.deleteAllReminders()
        }
    }

    //Test the displayed data on the UI.
    @Test
    fun inputs_displayedInUI() = runTest {
        val scenario = launchFragmentInContainer<SaveReminderFragment>(
            Bundle(),
            R.style.Theme_LocationReminder
        )

        val mockNavController = Mockito.mock(NavController::class.java)

        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, mockNavController)
        }

        Espresso.onView(withId(R.id.reminderTitle))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
        Espresso.onView(withId(R.id.reminderDescription))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

        Espresso.onView(withId(R.id.saveReminder))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))

    }
}
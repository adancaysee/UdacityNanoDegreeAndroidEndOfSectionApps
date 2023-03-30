package com.udacity.locationreminder.domain.reminderlist

import android.app.Application
import android.os.Bundle
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.udacity.locationreminder.R
import com.udacity.locationreminder.data.domain.Reminder
import com.udacity.locationreminder.data.source.FakeReminderRepository
import com.udacity.locationreminder.data.source.repository.ReminderRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.mockito.Mockito
import org.koin.test.get

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ReminderListFragmentTest : AutoCloseKoinTest() {

    private lateinit var fakeReminderRepository: ReminderRepository
    private lateinit var appContext: Application

    @Before
    fun setUp() {
        stopKoin()
        appContext = ApplicationProvider.getApplicationContext()
        
        val myModule = module {
            single<ReminderRepository> { FakeReminderRepository() }
            viewModel {
                ReminderListViewModel(
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

    @Test
    fun clickFirstItem_navigateToDetailFragment() = runTest {
        //GIVEN two task for list fragment
        val reminder1 = Reminder("TITLE1")
        val reminder2 = Reminder("TITLE2" )
        fakeReminderRepository.saveReminder(reminder1)
        fakeReminderRepository.saveReminder(reminder2)

        val scenario = launchFragmentInContainer<ReminderListFragment>(Bundle(), R.style.Theme_LocationReminder)

        val mockNavController = Mockito.mock(NavController::class.java)

        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, mockNavController)
        }

        //WHEN - Click on the first list item
        Espresso.onView(withId(R.id.reminders_recycler_view))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(
                    0,
                    ViewActions.click()
                )
            )

        //THEN - Navigated to detail for first item
        Mockito.verify(mockNavController)
            .navigate(ReminderListFragmentDirections.actionOpenDetail(reminder1.id))

    }
}
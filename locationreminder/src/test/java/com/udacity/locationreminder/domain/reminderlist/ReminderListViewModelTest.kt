package com.udacity.locationreminder.domain.reminderlist

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.udacity.locationreminder.base.NavigationCommand
import com.udacity.locationreminder.data.domain.Reminder
import com.udacity.locationreminder.data.source.FakeReminderRepository
import com.udacity.locationreminder.data.source.repository.ReminderRepository
import com.udacity.locationreminder.util.MainDispatcherRule
import com.udacity.locationreminder.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.AutoCloseKoinTest
import org.koin.test.get

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class ReminderListViewModelTest : AutoCloseKoinTest() {

    private lateinit var reminderListViewModel: ReminderListViewModel
    private lateinit var appContext: Application

    private lateinit var fakeReminderRepository: ReminderRepository

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        stopKoin()
        appContext = ApplicationProvider.getApplicationContext()

        val myModule = module {
            single<ReminderRepository> { FakeReminderRepository() }
        }
        startKoin {
            modules(listOf(myModule))
        }
        fakeReminderRepository = get()

        reminderListViewModel = ReminderListViewModel(appContext, get())

        runBlocking {
            fakeReminderRepository.deleteAllReminders()
        }
    }

    @Test
    fun setsNewReminderEvent_emitLiveData() {
        //When navigate for a new reminder
        reminderListViewModel.navigateToAddReminder(NavigationCommand.Back)

        val value = reminderListViewModel.navigationCommandEvent.getOrAwaitValue()

        //Then new reminder event is triggered
        assertThat(value).isNotNull()
    }

    @Test
    fun addNewReminder_emitNoDataLiveData() = runTest {
        //When add new reminder
        fakeReminderRepository.saveReminder(Reminder("test"))

        reminderListViewModel.reminders.getOrAwaitValue()

        //Then noData value is false
        assertThat(reminderListViewModel.showNoData.getOrAwaitValue()).isEqualTo(false)
    }


}
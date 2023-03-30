package com.udacity.locationreminder.domain.savereminder

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.udacity.locationreminder.R
import com.udacity.locationreminder.data.domain.Reminder
import com.udacity.locationreminder.data.source.FakeReminderRepository
import com.udacity.locationreminder.data.source.repository.ReminderRepository
import com.udacity.locationreminder.util.MainDispatcherRule
import com.udacity.locationreminder.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
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
class SaveReminderViewModelTest : AutoCloseKoinTest() {
    private lateinit var saveReminderViewModel: SaveReminderViewModel
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

        saveReminderViewModel = SaveReminderViewModel(appContext, get())

        runBlocking {
            fakeReminderRepository.deleteAllReminders()
        }
    }

    @Test
    fun saveLocation_emitLiveData() {
        //When navigate for a new reminder
        saveReminderViewModel.saveLocation()

        val value = saveReminderViewModel.navigationCommandEvent.getOrAwaitValue()

        //Then new navigationCommand event is triggered
        assertThat(value).isNotNull()
    }

    @Test
    fun validateEnteredData_emitError() {
        //Given no title
        val reminder = Reminder()

        //When call
        saveReminderViewModel.validateEnteredData(reminder)

        assertThat(saveReminderViewModel.showSnackBarIntEvent.value).isEqualTo(R.string.err_enter_title)
    }

    @Test
    fun clearData_resetCurrentAddress() {
        //When call
        saveReminderViewModel.clearData()

        assertThat(saveReminderViewModel.selectedAddress.value).isNull()
    }
}
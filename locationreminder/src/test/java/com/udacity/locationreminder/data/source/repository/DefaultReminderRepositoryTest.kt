package com.udacity.locationreminder.data.source.repository

import com.google.common.truth.Truth.assertThat
import com.udacity.locationreminder.data.domain.Reminder
import com.udacity.locationreminder.data.source.local.FakeRemindersDao
import com.udacity.locationreminder.data.source.local.RemindersDao
import com.udacity.locationreminder.util.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get

@ExperimentalCoroutinesApi
class DefaultReminderRepositoryTest : KoinTest {

    private lateinit var defaultReminderRepository: DefaultReminderRepository

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setUp() {
        stopKoin()

        val myModule = module {
            single<RemindersDao> { FakeRemindersDao() }
        }
        startKoin {
            modules(listOf(myModule))
        }
        defaultReminderRepository = DefaultReminderRepository(
            get(),
            mainDispatcherRule.testDispatcher
        )
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun saveReminder() = runTest {
        //Given
        val reminder1 = Reminder("title1")

        //WHEN
        defaultReminderRepository.saveReminder(reminder1)

        assertThat(defaultReminderRepository.getReminder(reminder1.id)).isNotNull()

    }
}
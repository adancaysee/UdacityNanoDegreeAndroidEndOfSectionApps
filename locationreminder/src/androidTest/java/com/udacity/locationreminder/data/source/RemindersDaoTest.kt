package com.udacity.locationreminder.data.source

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.udacity.locationreminder.data.domain.Reminder
import com.udacity.locationreminder.data.domain.asDatabase
import com.udacity.locationreminder.data.source.local.RemindersDatabase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class RemindersDaoTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var inMemoryDatabase: RemindersDatabase

    @Before
    fun setUp() {
        inMemoryDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            RemindersDatabase::class.java
        ).allowMainThreadQueries().build()
    }

    @After
    fun tearDown() {
        inMemoryDatabase.close()
    }

    @Test
    fun insertReminder_getById() = runTest {
        // GIVEN - Insert a reminder.
        val reminder = Reminder("test")
        inMemoryDatabase.remindersDao.saveReminder(reminder.asDatabase())

        //WHEN - Get the task by id from the database.
        val loadedReminder = inMemoryDatabase.remindersDao.getReminderById(reminder.id)

        assertThat(loadedReminder).isNotNull()
        assertThat(loadedReminder?.title).isEqualTo(reminder.title)
        assertThat(loadedReminder?.id).isEqualTo(reminder.id)

    }
}
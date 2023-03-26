package com.udacity.locationreminder

import androidx.room.Room
import com.udacity.locationreminder.data.source.DefaultReminderRepository
import com.udacity.locationreminder.data.source.ReminderRepository
import com.udacity.locationreminder.data.source.local.RemindersDatabase
import com.udacity.locationreminder.reminderdetail.ReminderDetailViewModel
import com.udacity.locationreminder.reminderlist.ReminderListViewModel
import com.udacity.locationreminder.savereminder.SaveReminderViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/**
 * single block --> object that inside that blocks are singletons
 */

val databaseKoinModule = module {
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

val reminderAppModule = module {

    includes(databaseKoinModule)

    /** Default Repository */
    single {
        DefaultReminderRepository(
            get()
        )
    }


    /** viewModels */
    viewModel {
        ReminderListViewModel(get(), (get() as ReminderRepository))
    }
    viewModel {
        ReminderDetailViewModel(get())
    }
    single {
        SaveReminderViewModel(get(), (get() as ReminderRepository))
    }
}
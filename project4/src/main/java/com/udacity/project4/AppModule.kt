package com.udacity.project4

import androidx.room.Room
import com.udacity.project4.data.source.local.RemindersDatabase
import com.udacity.project4.data.source.repository.DefaultReminderRepository
import com.udacity.project4.data.source.repository.ReminderRepository
import com.udacity.project4.domain.reminderdetail.ReminderDetailViewModel
import com.udacity.project4.domain.reminderlist.ReminderListViewModel
import com.udacity.project4.domain.savereminder.SaveReminderViewModel
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

    //Default Repository
    single<ReminderRepository> {
        DefaultReminderRepository(get())
    }


    // viewModels
    viewModel {
        ReminderListViewModel(get(), (get() as ReminderRepository))
    }
    viewModel {
        ReminderDetailViewModel(get(),get())
    }
    single {
        SaveReminderViewModel(get(), (get() as ReminderRepository))
    }
}
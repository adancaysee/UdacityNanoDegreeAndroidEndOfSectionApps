package com.udacity.politicalpreparedness

import androidx.room.Room
import com.udacity.politicalpreparedness.data.source.local.CivicsDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val databaseKoinModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            CivicsDatabase::class.java,
            "reminders-database"
        ).build()
    }
    single {
        get<CivicsDatabase>().electionsDao
    }
}

val appModule = module {
    includes(databaseKoinModule)

    //viewModels

}
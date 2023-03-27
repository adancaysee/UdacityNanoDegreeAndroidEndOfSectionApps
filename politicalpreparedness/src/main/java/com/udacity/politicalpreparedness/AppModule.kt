package com.udacity.politicalpreparedness

import androidx.room.Room
import com.udacity.politicalpreparedness.data.repository.ElectionRepository
import com.udacity.politicalpreparedness.data.repository.DefaultElectionRepository
import com.udacity.politicalpreparedness.data.source.local.CivicsDatabase
import com.udacity.politicalpreparedness.data.source.remote.*
import com.udacity.politicalpreparedness.elections.ElectionsViewModel
import com.udacity.politicalpreparedness.voterinfo.VoterInfoViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    //Database
    single {
        Room.databaseBuilder(
            androidContext(), CivicsDatabase::class.java, "politicalpreparedness-database"
        ).build()
    }
    //Dao
    single {
        get<CivicsDatabase>().electionsDao
    }

    //Retrofit api client
    single {
        getRetrofitClient().create(CivicsApi::class.java)
    }
    //Retrofit network data source
    single<CivicsNetworkDataSource> {
        CivicsRetrofitNetworkDataSource(get())
    }
    //Default repository
    single<ElectionRepository> {
        DefaultElectionRepository(get(), get())
    }
    //ViewModels
    viewModel {
        ElectionsViewModel(get())
    }
    viewModel {
        VoterInfoViewModel(get())
    }
}
